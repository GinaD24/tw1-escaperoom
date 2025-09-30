package com.tallerwebi.presentacion;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import com.tallerwebi.dominio.Partida;
import com.tallerwebi.dominio.ServicioPartida;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ControladorPartidaTest {

    ServicioPartida servicio;
    ControladorPartida controlador;

    @BeforeEach
    public void init() {
        servicio = mock(ServicioPartida.class);
        controlador = new ControladorPartida(servicio);
    }

    @Test
    void dadoQueHayUnaPartidaCuandoLaRegistroEnControladorEntoncesSeInvocaServicioRegistrar() {

        Partida partida = new Partida(1, "Alan", "Sala1", LocalDateTime.now(), true);

        controlador.registrar(partida);

        verify(servicio).registrarPartida(partida);
    }

    @Test
    void dadoQueHayPartidasCuandoTraigoElHistorialCompletoEntoncesDevuelvoTodas() {

        Partida p1 = new Partida(1, "Alan", "Sala1", LocalDateTime.now(), true);
        Partida p2 = new Partida(2, "Alan", "Sala2", LocalDateTime.now(), false);
        List<Partida> partidas = Arrays.asList(p1, p2);
        when(servicio.traerHistorial()).thenReturn(partidas);

        List<Partida> resultado = controlador.verHistorial();

        verify(servicio).traerHistorial();
        assertThat(resultado.size(), is(2));
    }

    @Test
    void dadoQueNoHayPartidasCuandoTraigoElHistorialCompletoEntoncesDevuelvoListaVacia() {
        when(servicio.traerHistorial()).thenReturn(new ArrayList<>());

        List<Partida> resultado = controlador.verHistorial();

        verify(servicio).traerHistorial();
        assertThat(resultado.size(), is(0));
    }

    @Test
    void dadoQueHayPartidasDeUnJugadorCuandoTraigoSuHistorialEntoncesDevuelvoLasCorrectas() {
        Partida p1 = new Partida(1, "Alan", "Sala1", LocalDateTime.now(), true);
        Partida p2 = new Partida(2, "Alan", "Sala2", LocalDateTime.now(), false);
        List<Partida> partidas = Arrays.asList(p1, p2);
        when(servicio.traerHistorialDeJugador("Alan")).thenReturn(partidas);

        List<Partida> resultado = controlador.verHistorialJugador("Alan");

        verify(servicio).traerHistorialDeJugador("Alan");
        assertThat(resultado.size(), is(2));
        assertThat(resultado.get(0).getGano(), is(true));
        assertThat(resultado.get(1).getGano(), is(false));
    }

    @Test
    void dadoQueNoHayPartidasDeUnJugadorCuandoTraigoSuHistorialEntoncesDevuelvoListaVacia() {
        when(servicio.traerHistorialDeJugador("Alan")).thenReturn(new ArrayList<>());

        List<Partida> resultado = controlador.verHistorialJugador("Alan");

        verify(servicio).traerHistorialDeJugador("Alan");
        assertThat(resultado.size(), is(0));
    }

    @Test
    void dadoQueRegistroMultiplesPartidasCuandoTraigoElHistorialEntoncesSeMuestranTodas() {
        Partida p1 = new Partida(1, "Alan", "Sala1", LocalDateTime.now(), true);
        Partida p2 = new Partida(2, "Alan", "Sala2", LocalDateTime.now(), false);
        List<Partida> historialEsperado = Arrays.asList(p1, p2);
        when(servicio.traerHistorial()).thenReturn(historialEsperado);

        controlador.registrar(p1);
        controlador.registrar(p2);
        List<Partida> resultado = controlador.verHistorial();

        verify(servicio, times(2)).registrarPartida(any(Partida.class));
        verify(servicio).traerHistorial();
        assertThat(resultado.size(), is(2));
    }
}
