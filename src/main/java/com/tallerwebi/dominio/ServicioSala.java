package com.tallerwebi.dominio;

import java.util.ArrayList;
import java.util.List;


public interface ServicioSala {

    ArrayList<Sala> traerSalas();

    Sala obtenerSalaPorId(Integer id);

    List<Sala> obtenerSalaPorDificultad(String dificultad);

    void habilitarSala(Sala sala);

    void descontarAcertijo(Sala salaConCincoAcertijos);
}
