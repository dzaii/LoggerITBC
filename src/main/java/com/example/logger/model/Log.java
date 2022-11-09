package com.example.logger.model;

import com.example.logger.model.enums.LogType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "log")
public class Log {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long logId;
    private String message;
    @Enumerated(EnumType.ORDINAL)
    private LogType logType;
    private Date createdDate;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(referencedColumnName = "clientId")
    private Client client;

    @PrePersist
    private void setLogDateTime() {
        this.setCreatedDate(Calendar.getInstance().getTime());
    }
}
