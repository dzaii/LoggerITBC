package com.example.logger.dto;

import com.example.logger.model.Log;
import com.example.logger.model.enums.LogType;
import com.example.logger.utils.enumValidation.ValueOfEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Data
@AllArgsConstructor
public class LogCreateDto {
    @NotNull(message = "Message is missing")
    @Size(max = 1024, message = "Log must be less than 1024 characters long.")
    private String message;
    @NotNull(message = "Log type is missing")
    @ValueOfEnum(enumClass = LogType.class, message = "Incorrect log type.")
    private int logType;

    public Log logCreateToLog(){
        return new Log(0,this.getMessage(),LogType.values()[this.getLogType()], null,null);
    }

}
