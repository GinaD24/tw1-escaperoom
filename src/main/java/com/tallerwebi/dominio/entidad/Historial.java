package com.tallerwebi.dominio.entidad;
import java.time.LocalDateTime;


public class Historial {


    private Integer id;
    private String jugador;
    private Integer idSala;
    private LocalDateTime fecha;
    private Boolean gano;

    public Historial() {
    }

    public Historial(Integer id, String jugador, Integer idSala, LocalDateTime fecha, Boolean gano) {
        this.id = id;
        this.jugador = jugador;
        this.idSala = idSala;
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

    public Integer getIdSala() {
        return idSala;
    }

    public void setIdSala(Integer idSala) {
        this.idSala = idSala;
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