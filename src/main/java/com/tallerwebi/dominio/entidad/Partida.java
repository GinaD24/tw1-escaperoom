package com.tallerwebi.dominio.entidad;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Partida {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_sala")
    private Sala sala;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @Column(nullable = false)
    private Integer puntaje = 0;

    @Column(nullable = false)
    private LocalDateTime inicio;
    @Column
    private LocalDateTime fin;
    @Column
    private Long tiempoTotal;

    @Column (nullable = false)
    private Boolean esta_activa;

    @Column
    private Integer pistasUsadas = 0;

    @Column
    private Boolean ganada;

    public Partida() {
    }

    public Partida(LocalDateTime inicio) {
        this.inicio = inicio;
    }

    public Sala getSala() {
        return sala;
    }

    public void setSala(Sala sala) {
        this.sala = sala;
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

    public Integer getPuntaje() {
        return puntaje;
    }

    public void setPuntaje(Integer puntaje) {
        this.puntaje = puntaje;
    }

    public LocalDateTime getFin() {
        return fin;
    }

    public void setFin(LocalDateTime fin) {
        this.fin = fin;
    }

    public LocalDateTime getInicio() {
        return inicio;
    }

    public void setInicio(LocalDateTime inicio) {
        this.inicio = inicio;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public Boolean getEsta_activa() {
        return esta_activa;
    }

    public void setEsta_activa(Boolean esta_activa) {
        this.esta_activa = esta_activa;
    }

    public Boolean getGanada() {
        return ganada;
    }

    public void setGanada(Boolean ganada) {
        this.ganada = ganada;
    }

    public Integer getPistasUsadas() {
        return pistasUsadas;
    }

    public void setPistasUsadas(Integer pistasUsadas) {
        this.pistasUsadas = pistasUsadas;
    }
}
