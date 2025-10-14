package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.*;
import com.tallerwebi.infraestructura.RepositorioUsuarioImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.tallerwebi.dominio.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ServicioPerfilJugadorTest {

    @Mock
    private RepositorioUsuario repositorioUsuario;

    @Mock
    private RankingRepository rankingRepository;

    @Mock
    private RepositorioLogro repositorioLogro;

    @InjectMocks
    private ServicioPerfilJugador servicioPerfilJugador;

    private Usuario usuarioMock;
    private Logro logro1;
    private Logro logro2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        logro1 = new Logro("Escapista Novato", "Completó su primera sala");
        logro2 = new Logro("Velocista", "Terminó una sala en menos de 10 minutos");
        logro1.setNombre("Logro 1");
        logro2.setNombre("Logro 2");

        usuarioMock = new Usuario();
        usuarioMock.setId(1L);
        usuarioMock.setNombreUsuario("Jugador.Prueba");
        usuarioMock.setFotoPerfil("/img/perfil-default.png");
        usuarioMock.setLogrosFavoritos(List.of(logro1));
    }


    @Test
    void deberiaObtenerPerfilCorrectamente() {
        when(repositorioUsuario.buscarPorId(1L)).thenReturn(usuarioMock);


        PerfilJugador perfil = servicioPerfilJugador.obtenerPerfil(1L);

        assertNotNull(perfil);
        assertEquals("Jugador.Prueba", perfil.getNombre());
        assertEquals("/img/perfil-default.png", perfil.getFotoPerfil());
        assertEquals(1, perfil.getLogrosFavoritos().size());

        verify(repositorioUsuario, times(1)).buscarPorId(1L);
    }

    @Test
    void deberiaLanzarErrorSiUsuarioNoExiste() {
        when(repositorioUsuario.buscarPorId(99L)).thenReturn(null);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> servicioPerfilJugador.obtenerPerfil(99L));

        assertEquals("Usuario no encontrado con ID: 99", ex.getMessage());
    }


    @Test
    void deberiaActualizarNombreYLogrosCorrectamente() {
        when(repositorioUsuario.buscarPorId(1L)).thenReturn(usuarioMock);
        when(repositorioLogro.buscar(10L)).thenReturn(logro1);
        when(repositorioLogro.buscar(11L)).thenReturn(logro2);

        List<Long> nuevosLogros = List.of(10L, 11L);

        servicioPerfilJugador.actualizarPerfil(1L, "NuevoNombre", null, nuevosLogros);

        verify(repositorioUsuario).modificar(usuarioMock);
        assertEquals("NuevoNombre", usuarioMock.getNombreUsuario());
        assertEquals(2, usuarioMock.getLogrosFavoritos().size());
    }

    @Test
    void deberiaLanzarErrorSiLogroNoExiste() {
        when(repositorioUsuario.buscarPorId(1L)).thenReturn(usuarioMock);
        when(repositorioLogro.buscar(10L)).thenReturn(null);

        List<Long> logrosInvalidos = List.of(10L);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> servicioPerfilJugador.actualizarPerfil(1L, "NombreValido", null, logrosInvalidos));

        assertEquals("Logro no encontrado con ID: 10", ex.getMessage());
    }

    @Test
    void deberiaLanzarErrorPorNombreInvalido() {
        when(repositorioUsuario.buscarPorId(1L)).thenReturn(usuarioMock);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> servicioPerfilJugador.actualizarPerfil(1L, "abc", null, null));

        assertTrue(ex.getMessage().contains("no es válido"));
    }


    @Test
    void deberiaActualizarFotoCorrectamente() {
        when(repositorioUsuario.buscarPorId(1L)).thenReturn(usuarioMock);

        servicioPerfilJugador.actualizarFotoPerfil(1L, "nueva-foto.png");

        verify(repositorioUsuario).modificar(usuarioMock);
        assertEquals("nueva-foto.png", usuarioMock.getFotoPerfil());
    }



    @Test
    void deberiaObtenerListaDeLogros() {
        when(repositorioLogro.obtenerTodosLosLogros()).thenReturn(List.of(logro1, logro2));

        List<Logro> resultado = servicioPerfilJugador.obtenerTodosLosLogros();

        assertEquals(2, resultado.size());
        verify(repositorioLogro, times(1)).obtenerTodosLosLogros();
    }

    @Test
    void deberiaLanzarErrorSiNoHayLogros() {
        when(repositorioLogro.obtenerTodosLosLogros()).thenReturn(Collections.emptyList());

        assertThrows(RuntimeException.class,
                () -> servicioPerfilJugador.obtenerTodosLosLogros());
    }
}