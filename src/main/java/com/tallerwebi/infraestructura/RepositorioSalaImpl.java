package com.tallerwebi.infraestructura;
import com.tallerwebi.dominio.RepositorioSala;
import com.tallerwebi.dominio.Sala;
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
    public List<Sala> obtenerSalasPorDificultad(String dificultad) {
        String hql = "SELECT s FROM Sala s WHERE s.dificultad = :dificultad";
        Query<Sala> query = this.sessionFactory.getCurrentSession().createQuery(hql, Sala.class);
        query.setParameter("dificultad", dificultad);
        return query.getResultList();
    }

}
//            List<Sala> salas = new ArrayList<>();
//            salas.add(new Sala(1, "La Mansión Misteriosa", "Principiante", "Mansión", "Una noche tormentosa te encuentras atrapado en una vieja mansión llena de acertijos.", true, null, null));
//            salas.add(new Sala(2, "El Laboratorio Secreto", "Intermedio", "Laboratorio", "Un científico desaparecido dejó pistas en su laboratorio. ¿Podrás descubrir qué tramaba?", true, null, null));
//            salas.add(new Sala(3, "La Cárcel Abandonada", "Avanzado", "Prisión", "Despiertas en una celda oxidada. Solo resolviendo complejos acertijos podrás escapar.", false, null, null));
//            return salas;