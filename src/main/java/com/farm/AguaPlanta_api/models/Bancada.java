package com.farm.AguaPlanta_api.models;

import jakarta.persistence.*;

@Entity
@Table(name = "bancadas") // Nome da tabela no banco de dados
public class Bancada {

    @Id

    private Long id;

    @Column(nullable = false)
    private String description;
    private String imagePath; // Caminho da imagem salva

    public Bancada() {}

    public Bancada(String description) {
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
