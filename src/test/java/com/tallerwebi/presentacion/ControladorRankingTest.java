package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.Ranking;
import com.tallerwebi.dominio.Sala;
import com.tallerwebi.dominio.ServicioRanking;
import com.tallerwebi.dominio.ServicioSala;
import com.tallerwebi.dominio.excepcion.SalaInexistente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

public class ControladorRankingTest {

    ControladorRanking controladorRanking;
    ServicioRanking servicioRanking;
    ServicioSala servicioSala;

    @BeforeEach
    public void init() {
        this.servicioRanking = mock(ServicioRanking.class);
        this.servicioSala = mock(ServicioSala.class);
        this.controladorRanking = new ControladorRanking(servicioRanking, servicioSala);
    }


    @Test
    public void dadoQueExisteUnaVistaRankingCuandoPidoQueLaMuestreLaDevuelve(){
        Integer idSala = 1;
        List<Ranking> rankingsDePrueba = new ArrayList<>();
        rankingsDePrueba.add(new Ranking(idSala, 900L, "Martina", 30.0, 1, LocalDate.now(), null));
        rankingsDePrueba.add(new Ranking(idSala, 800L, "Franco", 35.0, 2, LocalDate.now(), null));
        List<Sala> salas = new ArrayList<>();
        Sala sala = new Sala();
        sala.setId(idSala);
        salas.add(sala);
        when(servicioRanking.obtenerRankingPorSala(idSala)).thenReturn(rankingsDePrueba);
        when(servicioSala.traerSalas()).thenReturn((ArrayList<Sala>) salas);
        when(servicioSala.obtenerSalaPorId(idSala)).thenReturn(sala);

        ModelAndView modelAndView = controladorRanking.verRankings(idSala);

        assertThat(modelAndView.getViewName(), equalToIgnoringCase("ranking-sala"));
        List<Ranking> rankingsEnElModelo = (List<Ranking>) modelAndView.getModel().get("rankings");
        assertThat(rankingsEnElModelo, notNullValue());
        assertThat(rankingsEnElModelo, hasSize(2));
        assertThat(rankingsEnElModelo.get(0).getNombreUsuario(), equalTo("Martina"));

        verify(servicioRanking).obtenerRankingPorSala(idSala);
        verify(servicioSala).traerSalas();
        verify(servicioSala).obtenerSalaPorId(idSala);

    }

    @Test
    public void dadoQueExisteUnaVistaRankingPuedoDevolerUnaListaVacia(){
        Integer idSala = 1;
        List<Ranking> rankingsVacios = new ArrayList<>();
        when(servicioRanking.obtenerRankingPorSala(idSala)).thenReturn(rankingsVacios);
        List<Sala> salas = new ArrayList<>();
        Sala sala = new Sala();
        sala.setId(idSala);
        salas.add(sala);
        ModelAndView modelAndView = controladorRanking.verRankings(idSala);
        when(servicioSala.traerSalas()).thenReturn((ArrayList<Sala>) salas);
        when(servicioSala.obtenerSalaPorId(idSala)).thenReturn(sala);

        assertThat(modelAndView.getViewName(), equalToIgnoringCase("ranking-sala"));
        List<Ranking> rankingsEnElModelo = (List<Ranking>) modelAndView.getModel().get("rankings");
        assertThat(rankingsEnElModelo, hasSize(0));
        verify(servicioRanking).obtenerRankingPorSala(idSala);
        verify(servicioSala).traerSalas();
        verify(servicioSala).obtenerSalaPorId(idSala);
    }

    @Test
    public void dadoQueExisteUnaVistaRankingPuedoVerUnRankingCompleto(){
        Integer idSala = 1;
        List<Ranking> rankingsDePrueba = new ArrayList<>();
        rankingsDePrueba.add(new Ranking(idSala, 900L, "Martina", 30.0, 1, LocalDate.now(), null));
        rankingsDePrueba.add(new Ranking(idSala, 800L, "Franco", 35.0, 2, LocalDate.now(), null));

        when(servicioRanking.obtenerRankingPorSala(idSala)).thenReturn(rankingsDePrueba);

        ModelAndView modelAndView = controladorRanking.verRankings(idSala);
        List<Sala> salas = new ArrayList<>();
        Sala sala = new Sala();
        sala.setId(idSala);
        salas.add(sala);
        when(servicioSala.traerSalas()).thenReturn((ArrayList<Sala>) salas);

        assertThat(modelAndView.getViewName(), equalToIgnoringCase("ranking-sala"));
        List<Ranking> rankingsEnElModelo = (List<Ranking>) modelAndView.getModel().get("rankings");
        assertThat(rankingsEnElModelo.get(0).getNombreUsuario(), is("Martina"));
        assertThat(rankingsEnElModelo.get(1).getNombreUsuario(), is("Franco"));
    }



    @Test
    public void deberiaLanzarLaExcepcionSalaInexistenteCuandoRecibeUnIdNull(){

        Sala sala = new Sala();
        doThrow(SalaInexistente.class).when(servicioRanking).obtenerRankingPorSala(null);

        ModelAndView modelAndView = controladorRanking.verRankings(null);
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("ranking-sala"));
        String mensajeError = (String) modelAndView.getModel().get("error");
        assertThat(mensajeError, equalToIgnoringCase("Sala no encontrada."));

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

    @Test
    public void dadoQueExisteUnaVistaRankingPuedoFiltrarlosPorSala(){
        Integer idSala = 1;
        List<Sala> salas = new ArrayList<>();
        Sala sala = new Sala();
        sala.setId(idSala);
        salas.add(sala);

        List<Ranking> rankingDeSala1 = new ArrayList<>();
        rankingDeSala1.add(new Ranking(1, 1200L, "Alan", 25.0, 1, LocalDate.now(), null));
        rankingDeSala1.add(new Ranking(1, 1100L, "Maria", 25.0, 1, LocalDate.now(), null));

        List<Ranking> rankingDeSala2 = new ArrayList<>();
        rankingDeSala2.add(new Ranking(2, 900L, "Sofia", 35.0, 2, LocalDate.now(), null));
        rankingDeSala2.add(new Ranking(2, 800L, "Franco", 30.0, 2, LocalDate.now(), null));

        when(servicioRanking.obtenerRankingPorSala(idSala)).thenReturn(rankingDeSala1);
        when(servicioSala.traerSalas()).thenReturn((ArrayList<Sala>) salas);
        when(servicioSala.obtenerSalaPorId(idSala)).thenReturn(sala);
        ModelAndView modelAndView = controladorRanking.filtrarRanking("1");

        List<Sala> rankingObtenidoDeSala1 = (List<Sala>) modelAndView.getModel().get("rankings");

        assertThat(rankingObtenidoDeSala1, equalTo(rankingDeSala1));
        verify(servicioRanking).obtenerRankingPorSala(idSala);
        verify(servicioSala).traerSalas();
        verify(servicioSala).obtenerSalaPorId(idSala);



    }


}
