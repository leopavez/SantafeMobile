package com.ingenieria.leandro.combustiblesapp;

public class Surtidores {

    String id;
    String patente;
    String id_comb;

    public Surtidores() {

    }

    public Surtidores(String id, String patente, String id_comb) {
        this.id = id;
        this.patente = patente;
        this.id_comb = id_comb;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPatente() {
        return patente;
    }

    public void setPatente(String patente) {
        this.patente = patente;
    }

    public String getId_comb() {
        return id_comb;
    }

    public void setId_comb(String id_comb) {
        this.id_comb = id_comb;
    }
}
