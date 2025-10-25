package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.entidad.*;
import com.tallerwebi.dominio.enums.Dificultad;
import com.tallerwebi.dominio.enums.TipoAcertijo;
import com.tallerwebi.dominio.interfaz.repositorio.RepositorioPartida;
import com.tallerwebi.infraestructura.config.HibernateTestInfraestructuraConfig;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { HibernateTestInfraestructuraConfig.class })
@Transactional
public class RepositorioPartidaImplTest {

    @Autowired
    SessionFactory sessionFactory;
    RepositorioPartida repositorioPartida;


    @BeforeEach
    public void init() {
        this.repositorioPartida = new RepositorioPartidaImpl(sessionFactory);
    }

    @Test
    public void deberiaGuardarUnaPartidaIniciada(){
        Sala sala = new Sala(1, "La Mansión Misteriosa", Dificultad.PRINCIPIANTE, "Mansion", "Una noche tormentosa te encuentras atrapado en una vieja mansion llena de acertijos.",
                true, 10,"puerta-mansion.png");
        sala.setCantidadDeEtapas(5);
        this.sessionFactory.getCurrentSession().save(sala);

        Partida partida = new Partida(LocalDateTime.now());
        partida.setSala(sala);
        partida.setEsta_activa(true);

        this.repositorioPartida.guardarPartida(partida);

        assertNotNull(partida.getId());
    }

    @Test
    public void deberiaObtenerLaPrimeraEtapaDeLaSalaEnLaPartida(){
        Sala sala = new Sala(1, "La Mansión Misteriosa", Dificultad.PRINCIPIANTE, "Mansion", "Una noche tormentosa te encuentras atrapado en una vieja mansion llena de acertijos.",
                true, 10,"puerta-mansion.png");
        sala.setCantidadDeEtapas(5);
        this.sessionFactory.getCurrentSession().save(sala);

        Etapa etapa = new Etapa("Lobby", 1, "La puerta hacia la siguiente habitación está bloqueada por un candado, busca la clave en este acertijo.", "a.png");
        etapa.setSala(sala);
        this.sessionFactory.getCurrentSession().save(etapa);

        Etapa etapaObtenida = this.repositorioPartida.obtenerEtapaPorNumero(sala.getId(), etapa.getNumero());

        assertThat(etapaObtenida, equalTo(etapa));
    }

    @Test
    public void deberiaObtenerUnaListaDeAcertijosPropiosDeLaEtapa(){
        Sala sala = new Sala(1, "La Mansión Misteriosa", Dificultad.PRINCIPIANTE, "Mansion", "Una noche tormentosa te encuentras atrapado en una vieja mansion llena de acertijos.",
                true, 10,"puerta-mansion.png");
        sala.setCantidadDeEtapas(5);
        this.sessionFactory.getCurrentSession().save(sala);

        Etapa etapa = new Etapa("Lobby", 1, "La puerta hacia la siguiente habitación está bloqueada por un candado, busca la clave en este acertijo.", "a.png");
        etapa.setSala(sala);
        this.sessionFactory.getCurrentSession().save(etapa);

        Acertijo acertijo1 = new Acertijo( "a1");
        acertijo1.setEtapa(etapa);
        acertijo1.setTipo(TipoAcertijo.ADIVINANZA);
        Acertijo acertijo2 = new Acertijo( "a2");
        acertijo2.setEtapa(etapa);
        acertijo2.setTipo(TipoAcertijo.ADIVINANZA);
        Acertijo acertijo3 = new Acertijo( "a3");
        acertijo3.setEtapa(etapa);
        acertijo3.setTipo(TipoAcertijo.ADIVINANZA);

        this.sessionFactory.getCurrentSession().save(acertijo1);
        this.sessionFactory.getCurrentSession().save(acertijo2);
        this.sessionFactory.getCurrentSession().save(acertijo3);

        List<Acertijo> listaObtenida = this.repositorioPartida.obtenerListaDeAcertijos(etapa.getId());

        assertThat(listaObtenida.size(), equalTo(3));
        assertThat(listaObtenida.get(0), equalTo(acertijo1));
        assertThat(listaObtenida.get(1), equalTo(acertijo2));
        assertThat(listaObtenida.get(2), equalTo(acertijo3));
    }

