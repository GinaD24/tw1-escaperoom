package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.entidad.Compra;
import com.tallerwebi.dominio.entidad.Sala;
import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.interfaz.repositorio.RepositorioCompra;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RepositorioCompraImpl implements RepositorioCompra {

    private final SessionFactory sessionFactory;

    @Autowired
    public RepositorioCompraImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void guardar(Compra compra) {
        this.sessionFactory.getCurrentSession().saveOrUpdate(compra);
    }

    @Override
    public List<Compra> obtenerComprasPorUsuario(Usuario usuario) {
        String hql = "FROM Compra c WHERE c.usuario = :usuario";
        Query<Compra> query = this.sessionFactory.getCurrentSession().createQuery(hql, Compra.class);
        query.setParameter("usuario", usuario);
        return query.getResultList();
    }

    @Override
    public Compra obtenerCompraPorUsuarioYSala(Usuario usuario, Sala sala) {
        String hql = "FROM Compra c WHERE c.usuario = :usuario AND c.sala = :sala";
        Query<Compra> query = this.sessionFactory.getCurrentSession().createQuery(hql, Compra.class);
        query.setParameter("usuario", usuario);
        query.setParameter("sala", sala);
        return query.uniqueResult();
    }

    @Override
    public boolean salaDesbloqueadaParaUsuario(Usuario usuario, Sala sala) {
        Compra compra = obtenerCompraPorUsuarioYSala(usuario, sala);
        return compra != null && compra.getPagada();
    }

    @Override
    public Compra obtenerCompraPorPaymentId(String paymentId) {
        return sessionFactory.getCurrentSession()
                .createQuery("FROM Compra WHERE paymentId = :paymentId", Compra.class)
                .setParameter("paymentId", paymentId)
                .uniqueResult();
    }
    @Override
    public void guardarCompra(Compra compra) {
        sessionFactory.getCurrentSession().saveOrUpdate(compra);
    }


    @Override
    public Compra obtenerCompraPorExternalReference(String externalReference) {
        return sessionFactory.getCurrentSession()
                .createQuery("FROM Compra WHERE externalReference = :externalReference", Compra.class)
                .setParameter("externalReference", externalReference)
                .uniqueResult();
    }
}