package com.tallerwebi.punta_a_punta;

import com.microsoft.playwright.*;
import com.tallerwebi.punta_a_punta.helpers.AcertijoTestHelper;
import com.tallerwebi.punta_a_punta.vistas.VistaInicio;
import com.tallerwebi.punta_a_punta.vistas.VistaPartida;
import com.tallerwebi.punta_a_punta.vistas.VistaSala;
import org.junit.jupiter.api.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.matchesPattern;

public class VistaPartidaE2E {

    static Playwright playwright;
    static Browser browser;
    BrowserContext context;
    VistaSala vistaSala;
    VistaInicio vistaInicio;
    VistaPartida vistaPartida;


    @BeforeAll
    static void configurarPlaywrightYGuardarSesion() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions().setHeadless(false).setSlowMo(1000)
        );

        BrowserContext loginContext = browser.newContext();
        Page loginPage = loginContext.newPage();

        loginPage.navigate("http://localhost:8080/spring/login");
        loginPage.fill("#email", "test@unlam.edu.ar");
        loginPage.fill("#password", "test");
        loginPage.click("button[type=submit]");

        loginContext.storageState(new BrowserContext.StorageStateOptions()
                .setPath(Paths.get("estado-logueado.json")));

        loginContext.close();
    }


    @AfterAll
    static void cerrarNavegador() {
        playwright.close();
    }

    @BeforeEach
    void crearContextoYPaginaConSesion() {
        ReiniciarDB.limpiarBaseDeDatos();

        context = browser.newContext(new Browser.NewContextOptions()
                .setStorageStatePath(Paths.get("estado-logueado.json")));
        Page page = context.newPage();

        vistaInicio = new VistaInicio(page);
        vistaSala = new VistaSala(page);
        vistaPartida = new VistaPartida(page);
    }


    @AfterEach
    void cerrarContexto() {
        context.close();
    }

    @Test
    void deberiaNavegarALaVistaDeLaSalaElegidaPorElUsuario() throws MalformedURLException {
        dadoQueElUsuarioYaInicioSesion("test@unlam.edu.ar", "test");
        dadoQueElUsuarioEstaEnLaVistaDeInicio();
        cuandoElUsuarioTocaLaSala1();
        entoncesDeberiaSerRedirigidoALaVistaDeLaSala();
    }

    @Test
        void deberiaNavegarALaVistaDeLaPartida_ResponderCorrectamenteElPrimerAcertijoDeTipoAdivinanzaDeLaSala1_YSerRedirigidoALaEtapa2() throws MalformedURLException {
        dadoQueElUsuarioYaInicioSesion("test@unlam.edu.ar", "test");
        dadoQueElUsuarioEstaEnLaVistaDeInicio();
        dadoQueElUsuarioEstaEnLaSala1();
        dadoQueElusuarioTocaElBotonEstoyListo();
        dadoQueElUsuarioEstaEnLaVistaDeLaPartida();
        cuandoRespondeElPrimerAcertijoCorrectamente();
        entoncesLoRedirigeALaSiguienteEtapa(1, 2);
    }

    @Test
    void deberiaNavegarALaVistaDeLaPartida_ResponderCorrectamenteElSegundoAcertijoDeTipoSecuenciaDeLaSala1_YSerRedirigidoALaEtapa2() throws MalformedURLException {
        dadoQueElUsuarioYaInicioSesion("test@unlam.edu.ar", "test");
        dadoQueElUsuarioEstaEnLaVistaDeInicio();
        dadoQueElUsuarioEstaEnLaSala1();
        dadoQueElusuarioTocaElBotonEstoyListo();
        dadoQueElUsuarioEstaEnLaVistaDeLaPartida();
        dadoQueElUsuarioRespondioCorrectamenteElPrimerAcertijo();
        cuandoRespondeElSegundoAcertijoCorrectamente();
        entoncesLoRedirigeALaSiguienteEtapa(1, 3);
    }

    private void cuandoRespondeElSegundoAcertijoCorrectamente() {
        vistaPartida.darClickEnMostrarSecuencia();
        vistaPartida.esperarSecuenciaMostrada();
        vistaPartida.darClickEnBoton(1L);
        vistaPartida.darClickEnBoton(2L);
        vistaPartida.darClickEnBoton(3L);
        vistaPartida.darClickEnBoton(4L);
        vistaPartida.darClickEnBoton(5L);
        vistaPartida.darClickEnEnviar();

    }

    private void dadoQueElUsuarioRespondioCorrectamenteElPrimerAcertijo() throws MalformedURLException {
        cuandoRespondeElPrimerAcertijoCorrectamente();
        entoncesLoRedirigeALaSiguienteEtapa(1, 2);
    }

    private void entoncesLoRedirigeALaSiguienteEtapa(Integer id_sala, Integer numero_etapa) throws MalformedURLException {
        vistaPartida.irAEtapa(id_sala, numero_etapa);
        URL url = vistaPartida.obtenerURLActual();
        assertThat(url.getPath(), matchesPattern("^/spring/partida/sala1/etapa" + numero_etapa + "$"));
    }

    private void cuandoRespondeElPrimerAcertijoCorrectamente() {
        String acertijo = vistaPartida.obtenerTextoAcertijo();
        String respuestaCorrecta = AcertijoTestHelper.obtenerRespuesta(acertijo);

        vistaPartida.escribirRespuestaAcertijo(respuestaCorrecta);
        vistaPartida.darClickEnEnviar();
    }

    private void dadoQueElUsuarioEstaEnLaVistaDeLaPartida() throws MalformedURLException {

        vistaPartida.irAEtapa(1, 1);
        URL url = vistaPartida.obtenerURLActual();
        assertThat(url.getPath(), matchesPattern("^/spring/partida/sala1/etapa1"));
    }

    private void dadoQueElusuarioTocaElBotonEstoyListo() {
        vistaSala.darClickEnEstoyListo();
    }

    private void dadoQueElUsuarioEstaEnLaSala1() throws MalformedURLException {
        cuandoElUsuarioTocaLaSala1();
        entoncesDeberiaSerRedirigidoALaVistaDeLaSala();
    }


    private void dadoQueElUsuarioYaInicioSesion(String mail, String password) {
    }

    private void dadoQueElUsuarioEstaEnLaVistaDeInicio() throws MalformedURLException {
        URL url = vistaInicio.obtenerURLActual();
        assertThat(url.getPath(), matchesPattern("^/spring/inicio/?$"));
    }

    private void cuandoElUsuarioTocaLaSala1() {
    vistaInicio.darClickEnLaSala1();

    }

    private void entoncesDeberiaSerRedirigidoALaVistaDeLaSala() throws MalformedURLException {
        URL url = vistaSala.obtenerURLActual();
        assertThat(url.getPath(), matchesPattern("^/spring/inicio/sala/1"));
    }


}