    @Test
    public void deberiaObtenerLaListaDePistasDelAcertijo(){
        Etapa etapa = new Etapa("Lobby", 1, "La puerta hacia la siguiente habitación está bloqueada por un candado, busca la clave en este acertijo.", "a.png");
        this.sessionFactory.getCurrentSession().save(etapa);

        Acertijo acertijo = new Acertijo( "a1");
        acertijo.setEtapa(etapa);
        acertijo.setTipo(TipoAcertijo.ADIVINANZA);
        this.sessionFactory.getCurrentSession().save(acertijo);

        Pista pista1 = new Pista("pista", 1);
        pista1.setAcertijo(acertijo);
        this.sessionFactory.getCurrentSession().save(pista1);
        Pista pista2 = new Pista("pista", 2);
        pista2.setAcertijo(acertijo);
        this.sessionFactory.getCurrentSession().save(pista2);
        Pista pista3 = new Pista("pista", 3);
        pista3.setAcertijo(acertijo);
        this.sessionFactory.getCurrentSession().save(pista3);

        List<Pista> listaObtenida = this.repositorioPartida.obtenerListaDePistas(acertijo.getId());

        assertThat(listaObtenida.size(), equalTo(3));
        assertThat(listaObtenida.get(0), equalTo(pista1));
        assertThat(listaObtenida.get(1), equalTo(pista2));
        assertThat(listaObtenida.get(2), equalTo(pista3));

    }

    @Test
    public void deberiaObtenerLaRespuestaCorrectaDelAcertijo(){
        Etapa etapa = new Etapa("Lobby", 1, "La puerta hacia la siguiente habitación está bloqueada por un candado, busca la clave en este acertijo.", "a.png");
        this.sessionFactory.getCurrentSession().save(etapa);

        Acertijo acertijo = new Acertijo( "a1");
        acertijo.setEtapa(etapa);
        acertijo.setTipo(TipoAcertijo.ADIVINANZA);
        this.sessionFactory.getCurrentSession().save(acertijo);

        Respuesta respuesta = new Respuesta("Respuesta");
        respuesta.setEs_correcta(true);
        respuesta.setAcertijo(acertijo);
        this.sessionFactory.getCurrentSession().save(respuesta);

        Respuesta respuestaCorrecta = this.repositorioPartida.obtenerRespuestaCorrecta(acertijo.getId());

        assertThat(respuestaCorrecta, equalTo(respuesta));
    }

    @Test
    public void deberiaRegistrarUnAcertijoMostrado(){
        Etapa etapa = new Etapa("Lobby", 1, "La puerta hacia la siguiente habitación está bloqueada por un candado, busca la clave en este acertijo.", "a.png");
        this.sessionFactory.getCurrentSession().save(etapa);

        Acertijo acertijo = new Acertijo( "a1");
        acertijo.setEtapa(etapa);
        acertijo.setTipo(TipoAcertijo.ADIVINANZA);
        this.sessionFactory.getCurrentSession().save(acertijo);

        Usuario usuario = new Usuario();
        this.sessionFactory.getCurrentSession().save(usuario);
        AcertijoUsuario acertijoUsuario = new AcertijoUsuario(acertijo, usuario);
        acertijoUsuario.setEtapa(etapa);

        this.repositorioPartida.registrarAcertijoMostrado(acertijoUsuario);

        assertNotNull(acertijoUsuario.getId());
    }

