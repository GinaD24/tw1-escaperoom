package com.tallerwebi.dominio.interfaz.repositorio;

import com.tallerwebi.dominio.entidad.*;

import java.util.List;

public interface RepositorioPartida {
    void guardarPartida(Partida partida);

    Etapa obtenerEtapaPorNumero(Integer idSala, Integer numero);

    List<Acertijo> obtenerListaDeAcertijos(Long idEtapa);

    List<Pista> obtenerListaDePistas(Long idAcertijo);

    Respuesta obtenerRespuestaCorrecta(Long idAcertijo);

    void registrarAcertijoMostrado(AcertijoUsuario acertijoUsuario);

    Integer obtenerPistasUsadas(Long idAcertijo, Long id_usuario);

    void sumarPistaUsada(Long idAcertijo, Long idUsuario);

    List<Acertijo> obtenerAcertijosVistosPorUsuarioPorEtapa(Long idUsuario, Long idEtapa);

    void eliminarRegistrosDeAcertijosVistos(Long idUsuario);

    Acertijo buscarAcertijoPorId(Long idAcertijo);

    Etapa buscarEtapaPorId(Long idEtapa);

    Partida obtenerPartidaActivaPorUsuario(Long idUsuario);

    void finalizarPartida(Partida partida);

    void registrarPistaEnPartida(Long idUsuario);

    List<Long> obtenerOrdenDeImgCorrecto(Long idAcertijo);

    List<DragDropItem> obtenerItemsDragDrop(Long idAcertijo);

    Partida buscarPartidaPorId(Long idPartida);

    Acertijo traerAcertijoBonus(Long idEtapa);
}
