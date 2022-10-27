package com.example.logger.service;

import antlr.StringUtils;
import com.example.logger.model.Client;
import com.example.logger.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class ClientService {

    ClientRepository clientRepository;

    @Autowired
    ClientService(ClientRepository clientRepository){
        this.clientRepository = clientRepository;
    }

    public String login(String account, String password) {
        Optional<Client> client = clientRepository.getClientLogin(account, password);
        if (client.isPresent()) {
            clientRepository.updateClientToken(UUID.randomUUID().toString(),client.get().getClientId());
            return clientRepository.getClientToken(client.get().getClientId());
        }
            return "";

    }
}
