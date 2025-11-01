package com.tallerwebi.dominio.interfaz.servicio;

import com.tallerwebi.dominio.entidad.Historial;

import java.util.List;

public interface ServicioHistorial {
    void registrarPartida(Historial historial);
    List<Historial> traerHistorial();
    List<Historial> traerHistorialDeJugador(String jugador);
    List<Historial> obtenerHistorialPorSala(Integer idSala);
}