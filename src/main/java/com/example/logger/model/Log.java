package com.example.logger.model;

import com.example.logger.model.enums.ClientRole;
import com.example.logger.model.enums.LogType;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@Entity
@Table(name = "log")
public class Log {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long logId;
    private String message;
    private LogType logType;
    private LocalDateTime createdDate;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(referencedColumnName = "clientId")
    private Client client;

    @PrePersist
    private void setLogDateTime(){
        this.setCreatedDate(LocalDateTime.now());
    }
}
