package com.tallerwebi.infraestructura;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import com.tallerwebi.dominio.entidad.Compra;
import com.tallerwebi.dominio.entidad.Sala;
import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.enums.Dificultad;
import com.tallerwebi.dominio.interfaz.repositorio.RepositorioCompra;
import com.tallerwebi.infraestructura.config.HibernateTestInfraestructuraConfig;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { HibernateTestInfraestructuraConfig.class })
@Transactional
public class RepositorioCompraImplTest {

    @Autowired
    SessionFactory sessionFactory;
    RepositorioCompra repositorioCompra;

    private Usuario usuario1;
    private Usuario usuario2;
    private Sala sala1;
    private Sala sala2;

    @BeforeEach
    public void init() {
        this.repositorioCompra = new RepositorioCompraImpl(sessionFactory);

        usuario1 = new Usuario();
        usuario1.setEmail("usuario1@test.com");
        usuario2 = new Usuario();
        usuario2.setEmail("usuario2@test.com");

        sala1 = new Sala(4, "El campamento maldito", Dificultad.PRINCIPIANTE, "Campamento", "Estás perdido...", true, 5, "puerta-campamento.png");
        sala1.setCantidadDeEtapas(5);
        sala2 = new Sala(5, "El hospital psiquiatrico", Dificultad.AVANZADO, "Hospital", "Te han encerrado...", true, 12, "puerta-psiquiatrico.png");
        sala2.setCantidadDeEtapas(10);

        this.sessionFactory.getCurrentSession().save(usuario1);
        this.sessionFactory.getCurrentSession().save(usuario2);
        this.sessionFactory.getCurrentSession().save(sala1);
        this.sessionFactory.getCurrentSession().save(sala2);
    }

    private Compra crearCompraBase(Usuario usuario, Sala sala, String externalRef, Boolean pagada) {
        Compra compra = new Compra(usuario, sala, LocalDateTime.now(), pagada);
        compra.setExternalReference(externalRef);
        return compra;
    }

    @Test
    public void deberiaGuardarUnaNuevaCompraYRecuperarla() {
        // preparacion
        Compra compra = crearCompraBase(usuario1, sala1, "REF-1234", false);

        // ejecucion
        this.repositorioCompra.guardarCompra(compra);

        // verificacion
        assertNotNull(compra.getId());
        Compra compraGuardada = this.sessionFactory.getCurrentSession().get(Compra.class, compra.getId());
        assertNotNull(compraGuardada);
        assertThat(compraGuardada.getExternalReference(), equalTo("REF-1234"));
    }

    @Test
    public void deberiaObtenerTodasLasComprasDeUnUsuario() {
        Compra compra1 = crearCompraBase(usuario1, sala1, "REF-001", true);
        Compra compra2 = crearCompraBase(usuario1, sala2, "REF-002", true);
        Compra compra3 = crearCompraBase(usuario2, sala1, "REF-003", false);

        this.repositorioCompra.guardarCompra(compra1);
        this.repositorioCompra.guardarCompra(compra2);
        this.repositorioCompra.guardarCompra(compra3);

        // ejecucion
        List<Compra> comprasUsuario1 = this.repositorioCompra.obtenerComprasPorUsuario(usuario1);

        // verificacion
        assertThat(comprasUsuario1, is(notNullValue()));
        assertThat(comprasUsuario1.size(), equalTo(2));
        assertThat(comprasUsuario1, hasItem(compra1));
        assertThat(comprasUsuario1, hasItem(compra2));
        assertThat(comprasUsuario1, not(hasItem(compra3)));
    }

    @Test
    public void deberiaRetornarListaVaciaSiElUsuarioNoTieneCompras() {
        // ejecucion
        List<Compra> comprasUsuario2 = this.repositorioCompra.obtenerComprasPorUsuario(usuario2);

        // verificacion
        assertThat(comprasUsuario2, is(notNullValue()));
        assertThat(comprasUsuario2.size(), equalTo(0));
    }

    @Test
    public void deberiaObtenerUnaCompraDadaUnaSalaYUnUsuario() {
        // preparacion
        Compra compraEsperada = crearCompraBase(usuario1, sala1, "REF-004", true);
        this.repositorioCompra.guardarCompra(compraEsperada);

        // ejecucion
        Compra compraObtenida = this.repositorioCompra.obtenerCompraPorUsuarioYSala(usuario1, sala1);

        // verificacion
        assertThat(compraObtenida, is(notNullValue()));
        assertThat(compraObtenida.getId(), equalTo(compraEsperada.getId()));
    }

    @Test
    public void deberiaRetornarNullSiNoExisteCompraParaSalaYUsuario() {
        // ejecucion
        Compra compraObtenida = this.repositorioCompra.obtenerCompraPorUsuarioYSala(usuario1, sala2);

        // verificacion
        assertNull(compraObtenida);
    }

    @Test
    public void deberiaRetornarTrueSiLaSalaFueCompradaYPagada() {
        // preparacion
        Compra compra = crearCompraBase(usuario1, sala1, "REF-005", true);
        this.repositorioCompra.guardarCompra(compra);

        // ejecucion
        boolean desbloqueada = this.repositorioCompra.salaDesbloqueadaParaUsuario(usuario1, sala1);

        // verificacion
        assertThat(desbloqueada, is(true));
    }

    @Test
    public void deberiaRetornarFalseSiLaSalaFueCompradaPeroNoPagada() {
        // preparacion
        Compra compra = crearCompraBase(usuario1, sala1, "REF-006", false);
        this.repositorioCompra.guardarCompra(compra);

        // ejecucion
        boolean desbloqueada = this.repositorioCompra.salaDesbloqueadaParaUsuario(usuario1, sala1);

        // verificacion
        assertThat(desbloqueada, is(false));
    }

    @Test
    public void deberiaRetornarFalseSiLaSalaNoFueComprada() {
        // ejecucion
        boolean desbloqueada = this.repositorioCompra.salaDesbloqueadaParaUsuario(usuario1, sala2);

        // verificacion
        assertThat(desbloqueada, is(false));
    }

    @Test
    public void deberiaObtenerCompraPorPaymentId() {
        // preparacion
        Compra compraEsperada = crearCompraBase(usuario1, sala1, "REF-007", true);
        compraEsperada.setPaymentId("PAY-ID-TEST");
        this.repositorioCompra.guardarCompra(compraEsperada);

        // ejecucion
        Compra compraObtenida = this.repositorioCompra.obtenerCompraPorPaymentId("PAY-ID-TEST");

        // verificacion
        assertThat(compraObtenida, is(notNullValue()));
        assertThat(compraObtenida.getId(), equalTo(compraEsperada.getId()));
    }

    @Test
    public void deberiaObtenerCompraPorExternalReference() {
        // preparacion
        Compra compraEsperada = crearCompraBase(usuario1, sala1, "REF-008", true);
        this.repositorioCompra.guardarCompra(compraEsperada);

        // ejecucion
        Compra compraObtenida = this.repositorioCompra.obtenerCompraPorExternalReference("REF-008");

        // verificacion
        assertThat(compraObtenida, is(notNullValue()));
        assertThat(compraObtenida.getId(), equalTo(compraEsperada.getId()));
    }


}
