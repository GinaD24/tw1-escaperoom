package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.entidad.Partida;
import com.tallerwebi.dominio.interfaz.repositorio.RepositorioHistorial;
import org.springframework.stereotype.Repository;

import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

@Repository
public class RepositorioHistorialImpl implements RepositorioHistorial {

    private final SessionFactory sessionFactory;

    public RepositorioHistorialImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    @Override
    public List<Partida> obtenerPartidasPorJugador(Long idUsuario) {
        String hql = "FROM Partida p WHERE p.usuario.id = :idUsuario ORDER BY p.fin DESC";
        Query<Partida> query = this.sessionFactory.getCurrentSession().createQuery(hql, Partida.class);
        query.setParameter("idUsuario", idUsuario);
        return query.getResultList();
    }

}