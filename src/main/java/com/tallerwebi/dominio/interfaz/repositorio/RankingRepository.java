package com.tallerwebi.dominio.interfaz.repositorio;

import com.tallerwebi.dominio.entidad.Ranking;

import java.util.List;

public interface RankingRepository {
    void guardar(Ranking ranking);
    Ranking buscarPorIdDeSalaYNombreDeUsuario(Integer idSala, String nombreUsuario);
    List<Ranking> obtenerRankingPorSala(Integer idSala);

}
