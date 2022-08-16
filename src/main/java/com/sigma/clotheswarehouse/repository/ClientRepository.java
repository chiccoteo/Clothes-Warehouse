package com.sigma.clotheswarehouse.repository;

import com.sigma.clotheswarehouse.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {
    List<Client> findByDeletedIsFalse();
    Optional<Client> findByPhoneNumber(String phoneNumber);

}
