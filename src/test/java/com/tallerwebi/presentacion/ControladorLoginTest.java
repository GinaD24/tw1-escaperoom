package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.ServicioLogin;
import com.tallerwebi.dominio.Usuario;
import com.tallerwebi.dominio.excepcion.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.time.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;
import static org.mockito.Mockito.*;

public class ControladorLoginTest {

	private ControladorLogin controladorLogin;
	private Usuario usuarioMock;
	private DatosLogin datosLoginMock;
	private HttpServletRequest requestMock;
	private HttpSession sessionMock;
	private ServicioLogin servicioLoginMock;


	@BeforeEach
    public void init(){
        datosLoginMock = new DatosLogin("dami@unlam.com", "123");
        usuarioMock = mock(Usuario.class);
        when(usuarioMock.getEmail()).thenReturn("dami@unlam.com");
        when(usuarioMock.getPassword()).thenReturn("password123");
        when(usuarioMock.getNombre()).thenReturn("Dami");
        when(usuarioMock.getApellido()).thenReturn("Test");
        when(usuarioMock.getNombreUsuario()).thenReturn("damitest");
        when(usuarioMock.getFechaNacimiento()).thenReturn(java.time.LocalDate.of(1990, 1, 1));
        when(usuarioMock.getRol()).thenReturn("USUARIO");
        requestMock = mock(HttpServletRequest.class);
        sessionMock = mock(HttpSession.class);
        when(requestMock.getSession()).thenReturn(sessionMock);
        servicioLoginMock = mock(ServicioLogin.class);
        controladorLogin = new ControladorLogin(servicioLoginMock);
    }


    @Test
	public void loginConUsuarioYPasswordInorrectosDeberiaLlevarALoginNuevamente() throws CredencialesInvalidasException {
		// preparacion
		when(servicioLoginMock.consultarUsuario(anyString(), anyString())).thenReturn(null);

		// ejecucion
		ModelAndView modelAndView = controladorLogin.validarLogin(datosLoginMock, requestMock);

		// validacion
		assertThat(modelAndView.getViewName(), equalToIgnoringCase("login"));
		assertThat(modelAndView.getModel().get("error").toString(), equalToIgnoringCase("Usuario o clave incorrecta"));
		verify(sessionMock, times(0)).setAttribute("ROL", "ADMIN");
	}

	@Test
	public void loginConUsuarioYPasswordCorrectosDeberiaLLevarAHome() throws CredencialesInvalidasException {
		// preparacion
		Usuario usuarioEncontradoMock = mock(Usuario.class);
		when(usuarioEncontradoMock.getRol()).thenReturn("ADMIN");

		when(requestMock.getSession()).thenReturn(sessionMock);
		when(servicioLoginMock.consultarUsuario(anyString(), anyString())).thenReturn(usuarioEncontradoMock);

		// ejecucion
		ModelAndView modelAndView = controladorLogin.validarLogin(datosLoginMock, requestMock);

		// validacion
		assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/home"));
		verify(sessionMock, times(1)).setAttribute("ROL", usuarioEncontradoMock.getRol());
	}

    @Test
    public void registrameSiUsuarioNoExisteDeberiaCrearUsuarioYVolverAlLogin() throws UsuarioExistente, EdadInvalidaException, DatosIncompletosException, ValidacionInvalidaException, CredencialesInvalidasException  {
        when(requestMock.getParameter("confirmPassword")).thenReturn("password123");  // Coincide con usuarioMock.getPassword()
        ModelAndView modelAndView = controladorLogin.registrarme(usuarioMock, requestMock);
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/login"));
        verify(servicioLoginMock, times(1)).registrar(usuarioMock);  // Ahora compila con throws completo
    }
    @Test
    public void registrarmeSiUsuarioExisteDeberiaVolverAFormularioYMostrarError() throws UsuarioExistente, EdadInvalidaException, DatosIncompletosException, ValidacionInvalidaException, CredencialesInvalidasException  {
        // Preparación
        when(requestMock.getParameter("confirmPassword")).thenReturn("password123");

        doThrow(new UsuarioExistente("El usuario ya existe")).when(servicioLoginMock).registrar(usuarioMock);
        // Ejecución
        ModelAndView modelAndView = controladorLogin.registrarme(usuarioMock, requestMock);
        // Validación
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("nuevo-usuario"));
        assertThat(modelAndView.getModel().get("error").toString(), equalToIgnoringCase("El usuario ya existe"));
        verify(servicioLoginMock, times(1)).registrar(usuarioMock);
    }
    @Test
    public void errorEnRegistrarmeDeberiaVolverAFormularioYMostrarError() throws UsuarioExistente, EdadInvalidaException, DatosIncompletosException, ValidacionInvalidaException, CredencialesInvalidasException  {
        // Preparación
        when(requestMock.getParameter("confirmPassword")).thenReturn("password123");

        doThrow(new RuntimeException("Error simulado")).when(servicioLoginMock).registrar(usuarioMock);

        // Ejecución
        ModelAndView modelAndView = controladorLogin.registrarme(usuarioMock, requestMock);

        // Validación
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("nuevo-usuario"));
        assertThat(modelAndView.getModel().get("error").toString(), equalToIgnoringCase("Error inesperado al registrar el usuario"));
        verify(servicioLoginMock, times(1)).registrar(usuarioMock);
    }

}