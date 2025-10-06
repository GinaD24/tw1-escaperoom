package com.tallerwebi.dominio;

import com.tallerwebi.dominio.enums.Dificultad;

import java.util.ArrayList;
import java.util.List;


public interface ServicioSala {

    ArrayList<Sala> traerSalas();

    Sala obtenerSalaPorId(Integer id);

    List<Sala> obtenerSalaPorDificultad(Dificultad dificultad);


}
