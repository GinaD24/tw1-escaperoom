package com.tallerwebi.dominio;

import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.preference.PreferenceBackUrlsRequest;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
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

    @Autowired
    public ServicioCompraImpl(RepositorioCompra repositorioCompra) {
        this.repositorioCompra = repositorioCompra;
    }

    @Override
    @Transactional
    public void iniciarCompra(Usuario usuario, Sala sala) {
        Compra compra = new Compra(usuario, sala, LocalDateTime.now(), false);
        repositorioCompra.guardar(compra);
    }

    @Override
    @Transactional
    public void confirmarPago(String paymentId) {
        // Busca la compra por paymentId y marca como pagada
        Compra compra = repositorioCompra.obtenerCompraPorPaymentId(paymentId);
        if (compra != null) {
            compra.setPagada(true);
            repositorioCompra.guardarCompra(compra);
            // Opcional: Desbloquear sala para el usuario
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

    // En ServicioCompraImpl.java (implementación)
    @Override
    public String crearPreferenciaParaSala(Sala sala) {
        try {
            MercadoPagoConfig.setAccessToken("APP_USR-6211919620729480-102619-24d439b82c041fa247a03901e9badbd0-2948865251");

            PreferenceItemRequest itemRequest = PreferenceItemRequest.builder()
                    .id("test" + sala.getId().toString())
                    .title("Acceso a sala: " + sala.getNombre())  // Usa el nombre sanitizado
                    .description("Desbloquea sala en Escape Room")
                    .categoryId("entertainment")
                    .quantity(1)
                    .currencyId("ARS")
                    .unitPrice(new BigDecimal("1"))
                    .build();

            List<PreferenceItemRequest> items = new ArrayList<>();
            items.add(itemRequest);

            // BackUrls con localhost (sin autoReturn por ahora)
            PreferenceBackUrlsRequest backUrls = PreferenceBackUrlsRequest.builder()
                    .success("http://localhost:8080/spring/compra/exito")
                    .failure("http://localhost:8080/spring/compra/fallo")
                    .pending("http://localhost:8080/spring/compra/pendiente")
                    .build();

            PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                    .items(items)
                    .backUrls(backUrls)
                    // .autoReturn("approved")  // Comentado para desarrollo
                    .build();

            PreferenceClient client = new PreferenceClient();
            Preference preference = client.create(preferenceRequest);

            return preference.getSandboxInitPoint();
        } catch (MPApiException | MPException e) {
            throw new RuntimeException("Error al crear preferencia: " + e.getMessage());
        }
    }
}