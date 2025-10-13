package com.tallerwebi.dominio;

public interface ServicioPartida {

    void guardarPartida(Partida partida, Long idUsuario);

    Acertijo obtenerAcertijo(Long idEtapa, Long id_usuario);

    Etapa obtenerEtapaPorNumero(Integer idSala, Integer numeroEtapa);

    Pista obtenerSiguientePista(Long idAcertijo, Long id_usuario);

    Boolean validarRespuesta(Long idAcertijo , String respuesta);

    Acertijo buscarAcertijoPorId(Long idAcertijo);

    Etapa obtenerEtapaPorId(Long idEtapa);

    Integer obtenerCantidadDeEtapas(Integer idSala);

    void finalizarPartida(Long idUsuario);
}
