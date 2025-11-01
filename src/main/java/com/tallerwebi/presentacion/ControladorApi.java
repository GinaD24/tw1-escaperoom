package com.tallerwebi.presentacion;

import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.preference.Preference;
import org.springframework.web.bind.annotation.GetMapping;
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
    @GetMapping("/mercado")
    public Map<String, String> mercado() throws MPException, MPApiException {

        MercadoPagoConfig.setAccessToken("APP_USR-6211919620729480-102619-24d439b82c041fa247a03901e9badbd0-2948865251");

        PreferenceItemRequest itemRequest = PreferenceItemRequest.builder()
                .id("1234")
                .title("Games")
                .description("PS5")
                .pictureUrl("http://picture.com/PS5")
                .categoryId("games")
                .quantity(2)
                .currencyId("ARS")
                .unitPrice(new BigDecimal("4000"))
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
}
