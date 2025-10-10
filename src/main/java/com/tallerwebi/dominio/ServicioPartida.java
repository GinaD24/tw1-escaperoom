package com.tallerwebi.dominio;

public interface ServicioPartida {

    void guardarPartida(Partida partida);

    Acertijo obtenerAcertijo(Long id);

    Etapa obtenerEtapaPorNumero(Integer idSala, Integer numeroEtapa);

    Pista obtenerSiguientePista(Long idAcertijo);

    Boolean validarRespuesta(Long idAcertijo , String respuesta);
}
