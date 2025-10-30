package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidad.Sala;
import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.interfaz.repositorio.RepositorioUsuario;
import com.tallerwebi.dominio.interfaz.servicio.ServicioCompra;
import com.tallerwebi.dominio.interfaz.servicio.ServicioSala;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.mockito.Mockito.*;

public class ControladorCompraTest {

    @Mock
    private ServicioCompra servicioCompra;
    @Mock
    private ServicioSala servicioSala;
    @Mock
    private RepositorioUsuario repositorioUsuario;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpSession session;
    @Mock
    private RedirectAttributes redirectAttributes;

    @InjectMocks
    private ControladorCompra controladorCompra;

    private final Integer ID_SALA = 1;
    private final Long ID_USUARIO = 5L;
    private Usuario mockUsuario;
    private Sala mockSala;
    private final String INIT_POINT = "https://sandbox.mercadopago.com/checkout/v1/redirect_url";

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);

        when(request.getSession()).thenReturn(session);

        mockUsuario = new Usuario();
        mockUsuario.setId(ID_USUARIO);

        mockSala = new Sala();
        mockSala.setId(ID_SALA);
        mockSala.setNombre("Sala Test");
    }

    @Test
    public void alIntentarComprarSinUsuarioLogueadoDeberiaRedirigirALogin() {
        when(session.getAttribute("id_usuario")).thenReturn(null);

        ModelAndView modelAndView = controladorCompra.iniciarCompra(ID_SALA, request);

        assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/login"));
        verify(session, times(1)).getAttribute("id_usuario");
        verifyNoInteractions(repositorioUsuario); // No debe llamar a servicios
    }

    @Test
    public void alIniciarCompraDeSalaBloqueadaDeberiaCrearCompraYRedirigirAMercadoPago() {
        when(session.getAttribute("id_usuario")).thenReturn(ID_USUARIO);
        when(repositorioUsuario.obtenerUsuarioPorId(ID_USUARIO)).thenReturn(mockUsuario);
        when(servicioSala.obtenerSalaPorId(ID_SALA)).thenReturn(mockSala);
        when(servicioCompra.salaDesbloqueadaParaUsuario(mockUsuario, mockSala)).thenReturn(false);
        when(servicioCompra.crearPreferenciaParaSala(mockSala)).thenReturn(INIT_POINT);

        ModelAndView modelAndView = controladorCompra.iniciarCompra(ID_SALA, request);

        verify(servicioCompra, times(1)).iniciarCompra(mockUsuario, mockSala);
        verify(servicioCompra, times(1)).crearPreferenciaParaSala(mockSala);
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:" + INIT_POINT));
    }


    @Test
    public void alRecibirExitoDePagoDeberiaRedirigirAInicioConMensajeDeExito() {
        ModelAndView modelAndView = controladorCompra.exito(redirectAttributes);

        assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/inicio/"));
        verify(redirectAttributes, times(1)).addFlashAttribute("mensaje", "¡Pago exitoso! La sala ha sido desbloqueada.");
        verify(servicioCompra, never()).confirmarPago(any());
    }

    @Test
    public void alRecibirFalloDePagoDeberiaRedirigirAInicioConMensajeDeFallo() {
        ModelAndView modelAndView = controladorCompra.fallo(redirectAttributes);

        assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/inicio/"));
        verify(redirectAttributes, times(1)).addFlashAttribute("mensaje", "El pago falló. Inténtalo de nuevo.");
    }

    @Test
    public void alRecibirPendienteDePagoDeberiaRedirigirAInicioConMensajeDePendiente() {
        ModelAndView modelAndView = controladorCompra.pendiente(redirectAttributes);

        assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/inicio/"));
        verify(redirectAttributes, times(1)).addFlashAttribute("mensaje", "El pago está pendiente de aprobación.");
    }
}