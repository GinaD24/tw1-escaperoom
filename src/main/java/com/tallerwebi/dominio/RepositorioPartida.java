package com.tallerwebi.dominio;

import java.util.List;

public interface RepositorioPartida {
    void guardarPartida(Partida partida);

    Etapa obtenerEtapaPorNumero(Integer idSala, Integer numero);

    List<Acertijo> obtenerListaDeAcertijos(Long idEtapa);

    List<Pista> obtenerListaDePistas(Long idAcertijo);

    Respuesta obtenerRespuestaCorrecta(Long idAcertijo);

    void registrarAcertijoMostrado(AcertijoUsuario  acertijoUsuario);

    Integer obtenerPistasUsadas(Long idAcertijo, Long id_usuario);

    void sumarPistaUsada(Long idAcertijo, Long idUsuario);

    List<Acertijo> obtenerAcertijosVistosPorUsuarioPorEtapa(Long idUsuario, Long idEtapa);

    void eliminarRegistrosDePartidas(Long idUsuario);

    Acertijo buscarAcertijoPorId(Long idAcertijo);

    Etapa buscarEtapaPorId(Long idEtapa);

    Partida obtenerPartidaActivaPorUsuario(Long idUsuario);

    void finalizarPartida(Partida partida);

}
