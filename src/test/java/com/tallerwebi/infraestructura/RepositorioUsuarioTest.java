package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.RepositorioUsuario;
import com.tallerwebi.dominio.Usuario;
import com.tallerwebi.dominio.excepcion.DatosIncompletosException;
import com.tallerwebi.dominio.excepcion.UsuarioExistente;
import com.tallerwebi.infraestructura.config.HibernateTestInfraestructuraConfig;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;

import static jdk.internal.org.objectweb.asm.util.CheckClassAdapter.verify;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;


import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;

// AÑADE ESTAS ANOTACIONES
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { HibernateTestInfraestructuraConfig.class })
@Transactional

public class RepositorioUsuarioTest {
    @Autowired
    private SessionFactory sessionFactory;
    private RepositorioUsuario repositorioUsuario;

    @BeforeEach
    public void init() {
        this.repositorioUsuario = new RepositorioUsuarioImpl(sessionFactory);
    }

    @Test
    public void DadoQueTengoUnEmailYPasswordBuscoUnUnicoUsuario() {
        Usuario usuario = new Usuario();
        usuario.setEmail("email@gmail.com");
        usuario.setPassword("12345678");

        this.repositorioUsuario.guardar(usuario);

        Usuario buscarUsuario = this.repositorioUsuario.buscarUsuario("email@gmail.com", "12345678");

        assertThat(buscarUsuario, equalTo(usuario));
    }

    @Test
    public void DadoQueTengoUnUsuarioLoDeberiaEncontrarPorNombreUsuario() {
        Usuario usuario = new Usuario();
        usuario.setNombreUsuario("alanpro");

        this.repositorioUsuario.guardar(usuario);

        Usuario usuarioEncontrado =this.repositorioUsuario.buscarPorNombreUsuario("alanpro");

        assertThat(usuarioEncontrado, equalTo(usuario));
    }

    @Test
        public void DadoQueTengoUnUsuario() {
        String nombreUsuario = "alanpro";
        Usuario usuario = new Usuario();
        usuario.setNombreUsuario(nombreUsuario);
        Usuario usuario2 = new Usuario();
        usuario2.setNombreUsuario(nombreUsuario);

        this.repositorioUsuario.guardar(usuario);
        this.repositorioUsuario.guardar(usuario2);

        assertThrows(UsuarioExistente.class, () -> {
            repositorioUsuario.buscarPorNombreUsuario(nombreUsuario);
        });

    }
}
