package com.tallerwebi.dominio;

import java.util.List;

public class ServicioHistorialImpl implements ServicioHistorial {

    private RepositorioHistorial repositorio;

    public ServicioHistorialImpl(RepositorioHistorial repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    public void registrarPartida(Historial historial) {
        repositorio.guardar(historial);
    }

    @Override
    public List<Historial> traerHistorial() {
        List<Historial> historials = repositorio.obtenerTodas();
        historials.sort((p1, p2) -> p2.getFecha().compareTo(p1.getFecha())); // orden descendente por fecha
        return historials;
    }

    @Override
    public List<Historial> traerHistorialDeJugador(String jugador) {
        return repositorio.obtenerPorJugador(jugador);
    }


}