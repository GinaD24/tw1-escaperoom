package com.tallerwebi.dominio;

import javax.persistence.*;
import java.time.Duration;

@Entity
public class Sala {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String dificultad;

    @Column(nullable = false)
    private String escenario;

    @Column(nullable = false)
    private String historia;

    @Column(nullable = false)
    private Boolean esta_habilitada;

    @Column(nullable = false)
    private Integer cantidadAcertijos;

    @Column(nullable = false)
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

    public void descontarTiempo() {
        this.setDuracion(this.getDuracion().minusMinutes(3));
    }
}
