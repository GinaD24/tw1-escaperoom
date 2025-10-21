package com.tallerwebi.dominio.interfaz.servicio;

import com.tallerwebi.dominio.entidad.Sala;
import com.tallerwebi.dominio.enums.Dificultad;

import java.util.ArrayList;
import java.util.List;


public interface ServicioSala {

    ArrayList<Sala> traerSalas();

    Sala obtenerSalaPorId(Integer id);

    List<Sala> obtenerSalaPorDificultad(Dificultad dificultad);


}
