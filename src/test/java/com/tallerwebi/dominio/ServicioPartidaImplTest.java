package com.tallerwebi.dominio;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ServicioPartidaImplTest {

    RepositorioPartida repositorio;
    ServicioPartida servicio;

    @BeforeEach
    public void init() {
        repositorio = mock(RepositorioPartida.class);
        servicio = new ServicioPartidaImpl(repositorio);
    }

    @Test
    void dadoQueHayUnaPartidaCuandoLaRegistroEntoncesSeInvocaRepositorioGuardar() {
        Partida partida = new Partida(1, "Alan", "Sala1", LocalDateTime.now(), true);

        servicio.registrarPartida(partida);

        verify(repositorio).guardar(partida);
    }

    @Test
    void dadoQueHayPartidasDeUnJugadorCuandoTraigoSuHistorialEntoncesSeInvocaRepositorioObtenerPorJugador() {
        Partida p1 = new Partida(1, "Alan", "Sala1", LocalDateTime.now(), true);
        Partida p2 = new Partida(2, "Alan", "Sala2", LocalDateTime.now(), false);
        List<Partida> partidas = Arrays.asList(p1, p2);
        when(repositorio.obtenerPorJugador("Alan")).thenReturn(partidas);

        List<Partida> resultado = servicio.traerHistorialDeJugador("Alan");

        verify(repositorio).obtenerPorJugador("Alan");
        assertThat(resultado.size(), is(2));
        assertThat(resultado.get(0).getSala(), is("Sala1"));
    }

    @Test
    void dadoQueNoHayPartidasCuandoTraigoElHistorialCompletoEntoncesDevuelvoListaVacia() {
        when(repositorio.obtenerTodas()).thenReturn(new ArrayList<>());

        List<Partida> resultado = servicio.traerHistorial();

        verify(repositorio).obtenerTodas();
        assertThat(resultado.size(), is(0));
    }

    @Test
    void dadoQueHayMultiplesPartidasCuandoTraigoElHistorialCompletoEntoncesDevuelvoTodas() {
        Partida p1 = new Partida(1, "Alan", "Sala1", LocalDateTime.now(), true);
        Partida p2 = new Partida(2, "Alan", "Sala2", LocalDateTime.now(), false);
        Partida p3 = new Partida(3, "Alan", "Sala3", LocalDateTime.now(), true);
        List<Partida> partidas = Arrays.asList(p1, p2, p3);
        when(repositorio.obtenerTodas()).thenReturn(partidas);

        List<Partida> resultado = servicio.traerHistorial();

        verify(repositorio).obtenerTodas();
        assertThat(resultado.size(), is(3));
        assertThat(resultado.get(0).getJugador(), is("Alan"));
        assertThat(resultado.get(1).getJugador(), is("Alan"));
    }

    @Test
    void dadoQueNoHayPartidasDeUnJugadorCuandoTraigoSuHistorialEntoncesDevuelvoListaVacia() {
        when(repositorio.obtenerPorJugador("Alan")).thenReturn(new ArrayList<>());

        List<Partida> resultado = servicio.traerHistorialDeJugador("Alan");

        verify(repositorio).obtenerPorJugador("Alan");
        assertThat(resultado.size(), is(0));
    }

    @Test
    void dadoQueRegistroMultiplesPartidasCuandoTraigoElHistorialEntoncesSeReflejanTodas() {
        Partida p1 = new Partida(1, "Alan", "Sala1", LocalDateTime.now(), true);
        Partida p2 = new Partida(2, "Alan", "Sala2", LocalDateTime.now(), false);
        List<Partida> historialEsperado = Arrays.asList(p1, p2);
        when(repositorio.obtenerTodas()).thenReturn(historialEsperado);

        servicio.registrarPartida(p1);
        servicio.registrarPartida(p2);
        List<Partida> resultado = servicio.traerHistorial();

        verify(repositorio, times(2)).guardar(any(Partida.class));
        verify(repositorio).obtenerTodas();
        assertThat(resultado.size(), is(2));
    }

    @Test
    void dadoQueHayVariasPartidasCuandoTraigoElHistorialEntoncesLaPartidaMasRecienteAparecePrimera() {
        Partida partidaAntigua = new Partida(1, "Alan", "Sala1", LocalDateTime.of(2023, 1, 1, 10, 0), true);
        Partida partidaReciente = new Partida(2, "Alan", "Sala2", LocalDateTime.of(2023, 6, 1, 15, 0), false);
        Partida partidaIntermedia = new Partida(3, "Alan", "Sala3", LocalDateTime.of(2023, 3, 1, 12, 0), true);
        List<Partida> partidasDesordenadas = Arrays.asList(partidaAntigua, partidaReciente, partidaIntermedia);
        when(repositorio.obtenerTodas()).thenReturn(partidasDesordenadas);

        List<Partida> resultado = servicio.traerHistorial();

        assertThat(resultado.get(0), is(equalTo(partidaReciente)));
        assertThat(resultado.get(1), is(equalTo(partidaIntermedia)));
        assertThat(resultado.get(2), is(equalTo(partidaAntigua)));
    }
}
