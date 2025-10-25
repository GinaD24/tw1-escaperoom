package com.tallerwebi.dominio;

import com.tallerwebi.dominio.entidad.*;
import com.tallerwebi.dominio.enums.TipoAcertijo;
import com.tallerwebi.dominio.excepcion.EtapaInexistente;
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
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

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
        if (pistaSeleccionada != null) {
            this.repositorioPartida.registrarPistaEnPartida(id_usuario);
            Partida partida = this.repositorioPartida.obtenerPartidaActivaPorUsuario(id_usuario);
            partida.setPuntaje(partida.getPuntaje() - 25);
        }

        return pistaSeleccionada;
    }

    @Override
    @Transactional
    public Boolean validarRespuesta(Long idAcertijo, String respuesta, Long idUsuario) {
        Partida partida = this.repositorioPartida.obtenerPartidaActivaPorUsuario(idUsuario);
        boolean esCorrecta = false;

        Acertijo acertijo = this.repositorioPartida.buscarAcertijoPorId(idAcertijo);


        switch(acertijo.getTipo()){
            case ADIVINANZA:
                String[] palabrasIngresadas = respuesta.toLowerCase().split("\\s+");

                Respuesta correcta =this.repositorioPartida.obtenerRespuestaCorrecta(idAcertijo);
                if(Arrays.asList(palabrasIngresadas).contains(correcta.getRespuesta().toLowerCase())){
                    esCorrecta = true;
                    partida.setPuntaje(partida.getPuntaje() + 100);
                }
                break;

            case ORDENAR_IMAGEN:
                List<Long> ordenSeleccionado = Arrays.stream(respuesta.split(","))
                        .map(Long::valueOf)
                        .collect(Collectors.toList());

                List<Long> ordenCorrecto = this.repositorioPartida.obtenerOrdenDeImgCorrecto(idAcertijo);

                if (ordenSeleccionado.equals(ordenCorrecto)) {
                    esCorrecta = true;
                    partida.setPuntaje(partida.getPuntaje() + 100);
                }
                break;

            case DRAG_DROP:
                Map<Long, String> respuestaUsuario = Arrays.stream(respuesta.split(","))
                        .map(pair -> pair.split(":"))
                        .filter(arr -> arr.length == 2)
                        .collect(Collectors.toMap(
                                arr -> Long.valueOf(arr[0]), // id de imagen
                                arr -> arr[1]               // categoría donde la puso
                        ));

                List<DragDropItem> itemsCorrectos = this.repositorioPartida.obtenerItemsDragDrop(idAcertijo);

                boolean todoCorrecto = itemsCorrectos.stream()
                        .allMatch(item ->
                                respuestaUsuario.containsKey(item.getId()) &&
                                        respuestaUsuario.get(item.getId()).equals(item.getCategoriaCorrecta())
                        );
                if (todoCorrecto) {
                    esCorrecta = true;
                    partida.setPuntaje(partida.getPuntaje() + 100);
                }
                break;

            case SECUENCIA:
                // Parsear respuesta del usuario
                List<Long> secuenciaUsuario = Arrays.stream(respuesta.split(","))
                        .map(Long::valueOf)
                        .collect(Collectors.toList());

                // Obtener la secuencia correcta desde la base de datos
                List<Long> secuenciaCorrecta = this.repositorioPartida.obtenerOrdenDeImgCorrecto(idAcertijo);

                if(secuenciaUsuario.equals(secuenciaCorrecta)){
                    esCorrecta = true;
                    partida.setPuntaje(partida.getPuntaje() + 100);
                }
                break;



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
            if(partida.getPuntaje() < 0){
                partida.setPuntaje(0);
            }

            this.repositorioPartida.finalizarPartida(partida);
        }
    }

    @Override
    @Transactional
    public boolean validarTiempo(Long id_usuario) {
        Partida partida = repositorioPartida.obtenerPartidaActivaPorUsuario(id_usuario);
        Sala salaPartida = partida.getSala();

        LocalDateTime inicio = partida.getInicio();
        LocalDateTime tiempoActual = LocalDateTime.now();
        Long duracionMinutos = Duration.between(inicio, tiempoActual).toMinutes();

        boolean tiempoValido = true;

        if(duracionMinutos >= salaPartida.getDuracion()) {
            tiempoValido = false;
        }
        return tiempoValido;
    }

    @Override
    @Transactional
    public Partida obtenerPartidaActivaPorIdUsuario(Long idUsuario) {
        return this.repositorioPartida.obtenerPartidaActivaPorUsuario(idUsuario);
    }

    @Override
    @Transactional
    public List<String> obtenerCategoriasDelAcertijoDragDrop(Long idAcertijo) {
        Acertijo acertijo = this.repositorioPartida.buscarAcertijoPorId(idAcertijo);

        return acertijo.getDragDropItems()
                .stream()
                .map(DragDropItem::getCategoriaCorrecta)
                .distinct()
                .collect(Collectors.toList());
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
}
