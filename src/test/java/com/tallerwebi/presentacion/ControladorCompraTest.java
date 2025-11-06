package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidad.Sala;
import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.interfaz.servicio.ServicioCompra;
import com.tallerwebi.dominio.interfaz.servicio.ServicioLogin;
import com.tallerwebi.dominio.interfaz.servicio.ServicioSala;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ControladorCompraTest {

    @Mock
    private ServicioCompra servicioCompra;

    @Mock
    private ServicioSala servicioSala;

    @Mock
    private ServicioLogin servicioLogin;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpSession session;

    @Mock
    private RedirectAttributes redirectAttributes;

    @InjectMocks
    private ControladorCompra controladorCompra;

    private final Integer ID_SALA = 5;
    private final Long ID_USUARIO = 1L;
    private Usuario usuarioMock;
    private Sala salaMock;

    @BeforeEach
    public void setUp() {
        usuarioMock = new Usuario();
        usuarioMock.setId(ID_USUARIO);

        salaMock = new Sala();
        salaMock.setId(ID_SALA);

        lenient().when(request.getSession()).thenReturn(session);
        lenient().when(session.getAttribute("id_usuario")).thenReturn(ID_USUARIO);
    }


    @Test
    public void iniciarCompra_DeberiaRedirigirALoginSiNoHayUsuarioEnSesion() {
        // preparacion
        when(session.getAttribute("id_usuario")).thenReturn(null);

        // ejecución
        ModelAndView modelAndView = controladorCompra.iniciarCompra(ID_SALA, request);

        // verificación
        assertThat(modelAndView.getViewName(), is("redirect:/login"));
        verify(servicioCompra, never()).iniciarCompra(any(), any());
    }

    @Test
    public void iniciarCompra_DeberiaRedirigirASalaSiYaEstaDesbloqueada() {
        // preparación
        when(servicioLogin.buscarUsuarioPorId(ID_USUARIO)).thenReturn(usuarioMock);
        when(servicioSala.obtenerSalaPorId(ID_SALA)).thenReturn(salaMock);
        when(servicioCompra.salaDesbloqueadaParaUsuario(usuarioMock, salaMock)).thenReturn(true);

        // ejecución
        ModelAndView modelAndView = controladorCompra.iniciarCompra(ID_SALA, request);

        // verificación
        assertThat(modelAndView.getViewName(), is("redirect:/inicio/sala/" + ID_SALA));
        verify(servicioCompra, never()).iniciarCompra(any(), any());
    }

    @Test
    public void iniciarCompra_DeberiaRedirigirAMercadoPagoSiCompraEsExitosa() {
        // preparación
        String initPointEsperado = "https://checkout.mercadopago.com/redireccion";
        when(servicioLogin.buscarUsuarioPorId(ID_USUARIO)).thenReturn(usuarioMock);
        when(servicioSala.obtenerSalaPorId(ID_SALA)).thenReturn(salaMock);
        when(servicioCompra.salaDesbloqueadaParaUsuario(usuarioMock, salaMock)).thenReturn(false);
        when(servicioCompra.iniciarCompra(usuarioMock, salaMock)).thenReturn(initPointEsperado);

        // ejecución
        ModelAndView modelAndView = controladorCompra.iniciarCompra(ID_SALA, request);

        // verificación
        assertThat(modelAndView.getViewName(), is("redirect:" + initPointEsperado));
        verify(servicioCompra, times(1)).iniciarCompra(usuarioMock, salaMock);
    }


    @Test
    public void confirmarPago_DeberiaMostrarVistaConfirmacionYMarcarComoAprobado() {
        // preparacion
        String paymentId = "12345";
        String externalReference = "99";
        String status = "approved";

        // ejecución
        ModelAndView modelAndView = controladorCompra.confirmarPago(paymentId, status, externalReference, redirectAttributes, request);

        // verificación
        assertThat(modelAndView.getViewName(), is("confirmacion"));
        verify(servicioCompra, times(1)).confirmarCompraPorExternalReference(externalReference, paymentId);
        assertThat(modelAndView.getModelMap().get("mensaje"), is("¡Pago exitoso! La sala ha sido desbloqueada y está lista para ser usada."));
        assertThat(modelAndView.getModelMap().containsKey("error"), is(false));
    }

    @Test
    public void confirmarPago_DeberiaMostrarMensajePendienteSiElPagoEstaPendiente() {
        // preparación
        String status = "pending";

        // ejecución
        ModelAndView modelAndView = controladorCompra.confirmarPago("123", status, "99", redirectAttributes, request);

        // verificación
        assertThat(modelAndView.getViewName(), is("confirmacion"));
        verify(servicioCompra, never()).confirmarCompraPorExternalReference(any(), any());
        assertThat(modelAndView.getModelMap().get("mensaje"), is("Pago pendiente. Te notificaremos cuando se complete."));
    }

    @Test
    public void confirmarPago_DeberiaMostrarErrorSiElPagoFueRechazado() {
        // preparación
        String status = "rejected";

        // ejecución
        ModelAndView modelAndView = controladorCompra.confirmarPago("123", status, "99", redirectAttributes, request);

        // verificación
        assertThat(modelAndView.getViewName(), is("confirmacion"));
        verify(servicioCompra, never()).confirmarCompraPorExternalReference(any(), any());
        assertThat(modelAndView.getModelMap().get("error"), is("El pago no pudo ser aprobado. Estado: " + status));
    }


    @Test
    public void fallo_DeberiaRedirigirAInicioYAgregarFlashAttribute() {
        // ejecución
        ModelAndView modelAndView = controladorCompra.fallo(redirectAttributes);

        // verificación
        assertThat(modelAndView.getViewName(), is("redirect:/inicio/"));
        verify(redirectAttributes, times(1)).addFlashAttribute(eq("mensaje"), anyString());
    }

    @Test
    public void pendiente_DeberiaRedirigirAInicioYAgregarFlashAttribute() {
        // ejecución
        ModelAndView modelAndView = controladorCompra.pendiente(redirectAttributes);

        // verificación
        assertThat(modelAndView.getViewName(), is("redirect:/inicio/"));
        verify(redirectAttributes, times(1)).addFlashAttribute(eq("mensaje"), anyString());
    }
}