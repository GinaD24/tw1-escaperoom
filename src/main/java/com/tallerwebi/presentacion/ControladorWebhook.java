package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.interfaz.servicio.ServicioCompra;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController //indica que expone una API REST.
@RequestMapping("/webhook")
public class ControladorWebhook {

    private final ServicioCompra servicioCompra;
// controlador no es llamado por el navegador del usuario; es llamado directamente por los servidores de Mercado Pago.
    @Autowired
    public ControladorWebhook(ServicioCompra servicioCompra) {
        this.servicioCompra = servicioCompra;
    }

    @PostMapping("/mercado-pago")
    public void manejarWebhook(@RequestBody Map<String, Object> payload) { //encargado de recibir la notificación y asegurarse de que el pago se confirme en tu base de datos.
        try {
            Object dataObj = payload.get("data"); //Recibe todo el cuerpo de la notificación de Mercado Pago en la variable payload
           //busca la clave "data" y, dentro de ella, intenta obtener el valor de "id". Este ID es el Payment ID de Mercado Pago que necesitas.
            if (dataObj instanceof Map) { //Verifica si el objeto extraído es un Mapa sub-JSON válido)
                Map<String, Object> data = (Map<String, Object>) dataObj; //se convierte en un map para poder acceder a sus claves fácilmente.
                Object id = data.get("id"); //obtener el identificador del pago buscando la clave "id"
                if (id == null) id = data.get("id");
                if (id != null) {
                    String paymentId = id.toString();
                    servicioCompra.confirmarPago(paymentId); //función buscará la Compra asociada y la marcará como pagada
                                                                // en tu base de datos si el estado en MP es aprobado.
                    return;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
//el usuario cierra la pestaña o pierde la conexión
//es la caja de seguridad que asegura que tu base de datos refleje el estado final del pago,
// de forma autónoma y confiable, sin depender de lo que haga el usuario en su navegador.