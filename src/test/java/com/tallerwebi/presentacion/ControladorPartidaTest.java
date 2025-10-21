package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.*;
import com.tallerwebi.dominio.entidad.*;
import com.tallerwebi.dominio.enums.Dificultad;
import com.tallerwebi.dominio.interfaz.servicio.ServicioPartida;
import com.tallerwebi.dominio.interfaz.servicio.ServicioSala;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;

public class ControladorPartidaTest {

    ServicioSala servicioSala;
    ServicioPartida servicioPartida;
    ControladorPartida controladorPartida;
    HttpServletRequest requestMock;
    HttpSession sessionMock;

    @BeforeEach
    public void init() {
        this.servicioSala = mock(ServicioSala.class);
        this.servicioPartida = mock(ServicioPartidaImpl.class);
        this.controladorPartida = new ControladorPartida(servicioSala, servicioPartida);
        this.requestMock = mock(HttpServletRequest.class);
        this.sessionMock = mock(HttpSession.class);
    }


    @Test
    public void deberiaGuardarLaPartidaIniciada() {

        Sala sala = new Sala(1, "La Mansión Misteriosa", Dificultad.PRINCIPIANTE, "Mansion", "Una noche tormentosa te encuentras atrapado en una vieja mansion llena de acertijos.",
                true, 10,"puerta-mansion.png");
        Partida partida = new Partida(LocalDateTime.now());
        partida.setSala(sala);
        Etapa etapa = new Etapa("Lobby", 1, "La puerta hacia la siguiente habitación está bloqueada por un candado, busca la clave en este acertijo.", "a.png");
        Long idUsuario = 1L;
        when(requestMock.getSession()).thenReturn(sessionMock);
        when(servicioPartida.obtenerEtapaPorNumero(sala.getId(), 1)).thenReturn(etapa);
        when(sessionMock.getAttribute("id_usuario")).thenReturn(idUsuario);

        controladorPartida.iniciarPartida(sala.getId(), partida,requestMock );

        verify(servicioPartida).guardarPartida(partida, idUsuario, sala.getId());
    }

    @Test
    public void deberiaMostrarLaVistaDeLaPartida() {
        Sala sala = new Sala(1, "La Mansión Misteriosa", Dificultad.PRINCIPIANTE, "Mansion", "Una noche tormentosa te encuentras atrapado en una vieja mansion llena de acertijos.",
                true, 10,"puerta-mansion.png");
        Etapa etapa = new Etapa("Lobby", 1, "La puerta hacia la siguiente habitación está bloqueada por un candado, busca la clave en este acertijo.", "a.png");
        Acertijo acertijo = new Acertijo( "lalalal");
        Long idUsuario = 1L;

        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("id_sala_actual")).thenReturn(sala.getId());
        when(sessionMock.getAttribute("numero_etapa_actual")).thenReturn(etapa.getNumero());
        when(sessionMock.getAttribute("id_usuario")).thenReturn(idUsuario);

        when(servicioPartida.obtenerEtapaPorNumero(sala.getId(), etapa.getNumero())).thenReturn(etapa);
        when(servicioPartida.obtenerAcertijo(etapa.getId(), idUsuario)).thenReturn(acertijo);

        ModelAndView modelAndView = controladorPartida.mostrarPartida(sala.getId(), etapa.getNumero(), idUsuario, requestMock);

