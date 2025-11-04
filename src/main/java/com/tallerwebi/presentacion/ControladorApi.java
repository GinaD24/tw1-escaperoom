package com.tallerwebi.presentacion;

import com.mercadopago.client.preference.PreferenceBackUrlsRequest;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.preference.Preference;
import com.tallerwebi.dominio.entidad.Sala;
import com.tallerwebi.dominio.interfaz.servicio.ServicioSala;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.mercadopago.MercadoPagoConfig;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping
public class ControladorApi {

    private ServicioSala servicioSala;

    @Autowired
    public ControladorApi(ServicioSala servicioSala) {
        this.servicioSala = servicioSala;
    }

    @GetMapping("/mercado")
    public Map<String, String> mercado() throws MPException, MPApiException {
        MercadoPagoConfig.setAccessToken("APP_USR-6211919620729480-102619-24d439b82c041fa247a03901e9badbd0-2948865251");

        PreferenceItemRequest itemRequest = PreferenceItemRequest.builder()
                .id("1234")
                .title("Games")
                .description("PS5")
                .categoryId("games")
                .quantity(2)
                .currencyId("ARS")
                .unitPrice(new BigDecimal("1"))
                .build();

        List<PreferenceItemRequest> items = new ArrayList<>();
        items.add(itemRequest);

        PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                .items(items)
                .build();

        PreferenceClient client = new PreferenceClient();
        Preference preference = client.create(preferenceRequest);

        Map<String, String> response = new HashMap<>();
        response.put("init_point", preference.getSandboxInitPoint());
        return response;
    }

    @GetMapping("/mercado/preferencia/{idSala}")
    public Map<String, String> crearPreferenciaParaSala(@PathVariable Integer idSala) {
        try {
            MercadoPagoConfig.setAccessToken("APP_USR-6211919620729480-102619-24d439b82c041fa247a03901e9badbd0-2948865251");

            Sala sala = servicioSala.obtenerSalaPorId(idSala);
            if (sala == null) {
                throw new RuntimeException("Sala no encontrada");
            }

            PreferenceItemRequest itemRequest = PreferenceItemRequest.builder()
                    .id("test" + sala.getId().toString())
                    .title("Acceso a sala test")
                    .description("Desbloquea sala en Escape Room")
                    .categoryId("entertainment")
                    .quantity(1)
                    .currencyId("ARS")
                    .unitPrice(new BigDecimal("1"))
                    .build();

            List<PreferenceItemRequest> items = new ArrayList<>();
            items.add(itemRequest);

            // Logging y URLs con contexto /spring
            System.err.println("Creando backUrls con contexto /spring...");
            PreferenceBackUrlsRequest backUrls = PreferenceBackUrlsRequest.builder()
                    .success("https://b5c2b1539e2b.ngrok-free.app/spring/inicio/")
                    .failure("https://b5c2b1539e2b.ngrok-free.app/spring/compra/fallo")
                    .pending("https://b5c2b1539e2b.ngrok-free.app/spring/compra/pendiente")
                    .build();
            System.err.println("backUrls creadas: success=" + backUrls.getSuccess());

            PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                    .items(items)
                    .backUrls(backUrls)
                    .autoReturn("approved")
                    .build();

            PreferenceClient client = new PreferenceClient();
            Preference preference = client.create(preferenceRequest);

            Map<String, String> response = new HashMap<>();
            response.put("init_point", preference.getSandboxInitPoint());
            return response;
        } catch (MPApiException e) {
            System.err.println("Error en Mercado Pago API: " + e.getMessage());
            System.err.println("Status Code: " + e.getStatusCode());
            if (e.getApiResponse() != null) {
                System.err.println("Respuesta de API: " + e.getApiResponse().getContent());
            }
            e.printStackTrace();
            throw new RuntimeException("Error al crear preferencia: " + e.getMessage());
        } catch (MPException e) {
            System.err.println("Error general: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error general: " + e.getMessage());
        }
    }
}