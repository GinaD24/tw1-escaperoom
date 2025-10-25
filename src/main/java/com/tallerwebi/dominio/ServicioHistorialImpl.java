package com.tallerwebi.dominio;

import com.tallerwebi.dominio.entidad.Historial;
import com.tallerwebi.dominio.entidad.Ranking;
import com.tallerwebi.dominio.excepcion.SalaInexistente;
import com.tallerwebi.dominio.interfaz.repositorio.RepositorioHistorial;
import com.tallerwebi.dominio.interfaz.servicio.ServicioHistorial;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Comparator;
import java.util.List;

@Service
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

    @Override
    @Transactional
    public List<Historial> obtenerHistorialPorSala(Integer idSala) {
        List<Historial> historials = this.repositorio.ObtenerHistorialPorSala(idSala);
        if (historials.isEmpty()) {
            throw new SalaInexistente();
        }

        historials.sort(Comparator
                .comparing(Historial::getFecha).reversed()
        );

        return historials;
    }
}