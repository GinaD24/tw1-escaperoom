package com.tallerwebi.dominio.entidad;

import com.tallerwebi.dominio.enums.Dificultad;

import javax.persistence.*;

@Entity
public class Sala {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String nombre;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Dificultad dificultad;

    @Column(nullable = false)
    private String escenario;

    @Column(nullable = false)
    private String historia;

    @Column(nullable = false)
    private Boolean esta_habilitada;

    @Column(nullable = false)
    private Boolean es_paga;

    @Column(nullable = false)
    private Integer duracion;

    @Column
    private String imagenPuerta;

    @Column
    private String imagenSala;

    @Column
    private String imagenPerdida;

    @Column
    private String imagenGanada;

   @Column (nullable = false)
   private Integer cantidadDeEtapas;

    public Sala() {
    }

    public Sala(Integer id, String nombre, Dificultad dificultad, String escenario, String historia,
                Boolean esta_habilitada, Integer duracion, String imagenPuerta) {
        this.id = id;
        this.nombre = nombre;
        this.dificultad = dificultad;
        this.escenario = escenario;
        this.historia = historia;
        this.esta_habilitada = esta_habilitada;
        this.duracion = duracion;
        this.imagenPuerta = imagenPuerta;
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

    public Dificultad getDificultad() {
        return dificultad;
    }

    public void setDificultad(Dificultad dificultad) {
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

    public Integer getDuracion() {
        return duracion;
    }

    public void setDuracion(Integer duracion) {
        this.duracion = duracion;
    }

    public String getImagenPuerta() {
        return imagenPuerta;
    }

    public void setImagenPuerta(String imagen) {
        this.imagenPuerta = imagen;
    }

    public Integer getCantidadDeEtapas() {
        return cantidadDeEtapas;
    }

    public void setCantidadDeEtapas(Integer cantidadDeEtapas) {
        this.cantidadDeEtapas = cantidadDeEtapas;
    }

    public String getImagenSala() {
        return imagenSala;
    }

    public void setImagenSala(String imagenSala) {
        this.imagenSala = imagenSala;
    }

    public String getImagenGanada() {
        return imagenGanada;
    }

    public void setImagenGanada(String imagenGanada) {
        this.imagenGanada = imagenGanada;
    }

    public String getImagenPerdida() {
        return imagenPerdida;
    }

    public void setImagenPerdida(String imagenPerdida) {
        this.imagenPerdida = imagenPerdida;
    }

    public Boolean getEs_paga() {
        return es_paga;
    }

    public void setEs_paga(Boolean es_paga) {
        this.es_paga = es_paga;
    }
}

