package com.tallerwebi.dominio.interfaz.servicio;

import com.tallerwebi.dominio.entidad.Ranking;

import java.util.List;

public interface ServicioRanking {

    void agregarRanking(Ranking nuevoRanking);
    void actualizarRanking(Ranking nuevoRanking);
    List<Ranking> obtenerRankingPorSala(Integer idSala);


}
