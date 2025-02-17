package com.farm.AguaPlanta_api.repository;


import com.farm.AguaPlanta_api.models.Imagem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImagemRepository extends JpaRepository<Imagem, Long> {
    List<Imagem> findByBancadaId(int bancadaId);
}
