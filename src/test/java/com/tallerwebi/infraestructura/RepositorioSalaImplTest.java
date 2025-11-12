package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.interfaz.repositorio.RepositorioSala;
import com.tallerwebi.dominio.entidad.Sala;
import com.tallerwebi.dominio.enums.Dificultad;
import com.tallerwebi.infraestructura.config.HibernateTestInfraestructuraConfig;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;
import java.util.List;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { HibernateTestInfraestructuraConfig.class })
public class RepositorioSalaImplTest {

    @Autowired
    private SessionFactory sessionFactory;
    private RepositorioSala repositorioSala;

    @BeforeEach
    public void init() {
        repositorioSala = new RepositorioSalaImpl(this.sessionFactory);
    }

    @Test
    @Transactional
    public void deberiaDevolverTodasLasSalas() {

        Sala sala1 = new Sala(null, "La Mansión Misteriosa", Dificultad.PRINCIPIANTE, "Mansión",
                "Una noche tormentosa te encuentras atrapado en una vieja mansión llena de acertijos.", true, 10, null);
        Sala sala2 =new Sala(null, "El Laboratorio Secreto", Dificultad.INTERMEDIO, "Laboratorio",
                "Un científico desaparecido dejó pistas en su laboratorio. ¿Podrás descubrir qué tramaba?", true, 15, null);
        Sala sala3 =new Sala(null, "La Cárcel Abandonada", Dificultad.AVANZADO, "Prisión",
                "Despiertas en una celda oxidada. Solo resolviendo complejos acertijos podrás escapar.", false, 20, null);

        sala1.setCantidadDeEtapas(5);
        sala2.setCantidadDeEtapas(8);
        sala3.setCantidadDeEtapas(10);
        sala1.setEs_paga(false);
        sala2.setEs_paga(false);
        sala3.setEs_paga(false);

        this.sessionFactory.getCurrentSession().save(sala1);
        this.sessionFactory.getCurrentSession().save(sala2);
        this.sessionFactory.getCurrentSession().save(sala3);

        List<Sala> salasObtenidas = this.repositorioSala.obtenerSalas();

        assertThat(salasObtenidas.size(), equalTo(3));
        assertThat(salasObtenidas.get(0), equalTo(sala1));
        assertThat(salasObtenidas.get(1), equalTo(sala2));
        assertThat(salasObtenidas.get(2), equalTo(sala3));
    }

    @Test
    @Transactional
    public void deberiaDevolverLaSalaSolicitadaPorID() {

        Sala sala1 = new Sala(null, "La Mansión Misteriosa", Dificultad.PRINCIPIANTE, "Mansión",
                "Una noche tormentosa te encuentras atrapado en una vieja mansión llena de acertijos.", true, 10, null);
        Sala sala2 =new Sala(null, "El Laboratorio Secreto", Dificultad.INTERMEDIO, "Laboratorio",
                "Un científico desaparecido dejó pistas en su laboratorio. ¿Podrás descubrir qué tramaba?", true, 15, null);
        Sala sala3 =new Sala(null, "La Cárcel Abandonada", Dificultad.AVANZADO, "Prisión",
                "Despiertas en una celda oxidada. Solo resolviendo complejos acertijos podrás escapar.", false, 20, null);
        sala1.setCantidadDeEtapas(5);
        sala2.setCantidadDeEtapas(8);
        sala3.setCantidadDeEtapas(10);
        sala1.setEs_paga(false);
        sala2.setEs_paga(false);
        sala3.setEs_paga(false);
        this.sessionFactory.getCurrentSession().save(sala1);
        this.sessionFactory.getCurrentSession().save(sala2);
        this.sessionFactory.getCurrentSession().save(sala3);

        Sala salaObtenida = this.repositorioSala.obtenerSalaPorId(sala1.getId());

        assertThat(salaObtenida, equalTo(sala1));
    }

    @Test
    @Transactional
    public void deberiaDevolverUnaListaDeSalasSolicitadasPorDificultad() {

        Sala sala1 = new Sala(null, "La Mansión Misteriosa", Dificultad.PRINCIPIANTE, "Mansión",
                "Una noche tormentosa te encuentras atrapado en una vieja mansión llena de acertijos.", true, 10, null);
        Sala sala2 =new Sala(null, "El Laboratorio Secreto", Dificultad.INTERMEDIO, "Laboratorio",
                "Un científico desaparecido dejó pistas en su laboratorio. ¿Podrás descubrir qué tramaba?", true, 15, null);
        Sala sala3 =new Sala(null, "La Cárcel Abandonada", Dificultad.AVANZADO, "Prisión",
                "Despiertas en una celda oxidada. Solo resolviendo complejos acertijos podrás escapar.", false, 20, null);

        Sala sala4 = new Sala(null, "La casa Misteriosa", Dificultad.PRINCIPIANTE, "Casa",
                "Una noche tormentosa te encuentras atrapado en una vieja mansión llena de acertijos.", true, 10, null);
        sala1.setCantidadDeEtapas(5);
        sala2.setCantidadDeEtapas(8);
        sala3.setCantidadDeEtapas(10);
        sala4.setCantidadDeEtapas(5);
        sala1.setEs_paga(false);
        sala2.setEs_paga(false);
        sala3.setEs_paga(false);
        sala4.setEs_paga(false);
        this.sessionFactory.getCurrentSession().save(sala1);
        this.sessionFactory.getCurrentSession().save(sala2);
        this.sessionFactory.getCurrentSession().save(sala3);
        this.sessionFactory.getCurrentSession().save(sala4);


        List<Sala> salasObtenida = this.repositorioSala.obtenerSalasPorDificultad(Dificultad.PRINCIPIANTE);

        assertThat(salasObtenida.size(), equalTo(2));
        assertThat(salasObtenida.get(0), equalTo(sala1));
        assertThat(salasObtenida.get(1), equalTo(sala4));
    }
}
