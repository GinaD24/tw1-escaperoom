package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.interfaz.servicio.ServicioCompra;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

public class ControladorWebhookTest {

    @Mock
    private ServicioCompra servicioCompra;

    @InjectMocks
    private ControladorWebhook controladorWebhook;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void alRecibirUnWebhookDePagoActualizadoDeberiaLlamarAConfirmarPago() {
        String paymentId = "123456789";
        Map<String, Object> dataPayload = new HashMap<>();
        dataPayload.put("id", paymentId);

        Map<String, Object> webhookPayload = new HashMap<>();
        webhookPayload.put("action", "payment.updated");
        webhookPayload.put("data", dataPayload);

        controladorWebhook.manejarWebhook(webhookPayload);

        verify(servicioCompra, times(1)).confirmarPago(paymentId);
    }

    @Test
    public void alRecibirUnWebhookDeOtraAccionNoDeberiaLlamarAConfirmarPago() {
        Map<String, Object> webhookPayload = new HashMap<>();
        webhookPayload.put("action", "merchant_order.updated");
        webhookPayload.put("data", new HashMap<>());

        controladorWebhook.manejarWebhook(webhookPayload);

        verify(servicioCompra, never()).confirmarPago(anyString());
    }

    @Test
    public void alRecibirUnWebhookConEstructuraInvalidaNoDeberiaLanzarExcepcion() {
        Map<String, Object> webhookPayload = new HashMap<>();
        webhookPayload.put("action", "payment.updated");

        try {
            controladorWebhook.manejarWebhook(webhookPayload);
        } catch (Exception e) {
            assert(false);
        }

        verify(servicioCompra, never()).confirmarPago(anyString());
    }
}