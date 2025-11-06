package com.tallerwebi.dominio.interfaz.repositorio;

import com.tallerwebi.dominio.entidad.Partida;

import java.util.List;

public interface RepositorioRanking {


    List<Partida> obtenerPartidasPorSala(Integer idSala);

    Integer obtenerIdSalaConPartidaGanada();
}

