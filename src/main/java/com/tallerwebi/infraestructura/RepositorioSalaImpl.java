package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.RepositorioSala;
import com.tallerwebi.dominio.Sala;
import com.tallerwebi.dominio.enums.Dificultad;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.hibernate.query.Query;

import java.util.List;

@Repository
public class RepositorioSalaImpl implements RepositorioSala {


    private final SessionFactory sessionFactory;

    @Autowired
    public RepositorioSalaImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Sala> obtenerSalas() {
        String hql = "FROM Sala";
        Query<Sala> query = this.sessionFactory.getCurrentSession().createQuery(hql, Sala.class);
        return query.getResultList();
    }

    @Override
    public Sala obtenerSalaPorId(Integer id) {
        String hql = "SELECT s FROM Sala s WHERE s.id = :id";
        Query<Sala> query = this.sessionFactory.getCurrentSession().createQuery(hql, Sala.class);
        query.setParameter("id", id);
        return query.getSingleResult();
    }

    @Override
    public List<Sala> obtenerSalasPorDificultad(Dificultad dificultad) {
        String hql = "SELECT s FROM Sala s WHERE s.dificultad = :dificultad";
        Query<Sala> query = this.sessionFactory.getCurrentSession().createQuery(hql, Sala.class);
        query.setParameter("dificultad", dificultad);
        return query.getResultList();
    }

}
