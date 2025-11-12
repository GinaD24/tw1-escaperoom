package com.tallerwebi.dominio.interfaz.servicio;

import com.tallerwebi.dominio.entidad.DragDropItem;
import com.tallerwebi.dominio.entidad.Partida;
import com.tallerwebi.dominio.interfaz.repositorio.RepositorioPartida;
import com.tallerwebi.dominio.interfaz.servicio.ValidadorAcertijo;
import com.tallerwebi.presentacion.AcertijoActualDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ValidadorAcertijoDragDrop implements ValidadorAcertijo {

    private final RepositorioPartida repositorioPartida;

    @Autowired
    public ValidadorAcertijoDragDrop(RepositorioPartida repositorioPartida) {
        this.repositorioPartida = repositorioPartida;
    }

    @Override
    public boolean validar(AcertijoActualDTO acertijoActual, String respuestaUsuario, Partida partida, String ordenSecuenciaCorrecto) {
        if (acertijoActual.getId() == null) {
            return false;
        }

        Map<Long, String> respuestaDragDrop = Arrays.stream(respuestaUsuario.split(","))
                .map(pair -> pair.split(":"))
                .filter(arr -> arr.length == 2)
                .collect(Collectors.toMap(
                        arr -> Long.valueOf(arr[0]),
                        arr -> arr[1]
                ));

        List<DragDropItem> itemsCorrectos = this.repositorioPartida.obtenerItemsDragDrop(acertijoActual.getId());

        boolean esCorrecta = itemsCorrectos.stream()
                .allMatch(item ->
                        respuestaDragDrop.containsKey(item.getId()) &&
                                respuestaDragDrop.get(item.getId()).equals(item.getCategoriaCorrecta())
                );

        if (esCorrecta) {
            partida.setPuntaje(partida.getPuntaje() + 100);
        }
        return esCorrecta;
    }
}