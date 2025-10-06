package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.Historial;
import com.tallerwebi.dominio.ServicioHistorial;


import java.util.List;

public class ControladorPartida {

    private final ServicioHistorial servicio;

    public ControladorPartida(ServicioHistorial servicio) {
        this.servicio = servicio;
    }

    public void registrar(Historial historial) {
        servicio.registrarPartida(historial);
    }

    public List<Historial> verHistorial() {
        return servicio.traerHistorial();
    }

    public List<Historial> verHistorialJugador(String jugador) {
        return servicio.traerHistorialDeJugador(jugador);
    }
}