package com.example.logger.service;

import com.example.logger.dto.ClientShowDto;
import com.example.logger.model.Client;
import com.example.logger.model.enums.ClientRole;
import com.example.logger.repository.ClientRepository;
import com.example.logger.repository.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ClientService {

    ClientRepository clientRepository;
    LogRepository logRepository;
    PasswordEncoder passwordEncoder;

    @Autowired
    ClientService(ClientRepository clientRepository,LogRepository logRepository, PasswordEncoder passwordEncoder){
        this.clientRepository = clientRepository;
        this.logRepository = logRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public void save(Client client){
        client.setRole(ClientRole.USER);
        String encodedPassword = passwordEncoder.encode(client.getPassword());
        client.setPassword(encodedPassword);
        clientRepository.save(client);
    }

    public List<ClientShowDto> allClients(int page,int size){
        return clientRepository.findAll(PageRequest.of(page,size)).stream().map(client -> { ClientShowDto clientShow =ClientShowDto.clientToClientShow(client);
                                                                    clientShow.setLogCount(logRepository.findByClient(client).size());
                                                                    return clientShow;}).collect(Collectors.toList());
    }

    public void setRoleToAdmin(Client client){
        clientRepository.setAdminRole(client.getClientId());
    }

    public void setClientPassword(long id, String password){
        clientRepository.setClientPassword(id,passwordEncoder.encode(password));
    }

    public boolean passValid(String password){
        return password.matches("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$");
    }
}
