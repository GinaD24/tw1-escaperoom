package com.tallerwebi.presentacion;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import com.tallerwebi.dominio.Historial;
import com.tallerwebi.dominio.ServicioHistorial;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ControladorHistorialTest {

    ServicioHistorial servicio;
    ControladorPartida controlador;

    @BeforeEach
    public void init() {
        servicio = mock(ServicioHistorial.class);
        controlador = new ControladorPartida(servicio);
    }

    @Test
    void dadoQueHayUnaPartidaCuandoLaRegistroEnControladorEntoncesSeInvocaServicioRegistrar() {

        Historial historial = new Historial(1, "Alan", "Sala1", LocalDateTime.now(), true);

        controlador.registrar(historial);

        verify(servicio).registrarPartida(historial);
    }

    @Test
    void dadoQueHayPartidasCuandoTraigoElHistorialCompletoEntoncesDevuelvoTodas() {

        Historial p1 = new Historial(1, "Alan", "Sala1", LocalDateTime.now(), true);
        Historial p2 = new Historial(2, "Alan", "Sala2", LocalDateTime.now(), false);
        List<Historial> historials = Arrays.asList(p1, p2);
        when(servicio.traerHistorial()).thenReturn(historials);

        List<Historial> resultado = controlador.verHistorial();

        verify(servicio).traerHistorial();
        assertThat(resultado.size(), is(2));
    }

    @Test
    void dadoQueNoHayPartidasCuandoTraigoElHistorialCompletoEntoncesDevuelvoListaVacia() {
        when(servicio.traerHistorial()).thenReturn(new ArrayList<>());

        List<Historial> resultado = controlador.verHistorial();

        verify(servicio).traerHistorial();
        assertThat(resultado.size(), is(0));
    }

    @Test
    void dadoQueHayPartidasDeUnJugadorCuandoTraigoSuHistorialEntoncesDevuelvoLasCorrectas() {
        Historial p1 = new Historial(1, "Alan", "Sala1", LocalDateTime.now(), true);
        Historial p2 = new Historial(2, "Alan", "Sala2", LocalDateTime.now(), false);
        List<Historial> historials = Arrays.asList(p1, p2);
        when(servicio.traerHistorialDeJugador("Alan")).thenReturn(historials);

        List<Historial> resultado = controlador.verHistorialJugador("Alan");

        verify(servicio).traerHistorialDeJugador("Alan");
        assertThat(resultado.size(), is(2));
        assertThat(resultado.get(0).getGano(), is(true));
        assertThat(resultado.get(1).getGano(), is(false));
    }

    @Test
    void dadoQueNoHayPartidasDeUnJugadorCuandoTraigoSuHistorialEntoncesDevuelvoListaVacia() {
        when(servicio.traerHistorialDeJugador("Alan")).thenReturn(new ArrayList<>());

        List<Historial> resultado = controlador.verHistorialJugador("Alan");

        verify(servicio).traerHistorialDeJugador("Alan");
        assertThat(resultado.size(), is(0));
    }

    @Test
    void dadoQueRegistroMultiplesPartidasCuandoTraigoElHistorialEntoncesSeMuestranTodas() {
        Historial p1 = new Historial(1, "Alan", "Sala1", LocalDateTime.now(), true);
        Historial p2 = new Historial(2, "Alan", "Sala2", LocalDateTime.now(), false);
        List<Historial> historialEsperado = Arrays.asList(p1, p2);
        when(servicio.traerHistorial()).thenReturn(historialEsperado);

        controlador.registrar(p1);
        controlador.registrar(p2);
        List<Historial> resultado = controlador.verHistorial();

        verify(servicio, times(2)).registrarPartida(any(Historial.class));
        verify(servicio).traerHistorial();
        assertThat(resultado.size(), is(2));
    }
}
