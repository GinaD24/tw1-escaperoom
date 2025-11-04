package com.tallerwebi.dominio;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.tallerwebi.dominio.entidad.Partida;
import com.tallerwebi.dominio.entidad.PuestoRanking;
import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.excepcion.SalaSinRanking;
import com.tallerwebi.dominio.interfaz.repositorio.RepositorioRanking;
import com.tallerwebi.dominio.interfaz.servicio.ServicioRanking;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;

@Service
public class ServicioRankingImpl implements ServicioRanking {

    private final RepositorioRanking repositorioRanking;


    @Autowired
    public ServicioRankingImpl(RepositorioRanking repositorioRanking) {
        this.repositorioRanking = repositorioRanking;
    }


    @Override
    @Transactional
    public List<PuestoRanking> obtenerRankingPorSala(Integer idSala) {
        // Obtener todas las partidas de la sala
        List<Partida> listaDePartidas = this.repositorioRanking.obtenerPartidasPorSala(idSala);

        if (listaDePartidas.isEmpty()) {
            throw new SalaSinRanking();
        }

        // Mapear a un Map<Usuario, Partida> para quedarnos solo con la mejor partida de cada usuario
        Map<Usuario, Partida> mejorPartidaPorUsuario = listaDePartidas.stream()
                .collect(Collectors.toMap(
                        Partida::getUsuario,
                        p -> p,
                        (p1, p2) -> { // si hay más de una partida del mismo usuario, quedarnos con la mejor
                            if (p1.getPuntaje() > p2.getPuntaje()) return p1;
                            if (p1.getPuntaje() < p2.getPuntaje()) return p2;
                            // si el puntaje es igual, quedarse con menor tiempo
                            return p1.getTiempoTotal() <= p2.getTiempoTotal() ? p1 : p2;
                        }
                ));

        // Transformar a Ranking y ordenar
        List<PuestoRanking> rankingsDeSala = mejorPartidaPorUsuario.values().stream()
                .map(partida -> new PuestoRanking(
                        partida.getSala().getId(),
                        partida.getPuntaje(),
                        partida.getUsuario(),
                        partida.getTiempoTotal(),
                        partida.getPistasUsadas()))
                .sorted(Comparator
                        .comparing(PuestoRanking::getPuntaje).reversed()
                        .thenComparing(PuestoRanking::getTiempoTotal)
                        .thenComparing(PuestoRanking::getCantidadPistas)
                )
                .collect(Collectors.toList());

        return rankingsDeSala;
    }


}