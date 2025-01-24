package com.farm.AguaPlanta_api.models;

public class Planta {
    private Integer plantaId;
    private String nomePlanta;
    private String nomeImagem;
    public Planta(){

    }

    public Planta(Integer plantaId, String nomePlanta, String nomeImagem) {
        this.plantaId = plantaId;
        this.nomePlanta = nomePlanta;
        this.nomeImagem = nomeImagem;
    }

    public Integer getPlantaId() {
        return plantaId;
    }

    public void setPlantaId(Integer plantaId) {
        this.plantaId = plantaId;
    }

    public String getNomePlanta() {
        return nomePlanta;
    }

    public void setNomePlanta(String nomePlanta) {
        this.nomePlanta = nomePlanta;
    }

    public String getNomeImagem() {
        return nomeImagem;
    }

    public void setNomeImagem(String nomeImagem) {
        this.nomeImagem = nomeImagem;
    }
}
