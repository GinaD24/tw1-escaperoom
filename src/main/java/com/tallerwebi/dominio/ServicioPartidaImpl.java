package com.tallerwebi.dominio;

import com.tallerwebi.infraestructura.RepositorioPartidaImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Random;

@Service
public class ServicioPartidaImpl implements ServicioPartida {

    private ServicioSala servicioSala;
    private RepositorioPartida repositorioPartida;

    @Autowired
    public ServicioPartidaImpl(ServicioSala servicioSala, RepositorioPartida repositorioPartida) {
        this.servicioSala = servicioSala;
        this.repositorioPartida = repositorioPartida;
    }

    @Override
    public Etapa obtenerEtapaPorNumero(Integer idSala, Integer numeroEtapa) {
        return this.repositorioPartida.obtenerEtapaPorNumero(idSala, numeroEtapa);
    }

    @Override
    public Pista obtenerSiguientePista(Long idAcertijo) {

//        int pistasUsadas = this.repositorioPartida.obtenerPistasUsadas();

        int pistasUsadas = 0;

        List<Pista> listaObtenidaDePistas = this.repositorioPartida.obtenerListaDePistas(idAcertijo);
        Pista pistaSeleccionada = null;

        while (pistasUsadas < listaObtenidaDePistas.size() && pistaSeleccionada == null) {

            switch (pistasUsadas) {
                case 0:
                    pistaSeleccionada = listaObtenidaDePistas.get(0);
                    pistasUsadas++;
                    break;
                case 1:
                    pistaSeleccionada = listaObtenidaDePistas.get(1);
                    pistasUsadas++;
                    break;
                case 2:
                    pistaSeleccionada = listaObtenidaDePistas.get(2);
                    pistasUsadas++;
                    break;
            }

        }

        return pistaSeleccionada;
    }

    @Override
    public Boolean validarRespuesta(Long idAcertijo, String respuesta) {
        boolean esCorrecta = false;
        Respuesta correcta =this.repositorioPartida.obtenerRespuestaCorrecta(idAcertijo);
        String[] palabrasIngresadas = respuesta.toLowerCase().split("\\s+");

        if(Arrays.asList(palabrasIngresadas).contains(correcta.getRespuesta().toLowerCase())){
            esCorrecta = true;
        }

        return esCorrecta;
    }

    @Override
    public void guardarPartida(Partida partida) {
        this.repositorioPartida.guardarPartida(partida);
    }

    @Override
    public Acertijo obtenerAcertijo(Long idEtapa) {
        Acertijo acertijoSeleccionado = null;
        List<Acertijo> listaDeAcertijosObtenida = this.repositorioPartida.obtenerListaDeAcertijos(idEtapa);

        if(!listaDeAcertijosObtenida.isEmpty()) {
            Random random = new Random();
            acertijoSeleccionado = listaDeAcertijosObtenida.get(random.nextInt(listaDeAcertijosObtenida.size()));
        }

        return acertijoSeleccionado;
    }
}
