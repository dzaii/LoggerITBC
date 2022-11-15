package com.example.logger.service;

import com.example.logger.dto.ClientLoginDto;
import com.example.logger.dto.ClientShowDto;
import com.example.logger.dto.LoginResponseDto;
import com.example.logger.model.Client;
import com.example.logger.model.enums.ClientRole;
import com.example.logger.repository.ClientRepository;
import com.example.logger.repository.LogRepository;
import com.example.logger.security.JWTGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.management.InvalidAttributeValueException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ClientService {

    ClientRepository clientRepository;
    LogRepository logRepository;
    PasswordEncoder passwordEncoder;
    AuthenticationManager authenticationManager;
    private JWTGenerator jwtGenerator;

    @Autowired
    ClientService(ClientRepository clientRepository, LogRepository logRepository, PasswordEncoder passwordEncoder,
                  AuthenticationManager authenticationManager, JWTGenerator jwtGenerator) {

        this.clientRepository = clientRepository;
        this.logRepository = logRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtGenerator = jwtGenerator;
    }


    public void save(Client client) {
        if (clientRepository.existsClientByUsername(client.getUsername())) {
            throw new RuntimeException("Username already exists.");
        }
        if (clientRepository.existsClientByEmail(client.getEmail())) {
            throw new RuntimeException("Email already exists.");
        }
        client.setRole(ClientRole.USER);
        String encodedPassword = passwordEncoder.encode(client.getPassword());
        client.setPassword(encodedPassword);
        clientRepository.save(client);
    }

    public LoginResponseDto login(ClientLoginDto clientLoginDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(clientLoginDto.getAccount(),
                        clientLoginDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtGenerator.generateToken(authentication);

        return new LoginResponseDto(token);

    }

    public List<ClientShowDto> allClients(Integer page, int size) throws InvalidAttributeValueException {
        if (page == null || page < 0) {
            throw new InvalidAttributeValueException();
        }
        return clientRepository.findAll(PageRequest.of(page, size)).stream().map(client -> {
            ClientShowDto clientShow = ClientShowDto.clientToClientShow(client);
            clientShow.setLogCount(logRepository.findByClient(client).size());
            return clientShow;
        }).collect(Collectors.toList());
    }

    public void setRoleToAdmin(Client client) {
        clientRepository.setAdminRole(client.getClientId());
    }

    public void setClientPassword(long id, String password) throws IllegalArgumentException {
        if (!clientRepository.existsClientByClientId(id)) {
            throw new IllegalArgumentException("Wrong client Id.");
        }
        if (!this.passValid(password)) {
            throw new IllegalArgumentException("Password must contain at least: 8 characters, one upper Case letter," +
                    " one lower case letter, one number and one special character.");
        }
        clientRepository.setClientPassword(id, passwordEncoder.encode(password));
    }

    public boolean passValid(String password) {
        return password.matches("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$");
    }
}
