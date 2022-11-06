package com.example.logger.repository;

import com.example.logger.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Integer> {
    List<Client> findAll();
    boolean existsClientByUsername(String username);
    boolean existsClientByEmail(String email);
    boolean existsClientByClientId(long id);

    @Query(value = "SELECT TOP 1 * FROM client u WHERE (u.username like :username OR u.email like :username)", nativeQuery = true)
    Optional<Client> getClientLogin(@Param("username") String x);

    @Transactional
    @Modifying
    @Query("update Client u set u.role = 1 where u.clientId = :id")
    void setAdminRole(@Param("id") long x);

    @Transactional
    @Modifying
    @Query("update Client u set u.password = :password where u.clientId = :id")
    void setClientPassword(@Param("id") long x, @Param("password") String password);


}
