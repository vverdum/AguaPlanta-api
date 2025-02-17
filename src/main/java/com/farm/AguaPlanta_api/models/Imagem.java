package com.farm.AguaPlanta_api.models;

import jakarta.persistence.*;

@Entity
@Table(name = "imagens")
public class Imagem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int bancadaId;
    private String nomeArquivo;

    @Lob
    private byte[] dados; // Armazena a imagem como BLOB

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getBancadaId() {
        return bancadaId;
    }

    public void setBancadaId(int bancadaId) {
        this.bancadaId = bancadaId;
    }

    public String getNomeArquivo() {
        return nomeArquivo;
    }

    public void setNomeArquivo(String nomeArquivo) {
        this.nomeArquivo = nomeArquivo;
    }

    public byte[] getDados() {
        return dados;
    }

    public void setDados(byte[] dados) {
        this.dados = dados;
    }


}
