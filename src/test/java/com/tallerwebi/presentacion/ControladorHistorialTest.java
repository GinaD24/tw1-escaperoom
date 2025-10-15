package com.tallerwebi.presentacion;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import com.tallerwebi.dominio.Historial;
import com.tallerwebi.dominio.ServicioHistorial;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ControladorHistorialTest {

    ServicioHistorial servicio;
    ControladorHistorial controlador;

    @BeforeEach
    public void init() {
        servicio = mock(ServicioHistorial.class);
        controlador = new ControladorHistorial(servicio);
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

        // CAMBIO 1: El controlador ahora devuelve un ModelAndView
        ModelAndView resultado = controlador.verHistorial();

        verify(servicio).traerHistorial();

        // CAMBIO 2: Verificamos el nombre de la vista
        assertThat(resultado.getViewName(), is("historial-lista"));

        // CAMBIO 3: Obtenemos la lista desde el modelo para verificar su contenido
        List<Historial> historialesModelo = (List<Historial>) resultado.getModel().get("historiales");

        assertThat(historialesModelo.size(), is(2)); // Verificamos el tamaño de la lista dentro del modelo
        assertThat(historialesModelo, is(historials)); // Opcional: verifica que sea la lista correcta
    }

    @Test
    void dadoQueNoHayPartidasCuandoTraigoElHistorialCompletoEntoncesDevuelvoListaVacia() {
        when(servicio.traerHistorial()).thenReturn(new ArrayList<>());

        ModelAndView resultado = controlador.verHistorial(); // CAMBIO

        verify(servicio).traerHistorial();
        assertThat(resultado.getViewName(), is("historial-lista")); // Verificamos el nombre de la vista

        List<Historial> historialesModelo = (List<Historial>) resultado.getModel().get("historiales"); // CAMBIO
        assertThat(historialesModelo.size(), is(0));
    }

    @Test
    void dadoQueHayPartidasDeUnJugadorCuandoTraigoSuHistorialEntoncesDevuelvoLasCorrectas() {
        Historial p1 = new Historial(1, "Alan", "Sala1", LocalDateTime.now(), true);
        Historial p2 = new Historial(2, "Alan", "Sala2", LocalDateTime.now(), false);
        List<Historial> historials = Arrays.asList(p1, p2);
        when(servicio.traerHistorialDeJugador("Alan")).thenReturn(historials);

        // CAMBIO 1: Esperamos ModelAndView
        ModelAndView resultado = controlador.verHistorialJugador("Alan");

        verify(servicio).traerHistorialDeJugador("Alan");
        assertThat(resultado.getViewName(), is("historial-jugador")); // Verificamos la vista

        // CAMBIO 2: Obtenemos la lista del modelo
        List<Historial> historialesModelo = (List<Historial>) resultado.getModel().get("historiales");

        assertThat(historialesModelo.size(), is(2));
        assertThat(historialesModelo.get(0).getGano(), is(true));
        assertThat(historialesModelo.get(1).getGano(), is(false));
    }

    @Test
    void dadoQueNoHayPartidasDeUnJugadorCuandoTraigoSuHistorialEntoncesDevuelvoListaVacia() {
        when(servicio.traerHistorialDeJugador("Alan")).thenReturn(new ArrayList<>());

        ModelAndView resultado = controlador.verHistorialJugador("Alan"); // CAMBIO

        verify(servicio).traerHistorialDeJugador("Alan");
        assertThat(resultado.getViewName(), is("historial-jugador")); // Verificamos la vista

        List<Historial> historialesModelo = (List<Historial>) resultado.getModel().get("historiales"); // CAMBIO
        assertThat(historialesModelo.size(), is(0));
    }

    @Test
    void dadoQueRegistroMultiplesPartidasCuandoTraigoElHistorialEntoncesSeMuestranTodas() {
        Historial p1 = new Historial(1, "Alan", "Sala1", LocalDateTime.now(), true);
        Historial p2 = new Historial(2, "Alan", "Sala2", LocalDateTime.now(), false);
        List<Historial> historialEsperado = Arrays.asList(p1, p2);

        // Configurar el mock para que devuelva la lista esperada
        when(servicio.traerHistorial()).thenReturn(historialEsperado);

        // 1. Ejecutar las acciones
        controlador.registrar(p1);
        controlador.registrar(p2);

        // CAMBIO 1: El controlador.verHistorial() ahora devuelve un ModelAndView
        ModelAndView resultado = controlador.verHistorial();

        // 2. Verificaciones de Mockito
        verify(servicio, times(2)).registrarPartida(any(Historial.class));
        verify(servicio).traerHistorial();

        // 3. Verificaciones de Hamcrest (Modelo y Vista)
        // Verificamos el nombre de la vista
        assertThat(resultado.getViewName(), is("historial-lista"));

        // Extraemos la lista de Historial del Modelo (el atributo "historiales")
        List<Historial> historialesModelo = (List<Historial>) resultado.getModel().get("historiales");

        // Verificamos que la lista extraída sea la correcta
        assertThat(historialesModelo.size(), is(2));
        assertThat(historialesModelo, is(historialEsperado)); // Opcional: verifica que sea la lista exacta
    }
}
