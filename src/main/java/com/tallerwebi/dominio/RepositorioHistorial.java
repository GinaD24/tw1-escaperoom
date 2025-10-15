package com.tallerwebi.dominio;

import javax.transaction.Transactional;
import java.util.List;

@Transactional

public interface RepositorioHistorial {
    void guardar(Historial historial);
    List<Historial> obtenerTodas();
    List<Historial> obtenerPorJugador(String jugador);
}