package com.tallerwebi.dominio.interfaz.servicio;

import com.tallerwebi.dominio.entidad.Compra;
import com.tallerwebi.dominio.entidad.Sala;
import com.tallerwebi.dominio.entidad.Usuario;

import java.util.List;

public interface ServicioCompra {
    String iniciarCompra(Usuario usuario, Sala sala);
    void confirmarPago(String paymentId);
    List<Compra> obtenerComprasPorUsuario(Usuario usuario);
    boolean salaDesbloqueadaParaUsuario(Usuario usuario, Sala sala);
}