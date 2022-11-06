package com.example.logger.controller;

import com.example.logger.dto.LogCreateDto;
import com.example.logger.model.Client;
import com.example.logger.repository.LogRepository;
import com.example.logger.security.JWTGenerator;
import com.example.logger.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.Date;


@RestController
public class LogController {
    public static final String DATE_PATTERN = "yyyy/MM/dd";
    private LogService logService;
    private JWTGenerator jwtGenerator;

    @Autowired
    LogController(LogRepository logRepository, LogService logService, JWTGenerator jwtGenerator) {
        this.logService = logService;
        this.jwtGenerator = jwtGenerator;
    }

    @PostMapping("/api/logs/create")
    public ResponseEntity createLog(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @Valid @RequestBody LogCreateDto logCreateDto) {

        String account = jwtGenerator.getUsernameFromToken(token.substring(7));
        Client client = logService.findClientByAccount(account);
        logService.createLog(client, logCreateDto.logCreateToLog());

        return ResponseEntity.status(HttpStatus.OK).body("Created log!");
    }
    @GetMapping("/api/logs/search")
    public ResponseEntity<?> findAllFiltered(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                             @RequestParam(required = false) @DateTimeFormat(pattern = DATE_PATTERN) Date dateFrom,
                                             @RequestParam(required = false) @DateTimeFormat(pattern = DATE_PATTERN) Date dateTo,
                                             @RequestParam(required = false) String message,
                                             @RequestParam(required = false) Integer logType) {

        String account = jwtGenerator.getUsernameFromToken(token.substring(7));
        Client client = logService.findClientByAccount(account);

        return ResponseEntity.status(HttpStatus.OK).body(logService.getFiltered(client, dateFrom, dateTo, message, logType));
    }
}
