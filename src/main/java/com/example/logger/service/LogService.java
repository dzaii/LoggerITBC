package com.example.logger.service;

import antlr.StringUtils;
import com.example.logger.model.Client;
import com.example.logger.model.Log;
import com.example.logger.repository.ClientRepository;
import com.example.logger.repository.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class LogService {

    LogRepository logRepository;
    ClientRepository clientRepository;
    @Autowired
    LogService(LogRepository logRepository, ClientRepository clientRepository){
        this.logRepository = logRepository;
        this.clientRepository = clientRepository;

    }

    public boolean createLog(String token, Log log){
        if(clientRepository.findByToken(token).isPresent()){
            log.setClient(clientRepository.findByToken(token).get());
            logRepository.save(log);
            return true;
        }
        return false;
    }
    public List<Log> findAllForClient(String token) {
        if (clientRepository.findByToken(token).isPresent()) {
            Client client = clientRepository.findByToken(token).get();
            return logRepository.findByClient(client);
        }
        return null;
    }
    public Client findClientByToken(String token){
        if(clientRepository.findByToken(token).isPresent()) {
            return clientRepository.findByToken(token).get();
        }
        return null;
    }

    public List<Log> getFiltered(Client client, Date dateFrom, Date dateTo, String message, Integer logType) {
        List<Log> logs = logRepository.findAll(new Specification<Log>() {
            @Override
            public Predicate toPredicate(Root<Log> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                Predicate p = cb.conjunction();
                    p = cb.and(p, cb.equal(root.get("client"),client));
                if(Objects.nonNull(dateFrom) && Objects.nonNull(dateTo) && dateFrom.before(dateTo)){
                    p = cb.and(p, cb.between(root.get("createdDate"),dateFrom,dateTo));
                }
                if(message != null){
                    p = cb.and(p, cb.like(root.get("message"), "%" + message +"%"));
                }
                if(logType!= null && logType >=0 && logType <3){
                    p = cb.and(p, cb.equal(root.get("logType"), logType));
                }
                cq.orderBy(cb.desc(root.get("createdDate")));
                return p;
            }
        });
        return logs;
    }
}
