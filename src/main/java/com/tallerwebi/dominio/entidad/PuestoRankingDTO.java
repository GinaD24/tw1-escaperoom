package com.tallerwebi.dominio.entidad;

public class PuestoRankingDTO {

    private Sala sala;
    private Integer puntaje;
    private Usuario usuario;
    private Long tiempoTotal;
    private Integer cantidadPistas;
    private Double puntajeCalculado;
    private Integer puesto;


    public PuestoRankingDTO(Sala sala, Integer puntaje, Usuario usuario, Long tiempoTotal, Integer cantidadPistas, Double puntajeCalculado) {
        this.sala = sala;
        this.puntaje = puntaje;
        this.usuario = usuario;
        this.tiempoTotal = tiempoTotal;
        this.cantidadPistas = cantidadPistas;
        this.puntajeCalculado = puntajeCalculado;

    }

    public PuestoRankingDTO(){}

    public Integer getPuesto() {
        return puesto;
    }

    public void setPuesto(Integer puesto) {
        this.puesto = puesto;
    }

    public Double getPuntajeCalculado() {
        return puntajeCalculado;
    }

    public void setPuntajeCalculado(Double puntajeCalculado) {
        this.puntajeCalculado = puntajeCalculado;
    }

    public Sala getSala() {
        return sala;
    }

    public void setSala(Sala sala) {
        this.sala = sala;
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
