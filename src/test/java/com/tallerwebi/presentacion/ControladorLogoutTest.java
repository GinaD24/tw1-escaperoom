package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.interfaz.servicio.ServicioLogin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ControladorLogoutTest {

    private ControladorLogout controladorLogout;
    private HttpServletRequest mockRequest;
    private HttpSession mockSession;
    private ServicioLogin servicioLogin;

    @BeforeEach
    public void init() {
        servicioLogin = Mockito.mock(ServicioLogin.class);
        controladorLogout = new ControladorLogout(servicioLogin);
        mockRequest = Mockito.mock(HttpServletRequest.class);
        mockSession = Mockito.mock(HttpSession.class);
    }

    @Test
    public void alHacerLogoutDebeCerrarLaSesionDelUsuarioYRedirigirALogin() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);

        when(mockRequest.getSession(false)).thenReturn(mockSession);
        when(mockSession.getAttribute("id_usuario")).thenReturn(usuario.getId());
        when(servicioLogin.buscarUsuarioPorId(usuario.getId())).thenReturn(usuario);

        String laVista = controladorLogout.elLogout(mockRequest);

        verify(mockSession).invalidate();
        assertThat(laVista, equalTo("redirect:/login"));
    }
}