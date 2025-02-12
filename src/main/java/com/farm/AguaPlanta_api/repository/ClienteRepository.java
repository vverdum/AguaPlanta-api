package com.farm.AguaPlanta_api.repository;

import com.farm.AguaPlanta_api.entities.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente,Long> {
}