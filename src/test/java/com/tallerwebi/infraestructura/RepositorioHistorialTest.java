package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.entidad.Partida;
import com.tallerwebi.dominio.entidad.Sala;
import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.enums.Dificultad;
import com.tallerwebi.dominio.interfaz.repositorio.RepositorioHistorial;
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
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.verify;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { HibernateTestInfraestructuraConfig.class })
@Transactional
public class RepositorioHistorialTest {

    @Autowired
    SessionFactory sessionFactory;
    RepositorioHistorial repositorioHistorial;

    @BeforeEach
    public void init() {
        this.repositorioHistorial = new RepositorioHistorialImpl(sessionFactory);
    }

    @Test
    public void deberiaTraerLasPartidasJugadasPorUsuario(){
        Sala sala = new Sala(1, "La Mansión Misteriosa", Dificultad.PRINCIPIANTE, "Mansion", "Una noche tormentosa te encuentras atrapado en una vieja mansion llena de acertijos.",
                true, 10,"puerta-mansion.png");
        sala.setCantidadDeEtapas(5);
        sala.setEs_paga(false);
        this.sessionFactory.getCurrentSession().save(sala);

        Usuario usuario = new Usuario();
        usuario.setId(1L);
        this.sessionFactory.getCurrentSession().save(usuario);

        Partida partida1 = new Partida();
        partida1.setUsuario(usuario);
        partida1.setEsta_activa(false);
        partida1.setInicio(LocalDateTime.now());
        partida1.setPuntaje(100);
        partida1.setSala(sala);

        Partida partida2 = new Partida();
        partida2.setUsuario(usuario);
        partida2.setEsta_activa(false);
        partida2.setInicio(LocalDateTime.now());
        partida2.setPuntaje(100);
        partida2.setSala(sala);

        this.sessionFactory.getCurrentSession().save(partida1);
        this.sessionFactory.getCurrentSession().save(partida2);

        List<Partida> historial = new ArrayList<>();
        historial.add(partida1);
        historial.add(partida2);

        List<Partida> partidasObtenidas = repositorioHistorial.obtenerPartidasPorJugador(usuario.getId());

        assertThat(partidasObtenidas.size(), equalTo(2));
        assertThat(partidasObtenidas.get(0), equalTo(partida1));
        assertThat(partidasObtenidas.get(1), equalTo(partida2));
    }
}
