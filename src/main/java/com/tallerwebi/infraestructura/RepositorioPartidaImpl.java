package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.*;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RepositorioPartidaImpl implements RepositorioPartida {

    SessionFactory sessionFactory;

    @Autowired
    public RepositorioPartidaImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void guardarPartida(Partida partida) {
        this.sessionFactory.getCurrentSession().save(partida);
    }

    @Override
    public Etapa obtenerEtapaPorNumero(Integer idSala, Integer numero) {
        String hql = "SELECT e FROM Etapa e WHERE e.sala.id = :idSala AND e.numero = :numero";
        Query<Etapa> query = this.sessionFactory.getCurrentSession().createQuery(hql, Etapa.class);
        query.setParameter("idSala", idSala);
        query.setParameter("numero", numero);
        return query.getSingleResult();
    }

    @Override
    public List<Acertijo> obtenerListaDeAcertijos(Long idEtapa) {
        String hql = "SELECT a FROM Acertijo a WHERE a.etapa.id = :idEtapa";
        Query<Acertijo> query = this.sessionFactory.getCurrentSession().createQuery(hql, Acertijo.class);
        query.setParameter("idEtapa", idEtapa);
        return query.getResultList();
    }

    @Override
    public List<Pista> obtenerListaDePistas(Long idAcertijo) {
        String hql = "SELECT p FROM Pista p WHERE p.acertijo.id = :idAcertijo";
        Query<Pista> query = this.sessionFactory.getCurrentSession().createQuery(hql, Pista.class);
        query.setParameter("idAcertijo", idAcertijo);
        return query.getResultList();
    }

    @Override
    public Respuesta obtenerRespuestaCorrecta(Long idAcertijo) {
        String hql = "SELECT r FROM Respuesta r WHERE r.acertijo.id = :idAcertijo AND r.es_correcta = true";
        Query<Respuesta> query = this.sessionFactory.getCurrentSession().createQuery(hql, Respuesta.class);
        query.setParameter("idAcertijo", idAcertijo);
        return query.getSingleResult();
    }


}
