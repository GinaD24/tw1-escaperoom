package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.*;
import com.tallerwebi.dominio.entidad.*;
import com.tallerwebi.dominio.enums.Dificultad;
import com.tallerwebi.dominio.enums.TipoAcertijo;
import com.tallerwebi.dominio.interfaz.servicio.ServicioDatosPartida;
import com.tallerwebi.dominio.interfaz.servicio.ServicioPartida;
import com.tallerwebi.dominio.interfaz.servicio.ServicioSala;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.Arrays;

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
    DatosPartidaSesion datosPartidaSesion;

    @BeforeEach
    public void init() {
        this.servicioSala = mock(ServicioSala.class);
        this.servicioPartida = mock(ServicioPartida.class);
        this.servicioDatosPartida = mock(ServicioDatosPartida.class);
        this.datosPartidaSesion = mock(DatosPartidaSesion.class);
        this.controladorPartida = new ControladorPartida(servicioSala, servicioPartida,servicioDatosPartida, datosPartidaSesion);
        this.requestMock = mock(HttpServletRequest.class);
        this.sessionMock = mock(HttpSession.class);
    }


    @Test
    public void deberiaGuardarLaPartidaIniciada() {

        Sala sala = crearSalaTest();
        Partida partida = new Partida(LocalDateTime.now());
        partida.setSala(sala);

        Long idUsuario = 1L;
        mockUsuarioSesion(requestMock, sessionMock, idUsuario);

        when(servicioPartida.obtenerEtapaPorNumero(sala.getId(), 1)).thenReturn(new Etapa());

        controladorPartida.iniciarPartida(sala.getId(), partida, requestMock);

        verify(servicioPartida).obtenerEtapaPorNumero(sala.getId(), 1);
        verify(servicioPartida).guardarPartida(partida, idUsuario, sala.getId());
    }

    @Test
    public void deberiaMostrarLaVistaDeLaPartida() {

        Sala sala = crearSalaTest();
        Etapa etapa = crearEtapaTest();
        Acertijo acertijo = crearAcertijoTest();
        acertijo.setTipo(TipoAcertijo.ADIVINANZA);
        acertijo.setRespuesta(new Respuesta("mockRespuesta"));

        Long idUsuario = 1L;
        mockUsuarioSesion(requestMock, sessionMock, idUsuario);

        mockDatosPartidaSesion(datosPartidaSesion, sala.getId(), etapa.getNumero(), null, null);

        DatosPartidaDTO dto = new DatosPartidaDTO(sala, etapa, acertijo);
        when(servicioDatosPartida.obtenerDatosDePartida(sala.getId(), etapa.getNumero(), idUsuario)).thenReturn(dto);

        ModelAndView modelAndView = controladorPartida.mostrarPartida(sala.getId(), etapa.getNumero(), idUsuario);

        assertThat(modelAndView.getViewName(), equalTo("partida"));
        assertThat(modelAndView.getModel().get("salaElegida"), equalTo(sala));
        assertThat(modelAndView.getModel().get("etapa"), equalTo(etapa));
        assertThat(modelAndView.getModel().get("acertijo"), equalTo(acertijo));

        verify(servicioDatosPartida).obtenerDatosDePartida(sala.getId(), etapa.getNumero(), idUsuario);
        verify(datosPartidaSesion).setIdEtapa(etapa.getId());
        verify(datosPartidaSesion).setAcertijoActual(any(AcertijoActualDTO.class));
    }



    @Test
    public void deberiaMostrarLaPrimeraEtapaDeLaSalaEnLaPartida() {

        Sala sala = crearSalaTest();
        Etapa etapa = crearEtapaTest();
        Acertijo acertijo = crearAcertijoTest();
        acertijo.setTipo(TipoAcertijo.ADIVINANZA);
        acertijo.setRespuesta(new Respuesta("mockRespuesta"));

        Long idUsuario = 1L;

        mockUsuarioSesion(requestMock, sessionMock, idUsuario);

        mockDatosPartidaSesion(datosPartidaSesion, sala.getId(), etapa.getNumero(), null, null);

        DatosPartidaDTO dto = new DatosPartidaDTO(sala, etapa, acertijo);
        when(servicioDatosPartida.obtenerDatosDePartida(sala.getId(), etapa.getNumero(), idUsuario)).thenReturn(dto);

        ModelAndView modelAndView = controladorPartida.mostrarPartida(sala.getId(), etapa.getNumero(), idUsuario);

        assertThat(modelAndView.getModel().get("etapa"), equalTo(etapa));

        verify(servicioDatosPartida).obtenerDatosDePartida(sala.getId(), etapa.getNumero(), idUsuario);
        verify(datosPartidaSesion).setIdEtapa(etapa.getId());
        verify(datosPartidaSesion).setAcertijoActual(any(AcertijoActualDTO.class));
    }

    @Test
    public void deberiaMostrarElAcertijoDeLaEtapaEnLaPartida() {

        Sala sala = crearSalaTest();
        Etapa etapa = crearEtapaTest();
        Acertijo acertijo = crearAcertijoTest();
        acertijo.setTipo(TipoAcertijo.ADIVINANZA);

        acertijo.setRespuesta(new Respuesta("mockRespuesta"));

        Long idUsuario = 1L;

        mockUsuarioSesion(requestMock, sessionMock, idUsuario);

        mockDatosPartidaSesion(datosPartidaSesion, sala.getId(), etapa.getNumero(), null, null);

        DatosPartidaDTO dto = new DatosPartidaDTO(sala, etapa, acertijo);
        when(servicioDatosPartida.obtenerDatosDePartida(sala.getId(), etapa.getNumero(), idUsuario)).thenReturn(dto);

        ModelAndView modelAndView = controladorPartida.mostrarPartida(sala.getId(), etapa.getNumero(), idUsuario);

        assertThat(modelAndView.getModel().get("acertijo"), equalTo(acertijo));

        verify(servicioDatosPartida).obtenerDatosDePartida(sala.getId(), etapa.getNumero(), idUsuario);
        verify(datosPartidaSesion).setIdEtapa(etapa.getId());
        verify(datosPartidaSesion).setAcertijoActual(any(AcertijoActualDTO.class));
    }

    @Test
    public void deberiaPoderPedirUnaPistaDelAcertijo() {
        Long idUsuario = 1L;
        String textoPista = "Esta es la primera pista";

        AcertijoActualDTO acertijoDTO = new AcertijoActualDTO();
        acertijoDTO.getPistas().add(textoPista);
        acertijoDTO.setPistasUsadas(0);

        when(datosPartidaSesion.getAcertijoActual()).thenReturn(acertijoDTO);

        String pistaObtenida = controladorPartida.obtenerPista(idUsuario);

        assertThat(pistaObtenida, equalTo(textoPista));

        verify(servicioPartida).registrarUsoDePista(idUsuario);
        verify(datosPartidaSesion).getAcertijoActual();
        assertThat(acertijoDTO.getPistasUsadas(), equalTo(1));
    }

    @Test
    public void deberiaDevolverUnMensajeYaNoQuedanPistas_CuandoElUsuarioAgotoLasPistas() {
        Long idUsuario = 1L;

        AcertijoActualDTO acertijoDTO = new AcertijoActualDTO();
        acertijoDTO.getPistas().add("pista 1");
        acertijoDTO.setPistasUsadas(1);

        when(datosPartidaSesion.getAcertijoActual()).thenReturn(acertijoDTO);

        String pistaObtenida = controladorPartida.obtenerPista(idUsuario);

        assertThat(pistaObtenida, equalTo("Ya no quedan pistas."));

        verify(datosPartidaSesion).getAcertijoActual();
        verify(servicioPartida, times(0)).registrarUsoDePista(idUsuario);
    }

    @Test
    public void deberiaRedirigirALaSegundaEtapaConElSiguienteAcertijoSiYaSeResolvioElPrimero() {

        Sala sala = crearSalaTest();
        sala.setCantidadDeEtapas(5);
        Etapa etapa = crearEtapaTest();
        Long idUsuario = 1L;
        String respuestaCorrecta = "Respuesta";

        AcertijoActualDTO acertijoDTO = new AcertijoActualDTO();
        acertijoDTO.setId(1L);
        acertijoDTO.setTipo(TipoAcertijo.ADIVINANZA);
        acertijoDTO.setRespuestaCorrecta(respuestaCorrecta);

        when(datosPartidaSesion.getAcertijoActual()).thenReturn(acertijoDTO);

        Partida partidaMock = new Partida(LocalDateTime.now());
        when(servicioPartida.obtenerPartidaActivaPorIdUsuario(idUsuario)).thenReturn(partidaMock);
        when(servicioPartida.tiempoExpirado(partidaMock)).thenReturn(false);

        when(servicioPartida.validarRespuesta(acertijoDTO, respuestaCorrecta, idUsuario, null)).thenReturn(true);

        when(servicioSala.obtenerSalaPorId(sala.getId())).thenReturn(sala);
        when(servicioPartida.obtenerEtapaPorNumero(sala.getId(), etapa.getNumero())).thenReturn(etapa);

        ModelAndView modelAndView = controladorPartida.validarRespuesta(sala.getId(), etapa.getNumero(), respuestaCorrecta, idUsuario, null);

        assertThat(modelAndView.getViewName(), equalTo("redirect:/partida/sala" + sala.getId() + "/etapa" + (etapa.getNumero() + 1)));

        verify(datosPartidaSesion).getAcertijoActual();
        verify(servicioPartida).validarRespuesta(acertijoDTO, respuestaCorrecta, idUsuario, null);
        verify(datosPartidaSesion).setNumeroEtapaActual(etapa.getNumero() + 1);

        verify(servicioSala).obtenerSalaPorId(sala.getId());
        verify(servicioPartida).obtenerEtapaPorNumero(sala.getId(), etapa.getNumero());

        verify(servicioPartida, times(0)).buscarAcertijoPorId(any());
    }

    @Test
    public void deberiaMostrarUnMensajeSiNoSeRespondioCorrectamenteElAcertijo() {

        Sala sala = crearSalaTest();
        Etapa etapa = crearEtapaTest();
        Long idUsuario = 1L;
        String respuestaIncorrecta = "Respuesta Incorrecta";

        AcertijoActualDTO acertijoDTO = new AcertijoActualDTO();
        acertijoDTO.setId(1L);
        acertijoDTO.setTipo(TipoAcertijo.ADIVINANZA);
        acertijoDTO.setRespuestaCorrecta("Respuesta Correcta");
        acertijoDTO.setPistas(Arrays.asList("pista 1", "pista 2"));


        when(datosPartidaSesion.getAcertijoActual()).thenReturn(acertijoDTO);

        Partida partidaMock = new Partida(LocalDateTime.now());
        when(servicioPartida.obtenerPartidaActivaPorIdUsuario(idUsuario)).thenReturn(partidaMock);
        when(servicioPartida.tiempoExpirado(partidaMock)).thenReturn(false);

        when(servicioPartida.validarRespuesta(acertijoDTO, respuestaIncorrecta, idUsuario, null)).thenReturn(false);

        when(servicioSala.obtenerSalaPorId(sala.getId())).thenReturn(sala);
        when(servicioPartida.obtenerEtapaPorNumero(sala.getId(), etapa.getNumero())).thenReturn(etapa);

        ModelAndView modelAndView = controladorPartida.validarRespuesta(sala.getId(), etapa.getNumero(), respuestaIncorrecta, idUsuario, null);

        assertThat(modelAndView.getViewName(), equalTo("partida"));
        assertThat(modelAndView.getModel().get("error"), equalTo("Respuesta incorrecta. Intenta nuevamente."));

        verify(servicioPartida).validarRespuesta(acertijoDTO, respuestaIncorrecta, idUsuario, null);
        verify(servicioSala).obtenerSalaPorId(sala.getId());
        verify(servicioPartida).obtenerEtapaPorNumero(sala.getId(), etapa.getNumero());
        verify(datosPartidaSesion, times(0)).setNumeroEtapaActual(anyInt());
    }

    @Test
    public void deberiaSolicitarAlServicioQueFinaliceLaPartida_GuardarlaEnElModelo_YMostrarLaVistaPartidaGanada_SiGano() {

        Sala sala = crearSalaTest();
        sala.setCantidadDeEtapas(5);
        Partida partida = new Partida(LocalDateTime.now());
        partida.setSala(sala);
        partida.setGanada(true);
        partida.setPuntaje(100);
        Long idUsuario = 1L;

        when(datosPartidaSesion.getIdSalaActual()).thenReturn(sala.getId());
        when(datosPartidaSesion.getIdPartida()).thenReturn(partida.getId());
        when(datosPartidaSesion.getPartidaGanada()).thenReturn(partida.getGanada());

        mockUsuarioSesion(requestMock, sessionMock, idUsuario);

        when(servicioPartida.buscarPartidaPorId(partida.getId())).thenReturn(partida);
        when(servicioSala.obtenerSalaPorId(sala.getId())).thenReturn(sala);

        ModelAndView modelAndView = controladorPartida.finalizarPartida(requestMock);

        assertThat(modelAndView.getViewName(), equalTo("partidaGanada"));
        assertThat(modelAndView.getModel().get("partida"), equalTo(partida));
        verify(servicioPartida).finalizarPartida(idUsuario, partida.getGanada());
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

    private void mockDatosPartidaSesion(DatosPartidaSesion datosPartidaSesion, Integer idSala, Integer numeroEtapa, Long idEtapa, AcertijoActualDTO acertijoDTO) {
        when(datosPartidaSesion.getIdSalaActual()).thenReturn(idSala);
        when(datosPartidaSesion.getNumeroEtapaActual()).thenReturn(numeroEtapa);
        when(datosPartidaSesion.getIdEtapa()).thenReturn(idEtapa);
        when(datosPartidaSesion.getAcertijoActual()).thenReturn(acertijoDTO);
    }


}