package com.tallerwebi.dominio;

import java.time.Duration;

public class Sala {

    private Integer id;
    private String nombre;
    private String dificultad;
    private String escenario;
    private String historia;
    private Boolean esta_habilitada;
    private Integer cantidadAcertijos;
    private Duration duracion;

    public Sala() {
    }

    public Sala(Integer id, String nombre, String dificultad, String escenario, String historia,
                Boolean esta_habilitada, Integer cantidadAcertijos, Duration duracion) {
        this.id = id;
        this.nombre = nombre;
        this.dificultad = dificultad;
        this.escenario = escenario;
        this.historia = historia;
        this.esta_habilitada = esta_habilitada;
        this.cantidadAcertijos = cantidadAcertijos;
        this.duracion = duracion;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDificultad() {
        return dificultad;
    }

    public void setDificultad(String dificultad) {
        this.dificultad = dificultad;
    }

    public String getEscenario() {
        return escenario;
    }

    public void setEscenario(String escenario) {
        this.escenario = escenario;
    }

    public String getHistoria() {
        return historia;
    }

    public void setHistoria(String historia) {
        this.historia = historia;
    }

    public Boolean getEsta_habilitada() {
        return esta_habilitada;
    }

    public void setEsta_habilitada(Boolean esta_habilitada) {
        this.esta_habilitada = esta_habilitada;
    }

    public Integer getCantidadAcertijos() {
        return cantidadAcertijos;
    }

    public void setCantidadAcertijos(Integer cantidadAcertijos) {
        this.cantidadAcertijos = cantidadAcertijos;
    }

    public Duration getDuracion() {
        return duracion;
    }

    public void setDuracion(Duration duracion) {
        this.duracion = duracion;
    }
}
