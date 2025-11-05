package com.tallerwebi.dominio;

import com.tallerwebi.dominio.entidad.Compra;
import com.tallerwebi.dominio.entidad.Sala;
import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.enums.Dificultad;
import com.tallerwebi.dominio.interfaz.repositorio.RepositorioCompra;
import com.tallerwebi.dominio.interfaz.servicio.ServicioCompra; // Importa la interfaz
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ServicioCompraImplTest {

    @Mock
    private RepositorioCompra repositorioCompra;

    private ServicioCompra servicioCompra;

    private Usuario usuario;
    private Sala sala;

    @BeforeEach
    public void setUp() {
        servicioCompra = new ServicioCompraImpl(repositorioCompra);
        usuario = new Usuario();
        usuario.setId(1L);
        sala = new Sala(1, "Sala Test", Dificultad.PRINCIPIANTE, "Escenario", "Historia", true, 10, "imagen.png");
    }

    @Test
    public void iniciarCompra_DeberiaGuardarCompraYCrearPreferencia() {
        doAnswer(invocation -> {
            Compra compra = invocation.getArgument(0);
            if (compra.getId() == null) {
                compra.setId(123);
            }
            return null;
        }).when(repositorioCompra).guardar(any(Compra.class));

        // ejecución
        String initPoint = servicioCompra.iniciarCompra(usuario, sala);

        // verificación

        verify(repositorioCompra, times(1)).guardar(any(Compra.class));
        verify(repositorioCompra, times(1)).guardarCompra(any(Compra.class));
    }

    @Test
    public void confirmarPago_DeberiaConfirmarPagoAprobadoYActualizarCompra() {
        // preparación
        String paymentId = "12345";
        String externalReference = "1";
        Compra mockCompra = new Compra(usuario, sala, LocalDateTime.now(), false);
        mockCompra.setExternalReference(externalReference);

        when(repositorioCompra.obtenerCompraPorExternalReference(externalReference)).thenReturn(mockCompra);

        // Ejecución
        servicioCompra.confirmarCompraPorExternalReference(externalReference, paymentId);

        // verificación
        verify(repositorioCompra).obtenerCompraPorExternalReference(externalReference);
        verify(repositorioCompra).guardarCompra(mockCompra);
        assertThat(mockCompra.getPagada(), is(true));
        assertThat(mockCompra.getPaymentId(), is(paymentId));
    }

    @Test
    public void confirmarCompraPorExternalReference_DeberiaMarcarComoPagada() {
        // preparación
        String externalReference = "1";
        String paymentId = "12345";
        Compra mockCompra = new Compra();
        when(repositorioCompra.obtenerCompraPorExternalReference(externalReference)).thenReturn(mockCompra);

        // ejecución
        servicioCompra.confirmarCompraPorExternalReference(externalReference, paymentId);

        // verificación
        verify(repositorioCompra).obtenerCompraPorExternalReference(externalReference);
        verify(repositorioCompra).guardarCompra(mockCompra);
        assertThat(mockCompra.getPagada(), is(true));
        assertThat(mockCompra.getPaymentId(), is(paymentId));
    }

    @Test
    public void obtenerComprasPorUsuario_DeberiaDevolverListaDeCompras() {
        // preparación
        List<Compra> comprasMock = new ArrayList<>();
        when(repositorioCompra.obtenerComprasPorUsuario(usuario)).thenReturn(comprasMock);

        // ejecución
        List<Compra> compras = servicioCompra.obtenerComprasPorUsuario(usuario);

        // verificación
        assertThat(compras, is(comprasMock));
        verify(repositorioCompra).obtenerComprasPorUsuario(usuario);
    }

    @Test
    public void salaDesbloqueadaParaUsuario_DeberiaDevolverTrueSiPagada() {
        // preparación
        when(repositorioCompra.salaDesbloqueadaParaUsuario(usuario, sala)).thenReturn(true);

        // ejecución
        Boolean desbloqueada = servicioCompra.salaDesbloqueadaParaUsuario(usuario, sala);

        // verificación
        assertThat(desbloqueada, is(true));
        verify(repositorioCompra).salaDesbloqueadaParaUsuario(usuario, sala);
    }
}