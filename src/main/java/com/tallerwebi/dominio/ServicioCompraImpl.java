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
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${mp.base.url}")
    private String mpBaseUrl;

    @Autowired
    public ServicioCompraImpl(RepositorioCompra repositorioCompra) {
        this.repositorioCompra = repositorioCompra;
    }

    @Override
    @Transactional
    public String iniciarCompra(Usuario usuario, Sala sala) {
        Compra nuevaCompra = new Compra(usuario, sala, LocalDateTime.now(), false);
        repositorioCompra.guardar(nuevaCompra);

        nuevaCompra.setExternalReference(nuevaCompra.getId().toString());
        repositorioCompra.guardarCompra(nuevaCompra);

        try {
            String confirmacionUrl = "http://localhost:8080/spring/compra/confirmacion"
                    + "?payment_id=12345"
                    + "&status=approved"
                    + "&external_reference=" + nuevaCompra.getExternalReference();

            System.out.println("URL de confirmación generada: " + confirmacionUrl);
            return confirmacionUrl;

        } catch (Exception e) {
            throw new RuntimeException("Error al crear preferencia: " + e.getMessage());
        }
    }


    @Override
    @Transactional
    public void confirmarPago(String paymentId) {
        try {
            System.out.println("Procesando pago mock: " + paymentId);

        } catch (Exception e) {
            System.err.println("Error al confirmar el pago: " + e.getMessage());
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
            System.out.println("✓ Compra confirmada exitosamente. Sala ID: " + compra.getSala().getId());
        } else {
            throw new RuntimeException("No se encontró la compra con external_reference: " + externalReference);
        }
    }
}