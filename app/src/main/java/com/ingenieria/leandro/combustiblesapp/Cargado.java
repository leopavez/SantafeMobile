package com.ingenieria.leandro.combustiblesapp;

public class Cargado {

    String id_estatico="";
    String solicitud = "";
    String unegocio = "";
    String fentrega= "";
    String hentrega="";
    String patente= "";
    String tvehiculo= "";
    String ubicacion= "";
    String estado= "";
    String odometro="";
    String lcargados="";
    String qcarga="";

    public Cargado() {

    }

    public Cargado(String id_estatico, String solicitud, String unegocio, String fentrega, String hentrega, String patente, String tvehiculo, String ubicacion, String estado, String odometro, String lcargados, String qcarga) {
        this.id_estatico = id_estatico;
        this.solicitud = solicitud;
        this.unegocio = unegocio;
        this.fentrega = fentrega;
        this.hentrega = hentrega;
        this.patente = patente;
        this.tvehiculo = tvehiculo;
        this.ubicacion = ubicacion;
        this.estado = estado;
        this.odometro = odometro;
        this.lcargados = lcargados;
        this.qcarga = qcarga;
    }

    public String getId_estatico() {
        return id_estatico;
    }

    public void setId_estatico(String id_estatico) {
        this.id_estatico = id_estatico;
    }

    public String getSolicitud() {
        return solicitud;
    }

    public void setSolicitud(String solicitud) {
        this.solicitud = solicitud;
    }

    public String getUnegocio() {
        return unegocio;
    }

    public void setUnegocio(String unegocio) {
        this.unegocio = unegocio;
    }

    public String getFentrega() {
        return fentrega;
    }

    public void setFentrega(String fentrega) {
        this.fentrega = fentrega;
    }

    public String getHentrega() {
        return hentrega;
    }

    public void setHentrega(String hentrega) {
        this.hentrega = hentrega;
    }

    public String getPatente() {
        return patente;
    }

    public void setPatente(String patente) {
        this.patente = patente;
    }

    public String getTvehiculo() {
        return tvehiculo;
    }

    public void setTvehiculo(String tvehiculo) {
        this.tvehiculo = tvehiculo;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getOdometro() {
        return odometro;
    }

    public void setOdometro(String odometro) {
        this.odometro = odometro;
    }

    public String getLcargados() {
        return lcargados;
    }

    public void setLcargados(String lcargados) {
        this.lcargados = lcargados;
    }

    public String getQcarga() {
        return qcarga;
    }

    public void setQcarga(String qcarga) {
        this.qcarga = qcarga;
    }
}


