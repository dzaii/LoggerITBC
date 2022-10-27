package com.example.logger.repository;

import com.example.logger.model.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface LogRepository extends JpaRepository<Log,Integer> {
    List<Log> findAll();


}
