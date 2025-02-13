package com.farm.AguaPlanta_api.repository;

import com.farm.AguaPlanta_api.models.Bancada;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface BancadaRepository extends JpaRepository<Bancada,Long> {

}
