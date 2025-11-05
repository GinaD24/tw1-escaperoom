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
            Object dataObj = payload.get("data");
            if (dataObj instanceof Map) {
                Map<String, Object> data = (Map<String, Object>) dataObj;
                Object id = data.get("id");
                if (id == null) id = data.get("id");
                if (id != null) {
                    String paymentId = id.toString();
                    servicioCompra.confirmarPago(paymentId);
                    return;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
