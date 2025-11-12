// Archivo: com.tallerwebi.dominio.servicio.ValidadorAcertijoFactory.java
package com.tallerwebi.dominio.interfaz.servicio;

import com.tallerwebi.dominio.enums.TipoAcertijo;
import com.tallerwebi.dominio.interfaz.servicio.ValidadorAcertijo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Component
public class ValidadorAcertijoFactory {

    private final List<ValidadorAcertijo> validadores;

    private final Map<TipoAcertijo, ValidadorAcertijo> validadorMap = new EnumMap<>(TipoAcertijo.class);

    @Autowired
    public ValidadorAcertijoFactory(List<ValidadorAcertijo> validadores) {
        this.validadores = validadores;
    }

    @PostConstruct
    private void init() {
        for (ValidadorAcertijo validador : validadores) {
            if (validador instanceof ValidadorAcertijoAdivinanza) {
                validadorMap.put(TipoAcertijo.ADIVINANZA, validador);
            } else if (validador instanceof ValidadorAcertijoOrdenImagen) {
                validadorMap.put(TipoAcertijo.ORDENAR_IMAGEN, validador);
            } else if (validador instanceof ValidadorAcertijoSecuencia) {
                validadorMap.put(TipoAcertijo.SECUENCIA, validador);
            } else if (validador instanceof ValidadorAcertijoDragDrop) {
                validadorMap.put(TipoAcertijo.DRAG_DROP, validador);
            }
        }
    }

    public ValidadorAcertijo getValidador(TipoAcertijo tipo) {
        ValidadorAcertijo validador = validadorMap.get(tipo);
        if (validador == null) {
            throw new UnsupportedOperationException("Tipo de acertijo no soportado: " + tipo);
        }
        return validador;
    }
}