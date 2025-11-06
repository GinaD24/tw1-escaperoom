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
    // Es una forma de comprobar rápidamente que la configuración de Mercado Pago (el access_token) funciona y se puede generar una URL de pago.
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

    @GetMapping("/mercado/preferencia/{idSala}") //Generar una URL de pago para una prueba rápida o integración simple.
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

            PreferenceBackUrlsRequest backUrls = PreferenceBackUrlsRequest.builder()
                    .success("http://localhost:8080/spring/compra/confirmacion")
                    .failure("http://localhost:8080/spring/inicio?pago=fallido")
                    .pending("http://localhost:8080/spring/inicio?pago=pendiente")
                    .build();

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
            e.printStackTrace();
            throw new RuntimeException("Error al crear preferencia: " + e.getMessage());
        } catch (MPException e) {
            e.printStackTrace();
            throw new RuntimeException("Error general: " + e.getMessage());
        }
    }
}