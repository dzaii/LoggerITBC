package com.example.logger.service;

import antlr.StringUtils;
import com.example.logger.dto.ClientShowDto;
import com.example.logger.model.Client;
import com.example.logger.repository.ClientRepository;
import com.example.logger.repository.LogRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class ClientService {

    ClientRepository clientRepository;
    LogRepository logRepository;
    public String login(String account, String password) {
        Optional<Client> client = clientRepository.getClientLogin(account, password);
        if (client.isPresent()) {
            clientRepository.updateClientToken(UUID.randomUUID().toString(),client.get().getClientId());
            return clientRepository.getClientToken(client.get().getClientId());
        }
            return "";

    }

    public List<ClientShowDto> allClients(){
        return clientRepository.findAll().stream().map(client -> { ClientShowDto clientShow =ClientShowDto.clientToClientShow(client);
                                                                    clientShow.setLogCount(logRepository.findByClient(client).size());
                                                                    return clientShow;}).collect(Collectors.toList());
    }
}
