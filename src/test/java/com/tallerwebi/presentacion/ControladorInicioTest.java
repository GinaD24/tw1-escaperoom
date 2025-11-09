package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidad.Sala;
import com.tallerwebi.dominio.interfaz.servicio.ServicioCompra;
import com.tallerwebi.dominio.interfaz.servicio.ServicioLogin;
import com.tallerwebi.dominio.interfaz.servicio.ServicioSala;
import com.tallerwebi.dominio.enums.Dificultad;
import com.tallerwebi.dominio.excepcion.NoHaySalasExistentes;
import com.tallerwebi.dominio.excepcion.SalaInexistente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;
import static org.mockito.Mockito.*;

public class ControladorInicioTest {

    private ControladorInicio controladorInicio;
    private ServicioSala servicioSala;
    private ServicioCompra servicioCompra;
    private ServicioLogin servicioLogin;
    HttpServletRequest requestMock;

    @BeforeEach
    public void init(){
        this.servicioSala = mock(ServicioSala.class);
        this.servicioCompra = mock(ServicioCompra.class);
        this.servicioLogin = mock(ServicioLogin.class);
        this.controladorInicio = new ControladorInicio(servicioSala, servicioCompra, servicioLogin);
        this.requestMock = mock(HttpServletRequest.class);
    }

