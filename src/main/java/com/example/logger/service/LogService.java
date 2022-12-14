package com.example.logger.service;

import com.example.logger.dto.LogShowDto;
import com.example.logger.model.Client;
import com.example.logger.model.Log;
import com.example.logger.repository.ClientRepository;
import com.example.logger.repository.LogRepository;
import com.example.logger.security.JWTGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class LogService {

    LogRepository logRepository;
    ClientRepository clientRepository;
    private JWTGenerator jwtGenerator;

    @Autowired
    LogService(LogRepository logRepository, ClientRepository clientRepository, JWTGenerator jwtGenerator) {
        this.logRepository = logRepository;
        this.clientRepository = clientRepository;
        this.jwtGenerator = jwtGenerator;

    }

    public void createLog(String token, Log log) {
        String account = jwtGenerator.getUsernameFromToken(token.substring(7));
        Client client = this.findClientByAccount(account);

        log.setClient(client);
        logRepository.save(log);
    }

    public Client findClientByAccount(String account) {
        if (clientRepository.getClientLogin(account).isPresent()) {
            return clientRepository.getClientLogin(account).get();
        }
        return null;
    }

    public List<LogShowDto> getFiltered(String token, Date dateFrom, Date dateTo, String message, Integer logType) {

        String account = jwtGenerator.getUsernameFromToken(token.substring(7));
        Client client = this.findClientByAccount(account);

        if (Objects.isNull(dateFrom)) {
            Calendar cal = Calendar.getInstance();
            cal.set(1, 0, 1);
            dateFrom = cal.getTime();
        }
        if (Objects.isNull(dateTo)) {
            dateTo = Calendar.getInstance().getTime();
        }

        Date finalDateFrom = dateFrom;
        Date finalDateTo = dateTo;

        List<Log> logs = logRepository.findAll(new Specification<Log>() {
            @Override
            public Predicate toPredicate(Root<Log> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {

                Predicate p = cb.conjunction();
                p = cb.and(p, cb.equal(root.get("client"), client));
                if (finalDateFrom.before(finalDateTo)) {
                    p = cb.and(p, cb.between(root.get("createdDate"), finalDateFrom, finalDateTo));
                } else {
                    p = cb.and(root.isNull());
                }

                if (message != null) {
                    p = cb.and(p, cb.like(root.get("message"), "%" + message + "%"));
                }
                if (logType != null && logType >= 0 && logType < 3) {
                    p = cb.and(p, cb.equal(root.get("logType"), logType));
                }
                cq.orderBy(cb.desc(root.get("createdDate")));
                return p;
            }
        });
        return logs.stream().map(log -> LogShowDto.logToLogShow(log)).collect(Collectors.toList());
    }
}