    @Test
    public void deberiaObtener1EnLaCantidadDePistasUsadasCuandoSePideUna(){
        Etapa etapa = new Etapa("Lobby", 1, "La puerta hacia la siguiente habitación está bloqueada por un candado, busca la clave en este acertijo.", "a.png");
        this.sessionFactory.getCurrentSession().save(etapa);

        Acertijo acertijo = new Acertijo( "a1");
        acertijo.setEtapa(etapa);
        acertijo.setTipo(TipoAcertijo.ADIVINANZA);
        this.sessionFactory.getCurrentSession().save(acertijo);

        Usuario usuario = new Usuario();
        this.sessionFactory.getCurrentSession().save(usuario);


        AcertijoUsuario acertijoUsuario = new AcertijoUsuario(acertijo, usuario);
            acertijoUsuario.setEtapa(etapa);
        this.repositorioPartida.registrarAcertijoMostrado(acertijoUsuario);

        this.repositorioPartida.sumarPistaUsada(acertijo.getId(), usuario.getId());

        Integer pistasUsadas = this.repositorioPartida.obtenerPistasUsadas(acertijo.getId(), usuario.getId());

        assertThat(pistasUsadas, equalTo(1));
    }

    @Test
    public void deberiaObtenerUnSoloAcertijoEnLaListaDeAcertijosVistosPorUsuarioPorEtapa(){
        Etapa etapa = new Etapa("Lobby", 1, "La puerta hacia la siguiente habitación está bloqueada por un candado, busca la clave en este acertijo.", "a.png");
        this.sessionFactory.getCurrentSession().save(etapa);

        Usuario usuario = new Usuario();
        this.sessionFactory.getCurrentSession().save(usuario);

        Acertijo acertijo = new Acertijo( "a1");
        acertijo.setEtapa(etapa);
        acertijo.setTipo(TipoAcertijo.ADIVINANZA);
        this.sessionFactory.getCurrentSession().save(acertijo);
        Acertijo acertijo2 = new Acertijo( "a2");
        acertijo2.setEtapa(etapa);
        acertijo2.setTipo(TipoAcertijo.ADIVINANZA);
        this.sessionFactory.getCurrentSession().save(acertijo2);

        AcertijoUsuario acertijoUsuario = new AcertijoUsuario(acertijo, usuario);
        acertijoUsuario.setEtapa(etapa);
        this.repositorioPartida.registrarAcertijoMostrado(acertijoUsuario);


        List<Acertijo> listaObtenidaDeAcertijosVistos = this.repositorioPartida.obtenerAcertijosVistosPorUsuarioPorEtapa(usuario.getId(), etapa.getId());

        assertThat(listaObtenidaDeAcertijosVistos.size(), equalTo(1));
        assertThat(listaObtenidaDeAcertijosVistos.get(0), equalTo(acertijo));


    }

    @Test
    public void deberiaEliminarLosRegistrosDeAcertijosVistos(){
        Etapa etapa = new Etapa("Lobby", 1, "La puerta hacia la siguiente habitación está bloqueada por un candado, busca la clave en este acertijo.", "a.png");
        this.sessionFactory.getCurrentSession().save(etapa);

        Usuario usuario = new Usuario();
        this.sessionFactory.getCurrentSession().save(usuario);

        Acertijo acertijo = new Acertijo( "a1");
        acertijo.setEtapa(etapa);
        acertijo.setTipo(TipoAcertijo.ADIVINANZA);
        this.sessionFactory.getCurrentSession().save(acertijo);
        Acertijo acertijo2 = new Acertijo( "a2");
        acertijo2.setEtapa(etapa);
        acertijo2.setTipo(TipoAcertijo.ADIVINANZA);
        this.sessionFactory.getCurrentSession().save(acertijo2);

        AcertijoUsuario acertijoUsuario = new AcertijoUsuario(acertijo, usuario);
        acertijoUsuario.setEtapa(etapa);
        AcertijoUsuario acertijoUsuario2 = new AcertijoUsuario(acertijo2, usuario);
        acertijoUsuario2.setEtapa(etapa);

        this.repositorioPartida.registrarAcertijoMostrado(acertijoUsuario);
        this.repositorioPartida.registrarAcertijoMostrado(acertijoUsuario2);

        this.repositorioPartida.eliminarRegistrosDeAcertijosVistos(usuario.getId());

        List<Acertijo> listaObtenidaDeAcertijosVistos = this.repositorioPartida.obtenerAcertijosVistosPorUsuarioPorEtapa(usuario.getId(), etapa.getId());

        assertThat(listaObtenidaDeAcertijosVistos.size(), equalTo(0));

    }

