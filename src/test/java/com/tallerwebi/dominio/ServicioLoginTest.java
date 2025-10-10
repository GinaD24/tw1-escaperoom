package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.*;
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
    public void dadoQueTengoUnServicioLoginPuedoCrearUnRegistroExitoso() throws UsuarioExistente, EdadInvalidaException, DatosIncompletosException, ValidacionInvalidaException {
        // Preparación: Usuario completo y válido
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setId(1L);
        nuevoUsuario.setEmail("franco@gmail.com");  // Válido
        nuevoUsuario.setPassword("password123");    // >=8 chars (cambiado de "1234")
        nuevoUsuario.setFechaNacimiento(LocalDate.of(2003, 9, 13));  // Edad >7
        nuevoUsuario.setNombre("Franco");
        nuevoUsuario.setApellido("T");
        nuevoUsuario.setNombreUsuario("francot");   // ← AGREGADO: Obligatorio y >=3 chars
        nuevoUsuario.setActivo(true);
        nuevoUsuario.setRol("USUARIO");
        // Mock: No existe email ni nombreUsuario
        when(repositorioUsuarioMock.buscar("franco@gmail.com")).thenReturn(null);
        when(repositorioUsuarioMock.buscarPorNombreUsuario("francot")).thenReturn(null);
        // Ejecución
        servicioLogin.registrar(nuevoUsuario);
        // Validación
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
    public void dadoQueExisteUnServicioLoginNoPuedoTener2UsuariosConElMismoEmail() {
        // Preparación: Usuario existente (solo email para mock)
        String emailDuplicado = "diego@hotmail.com";
        Usuario usuarioExistente = new Usuario();
        usuarioExistente.setEmail(emailDuplicado);
        // Nuevo usuario: Completo y válido, pero email duplicado
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setEmail(emailDuplicado);
        nuevoUsuario.setPassword("password123");  // Válido
        nuevoUsuario.setFechaNacimiento(LocalDate.of(2000, 1, 1));  // Válido
        nuevoUsuario.setNombre("Diego");
        nuevoUsuario.setApellido("G");
        nuevoUsuario.setNombreUsuario("diegog");  // Válido
        nuevoUsuario.setRol("USUARIO");
        // Mock: Email existe, nombreUsuario no (para que pase esa validación)
        when(repositorioUsuarioMock.buscar(emailDuplicado)).thenReturn(usuarioExistente);
        when(repositorioUsuarioMock.buscarPorNombreUsuario("diegog")).thenReturn(null);
        // Ejecución y Validación
        assertThrows(UsuarioExistente.class, () -> {
            servicioLogin.registrar(nuevoUsuario);
        });
        verify(repositorioUsuarioMock, never()).guardar(any(Usuario.class));
    }
    @Test
    public void dadoQueTengoUnServicioLoginNoPuedoRegistrarUnUsuarioQueTengaMenosDe7Anios(){
        // Preparación: Usuario completo y válido, pero edad <7
        Usuario usuarioMenor = new Usuario();
        usuarioMenor.setEmail("menor@gmail.com");  // Válido
        usuarioMenor.setPassword("password123");   // Válido
        usuarioMenor.setFechaNacimiento(LocalDate.of(2019, 8, 10));  // Edad ~5 años <7
        usuarioMenor.setNombre("Menor");
        usuarioMenor.setApellido("Test");
        usuarioMenor.setNombreUsuario("menortest");  // Válido
        usuarioMenor.setRol("USUARIO");
        // Mock: No duplicados
        when(repositorioUsuarioMock.buscar("menor@gmail.com")).thenReturn(null);
        when(repositorioUsuarioMock.buscarPorNombreUsuario("menortest")).thenReturn(null);
        // Ejecución y Validación
        assertThrows(EdadInvalidaException.class, () -> {
            servicioLogin.registrar(usuarioMenor);
        });
        verify(repositorioUsuarioMock, never()).guardar(any(Usuario.class));
    }


    @Test
    public void dadoQueExisteUnNombreUsuarioDuplicadoNoPuedoRegistrar() {
        // Preparación
        Usuario usuarioExistente = new Usuario();
        usuarioExistente.setNombreUsuario("alan123");
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setEmail("alan@email.com");
        nuevoUsuario.setNombre("alan");
        nuevoUsuario.setApellido("thomas ");
        nuevoUsuario.setPassword("123456789");
        nuevoUsuario.setNombreUsuario("alan123");
        nuevoUsuario.setFechaNacimiento(LocalDate.of(2000, 1, 1));
        when(repositorioUsuarioMock.buscar("alan@email.com")).thenReturn(null);
        when(repositorioUsuarioMock.buscarPorNombreUsuario("alan123")).thenReturn(usuarioExistente);

        assertThrows(UsuarioExistente.class, () -> {
            servicioLogin.registrar(nuevoUsuario);
        });

        verify(repositorioUsuarioMock, never()).guardar(any(Usuario.class));
    }

    @Test
    public void dadoQuePasswordEsCortoNoPuedoRegistrar() {
        Usuario usuario = new Usuario();
        usuario.setEmail("alan@email.com");
        usuario.setNombre("alan");
        usuario.setApellido("thomas ");
        usuario.setPassword("123");
        usuario.setNombreUsuario("alan123");
        usuario.setFechaNacimiento(LocalDate.of(2000, 1, 1));
        when(repositorioUsuarioMock.buscar("alan@email.com")).thenReturn(null);
        when(repositorioUsuarioMock.buscarPorNombreUsuario("alan123")).thenReturn(null);

        assertThrows(ValidacionInvalidaException.class, () -> {
            servicioLogin.registrar(usuario);
        });

        verify(repositorioUsuarioMock, never()).guardar(any(Usuario.class));
        }

    @Test
    public void dadoQueTengoUnServicioLoginElEmailRegistradoDebeTenerUnFormatoValido(){
        Usuario nuevoUsuario  = new Usuario();
        nuevoUsuario.setPassword("martinarrobagmailcom");

        assertThrows(DatosIncompletosException.class, () -> {
            servicioLogin.registrar(nuevoUsuario);
        });

        verify(repositorioUsuarioMock, never()).guardar(any(Usuario.class));
    }

    @Test
    public void dadoQueTengoUnServicioLoginNoPuedoIniciarSesionSiLasCredencialesEstanVacias(){
        Usuario usuarioExistente = new Usuario();
        usuarioExistente.setEmail(null);
        usuarioExistente.setPassword(null);

        when(repositorioUsuarioMock.buscarUsuario(null,  null)).thenReturn(usuarioExistente);

        assertThrows(CredencialesInvalidasException.class, () -> {
            servicioLogin.consultarUsuario(usuarioExistente.getEmail(), usuarioExistente.getPassword());
        });

        verify(repositorioUsuarioMock, never()).guardar(any(Usuario.class));
    }


}
