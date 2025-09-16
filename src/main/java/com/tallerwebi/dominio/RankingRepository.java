package com.tallerwebi.dominio;

import java.util.List;

public interface RankingRepository {
    void guardar(Ranking ranking);
    Ranking buscarPorIdDeSalaYNombreDeUsuario(Integer idSala, String nombreUsuario);
    List<Ranking> obtenerRankingPorSala(Integer idSala);

}
