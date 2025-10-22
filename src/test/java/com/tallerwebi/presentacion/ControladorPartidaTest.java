package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.*;
import com.tallerwebi.dominio.entidad.*;
import com.tallerwebi.dominio.enums.Dificultad;
import com.tallerwebi.dominio.interfaz.servicio.ServicioDatosPartida;
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
    ServicioDatosPartida servicioDatosPartida;
    ControladorPartida controladorPartida;
    HttpServletRequest requestMock;
    HttpSession sessionMock;
    DatosPartidaSesion datosPartida;

    @BeforeEach
    public void init() {
        this.servicioSala = mock(ServicioSala.class);
        this.servicioPartida = mock(ServicioPartidaImpl.class);
        this.servicioDatosPartida = mock(ServicioDatosPartida.class);
        this.datosPartida = mock(DatosPartidaSesion.class);
        this.controladorPartida = new ControladorPartida(servicioSala, servicioPartida,servicioDatosPartida, datosPartida);
        this.requestMock = mock(HttpServletRequest.class);
        this.sessionMock = mock(HttpSession.class);
    }


    @Test
    public void deberiaGuardarLaPartidaIniciada() {

        Sala sala = crearSalaTest();
        Partida partida = new Partida(LocalDateTime.now());
        partida.setSala(sala);
        Etapa etapa = crearEtapaTest();

        Long idUsuario = 1L;
        mockUsuarioSesion(requestMock, sessionMock, idUsuario);

        controladorPartida.iniciarPartida(sala.getId(), partida, requestMock);

        verify(servicioPartida).guardarPartida(partida, idUsuario, sala.getId());
    }

    @Test
    public void deberiaMostrarLaVistaDeLaPartida() {
        Sala sala = crearSalaTest();
        Etapa etapa = crearEtapaTest();
        Acertijo acertijo = crearAcertijoTest();

        Long idUsuario = 1L;
        mockUsuarioSesion(requestMock, sessionMock, idUsuario);

        mockDatosPartidaSesion(datosPartida, sala.getId(), etapa.getNumero(), null, null);

        DatosPartidaDTO dto = new DatosPartidaDTO(sala, etapa, acertijo);
        when(servicioDatosPartida.obtenerDatosDePartida(sala.getId(), etapa.getNumero(), idUsuario)).thenReturn(dto);

        ModelAndView modelAndView = controladorPartida.mostrarPartida(sala.getId(), etapa.getNumero(), idUsuario);

        assertThat(modelAndView.getViewName(), equalTo("partida"));
        assertThat(modelAndView.getModel().get("salaElegida"), equalTo(sala));
        assertThat(modelAndView.getModel().get("etapa"), equalTo(etapa));
        assertThat(modelAndView.getModel().get("acertijo"), equalTo(acertijo));

        verify(servicioDatosPartida).obtenerDatosDePartida(sala.getId(), etapa.getNumero(), idUsuario);
        verify(datosPartida).setIdEtapa(etapa.getId());
        verify(datosPartida).setIdAcertijo(acertijo.getId());
    }




    @Test
    public void deberiaMostrarLaPrimeraEtapaDeLaSalaEnLaPartida() {

        Sala sala = crearSalaTest();
        Etapa etapa = crearEtapaTest();
        Acertijo acertijo = crearAcertijoTest();
        Long idUsuario = 1L;
        DatosPartidaDTO dto = new DatosPartidaDTO(sala, etapa, acertijo);
        when(servicioDatosPartida.obtenerDatosDePartida(sala.getId(), etapa.getNumero(), idUsuario)).thenReturn(dto);

        mockUsuarioSesion(requestMock, sessionMock, idUsuario);
        mockDatosPartidaSesion(datosPartida, sala.getId(), etapa.getNumero(), null, null);

        when(servicioPartida.obtenerAcertijo(etapa.getId(), idUsuario)).thenReturn(acertijo);
        when(servicioPartida.obtenerEtapaPorNumero(sala.getId(), etapa.getNumero())).thenReturn(etapa);

        ModelAndView modelAndView = controladorPartida.mostrarPartida(sala.getId(), etapa.getNumero(), idUsuario);

        assertThat(modelAndView.getModel().get("etapa"), equalTo(etapa));

        verify(servicioDatosPartida).obtenerDatosDePartida(sala.getId(), etapa.getNumero(), idUsuario);
        verify(datosPartida).setIdEtapa(etapa.getId());
        verify(datosPartida).setIdAcertijo(acertijo.getId());
    }

    @Test
    public void deberiaMostrarElAcertijoDeLaEtapaEnLaPartida() {

        Sala sala = crearSalaTest();
        Etapa etapa = crearEtapaTest();
        Acertijo acertijo = crearAcertijoTest();

        Long idUsuario = 1L;
        mockUsuarioSesion(requestMock, sessionMock, idUsuario);

        DatosPartidaDTO dto = new DatosPartidaDTO(sala, etapa, acertijo);
        when(servicioDatosPartida.obtenerDatosDePartida(sala.getId(), etapa.getNumero(), idUsuario)).thenReturn(dto);
        mockDatosPartidaSesion(datosPartida, sala.getId(), etapa.getNumero(), null, null);

        when(servicioPartida.obtenerEtapaPorNumero(sala.getId(), etapa.getNumero())).thenReturn(etapa);
        when(servicioPartida.obtenerAcertijo(etapa.getId(), idUsuario )).thenReturn(acertijo);

        ModelAndView modelAndView = controladorPartida.mostrarPartida(sala.getId(), etapa.getNumero(), idUsuario);

        assertThat(modelAndView.getModel().get("acertijo"), equalTo(acertijo));
        verify(servicioDatosPartida).obtenerDatosDePartida(sala.getId(), etapa.getNumero(), idUsuario);
        verify(datosPartida).setIdEtapa(etapa.getId());
        verify(datosPartida).setIdAcertijo(acertijo.getId());
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

       when(servicioPartida.validarRespuesta(acertijo.getId(),respuesta.getRespuesta(), idUsuario)).thenReturn(true);
       when(servicioSala.obtenerSalaPorId(sala.getId())).thenReturn(sala);
       when(servicioPartida.obtenerEtapaPorNumero(sala.getId(), etapa.getNumero())).thenReturn(etapa);
       when(servicioPartida.obtenerAcertijo(etapa.getId(), idUsuario)).thenReturn(acertijo);

        when(requestMock.getSession()).thenReturn(sessionMock);
       ModelAndView modelAndView = controladorPartida.validarRespuesta(sala.getId(), etapa.getNumero(),acertijo.getId(),respuesta.getRespuesta(), idUsuario);

       assertThat(modelAndView.getViewName(), equalTo("redirect:/partida/sala" + sala.getId() + "/etapa" + (etapa.getNumero() + 1)));
       verify(servicioPartida).validarRespuesta(acertijo.getId(),respuesta.getRespuesta(),idUsuario);
    }

    @Test
    public void deberiaMostrarUnMensajeSiNoSeRespondioCorrectamenteElAcertijo() {

        Sala sala = new Sala(1, "La Mansión Misteriosa", Dificultad.PRINCIPIANTE, "Mansion", "Una noche tormentosa te encuentras atrapado en una vieja mansion llena de acertijos.",
                true, 10,"puerta-mansion.png");
        Etapa etapa = new Etapa("Lobby", 1, "La puerta hacia la siguiente habitación está bloqueada por un candado, busca la clave en este acertijo.", "a.png");
        etapa.setId(1L);
        Acertijo acertijo = new Acertijo( "lalalal");
        Respuesta respuesta = new Respuesta("Respuesta");
        Long idUsuario = 1L;
        when(requestMock.getSession()).thenReturn(sessionMock);
        when(servicioPartida.validarRespuesta(acertijo.getId(),respuesta.getRespuesta(),idUsuario )).thenReturn(false);
        ModelAndView modelAndView = controladorPartida.validarRespuesta(sala.getId(), etapa.getNumero(),acertijo.getId(),respuesta.getRespuesta(), idUsuario );


        assertThat(modelAndView.getModel().get("error"), equalTo("Respuesta incorrecta. Intenta nuevamente."));
        verify(servicioPartida).validarRespuesta(acertijo.getId(),respuesta.getRespuesta(), idUsuario);
    }


    private Sala crearSalaTest() {
        return new Sala(1, "La Mansión Misteriosa", Dificultad.PRINCIPIANTE, "Mansion",
                "Una noche tormentosa te encuentras atrapado en una vieja mansion llena de acertijos.",
                true, 10, "puerta-mansion.png");
    }

    private Etapa crearEtapaTest() {
        Etapa e = new Etapa("Lobby", 1, "La puerta hacia la siguiente habitación está bloqueada por un candado.", "a.png");
        e.setId(10L);
        return e;
    }

    private Acertijo crearAcertijoTest() {
        Acertijo a = new Acertijo("lalalal");
        a.setId(20L);
        return a;
    }

    private void mockUsuarioSesion(HttpServletRequest requestMock, HttpSession sessionMock, Long idUsuario) {
        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("id_usuario")).thenReturn(idUsuario);
    }

    private void mockDatosPartidaSesion(DatosPartidaSesion datosPartida, Integer idSala, Integer numeroEtapa, Long idEtapa, Long idAcertijo) {
        when(datosPartida.getIdSalaActual()).thenReturn(idSala);
        when(datosPartida.getNumeroEtapaActual()).thenReturn(numeroEtapa);
        when(datosPartida.getIdEtapa()).thenReturn(idEtapa);
        when(datosPartida.getIdAcertijo()).thenReturn(idAcertijo);
    }


}
