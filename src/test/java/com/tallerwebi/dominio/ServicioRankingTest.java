package com.tallerwebi.dominio;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.tallerwebi.dominio.excepcion.SalaInexistente;
import com.tallerwebi.dominio.excepcion.SesionDeUsuarioExpirada;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ServicioRankingTest {
    
    private ServicioRankingImpl servicioRanking;
    private RankingRepository rankingRepository;

    @BeforeEach
    public void init() {
        this.rankingRepository = mock(RankingRepository.class);
        this.servicioRanking = new ServicioRankingImpl(rankingRepository);
    }

    @Test
    public void dadoQueExisteUnRankingPuedoAgregarNuevosRankingsDeJugadores(){
        
       Integer idSala = 1;
       when(rankingRepository.buscarPorIdDeSalaYNombreDeUsuario(idSala, "Ruben")).thenReturn(null);

       Ranking nuevoRanking = new Ranking(idSala, 600L, "Ruben", 35.0, 2, LocalDate.now(), new ArrayList<>());

       servicioRanking.agregarRanking(nuevoRanking);

       verify(rankingRepository, times(1)).guardar(nuevoRanking);
    }



    @Test
    public void dadoQueExisteUnRankingPuedoObtenerRankingPorSalaQueDevuelveUnaListaDeRankingsOrdenados() {
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


    @Test
    public void dadoQueExisteUnRankingSiTengoUnPuntajeMejorDeUnJugadorElRankingSeActualiza(){
        Integer idSala = 2;

        Ranking antiguoRanking = new Ranking(idSala, 700L, "Martina", 25.0, 3, LocalDate.now(), new ArrayList<>());

        when(rankingRepository.buscarPorIdDeSalaYNombreDeUsuario(idSala, "Martina")).thenReturn(antiguoRanking);

        Ranking nuevoRanking = new Ranking(idSala, 1000L, "Martina", 10.0, 1, LocalDate.now(), new ArrayList<>());

        servicioRanking.actualizarRanking(nuevoRanking);
        verify(rankingRepository, times(1)).guardar(nuevoRanking);
    }


    @Test
    public void dadoQueExisteUnRankingSiNoExistenPuntajesSeDevuelveUnaListaVacia(){
        Integer idSala = 1;
        List<Ranking> rankingVacio = new ArrayList<>();

        when(rankingRepository.obtenerRankingPorSala(idSala)).thenReturn(rankingVacio);

        assertThrows(SalaInexistente.class, () -> {
            this.servicioRanking.obtenerRankingPorSala(idSala);
        });
        assertThat(rankingVacio, is(empty()));
    }

    @Test
    public void dadoQueExisteUnRankingSi2JugadoresTienenElMismoPuntajeSeDecidePorTiempoFinalizacionYPistasUtilizadas(){
        Integer idSala = 1;
        List<Ranking> rankingsDesordenados = new ArrayList<>();

        Ranking puntaje1 = new Ranking(idSala, 800L,"Clara", 25.0, 3, LocalDate.now(), new ArrayList<>());
        Ranking puntaje2 = new Ranking(idSala, 800L, "Mauro", 24.0, 2, LocalDate.now(), new ArrayList<>());

        rankingsDesordenados.add(puntaje1);
        rankingsDesordenados.add(puntaje2);
        
        when(rankingRepository.obtenerRankingPorSala(idSala)).thenReturn(rankingsDesordenados);

        List<Ranking> rankingsOrdenados = servicioRanking.obtenerRankingPorSala(idSala);

        assertThat(rankingsOrdenados.get(0).getNombreUsuario(), is("Mauro"));
        assertThat(rankingsOrdenados.get(1).getNombreUsuario(), is("Clara"));
    }

}
