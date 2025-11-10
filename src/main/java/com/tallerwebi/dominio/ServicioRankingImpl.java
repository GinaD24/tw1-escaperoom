package com.tallerwebi.dominio;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.tallerwebi.dominio.entidad.Partida;
import com.tallerwebi.dominio.entidad.PuestoRankingDTO;
import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.enums.Dificultad;
import com.tallerwebi.dominio.excepcion.SalaSinRanking;
import com.tallerwebi.dominio.interfaz.repositorio.RepositorioRanking;
import com.tallerwebi.dominio.interfaz.servicio.ServicioRanking;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;

@Service
@Transactional
public class ServicioRankingImpl implements ServicioRanking {

    private final RepositorioRanking repositorioRanking;


    @Autowired
    public ServicioRankingImpl(RepositorioRanking repositorioRanking) {
        this.repositorioRanking = repositorioRanking;
    }


    @Override
    public List<PuestoRankingDTO> obtenerRanking() {
        List<Partida> listaDePartidas = this.repositorioRanking.obtenerTodasLasPartidasGanadas();

        if (listaDePartidas.isEmpty()) {
            throw new SalaSinRanking();
        }

        // 2. Agrupar por usuario y elegir su mejor partida según puntaje ponderado
        Map<Usuario, Partida> mejorPartidaPorUsuario = listaDePartidas.stream()
                .collect(Collectors.toMap(
                        Partida::getUsuario,
                        p -> p,
                        (p1, p2) -> calcularPuntajePonderado(p1) >= calcularPuntajePonderado(p2) ? p1 : p2
                ));

        // 3. Convertir a DTO y ordenar por el puntaje ponderado
        List<PuestoRankingDTO> rankingGlobal = mejorPartidaPorUsuario.values().stream()
                .map(p -> new PuestoRankingDTO(
                        p.getSala(),
                        p.getPuntaje(),
                        p.getUsuario(),
                        p.getTiempoTotal(),
                        p.getPistasUsadas(),
                        calcularPuntajePonderado(p) // no se muestra, solo para ordenar
                ))
                .sorted(Comparator.comparing(PuestoRankingDTO::getPuntajeCalculado).reversed())
                .collect(Collectors.toList());

        // 4. Asignar los puestos
        for (int i = 0; i < rankingGlobal.size(); i++) {
            rankingGlobal.get(i).setPuesto(i + 1);
        }

        return rankingGlobal;
    }


    public Double calcularPuntajePonderado(Partida partida) {
        double puntajeMaximo = partida.getSala().getCantidadDeEtapas() * 100;
        double rendimiento = (partida.getPuntaje() / puntajeMaximo); // entre 0 y 1

        double factorDificultad = obtenerFactorDificultad(partida.getSala().getDificultad());

        double penalizacionPistas = 1.0 / (1 + partida.getPistasUsadas() * 0.1);

        double duracionSalaEnSegundos = partida.getSala().getDuracion() * 60.0;
        double ratio = partida.getTiempoTotal() / duracionSalaEnSegundos;
        if (ratio > 1) ratio = 1; // por si acaso alguien se pasa del límite
        double bonusTiempo = 0.7 + (1 - ratio) * 0.3;

        return rendimiento * factorDificultad * penalizacionPistas * bonusTiempo * 100.0;
    }


    private double obtenerFactorDificultad(Dificultad dificultad) {
        switch (dificultad) {
            case PRINCIPIANTE: return 1.0;
            case INTERMEDIO: return 1.1;
            case AVANZADO: return 1.25;
        }
        return 0;
    }


}