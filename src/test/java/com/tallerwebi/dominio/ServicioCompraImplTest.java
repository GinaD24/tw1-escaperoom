package com.tallerwebi.dominio;

import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.preference.Preference;
import com.tallerwebi.dominio.entidad.Compra;
import com.tallerwebi.dominio.entidad.Sala;
import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.interfaz.repositorio.RepositorioCompra;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ServicioCompraImplTest {

    @Mock
    private RepositorioCompra repositorioCompra;

    @InjectMocks
    private ServicioCompraImpl servicioCompra;

    private Usuario mockUsuario;
    private Sala mockSala;
    private Compra mockCompra;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);

        mockUsuario = new Usuario();
        mockUsuario.setId(1L);
        mockSala = new Sala();
        mockSala.setId(10);
        mockSala.setNombre("Sala Test");

        mockCompra = new Compra(mockUsuario, mockSala, LocalDateTime.now(), false);
    }

    @Test
    public void dadoQueUnUsuarioIniciaUnaCompraDeberiaGuardarseEnElRepositorio() {
        servicioCompra.iniciarCompra(mockUsuario, mockSala);

        verify(repositorioCompra, times(1)).guardar(any(Compra.class));
    }

    @Test
    public void dadoUnPaymentIdExistenteAlConfirmarPagoDeberiaMarcarLaCompraComoPagada() {
        String paymentId = "mp_12345";
        Compra compraPendiente = new Compra(mockUsuario, mockSala, LocalDateTime.now(), false);

        when(repositorioCompra.obtenerCompraPorPaymentId(paymentId)).thenReturn(compraPendiente);

        servicioCompra.confirmarPago(paymentId);

        verify(repositorioCompra, times(1)).obtenerCompraPorPaymentId(paymentId);
        verify(repositorioCompra, times(1)).guardarCompra(argThat(compra -> compra.getPagada() == true));
    }

    @Test
    public void dadoUnPaymentIdInexistenteAlConfirmarPagoNoDeberiaHacerNada() {
        String paymentId = "mp_99999";
        when(repositorioCompra.obtenerCompraPorPaymentId(paymentId)).thenReturn(null);

        servicioCompra.confirmarPago(paymentId);

        verify(repositorioCompra, times(1)).obtenerCompraPorPaymentId(paymentId);
        verify(repositorioCompra, never()).guardarCompra(any(Compra.class));
    }

    @Test
    public void alObtenerComprasPorUsuarioDeberiaDevolverLaListaDelRepositorio() {
        List<Compra> comprasEsperadas = Collections.singletonList(mockCompra);
        when(repositorioCompra.obtenerComprasPorUsuario(mockUsuario)).thenReturn(comprasEsperadas);

        List<Compra> comprasObtenidas = servicioCompra.obtenerComprasPorUsuario(mockUsuario);

        assertThat(comprasObtenidas, is(comprasEsperadas));
        assertThat(comprasObtenidas, hasSize(1));
        verify(repositorioCompra, times(1)).obtenerComprasPorUsuario(mockUsuario);
    }

    @Test
    public void siLaSalaEstaDesbloqueadaParaElUsuarioDeberiaDevolverTrue() {
        when(repositorioCompra.salaDesbloqueadaParaUsuario(mockUsuario, mockSala)).thenReturn(true);

        boolean desbloqueada = servicioCompra.salaDesbloqueadaParaUsuario(mockUsuario, mockSala);

        assertThat(desbloqueada, is(true));
        verify(repositorioCompra, times(1)).salaDesbloqueadaParaUsuario(mockUsuario, mockSala);
    }

}