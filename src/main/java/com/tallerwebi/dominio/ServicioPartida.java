package com.tallerwebi.dominio;

public interface ServicioPartida {

    void guardarPartida(Integer idSala);

    Acertijo obtenerAcertijo(Long id);

    Etapa obtenerEtapaPorNumero(Integer idSala, Integer numeroEtapa);

    Pista obtenerSiguientePista(Long idAcertijo);
}
