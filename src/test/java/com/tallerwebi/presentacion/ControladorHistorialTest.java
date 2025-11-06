package com.tallerwebi.presentacion;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;

import com.tallerwebi.dominio.entidad.*;
import com.tallerwebi.dominio.interfaz.servicio.ServicioHistorial;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class ControladorHistorialTest {

    private ServicioHistorial servicioHistorial;

    private ControladorHistorial controladorHistorial;

    @BeforeEach
    public void init() {

        servicioHistorial = mock(ServicioHistorial.class);
        controladorHistorial = new ControladorHistorial(servicioHistorial);
    }


    @Test
    public void DeberiaMostrarLaVistaHistorialConLasPartidasJugadasDelUsuario() {
        Usuario usuario = new Usuario();
        Partida partida1 = new Partida();
        partida1.setUsuario(usuario);
        partida1.setEsta_activa(false);
        partida1.setInicio(LocalDateTime.now());
        partida1.setPuntaje(100);

        Partida partida2 = new Partida();
        partida2.setUsuario(usuario);
        partida2.setEsta_activa(false);
        partida2.setInicio(LocalDateTime.now());
        partida2.setPuntaje(100);

        List<Partida> historial = new ArrayList<>();
        historial.add(partida1);
        historial.add(partida2);

        when(servicioHistorial.traerHistorialDeJugador(usuario.getId())).thenReturn(historial);

        ModelAndView modelAndView = controladorHistorial.verHistorial(usuario.getId());

        assertThat(modelAndView.getViewName(), equalTo("historial"));
        assertThat(modelAndView.getModel().get("historial"), equalTo(historial));
        verify(servicioHistorial).traerHistorialDeJugador(usuario.getId());
    }

}
