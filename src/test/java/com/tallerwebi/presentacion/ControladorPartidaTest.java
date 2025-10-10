package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.*;
import com.tallerwebi.dominio.enums.Dificultad;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;

public class ControladorPartidaTest {

    ServicioSala servicioSala;
    ServicioPartida servicioPartida;
    ControladorPartida controladorPartida;

    @BeforeEach
    public void init() {
        this.servicioSala = mock(ServicioSala.class);
        this.servicioPartida = mock(ServicioPartidaImpl.class);
        this.controladorPartida = new ControladorPartida(servicioSala, servicioPartida);
    }


    @Test
    public void deberiaGuardarLaPartidaIniciada() {

        Sala sala = new Sala(1, "La Mansión Misteriosa", Dificultad.PRINCIPIANTE, "Mansion", "Una noche tormentosa te encuentras atrapado en una vieja mansion llena de acertijos.",
                true, 10,"puerta-mansion.png");
        Partida partida = new Partida(LocalDateTime.now());
        partida.setSala(sala);
//        Usuario usuario = new Usuario();
//        usuario.setId(1L);

        controladorPartida.iniciarPartida(sala.getId(), partida);

        verify(servicioPartida).guardarPartida(partida);
    }

    @Test
    public void deberiaMostrarLaVistaDeLaPartida() {

        Sala sala = new Sala(1, "La Mansión Misteriosa", Dificultad.PRINCIPIANTE, "Mansion", "Una noche tormentosa te encuentras atrapado en una vieja mansion llena de acertijos.",
                true, 10,"puerta-mansion.png");
        Etapa etapa = new Etapa("Lobby", 1, "La puerta hacia la siguiente habitación está bloqueada por un candado, busca la clave en este acertijo.", "a.png");


        ModelAndView modelAndView = controladorPartida.mostrarPartida(sala.getId(), etapa.getNumero());

        assertThat(modelAndView.getViewName(), equalTo("partida"));
    }



    @Test
    public void deberiaMostrarLaPrimeraEtapaDeLaSalaEnLaPartida() {

        Sala sala = new Sala(1, "La Mansión Misteriosa", Dificultad.PRINCIPIANTE, "Mansion", "Una noche tormentosa te encuentras atrapado en una vieja mansion llena de acertijos.",
                true, 10,"puerta-mansion.png");
        Etapa etapa = new Etapa("Lobby", 1, "La puerta hacia la siguiente habitación está bloqueada por un candado, busca la clave en este acertijo.", "a.png");
        etapa.setId(1L);

        when(servicioPartida.obtenerEtapaPorNumero(sala.getId(), etapa.getNumero())).thenReturn(etapa);

        ModelAndView modelAndView = controladorPartida.mostrarPartida(sala.getId(), etapa.getNumero());

        assertThat(modelAndView.getModel().get("etapa"), equalTo(etapa));
        verify(servicioPartida).obtenerEtapaPorNumero(sala.getId(), etapa.getNumero());
    }

    @Test
    public void deberiaMostrarElAcertijoDeLaEtapaEnLaPartida() {

        Sala sala = new Sala(1, "La Mansión Misteriosa", Dificultad.PRINCIPIANTE, "Mansion", "Una noche tormentosa te encuentras atrapado en una vieja mansion llena de acertijos.",
                true, 10,"puerta-mansion.png");
        Etapa etapa = new Etapa("Lobby", 1, "La puerta hacia la siguiente habitación está bloqueada por un candado, busca la clave en este acertijo.", "a.png");
        etapa.setId(1L);
        Acertijo acertijo = new Acertijo( "lalalal");

        when(servicioPartida.obtenerEtapaPorNumero(sala.getId(), etapa.getNumero())).thenReturn(etapa);
        when(servicioPartida.obtenerAcertijo(etapa.getId())).thenReturn(acertijo);

        ModelAndView modelAndView = controladorPartida.mostrarPartida(sala.getId(), etapa.getNumero());

        assertThat(modelAndView.getModel().get("acertijo"), equalTo(acertijo));
        verify(servicioPartida).obtenerAcertijo(etapa.getId());
        verify(servicioPartida).obtenerEtapaPorNumero(sala.getId(), etapa.getNumero());
    }

    @Test
    public void deberiaPoderPedirUnaPistaDelAcertijo() {
        Acertijo acertijo = new Acertijo( "lalalal");
        acertijo.setId(1L);
        Pista pista = new Pista("pista", 1);

        when(servicioPartida.obtenerSiguientePista(acertijo.getId())).thenReturn(pista);

        String pistaObtenida = controladorPartida.obtenerPista(acertijo.getId());

        assertThat(pistaObtenida, equalTo(pista.getDescripcion()));
        verify(servicioPartida).obtenerSiguientePista(acertijo.getId());
    }

    @Test
    public void deberiaMostrarLaSegundaEtapaYelSegundoAcertijoSiYaSeResolvioElPrimero() {

        Sala sala = new Sala(1, "La Mansión Misteriosa", Dificultad.PRINCIPIANTE, "Mansion", "Una noche tormentosa te encuentras atrapado en una vieja mansion llena de acertijos.",
             true, 10,"puerta-mansion.png");
        Etapa etapa = new Etapa("Lobby", 1, "La puerta hacia la siguiente habitación está bloqueada por un candado, busca la clave en este acertijo.", "a.png");
        etapa.setId(1L);
        Acertijo acertijo = new Acertijo( "lalalal");
        Respuesta respuesta = new Respuesta("Respuesta");

       when(servicioPartida.validarRespuesta(acertijo.getId(),respuesta.getRespuesta())).thenReturn(true);

       ModelAndView modelAndView = controladorPartida.validarRespuesta(sala.getId(), etapa.getNumero(),acertijo.getId(),respuesta.getRespuesta());

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

        when(servicioPartida.validarRespuesta(acertijo.getId(),respuesta.getRespuesta())).thenReturn(false);

        ModelAndView modelAndView = controladorPartida.validarRespuesta(sala.getId(), etapa.getNumero(),acertijo.getId(),respuesta.getRespuesta());

        assertThat(modelAndView.getViewName(), equalTo("partida"));
        assertThat(modelAndView.getModel().get("error"), equalTo("Respuesta incorrecta. Intenta nuevamente."));
        verify(servicioPartida).validarRespuesta(acertijo.getId(),respuesta.getRespuesta());
    }

    /*
    @Test
    public void deberiaMostrarActualizadoElTiempoDeLaPartidaCuandoSolicitoUnaPista() {
        Partida partida = new Partida();
        Sala sala = new Sala(1, "La Mansión Misteriosa", Dificultad.PRINCIPIANTE, "Mansion", "Una noche tormentosa te encuentras atrapado en una vieja mansion llena de acertijos.",
                true, 10,"puerta-mansion.png");
        Etapa etapa = new Etapa("Lobby", 1, "La puerta hacia la siguiente habitación está bloqueada por un candado, busca la clave en este acertijo.", "a.png");
        etapa.setId(1L);
        Acertijo acertijo = new Acertijo( "lalalal");
        Pista pista = new Pista("pista", 1);
        Respuesta respuesta = new Respuesta("Respuesta");

        when(servicioPartida.obtenerSiguientePista(acertijo.getId())).thenReturn(pista);
        verify(servicioPartida).descontarTiempoPista();
    }
    */
}
