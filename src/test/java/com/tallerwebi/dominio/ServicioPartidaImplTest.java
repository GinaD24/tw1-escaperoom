package com.tallerwebi.dominio;

import com.tallerwebi.dominio.enums.Dificultad;
import com.tallerwebi.infraestructura.RepositorioPartidaImpl;
import com.tallerwebi.infraestructura.RepositorioUsuarioImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class ServicioPartidaImplTest {

    private RepositorioPartida repositorioPartida;
    private ServicioPartida servicioPartida;
    private ServicioSala servicioSala;
    private RepositorioUsuario repositorioUsuario;

    @BeforeEach
    public void init() {
        this.repositorioPartida = mock(RepositorioPartidaImpl.class);
        this.repositorioUsuario = mock(RepositorioUsuarioImpl.class);
        this.servicioPartida = new ServicioPartidaImpl(servicioSala,repositorioPartida, repositorioUsuario );

    }

    @Test
    public void deberiaSolicitarAlRepositorioPartidaQueGuardeLaPartida() {
        Sala sala = new Sala(1, "La Mansión Misteriosa", Dificultad.PRINCIPIANTE, "Mansion", "Una noche tormentosa te encuentras atrapado en una vieja mansion llena de acertijos.",
                true, 10,"puerta-mansion.png");
        Partida partida = new Partida(LocalDateTime.now());
        partida.setSala(sala);

        this.servicioPartida.guardarPartida(partida);

        verify(repositorioPartida).guardarPartida(partida);
    }

    @Test
    public void deberiaDevolverLaPrimeraEtapaDeLaSalaEnLaPartida(){
        Sala sala = new Sala(1, "La Mansión Misteriosa", Dificultad.PRINCIPIANTE, "Mansion", "Una noche tormentosa te encuentras atrapado en una vieja mansion llena de acertijos.",
                true, 10,"puerta-mansion.png");
        Etapa etapa = new Etapa("Lobby", 1, "La puerta hacia la siguiente habitación está bloqueada por un candado, busca la clave en este acertijo.", "a.png");
        etapa.setId(1L);

        when(repositorioPartida.obtenerEtapaPorNumero(sala.getId(), etapa.getNumero())).thenReturn(etapa);

        this.servicioPartida.obtenerEtapaPorNumero(sala.getId(), etapa.getNumero());
        verify(repositorioPartida).obtenerEtapaPorNumero(sala.getId(), etapa.getNumero());
    }

    @Test
    public void deberiaDevolverElAcertijoDeLaEtapaEnLaPartida(){
        Etapa etapa = new Etapa("Lobby", 1, "La puerta hacia la siguiente habitación está bloqueada por un candado, busca la clave en este acertijo.", "a.png");
        etapa.setId(1L);
        Acertijo acertijo1 = new Acertijo( "lalalal");
        Acertijo acertijo2 = new Acertijo( "lelele");
        Acertijo acertijo3 = new Acertijo( "lilili");

        List<Acertijo> listaDeAcertijos = new ArrayList<>();

        listaDeAcertijos.add(acertijo1);
        listaDeAcertijos.add(acertijo2);
        listaDeAcertijos.add(acertijo3);

        when(repositorioPartida.obtenerListaDeAcertijos(etapa.getId())).thenReturn(listaDeAcertijos);
        Long idUsuario = 1L;
        Acertijo acertijoElegido = this.servicioPartida.obtenerAcertijo(etapa.getId(),idUsuario );

        assertTrue(acertijoElegido.equals(acertijo1) || acertijoElegido.equals(acertijo2) || acertijoElegido.equals(acertijo3));
        verify(repositorioPartida).obtenerListaDeAcertijos(etapa.getId());
    }

    @Test
    public void deberiaDevolverLaPrimeraPistaDelAcertijo(){
        Acertijo acertijo = new Acertijo( "lalalal");
        acertijo.setId(1L);
        Pista pista1 = new Pista("pista", 1);
        Pista pista2 = new Pista("pista", 2);
        Pista pista3 = new Pista("pista", 3);

        List<Pista> listaDePistas = new ArrayList<>();

        listaDePistas.add(pista1);
        listaDePistas.add(pista2);
        listaDePistas.add(pista3);

        when(repositorioPartida.obtenerListaDePistas(acertijo.getId())).thenReturn(listaDePistas);
        Long idUsuario = 1L;
        Pista pista = this.servicioPartida.obtenerSiguientePista(acertijo.getId(), idUsuario);

        verify(repositorioPartida).obtenerListaDePistas(acertijo.getId());
        assertThat(pista, equalTo(listaDePistas.get(0)));
    }

    @Test
    public void deberiaDevolverLaSegundaPistaDelAcertijo_UnaVezQueYaPidioLaPrimera(){
        Acertijo acertijo = new Acertijo( "lalalal");
        acertijo.setId(1L);
        Pista pista1 = new Pista("pista", 1);
        Pista pista2 = new Pista("pista", 2);
        Long idUsuario = 1L;

        List<Pista> listaDePistas = new ArrayList<>();
        listaDePistas.add(pista1);
        listaDePistas.add(pista2);

        AtomicInteger pistasUsadas = new AtomicInteger(0);

        when(repositorioPartida.obtenerListaDePistas(acertijo.getId())).thenReturn(listaDePistas);
        when(repositorioPartida.obtenerPistasUsadas(acertijo.getId(), idUsuario)).thenAnswer(invocation -> pistasUsadas.get());

        doAnswer(invocation -> {
            pistasUsadas.incrementAndGet();
            return null;
        }).when(repositorioPartida).sumarPistaUsada(acertijo.getId(), idUsuario);

        this.servicioPartida.obtenerSiguientePista(acertijo.getId(), idUsuario);
        Pista pista = this.servicioPartida.obtenerSiguientePista(acertijo.getId(), idUsuario);

        verify(repositorioPartida, times(2)).obtenerListaDePistas(acertijo.getId());
        assertThat(pista, equalTo(listaDePistas.get(1)));
    }

    @Test
    public void deberiaDevolverTrueSiSeRespondioCorrectamenteElAcertijo(){
        Sala sala = new Sala(1, "La Mansión Misteriosa", Dificultad.PRINCIPIANTE, "Mansion", "Una noche tormentosa te encuentras atrapado en una vieja mansion llena de acertijos.",
                true, 10,"puerta-mansion.png");
        Acertijo acertijo = new Acertijo( "lalalal");
        Respuesta respuestaCorrecta = new Respuesta("Respuesta");
        Respuesta respuestaIngresada = new Respuesta("Respuesta");

        when(repositorioPartida.obtenerRespuestaCorrecta(acertijo.getId())).thenReturn(respuestaCorrecta);

        Boolean validacionDeRespuesta = this.servicioPartida.validarRespuesta(acertijo.getId(),respuestaIngresada.getRespuesta());

        verify(repositorioPartida).obtenerRespuestaCorrecta(acertijo.getId());
        assertTrue(validacionDeRespuesta);
    }

    @Test
    public void deberiaDevolverTrue_SiLaRespuestaIngresadaCONTIENELaRespuestaCorrecta(){
        Sala sala = new Sala(1, "La Mansión Misteriosa", Dificultad.PRINCIPIANTE, "Mansion", "Una noche tormentosa te encuentras atrapado en una vieja mansion llena de acertijos.",
                true, 10,"puerta-mansion.png");
        Acertijo acertijo = new Acertijo( "lalalal");

        Respuesta respuestaCorrecta = new Respuesta("Respuesta");
        Respuesta respuestaIngresada = new Respuesta("LA Respuesta INGRESADA");

        when(repositorioPartida.obtenerRespuestaCorrecta(acertijo.getId())).thenReturn(respuestaCorrecta);

        Boolean validacionDeRespuesta = this.servicioPartida.validarRespuesta(acertijo.getId(),respuestaIngresada.getRespuesta());

        verify(repositorioPartida).obtenerRespuestaCorrecta(acertijo.getId());
        assertTrue(validacionDeRespuesta);
    }

    @Test
    public void deberiaDevolverFalsoSiNOSeRespondioCorrectamenteElAcertijo(){
        Sala sala = new Sala(1, "La Mansión Misteriosa", Dificultad.PRINCIPIANTE, "Mansion", "Una noche tormentosa te encuentras atrapado en una vieja mansion llena de acertijos.",
                true, 10,"puerta-mansion.png");
        Acertijo acertijo = new Acertijo( "lalalal");
        Respuesta respuestaCorrecta = new Respuesta("Respuesta");
        Respuesta respuestaIngresada = new Respuesta("akhsdgauysduiaRespuestahbsduykhagsdygasdyi");

        when(repositorioPartida.obtenerRespuestaCorrecta(acertijo.getId())).thenReturn(respuestaCorrecta);

        Boolean validacionDeRespuesta = this.servicioPartida.validarRespuesta(acertijo.getId(),respuestaIngresada.getRespuesta());

        verify(repositorioPartida).obtenerRespuestaCorrecta(acertijo.getId());
        assertFalse(validacionDeRespuesta);
    }

}
