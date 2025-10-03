package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.Ranking;
import com.tallerwebi.dominio.ServicioRanking;
import com.tallerwebi.dominio.excepcion.NoExisteSala;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.cglib.core.Local;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

public class ControladorRankingTest {

    ControladorRanking controladorRanking;
    ServicioRanking servicioRanking;

    @BeforeEach
    public void init() {
        this.servicioRanking = mock(ServicioRanking.class);
        this.controladorRanking = new ControladorRanking(servicioRanking);
    }


    @Test
    public void dadoQueExisteUnaVistaRankingCuandoPidoQueLaMuestreLaDevuelve(){
        Integer idSala = 1;
        List<Ranking> rankingsDePrueba = new ArrayList<>();
        rankingsDePrueba.add(new Ranking(idSala, 900L, "Martina", 30.0, 1, LocalDate.now(), null));
        rankingsDePrueba.add(new Ranking(idSala, 800L, "Franco", 35.0, 2, LocalDate.now(), null));

        when(servicioRanking.obtenerRankingPorSala(idSala)).thenReturn(rankingsDePrueba);

        ModelAndView modelAndView = controladorRanking.verRankings(idSala);

        assertThat(modelAndView.getViewName(), equalToIgnoringCase("ranking-sala"));
        List<Ranking> rankingsEnElModelo = (List<Ranking>) modelAndView.getModel().get("rankings");
        assertThat(rankingsEnElModelo, notNullValue());
        assertThat(rankingsEnElModelo, hasSize(2));
        assertThat(rankingsEnElModelo.get(0).getNombreUsuario(), equalTo("Martina"));

    }

    @Test
    public void dadoQueExisteUnaVistaRankingPuedoDevolerUnaListaVacia(){
        Integer idSala = 1;
        List<Ranking> rankingsVacios = new ArrayList<>();
        when(servicioRanking.obtenerRankingPorSala(idSala)).thenReturn(rankingsVacios);

        ModelAndView modelAndView = controladorRanking.verRankings(idSala);

        assertThat(modelAndView.getViewName(), equalToIgnoringCase("ranking-sala"));
        List<Ranking> rankingsEnElModelo = (List<Ranking>) modelAndView.getModel().get("rankings");
        assertThat(rankingsEnElModelo, hasSize(0));
    }

    @Test
    public void dadoQueExisteUnaVistaRankingPuedoVerUnRankingCompleto(){
        Integer idSala = 1;
        List<Ranking> rankingsDePrueba = new ArrayList<>();
        rankingsDePrueba.add(new Ranking(idSala, 900L, "Martina", 30.0, 1, LocalDate.now(), null));
        rankingsDePrueba.add(new Ranking(idSala, 800L, "Franco", 35.0, 2, LocalDate.now(), null));

        when(servicioRanking.obtenerRankingPorSala(idSala)).thenReturn(rankingsDePrueba);

        ModelAndView modelAndView = controladorRanking.verRankings(idSala);

        assertThat(modelAndView.getViewName(), equalToIgnoringCase("ranking-sala"));
        List<Ranking> rankingsEnElModelo = (List<Ranking>) modelAndView.getModel().get("rankings");
        assertThat(rankingsEnElModelo.get(0).getNombreUsuario(), is("Martina"));
        assertThat(rankingsEnElModelo.get(1).getNombreUsuario(), is("Franco"));
    }


    @Test
    public void dadoQuExisteUnaVistaRankingCuandoQuieroVerUnaSalaQueNoExisteDevuelveError(){
        Integer idSala = -1;

        when(servicioRanking.obtenerRankingPorSala(idSala)).thenThrow(new RuntimeException());

        ModelAndView modelAndView = controladorRanking.verRankings(idSala);

        assertThat(modelAndView.getViewName(), equalToIgnoringCase("error-ranking"));

        String mensajeError = (String) modelAndView.getModel().get("error");
        assertThat(mensajeError, equalToIgnoringCase("ID de Sala invalido"));

    }

    @Test
    public void dadoQueExisteUnaVistaRankingNoPuedoIngresarAUnaSalaConIDNulo(){
        ModelAndView modelAndView = controladorRanking.verRankings(null);

        assertThat(modelAndView.getViewName(), equalToIgnoringCase("error-ranking"));

        String mensajeError = (String) modelAndView.getModel().get("error");
        assertThat(mensajeError, equalToIgnoringCase("ID de Sala invalido"));

    }

    @Test
    public void dadoQueExisteUnaVistaRankingSiHayUnErrorInternoDevuelveError(){
        Integer idSala = 1;

        when(servicioRanking.obtenerRankingPorSala(idSala)).thenThrow(new RuntimeException());

        ModelAndView modelAndView = controladorRanking.verRankings(idSala);

        assertThat(modelAndView.getViewName(), equalToIgnoringCase("error-ranking"));
        String mensajeError = (String) modelAndView.getModel().get("error");
        assertThat(mensajeError, equalToIgnoringCase("Error interno al cargar los rankings"));
    }

    @Test
    public void dadoQueExisteUnaVistaRankingObtengoLosPuntajesOrdenados(){
        Integer idSala = 1;

        List<Ranking> rankingsDePrueba = new ArrayList<>();
        rankingsDePrueba.add(new Ranking(idSala, 1200L, "Alan", 25.0, 1, LocalDate.now(), null));
        rankingsDePrueba.add(new Ranking(idSala, 1100L, "Maria", 25.0, 1, LocalDate.now(), null));
        rankingsDePrueba.add(new Ranking(idSala, 900L, "Sofia", 35.0, 2, LocalDate.now(), null));
        rankingsDePrueba.add(new Ranking(idSala, 800L, "Franco", 30.0, 2, LocalDate.now(), null));

        when(servicioRanking.obtenerRankingPorSala(idSala)).thenReturn(rankingsDePrueba);

        ModelAndView modelAndView = controladorRanking.verRankings(idSala);

        assertThat(modelAndView.getViewName(), equalToIgnoringCase("ranking-sala"));

        List<Ranking> rankingsEnElModelo = (List<Ranking>) modelAndView.getModel().get("rankings");

        assertThat(rankingsEnElModelo.get(0).getNombreUsuario(), is("Alan"));
        assertThat(rankingsEnElModelo.get(1).getNombreUsuario(), is("Maria"));
        assertThat(rankingsEnElModelo.get(2).getNombreUsuario(), is("Sofia"));
        assertThat(rankingsEnElModelo.get(3).getNombreUsuario(), is("Franco"));

    }


}
