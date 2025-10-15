package com.tallerwebi.dominio;

import java.util.Comparator;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;

@Service
public class ServicioRankingImpl implements ServicioRanking {

    private final RankingRepository rankingRepository;


    @Autowired
    public ServicioRankingImpl(@Qualifier("rankingRepositoryImpl") RankingRepository rankingRepository) {
        this.rankingRepository = rankingRepository;
    }

    @Override
    @Transactional
    public void agregarRanking(Ranking nuevoRanking) {
        this.rankingRepository.guardar(nuevoRanking);
    }

    @Override
    @Transactional
    public void actualizarRanking(Ranking nuevoRanking) {
        Ranking rankingExistente = this.rankingRepository.buscarPorIdDeSalaYNombreDeUsuario(nuevoRanking.getIdSala(), nuevoRanking.getNombreUsuario());

        if (rankingExistente != null) {
            if (esMejorRanking(nuevoRanking, rankingExistente)) {
                this.rankingRepository.guardar(nuevoRanking);
            }
        } else {
            this.rankingRepository.guardar(nuevoRanking);
        }
    }

    @Override
    @Transactional
    public List<Ranking> obtenerRankingPorSala(Integer idSala) {
        List<Ranking> rankingsDeSala = this.rankingRepository.obtenerRankingPorSala(idSala);

        rankingsDeSala.sort(Comparator
                .comparing(Ranking::getTiempoFinalizacion)
                .thenComparing(Ranking::getCantidadPistas));

        return rankingsDeSala;
    }

    private boolean esMejorRanking(Ranking nuevoRanking, Ranking rankingExistente) {
        if (nuevoRanking.getTiempoFinalizacion() < rankingExistente.getTiempoFinalizacion()) {
            return true;
        }
        if (nuevoRanking.getTiempoFinalizacion().equals(rankingExistente.getTiempoFinalizacion()) &&
            nuevoRanking.getCantidadPistas() < rankingExistente.getCantidadPistas()) {
            return true;
        }
        return false;
    }
}