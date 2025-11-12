package com.tallerwebi.dominio.interfaz.servicio;

import com.tallerwebi.dominio.entidad.Partida;
import com.tallerwebi.presentacion.AcertijoActualDTO;

public interface ValidadorAcertijo {

    boolean validar(AcertijoActualDTO acertijoActual, String respuestaUsuario, Partida partida, String ordenSecuenciaCorrecto);
}