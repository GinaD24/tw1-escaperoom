//package com.tallerwebi.infraestructura;
//
//import com.tallerwebi.dominio.entidad.*;
//import com.tallerwebi.dominio.enums.Dificultad;
//import com.tallerwebi.dominio.enums.TipoAcertijo;
//import com.tallerwebi.dominio.interfaz.repositorio.RepositorioPartida;
//import com.tallerwebi.dominio.interfaz.repositorio.RepositorioRanking;
//import com.tallerwebi.infraestructura.config.HibernateTestInfraestructuraConfig;
//import org.hibernate.SessionFactory;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//
//import javax.transaction.Transactional;
//import java.time.LocalDateTime;
//import java.util.List;
//
//import static org.hamcrest.MatcherAssert.assertThat;
//import static org.hamcrest.Matchers.equalTo;
//
//@ExtendWith(SpringExtension.class)
//@ContextConfiguration(classes = { HibernateTestInfraestructuraConfig.class })
//@Transactional
//public class RepositorioRankingImplTest {
//
//    @Autowired
//    SessionFactory sessionFactory;
//    RepositorioRanking repositorioRanking;
//
//
//    @BeforeEach
//    public void init() {
//        this.repositorioRanking = new RepositorioRankingImpl(sessionFactory);
//    }
//
//    @Test
//    public void deberiaObtenerUnaListaDePartidasDeUnaSala(){
//        Sala sala = new Sala(1, "La Mansión Misteriosa", Dificultad.PRINCIPIANTE, "Mansion", "Una noche tormentosa te encuentras atrapado en una vieja mansion llena de acertijos.",
//                true, 10,"puerta-mansion.png");
//        sala.setCantidadDeEtapas(5);
//        sala.setEs_paga(false);
//        this.sessionFactory.getCurrentSession().save(sala);
//
//        Usuario usuario = new Usuario();
//        this.sessionFactory.getCurrentSession().save(usuario);
//        Usuario usuario2 = new Usuario();
//        this.sessionFactory.getCurrentSession().save(usuario2);
//
//
//        Partida partida = new Partida();
//        partida.setId(1L);
//        partida.setInicio(LocalDateTime.now());
//        partida.setFin(LocalDateTime.now().plusMinutes(1)); //termino al minuto
//        partida.setSala(sala);
//        partida.setPuntaje(500);
//        partida.setEsta_activa(false);
//        partida.setUsuario(usuario);
//        partida.setPistasUsadas(0);
//        partida.setGanada(true);
//        partida.setTiempoTotal(60L);
//
//        this.sessionFactory.getCurrentSession().save(partida);
//
//
//        Partida partida2 = new Partida();
//        partida2.setId(2L);
//        partida2.setInicio(LocalDateTime.now());
//        partida2.setFin(LocalDateTime.now().plusMinutes(2)); //2 mins
//        partida2.setSala(sala);
//        partida2.setPuntaje(450);
//        partida2.setEsta_activa(false);
//        partida2.setUsuario(usuario2);
//        partida2.setPistasUsadas(0);
//        partida2.setGanada(true);
//        partida2.setTiempoTotal(120L);
//
//        this.sessionFactory.getCurrentSession().save(partida2);
//
//
//        List<Partida> listaDePartidasObtenidas = this.repositorioRanking.obtenerPartidasPorSala(sala.getId());
//
//        assertThat(listaDePartidasObtenidas.size(), equalTo(2));
//        assertThat(listaDePartidasObtenidas.get(0), equalTo(partida));
//        assertThat(listaDePartidasObtenidas.get(1), equalTo(partida2));
//
//    }
//
//
//
//
//}
