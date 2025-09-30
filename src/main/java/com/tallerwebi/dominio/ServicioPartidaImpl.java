package com.tallerwebi.dominio;

import java.util.List;

public class ServicioPartidaImpl implements ServicioPartida {

    private RepositorioPartida repositorio;

    public ServicioPartidaImpl(RepositorioPartida repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    public void registrarPartida(Partida partida) {
        repositorio.guardar(partida);
    }

    @Override
    public List<Partida> traerHistorial() {
        List<Partida> partidas = repositorio.obtenerTodas();
        partidas.sort((p1, p2) -> p2.getFecha().compareTo(p1.getFecha())); // orden descendente por fecha
        return partidas;
    }

    @Override
    public List<Partida> traerHistorialDeJugador(String jugador) {
        return repositorio.obtenerPorJugador(jugador);
    }


}