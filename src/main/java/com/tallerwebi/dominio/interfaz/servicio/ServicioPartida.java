package com.tallerwebi.dominio.interfaz.servicio;

import com.tallerwebi.dominio.entidad.*;
import com.tallerwebi.presentacion.AcertijoActualDTO;

import java.util.List;

public interface ServicioPartida {

    void guardarPartida(Partida partida, Long idUsuario, Integer idSala);

    Acertijo obtenerAcertijo(Long idEtapa, Long id_usuario);

    Etapa obtenerEtapaPorNumero(Integer idSala, Integer numeroEtapa);

    void registrarUsoDePista(Long idUsuario);

    Boolean validarRespuesta(AcertijoActualDTO acertijoActual, String respuestaUsuario, Long idUsuario);

    Acertijo buscarAcertijoPorId(Long idAcertijo);

    Etapa obtenerEtapaPorId(Long idEtapa);

    void finalizarPartida(Long idUsuario, Boolean ganada);

    boolean tiempoExpirado(Partida partida);

    Partida obtenerPartidaActivaPorIdUsuario(Long idUsuario);

    List<String> obtenerCategoriasDelAcertijoDragDrop(Long idAcertijo);

    Partida buscarPartidaPorId(Long idPartida);
}
