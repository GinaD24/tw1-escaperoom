package com.tallerwebi.presentacion;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.tallerwebi.dominio.entidad.Historial;
import com.tallerwebi.dominio.entidad.Partida;
import com.tallerwebi.dominio.interfaz.servicio.ServicioHistorial;
import com.tallerwebi.dominio.interfaz.servicio.ServicioPartida;
import com.tallerwebi.dominio.interfaz.servicio.ServicioPerfil; // IMPORTANTE: Nueva dependencia
import com.tallerwebi.dominio.entidad.Usuario; // Necesario para el mock de usuario

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.util.List;


public class ControladorHistorialTest {




   private ServicioHistorial servicioHistorial;
    private ServicioPartida servicioPartida;

    private ControladorHistorial controladorHistorial;

    @BeforeEach
    public void init() {
        servicioPartida = mock(ServicioPartida.class);

        controladorHistorial = new ControladorHistorial(servicioPartida);
    }

    @Test
    public void obtenerHistorialDeberiaRedirigirALoginSiUsuarioEsNulo() {

        Long idUsuarioNulo = null;

        ModelAndView modelAndView = controladorHistorial.obtenerHistorial(idUsuarioNulo);

        assertThat(modelAndView.getViewName(), is("redirect:/login"));

        verify(servicioPartida, never()).obtenerHistorialDePartida(anyLong());
    }

    @Test
    public void obtenerHistorialDeberiaRetornarVistaConPartidas() {

        final Long ID_USUARIO = 1L;

        Partida partidaMock = mock(Partida.class);
        List<Partida> historialEsperado = List.of(partidaMock);

        when(servicioPartida.obtenerHistorialDePartida(ID_USUARIO)).thenReturn(historialEsperado);

        ModelAndView modelAndView = controladorHistorial.obtenerHistorial(ID_USUARIO);

        assertThat(modelAndView.getViewName(), is("historial"));

        assertThat(modelAndView.getModel().get("historial"), is(historialEsperado));

        verify(servicioPartida, times(1)).obtenerHistorialDePartida(ID_USUARIO);
    }

    @Test
    public void obtenerHistorialDeberiaRetornarVistaVaciaSiNoHayPartidas() {

        final Long ID_USUARIO = 2L;

        List<Partida> historialVacio = List.of();

        when(servicioPartida.obtenerHistorialDePartida(ID_USUARIO)).thenReturn(historialVacio);

        ModelAndView modelAndView = controladorHistorial.obtenerHistorial(ID_USUARIO);

        assertThat(modelAndView.getViewName(), is("historial"));

        assertThat(modelAndView.getModel().get("historial"), is(historialVacio));

        verify(servicioPartida, times(1)).obtenerHistorialDePartida(ID_USUARIO);
    }
}