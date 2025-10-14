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
        return query.getResultList().stream().findFirst().orElse(null);
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

    @Override
    public void registrarAcertijoMostrado(AcertijoUsuario acertijoUsuario) {
        this.sessionFactory.getCurrentSession().save(acertijoUsuario);
    }

    @Override
    public Integer obtenerPistasUsadas(Long idAcertijo, Long  id_usuario) {
        String hql = "SELECT au.pistasUsadas FROM AcertijoUsuario au WHERE au.acertijo.id = :idAcertijo AND au.usuario.id = :id_usuario";
        Query<Integer> query = this.sessionFactory.getCurrentSession().createQuery(hql, Integer.class);
        query.setParameter("idAcertijo", idAcertijo);
        query.setParameter("id_usuario", id_usuario);
        return query.getSingleResult();
    }

    @Override
    public void sumarPistaUsada(Long idAcertijo, Long idUsuario) {
        String hql = "UPDATE AcertijoUsuario au " +
                "SET au.pistasUsadas = au.pistasUsadas + 1 " +
                "WHERE au.acertijo.id = :idAcertijo AND au.usuario.id = :idUsuario";

         int updated = sessionFactory.getCurrentSession()
                .createQuery(hql)
                .setParameter("idAcertijo", idAcertijo)
                .setParameter("idUsuario", idUsuario)
                .executeUpdate();
    }

    @Override
    public List<Acertijo> obtenerAcertijosVistosPorUsuarioPorEtapa(Long idUsuario, Long idEtapa) {
        String hql = "SELECT au.acertijo FROM AcertijoUsuario au WHERE au.usuario.id = :idUsuario AND au.etapa.id = :idEtapa";
        Query<Acertijo> query = this.sessionFactory.getCurrentSession().createQuery(hql, Acertijo.class);
        query.setParameter("idUsuario", idUsuario);
        query.setParameter("idEtapa", idEtapa);
        return query.getResultList();
    }


    @Override
    public void eliminarRegistrosDeAcertijosVistos(Long idUsuario) {
        String hql = "DELETE FROM AcertijoUsuario au WHERE au.usuario.id = :idUsuario";
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("idUsuario", idUsuario);
        query.executeUpdate();
    }

    @Override
    public Acertijo buscarAcertijoPorId(Long idAcertijo) {
        String hql = "SELECT a FROM Acertijo a WHERE a.id = :idAcertijo";
        Query<Acertijo> query = this.sessionFactory.getCurrentSession().createQuery(hql, Acertijo.class);
        query.setParameter("idAcertijo", idAcertijo);
        return query.getSingleResult();
    }

    @Override
    public Etapa buscarEtapaPorId(Long idEtapa) {
        String hql = "SELECT e FROM Etapa e WHERE e.id = :idEtapa";
        Query<Etapa> query = this.sessionFactory.getCurrentSession().createQuery(hql, Etapa.class);
        query.setParameter("idEtapa", idEtapa);
        return query.getSingleResult();
    }


    @Override
    public Partida obtenerPartidaActivaPorUsuario(Long idUsuario) {
        String hql = "SELECT p FROM Partida p WHERE p.usuario.id = :idUsuario AND p.esta_activa = true";
        Query<Partida> query = this.sessionFactory.getCurrentSession().createQuery(hql, Partida.class);
        query.setParameter("idUsuario", idUsuario);
        return query.getSingleResult();
    }

    @Override
    public void finalizarPartida(Partida partida) {
        this.sessionFactory.getCurrentSession().update(partida);
    }



}
