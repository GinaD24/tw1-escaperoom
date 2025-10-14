package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Logro;
import com.tallerwebi.dominio.RepositorioUsuario;
import com.tallerwebi.dominio.Usuario;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Repository("repositorioUsuario")
@Transactional
public class RepositorioUsuarioImpl implements RepositorioUsuario {

    private SessionFactory sessionFactory;

    @Autowired
    public RepositorioUsuarioImpl(SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Usuario buscarUsuario(String email, String password) {

        final Session session = sessionFactory.getCurrentSession();
        return (Usuario) session.createCriteria(Usuario.class)
                .add(Restrictions.eq("email", email))
                .add(Restrictions.eq("password", password))
                .uniqueResult();
    }

    @Override
    public void guardar(Usuario usuario) {
        sessionFactory.getCurrentSession().save(usuario);
    }

    @Override
    public Usuario buscar(String email) {
        return (Usuario) sessionFactory.getCurrentSession().createCriteria(Usuario.class)
                .add(Restrictions.eq("email", email))
                .uniqueResult();
    }

    @Override
    public void modificar(Usuario usuario) {
        sessionFactory.getCurrentSession().update(usuario);
    }

    @Override
    public Usuario buscarPorNombreUsuario(String nombreUsuario) {
        final Session session = sessionFactory.getCurrentSession();
        return (Usuario) session.createCriteria(Usuario.class)
                .add(Restrictions.eq("nombreUsuario", nombreUsuario))
                .uniqueResult();
    }

    @Override
    public Usuario buscarPorId(Long id) {
        return (Usuario) sessionFactory.getCurrentSession().createCriteria(Usuario.class)
                .add(Restrictions.eq("id", id))
                .uniqueResult();
    }


    @Transactional
    public void crearUsuarioDePrueba() {
        Session session = sessionFactory.getCurrentSession();


        Logro logro1 = new Logro("Escapista Novato", "Completó su primera sala");
        Logro logro2 = new Logro("Velocista", "Terminó una sala en menos de 10 minutos");
        Logro logro3 = new Logro("Coleccionista", "Obtuvo todos los logros disponibles");

        session.saveOrUpdate(logro1);
        session.saveOrUpdate(logro2);
        session.saveOrUpdate(logro3);


        Usuario usuario = new Usuario();

        usuario.setNombre("Juan");
        usuario.setApellido("Pérez");
        usuario.setEmail("juan@prueba.com");
        usuario.setPassword("123456789");
        usuario.setNombreUsuario("Jugador.Prueba");
        usuario.setRol("JUGADOR");
        usuario.setActivo(true);
        usuario.setFotoPerfil("pruebafoto.png");
        usuario.setFechaNacimiento(java.time.LocalDate.of(1995, 5, 10));


        List<Logro> logros = new ArrayList<>();
        logros.add(logro1);
        logros.add(logro2);
        usuario.setLogrosFavoritos(logros);

        session.save(usuario);

    }






}
