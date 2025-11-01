package com.tallerwebi.presentacion;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class ControladorLogoutTest {

    private ControladorLogout controladorLogout;
    private HttpServletRequest mockRequest;
    private HttpSession mockSession;

    @BeforeEach
    public void init() {
        controladorLogout = new ControladorLogout();
        mockRequest = Mockito.mock(HttpServletRequest.class);
        mockSession = Mockito.mock(HttpSession.class);
    }

    @Test
    public void alHacerLogoutDebeCerrarLaSesionDelUsuarioYRedirigirALogin() {

        Mockito.when(mockRequest.getSession(false)).thenReturn(mockSession);

        String laVista = controladorLogout.elLogout(mockRequest);

        Mockito.verify(mockSession, Mockito.times(1)).invalidate();

        assertThat(laVista, equalTo("redirect:/login"));
    }
}