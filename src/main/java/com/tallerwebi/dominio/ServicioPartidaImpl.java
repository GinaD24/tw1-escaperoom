package com.tallerwebi.dominio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServicioPartidaImpl implements ServicioPartida{

    @Autowired
    ServicioSala servicioSala;


    @Override
    public Etapa obtenerEtapaPorNumero(Integer idSala, Integer numeroEtapa) {
        return new Etapa();
    }

    @Override
    public Pista obtenerSiguientePista(Long idAcertijo) {
//        // Por ejemplo:
//        // 1. Obtener la lista de pistas del acertijo
//        List<String> pistas = repoTal.obtenerPistasPorAcertijo(idAcertijo);
//
//        // 2. Obtener la cantidad de pistas usadas por el usuario actual en esta partida
//        int indice = obtenerCantidadPistasUsadas(idAcertijo, idUsuarioActual); // implementa esto
//
//        // 3. Devolver la siguiente pista disponible
//        if(indice < pistas.size()) {
//            marcarPistaUsada(idAcertijo, idUsuarioActual, indice);
//            return pistas.get(indice);
//        } else {
//            return "No hay más pistas disponibles.";
//        }
//    }
        return null;
    }

    @Override
    public void guardarPartida(Integer idSala) {

    }

    @Override
    public Acertijo obtenerAcertijo(Long idAcertijo) {
        return new Acertijo();
    }
}
