package com.tallerwebi.dominio;

import com.tallerwebi.dominio.entidad.*;
import com.tallerwebi.dominio.excepcion.EtapaInexistente;
import com.tallerwebi.dominio.excepcion.IDUsuarioInvalido;
import com.tallerwebi.dominio.excepcion.SesionDeUsuarioExpirada;
import com.tallerwebi.dominio.excepcion.UsuarioInexistente;
import com.tallerwebi.dominio.interfaz.repositorio.RepositorioPartida;
import com.tallerwebi.dominio.interfaz.repositorio.RepositorioSala;
import com.tallerwebi.dominio.interfaz.repositorio.RepositorioUsuario;
import com.tallerwebi.dominio.interfaz.servicio.ServicioPartida;
import com.tallerwebi.dominio.interfaz.servicio.ServicioSala;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Service

public class ServicioPartidaImpl implements ServicioPartida {

    private ServicioSala servicioSala;
    private RepositorioPartida repositorioPartida;
    private RepositorioUsuario repositorioUsuario;
    private RepositorioSala repositorioSala;

    @Autowired
    public ServicioPartidaImpl(ServicioSala servicioSala, RepositorioPartida repositorioPartida,
                               RepositorioUsuario repositorioUsuario, RepositorioSala repositorioSala) {
        this.servicioSala = servicioSala;
        this.repositorioPartida = repositorioPartida;
        this.repositorioUsuario = repositorioUsuario;
        this.repositorioSala = repositorioSala;
    }

    @Override
    @Transactional
    public Etapa obtenerEtapaPorNumero(Integer idSala, Integer numeroEtapa) {
        Etapa etapaObtenida = this.repositorioPartida.obtenerEtapaPorNumero(idSala, numeroEtapa);
        if (etapaObtenida == null){
            throw new EtapaInexistente();
        }
        return etapaObtenida;
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
        this.repositorioPartida.registrarPistaEnPartida(id_usuario);
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
    public void finalizarPartida(Long idUsuario, Boolean ganada) {
        Partida partida = this.repositorioPartida.obtenerPartidaActivaPorUsuario(idUsuario);

        if (partida != null) {
            partida.setEsta_activa(false);
            partida.setGanada(ganada);
            LocalDateTime fin = LocalDateTime.now();
            partida.setFin(fin);
            LocalDateTime inicio = partida.getInicio();

            if (inicio != null) {
                Long duracionSegundos = Duration.between(inicio, fin).getSeconds();
                partida.setTiempoTotal(duracionSegundos);
            }
            this.repositorioPartida.finalizarPartida(partida);
        }
    }

    @Override
    @Transactional
    public void guardarPartida(Partida partida, Long idUsuario, Integer idSala) {

        if(idUsuario == null){
            throw new SesionDeUsuarioExpirada();
        }

        Usuario usuario = repositorioUsuario.obtenerUsuarioPorId(idUsuario);

        if(usuario == null){
            throw new UsuarioInexistente();
        }

        Sala sala = repositorioSala.obtenerSalaPorId(idSala);
        partida.setUsuario(usuario);
        partida.setSala(sala);
        partida.setInicio(LocalDateTime.now());
        partida.setEsta_activa(true);
        this.repositorioPartida.guardarPartida(partida);
    }

    @Override
    @Transactional
    public Acertijo obtenerAcertijo(Long idEtapa, Long id_usuario) {
        Acertijo acertijoSeleccionado = null;
        List<Acertijo> listaDeAcertijosObtenida = this.repositorioPartida.obtenerListaDeAcertijos(idEtapa);

        if(!listaDeAcertijosObtenida.isEmpty()) {
            List<Acertijo> acertijosVistos = this.repositorioPartida.obtenerAcertijosVistosPorUsuarioPorEtapa(id_usuario, idEtapa);
            if (acertijosVistos != null && acertijosVistos.size() == listaDeAcertijosObtenida.size()) {
                this.repositorioPartida.eliminarRegistrosDeAcertijosVistos(id_usuario);
                acertijosVistos.clear();
            }

            Random random = new Random();
            do {
                acertijoSeleccionado = listaDeAcertijosObtenida.get(random.nextInt(listaDeAcertijosObtenida.size()));
            } while (acertijosVistos != null && acertijosVistos.contains(acertijoSeleccionado));


            Usuario usuario = repositorioUsuario.obtenerUsuarioPorId(id_usuario);
            AcertijoUsuario acertijoUsuario = new AcertijoUsuario(acertijoSeleccionado, usuario);
            Etapa etapa = this.repositorioPartida.buscarEtapaPorId(idEtapa);
            acertijoUsuario.setEtapa(etapa);
            this.repositorioPartida.registrarAcertijoMostrado(acertijoUsuario);
        }

        return acertijoSeleccionado;
    }

    public List<Partida> obtenerHistorialDePartida(Long idUsuario) {
        if (idUsuario == null) {
            throw new IDUsuarioInvalido();
        }
        return repositorioPartida.obtenerHistorialDePartida(idUsuario);
    }
}
