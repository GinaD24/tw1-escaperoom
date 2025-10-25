package com.tallerwebi.dominio;

import com.tallerwebi.dominio.entidad.*;
import com.tallerwebi.dominio.enums.Dificultad;
import com.tallerwebi.dominio.enums.TipoAcertijo;
import com.tallerwebi.dominio.excepcion.EtapaInexistente;
import com.tallerwebi.dominio.excepcion.SesionDeUsuarioExpirada;
import com.tallerwebi.dominio.excepcion.UsuarioInexistente;
import com.tallerwebi.dominio.interfaz.repositorio.RepositorioPartida;
import com.tallerwebi.dominio.interfaz.repositorio.RepositorioSala;
import com.tallerwebi.dominio.interfaz.repositorio.RepositorioUsuario;
import com.tallerwebi.dominio.interfaz.servicio.ServicioPartida;
import com.tallerwebi.dominio.interfaz.servicio.ServicioSala;
import com.tallerwebi.infraestructura.RepositorioPartidaImpl;
import com.tallerwebi.infraestructura.RepositorioSalaImpl;
import com.tallerwebi.infraestructura.RepositorioUsuarioImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ServicioPartidaImplTest {

    private RepositorioPartida repositorioPartida;
    private ServicioPartida servicioPartida;
    private ServicioSala servicioSala;
    private RepositorioUsuario repositorioUsuario;
    private RepositorioSala repositorioSala;

    @BeforeEach
    public void init() {
        this.repositorioPartida = mock(RepositorioPartidaImpl.class);
        this.repositorioUsuario = mock(RepositorioUsuarioImpl.class);
        this.repositorioSala = mock(RepositorioSalaImpl.class);
        this.servicioPartida = new ServicioPartidaImpl(servicioSala,repositorioPartida, repositorioUsuario, repositorioSala );

    }

    @Test
    public void deberiaSolicitarAlRepositorioPartidaQueGuardeLaPartida() {
        Sala sala = new Sala(1, "La Mansión Misteriosa", Dificultad.PRINCIPIANTE, "Mansion", "Una noche tormentosa te encuentras atrapado en una vieja mansion llena de acertijos.",
                true, 10,"puerta-mansion.png");
        Partida partida = new Partida(LocalDateTime.now());
        partida.setSala(sala);
        Usuario usuario = new Usuario();
        usuario.setId(1L);

        when(this.repositorioUsuario.obtenerUsuarioPorId(usuario.getId())).thenReturn(usuario);
        this.servicioPartida.guardarPartida(partida, usuario.getId(), sala.getId());

        verify(repositorioPartida).guardarPartida(partida);
    }

    @Test
    public void deberiaLanzarLaExcepcionSesionDeUsuarioExpirada_SiLeLlegaUnIdUsuarioNulo() {
        Sala sala = new Sala(1, "La Mansión Misteriosa", Dificultad.PRINCIPIANTE, "Mansion", "Una noche tormentosa te encuentras atrapado en una vieja mansion llena de acertijos.",
                true, 10,"puerta-mansion.png");
        Partida partida = new Partida(LocalDateTime.now());
        partida.setSala(sala);
        Usuario usuario = new Usuario();
        usuario.setId(1L);

        when(this.repositorioUsuario.obtenerUsuarioPorId(usuario.getId())).thenReturn(usuario);

        assertThrows(SesionDeUsuarioExpirada.class, () -> {
            this.servicioPartida.guardarPartida(partida, null, sala.getId());
        });

    }

    @Test
    public void deberiaLanzarLaExcepcionUsuarioInexistente_SiLeLlegaUnUsuarioNulo() {
        Sala sala = new Sala(1, "La Mansión Misteriosa", Dificultad.PRINCIPIANTE, "Mansion", "Una noche tormentosa te encuentras atrapado en una vieja mansion llena de acertijos.",
                true, 10,"puerta-mansion.png");
        Partida partida = new Partida(LocalDateTime.now());
        partida.setSala(sala);
        Usuario usuario = new Usuario();
        usuario.setId(1L);

        when(this.repositorioUsuario.obtenerUsuarioPorId(usuario.getId())).thenReturn(null);


        assertThrows(UsuarioInexistente.class, () -> {
            this.servicioPartida.guardarPartida(partida, usuario.getId(), sala.getId());
        });

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
    public void deberiaLanzarUnaExcepcionEtapaInexistente_CuandoLaEtapaObtenidaEsNula(){
        Sala sala = new Sala(1, "La Mansión Misteriosa", Dificultad.PRINCIPIANTE, "Mansion", "Una noche tormentosa te encuentras atrapado en una vieja mansion llena de acertijos.",
                true, 10,"puerta-mansion.png");
        Etapa etapa = new Etapa("Lobby", 1, "La puerta hacia la siguiente habitación está bloqueada por un candado, busca la clave en este acertijo.", "a.png");
        etapa.setId(1L);

        when(repositorioPartida.obtenerEtapaPorNumero(sala.getId(), etapa.getNumero())).thenReturn(null);

        assertThrows(EtapaInexistente.class, () -> {
            this.servicioPartida.obtenerEtapaPorNumero(sala.getId(), etapa.getNumero());
        });
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
    public void deberiaDevolverUnAcertijo_QueElUsuarioNoHayaVisto(){
        Etapa etapa = new Etapa("Lobby", 1, "La puerta hacia la siguiente habitación está bloqueada por un candado, busca la clave en este acertijo.", "a.png");
        etapa.setId(1L);
        Acertijo acertijo1 = new Acertijo( "lalalal");
        Acertijo acertijo2 = new Acertijo( "lelele");
        Acertijo acertijo3 = new Acertijo( "lilili");

        List<Acertijo> listaDeAcertijos = new ArrayList<>();
        listaDeAcertijos.add(acertijo1);
        listaDeAcertijos.add(acertijo2);
        listaDeAcertijos.add(acertijo3);

        List<Acertijo> listaDeAcertijosVISTOS = new ArrayList<>();
        listaDeAcertijosVISTOS.add(acertijo1);

        Long idUsuario = 1L;

        when(repositorioPartida.obtenerListaDeAcertijos(etapa.getId())).thenReturn(listaDeAcertijos);
        when(repositorioPartida.obtenerAcertijosVistosPorUsuarioPorEtapa(idUsuario, etapa.getId())).thenReturn(listaDeAcertijosVISTOS);

        Acertijo acertijoElegido = this.servicioPartida.obtenerAcertijo(etapa.getId(),idUsuario );

        assertTrue(acertijoElegido.equals(acertijo2) || acertijoElegido.equals(acertijo3));
        assertNotEquals(acertijoElegido, acertijo1);

        verify(repositorioPartida).obtenerAcertijosVistosPorUsuarioPorEtapa(idUsuario, etapa.getId());
        verify(repositorioPartida).obtenerListaDeAcertijos(etapa.getId());
    }

    @Test
    public void deberiaDevolverLaPrimeraPistaDelAcertijo(){
        Long idUsuario = 1L;
        Acertijo acertijo = new Acertijo( "lalalal");
        acertijo.setId(1L);
        Pista pista1 = new Pista("pista", 1);
        Pista pista2 = new Pista("pista", 2);
        Pista pista3 = new Pista("pista", 3);

        List<Pista> listaDePistas = new ArrayList<>();

        listaDePistas.add(pista1);
        listaDePistas.add(pista2);
        listaDePistas.add(pista3);

        Partida partida = new Partida(LocalDateTime.now());
        when(repositorioPartida.obtenerPartidaActivaPorUsuario(idUsuario)).thenReturn(partida);

        when(repositorioPartida.obtenerListaDePistas(acertijo.getId())).thenReturn(listaDePistas);

        Pista pista = this.servicioPartida.obtenerSiguientePista(acertijo.getId(), idUsuario);

        verify(repositorioPartida).obtenerListaDePistas(acertijo.getId());
        verify(repositorioPartida).obtenerPartidaActivaPorUsuario(idUsuario);
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

        Partida partida = new Partida(LocalDateTime.now());
        when(repositorioPartida.obtenerPartidaActivaPorUsuario(idUsuario)).thenReturn(partida);

        this.servicioPartida.obtenerSiguientePista(acertijo.getId(), idUsuario);
        Pista pista = this.servicioPartida.obtenerSiguientePista(acertijo.getId(), idUsuario);

        verify(repositorioPartida, times(2)).obtenerListaDePistas(acertijo.getId());
        verify(repositorioPartida, times(2)).obtenerPartidaActivaPorUsuario(idUsuario);
        assertThat(pista, equalTo(listaDePistas.get(1)));
    }

    @Test
    public void deberiaDevolverTrueSiSeRespondioCorrectamenteElAcertijo(){
        Sala sala = new Sala(1, "La Mansión Misteriosa", Dificultad.PRINCIPIANTE, "Mansion", "Una noche tormentosa te encuentras atrapado en una vieja mansion llena de acertijos.",
                true, 10,"puerta-mansion.png");
        Acertijo acertijo = new Acertijo( "lalalal");
        acertijo.setTipo(TipoAcertijo.ADIVINANZA);
        Respuesta respuestaCorrecta = new Respuesta("Respuesta");
        Respuesta respuestaIngresada = new Respuesta("Respuesta");
        Long idUsuario = 1L;

        when(repositorioPartida.obtenerRespuestaCorrecta(acertijo.getId())).thenReturn(respuestaCorrecta);
        when(repositorioPartida.buscarAcertijoPorId(acertijo.getId())).thenReturn(acertijo);

        Partida partida = new Partida(LocalDateTime.now());
        when(repositorioPartida.obtenerPartidaActivaPorUsuario(idUsuario)).thenReturn(partida);

        Boolean validacionDeRespuesta = this.servicioPartida.validarRespuesta(acertijo.getId(),respuestaIngresada.getRespuesta(), idUsuario);

        verify(repositorioPartida).obtenerRespuestaCorrecta(acertijo.getId());
        verify(repositorioPartida).buscarAcertijoPorId(acertijo.getId());
        verify(repositorioPartida).obtenerPartidaActivaPorUsuario(idUsuario);
        assertTrue(validacionDeRespuesta);
    }

    @Test
    public void deberiaDevolverTrue_SiLaRespuestaIngresadaCONTIENELaRespuestaCorrecta(){
        Sala sala = new Sala(1, "La Mansión Misteriosa", Dificultad.PRINCIPIANTE, "Mansion", "Una noche tormentosa te encuentras atrapado en una vieja mansion llena de acertijos.",
                true, 10,"puerta-mansion.png");
        Acertijo acertijo = new Acertijo( "lalalal");
        acertijo.setTipo(TipoAcertijo.ADIVINANZA);
        Long idUsuario = 1L;

        Respuesta respuestaCorrecta = new Respuesta("Respuesta");
        Respuesta respuestaIngresada = new Respuesta("LA Respuesta INGRESADA");

        when(repositorioPartida.obtenerRespuestaCorrecta(acertijo.getId())).thenReturn(respuestaCorrecta);
        when(repositorioPartida.buscarAcertijoPorId(acertijo.getId())).thenReturn(acertijo);

        Partida partida = new Partida(LocalDateTime.now());
        when(repositorioPartida.obtenerPartidaActivaPorUsuario(idUsuario)).thenReturn(partida);

        Boolean validacionDeRespuesta = this.servicioPartida.validarRespuesta(acertijo.getId(),respuestaIngresada.getRespuesta(), idUsuario);

        verify(repositorioPartida).obtenerRespuestaCorrecta(acertijo.getId());
        verify(repositorioPartida).buscarAcertijoPorId(acertijo.getId());
        verify(repositorioPartida).obtenerPartidaActivaPorUsuario(idUsuario);
        assertTrue(validacionDeRespuesta);
    }

    @Test
    public void deberiaDevolverFalsoSiNOSeRespondioCorrectamenteElAcertijo(){
        Sala sala = new Sala(1, "La Mansión Misteriosa", Dificultad.PRINCIPIANTE, "Mansion", "Una noche tormentosa te encuentras atrapado en una vieja mansion llena de acertijos.",
                true, 10,"puerta-mansion.png");
        Acertijo acertijo = new Acertijo( "lalalal");
        acertijo.setTipo(TipoAcertijo.ADIVINANZA);
        Respuesta respuestaCorrecta = new Respuesta("Respuesta");
        Respuesta respuestaIngresada = new Respuesta("akhsdgauysduiaRespuestahbsduykhagsdygasdyi");
        Long idUsuario = 1L;

        when(repositorioPartida.obtenerRespuestaCorrecta(acertijo.getId())).thenReturn(respuestaCorrecta);
        when(repositorioPartida.buscarAcertijoPorId(acertijo.getId())).thenReturn(acertijo);

        Partida partida = new Partida(LocalDateTime.now());
        when(repositorioPartida.obtenerPartidaActivaPorUsuario(idUsuario)).thenReturn(partida);

        Boolean validacionDeRespuesta = this.servicioPartida.validarRespuesta(acertijo.getId(),respuestaIngresada.getRespuesta(), idUsuario);

        verify(repositorioPartida).obtenerRespuestaCorrecta(acertijo.getId());
        verify(repositorioPartida).buscarAcertijoPorId(acertijo.getId());
        verify(repositorioPartida).obtenerPartidaActivaPorUsuario(idUsuario);
        assertFalse(validacionDeRespuesta);
    }


    @Test
    public void deberiaDevolverUnaEtapaPorID(){
        Etapa etapa = new Etapa("Lobby", 1, "La puerta hacia la siguiente habitación está bloqueada por un candado, busca la clave en este acertijo.", "a.png");
        etapa.setId(1L);

        when(repositorioPartida.buscarEtapaPorId(etapa.getId())).thenReturn(etapa);

        Etapa etapaObtenida = this.servicioPartida.obtenerEtapaPorId(etapa.getId());

        verify(repositorioPartida).buscarEtapaPorId(etapa.getId());
        assertThat(etapaObtenida, equalTo(etapa));
    }

    @Test
    public void deberiaDevolverUnAcertijoPorID(){
        Acertijo acertijo = new Acertijo( "a1");
        acertijo.setId(1L);

        when(repositorioPartida.buscarAcertijoPorId(acertijo.getId())).thenReturn(acertijo);

        Acertijo acertijoObtenido = this.servicioPartida.buscarAcertijoPorId(acertijo.getId());

        verify(repositorioPartida).buscarAcertijoPorId(acertijo.getId());
        assertThat(acertijoObtenido, equalTo(acertijo));
    }

    @Test
    public void deberiaSolicitarAlRepoFinalizarUnaPartida(){
        Sala sala = new Sala(1, "La Mansión Misteriosa", Dificultad.PRINCIPIANTE, "Mansion", "Una noche tormentosa te encuentras atrapado en una vieja mansion llena de acertijos.",
                true, 10,"puerta-mansion.png");
        Partida partida = new Partida(LocalDateTime.now());
        partida.setSala(sala);
        Usuario usuario = new Usuario();
        usuario.setId(1L);

        when(repositorioPartida.obtenerPartidaActivaPorUsuario(usuario.getId())).thenReturn(partida);

        this.servicioPartida.finalizarPartida(usuario.getId(), true);

        verify(repositorioPartida).finalizarPartida(partida);
        verify(repositorioPartida).obtenerPartidaActivaPorUsuario(usuario.getId());
    }


}
