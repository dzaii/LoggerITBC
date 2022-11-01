package com.example.logger.service;

import com.example.logger.dto.ClientShowDto;
import com.example.logger.model.Client;
import com.example.logger.model.enums.ClientRole;
import com.example.logger.repository.ClientRepository;
import com.example.logger.repository.LogRepository;
import lombok.AllArgsConstructor;
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


    public void save(Client client){
        client.setRole(ClientRole.USER);
        clientRepository.save(client);
    }
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
        clientRepository.setClientPassword(id,password);
    }

    public boolean passValid(String password){
        return password.matches("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$");
    }
}
