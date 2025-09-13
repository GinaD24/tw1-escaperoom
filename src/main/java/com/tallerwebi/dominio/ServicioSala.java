package com.tallerwebi.dominio;

import java.util.ArrayList;


public interface ServicioSala {

    ArrayList<Sala> getSalas();

    Sala obtenerSalaPorId(Integer id);
}
