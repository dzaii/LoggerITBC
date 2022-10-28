package com.example.logger.controller;

import com.example.logger.model.Client;
import com.example.logger.model.Log;
import com.example.logger.repository.LogRepository;
import com.example.logger.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

@RestController
public class LogController {
    public static final String DATE_PATTERN = "yyyy/MM/dd";
    LogService logService;
    @Autowired
    LogController(LogRepository logRepository, LogService logService){
        this.logService = logService;
    }

    @PostMapping("/api/logs/create")
    public ResponseEntity createLog(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,@Valid @RequestBody Log log) {
        System.out.println(token);
        if(this.logService.createLog(token,log)){
            return ResponseEntity.status(HttpStatus.OK).body("Created log!");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token.");
    }
    @GetMapping("/api/logs/search")
    public List<Log> findAllFiltered(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                     @RequestParam(required = false)@DateTimeFormat(pattern = DATE_PATTERN) Date dateFrom,
                                     @RequestParam(required = false)@DateTimeFormat(pattern = DATE_PATTERN) Date dateTo,
                                     @RequestParam(required = false)String message,
                                     @RequestParam(required = false)Integer logType) {
        if (logService.findClientByToken(token) != null) {
            return logService.getFiltered(logService.findClientByToken(token), dateFrom, dateTo, message, logType);
        }
        return null;
    }
}
