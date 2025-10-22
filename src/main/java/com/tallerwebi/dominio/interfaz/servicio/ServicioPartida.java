package com.tallerwebi.dominio.interfaz.servicio;

import com.tallerwebi.dominio.entidad.Acertijo;
import com.tallerwebi.dominio.entidad.Etapa;
import com.tallerwebi.dominio.entidad.Partida;
import com.tallerwebi.dominio.entidad.Pista;

import java.util.List;

public interface ServicioPartida {

    void guardarPartida(Partida partida, Long idUsuario, Integer idSala);

    Acertijo obtenerAcertijo(Long idEtapa, Long id_usuario);

    Etapa obtenerEtapaPorNumero(Integer idSala, Integer numeroEtapa);

    Pista obtenerSiguientePista(Long idAcertijo, Long id_usuario);

    Boolean validarRespuesta(Long idAcertijo , String respuesta);

    Acertijo buscarAcertijoPorId(Long idAcertijo);

    Etapa obtenerEtapaPorId(Long idEtapa);

    void finalizarPartida(Long idUsuario, Boolean ganada);

    List<Partida> obtenerHistorialDePartida(Long idUsuario);
}
