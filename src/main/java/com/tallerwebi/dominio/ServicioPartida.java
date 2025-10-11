package com.tallerwebi.dominio;

public interface ServicioPartida {

    void guardarPartida(Partida partida);

    Acertijo obtenerAcertijo(Long id, Long id_usuario);

    Etapa obtenerEtapaPorNumero(Integer idSala, Integer numeroEtapa);

    Pista obtenerSiguientePista(Long idAcertijo, Long id_usuario);

    Boolean validarRespuesta(Long idAcertijo , String respuesta);
}
