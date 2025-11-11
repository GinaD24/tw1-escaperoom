package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.entidad.*;
import com.tallerwebi.dominio.enums.Dificultad;
import com.tallerwebi.dominio.interfaz.repositorio.RepositorioRanking;
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

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { HibernateTestInfraestructuraConfig.class })
@Transactional
public class RepositorioRankingImplTest {

    @Autowired
    SessionFactory sessionFactory;
    RepositorioRanking repositorioRanking;


    @BeforeEach
    public void init() {
        this.repositorioRanking = new RepositorioRankingImpl(sessionFactory);
    }

    @Test
    public void deberiaObtenerUnaListaDeTodasLasPartidasGanadas(){
        Sala sala = new Sala(1, "La Mansión Misteriosa", Dificultad.PRINCIPIANTE, "Mansion", "Una noche tormentosa te encuentras atrapado en una vieja mansion llena de acertijos.",
                true, 10,"puerta-mansion.png");
        sala.setCantidadDeEtapas(5);
        sala.setEs_paga(false);
        this.sessionFactory.getCurrentSession().save(sala);

        Usuario usuario = new Usuario();
        this.sessionFactory.getCurrentSession().save(usuario);
        Usuario usuario2 = new Usuario();
        this.sessionFactory.getCurrentSession().save(usuario2);


        Partida partida = dadoQueExisteUnaPartidaGanada(1L, sala, 500, usuario, 0, 60L);
        this.sessionFactory.getCurrentSession().save(partida);

        Partida partida2 = dadoQueExisteUnaPartidaGanada(2L, sala, 450, usuario, 0, 120L);
        this.sessionFactory.getCurrentSession().save(partida2);

        List<Partida> listaDePartidasObtenidas = this.repositorioRanking.obtenerTodasLasPartidasGanadas();

        assertThat(listaDePartidasObtenidas.size(), equalTo(2));
        assertThat(listaDePartidasObtenidas.get(0), equalTo(partida));
        assertThat(listaDePartidasObtenidas.get(1), equalTo(partida2));
    }



    public Partida dadoQueExisteUnaPartidaGanada(Long id, Sala sala, Integer puntaje,  Usuario usuario, Integer pistasUsadas, Long tiempoTotal) {
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
