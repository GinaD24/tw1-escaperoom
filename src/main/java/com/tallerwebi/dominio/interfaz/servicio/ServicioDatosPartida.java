package com.tallerwebi.dominio.interfaz.servicio;

import com.tallerwebi.presentacion.DatosPartidaDTO;

public interface ServicioDatosPartida {

    DatosPartidaDTO obtenerDatosDePartida(Integer idSala, Integer numeroEtapa, Long idUsuario);
}
