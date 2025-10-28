package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.DatosEdicionPerfilDTO;
import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.excepcion.DatosIncompletosException;
import com.tallerwebi.dominio.excepcion.ValidacionInvalidaException;
import com.tallerwebi.dominio.excepcion.UsuarioExistente;
import com.tallerwebi.dominio.interfaz.servicio.ServicioEditarPerfil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;
import static org.mockito.Mockito.*;

public class ControladorEditarPerfilTest {

    private ControladorEditarPerfil controladorEditarPerfil;
    private ServicioEditarPerfil servicioEditarPerfilMock;
    private HttpSession sessionMock;
    private DatosEdicionPerfilDTO datosValidos;

    @BeforeEach
    void init() {
        servicioEditarPerfilMock = mock(ServicioEditarPerfil.class);
        controladorEditarPerfil = new ControladorEditarPerfil(servicioEditarPerfilMock);
        sessionMock = mock(HttpSession.class);
        when(sessionMock.getAttribute("id_usuario")).thenReturn(1L);

        datosValidos = new DatosEdicionPerfilDTO();
        datosValidos.setId(1L);
        datosValidos.setNombreUsuario("usuario");
        datosValidos.setUrlFotoPerfil("/img/test.png");
    }

    @Test
    void dadoQueUsuarioEstaLogueadoCuandoAccedeAEditarPerfilDeberiaMostrarVistaConDatos() {
        when(servicioEditarPerfilMock.obtenerDatosPerfil(1L)).thenReturn(datosValidos);

        ModelAndView modelAndView = controladorEditarPerfil.vistaEditarPerfil(sessionMock);

        assertThat(modelAndView.getViewName(), equalToIgnoringCase("editar-perfil"));
        assertThat(modelAndView.getModel().get("datosPerfil"), equalTo(datosValidos));
    }

    @Test
    void dadoQueUsuarioNoEstaLogueadoCuandoAccedeAEditarPerfilDeberiaRedirigirALogin() {
        when(sessionMock.getAttribute("id_usuario")).thenReturn(null);

        ModelAndView modelAndView = controladorEditarPerfil.vistaEditarPerfil(sessionMock);

        assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/login"));
    }

    @Test
    void dadoQueDatosSonValidosCuandoGuardaCambiosDeberiaActualizarYRedirigirAVerPerfil() throws UsuarioExistente {
        doNothing().when(servicioEditarPerfilMock).actualizarPerfil(any(DatosEdicionPerfilDTO.class));
        when(sessionMock.getAttribute("id_usuario")).thenReturn(1L);

        ModelAndView modelAndView = controladorEditarPerfil.guardarCambios(datosValidos, sessionMock);

        verify(servicioEditarPerfilMock, times(1)).actualizarPerfil(datosValidos);
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/perfil/verPerfil"));
    }

    @Test
    void dadoQueUsuarioNoEstaLogueadoCuandoGuardaCambiosDeberiaRedirigirALogin() {
        when(sessionMock.getAttribute("id_usuario")).thenReturn(null);

        ModelAndView modelAndView = controladorEditarPerfil.guardarCambios(datosValidos, sessionMock);

        assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/login"));
    }

    @Test
    void dadoQueDatosSonIncompletosCuandoGuardaCambiosDeberiaVolverAEditarPerfilConError() throws UsuarioExistente {
        doThrow(new DatosIncompletosException("Nombre requerido")).when(servicioEditarPerfilMock).actualizarPerfil(any());

        ModelAndView modelAndView = controladorEditarPerfil.guardarCambios(datosValidos, sessionMock);

        assertThat(modelAndView.getViewName(), equalToIgnoringCase("editar-perfil"));
        assertThat(modelAndView.getModel().get("error").toString(), equalToIgnoringCase("Error interno al actualizar: Nombre requerido"));
    }

    @Test
    void dadoQueValidacionFallaCuandoGuardaCambiosDeberiaVolverAEditarPerfilConError() throws UsuarioExistente {
        doThrow(new ValidacionInvalidaException("Nombre inválido")).when(servicioEditarPerfilMock).actualizarPerfil(any());

        ModelAndView modelAndView = controladorEditarPerfil.guardarCambios(datosValidos, sessionMock);

        assertThat(modelAndView.getViewName(), equalToIgnoringCase("editar-perfil"));
        assertThat(modelAndView.getModel().get("error").toString(), equalToIgnoringCase("Error interno al actualizar: Nombre inválido"));
    }

    @Test
    void dadoQueUsuarioYaExisteCuandoGuardaCambiosDeberiaVolverAEditarPerfilConError() throws UsuarioExistente {
        doThrow(new UsuarioExistente("Nombre ya registrado")).when(servicioEditarPerfilMock).actualizarPerfil(any());

        ModelAndView modelAndView = controladorEditarPerfil.guardarCambios(datosValidos, sessionMock);

        assertThat(modelAndView.getViewName(), equalToIgnoringCase("editar-perfil"));
        assertThat(modelAndView.getModel().get("error").toString(), equalToIgnoringCase("Error de actualización: Nombre ya registrado"));
    }
}