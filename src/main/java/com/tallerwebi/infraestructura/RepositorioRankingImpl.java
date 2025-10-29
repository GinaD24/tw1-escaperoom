package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.entidad.Partida;
import com.tallerwebi.dominio.interfaz.repositorio.RepositorioRanking;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RepositorioRankingImpl implements RepositorioRanking {

    SessionFactory sessionFactory;
    @Autowired
    public RepositorioRankingImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    @Override
    public List<Partida> obtenerPartidasPorSala(Integer idSala) {
            String hql = "SELECT p FROM Partida p WHERE p.sala.id = :idSala AND p.ganada = true";
            Query<Partida> query = this.sessionFactory.getCurrentSession().createQuery(hql, Partida.class);
            query.setParameter("idSala", idSala);
            return query.getResultList();
        }

}
