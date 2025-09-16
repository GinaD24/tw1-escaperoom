package com.tallerwebi.dominio;

import java.util.List;

public interface ServicioRanking {

    void agregarRanking(Ranking nuevoRanking);
    void actualizarRanking(Ranking nuevoRanking);
    List<Ranking> obtenerRankingPorSala(Integer idSala);


}
