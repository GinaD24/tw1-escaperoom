package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.RepositorioPartida;
import com.tallerwebi.dominio.Partida;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RepositorioPartidaImpl implements RepositorioPartida {

    private List<Partida> partidas = new ArrayList<>();

    @Override
    public void guardar(Partida partida) {
        partidas.add(partida);
    }

    @Override
    public List<Partida> obtenerTodas() {
        return new ArrayList<>(partidas);
    }

    @Override
    public List<Partida> obtenerPorJugador(String jugador) {
        return partidas.stream()
                .filter(p -> p.getJugador().equals(jugador))
                .collect(Collectors.toList());
    }
}