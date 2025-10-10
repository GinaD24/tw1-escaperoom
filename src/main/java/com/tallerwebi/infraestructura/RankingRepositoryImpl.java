package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Ranking;
import com.tallerwebi.dominio.RankingRepository;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

@Repository

public class RankingRepositoryImpl implements RankingRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public RankingRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void guardar(Ranking ranking) {
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(ranking);
    }

    @Override
    public Ranking buscarPorIdDeSalaYNombreDeUsuario(Integer idSala, String nombreUsuario) {
        return (Ranking) sessionFactory.getCurrentSession()
                .createCriteria(Ranking.class)
                .add(Restrictions.eq("idSala", idSala))
                .add(Restrictions.eq("nombreUsuario", nombreUsuario))
                .uniqueResult();

    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Ranking> obtenerRankingPorSala(Integer idSala) {
        return (List<Ranking>) sessionFactory.getCurrentSession()
                .createCriteria(Ranking.class)
                .add(Restrictions.eq("idSala", idSala))
                .list();
    }

}
