package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.Partida;
import com.tallerwebi.dominio.ServicioPartida;


import java.util.List;

public class ControladorPartida {

    private final ServicioPartida servicio;

    public ControladorPartida(ServicioPartida servicio) {
        this.servicio = servicio;
    }

    public void registrar(Partida partida) {
        servicio.registrarPartida(partida);
    }

    public List<Partida> verHistorial() {
        return servicio.traerHistorial();
    }

    public List<Partida> verHistorialJugador(String jugador) {
        return servicio.traerHistorialDeJugador(jugador);
    }
}