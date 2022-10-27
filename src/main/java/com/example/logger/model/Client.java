package com.example.logger.model;

import com.example.logger.model.enums.ClientRole;
import com.sun.istack.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.*;
import java.util.UUID;

@Data
@NoArgsConstructor
@Entity
@Table(name = "client")
public class Client {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long clientId;
    private String username;
    private String email;
    private String password;
    private ClientRole role;
    private String token;
    @PrePersist
    private void setUserRoleAndToken(){
        this.setRole(ClientRole.USER);
        this.setToken(UUID.randomUUID().toString());
        }

}
