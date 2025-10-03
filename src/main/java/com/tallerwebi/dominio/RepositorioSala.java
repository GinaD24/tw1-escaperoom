package com.tallerwebi.dominio;

import java.util.List;

public interface RepositorioSala {

    List<Sala> obtenerSalas();

    Sala obtenerSalaPorId(Integer id);

    List<Sala> obtenerSalasPorDificultad(String dificultad);
}
