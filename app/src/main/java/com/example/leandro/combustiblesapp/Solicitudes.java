package com.example.leandro.combustiblesapp;

public class Solicitudes {


    String id_estatico="";
    String solicitud_id = "";
    String unegocio = "";
    String fentrega= "";
    String patente= "";
    String tvehiculo= "";
    String ubicacion= "";
    String litros= "";
    String lasignados="";
    String qrecibe="";
    String estado="";

    public Solicitudes(String id_estatico, String solicitud_id, String unegocio, String fentrega, String patente, String tvehiculo, String ubicacion, String litros, String lasignados, String qrecibe, String estado) {
        this.id_estatico = id_estatico;
        this.solicitud_id = solicitud_id;
        this.unegocio = unegocio;
        this.fentrega = fentrega;
        this.patente = patente;
        this.tvehiculo = tvehiculo;
        this.ubicacion = ubicacion;
        this.litros = litros;
        this.lasignados = lasignados;
        this.qrecibe = qrecibe;
        this.estado = estado;
    }

    public Solicitudes() {

    }

    public String getId_estatico(){
        return id_estatico;
    }

    public String getSolicitud_id() {
        return solicitud_id;
    }

    public void setId_estatico(String id_estatico){
        this.id_estatico = id_estatico;
    }

    public void setSolicitud_id(String solicitud_id) {
        this.solicitud_id = solicitud_id;
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

    public String getLitros() {
        return litros;
    }

    public void setLitros(String litros) {
        this.litros = litros;
    }

    public String getLasignados() {
        return lasignados;
    }

    public void setLasignados(String lasignados) {
        this.lasignados = lasignados;
    }

    public String getQrecibe() {
        return qrecibe;
    }

    public void setQrecibe(String qrecibe) {
        this.qrecibe = qrecibe;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
