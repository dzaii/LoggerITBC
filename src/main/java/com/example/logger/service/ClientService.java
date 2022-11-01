package com.example.logger.service;

import com.example.logger.dto.ClientShowDto;
import com.example.logger.model.Client;
import com.example.logger.model.enums.ClientRole;
import com.example.logger.repository.ClientRepository;
import com.example.logger.repository.LogRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class ClientService {

    ClientRepository clientRepository;
    LogRepository logRepository;

    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    ClientService(ClientRepository clientRepository,LogRepository logRepository){
        this.clientRepository = clientRepository;
        this.logRepository = logRepository;
        this.bCryptPasswordEncoder = new BCryptPasswordEncoder(BCryptPasswordEncoder.BCryptVersion.$2A,12);
    }


    public void save(Client client){
        client.setRole(ClientRole.USER);
        String encodedPassword = bCryptPasswordEncoder.encode(client.getPassword());
        client.setPassword(encodedPassword);
        clientRepository.save(client);
    }
    public String login(String account, String password) {

        Optional<Client> client = clientRepository.getClientLogin(account);
        if (client.isPresent()) {
            if(bCryptPasswordEncoder.matches(password,client.get().getPassword())) {
                clientRepository.updateClientToken(UUID.randomUUID().toString(), client.get().getClientId());
                return clientRepository.getClientToken(client.get().getClientId());
            }
        }
            return "";

    }

    public List<ClientShowDto> allClients(){
        return clientRepository.findAll().stream().map(client -> { ClientShowDto clientShow =ClientShowDto.clientToClientShow(client);
                                                                    clientShow.setLogCount(logRepository.findByClient(client).size());
                                                                    return clientShow;}).collect(Collectors.toList());
    }

    public ClientRole getRoleFromToken(String token){
        if(clientRepository.findByMyToken(token).isPresent()) {
            return clientRepository.findByMyToken(token).get().getRole();
        }
        return null;
    }

    public void setRoleToAdmin(Client client){
        clientRepository.setAdminRole(client.getClientId());
    }

    public void setClientPassword(long id, String password){
        clientRepository.setClientPassword(id,bCryptPasswordEncoder.encode(password));
    }

    public boolean passValid(String password){
        return password.matches("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$");
    }
}
