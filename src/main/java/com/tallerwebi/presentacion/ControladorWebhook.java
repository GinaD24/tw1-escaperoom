package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.interfaz.servicio.ServicioCompra;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/webhook")
public class ControladorWebhook {

    private ServicioCompra servicioCompra;

    @Autowired
    public ControladorWebhook(ServicioCompra servicioCompra) {
        this.servicioCompra = servicioCompra;
    }

    @PostMapping("/mercado-pago")
    public void manejarWebhook(@RequestBody Map<String, Object> payload) {
        try {
            String action = (String) payload.get("action");
            if ("payment.updated".equals(action)) {
                Map<String, Object> data = (Map<String, Object>) payload.get("data");
                String paymentId = data.get("id").toString();

                // Confirma el pago (actualiza la compra en DB)
                servicioCompra.confirmarPago(paymentId);
            }
        } catch (Exception e) {
            // Log error
            System.err.println("Error procesando webhook: " + e.getMessage());
        }
    }
}