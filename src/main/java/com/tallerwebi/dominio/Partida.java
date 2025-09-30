package com.tallerwebi.dominio;

import java.time.LocalDateTime;

public class Partida {

    private Integer id;
    private String jugador;
    private String sala;
    private LocalDateTime fecha;
    private Boolean gano;

    public Partida() {
    }

    public Partida(Integer id, String jugador, String sala, LocalDateTime fecha, Boolean gano) {
        this.id = id;
        this.jugador = jugador;
        this.sala = sala;
        this.fecha = fecha;
        this.gano = gano;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getJugador() {
        return jugador;
    }

    public void setJugador(String jugador) {
        this.jugador = jugador;
    }

    public String getSala() {
        return sala;
    }

    public void setSala(String sala) {
        this.sala = sala;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public Boolean getGano() {
        return gano;
    }

    public void setGano(Boolean gano) {
        this.gano = gano;
    }
}
