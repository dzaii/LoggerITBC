package com.example.logger.controller;

import com.example.logger.dto.ClientLoginDto;
import com.example.logger.model.Client;
import com.example.logger.model.enums.ClientRole;
import com.example.logger.repository.ClientRepository;
import com.example.logger.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
public class ClientController {

    ClientRepository clientRepository;
    ClientService clientService;

    @Autowired
    ClientController(ClientRepository clientRepository, ClientService clientService){
        this.clientRepository = clientRepository;
        this.clientService= clientService;
    }

    @GetMapping("/api/clients/all")
    public List<Client> allClients() {
        return clientRepository.findAll();
    }

    @PostMapping("/api/clients/register")
    public ResponseEntity registerUser(@RequestBody @Valid Client client) {
        if(clientRepository.existsClientByEmail(client.getEmail())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already exists.");
        }
        if(clientRepository.existsClientByUsername(client.getUsername())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists.");
        }

        clientRepository.save(client);
        return ResponseEntity.status(HttpStatus.OK).body("Registered!");
    }

    @PostMapping("/api/clients/login")
    public ResponseEntity loginUser(@RequestBody @ Valid ClientLoginDto clientLoginDto){
        String account= clientLoginDto.getAccount();
        String password= clientLoginDto.getPassword();

        if(!clientService.login(account,password).isEmpty()){
        return ResponseEntity.status(HttpStatus.OK).body("token : " + clientService.login(account,password));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email/Username or password incorrect");
    }
}
