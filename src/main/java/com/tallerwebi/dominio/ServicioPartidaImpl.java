package com.tallerwebi.dominio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Service

public class ServicioPartidaImpl implements ServicioPartida {

    private ServicioSala servicioSala;
    private RepositorioPartida repositorioPartida;
    private RepositorioUsuario repositorioUsuario;

    @Autowired
    public ServicioPartidaImpl(ServicioSala servicioSala, RepositorioPartida repositorioPartida, RepositorioUsuario repositorioUsuario) {
        this.servicioSala = servicioSala;
        this.repositorioPartida = repositorioPartida;
        this.repositorioUsuario = repositorioUsuario;
    }

    @Override
    @Transactional
    public Etapa obtenerEtapaPorNumero(Integer idSala, Integer numeroEtapa) {
        return this.repositorioPartida.obtenerEtapaPorNumero(idSala, numeroEtapa);
    }

    @Override
    @Transactional
    public Pista obtenerSiguientePista(Long idAcertijo, Long id_usuario) {

        Integer pistasUsadas = this.repositorioPartida.obtenerPistasUsadas(idAcertijo, id_usuario);


        List<Pista> listaObtenidaDePistas = this.repositorioPartida.obtenerListaDePistas(idAcertijo);
        Pista pistaSeleccionada = null;

        while (pistasUsadas < listaObtenidaDePistas.size() && pistaSeleccionada == null) {

            switch (pistasUsadas) {
                case 0:
                    pistaSeleccionada = listaObtenidaDePistas.get(0);
                    this.repositorioPartida.sumarPistaUsada(idAcertijo, id_usuario);
                    break;
                case 1:
                    pistaSeleccionada = listaObtenidaDePistas.get(1);
                    this.repositorioPartida.sumarPistaUsada(idAcertijo, id_usuario);
                    break;
                case 2:
                    pistaSeleccionada = listaObtenidaDePistas.get(2);
                    this.repositorioPartida.sumarPistaUsada(idAcertijo, id_usuario);
                    break;
            }

        }

        return pistaSeleccionada;
    }

    @Override
    @Transactional
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
    @Transactional
    public Acertijo buscarAcertijoPorId(Long idAcertijo) {
        return this.repositorioPartida.buscarAcertijoPorId(idAcertijo);
    }

    @Override
    @Transactional
    public Etapa obtenerEtapaPorId(Long idEtapa) {
        return this.repositorioPartida.buscarEtapaPorId(idEtapa);
    }

    @Override
    @Transactional
    public void guardarPartida(Partida partida) {
        partida.setInicio(LocalDateTime.now());
        this.repositorioPartida.guardarPartida(partida);
    }

    @Override
    @Transactional
    public Acertijo obtenerAcertijo(Long idEtapa, Long id_usuario) {
        Acertijo acertijoSeleccionado = null;
        List<Acertijo> listaDeAcertijosObtenida = this.repositorioPartida.obtenerListaDeAcertijos(idEtapa);

        if(!listaDeAcertijosObtenida.isEmpty()) {
            List<Acertijo> acertijosVistos = this.repositorioPartida.obtenerAcertijosVistosPorUsuario(id_usuario);
            if (acertijosVistos != null && acertijosVistos.size() == listaDeAcertijosObtenida.size()) {
                this.repositorioPartida.eliminarRegistrosDePartidas(id_usuario);
                acertijosVistos.clear();
            }

            Random random = new Random();
            do {
                acertijoSeleccionado = listaDeAcertijosObtenida.get(random.nextInt(listaDeAcertijosObtenida.size()));
            } while (acertijosVistos != null && acertijosVistos.contains(acertijoSeleccionado));


            Usuario usuario = repositorioUsuario.obtenerUsuarioPorId(id_usuario);
            AcertijoUsuario acertijoUsuario = new AcertijoUsuario(acertijoSeleccionado, usuario);
            this.repositorioPartida.registrarAcertijoMostrado(acertijoUsuario);
        }

        return acertijoSeleccionado;
    }
}
