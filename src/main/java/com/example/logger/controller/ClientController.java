package com.example.logger.controller;

import com.example.logger.dto.ClientLoginDto;
import com.example.logger.dto.LoginResponseDto;
import com.example.logger.model.Client;
import com.example.logger.repository.ClientRepository;
import com.example.logger.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.management.InvalidAttributeValueException;
import javax.validation.Valid;
import java.util.Map;

@RestController
public class ClientController {

    ClientRepository clientRepository;
    ClientService clientService;

    @Autowired
    ClientController(ClientRepository clientRepository, ClientService clientService) {
        this.clientRepository = clientRepository;
        this.clientService = clientService;

    }

    @GetMapping("/api/clients")
    public ResponseEntity<?> allClients(@RequestParam(required = false) Integer page) throws InvalidAttributeValueException {

        return ResponseEntity.status(HttpStatus.OK).body(clientService.allClients(page, 5));
    }

    @PatchMapping("/api/clients/{clientId}/reset-password")
    public ResponseEntity resetClientPassword(@PathVariable("clientId") long clientId, @RequestBody Map<String, String> password) {

        clientService.setClientPassword(clientId, password.get("password"));
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Client password changed.");
    }

    @PostMapping("/api/clients/register")
    public ResponseEntity registerUser(@RequestBody @Valid Client client) {

        clientService.save(client);
        return ResponseEntity.status(HttpStatus.OK).body("Registered!");
    }

    @PostMapping("/api/clients/login")
    public ResponseEntity<LoginResponseDto> loginUser(@RequestBody @Valid ClientLoginDto clientLoginDto) {

        return ResponseEntity.status(HttpStatus.OK).body(clientService.login(clientLoginDto));
    }
}
