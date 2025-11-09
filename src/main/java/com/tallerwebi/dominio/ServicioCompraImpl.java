package com.tallerwebi.dominio;

import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.preference.PreferenceBackUrlsRequest;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.payment.Payment;
import com.mercadopago.resources.preference.Preference;
import com.tallerwebi.dominio.entidad.Compra;
import com.tallerwebi.dominio.entidad.Sala;
import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.interfaz.repositorio.RepositorioCompra;
import com.tallerwebi.dominio.interfaz.servicio.ServicioCompra;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ServicioCompraImpl implements ServicioCompra {

    private RepositorioCompra repositorioCompra;
    private static final String MP_ACCESS_TOKEN = "APP_USR-6211919620729480-102619-24d439b82c041fa247a03901e9badbd0-2948865251";

    public ServicioCompraImpl() {}

    @Autowired
    public ServicioCompraImpl(RepositorioCompra repositorioCompra) {
        this.repositorioCompra = repositorioCompra;
    }

    @Override
    @Transactional
    public String iniciarCompra(Usuario usuario, Sala sala) {
        Compra nuevaCompra = new Compra(usuario, sala, LocalDateTime.now(), false);
        repositorioCompra.guardarCompra(nuevaCompra);
        nuevaCompra.setExternalReference(nuevaCompra.getId().toString());
        repositorioCompra.guardarCompra(nuevaCompra);

        try {
            MercadoPagoConfig.setAccessToken(MP_ACCESS_TOKEN);

            PreferenceItemRequest itemRequest = PreferenceItemRequest.builder()
                    .id(nuevaCompra.getExternalReference())
                    .title("Acceso a sala: " + sala.getNombre())
                    .description("Desbloquea la sala en Escape Room")
                    .categoryId("entertainment")
                    .quantity(1)
                    .currencyId("ARS")
                    .unitPrice(new BigDecimal("100"))
                    .build();

            List<PreferenceItemRequest> items = new ArrayList<>();
            items.add(itemRequest);

            String baseUrl = "https://obtundent-ashton-lawlessly.ngrok-free.dev";
            String notificationBaseUrl = "https://obtundent-ashton-lawlessly.ngrok-free.dev";
            PreferenceBackUrlsRequest backUrls = PreferenceBackUrlsRequest.builder()
                    .success(baseUrl + "/spring/compra/confirmacion")
                    .failure(baseUrl + "/spring/inicio?pago=fallido")
                    .pending(baseUrl + "/spring/inicio?pago=pendiente")
                    .build();
            PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                    .items(items)
                    .backUrls(backUrls)
                    .externalReference(nuevaCompra.getExternalReference())
                    .autoReturn("approved")
                    .notificationUrl(notificationBaseUrl + "/spring/webhook/mercado-pago")
                    .build();

            PreferenceClient client = new PreferenceClient();
            Preference preference = client.create(preferenceRequest);

            return preference.getInitPoint();

        } catch (MPApiException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al crear preferencia (MP): " + e.getMessage(), e);
        } catch (MPException e) { ;

            e.printStackTrace();
            throw new RuntimeException("Error al crear preferencia (General): " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public void confirmarPago(String paymentId) {
        try {
            MercadoPagoConfig.setAccessToken(MP_ACCESS_TOKEN);
            PaymentClient paymentClient = new PaymentClient();
            Payment payment = paymentClient.get(Long.valueOf(paymentId));

            String externalReference = payment.getExternalReference();

            if (externalReference != null) {
                Compra compra = repositorioCompra.obtenerCompraPorExternalReference(externalReference);

                if (compra != null && "approved".equals(payment.getStatus())) {
                    compra.setPagada(true);
                    compra.setPaymentId(paymentId);
                    repositorioCompra.guardarCompra(compra);
                }
            }
        } catch (MPException e) {
            throw new RuntimeException(e);
        } catch (MPApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional
    public List<Compra> obtenerComprasPorUsuario(Usuario usuario) {
        return repositorioCompra.obtenerComprasPorUsuario(usuario);
    }

    @Override
    @Transactional
    public boolean salaDesbloqueadaParaUsuario(Usuario usuario, Sala sala) {
        return repositorioCompra.salaDesbloqueadaParaUsuario(usuario, sala);
    }

    @Override
    @Transactional
    public void confirmarCompraPorExternalReference(String externalReference, String paymentId) {
        Compra compra = repositorioCompra.obtenerCompraPorExternalReference(externalReference);
        if (compra != null) {
            compra.setPagada(true);
            compra.setPaymentId(paymentId);
            repositorioCompra.guardarCompra(compra);
        }
    }

}
