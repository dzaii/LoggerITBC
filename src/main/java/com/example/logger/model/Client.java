package com.example.logger.model;

import com.example.logger.model.enums.ClientRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "client")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long clientId;
    @NotNull(message = "Username is required.")
    @Size(min = 3, message = "Username must be at least 3 characters long.")
    private String username;
    @NotBlank(message = "Email is required.")
    @Email
    private String email;
    @NotNull(message = "Password is required.")
    @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$",
            message = "Password must contain at least: 8 characters, one upper Case letter," +
                    " one lower case letter, one number and one special character.")
    private String password;
    private ClientRole role;


}
