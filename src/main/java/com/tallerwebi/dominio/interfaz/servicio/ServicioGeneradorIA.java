package com.tallerwebi.dominio.interfaz.servicio;

import java.util.List;

public interface ServicioGeneradorIA {

    List<String> generarPistas(String descripcionAcertijo, String respuestaCorrecta) throws Exception;
}