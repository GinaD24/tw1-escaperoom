package com.tallerwebi.dominio;

import java.util.List;

public interface RepositorioPartida {
    void guardarPartida(Integer id);

    Etapa obtenerEtapaPorNumero(Integer idSala, Integer numero);

    List<Acertijo> obtenerListaDeAcertijos(Long id);

    List<Pista> obtenerListaDePistas(Long id);

    Boolean obtenerRespuesta(Long id, String respuesta);
}