        assertThat(modelAndView.getViewName(), equalTo("partida"));
        verify(sessionMock).setAttribute("id_etapa", etapa.getId());
        verify(sessionMock).setAttribute("id_acertijo", acertijo.getId());
    }



    @Test
    public void deberiaMostrarLaPrimeraEtapaDeLaSalaEnLaPartida() {

        Sala sala = new Sala(1, "La Mansión Misteriosa", Dificultad.PRINCIPIANTE, "Mansion", "Una noche tormentosa te encuentras atrapado en una vieja mansion llena de acertijos.",
                true, 10,"puerta-mansion.png");
        Etapa etapa = new Etapa("Lobby", 1, "La puerta hacia la siguiente habitación está bloqueada por un candado, busca la clave en este acertijo.", "a.png");
        etapa.setId(1L);
        Acertijo acertijo = new Acertijo( "lalalal");
        acertijo.setId(1L);
        Long idUsuario = 1L;

        when(servicioPartida.obtenerAcertijo(etapa.getId(), idUsuario)).thenReturn(acertijo);
        when(servicioPartida.obtenerEtapaPorNumero(sala.getId(), etapa.getNumero())).thenReturn(etapa);

        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("id_sala_actual")).thenReturn(sala.getId());
        when(sessionMock.getAttribute("numero_etapa_actual")).thenReturn(etapa.getNumero());
        when(sessionMock.getAttribute("id_usuario")).thenReturn(idUsuario);

        ModelAndView modelAndView = controladorPartida.mostrarPartida(sala.getId(), etapa.getNumero(), idUsuario, requestMock);

        assertThat(modelAndView.getModel().get("etapa"), equalTo(etapa));
        verify(servicioPartida).obtenerEtapaPorNumero(sala.getId(), etapa.getNumero());
        verify(servicioPartida).obtenerAcertijo(etapa.getId(), idUsuario );
        verify(sessionMock).setAttribute("id_etapa", etapa.getId());
        verify(sessionMock).setAttribute("id_acertijo", acertijo.getId());
    }

    @Test
    public void deberiaMostrarElAcertijoDeLaEtapaEnLaPartida() {

        Sala sala = new Sala(1, "La Mansión Misteriosa", Dificultad.PRINCIPIANTE, "Mansion", "Una noche tormentosa te encuentras atrapado en una vieja mansion llena de acertijos.",
                true, 10,"puerta-mansion.png");
        Etapa etapa = new Etapa("Lobby", 1, "La puerta hacia la siguiente habitación está bloqueada por un candado, busca la clave en este acertijo.", "a.png");
        etapa.setId(1L);
        Acertijo acertijo = new Acertijo( "lalalal");
        Long idUsuario = 1L;

        when(servicioPartida.obtenerEtapaPorNumero(sala.getId(), etapa.getNumero())).thenReturn(etapa);
        when(servicioPartida.obtenerAcertijo(etapa.getId(), idUsuario )).thenReturn(acertijo);

        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("id_sala_actual")).thenReturn(sala.getId());
        when(sessionMock.getAttribute("numero_etapa_actual")).thenReturn(etapa.getNumero());
        when(sessionMock.getAttribute("id_usuario")).thenReturn(idUsuario);

        ModelAndView modelAndView = controladorPartida.mostrarPartida(sala.getId(), etapa.getNumero(), idUsuario, requestMock);

        assertThat(modelAndView.getModel().get("acertijo"), equalTo(acertijo));
        verify(servicioPartida).obtenerAcertijo(etapa.getId(), idUsuario );
        verify(servicioPartida).obtenerEtapaPorNumero(sala.getId(), etapa.getNumero());
    }

    @Test
    public void deberiaPoderPedirUnaPistaDelAcertijo() {
        Acertijo acertijo = new Acertijo( "lalalal");
        acertijo.setId(1L);
        Pista pista = new Pista("pista", 1);
        Long idUsuario = 1L;
        when(servicioPartida.obtenerSiguientePista(acertijo.getId(), idUsuario)).thenReturn(pista);

        String pistaObtenida = controladorPartida.obtenerPista(acertijo.getId(), idUsuario);

        assertThat(pistaObtenida, equalTo(pista.getDescripcion()));
        verify(servicioPartida).obtenerSiguientePista(acertijo.getId(),idUsuario );
    }

    @Test
    public void deberiaDevolverUnMensajeYaNoQuedanPistas_CuandoElUsuarioAgotoLasPistas() {
        Acertijo acertijo = new Acertijo( "lalalal");
        acertijo.setId(1L);
        Pista pista = new Pista("pista", 1);
        Long idUsuario = 1L;
        when(servicioPartida.obtenerSiguientePista(acertijo.getId(), idUsuario)).thenReturn(null);

        String pistaObtenida = controladorPartida.obtenerPista(acertijo.getId(), idUsuario);

        assertThat(pistaObtenida, equalTo("Ya no quedan pistas."));
        verify(servicioPartida).obtenerSiguientePista(acertijo.getId(),idUsuario);
    }

    @Test
    public void deberiaMostrarLaSegundaEtapaYelSegundoAcertijoSiYaSeResolvioElPrimero() {

        Sala sala = new Sala(1, "La Mansión Misteriosa", Dificultad.PRINCIPIANTE, "Mansion", "Una noche tormentosa te encuentras atrapado en una vieja mansion llena de acertijos.",
             true, 10,"puerta-mansion.png");
        sala.setCantidadDeEtapas(5);
        Etapa etapa = new Etapa("Lobby", 1, "La puerta hacia la siguiente habitación está bloqueada por un candado, busca la clave en este acertijo.", "a.png");
        etapa.setId(1L);
        Acertijo acertijo = new Acertijo( "lalalal");
        Respuesta respuesta = new Respuesta("Respuesta");
        Long idUsuario = 1L;

       when(servicioPartida.validarRespuesta(acertijo.getId(),respuesta.getRespuesta())).thenReturn(true);
       when(servicioSala.obtenerSalaPorId(sala.getId())).thenReturn(sala);
       when(servicioPartida.obtenerEtapaPorNumero(sala.getId(), etapa.getNumero())).thenReturn(etapa);
       when(servicioPartida.obtenerAcertijo(etapa.getId(), idUsuario)).thenReturn(acertijo);

        when(requestMock.getSession()).thenReturn(sessionMock);
       ModelAndView modelAndView = controladorPartida.validarRespuesta(sala.getId(), etapa.getNumero(),acertijo.getId(),respuesta.getRespuesta(), requestMock);

       assertThat(modelAndView.getViewName(), equalTo("redirect:/partida/sala" + sala.getId() + "/etapa" + (etapa.getNumero() + 1)));
       verify(servicioPartida).validarRespuesta(acertijo.getId(),respuesta.getRespuesta());
    }

    @Test
    public void deberiaMostrarUnMensajeSiNoSeRespondioCorrectamenteElAcertijo() {

        Sala sala = new Sala(1, "La Mansión Misteriosa", Dificultad.PRINCIPIANTE, "Mansion", "Una noche tormentosa te encuentras atrapado en una vieja mansion llena de acertijos.",
                true, 10,"puerta-mansion.png");
        Etapa etapa = new Etapa("Lobby", 1, "La puerta hacia la siguiente habitación está bloqueada por un candado, busca la clave en este acertijo.", "a.png");
        etapa.setId(1L);
        Acertijo acertijo = new Acertijo( "lalalal");
        Respuesta respuesta = new Respuesta("Respuesta");
        when(requestMock.getSession()).thenReturn(sessionMock);
        when(servicioPartida.validarRespuesta(acertijo.getId(),respuesta.getRespuesta())).thenReturn(false);
        ModelAndView modelAndView = controladorPartida.validarRespuesta(sala.getId(), etapa.getNumero(),acertijo.getId(),respuesta.getRespuesta(), requestMock);


        assertThat(modelAndView.getModel().get("error"), equalTo("Respuesta incorrecta. Intenta nuevamente."));
        verify(servicioPartida).validarRespuesta(acertijo.getId(),respuesta.getRespuesta());
    }

}
