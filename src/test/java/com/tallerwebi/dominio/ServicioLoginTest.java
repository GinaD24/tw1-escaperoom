package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.CredencialesInvalidasException;
import com.tallerwebi.dominio.excepcion.DatosIncompletosException;
import com.tallerwebi.dominio.excepcion.EdadInvalidaException;
import com.tallerwebi.dominio.excepcion.UsuarioExistente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class ServicioLoginTest {

    private ServicioLoginImpl servicioLogin;
    private RepositorioUsuario repositorioUsuarioMock;

    @BeforeEach
    public void init() {
        repositorioUsuarioMock = Mockito.mock(RepositorioUsuario.class);
        servicioLogin = new ServicioLoginImpl(repositorioUsuarioMock);
    }

    @Test
    public void dadoQueTengoUnServicioLoginPuedoCrearUnRegistroExitoso() throws UsuarioExistente, EdadInvalidaException, DatosIncompletosException {
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setId(1L);
        nuevoUsuario.setEmail("franco@gmail.com");
        nuevoUsuario.setPassword("1234");
        nuevoUsuario.setFechaNacimiento(LocalDate.of(2003, 9, 13));
        nuevoUsuario.setNombre("Franco");
        nuevoUsuario.setApellido("T");
        nuevoUsuario.setActivo(true);
        nuevoUsuario.setRol("USUARIO");

        when(repositorioUsuarioMock.buscar("franco@gmail.com")).thenReturn(null);

        servicioLogin.registrar(nuevoUsuario);

        verify(repositorioUsuarioMock, times(1)).guardar(nuevoUsuario);

    }

    @Test
    public void dadoQueTengoUnServicioLoginPuedoHacerIniciarSesionDeFormaExitosa() throws CredencialesInvalidasException {
        Usuario usuarioCreado = new Usuario();
        usuarioCreado.setId(1L);
        usuarioCreado.setEmail("delfina@outlook.com");
        usuarioCreado.setNombre("Delfina");
        usuarioCreado.setPassword("1234");

        when(repositorioUsuarioMock.buscarUsuario("delfina@outlook.com", "1234")).thenReturn(usuarioCreado);

        Usuario usuarioLogueado = servicioLogin.consultarUsuario("delfina@outlook.com", "1234");

        assertThat(usuarioLogueado, is(usuarioCreado));

        verify(repositorioUsuarioMock, times(1)).buscarUsuario("delfina@outlook.com", "1234");

        verify(repositorioUsuarioMock, never()).guardar(any(Usuario.class));
    }

    @Test
    public void dadoQueTengoUnServicioLoginSiEscriboMalLaContraseniaObtengoUnaException(){
        when(repositorioUsuarioMock.buscarUsuario("diego@outlook.com", "claveIncorrecta")).thenReturn(null);

        assertThrows(CredencialesInvalidasException.class, () -> {
            servicioLogin.consultarUsuario("diego@outlook.com", "claveIncorrecta");
        });

        verify(repositorioUsuarioMock, times(1)).buscarUsuario("diego@outlook.com", "claveIncorrecta");
    }

    @Test
    public void dadoQueExisteUnServicioLoginNoPuedoTener2UsuariosConElMismoEmail(){
        String emailDuplicado = "diego@hotmail.com";

        Usuario usuarioExistente = new Usuario();
        usuarioExistente.setEmail(emailDuplicado);

        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setEmail(emailDuplicado);

        when(repositorioUsuarioMock.buscar(emailDuplicado)).thenReturn(usuarioExistente);

        assertThrows(UsuarioExistente.class, () -> {
            servicioLogin.registrar(nuevoUsuario);
        });

        verify(repositorioUsuarioMock, never()).guardar(any(Usuario.class));
    }

    @Test
    public void dadoQueTengoUnServicioLoginNoPuedoRegistrarUnUsuarioQueTengaMenosDe7Anios(){
        Usuario usuarioMenor = new Usuario();
        usuarioMenor.setFechaNacimiento(LocalDate.of(2019,8,10));

        assertThrows(EdadInvalidaException.class, () -> {
            servicioLogin.registrar(usuarioMenor);
        });

        verify(repositorioUsuarioMock, never()).guardar(any(Usuario.class));
    }





}
