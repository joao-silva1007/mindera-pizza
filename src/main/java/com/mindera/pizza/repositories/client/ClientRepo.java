package com.mindera.pizza.repositories.client;

import com.mindera.pizza.domain.client.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepo extends JpaRepository<Client, Long> {

}
