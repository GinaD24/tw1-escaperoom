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
    public List<Partida> obtenerTodasLasPartidasGanadas() {
        String hql = "FROM Partida p WHERE ganada = true";
        Query<Partida> query = this.sessionFactory.getCurrentSession().createQuery(hql, Partida.class);
        return query.getResultList();
    }

    @Override
    public Integer obtenerCantidadDeBonusPorSala(Integer idSala) {
        String hql = "SELECT COUNT(e) FROM Etapa e WHERE e.sala.id = :idSala AND e.tieneBonus = true";
        Query<Long> query = sessionFactory.getCurrentSession().createQuery(hql, Long.class);
        query.setParameter("idSala", idSala);
        return query.uniqueResult().intValue();
    }
}
