package com.tallerwebi.dominio;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.hamcrest.MatcherAssert.assertThat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.tallerwebi.dominio.entidad.Partida;
import com.tallerwebi.dominio.entidad.PuestoRankingDTO;
import com.tallerwebi.dominio.entidad.Sala;
import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.enums.Dificultad;
import com.tallerwebi.dominio.interfaz.repositorio.RepositorioRanking;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ServicioRankingTest {

    private ServicioRankingImpl servicioRanking;
    private RepositorioRanking repositorioRanking;

    @BeforeEach
    public void init() {
        this.repositorioRanking = mock(RepositorioRanking.class);
        this.servicioRanking = new ServicioRankingImpl(repositorioRanking);
    }

    @Test
    public void deberiaDevolverElPuntajeCalculadoDeUnaPartida_TeniendoEnCuentaLaDificultadYDuracionDeLaSala_YElPedidoDePistas(){
        Sala sala = new Sala();
        sala.setId(1);
        sala.setCantidadDeEtapas(5);
        sala.setDificultad(Dificultad.PRINCIPIANTE);
        sala.setDuracion(5);

        Usuario usuario = new Usuario();
        usuario.setId(1L);
        Usuario usuario2 = new Usuario();
        usuario.setId(2L);


        Partida partida = dadoQueExisteUnaPartida(1L, sala, 500, usuario, 0, 30L);

        //mismo tiempo pero con una pista pedida
        Partida partida2 = dadoQueExisteUnaPartida(2L, sala, 475, usuario2, 1, 30L);


        Double puntajeCalculadoPartida1 = this.servicioRanking.obtenerPuntajeCalculado(partida);
        Double puntajeCalculadoPartida2 = this.servicioRanking.obtenerPuntajeCalculado(partida2);

        assertThat(puntajeCalculadoPartida1, greaterThan(puntajeCalculadoPartida2));
    }

    @Test
    public void deberiaDevolverMenosPuntajeCalculadoSItardasMasTiempo_TeniendoEnCuentaLaDificultadYDuracionDeLaSala_YElPedidoDePistas(){
        Sala sala = new Sala();
        sala.setId(1);
        sala.setCantidadDeEtapas(5);
        sala.setDificultad(Dificultad.PRINCIPIANTE);
        sala.setDuracion(5);

        Usuario usuario = new Usuario();
        usuario.setId(1L);
        Usuario usuario2 = new Usuario();
        usuario.setId(2L);


        Partida partida = dadoQueExisteUnaPartida(1L, sala, 500, usuario, 0, 30L);

        //mismo puntaje, mismas pistas pero mas tiempo
        Partida partida2 = dadoQueExisteUnaPartida(2L, sala, 500, usuario2, 0, 40L);


        Double puntajeCalculadoPartida1 = this.servicioRanking.obtenerPuntajeCalculado(partida);
        Double puntajeCalculadoPartida2 = this.servicioRanking.obtenerPuntajeCalculado(partida2);

        assertThat(puntajeCalculadoPartida1, greaterThan(puntajeCalculadoPartida2));
    }

    @Test
    public void deberiaDevolverMasPuntajeAUnaPartidaConSalaAvanzada_AunqueHayaSolicitadoPistaYTardadoMas(){
        Sala salaPrincipiante = new Sala();
        salaPrincipiante.setId(1);
        salaPrincipiante.setCantidadDeEtapas(5);
        salaPrincipiante.setDificultad(Dificultad.PRINCIPIANTE);
        salaPrincipiante.setDuracion(5);

        Sala salaAvanzada = new Sala();
        salaAvanzada.setId(2);
        salaAvanzada.setCantidadDeEtapas(10);
        salaAvanzada.setDificultad(Dificultad.AVANZADO);
        salaAvanzada.setDuracion(12);

        Usuario usuario = new Usuario();
        usuario.setId(1L);
        Usuario usuario2 = new Usuario();
        usuario.setId(2L);


        Partida partida = dadoQueExisteUnaPartida(1L, salaPrincipiante, 500, usuario, 0, 40L);

        Partida partida2 = dadoQueExisteUnaPartida(2L, salaAvanzada, 1000, usuario2, 1, 60L);


        Double puntajeCalculadoPartida1 = this.servicioRanking.obtenerPuntajeCalculado(partida);
        Double puntajeCalculadoPartida2 = this.servicioRanking.obtenerPuntajeCalculado(partida2);

        assertThat(puntajeCalculadoPartida2, greaterThan(puntajeCalculadoPartida1));
    }

    @Test
    public void deberiaDevolverUnaListaDePuestoRankingSiendoElPrimerPuesto_ElQueTerminoConMayorPuntajeCalculado(){
        Sala sala = new Sala();
        sala.setId(1);
        sala.setCantidadDeEtapas(5);
        sala.setDificultad(Dificultad.PRINCIPIANTE);
        sala.setDuracion(5);

        Usuario usuario = new Usuario();
        usuario.setId(1L);
        Usuario usuario2 = new Usuario();
        usuario.setId(2L);

        List<Partida> listaDePartidasGanadas  = new ArrayList<>();

        Partida partida = dadoQueExisteUnaPartida(1L, sala, 500, usuario, 0, 30L);
        Partida partida2 = dadoQueExisteUnaPartida(2L, sala, 300, usuario2, 0, 50L);

        listaDePartidasGanadas.add(partida);
        listaDePartidasGanadas.add(partida2);

        List<PuestoRankingDTO> listaDePuestoRankingDTO = new ArrayList<>();
        listaDePuestoRankingDTO.add(new PuestoRankingDTO(sala, partida.getPuntaje(), usuario, partida.getTiempoTotal(), 0 , null));
        listaDePuestoRankingDTO.add(new PuestoRankingDTO(sala, partida2.getPuntaje(), usuario2, partida2.getTiempoTotal(), 0, null));

        when(repositorioRanking.obtenerTodasLasPartidasGanadas()).thenReturn(listaDePartidasGanadas);

        List<PuestoRankingDTO> puestosRankingObtenidos = this.servicioRanking.obtenerRanking();

        assertThat(puestosRankingObtenidos.get(0).getUsuario(), equalTo(usuario));
        assertThat(puestosRankingObtenidos.get(0).getPuntaje(), equalTo(partida.getPuntaje()));
        assertThat(puestosRankingObtenidos.get(1).getUsuario(), equalTo(usuario2));
        assertThat(puestosRankingObtenidos.get(1).getPuntaje(), equalTo(partida2.getPuntaje()));
        verify(repositorioRanking).obtenerTodasLasPartidasGanadas();
    }

    public Partida dadoQueExisteUnaPartida(Long id, Sala sala, Integer puntaje,  Usuario usuario, Integer pistasUsadas, Long tiempoTotal) {
        Partida partida = new Partida();
        partida.setId(id);
        partida.setInicio(LocalDateTime.now());
        partida.setSala(sala);
        partida.setPuntaje(puntaje);
        partida.setEsta_activa(false);
        partida.setUsuario(usuario);
        partida.setPistasUsadas(pistasUsadas);
        partida.setGanada(true);
        partida.setTiempoTotal(tiempoTotal);

        return  partida;
    }

}
