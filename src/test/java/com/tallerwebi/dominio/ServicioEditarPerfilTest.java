package com.tallerwebi.dominio;

import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.excepcion.ContraseniaInvalidaException;
import com.tallerwebi.dominio.excepcion.DatosIncompletosException;
import com.tallerwebi.dominio.excepcion.ValidacionInvalidaException;
import com.tallerwebi.dominio.excepcion.UsuarioExistente;
import com.tallerwebi.dominio.interfaz.repositorio.RepositorioUsuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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
        when(usuarioExistente.getFotoPerfil()).thenReturn("/img/vieja.png");

        datosValidos = new DatosEdicionPerfilDTO();
        datosValidos.setId(1L);
        datosValidos.setNombreUsuario("usuarioNuevo");
        datosValidos.setUrlFotoPerfil("/img/nueva.png");

        datosInvalidos = new DatosEdicionPerfilDTO();
        datosInvalidos.setNombreUsuario("");
    }

    @Test
    void dadoQueExisteUnUsuarioCuandoObtengoDatosPerfilDeberiaDevolverLosDatosDelUsuario() {
        when(repositorioUsuario.obtenerUsuarioPorId(1L)).thenReturn(usuarioExistente);

        DatosEdicionPerfilDTO resultado = servicioEditarPerfil.obtenerDatosPerfil(1L);

        assertNotNull(resultado);
        assertThat(resultado.getNombreUsuario(), equalTo("usuarioViejo"));
        assertThat(resultado.getUrlFotoPerfil(), equalTo("/img/uploads//img/vieja.png"));
        verify(repositorioUsuario, times(1)).obtenerUsuarioPorId(1L);
    }

    @Test
    void dadoQueDatosSonValidosYUnicosCuandoActualizoPerfilDeberiaGuardarCambios() throws UsuarioExistente, ContraseniaInvalidaException {
        when(repositorioUsuario.obtenerUsuarioPorId(1L)).thenReturn(usuarioExistente);
        when(repositorioUsuario.buscarPorNombreUsuario("usuarioNuevo")).thenReturn(null);

        servicioEditarPerfil.actualizarPerfil(datosValidos);

        verify(usuarioExistente, times(1)).setNombreUsuario("usuarioNuevo");
        verify(repositorioUsuario, times(1)).modificar(usuarioExistente);
    }

    @Test
    void dadoQueNombreUsuarioYaExisteCuandoActualizoPerfilDeberiaLanzarExcepcionUsuarioExistente() {
        when(repositorioUsuario.obtenerUsuarioPorId(1L)).thenReturn(usuarioExistente);
        when(repositorioUsuario.buscarPorNombreUsuario("usuarioNuevo")).thenReturn(mock(Usuario.class));

        UsuarioExistente exception = assertThrows(UsuarioExistente.class, () -> {
            servicioEditarPerfil.actualizarPerfil(datosValidos);
        });
        assertThat(exception.getMessage(), equalTo("El nombre de usuario ya está en uso."));
    }

    @Test
    void dadoQueUsuarioNoExisteCuandoActualizoPerfilDeberiaActualizarDatosYGuardarCambios() throws ContraseniaInvalidaException {
        when(repositorioUsuario.obtenerUsuarioPorId(1L)).thenReturn(usuarioExistente);
        when(repositorioUsuario.buscarPorNombreUsuario(anyString())).thenReturn(null);

        DatosEdicionPerfilDTO datosValidos = new DatosEdicionPerfilDTO();
        datosValidos.setId(1L);
        datosValidos.setNombreUsuario("usuarioNuevo");
        datosValidos.setUrlFotoPerfil("/img/default.png");

        servicioEditarPerfil.actualizarPerfil(datosValidos);

        verify(repositorioUsuario, times(1)).modificar(any(Usuario.class));
        verify(repositorioUsuario, never()).guardar(any(Usuario.class));
    }

    @Test
    void dadoQueDatosSonCompletosYValidosCuandoValidoDatosNoDeberiaLanzarExcepcion() throws DatosIncompletosException, ValidacionInvalidaException, ContraseniaInvalidaException {
        datosValidos.validarDatos();
    }

    @Test
    void dadoQueNombreUsuarioEstaVacioCuandoValidoDatosDeberiaLanzarExcepcionDatosIncompletos() {
        DatosEdicionPerfilDTO dto = new DatosEdicionPerfilDTO();
        dto.setNombreUsuario("");

        DatosIncompletosException exception = assertThrows(DatosIncompletosException.class, () -> {
            dto.validarDatos();
        });
        assertThat(exception.getMessage(), equalTo("El nombre de usuario es obligatorio."));
    }

    @Test
    void dadoQueNombreUsuarioEsCortoCuandoValidoDatosDeberiaLanzarExcepcionValidacionInvalida() {
        DatosEdicionPerfilDTO dto = new DatosEdicionPerfilDTO();
        dto.setNombreUsuario("abc");

        ValidacionInvalidaException exception = assertThrows(ValidacionInvalidaException.class, () -> {
            dto.validarDatos();
        });
        assertThat(exception.getMessage(), equalTo("El nombre de usuario debe tener al menos 4 caracteres."));
    }

    @Test
    void dadoQueContraseniaNuevaEsDiferenteALaActualCuandoValidoDatosNoDeberiaLanzarExcepcion() throws DatosIncompletosException, ValidacionInvalidaException, ContraseniaInvalidaException {
        DatosEdicionPerfilDTO dto = new DatosEdicionPerfilDTO();
        dto.setNombreUsuario("usuario");
        dto.setContrasenaActual("viejaPass");
        dto.setContrasenaNueva("nuevaPass");
        dto.validarDatos();
    }
    @Test
    void dadoQueContraseniaActualEsIncorrectaCuandoActualizoPerfilDeberiaLanzarExcepcionContraseniaInvalida() {
        reset(usuarioExistente);

        when(repositorioUsuario.obtenerUsuarioPorId(1L)).thenReturn(usuarioExistente);
        when(repositorioUsuario.buscarPorNombreUsuario(anyString())).thenReturn(null);
        when(usuarioExistente.getPassword()).thenReturn("otraPass");
        when(usuarioExistente.getId()).thenReturn(1L);
        when(usuarioExistente.getNombreUsuario()).thenReturn("usuarioViejo");
        when(usuarioExistente.getFotoPerfil()).thenReturn("/img/vieja.png");

        DatosEdicionPerfilDTO datosTest = new DatosEdicionPerfilDTO();
        datosTest.setId(1L);
        datosTest.setNombreUsuario("usuarioNuevo");
        datosTest.setUrlFotoPerfil("/img/nueva.png");
        datosTest.setContrasenaActual("viejaPass");
        datosTest.setContrasenaNueva("nuevaPass");

        ContraseniaInvalidaException exception = assertThrows(ContraseniaInvalidaException.class, () -> {
            servicioEditarPerfil.actualizarPerfil(datosTest);
        });
        assertThat(exception.getMessage(), equalTo("La contraseña actual es incorrecta."));

        verify(repositorioUsuario, never()).modificar(any(Usuario.class));
    }


}
