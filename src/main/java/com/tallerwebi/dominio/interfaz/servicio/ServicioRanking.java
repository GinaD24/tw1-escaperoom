package com.tallerwebi.dominio.interfaz.servicio;

import com.tallerwebi.dominio.entidad.PuestoRanking;

import java.util.List;

public interface ServicioRanking {

    List<PuestoRanking> obtenerRankingPorSala(Integer idSala);


    Integer obtenerIdSalaConPartidaGanada();
}
