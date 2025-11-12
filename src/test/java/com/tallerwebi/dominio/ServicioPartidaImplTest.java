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
import com.tallerwebi.dominio.interfaz.servicio.ValidadorAcertijo;
import com.tallerwebi.dominio.interfaz.servicio.ValidadorAcertijoFactory;
import com.tallerwebi.infraestructura.RepositorioPartidaImpl;
import com.tallerwebi.infraestructura.RepositorioSalaImpl;
import com.tallerwebi.infraestructura.RepositorioUsuarioImpl;
import com.tallerwebi.presentacion.AcertijoActualDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ServicioPartidaImplTest {

    private RepositorioPartida repositorioPartida;
    private RepositorioUsuario repositorioUsuario;
    private RepositorioSala repositorioSala;
    private ValidadorAcertijoFactory validadorFactory;
    private ServicioPartida servicioPartida;

    @BeforeEach
    public void init() {
        this.repositorioPartida = mock(RepositorioPartida.class);
        this.repositorioUsuario = mock(RepositorioUsuario.class);
        this.repositorioSala = mock(RepositorioSala.class);
        this.validadorFactory = mock(ValidadorAcertijoFactory.class);

        this.servicioPartida = new ServicioPartidaImpl(repositorioPartida, repositorioUsuario, repositorioSala, validadorFactory);
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
        when(this.repositorioSala.obtenerSalaPorId(sala.getId())).thenReturn(sala); // Mock para obtener la sala

        this.servicioPartida.guardarPartida(partida, usuario.getId(), sala.getId());

        verify(repositorioPartida).guardarPartida(partida);
    }

    @Test
    public void deberiaLanzarLaExcepcionSesionDeUsuarioExpirada_SiLeLlegaUnIdUsuarioNulo() {
        Sala sala = new Sala(1, "La Mansión Misteriosa", Dificultad.PRINCIPIANTE, "Mansion", "Una noche tormentosa te encuentras atrapado en una vieja mansion llena de acertijos.",
                true, 10,"puerta-mansion.png");
        Partida partida = new Partida(LocalDateTime.now());
        partida.setSala(sala);

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
        Long idUsuario = 1L;

        when(this.repositorioUsuario.obtenerUsuarioPorId(idUsuario)).thenReturn(null);
        when(this.repositorioSala.obtenerSalaPorId(sala.getId())).thenReturn(sala); // Se necesita este mock

        assertThrows(UsuarioInexistente.class, () -> {
            this.servicioPartida.guardarPartida(partida, idUsuario, sala.getId());
        });

    }

    @Test
    public void deberiaDevolverLaPrimeraEtapaDeLaSalaEnLaPartida(){
        Sala sala = new Sala(1, "La Mansión Misteriosa", Dificultad.PRINCIPIANTE, "Mansion", "Una noche tormentosa te encuentras atrapado en una vieja mansion llena de acertijos.",
                true, 10,"puerta-mansion.png");
        Etapa etapa = new Etapa("Lobby", 1, "La puerta hacia la siguiente habitación está bloqueada por un candado, busca la clave en este acertijo.", "a.png");
        etapa.setId(1L);

        when(repositorioPartida.obtenerEtapaPorNumero(sala.getId(), etapa.getNumero())).thenReturn(etapa);

        Etapa etapaObtenida = this.servicioPartida.obtenerEtapaPorNumero(sala.getId(), etapa.getNumero());

        verify(repositorioPartida).obtenerEtapaPorNumero(sala.getId(), etapa.getNumero());
        assertThat(etapaObtenida, equalTo(etapa));
    }

    @Test
    public void deberiaLanzarUnaExcepcionEtapaInexistente_CuandoLaEtapaObtenidaEsNula(){
        Sala sala = new Sala(1, "La Mansión Misteriosa", Dificultad.PRINCIPIANTE, "Mansion", "Una noche tormentosa te encuentras atrapado en una vieja mansion llena de acertijos.",
                true, 10,"puerta-mansion.png");
        Etapa etapa = new Etapa("Lobby", 1, "La puerta hacia la siguiente habitación está bloqueada por un candado, busca la clave en este acertijo.", "a.png");

        when(repositorioPartida.obtenerEtapaPorNumero(sala.getId(), etapa.getNumero())).thenReturn(null);

        assertThrows(EtapaInexistente.class, () -> {
            this.servicioPartida.obtenerEtapaPorNumero(sala.getId(), etapa.getNumero());
        });
    }

    @Test
    public void deberiaDevolverElAcertijoDeLaEtapaEnLaPartida(){
        Etapa etapa = new Etapa("Lobby", 1, "...", "a.png");
        etapa.setId(1L);
        Acertijo acertijo1 = new Acertijo("lalalal");
        List<Acertijo> listaDeAcertijos = new ArrayList<>();
        listaDeAcertijos.add(acertijo1);
        Long idUsuario = 1L;
        Usuario usuarioMock = new Usuario();

        when(repositorioPartida.obtenerListaDeAcertijos(etapa.getId())).thenReturn(listaDeAcertijos);
        when(repositorioPartida.obtenerAcertijosVistosPorUsuarioPorEtapa(anyLong(), anyLong())).thenReturn(new ArrayList<>()); // Evitar NullPointerException
        when(repositorioUsuario.obtenerUsuarioPorId(idUsuario)).thenReturn(usuarioMock);
        when(repositorioPartida.buscarEtapaPorId(etapa.getId())).thenReturn(etapa);

        Acertijo acertijoElegido = this.servicioPartida.obtenerAcertijo(etapa.getId(), idUsuario);

        assertNotNull(acertijoElegido);
        verify(repositorioPartida).obtenerListaDeAcertijos(etapa.getId());
        verify(repositorioPartida).registrarAcertijoMostrado(any());
    }

    @Test
    public void deberiaDevolverUnAcertijo_QueElUsuarioNoHayaVisto(){

        Etapa etapa = new Etapa("Lobby", 1, "La puerta hacia la siguiente habitación está bloqueada por un candado, busca la clave en este acertijo.", "a.png");
        etapa.setId(1L);
        Acertijo acertijoVisto = new Acertijo( "lalalal");
        Acertijo acertijoNoVisto1 = new Acertijo( "lelele");
        Acertijo acertijoNoVisto2 = new Acertijo( "lilili");

        List<Acertijo> listaDeAcertijos = new ArrayList<>();
        listaDeAcertijos.add(acertijoVisto);
        listaDeAcertijos.add(acertijoNoVisto1);
        listaDeAcertijos.add(acertijoNoVisto2);

        List<Acertijo> listaDeAcertijosVISTOS = new ArrayList<>();
        listaDeAcertijosVISTOS.add(acertijoVisto);

        Long idUsuario = 1L;
        Usuario usuarioMock = new Usuario();

        when(repositorioPartida.obtenerListaDeAcertijos(etapa.getId())).thenReturn(listaDeAcertijos);
        when(repositorioPartida.obtenerAcertijosVistosPorUsuarioPorEtapa(idUsuario, etapa.getId())).thenReturn(listaDeAcertijosVISTOS);

        when(repositorioUsuario.obtenerUsuarioPorId(idUsuario)).thenReturn(usuarioMock);
        when(repositorioPartida.buscarEtapaPorId(etapa.getId())).thenReturn(etapa);

        Acertijo acertijoElegido = this.servicioPartida.obtenerAcertijo(etapa.getId(), idUsuario);

        assertNotEquals(acertijoVisto, acertijoElegido);
        assertTrue(acertijoElegido.equals(acertijoNoVisto1) || acertijoElegido.equals(acertijoNoVisto2));

        verify(repositorioPartida).obtenerListaDeAcertijos(etapa.getId());
        verify(repositorioPartida).obtenerAcertijosVistosPorUsuarioPorEtapa(idUsuario, etapa.getId());
        verify(repositorioUsuario).obtenerUsuarioPorId(idUsuario);
        verify(repositorioPartida).buscarEtapaPorId(etapa.getId());
        verify(repositorioPartida).registrarAcertijoMostrado(any());
    }

    @Test
    public void deberiaDevolverTrueSiSeRespondioCorrectamenteElAcertijo_DeTipoADIVINANZA(){
        AcertijoActualDTO acertijoDTO =  new AcertijoActualDTO();
        acertijoDTO.setTipo(TipoAcertijo.ADIVINANZA);
        acertijoDTO.setRespuestaCorrecta("Respuesta");

        Long idUsuario = 1L;
        Partida partida = new Partida(LocalDateTime.now());
        ValidadorAcertijo validadorMock = mock(ValidadorAcertijo.class); // Mock del validador

        when(repositorioPartida.obtenerPartidaActivaPorUsuario(idUsuario)).thenReturn(partida);
        // Configura el Factory para que devuelva el mock específico para ADIVINANZA
        when(validadorFactory.getValidador(TipoAcertijo.ADIVINANZA)).thenReturn(validadorMock);
        // Simula que la validación fue exitosa
        when(validadorMock.validar(eq(acertijoDTO), eq("Respuesta"), eq(partida), isNull())).thenReturn(true);

        Boolean validacionDeRespuesta = this.servicioPartida.validarRespuesta(acertijoDTO, "Respuesta", idUsuario, null);

        assertTrue(validacionDeRespuesta);
        // Verifica que se haya llamado al validador correcto
        verify(validadorMock).validar(eq(acertijoDTO), eq("Respuesta"), eq(partida), isNull());
    }

    @Test
    public void deberiaDevolverTrue_SiLaRespuestaDelAcertijoADIVINANZA_CONTIENELaRespuestaCorrecta(){
        AcertijoActualDTO acertijoDTO =  new AcertijoActualDTO();
        acertijoDTO.setTipo(TipoAcertijo.ADIVINANZA);
        acertijoDTO.setRespuestaCorrecta("Respuesta");

        Long idUsuario = 1L;
        String respuestaIngresada = "LA Respuesta INGRESADA"; // Respuesta con contenido
        Partida partida = new Partida(LocalDateTime.now());
        ValidadorAcertijo validadorMock = mock(ValidadorAcertijo.class);

        when(repositorioPartida.obtenerPartidaActivaPorUsuario(idUsuario)).thenReturn(partida);
        when(validadorFactory.getValidador(TipoAcertijo.ADIVINANZA)).thenReturn(validadorMock);
        when(validadorMock.validar(eq(acertijoDTO), eq(respuestaIngresada), eq(partida), isNull())).thenReturn(true);

        Boolean validacionDeRespuesta = this.servicioPartida.validarRespuesta(acertijoDTO, respuestaIngresada, idUsuario, null);

        verify(repositorioPartida).obtenerPartidaActivaPorUsuario(idUsuario);
        verify(validadorMock).validar(eq(acertijoDTO), eq(respuestaIngresada), eq(partida), isNull());
        assertTrue(validacionDeRespuesta);
    }

    @Test
    public void deberiaDevolverFalsoSiNOSeRespondioCorrectamenteElAcertijo_DeTipoADIVINANZA(){
        AcertijoActualDTO acertijoDTO =  new AcertijoActualDTO();
        acertijoDTO.setId(1L);
        acertijoDTO.setTipo(TipoAcertijo.ADIVINANZA);
        acertijoDTO.setRespuestaCorrecta("Respuesta");

        String respuestaIngresada = "akhsdgauysduiaRespuestahbsduykhagsdygasdyi";
        Long idUsuario = 1L;

        Partida partida = new Partida(LocalDateTime.now());
        ValidadorAcertijo validadorMock = mock(ValidadorAcertijo.class);

        when(repositorioPartida.obtenerPartidaActivaPorUsuario(idUsuario)).thenReturn(partida);
        when(validadorFactory.getValidador(TipoAcertijo.ADIVINANZA)).thenReturn(validadorMock);
        when(validadorMock.validar(eq(acertijoDTO), eq(respuestaIngresada), eq(partida), isNull())).thenReturn(false);

        Boolean validacionDeRespuesta = this.servicioPartida.validarRespuesta(acertijoDTO, respuestaIngresada, idUsuario, null);

        verify(repositorioPartida).obtenerPartidaActivaPorUsuario(idUsuario);
        verify(validadorMock).validar(eq(acertijoDTO), eq(respuestaIngresada), eq(partida), isNull());
        assertFalse(validacionDeRespuesta);
    }

    @Test
    public void deberiaDevolverTrueSiORDENOCorrectamenteElAcertijo_DeTipoORDENAR_IMAGEN(){
        Partida partida = new Partida(LocalDateTime.now());
        AcertijoActualDTO acertijoDTO =  new AcertijoActualDTO();
        acertijoDTO.setId(1L);
        acertijoDTO.setTipo(TipoAcertijo.ORDENAR_IMAGEN);

        Long idUsuario = 1L;
        String ordenIngresado = "1,2,3";
        ValidadorAcertijo validadorMock = mock(ValidadorAcertijo.class);

        when(repositorioPartida.obtenerPartidaActivaPorUsuario(idUsuario)).thenReturn(partida);
        when(validadorFactory.getValidador(TipoAcertijo.ORDENAR_IMAGEN)).thenReturn(validadorMock);
        when(validadorMock.validar(eq(acertijoDTO), eq(ordenIngresado), eq(partida), isNull())).thenReturn(true);

        Boolean validacionDeRespuesta = this.servicioPartida.validarRespuesta(acertijoDTO, ordenIngresado, idUsuario, null);

        verify(repositorioPartida).obtenerPartidaActivaPorUsuario(idUsuario);
        verify(validadorMock).validar(eq(acertijoDTO), eq(ordenIngresado), eq(partida), isNull());
        assertTrue(validacionDeRespuesta);
    }

    @Test
    public void deberiaDevolverFalseSi_NoORDENOCorrectamenteElAcertijo_DeTipoORDENAR_IMAGEN(){
        Partida partida = new Partida(LocalDateTime.now());
        AcertijoActualDTO acertijoDTO =  new AcertijoActualDTO();
        acertijoDTO.setId(1L);
        acertijoDTO.setTipo(TipoAcertijo.ORDENAR_IMAGEN);

        Long idUsuario = 1L;
        String ordenIngresado = "2,1,3";
        ValidadorAcertijo validadorMock = mock(ValidadorAcertijo.class);

        when(repositorioPartida.obtenerPartidaActivaPorUsuario(idUsuario)).thenReturn(partida);
        when(validadorFactory.getValidador(TipoAcertijo.ORDENAR_IMAGEN)).thenReturn(validadorMock);
        when(validadorMock.validar(eq(acertijoDTO), eq(ordenIngresado), eq(partida), isNull())).thenReturn(false);

        Boolean validacionDeRespuesta = this.servicioPartida.validarRespuesta(acertijoDTO, ordenIngresado, idUsuario, null);

        verify(repositorioPartida).obtenerPartidaActivaPorUsuario(idUsuario);
        verify(validadorMock).validar(eq(acertijoDTO), eq(ordenIngresado), eq(partida), isNull());
        assertFalse(validacionDeRespuesta);
    }

    @Test
    public void deberiaDevolverTrueSiResolvioCorrectamenteElAcertijo_DeTipoDRAG_DROP(){
        Partida partida = new Partida(LocalDateTime.now());
        AcertijoActualDTO acertijoDTO =  new AcertijoActualDTO();
        acertijoDTO.setId(1L);
        acertijoDTO.setTipo(TipoAcertijo.DRAG_DROP);

        Long idUsuario = 1L;
        String ordenIngresado = "1:cat1,2:cat2";
        ValidadorAcertijo validadorMock = mock(ValidadorAcertijo.class);

        when(repositorioPartida.obtenerPartidaActivaPorUsuario(idUsuario)).thenReturn(partida);
        when(validadorFactory.getValidador(TipoAcertijo.DRAG_DROP)).thenReturn(validadorMock);
        when(validadorMock.validar(eq(acertijoDTO), eq(ordenIngresado), eq(partida), isNull())).thenReturn(true);

        Boolean validacionDeRespuesta = this.servicioPartida.validarRespuesta(acertijoDTO, ordenIngresado, idUsuario, null);

        verify(repositorioPartida).obtenerPartidaActivaPorUsuario(idUsuario);
        verify(validadorMock).validar(eq(acertijoDTO), eq(ordenIngresado), eq(partida), isNull());
        assertTrue(validacionDeRespuesta);
    }

    @Test
    public void deberiaDevolverFalseSiNOResolvioCorrectamenteElAcertijo_DeTipoDRAG_DROP(){
        Partida partida = new Partida(LocalDateTime.now());
        AcertijoActualDTO acertijoDTO =  new AcertijoActualDTO();
        acertijoDTO.setId(1L);
        acertijoDTO.setTipo(TipoAcertijo.DRAG_DROP);

        Long idUsuario = 1L;
        String ordenIngresado = "1:cat2,2:cat1";
        ValidadorAcertijo validadorMock = mock(ValidadorAcertijo.class);

        when(repositorioPartida.obtenerPartidaActivaPorUsuario(idUsuario)).thenReturn(partida);
        when(validadorFactory.getValidador(TipoAcertijo.DRAG_DROP)).thenReturn(validadorMock);
        when(validadorMock.validar(eq(acertijoDTO), eq(ordenIngresado), eq(partida), isNull())).thenReturn(false);

        Boolean validacionDeRespuesta = this.servicioPartida.validarRespuesta(acertijoDTO, ordenIngresado, idUsuario, null);

        verify(repositorioPartida).obtenerPartidaActivaPorUsuario(idUsuario);
        verify(validadorMock).validar(eq(acertijoDTO), eq(ordenIngresado), eq(partida), isNull());
        assertFalse(validacionDeRespuesta);
    }

    @Test
    public void deberiaDevolverTrueSiCompletoCorrectamenteElAcertijo_DeTipoSECUENCIA(){
        Partida partida = new Partida(LocalDateTime.now());
        AcertijoActualDTO acertijoDTO =  new AcertijoActualDTO();
        acertijoDTO.setId(1L);
        acertijoDTO.setTipo(TipoAcertijo.SECUENCIA);

        String ordenCorrecto = "1,2,3";
        Long idUsuario = 1L;
        String ordenIngresado = "1,2,3";
        ValidadorAcertijo validadorMock = mock(ValidadorAcertijo.class);

        when(repositorioPartida.obtenerPartidaActivaPorUsuario(idUsuario)).thenReturn(partida);
        when(validadorFactory.getValidador(TipoAcertijo.SECUENCIA)).thenReturn(validadorMock);
        when(validadorMock.validar(eq(acertijoDTO), eq(ordenIngresado), eq(partida), eq(ordenCorrecto))).thenReturn(true);

        Boolean validacionDeRespuesta = this.servicioPartida.validarRespuesta(acertijoDTO, ordenIngresado, idUsuario, ordenCorrecto);

        verify(repositorioPartida).obtenerPartidaActivaPorUsuario(idUsuario);
        verify(validadorMock).validar(eq(acertijoDTO), eq(ordenIngresado), eq(partida), eq(ordenCorrecto));
        assertTrue(validacionDeRespuesta);
    }

    @Test
    public void deberiaDevolverFalseSiCompletoIncorrectamenteElAcertijo_DeTipoSECUENCIA(){
        Partida partida = new Partida(LocalDateTime.now());
        AcertijoActualDTO acertijoDTO =  new AcertijoActualDTO();
        acertijoDTO.setId(1L);
        acertijoDTO.setTipo(TipoAcertijo.SECUENCIA);

        String ordenCorrecto = "2,3,1";
        Long idUsuario = 1L;
        String ordenIngresado = "1,2,3";
        ValidadorAcertijo validadorMock = mock(ValidadorAcertijo.class);

        when(repositorioPartida.obtenerPartidaActivaPorUsuario(idUsuario)).thenReturn(partida);
        when(validadorFactory.getValidador(TipoAcertijo.SECUENCIA)).thenReturn(validadorMock);
        when(validadorMock.validar(eq(acertijoDTO), eq(ordenIngresado), eq(partida), eq(ordenCorrecto))).thenReturn(false);

        Boolean validacionDeRespuesta = this.servicioPartida.validarRespuesta(acertijoDTO, ordenIngresado, idUsuario, ordenCorrecto);

        verify(repositorioPartida).obtenerPartidaActivaPorUsuario(idUsuario);
        verify(validadorMock).validar(eq(acertijoDTO), eq(ordenIngresado), eq(partida), eq(ordenCorrecto));
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
        partida.setInicio(LocalDateTime.now().minusMinutes(5));

        when(repositorioPartida.obtenerPartidaActivaPorUsuario(usuario.getId())).thenReturn(partida);

        this.servicioPartida.finalizarPartida(usuario.getId(), true);

        verify(repositorioPartida).finalizarPartida(partida);
        verify(repositorioPartida).obtenerPartidaActivaPorUsuario(usuario.getId());
    }

    @Test
    public void deberiaDevolverUnaPartidaActivaPorUsuario(){
        Sala sala = new Sala(1, "La Mansión Misteriosa", Dificultad.PRINCIPIANTE, "Mansion", "Una noche tormentosa te encuentras atrapado en una vieja mansion llena de acertijos.",
                true, 10,"puerta-mansion.png");
        Usuario usuario = new Usuario();
        Partida partida = new Partida(LocalDateTime.now());
        partida.setSala(sala);
        partida.setUsuario(usuario);

        when(repositorioPartida.obtenerPartidaActivaPorUsuario(usuario.getId())).thenReturn(partida);

        Partida partidaObtenida =this.servicioPartida.obtenerPartidaActivaPorIdUsuario(usuario.getId());

        assertThat(partidaObtenida, equalTo(partida));
        verify(repositorioPartida).obtenerPartidaActivaPorUsuario(usuario.getId());
    }

    @Test
    public void deberiaDevolverUnaListaDeCategoriasCreadasAPartirDeLasCategoriasCorrectasDelAcertijoDragDrop(){
        DragDropItem item1 = new DragDropItem();
        item1.setCategoriaCorrecta("cat1");
        DragDropItem item2 = new DragDropItem();
        item2.setCategoriaCorrecta("cat2");

        Set<DragDropItem> itemsDD = new HashSet<>();
        itemsDD.add(item1);
        itemsDD.add(item2);

        Acertijo acertijo = new Acertijo( "a1");
        acertijo.setId(1L);
        acertijo.setTipo(TipoAcertijo.DRAG_DROP);
        acertijo.setDragDropItems(itemsDD);

        when(repositorioPartida.buscarAcertijoPorId(acertijo.getId())).thenReturn(acertijo);

        List <String> listaObtenida = this.servicioPartida.obtenerCategoriasDelAcertijoDragDrop(acertijo.getId());

        assertThat(listaObtenida, containsInAnyOrder("cat1", "cat2"));
        verify(repositorioPartida).buscarAcertijoPorId(acertijo.getId());
    }

    @Test
    public void deberiaDevolverUnaPartida_CuandoLaBuscoporId(){
        Partida partida = new Partida(LocalDateTime.now());
        partida.setId(1L);

        when(repositorioPartida.buscarPartidaPorId(partida.getId())).thenReturn(partida);

        Partida partidaObtenida = this.servicioPartida. buscarPartidaPorId(partida.getId());

        assertThat(partidaObtenida, equalTo(partida));
        verify(repositorioPartida).buscarPartidaPorId(partida.getId());
    }
}