    @Test
    public void dadoQueExisteUnaVistaDeInicioCuandoPidoQueLaMuestreLaDevuelve() {
        // preparacion
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setSession(new MockHttpSession());

        when(servicioSala.traerSalas()).thenReturn(new ArrayList<>());

        // ejecucion
        ModelAndView modelAndView = controladorInicio.verInicio(request);

        // verificación
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("inicio"));
    }

    @Test
    public void dadoQueExisteUnaListaDeSalasCuandoPidoQueLasMuestreDevuelveLaVistaDeInicioCon3Salas() {
        // preparacion
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setSession(new MockHttpSession());

        Sala sala1 = new Sala(1, "Sala 1", Dificultad.PRINCIPIANTE, "", "", true, 10, "");
        Sala sala2 = new Sala(2, "Sala 2", Dificultad.INTERMEDIO, "", "", true, 15, "");
        Sala sala3 = new Sala(3, "Sala 3", Dificultad.AVANZADO, "", "", true, 20, "");

        sala1.setEs_paga(false);
        sala2.setEs_paga(false);
        sala3.setEs_paga(false);

        List<Sala> salas = new ArrayList<>();
        salas.add(sala1);
        salas.add(sala2);
        salas.add(sala3);

        when(servicioSala.traerSalas()).thenReturn((ArrayList<Sala>) salas);
        when(servicioCompra.salaDesbloqueadaParaUsuario(any(), any())).thenReturn(false);

        // ejecución
        ModelAndView modelAndView = controladorInicio.verInicio(request);

        List<Sala> salasObtenidas = (List<Sala>) modelAndView.getModel().get("salas");

        // verificación
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("inicio"));
        assertThat(salasObtenidas.size(), equalTo(3));
        verify(servicioSala).traerSalas();
    }

    @Test
    public void dadoQueNOExisteUnaListaDeSalasCuandoPidoQueLasMuestreDevuelveUnMensajeDeError_NoHaySalasExistentes() {
        // preparacion
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setSession(new MockHttpSession());

        doThrow(NoHaySalasExistentes.class).when(servicioSala).traerSalas();

        // ejecución
        ModelAndView modelAndView = controladorInicio.verInicio(request);

        // verificación
        assertThat(modelAndView.getModel().get("error").toString(), equalToIgnoringCase("No hay salas existentes."));
    }

    @Test
    public void dadoQueExistenSalasCuandoQuieroVerUnaEnEspecificoMeDevuelveLaVistaDeEsaSala() {
        // preparación
        Sala sala1 = new Sala(1, "Sala 1", Dificultad.PRINCIPIANTE, "", "", true, 10, "");
        when(servicioSala.obtenerSalaPorId(1)).thenReturn(sala1);


        // ejecución
        ModelAndView modelAndView = controladorInicio.verSala(sala1.getId(), requestMock);

        // verificación
        assertThat(modelAndView.getModel().get("SalaObtenida"), equalTo(sala1));
        verify(servicioSala).obtenerSalaPorId(1);
    }

    @Test
    public void dadoQueExistenSalasCuandoQuieroVerUnaQueNoExisteMeRedirigeAElInicio() {
        // preparación
        doThrow(SalaInexistente.class).when(servicioSala).obtenerSalaPorId(5);
        // ejecución

        ModelAndView modelAndView = controladorInicio.verSala(5, requestMock);

        // verificación
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/inicio/"));
        verify(servicioSala).obtenerSalaPorId(5);
    }

    @Test
    public void dadoQueExisteUnaListaDeSalasCuandoPidoQueMuestreUnaSalaPrincipianteEstaTieneUnaDuracionDe10Minutos() {
        // preparación
        Sala sala1 = new Sala(1, "Sala 1", Dificultad.PRINCIPIANTE, "", "", true, 10, "");

        when(servicioSala.obtenerSalaPorId(1)).thenReturn(sala1);
        // ejecución
        ModelAndView modelAndView = controladorInicio.verSala(sala1.getId(), requestMock);

        Sala salaObtenida = (Sala) modelAndView.getModel().get("SalaObtenida");

        // verificación
        assertThat(salaObtenida.getDuracion(), equalTo(10));
        verify(servicioSala).obtenerSalaPorId(1);
    }

    @Test
    public void deberiaDevolverLasSalasPrincipiantesCuandoLasFiltroPorDificultad(){
        Sala sala1 = new Sala(1, "SALA 1", Dificultad.PRINCIPIANTE, "", "", null, null, null);
        Sala sala2 = new Sala(2, "SALA 2", Dificultad.INTERMEDIO, "", "", null, null, null);
        Sala sala3 = new Sala(3, "SALA 3", Dificultad.PRINCIPIANTE, "", "", null, null, null);
        Sala sala4 = new Sala(4, "SALA 4", Dificultad.AVANZADO, "", "", null, null, null);

        List<Sala> salas = new ArrayList<>();
        salas.add(sala1);
        salas.add(sala3);

        when(servicioSala.obtenerSalaPorDificultad(Dificultad.PRINCIPIANTE)).thenReturn(salas);

        List<Sala> salasObtenidas = servicioSala.obtenerSalaPorDificultad(Dificultad.PRINCIPIANTE);

        verify(servicioSala).obtenerSalaPorDificultad(Dificultad.PRINCIPIANTE);
        assertThat(salasObtenidas, equalTo(salas));
    }

    @Test
    public void deberiaDevolverTodasLasSalasCuandoLasFiltroPorTodasLasDificultades(){
        Sala sala1 = new Sala(1, "SALA 1", Dificultad.PRINCIPIANTE, "", "", null, null, null);
        Sala sala2 = new Sala(2, "SALA 2", Dificultad.INTERMEDIO, "", "", null, null, null);
        Sala sala3 = new Sala(3, "SALA 3", Dificultad.PRINCIPIANTE, "", "", null, null, null);
        Sala sala4 = new Sala(4, "SALA 4", Dificultad.AVANZADO, "", "", null, null, null);

        List<Sala> salas = new ArrayList<>();
        salas.add(sala1);
        salas.add(sala2);
        salas.add(sala3);
        salas.add(sala4);

        when(servicioSala.obtenerSalaPorDificultad(null)).thenReturn(salas);

        List<Sala> salasObtenidas = servicioSala.obtenerSalaPorDificultad(null);

        verify(servicioSala).obtenerSalaPorDificultad(null);
        assertThat(salasObtenidas, equalTo(salas));
    }
}