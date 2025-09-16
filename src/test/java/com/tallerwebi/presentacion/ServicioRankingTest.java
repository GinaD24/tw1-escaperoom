package com.tallerwebi.presentacion;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.tallerwebi.dominio.Ranking;
import com.tallerwebi.dominio.RankingRepository;
import com.tallerwebi.dominio.ServicioRankingImpl;

public class ServicioRankingTest {
    
    private ServicioRankingImpl servicioRanking;
    private RankingRepository rankingRepository;

    @BeforeEach
    public void init() {
        this.rankingRepository = mock(RankingRepository.class);
        this.servicioRanking = new ServicioRankingImpl(rankingRepository);
    }

    @Test
    public void obtenerRankingPorSalaDebeDevolverUnaListaDeRankingsOrdenados() {
        Integer idSala = 1;
        List<Ranking> rankingsDesordenados = new ArrayList<>();
        
        rankingsDesordenados.add(new Ranking(idSala, 800L, "Ana", 15.0, 2, LocalDate.now(), new ArrayList<>()));
        rankingsDesordenados.add(new Ranking(idSala, 1200L, "Carlos", 10.0, 1, LocalDate.now(), new ArrayList<>()));
        rankingsDesordenados.add(new Ranking(idSala, 1000L, "Diego", 12.0, 3, LocalDate.now(), new ArrayList<>()));

        when(rankingRepository.obtenerRankingPorSala(idSala)).thenReturn(rankingsDesordenados);
        
        List<Ranking> rankingsObtenidos = servicioRanking.obtenerRankingPorSala(idSala);
        
        assertThat(rankingsObtenidos, notNullValue());
        
        assertThat(rankingsObtenidos.get(0).getPuntaje(), is(1200L));
        assertThat(rankingsObtenidos.get(1).getPuntaje(), is(1000L));
        assertThat(rankingsObtenidos.get(2).getPuntaje(), is(800L));
    }

}
