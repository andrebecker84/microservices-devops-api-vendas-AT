package com.exemplo.clientesservice.repository;

import com.exemplo.clientesservice.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}
