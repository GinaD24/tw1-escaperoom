package com.tallerwebi.dominio;

import java.util.List;

public interface ServicioPartida {
    void registrarPartida(Partida partida);
    List<Partida> traerHistorial();
    List<Partida> traerHistorialDeJugador(String jugador);
}