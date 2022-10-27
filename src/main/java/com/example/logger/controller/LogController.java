package com.example.logger.controller;

import com.example.logger.model.Client;
import com.example.logger.model.Log;
import com.example.logger.repository.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class LogController {
    LogRepository logRepository;

    @Autowired
    LogController(LogRepository logRepository){
        this.logRepository = logRepository;
    }

    @PostMapping("/api/logs/create")
    public ResponseEntity registerUser(@RequestBody Log log) {
        logRepository.save(log);
        return ResponseEntity.status(HttpStatus.OK).body("Created log!");
    }
    @GetMapping("/api/logs/search")
    public List<Log> allLogs() {
        return logRepository.findAll();
    }
}
