package com.example.logger.controller;

import com.example.logger.dto.LogCreateDto;
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


    @Autowired
    LogController(LogService logService) {
        this.logService = logService;
    }

    @PostMapping("/api/logs/create")
    public ResponseEntity createLog(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @Valid @RequestBody LogCreateDto logCreateDto) {

        logService.createLog(token, logCreateDto.logCreateToLog());
        return ResponseEntity.status(HttpStatus.OK).body("Created log!");
    }

    @GetMapping("/api/logs/search")
    public ResponseEntity<?> findAllFiltered(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                             @RequestParam(required = false) @DateTimeFormat(pattern = DATE_PATTERN) Date dateFrom,
                                             @RequestParam(required = false) @DateTimeFormat(pattern = DATE_PATTERN) Date dateTo,
                                             @RequestParam(required = false) String message,
                                             @RequestParam(required = false) Integer logType) {

        return ResponseEntity.status(HttpStatus.OK).body(logService.getFiltered(token, dateFrom, dateTo, message, logType));
    }
}
