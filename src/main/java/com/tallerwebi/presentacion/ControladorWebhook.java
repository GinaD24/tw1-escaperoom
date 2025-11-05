package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.interfaz.servicio.ServicioCompra;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/webhook")
public class ControladorWebhook {

    private final ServicioCompra servicioCompra;

    @Autowired
    public ControladorWebhook(ServicioCompra servicioCompra) {
        this.servicioCompra = servicioCompra;
    }

    @PostMapping("/mercado-pago")
    public void manejarWebhook(@RequestBody Map<String, Object> payload) {
        try {
            // Posibles formas: { "type":"payment", "data": {"id": "..."} }
            // o { "action":"payment.updated", "data": {"id": "..."} }
            Object dataObj = payload.get("data");
            if (dataObj instanceof Map) {
                Map<String, Object> data = (Map<String, Object>) dataObj;
                Object id = data.get("id");
                if (id == null) id = data.get("id"); // redundante pero seguro
                if (id != null) {
                    String paymentId = id.toString();
                    servicioCompra.confirmarPago(paymentId);
                    return;
                }
            }

            // Fallback: a veces MP envía { "action":"payment.updated", "data":{"id":123} } - cubierto arriba
            // Si no encontramos id, logueamos el payload para debug
            System.err.println("Webhook MercadoPago sin id de pago: " + payload);

        } catch (Exception e) {
            System.err.println("Error procesando webhook: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
