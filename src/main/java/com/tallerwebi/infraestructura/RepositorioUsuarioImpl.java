package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.interfaz.repositorio.RepositorioUsuario;
import com.tallerwebi.dominio.entidad.Usuario;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

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

    public Usuario obtenerUsuarioPorId(Long id) {
        return sessionFactory.getCurrentSession().get(Usuario.class, id);
    }
//    @Transactional
//    public Usuario obtenerUsuarioDePrueba() {
//        Session session = sessionFactory.getCurrentSession();
//
//
//        Logro logro1 = new Logro("Escapista Novato", "Completó su primera sala");
//        Logro logro2 = new Logro("Velocista", "Terminó una sala en menos de 10 minutos");
//        Logro logro3 = new Logro("Coleccionista", "Obtuvo todos los logros disponibles");
//
//        session.saveOrUpdate(logro1);
//        session.saveOrUpdate(logro2);
//        session.saveOrUpdate(logro3);
//
//
//        Usuario usuario = new Usuario();
//
//        usuario.setId(1L);
//        usuario.setNombre("Juan");
//        usuario.setApellido("Pérez");
//        usuario.setEmail("juan@prueba.com");
//        usuario.setPassword("123456789");
//        usuario.setNombreUsuario("Jugador.Prueba");
//        usuario.setRol("JUGADOR");
//        usuario.setActivo(true);
//        usuario.setFotoPerfil("pruebafoto.png");
//        usuario.setFechaNacimiento(java.time.LocalDate.of(1995, 5, 10));
//
//
//        List<Logro> logros = new ArrayList<>();
//        logros.add(logro1);
//        logros.add(logro2);
//        usuario.setLogrosFavoritos(logros);
//
//        return usuario;
//    }






}