    @Test
    public void deberiaObtenerUnAcertijoBuscandoloPorId(){

        Etapa etapa = new Etapa("Lobby", 1, "La puerta hacia la siguiente habitación está bloqueada por un candado, busca la clave en este acertijo.", "a.png");
        this.sessionFactory.getCurrentSession().save(etapa);
        Acertijo acertijo = new Acertijo( "a1");
        acertijo.setEtapa(etapa);
        acertijo.setTipo(TipoAcertijo.ADIVINANZA);
        this.sessionFactory.getCurrentSession().save(acertijo);

        Acertijo acertijoObtenido = this.repositorioPartida.buscarAcertijoPorId(acertijo.getId());

        assertThat(acertijoObtenido, equalTo(acertijo));
    }

    @Test
    public void deberiaObtenerUnaEtapaBuscandolaPorId(){

        Etapa etapa = new Etapa("Lobby", 1, "La puerta hacia la siguiente habitación está bloqueada por un candado, busca la clave en este acertijo.", "a.png");
        this.sessionFactory.getCurrentSession().save(etapa);

        Etapa etapaObtenida = this.repositorioPartida.buscarEtapaPorId(etapa.getId());

        assertThat(etapaObtenida, equalTo(etapa));
    }

    @Test
    public void deberiaObtenerLaPartidaActivaPorUsuario(){

        Sala sala = new Sala(1, "La Mansión Misteriosa", Dificultad.PRINCIPIANTE, "Mansion", "Una noche tormentosa te encuentras atrapado en una vieja mansion llena de acertijos.",
                true, 10,"puerta-mansion.png");
        sala.setCantidadDeEtapas(5);
        this.sessionFactory.getCurrentSession().save(sala);

        Usuario usuario = new Usuario();
        this.sessionFactory.getCurrentSession().save(usuario);

        Partida partida = new Partida(LocalDateTime.now());
        partida.setUsuario(usuario);
        partida.setSala(sala);
        partida.setEsta_activa(true);

        this.repositorioPartida.guardarPartida(partida);

        Partida partidaObtenida = this.repositorioPartida.obtenerPartidaActivaPorUsuario(usuario.getId());

        assertThat(partidaObtenida, equalTo(partida));
    }

    @Test
    public void deberiaFinalizarUnaPartida(){
        Sala sala = new Sala(1, "La Mansión Misteriosa", Dificultad.PRINCIPIANTE, "Mansion", "Una noche tormentosa te encuentras atrapado en una vieja mansion llena de acertijos.",
                true, 10,"puerta-mansion.png");
        sala.setCantidadDeEtapas(5);
        this.sessionFactory.getCurrentSession().save(sala);

        Usuario usuario = new Usuario();
        this.sessionFactory.getCurrentSession().save(usuario);

        Partida partida = new Partida(LocalDateTime.now());
        partida.setUsuario(usuario);
        partida.setSala(sala);
        partida.setEsta_activa(true);
        this.repositorioPartida.guardarPartida(partida);

        partida.setFin(LocalDateTime.now());
        partida.setTiempoTotal(50L);

        this.repositorioPartida.finalizarPartida(partida);

        assertNotNull(partida.getFin());
        assertNotNull(partida.getTiempoTotal());
    }

}
