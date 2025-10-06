package com.tallerwebi.dominio;

import java.util.List;

public interface ServicioHistorial {
    void registrarPartida(Historial historial);
    List<Historial> traerHistorial();
    List<Historial> traerHistorialDeJugador(String jugador);
}