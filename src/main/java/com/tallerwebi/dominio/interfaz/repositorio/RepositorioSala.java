package com.tallerwebi.dominio.interfaz.repositorio;

import com.tallerwebi.dominio.entidad.Sala;
import com.tallerwebi.dominio.enums.Dificultad;

import java.util.List;

public interface RepositorioSala {

    List<Sala> obtenerSalas();

    Sala obtenerSalaPorId(Integer id);

    List<Sala> obtenerSalasPorDificultad(Dificultad dificultad);
}
