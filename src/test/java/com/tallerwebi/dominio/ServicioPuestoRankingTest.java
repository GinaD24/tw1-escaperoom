package com.tallerwebi.dominio;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.hamcrest.MatcherAssert.assertThat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.tallerwebi.dominio.entidad.Partida;
import com.tallerwebi.dominio.entidad.PuestoRanking;
import com.tallerwebi.dominio.entidad.Sala;
import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.interfaz.repositorio.RepositorioRanking;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ServicioPuestoRankingTest {
    
    private ServicioRankingImpl servicioRanking;
    private RepositorioRanking repositorioRanking;

    @BeforeEach
    public void init() {
        this.repositorioRanking = mock(RepositorioRanking.class);
        this.servicioRanking = new ServicioRankingImpl(repositorioRanking);
    }


    @Test
    public void deberiaDevolverUnaListaDePuestoRanking(){
        Sala sala = new Sala();
        sala.setId(1);

        Usuario usuario = new Usuario();
        usuario.setId(1L);
        Usuario usuario2 = new Usuario();
        usuario.setId(2L);

        List<Partida> listaDePartidas  = new ArrayList<>();

        Partida partida = new Partida();
        partida.setId(1L);
        partida.setInicio(LocalDateTime.now());
        partida.setFin(LocalDateTime.now().plusMinutes(1)); //termino al minuto
        partida.setSala(sala);
        partida.setPuntaje(500);
        partida.setEsta_activa(false);
        partida.setUsuario(usuario);
        partida.setPistasUsadas(0);
        partida.setGanada(true);
        partida.setTiempoTotal(60L);


        Partida partida2 = new Partida();
        partida2.setId(2L);
        partida2.setInicio(LocalDateTime.now());
        partida2.setFin(LocalDateTime.now().plusMinutes(2)); //2 mins
        partida2.setSala(sala);
        partida2.setPuntaje(450);
        partida2.setEsta_activa(false);
        partida2.setUsuario(usuario2);
        partida2.setPistasUsadas(0);
        partida2.setGanada(true);
        partida2.setTiempoTotal(120L);

        listaDePartidas.add(partida);
        listaDePartidas.add(partida2);

        List<PuestoRanking> listaDePuestoRanking = new ArrayList<>();
        listaDePuestoRanking.add(new PuestoRanking(sala.getId(), partida.getPuntaje(), usuario, partida.getTiempoTotal(), 0 ));
        listaDePuestoRanking.add(new PuestoRanking(sala.getId(), partida2.getPuntaje(), usuario2, partida2.getTiempoTotal(), 0));

        when(repositorioRanking.obtenerPartidasPorSala(sala.getId())).thenReturn(listaDePartidas);

        List<PuestoRanking> puestosObtenidos = this.servicioRanking.obtenerRankingPorSala(sala.getId());

        assertThat(puestosObtenidos.get(0).getUsuario(), equalTo(usuario));
        assertThat(puestosObtenidos.get(0).getPuntaje(), equalTo(partida.getPuntaje()));
        assertThat(puestosObtenidos.get(1).getUsuario(), equalTo(usuario2));
        assertThat(puestosObtenidos.get(1).getPuntaje(), equalTo(partida2.getPuntaje()));
        verify(repositorioRanking).obtenerPartidasPorSala(sala.getId());
    }

}
