package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.RepositorioHistorial;
import com.tallerwebi.dominio.Historial;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RepositorioHistorialImpl implements RepositorioHistorial {

    private List<Historial> historials = new ArrayList<>();

    @Override
    public void guardar(Historial historial) {
        historials.add(historial);
    }

    @Override
    public List<Historial> obtenerTodas() {
        return new ArrayList<>(historials);
    }

    @Override
    public List<Historial> obtenerPorJugador(String jugador) {
        return historials.stream()
                .filter(p -> p.getJugador().equals(jugador))
                .collect(Collectors.toList());
    }
}