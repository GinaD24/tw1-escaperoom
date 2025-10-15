package com.tallerwebi.dominio;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.servlet.http.HttpSession;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class ServicioPerfilTest {
    @Mock
    private RepositorioUsuario repositorioUsuario;

    @Mock
    private HttpSession sessionMock;

    @Mock
    private RepositorioLogro repositorioLogro;

    @InjectMocks
    private ServicioPerfilImpl servicioPerfilJugador;

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
    }


    @Test
    void deberiaObtenerPerfilCorrectamente() {
        when(repositorioUsuario.obtenerUsuarioPorId(1L)).thenReturn(usuarioMock);
        when(sessionMock.getAttribute("id_usuario")).thenReturn(1L);

        Usuario perfil = servicioPerfilJugador.obtenerPerfil(1L);

        assertNotNull(perfil);
        assertEquals("Jugador.Prueba", perfil.getNombreUsuario());

        verify(repositorioUsuario, times(1)).obtenerUsuarioPorId(1L);
    }

    @Test
    void deberiaLanzarErrorSiUsuarioNoExiste() {
        when(repositorioUsuario.obtenerUsuarioPorId(99L)).thenReturn(null);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> servicioPerfilJugador.obtenerPerfil(99L));

        assertEquals("Usuario no encontrado con ID: 99", ex.getMessage());
    }


}
