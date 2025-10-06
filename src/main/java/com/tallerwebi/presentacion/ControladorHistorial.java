package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.Historial;
import com.tallerwebi.dominio.ServicioHistorial;


import java.util.List;

public class ControladorHistorial {

    private final ServicioHistorial servicio;

    public ControladorHistorial(ServicioHistorial servicio) {
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