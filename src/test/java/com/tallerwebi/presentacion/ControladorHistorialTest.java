package com.tallerwebi.presentacion;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.tallerwebi.dominio.entidad.*;
import com.tallerwebi.dominio.interfaz.servicio.ServicioHistorial;
import com.tallerwebi.dominio.interfaz.servicio.ServicioPartida;
import com.tallerwebi.dominio.interfaz.servicio.ServicioPerfil; // IMPORTANTE: Nueva dependencia

import com.tallerwebi.dominio.interfaz.servicio.ServicioSala;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class ControladorHistorialTest {

   private ServicioHistorial servicioHistorial;
    private ServicioPartida servicioPartida;
    private ServicioSala servicioSala;
    private ControladorHistorial controladorHistorial;

    @BeforeEach
    public void init() {
        servicioPartida = mock(ServicioPartida.class);
        servicioSala = mock(ServicioSala.class);
        servicioHistorial = mock(ServicioHistorial.class);

        controladorHistorial = new ControladorHistorial(servicioPartida, servicioSala, servicioHistorial);
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

    @Test
    public void dadoQueExisteUnaVistaHistorialPuedoFiltrarlosPorSala(){
        LocalDateTime ahora = LocalDateTime.now();
        Integer idSala = 1;
        List<Sala> salas = new ArrayList<>();
        Sala sala = new Sala();
        sala.setId(idSala);
        salas.add(sala);

        List<Historial> historialDeSala1 = new ArrayList<>();
        historialDeSala1.add(new Historial(1, "Alan", 1, ahora.minusMinutes(5), true));
        historialDeSala1.add(new Historial(2, "Maria", 1, ahora, false));

        List<Historial> historialDeSala2 = new ArrayList<>();
        historialDeSala2.add(new Historial(3, "Sofia", 2, ahora.minusMinutes(15), true));
        historialDeSala2.add(new Historial(4, "Franco", 2, ahora.minusMinutes(10), true));

        when(servicioHistorial.obtenerHistorialPorSala(idSala)).thenReturn(historialDeSala1);
        when(servicioSala.traerSalas()).thenReturn((ArrayList<Sala>) salas);
        when(servicioSala.obtenerSalaPorId(idSala)).thenReturn(sala);
        ModelAndView modelAndView = controladorHistorial.filtrarHistorial("1");

        List<Historial> historialObtenidoDeSala1 = (List<Historial>) modelAndView.getModel().get("historials");

        assertThat(historialObtenidoDeSala1, equalTo(historialDeSala1));
        verify(servicioHistorial).obtenerHistorialPorSala(idSala);
        verify(servicioSala).traerSalas();
        verify(servicioSala).obtenerSalaPorId(idSala);

    }
}