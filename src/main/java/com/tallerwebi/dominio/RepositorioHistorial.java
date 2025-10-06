package com.tallerwebi.dominio;

import java.util.List;

public interface RepositorioHistorial {
    void guardar(Historial historial);
    List<Historial> obtenerTodas();
    List<Historial> obtenerPorJugador(String jugador);
}