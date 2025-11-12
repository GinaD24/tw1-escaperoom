package com.tallerwebi.dominio;

import com.tallerwebi.dominio.entidad.*;
import com.tallerwebi.dominio.enums.TipoAcertijo;
import com.tallerwebi.dominio.excepcion.EtapaInexistente;
import com.tallerwebi.dominio.excepcion.SesionDeUsuarioExpirada;
import com.tallerwebi.dominio.excepcion.UsuarioInexistente;
import com.tallerwebi.dominio.interfaz.repositorio.RepositorioPartida;
import com.tallerwebi.dominio.interfaz.repositorio.RepositorioSala;
import com.tallerwebi.dominio.interfaz.repositorio.RepositorioUsuario;
import com.tallerwebi.dominio.interfaz.servicio.ServicioGeneradorIA;
import com.tallerwebi.dominio.interfaz.servicio.ServicioPartida;
import com.tallerwebi.dominio.interfaz.servicio.ServicioSala;
import com.tallerwebi.presentacion.AcertijoActualDTO;
import org.eclipse.sisu.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service

public class ServicioPartidaImpl implements ServicioPartida {


    private RepositorioPartida repositorioPartida;
    private RepositorioUsuario repositorioUsuario;
    private RepositorioSala repositorioSala;

    @Autowired
    public ServicioPartidaImpl(RepositorioPartida repositorioPartida,
                               RepositorioUsuario repositorioUsuario, RepositorioSala repositorioSala) {
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
    public void registrarUsoDePista(Long idUsuario) {
        Partida partida = this.repositorioPartida.obtenerPartidaActivaPorUsuario(idUsuario);

        if (partida != null) {
            partida.setPistasUsadas(partida.getPistasUsadas() + 1);
            partida.setPuntaje(partida.getPuntaje() - 25);
            this.repositorioPartida.registrarPistaEnPartida(idUsuario);
        }

    }

    @Transactional
    @Override
    public Boolean validarRespuesta(AcertijoActualDTO acertijoActual, String respuestaUsuario, Long idUsuario, @Nullable String ordenSecuenciaCorrecto) {
        TipoAcertijo tipo = acertijoActual.getTipo();
        Long idAcertijo = acertijoActual.getId();
        Partida partida = this.repositorioPartida.obtenerPartidaActivaPorUsuario(idUsuario);
        boolean esCorrecta = false;

        switch(tipo){
            case ADIVINANZA:
                String respuestaCorrecta = acertijoActual.getRespuestaCorrecta();

                String[] palabrasIngresadas = respuestaUsuario.toLowerCase().split("\\s+");
                if(Arrays.asList(palabrasIngresadas).contains(respuestaCorrecta.toLowerCase())){
                    esCorrecta = true;
                    partida.setPuntaje(partida.getPuntaje() + 100);
                }
                break;

            case ORDENAR_IMAGEN:
                if (idAcertijo == null) {
                    return false;
                }

                List<Long> ordenSeleccionado = Arrays.stream(respuestaUsuario.split(","))
                        .map(Long::valueOf)
                        .collect(Collectors.toList());

                List<Long> ordenCorrecto = this.repositorioPartida.obtenerOrdenDeImgCorrecto(idAcertijo);

                if (ordenSeleccionado.equals(ordenCorrecto)) {
                    esCorrecta = true;
                    partida.setPuntaje(partida.getPuntaje() + 100);
                }
                break;
            case SECUENCIA:
                if (ordenSecuenciaCorrecto == null) {
                    throw new IllegalArgumentException("Se requiere el orden correcto para acertijo SECUENCIA");
                }

                List<Long> ordenSeleccionadoSecuencia = Arrays.stream(respuestaUsuario.split(","))
                        .map(Long::valueOf)
                        .collect(Collectors.toList());

                List<Long> ordenCorrectoList = Arrays.stream(ordenSecuenciaCorrecto.split(","))
                        .map(Long::valueOf)
                        .collect(Collectors.toList());

                if (ordenSeleccionadoSecuencia.equals(ordenCorrectoList)) {
                    esCorrecta = true;
                    partida.setPuntaje(partida.getPuntaje() + 100);
                }
                break;

            case DRAG_DROP:
                if (idAcertijo == null) {
                    return false;
                }

                Map<Long, String> respuestaDragDrop = Arrays.stream(respuestaUsuario.split(","))
                        .map(pair -> pair.split(":"))
                        .filter(arr -> arr.length == 2)
                        .collect(Collectors.toMap(
                                arr -> Long.valueOf(arr[0]),
                                arr -> arr[1]
                        ));

                List<DragDropItem> itemsCorrectos = this.repositorioPartida.obtenerItemsDragDrop(idAcertijo);

                boolean todoCorrecto = itemsCorrectos.stream()
                        .allMatch(item ->
                                respuestaDragDrop.containsKey(item.getId()) &&
                                        respuestaDragDrop.get(item.getId()).equals(item.getCategoriaCorrecta())
                        );
                if (todoCorrecto) {
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
    public Partida buscarPartidaPorId(Long idPartida) {
        return this.repositorioPartida.buscarPartidaPorId(idPartida);
    }

    @Override
    public List<ImagenAcertijo> obtenerSecuenciaAleatoria(Acertijo acertijo) {
        List<ImagenAcertijo> imagenesAcertijo = new ArrayList<>(acertijo.getImagenes());
        Collections.shuffle(imagenesAcertijo); //lo hace de forma aleatoria
        return imagenesAcertijo;
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
            } while (acertijosVistos != null && acertijosVistos.contains(acertijoSeleccionado)); //trae los acertijos random


            Usuario usuario = repositorioUsuario.obtenerUsuarioPorId(id_usuario);
            AcertijoUsuario acertijoUsuario = new AcertijoUsuario(acertijoSeleccionado, usuario);
            Etapa etapa = this.repositorioPartida.buscarEtapaPorId(idEtapa);
            acertijoUsuario.setEtapa(etapa);
            this.repositorioPartida.registrarAcertijoMostrado(acertijoUsuario);
        }

        return acertijoSeleccionado;
    }

    @Transactional
    public boolean tiempoExpirado(Partida partida) {
        Integer duracionMinutos = partida.getSala().getDuracion();
        LocalDateTime finEsperado = partida.getInicio().plusMinutes(duracionMinutos);
        return LocalDateTime.now().isAfter(finEsperado);
    }

}
