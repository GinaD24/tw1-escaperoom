package com.tallerwebi.dominio.interfaz.repositorio;

import com.tallerwebi.dominio.entidad.Compra;
import com.tallerwebi.dominio.entidad.Sala;
import com.tallerwebi.dominio.entidad.Usuario;
import java.util.List;

public interface RepositorioCompra {
    void guardar(Compra compra);
    List<Compra> obtenerComprasPorUsuario(Usuario usuario);
    Compra obtenerCompraPorUsuarioYSala(Usuario usuario, Sala sala);
    boolean salaDesbloqueadaParaUsuario(Usuario usuario, Sala sala);
    Compra obtenerCompraPorPaymentId(String paymentId);
    void guardarCompra(Compra compra);
    Compra obtenerCompraPorExternalReference(String externalReference);
}