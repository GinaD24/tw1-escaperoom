package com.tallerwebi.dominio.entidad;

public class SalaVista {
    private Sala sala;
    private boolean desbloqueada;
    public SalaVista(Sala sala, boolean desbloqueada) {
        this.sala = sala;
        this.desbloqueada = desbloqueada;
    }
    // Getters
    public Sala getSala() { return sala; }
    public boolean isDesbloqueada() { return desbloqueada; }
}

