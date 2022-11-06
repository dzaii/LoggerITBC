package com.example.logger.dto;

import lombok.Data;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class ClientLoginDto {

    @Size(min = 2, message = "Invalid username or password")
    private String account;
    @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$", message = "Invalid username or password")
    private String password;
}
