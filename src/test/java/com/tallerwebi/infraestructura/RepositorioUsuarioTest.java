package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.interfaz.repositorio.RepositorioUsuario;
import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.infraestructura.config.HibernateTestInfraestructuraConfig;
import org.hibernate.NonUniqueResultException;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;


import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;

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
    public void DadoQueTengoUnEmailYPasswordBuscoUnUnicoUsuarioYLoDevuelvo() {
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
        public void DadoQueTengoUnUsuarioConElMismoNombreUsuarioElSistemaDebeDevolverUnaExcepcion() {
        String nombreUsuario = "alanpro";
        Usuario usuario1 = new Usuario();
        usuario1.setNombreUsuario(nombreUsuario);
        Usuario usuario2 = new Usuario();
        usuario2.setNombreUsuario(nombreUsuario);

        this.repositorioUsuario.guardar(usuario1);
        this.repositorioUsuario.guardar(usuario2);

        assertThrows(NonUniqueResultException.class, () -> {
            repositorioUsuario.buscarPorNombreUsuario(nombreUsuario);
        });

    }

    @Test
    public void dadoQueTengoUnUsuarioExistentePuedoModificarSuContraseniaYElCambioSeGuarda(){
        String contraseniaExistente = "12345678";
        String contraseniaNueva = "87654321";

        Usuario usuario = new Usuario();
        usuario.setEmail("franco@hotmail.com");
        usuario.setPassword(contraseniaExistente);

        this.repositorioUsuario.guardar(usuario);

        usuario.setPassword(contraseniaNueva);

        this.repositorioUsuario.modificar(usuario);

        Usuario buscarUsuarioConContraseniaAntigua = this.repositorioUsuario.buscarUsuario("franco@hotmail.com", contraseniaExistente);
        assertThat(buscarUsuarioConContraseniaAntigua, nullValue());

        Usuario buscarUsuarioConContraseniaActualizada = this.repositorioUsuario.buscarUsuario("franco@hotmail.com",  contraseniaNueva);
        assertThat(buscarUsuarioConContraseniaActualizada, equalTo(usuario));
    }

    @Test
    public void dadoQueTengoUnUsuarioSiAlBuscarloEscriboMalSuMailDebeDevolverNull(){
        String emailUsuario = "franco@outlook.com";

        Usuario usuario = new Usuario();
        usuario.setEmail(emailUsuario);
        usuario.setPassword("12345678");

        this.repositorioUsuario.guardar(usuario);

        Usuario usuarioNoEncontrado = this.repositorioUsuario.buscarUsuario("alan@outlook.com", "12345678");

        assertThat(usuarioNoEncontrado, nullValue());

    }

    @Test
    public void dadoQueBuscoUnNombreUSuarioQueNoExisteElSistemaDevuelveNull(){

        Usuario usuario = new Usuario();
        usuario.setNombreUsuario(null);

        this.repositorioUsuario.guardar(usuario);

        Usuario buscarUsuarioNull = this.repositorioUsuario.buscarPorNombreUsuario(usuario.getNombreUsuario());

        assertThat(buscarUsuarioNull, nullValue());
    }

    @Test
    public void dadoQueTengoUnUsuarioPuedoBuscarloPorEmailYDevolverlo(){
        Usuario usuario = new Usuario();
        usuario.setEmail("franco@outlook.com");

        this.repositorioUsuario.guardar(usuario);

        Usuario buscarUsuarioPorEmail = this.repositorioUsuario.buscar("franco@outlook.com");

        assertThat(buscarUsuarioPorEmail, equalTo(usuario));

    }

    @Test
    public void dadoQueTengoUnUsuarioAlBuscarloPorUnEmailQueNoExisteElSistemaDevuelveNull(){
        Usuario usuario = new Usuario();
        usuario.setEmail(null);

        this.repositorioUsuario.guardar(usuario);

        Usuario buscarEmailNull = this.repositorioUsuario.buscar(usuario.getEmail());

        assertThat(buscarEmailNull, nullValue());
    }

    @Test
    public void dadoQueTengoUnUsuarioConContraseniaNullAlBuscarloObtengoNull(){
        Usuario usuario = new Usuario();
        usuario.setEmail("franco@outlook.com");
        usuario.setPassword(null);

        this.repositorioUsuario.guardar(usuario);

        Usuario buscarUsuario = this.repositorioUsuario.buscarUsuario(usuario.getEmail(), usuario.getPassword());

        assertThat(buscarUsuario, nullValue());

    }

    @Test
    public void dadoQueTengoUnUsuarioSiLoBuscoYEscriboUnaCadenaVaciaDevuelveNull(){
        Usuario usuario = new Usuario();

        this.repositorioUsuario.guardar(usuario);

        Usuario buscarUsuario = this.repositorioUsuario.buscar("");

        assertThat(buscarUsuario, nullValue());
    }

    @Test
    public void dadoUnUsuarioGuardadoDeberiaPoderObtenerloPorSuId() {
        Usuario usuario = new Usuario();
        usuario.setEmail("test@id.com");
        usuario.setPassword("pass");
        this.repositorioUsuario.guardar(usuario);

        Usuario usuarioEncontrado = this.repositorioUsuario.obtenerUsuarioPorId(usuario.getId());

        assertThat(usuarioEncontrado, equalTo(usuario));
    }

    @Test
    public void dadoUnIdInexistenteAlBuscarUsuarioDebeDevolverNull() {
        Long idInexistente = 9999L;

        Usuario usuarioNoEncontrado = this.repositorioUsuario.obtenerUsuarioPorId(idInexistente);

        assertThat(usuarioNoEncontrado, nullValue());
    }

    @Test
    public void dadoUnUsuarioExistentePuedoModificarSuNombreYFechaDeNacimiento() {
        Usuario usuario = new Usuario();
        usuario.setEmail("perfil@edit.com");
        usuario.setNombreUsuario("NombreAntiguo");
        usuario.setFechaNacimiento(java.time.LocalDate.of(1990, 1, 1));
        this.repositorioUsuario.guardar(usuario);

        String nuevoNombre = "NombreNuevo";
        java.time.LocalDate nuevaFecha = java.time.LocalDate.of(2000, 12, 31);

        usuario.setNombreUsuario(nuevoNombre);
        usuario.setFechaNacimiento(nuevaFecha);

        this.repositorioUsuario.modificar(usuario);

        Usuario usuarioModificado = this.repositorioUsuario.obtenerUsuarioPorId(usuario.getId());

        assertThat(usuarioModificado.getNombreUsuario(), equalTo(nuevoNombre));
        assertThat(usuarioModificado.getFechaNacimiento(), equalTo(nuevaFecha));
        assertThat(usuarioModificado.getEmail(), equalTo("perfil@edit.com"));
    }
}
