package com.tallerwebi.dominio;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.*;

import com.tallerwebi.dominio.entidad.Historial;
import com.tallerwebi.dominio.interfaz.repositorio.RepositorioHistorial;
import com.tallerwebi.dominio.interfaz.servicio.ServicioHistorial;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ServicioHistorialImplTest {

    RepositorioHistorial repositorio;
    ServicioHistorial servicio;

    @BeforeEach
    public void init() {
        repositorio = mock(RepositorioHistorial.class);
        servicio = new ServicioHistorialImpl(repositorio);
    }

    @Test
    void dadoQueHayUnaPartidaCuandoLaRegistroEntoncesSeInvocaRepositorioGuardar() {
        Historial historial = new Historial(1, "Alan", 1, LocalDateTime.now(), true);

        servicio.registrarPartida(historial);

        verify(repositorio).guardar(historial);
    }

    @Test
    void dadoQueHayPartidasDeUnJugadorCuandoTraigoSuHistorialEntoncesSeInvocaRepositorioObtenerPorJugador() {
        Historial p1 = new Historial(1, "Alan", 1, LocalDateTime.now(), true);
        Historial p2 = new Historial(2, "Alan", 2, LocalDateTime.now(), false);
        List<Historial> historials = Arrays.asList(p1, p2);
        when(repositorio.obtenerPorJugador("Alan")).thenReturn(historials);

        List<Historial> resultado = servicio.traerHistorialDeJugador("Alan");

        verify(repositorio).obtenerPorJugador("Alan");
        assertThat(resultado.size(), is(2));
        assertThat(resultado.get(0).getIdSala(), is(1));
    }

    @Test
    void dadoQueNoHayPartidasCuandoTraigoElHistorialCompletoEntoncesDevuelvoListaVacia() {
        when(repositorio.obtenerTodas()).thenReturn(new ArrayList<>());

        List<Historial> resultado = servicio.traerHistorial();

        verify(repositorio).obtenerTodas();
        assertThat(resultado.size(), is(0));
    }

    @Test
    void dadoQueHayMultiplesPartidasCuandoTraigoElHistorialCompletoEntoncesDevuelvoTodas() {
        Historial p1 = new Historial(1, "Alan", 1, LocalDateTime.now(), true);
        Historial p2 = new Historial(2, "Alan", 2, LocalDateTime.now(), false);
        Historial p3 = new Historial(3, "Alan", 3, LocalDateTime.now(), true);
        List<Historial> historials = Arrays.asList(p1, p2, p3);
        when(repositorio.obtenerTodas()).thenReturn(historials);

        List<Historial> resultado = servicio.traerHistorial();

        verify(repositorio).obtenerTodas();
        assertThat(resultado.size(), is(3));
        assertThat(resultado.get(0).getJugador(), is("Alan"));
        assertThat(resultado.get(1).getJugador(), is("Alan"));
    }

    @Test
    void dadoQueNoHayPartidasDeUnJugadorCuandoTraigoSuHistorialEntoncesDevuelvoListaVacia() {
        when(repositorio.obtenerPorJugador("Alan")).thenReturn(new ArrayList<>());

        List<Historial> resultado = servicio.traerHistorialDeJugador("Alan");

        verify(repositorio).obtenerPorJugador("Alan");
        assertThat(resultado.size(), is(0));
    }

    @Test
    void dadoQueRegistroMultiplesPartidasCuandoTraigoElHistorialEntoncesSeReflejanTodas() {
        Historial p1 = new Historial(1, "Alan", 1, LocalDateTime.now(), true);
        Historial p2 = new Historial(2, "Alan", 2, LocalDateTime.now(), false);
        List<Historial> historialEsperado = Arrays.asList(p1, p2);
        when(repositorio.obtenerTodas()).thenReturn(historialEsperado);

        servicio.registrarPartida(p1);
        servicio.registrarPartida(p2);
        List<Historial> resultado = servicio.traerHistorial();

        verify(repositorio, times(2)).guardar(any(Historial.class));
        verify(repositorio).obtenerTodas();
        assertThat(resultado.size(), is(2));
    }

    @Test
    void dadoQueHayVariasPartidasCuandoTraigoElHistorialEntoncesLaPartidaMasRecienteAparecePrimera() {
        Historial historialAntigua = new Historial(1, "Alan", 1, LocalDateTime.of(2023, 1, 1, 10, 0), true);
        Historial historialReciente = new Historial(2, "Alan", 2, LocalDateTime.of(2023, 6, 1, 15, 0), false);
        Historial historialIntermedia = new Historial(3, "Alan", 3, LocalDateTime.of(2023, 3, 1, 12, 0), true);
        List<Historial> partidasDesordenadas = Arrays.asList(historialAntigua, historialReciente, historialIntermedia);
        when(repositorio.obtenerTodas()).thenReturn(partidasDesordenadas);

        List<Historial> resultado = servicio.traerHistorial();

        assertThat(resultado.get(0), is(equalTo(historialReciente)));
        assertThat(resultado.get(1), is(equalTo(historialIntermedia)));
        assertThat(resultado.get(2), is(equalTo(historialAntigua)));
    }
}
