package com.tallerwebi.dominio.interfaz.servicio;

import com.tallerwebi.dominio.entidad.Partida;

import java.util.List;

public interface ServicioHistorial {
    List<Partida> traerHistorialDeJugador(Long idUsuario);

}