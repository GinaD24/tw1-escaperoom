package com.tallerwebi.dominio;

import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.excepcion.DatosIncompletosException;
import com.tallerwebi.dominio.excepcion.ValidacionInvalidaException;
import com.tallerwebi.dominio.excepcion.UsuarioExistente;
import com.tallerwebi.dominio.interfaz.repositorio.RepositorioUsuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ServicioEditarPerfilTest {

    @Mock
    private RepositorioUsuario repositorioUsuario;

    @InjectMocks
    private ServicioEditarPerfilImpl servicioEditarPerfil;

    private Usuario usuarioExistente;
    private DatosEdicionPerfilDTO datosValidos;
    private DatosEdicionPerfilDTO datosInvalidos;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        usuarioExistente = mock(Usuario.class);
        when(usuarioExistente.getId()).thenReturn(1L);
        when(usuarioExistente.getNombreUsuario()).thenReturn("usuarioViejo");
        when(usuarioExistente.getEmail()).thenReturn("viejo@email.com");
        when(usuarioExistente.getFechaNacimiento()).thenReturn(LocalDate.of(2000, 1, 1));
        when(usuarioExistente.getFotoPerfil()).thenReturn("/img/vieja.png");

        datosValidos = new DatosEdicionPerfilDTO();
        datosValidos.setId(1L);
        datosValidos.setNombreUsuario("usuarioNuevo");
        datosValidos.setEmail("nuevo@email.com");
        datosValidos.setFechaNacimiento(LocalDate.of(1995, 1, 1));
        datosValidos.setUrlFotoPerfil("/img/nueva.png");

        datosInvalidos = new DatosEdicionPerfilDTO();
        datosInvalidos.setNombreUsuario("");
        datosInvalidos.setEmail("emailinvalido");
        datosInvalidos.setFechaNacimiento(LocalDate.now().plusDays(1));
    }

    @Test
    void dadoQueExisteUnUsuarioCuandoObtengoDatosPerfilDeberiaDevolverLosDatosDelUsuario() {
        when(repositorioUsuario.obtenerUsuarioPorId(1L)).thenReturn(usuarioExistente);

        DatosEdicionPerfilDTO resultado = servicioEditarPerfil.obtenerDatosPerfil(1L);

        assertNotNull(resultado);
        assertThat(resultado.getNombreUsuario(), equalTo("usuarioViejo"));
        assertThat(resultado.getEmail(), equalTo("viejo@email.com"));
        verify(repositorioUsuario, times(1)).obtenerUsuarioPorId(1L);
    }

    @Test
    void dadoQueDatosSonValidosYUnicosCuandoActualizoPerfilDeberiaGuardarCambios() throws UsuarioExistente {
        when(repositorioUsuario.obtenerUsuarioPorId(1L)).thenReturn(usuarioExistente);
        when(repositorioUsuario.buscar("nuevo@email.com")).thenReturn(null);
        when(repositorioUsuario.buscarPorNombreUsuario("usuarioNuevo")).thenReturn(null);

        servicioEditarPerfil.actualizarPerfil(datosValidos);

        verify(usuarioExistente, times(1)).setNombreUsuario("usuarioNuevo");
        verify(usuarioExistente, times(1)).setEmail("nuevo@email.com");
        verify(repositorioUsuario, times(1)).modificar(usuarioExistente);
    }

    @Test
    void dadoQueEmailYaExisteCuandoActualizoPerfilDeberiaLanzarExcepcionUsuarioExistente() {
        when(repositorioUsuario.obtenerUsuarioPorId(1L)).thenReturn(usuarioExistente);
        when(repositorioUsuario.buscar("nuevo@email.com")).thenReturn(mock(Usuario.class));

        UsuarioExistente exception = assertThrows(UsuarioExistente.class, () -> {
            servicioEditarPerfil.actualizarPerfil(datosValidos);
        });
        assertThat(exception.getMessage(), equalTo("El email ya está registrado."));
    }

    @Test
    void dadoQueNombreUsuarioYaExisteCuandoActualizoPerfilDeberiaLanzarExcepcionUsuarioExistente() {
        when(repositorioUsuario.obtenerUsuarioPorId(1L)).thenReturn(usuarioExistente);
        when(repositorioUsuario.buscar("nuevo@email.com")).thenReturn(null);
        when(repositorioUsuario.buscarPorNombreUsuario("usuarioNuevo")).thenReturn(mock(Usuario.class));

        UsuarioExistente exception = assertThrows(UsuarioExistente.class, () -> {
            servicioEditarPerfil.actualizarPerfil(datosValidos);
        });
        assertThat(exception.getMessage(), equalTo("El nombre de usuario ya está en uso."));
    }

    @Test
    void dadoQueUsuarioNoExisteCuandoActualizoPerfilDeberiaActualizarDatosYGuardarCambios() {

        when(repositorioUsuario.obtenerUsuarioPorId(1L)).thenReturn(usuarioExistente);
        when(repositorioUsuario.buscarPorEmail(anyString())).thenReturn(null);
        when(repositorioUsuario.buscarPorNombreUsuario(anyString())).thenReturn(null);

        DatosEdicionPerfilDTO datosValidos = new DatosEdicionPerfilDTO();
        datosValidos.setId(1L);
        datosValidos.setNombreUsuario("usuarioNuevo");
        datosValidos.setEmail("nuevo@email.com");
        datosValidos.setFechaNacimiento(LocalDate.now().minusYears(10));
        datosValidos.setUrlFotoPerfil("/img/default.png");


        servicioEditarPerfil.actualizarPerfil(datosValidos);

        verify(repositorioUsuario, times(1)).modificar(any(Usuario.class));
        verify(repositorioUsuario, never()).guardar(any(Usuario.class));
    }



    @Test
    void dadoQueDatosSonCompletosYValidosCuandoValidoDatosNoDeberiaLanzarExcepcion() throws DatosIncompletosException, ValidacionInvalidaException {
        datosValidos.validarDatos();
    }

    @Test
    void dadoQueNombreUsuarioEstaVacioCuandoValidoDatosDeberiaLanzarExcepcionDatosIncompletos() {
        DatosEdicionPerfilDTO dto = new DatosEdicionPerfilDTO();
        dto.setNombreUsuario("");
        dto.setEmail("test@email.com");
        dto.setFechaNacimiento(LocalDate.of(2000, 1, 1));

        DatosIncompletosException exception = assertThrows(DatosIncompletosException.class, () -> {
            dto.validarDatos();
        });
        assertThat(exception.getMessage(), equalTo("El nombre de usuario es obligatorio."));
    }

    @Test
    void dadoQueEmailEstaVacioCuandoValidoDatosDeberiaLanzarExcepcionDatosIncompletos() {
        DatosEdicionPerfilDTO dto = new DatosEdicionPerfilDTO();
        dto.setNombreUsuario("usuario");
        dto.setEmail("");
        dto.setFechaNacimiento(LocalDate.of(2000, 1, 1));

        DatosIncompletosException exception = assertThrows(DatosIncompletosException.class, () -> {
            dto.validarDatos();
        });
        assertThat(exception.getMessage(), equalTo("El email es obligatorio."));
    }

    @Test
    void dadoQueFechaNacimientoEsNullCuandoValidoDatosDeberiaLanzarExcepcionDatosIncompletos() {
        DatosEdicionPerfilDTO dto = new DatosEdicionPerfilDTO();
        dto.setNombreUsuario("usuario");
        dto.setEmail("test@email.com");
        dto.setFechaNacimiento(null);

        DatosIncompletosException exception = assertThrows(DatosIncompletosException.class, () -> {
            dto.validarDatos();
        });
        assertThat(exception.getMessage(), equalTo("La fecha de nacimiento es obligatoria."));
    }

    @Test
    void dadoQueNombreUsuarioEsCortoCuandoValidoDatosDeberiaLanzarExcepcionValidacionInvalida() {
        DatosEdicionPerfilDTO dto = new DatosEdicionPerfilDTO();
        dto.setNombreUsuario("abc");
        dto.setEmail("test@email.com");
        dto.setFechaNacimiento(LocalDate.of(2000, 1, 1));

        ValidacionInvalidaException exception = assertThrows(ValidacionInvalidaException.class, () -> {
            dto.validarDatos();
        });
        assertThat(exception.getMessage(), equalTo("El nombre de usuario debe tener al menos 4 caracteres."));
    }

    @Test
    void dadoQueEmailEsInvalidoCuandoValidoDatosDeberiaLanzarExcepcionValidacionInvalida() {
        DatosEdicionPerfilDTO dto = new DatosEdicionPerfilDTO();
        dto.setNombreUsuario("usuario");
        dto.setEmail("emailinvalido");
        dto.setFechaNacimiento(LocalDate.of(2000, 1, 1));

        ValidacionInvalidaException exception = assertThrows(ValidacionInvalidaException.class, () -> {
            dto.validarDatos();
        });
        assertThat(exception.getMessage(), equalTo("El formato del email es inválido (debe contener @ y dominio)."));
    }

    @Test
    void dadoQueFechaNacimientoEsFuturaCuandoValidoDatosDeberiaLanzarExcepcionValidacionInvalida() {
        DatosEdicionPerfilDTO dto = new DatosEdicionPerfilDTO();
        dto.setNombreUsuario("usuario");
        dto.setEmail("test@email.com");
        dto.setFechaNacimiento(LocalDate.now().plusDays(1));

        ValidacionInvalidaException exception = assertThrows(ValidacionInvalidaException.class, () -> {
            dto.validarDatos();
        });
        assertThat(exception.getMessage(), equalTo("La fecha de nacimiento no puede ser futura."));
    }

    @Test
    void dadoQueEdadEsMenorA7CuandoValidoDatosDeberiaLanzarExcepcionValidacionInvalida() {
        DatosEdicionPerfilDTO dto = new DatosEdicionPerfilDTO();
        dto.setNombreUsuario("usuario");
        dto.setEmail("test@email.com");
        dto.setFechaNacimiento(LocalDate.now().minusYears(5));

        ValidacionInvalidaException exception = assertThrows(ValidacionInvalidaException.class, () -> {
            dto.validarDatos();
        });
        assertThat(exception.getMessage(), equalTo("La edad mínima permitida es 7 años."));
    }
}