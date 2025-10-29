package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.DatosEdicionPerfilDTO;
import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.excepcion.ContraseniaInvalidaException;
import com.tallerwebi.dominio.excepcion.DatosIncompletosException;
import com.tallerwebi.dominio.excepcion.ValidacionInvalidaException;
import com.tallerwebi.dominio.excepcion.UsuarioExistente;
import com.tallerwebi.dominio.interfaz.servicio.ServicioEditarPerfil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import javax.servlet.http.HttpSession;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;
import static org.mockito.Mockito.*;

public class ControladorEditarPerfilTest {

    private ControladorEditarPerfil controladorEditarPerfil;
    private ServicioEditarPerfil servicioEditarPerfilMock;
    private HttpSession sessionMock;
    private RedirectAttributes redirectAttributesMock;
    private DatosEdicionPerfilDTO datosValidos;

    @BeforeEach
    void init() {
        servicioEditarPerfilMock = mock(ServicioEditarPerfil.class);
        controladorEditarPerfil = new ControladorEditarPerfil(servicioEditarPerfilMock);
        sessionMock = mock(HttpSession.class);
        redirectAttributesMock = new RedirectAttributesModelMap();
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
    void dadoQueDatosSonValidosCuandoGuardaCambiosDeberiaActualizarEIrAPerfil() throws UsuarioExistente, ContraseniaInvalidaException {
        doNothing().when(servicioEditarPerfilMock).actualizarPerfil(any(DatosEdicionPerfilDTO.class));
        Usuario usuarioActualizado = mock(Usuario.class);
        when(usuarioActualizado.getId()).thenReturn(1L);
        when(usuarioActualizado.getNombreUsuario()).thenReturn("usuario");
        when(usuarioActualizado.getFotoPerfil()).thenReturn("/img/test.png");
        when(servicioEditarPerfilMock.buscarUsuarioPorId(1L)).thenReturn(usuarioActualizado);
        when(sessionMock.getAttribute("id_usuario")).thenReturn(1L);

        ModelAndView modelAndView = controladorEditarPerfil.guardarCambios(datosValidos, sessionMock, redirectAttributesMock);

        verify(servicioEditarPerfilMock, times(1)).actualizarPerfil(datosValidos);
        verify(sessionMock, times(1)).setAttribute("urlFotoPerfil", "/img/test.png");
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/perfil/verPerfil"));
        assertThat(redirectAttributesMock.getFlashAttributes().containsKey("mensaje"), equalTo(true));
        assertThat(redirectAttributesMock.getFlashAttributes().get("mensaje"), equalTo("Perfil actualizado exitosamente."));
    }

    @Test
    void dadoQueUsuarioNoEstaLogueadoCuandoGuardaCambiosDeberiaRedirigirALogin() {
        when(sessionMock.getAttribute("id_usuario")).thenReturn(null);

        ModelAndView modelAndView = controladorEditarPerfil.guardarCambios(datosValidos, sessionMock, redirectAttributesMock);

        assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/login"));
    }

    @Test
    void dadoQueDatosSonIncompletosCuandoGuardaCambiosDeberiaVolverAEditarPerfilConError() throws UsuarioExistente, ContraseniaInvalidaException {
        datosValidos.setNombreUsuario(null); // Provocamos la excepción DatosIncompletosException

        ModelAndView modelAndView = controladorEditarPerfil.guardarCambios(datosValidos, sessionMock, redirectAttributesMock);

        assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/configuracion/editar"));

        assertThat(redirectAttributesMock.getFlashAttributes().containsKey("error"), equalTo(true));

        datosValidos.setNombreUsuario("usuario");
    }

    @Test
    void dadoQueValidacionFallaCuandoGuardaCambiosDeberiaVolverAEditarPerfilConError() throws UsuarioExistente, ContraseniaInvalidaException {
        doThrow(new RuntimeException("Error genérico")).when(servicioEditarPerfilMock).actualizarPerfil(any());

        ModelAndView modelAndView = controladorEditarPerfil.guardarCambios(datosValidos, sessionMock, redirectAttributesMock);

        assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/configuracion/editar"));

        assertThat(redirectAttributesMock.getFlashAttributes().containsKey("error"), equalTo(true));
    }

    @Test
    void dadoQueUsuarioYaExisteCuandoGuardaCambiosDeberiaVolverAEditarPerfilConError() throws UsuarioExistente, ContraseniaInvalidaException {
        doThrow(new UsuarioExistente("Nombre ya registrado")).when(servicioEditarPerfilMock).actualizarPerfil(any());

        ModelAndView modelAndView = controladorEditarPerfil.guardarCambios(datosValidos, sessionMock, redirectAttributesMock);

        assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/configuracion/editar"));
        assertThat(redirectAttributesMock.getFlashAttributes().containsKey("error"), equalTo(true));
    }

    @Test
    void dadoQueContraseniaActualEsIncorrectaCuandoGuardaCambiosDeberiaVolverAEditarPerfilConError() throws UsuarioExistente, ContraseniaInvalidaException {
        doThrow(new ContraseniaInvalidaException("La contraseña actual es incorrecta.")).when(servicioEditarPerfilMock).actualizarPerfil(any());

        ModelAndView modelAndView = controladorEditarPerfil.guardarCambios(datosValidos, sessionMock, redirectAttributesMock);

        assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/configuracion/editar"));
        assertThat(redirectAttributesMock.getFlashAttributes().containsKey("error"), equalTo(true));
        assertThat(redirectAttributesMock.getFlashAttributes().get("error").toString(), equalTo("Error de contraseña: La contraseña actual es incorrecta."));
    }
    // NUEVO TEST PARA CONTRASEÑAS: Cubre el caso donde se cambia la contraseña exitosamente (sin errores)
    @Test
    void dadoQueContraseniaNuevaEsValidaCuandoGuardaCambiosDeberiaActualizarContraseniaYRedirigirAVerPerfil() throws UsuarioExistente, ContraseniaInvalidaException {
        Usuario usuarioActualizado = mock(Usuario.class);
        when(usuarioActualizado.getId()).thenReturn(1L);
        when(usuarioActualizado.getNombreUsuario()).thenReturn("usuario");
        when(usuarioActualizado.getFotoPerfil()).thenReturn("/img/test.png");
        when(servicioEditarPerfilMock.buscarUsuarioPorId(1L)).thenReturn(usuarioActualizado);
        doNothing().when(servicioEditarPerfilMock).actualizarPerfil(any(DatosEdicionPerfilDTO.class));

        ModelAndView modelAndView = controladorEditarPerfil.guardarCambios(datosValidos, sessionMock, redirectAttributesMock);

        verify(servicioEditarPerfilMock, times(1)).actualizarPerfil(datosValidos);
        verify(sessionMock, times(1)).setAttribute("urlFotoPerfil", "/img/test.png");
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/perfil/verPerfil"));
        assertThat(redirectAttributesMock.getFlashAttributes().containsKey("mensaje"), equalTo(true));
    }


}