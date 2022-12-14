package com.example.logger.dto;

import com.example.logger.model.Log;
import com.example.logger.model.enums.LogType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@AllArgsConstructor
@Data
public class LogShowDto {

    private String message;
    private LogType logType;
    private Date createdDate;

    public static LogShowDto logToLogShow(Log log) {
        return new LogShowDto(log.getMessage(), log.getLogType(), log.getCreatedDate());
    }
}
