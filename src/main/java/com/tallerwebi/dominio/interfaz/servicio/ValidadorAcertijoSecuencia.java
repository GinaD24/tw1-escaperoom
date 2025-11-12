package com.tallerwebi.dominio.interfaz.servicio;

import com.tallerwebi.dominio.entidad.Partida;
import com.tallerwebi.dominio.interfaz.servicio.ValidadorAcertijo;
import com.tallerwebi.presentacion.AcertijoActualDTO;
import org.springframework.stereotype.Component;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ValidadorAcertijoSecuencia implements ValidadorAcertijo {

    @Override
    public boolean validar(AcertijoActualDTO acertijoActual, String respuestaUsuario, Partida partida, String ordenSecuenciaCorrecto) {
        if (ordenSecuenciaCorrecto == null) {
            throw new IllegalArgumentException("Se requiere el orden correcto para acertijo SECUENCIA");
        }

        List<Long> ordenSeleccionadoSecuencia = Arrays.stream(respuestaUsuario.split(","))
                .map(Long::valueOf)
                .collect(Collectors.toList());

        List<Long> ordenCorrectoList = Arrays.stream(ordenSecuenciaCorrecto.split(","))
                .map(Long::valueOf)
                .collect(Collectors.toList());

        boolean esCorrecta = ordenSeleccionadoSecuencia.equals(ordenCorrectoList);

        if (esCorrecta) {
            partida.setPuntaje(partida.getPuntaje() + 100);
        }
        return esCorrecta;
    }
}