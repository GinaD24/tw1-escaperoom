package com.tallerwebi.presentacion;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.tallerwebi.dominio.Historial;
import com.tallerwebi.dominio.ServicioHistorial;
import com.tallerwebi.dominio.ServicioPerfil; // IMPORTANTE: Nueva dependencia
import com.tallerwebi.dominio.Usuario; // Necesario para el mock de usuario

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView; // Aunque no se use, puede mantenerse

import java.time.LocalDateTime;


public class ControladorHistorialTest {


    ServicioHistorial servicioHistorial;
    ServicioPerfil servicioPerfil;
    ControladorHistorial controlador;

    @BeforeEach
    public void init() {
        servicioHistorial = mock(ServicioHistorial.class);
        servicioPerfil = mock(ServicioPerfil.class);

        controlador = new ControladorHistorial(servicioHistorial, servicioPerfil);
    }

    @Test
    void dadoQueRegistroUnaPartidaExitosaEntoncesSeGuardaYRedirigeAlPerfil() {

        String jugadorEmail = "alan@mail.com";
        Long usuarioId = 5L;
        Historial historial = new Historial(1, jugadorEmail, "Sala1", LocalDateTime.now(), true);

        Usuario usuarioMock = mock(Usuario.class);
        when(usuarioMock.getId()).thenReturn(usuarioId);

        when(servicioPerfil.buscarPorEmail(jugadorEmail)).thenReturn(usuarioMock);

        String resultado = controlador.registrar(historial);

        verify(servicioHistorial).registrarPartida(historial);
        verify(servicioPerfil).buscarPorEmail(jugadorEmail);
        assertThat(resultado, is("redirect:/perfil/" + usuarioId + "/historial"));
    }

    @Test
    void dadoQueRegistroUnaPartidaPeroElUsuarioNoExisteEntoncesRedirigeAInicio() {
        String jugadorEmail = "usuario_no_existe@mail.com";
        Historial historial = new Historial(1, jugadorEmail, "Sala1", LocalDateTime.now(), true);


        when(servicioPerfil.buscarPorEmail(jugadorEmail)).thenReturn(null);

        String resultado = controlador.registrar(historial);

        verify(servicioHistorial).registrarPartida(historial);
        verify(servicioPerfil).buscarPorEmail(jugadorEmail);
        assertThat(resultado, is("redirect:/inicio"));
    }

}