package com.example.logger.model;

import javax.persistence.*;
import java.util.UUID;
@Entity
@Table(name = "client")
public class Client {

    @Id
    private Long clientId;
    private String username;
    private String email;
    private String password;

    public Client(){
    }


}
