package com.example.logger.controller;

import com.example.logger.dto.ClientLoginDto;
import com.example.logger.model.Client;
import com.example.logger.model.enums.ClientRole;
import com.example.logger.repository.ClientRepository;
import com.example.logger.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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

    @GetMapping("/api/clients")
    public ResponseEntity<?> allClients(@RequestHeader(HttpHeaders.AUTHORIZATION) String token){
        ClientRole role = clientService.getRoleFromToken(token);

        if(role == null){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid token.");
        }
        if(role!= ClientRole.ADMIN){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Correct token, but not admin");
        }
        return ResponseEntity.status(HttpStatus.OK).body(clientService.allClients());
    }

    @PatchMapping("/api/clients/{clientId}/reset-password")
    public ResponseEntity resetClientPassword(@PathVariable("clientId") long clientId,@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestBody Map<String,String> password){

        ClientRole role = clientService.getRoleFromToken(token);

        if(role == null){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid token.");
        }
        if(role!= ClientRole.ADMIN){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Correct token, but not admin");
        }

        if(!clientRepository.existsClientByClientId(clientId)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Wrong client id.");
        }
        if(!clientService.passValid(password.get("password"))){
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Password must contain at least: 8 characters, one upper Case letter," +
                    " one lower case letter, one number and one special character.");
        }
        clientService.setClientPassword(clientId,password.get("password"));
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Client password changed.");
    }

    @PostMapping("/api/clients/register")
    public ResponseEntity registerUser(@RequestBody @Valid Client client) {
        if(clientRepository.existsClientByEmail(client.getEmail())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already exists.");
        }
        if(clientRepository.existsClientByUsername(client.getUsername())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists.");
        }

        clientService.save(client);
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
