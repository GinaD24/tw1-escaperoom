package com.tallerwebi.dominio;

import com.tallerwebi.dominio.entidad.*;
import com.tallerwebi.dominio.enums.TipoAcertijo;
import com.tallerwebi.dominio.interfaz.servicio.ServicioDatosPartida;
import com.tallerwebi.dominio.interfaz.servicio.ServicioGeneradorIA;
import com.tallerwebi.dominio.interfaz.servicio.ServicioPartida;
import com.tallerwebi.dominio.interfaz.servicio.ServicioSala;
import com.tallerwebi.presentacion.DatosPartidaDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class ServicioDatosPartidaImplTest {

    private ServicioSala servicioSalaMock;
    private ServicioPartida servicioPartidaMock;
    private ServicioGeneradorIA servicioGeneradorIAMock;

    private ServicioDatosPartidaImpl servicioDatosPartida;

    @BeforeEach
    public void init() {
        this.servicioSalaMock = mock(ServicioSala.class);
        this.servicioPartidaMock = mock(ServicioPartida.class);
        this.servicioGeneradorIAMock = mock(ServicioGeneradorIA.class);

        this.servicioDatosPartida = new ServicioDatosPartidaImpl(
                servicioSalaMock,
                servicioPartidaMock,
                servicioGeneradorIAMock
        );
    }

    @Test
    public void deberiaLlamarAlServicioDeIAParaGenerarPistasSiElAcertijoEsAdivinanza() throws Exception {

        Integer idSala = 1;
        Integer numeroEtapa = 1;
        Long idUsuario = 1L;
        Long idEtapa = 10L;
        String descripcionAcertijo = "Acertijo";
        String respuestaAcertijo = "Respuesta";

        Sala salaMock = new Sala();
        Etapa etapaMock = new Etapa();
        etapaMock.setId(idEtapa);

        Respuesta respuestaMock = new Respuesta(respuestaAcertijo);

        Acertijo acertijoMock = new Acertijo(descripcionAcertijo);
        acertijoMock.setTipo(TipoAcertijo.ADIVINANZA);
        acertijoMock.setRespuesta(respuestaMock);

        List<String> pistasDeIAMock = Arrays.asList("pista 1 de IA", "pista 2 de IA", "pista 3 de IA");

        when(servicioSalaMock.obtenerSalaPorId(idSala)).thenReturn(salaMock);
        when(servicioPartidaMock.obtenerEtapaPorNumero(idSala, numeroEtapa)).thenReturn(etapaMock);
        when(servicioPartidaMock.obtenerAcertijo(idEtapa, idUsuario)).thenReturn(acertijoMock);

        when(servicioGeneradorIAMock.generarPistas(descripcionAcertijo, respuestaAcertijo)).thenReturn(pistasDeIAMock);

        DatosPartidaDTO dtoResultado = servicioDatosPartida.obtenerDatosDePartida(idSala, numeroEtapa, idUsuario);


        verify(servicioGeneradorIAMock, times(1)).generarPistas(descripcionAcertijo, respuestaAcertijo);

        Acertijo acertijoResultado = dtoResultado.getAcertijo();
        assertThat(acertijoResultado.getPistas().size(), equalTo(3));

        List<String> descripcionesEnAcertijo = acertijoResultado.getPistas().stream()
                .map(Pista::getDescripcion)
                .collect(Collectors.toList());

        assertThat(descripcionesEnAcertijo, containsInAnyOrder("pista 1 de IA", "pista 2 de IA", "pista 3 de IA"));
    }

    @Test
    public void NOdeberiaLlamarAlServicioDeIASiElAcertijoNoEsAdivinanza() throws Exception {

        Integer idSala = 1;
        Integer numeroEtapa = 1;
        Long idUsuario = 1L;
        Long idEtapa = 10L;

        Sala salaMock = new Sala();
        Etapa etapaMock = new Etapa();
        etapaMock.setId(idEtapa);

        Acertijo acertijoMock = new Acertijo("Acertijo Drag drop");
        acertijoMock.setTipo(TipoAcertijo.DRAG_DROP);
        acertijoMock.getPistas().add(new Pista("Pista de BD", 1));

        when(servicioSalaMock.obtenerSalaPorId(idSala)).thenReturn(salaMock);
        when(servicioPartidaMock.obtenerEtapaPorNumero(idSala, numeroEtapa)).thenReturn(etapaMock);
        when(servicioPartidaMock.obtenerAcertijo(idEtapa, idUsuario)).thenReturn(acertijoMock);

        DatosPartidaDTO dtoResultado = servicioDatosPartida.obtenerDatosDePartida(idSala, numeroEtapa, idUsuario);


        verify(servicioGeneradorIAMock, times(0)).generarPistas(anyString(), anyString());

        Acertijo acertijoResultado = dtoResultado.getAcertijo();
        assertThat(acertijoResultado.getPistas().size(), equalTo(1));
        assertThat(acertijoResultado.getPistas().stream().findFirst().get().getDescripcion(), equalTo("Pista de BD"));
    }
}