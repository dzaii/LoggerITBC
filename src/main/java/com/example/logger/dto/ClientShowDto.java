package com.example.logger.dto;

import com.example.logger.model.Client;
import com.example.logger.model.Log;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
public class ClientShowDto {

    private long clientId;
    private String username;
    private String email;
    private int logCount;

    public static ClientShowDto clientToClientShow(Client client){
        return new ClientShowDto(client.getClientId(),client.getUsername(),client.getEmail(),0);
    }
}
