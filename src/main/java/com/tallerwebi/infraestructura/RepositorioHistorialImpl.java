package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.interfaz.repositorio.RepositorioHistorial;
import com.tallerwebi.dominio.entidad.Historial;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

@Repository
public class RepositorioHistorialImpl implements RepositorioHistorial {

    private final SessionFactory sessionFactory;
    private List<Historial> historials = new ArrayList<>();

    public RepositorioHistorialImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void guardar(Historial historial) {
        historials.add(historial);
    }

    @Override
    public List<Historial> obtenerTodas() {
        return new ArrayList<>(historials);
    }

    @Override
    public List<Historial> obtenerPorJugador(String jugador) {
        return historials.stream()
                .filter(p -> p.getJugador().equals(jugador))
                .collect(Collectors.toList());
    }

    @Override
    public List<Historial> ObtenerHistorialPorSala(Integer idSala) {

        final Session session = sessionFactory.getCurrentSession();

        String hql = "FROM Historial h WHERE h.idSala = :idSala";

        Query<Historial> query = session.createQuery(hql, Historial.class);

        query.setParameter("idSala", idSala);

        return query.getResultList();
    }
}