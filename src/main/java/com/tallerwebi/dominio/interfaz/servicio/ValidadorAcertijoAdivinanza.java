
package com.tallerwebi.dominio.interfaz.servicio;

import com.tallerwebi.dominio.entidad.Partida;
import com.tallerwebi.dominio.interfaz.servicio.ValidadorAcertijo;
import com.tallerwebi.presentacion.AcertijoActualDTO;
import org.springframework.stereotype.Component;
import java.util.Arrays;

@Component
public class ValidadorAcertijoAdivinanza implements ValidadorAcertijo {

    @Override
    public boolean validar(AcertijoActualDTO acertijoActual, String respuestaUsuario, Partida partida, String ordenSecuenciaCorrecto) {
        String respuestaCorrecta = acertijoActual.getRespuestaCorrecta();

        String[] palabrasIngresadas = respuestaUsuario.toLowerCase().split("\\s+");
        boolean esCorrecta = Arrays.asList(palabrasIngresadas).contains(respuestaCorrecta.toLowerCase());

        if(esCorrecta){
            partida.setPuntaje(partida.getPuntaje() + 100);
        }
        return esCorrecta;
    }
}