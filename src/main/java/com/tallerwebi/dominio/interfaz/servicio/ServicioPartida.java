package com.tallerwebi.dominio.interfaz.servicio;

import com.tallerwebi.dominio.entidad.*;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.Set;

public interface ServicioPartida {

    void guardarPartida(Partida partida, Long idUsuario, Integer idSala);

    Acertijo obtenerAcertijo(Long idEtapa, Long id_usuario);

    Etapa obtenerEtapaPorNumero(Integer idSala, Integer numeroEtapa);

    Pista obtenerSiguientePista(Long idAcertijo, Long id_usuario);

    Boolean validarRespuesta(Long idAcertijo , String respuesta, Long idUsuario,  @Nullable String ordenSecuenciaCorrecto);

    Acertijo buscarAcertijoPorId(Long idAcertijo);

    Etapa obtenerEtapaPorId(Long idEtapa);

    void finalizarPartida(Long idUsuario, Boolean ganada);

    boolean tiempoExpirado(Partida partida);

    Partida obtenerPartidaActivaPorIdUsuario(Long idUsuario);

    List<String> obtenerCategoriasDelAcertijoDragDrop(Long idAcertijo);

    Partida buscarPartidaPorId(Long idPartida);

    List<ImagenAcertijo> obtenerSecuenciaAleatoria(Acertijo acertijo);
}
