package com.vladima.gamingrental.repository;

import com.vladima.gamingrental.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    Client findByClientEmail(String email);
    Client findByClientPhone(String phone);
}
