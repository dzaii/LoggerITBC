package com.example.logger.controller;


import com.example.logger.dto.ClientLoginDto;
import com.example.logger.dto.LoginResponseDto;
import com.example.logger.model.Client;
import com.example.logger.repository.ClientRepository;
import com.example.logger.security.JWTGenerator;
import com.example.logger.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.Map;

@RestController
public class ClientController {

    ClientRepository clientRepository;
    ClientService clientService;
    AuthenticationManager authenticationManager;
    private JWTGenerator jwtGenerator;

    @Autowired
    ClientController(ClientRepository clientRepository, ClientService clientService, AuthenticationManager authenticationManager, JWTGenerator jwtGenerator) {
        this.clientRepository = clientRepository;
        this.clientService = clientService;
        this.authenticationManager = authenticationManager;
        this.jwtGenerator = jwtGenerator;
    }
    @GetMapping("/api/clients")
    public ResponseEntity<?> allClients(@RequestParam(required = false) Integer page) {

        if (page == null) {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Location","/api/clients?page=0");
            return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
        }
        return ResponseEntity.status(HttpStatus.OK).body(clientService.allClients(page, 5));
    }

    @PatchMapping("/api/clients/{clientId}/reset-password")
    public ResponseEntity resetClientPassword(@PathVariable("clientId") long clientId, @RequestBody Map<String, String> password) {

        if (!clientRepository.existsClientByClientId(clientId)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Wrong client id.");
        }
        if (!clientService.passValid(password.get("password"))) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Password must contain at least: 8 characters, one upper Case letter," +
                    " one lower case letter, one number and one special character.");
        }
        clientService.setClientPassword(clientId, password.get("password"));
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Client password changed.");
    }

    @PostMapping("/api/clients/register")
    public ResponseEntity registerUser(@RequestBody @Valid Client client) {

        if (clientRepository.existsClientByEmail(client.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already exists.");
        }
        if (clientRepository.existsClientByUsername(client.getUsername())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists.");
        }

        clientService.save(client);
        return ResponseEntity.status(HttpStatus.OK).body("Registered!");
    }

    @PostMapping("/api/clients/login")
    public ResponseEntity<LoginResponseDto> loginUser(@RequestBody @Valid ClientLoginDto clientLoginDto) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(clientLoginDto.getAccount(),
                        clientLoginDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtGenerator.generateToken(authentication);
        return ResponseEntity.status(HttpStatus.OK).body(new LoginResponseDto(token));
    }
}
