package com.tallerwebi.dominio.entidad;

public class PuestoRanking {

    private Integer idSala;
    private Integer puntaje;
    private Usuario usuario;
    private Long tiempoTotal;
    private Integer cantidadPistas;

    public PuestoRanking() {}

    public PuestoRanking(Integer idSala, Integer puntaje, Usuario usuario, Long tiempoTotal, Integer cantidadPistas){
        this.idSala = idSala;
        this.puntaje = puntaje;
        this.usuario = usuario;
        this.tiempoTotal = tiempoTotal;
        this.cantidadPistas = cantidadPistas;

    }

    public Integer getIdSala() {
        return idSala;
    }

    public void setIdSala(Integer idSala) {
        this.idSala = idSala;
    }

    public Integer getPuntaje() {
        return puntaje;
    }

    public void setPuntaje(Integer puntaje) {
        this.puntaje = puntaje;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Long getTiempoTotal() {
        return tiempoTotal;
    }

    public void setTiempoTotal(Long tiempoTotal) {
        this.tiempoTotal = tiempoTotal;
    }

    public Integer getCantidadPistas() {
        return cantidadPistas;
    }

    public void setCantidadPistas(Integer cantidadPistas) {
        this.cantidadPistas = cantidadPistas;
    }





}
