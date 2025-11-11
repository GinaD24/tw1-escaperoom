package com.tallerwebi.dominio.interfaz.servicio;

import com.tallerwebi.dominio.entidad.Partida;
import com.tallerwebi.dominio.entidad.PuestoRankingDTO;

import java.util.List;

public interface ServicioRanking {

    List<PuestoRankingDTO> obtenerRanking();

    Double obtenerPuntajeCalculado(Partida partida);
}
