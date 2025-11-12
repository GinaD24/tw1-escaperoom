package com.tallerwebi.dominio.interfaz.servicio;

import com.tallerwebi.dominio.entidad.Partida;
import com.tallerwebi.dominio.interfaz.repositorio.RepositorioPartida;
import com.tallerwebi.dominio.interfaz.servicio.ValidadorAcertijo;
import com.tallerwebi.presentacion.AcertijoActualDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ValidadorAcertijoOrdenImagen implements ValidadorAcertijo {

    private final RepositorioPartida repositorioPartida;

    @Autowired
    public ValidadorAcertijoOrdenImagen(RepositorioPartida repositorioPartida) {
        this.repositorioPartida = repositorioPartida;
    }

    @Override
    public boolean validar(AcertijoActualDTO acertijoActual, String respuestaUsuario, Partida partida, String ordenSecuenciaCorrecto) {
        if (acertijoActual.getId() == null) {
            return false;
        }

        List<Long> ordenSeleccionado = Arrays.stream(respuestaUsuario.split(","))
                .map(Long::valueOf)
                .collect(Collectors.toList());

        List<Long> ordenCorrecto = this.repositorioPartida.obtenerOrdenDeImgCorrecto(acertijoActual.getId());

        boolean esCorrecta = ordenSeleccionado.equals(ordenCorrecto);

        if (esCorrecta) {
            partida.setPuntaje(partida.getPuntaje() + 100);
        }
        return esCorrecta;
    }
}