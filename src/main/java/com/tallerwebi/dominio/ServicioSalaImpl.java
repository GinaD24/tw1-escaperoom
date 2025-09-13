package com.tallerwebi.dominio;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ServicioSalaImpl implements ServicioSala{

    @Override
    public ArrayList<Sala> getSalas() {
        return new ArrayList<Sala>();
    }

    @Override
    public Sala obtenerSalaPorId(Integer id) {
        return new Sala();
    }
}
