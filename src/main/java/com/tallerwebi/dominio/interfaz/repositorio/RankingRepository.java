package com.tallerwebi.dominio.interfaz.repositorio;

import com.tallerwebi.dominio.entidad.Ranking;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RankingRepository {
    void guardar(Ranking ranking);
    Ranking buscarPorIdDeSalaYNombreDeUsuario(Integer idSala, String nombreUsuario);
    List<Ranking> obtenerRankingPorSala(Integer idSala);

}
