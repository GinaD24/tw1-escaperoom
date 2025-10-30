package com.tallerwebi.dominio.interfaz.servicio;

import com.tallerwebi.dominio.entidad.Compra;
import com.tallerwebi.dominio.entidad.Sala;
import com.tallerwebi.dominio.entidad.Usuario;
import java.util.List;

public interface ServicioCompra {
    void iniciarCompra(Usuario usuario, Sala sala);
    void confirmarPago(String paymentId);  // Para webhook
    List<Compra> obtenerComprasPorUsuario(Usuario usuario);
    boolean salaDesbloqueadaParaUsuario(Usuario usuario, Sala sala);
    String crearPreferenciaParaSala(Sala sala);
}