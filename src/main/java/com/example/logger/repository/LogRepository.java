package com.example.logger.repository;

import com.example.logger.model.Client;
import com.example.logger.model.Log;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LogRepository extends JpaRepository<Log,Integer>, JpaSpecificationExecutor<Log> {
    List<Log> findAll();

    List<Log> findByClient(Client client);



}
