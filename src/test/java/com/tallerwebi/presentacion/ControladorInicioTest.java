package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidad.Sala;
import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.interfaz.repositorio.RepositorioUsuario;
import com.tallerwebi.dominio.interfaz.servicio.ServicioCompra;
import com.tallerwebi.dominio.interfaz.servicio.ServicioSala;
import com.tallerwebi.dominio.enums.Dificultad;
import com.tallerwebi.dominio.excepcion.NoHaySalasExistentes;
import com.tallerwebi.dominio.excepcion.SalaInexistente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;
import static org.mockito.Mockito.*;

public class ControladorInicioTest {

    private ControladorInicio controladorInicio;
    private ServicioSala servicioSala;
    private ServicioCompra servicioCompra;
    private RepositorioUsuario repositorioUsuario;
    private HttpServletRequest request;
    private HttpSession session;

    @BeforeEach
    public void init() {
        this.servicioSala = mock(ServicioSala.class);
        this.servicioCompra = mock(ServicioCompra.class);
        this.repositorioUsuario = mock(RepositorioUsuario.class);
        this.request = mock(HttpServletRequest.class);
        this.session = mock(HttpSession.class);

        when(request.getSession()).thenReturn(session);
        // Quita el stubbing de session.getAttribute aquí para evitar conflictos

        this.controladorInicio = new ControladorInicio(servicioSala, servicioCompra, repositorioUsuario);
    }

    @Test
    public void dadoQueExisteUnaVistaDeInicioCuandoPidoQueLaMuestreLaDevuelve() {
        // Simula no logueado
        when(session.getAttribute("id_usuario")).thenReturn(null);

        ModelAndView modelAndView = controladorInicio.verInicio(request);
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("inicio"));
    }

    @Test
    public void dadoQueExisteUnaListaDeSalasCuandoPidoQueLasMuestreDevuelveLaVistaDeInicioCon3Salas() {
        // Simula no logueado
        when(session.getAttribute("id_usuario")).thenReturn(null);

        Sala sala1 = new Sala();
        sala1.setId(1);
        Sala sala2 = new Sala();
        sala2.setId(2);
        Sala sala3 = new Sala();
        sala3.setId(3);

        List<Sala> salas = new ArrayList<>();
        salas.add(sala1);
        salas.add(sala2);
        salas.add(sala3);

        when(servicioSala.traerSalas()).thenReturn((ArrayList<Sala>) salas);

        ModelAndView modelAndView = controladorInicio.verInicio(request);

        List<Map<String, Object>> salasObtenidas = (List<Map<String, Object>>) modelAndView.getModel().get("salas");

        assertThat(modelAndView.getViewName(), equalToIgnoringCase("inicio"));
        assertThat(salasObtenidas.size(), equalTo(3));
        verify(servicioSala).traerSalas();
    }

    @Test
    public void dadoQueExistenSalasCuandoQuieroVerUnaEnEspecificoMeDevuelveLaVistaDeEsaSala() {
        Sala sala1 = new Sala();
        sala1.setId(1);

        when(servicioSala.obtenerSalaPorId(1)).thenReturn(sala1);

        ModelAndView modelAndView = controladorInicio.verSala(1);

        assertThat(modelAndView.getModel().get("SalaObtenida"), equalTo(sala1));
        verify(servicioSala).obtenerSalaPorId(1);
    }

    @Test
    public void dadoQueExistenSalasCuandoQuieroVerUnaQueNoExisteMeRedirigeAElInicio() {
        doThrow(SalaInexistente.class).when(servicioSala).obtenerSalaPorId(5);

        ModelAndView modelAndView = controladorInicio.verSala(5);

        assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/inicio/"));
        verify(servicioSala).obtenerSalaPorId(5);
    }

    @Test
    public void dadoQueNOExisteUnaListaDeSalasCuandoPidoQueLasMuestreDevuelveUnMensajeDeError_NoHaySalasExistentes() {
        // Simula no logueado
        when(session.getAttribute("id_usuario")).thenReturn(null);

        doThrow(NoHaySalasExistentes.class).when(servicioSala).traerSalas();

        ModelAndView modelAndView = controladorInicio.verInicio(request);

        assertThat(modelAndView.getModel().get("error").toString(), equalToIgnoringCase("No hay salas existentes."));
    }

    @Test
    public void dadoQueExisteUnaListaDeSalasCuandoPidoQueMuestreUnaSalaPrincipianteEstaTieneUnaDuracionDe10Minutos() {
        Sala sala1 = new Sala();
        sala1.setId(1);
        sala1.setDuracion(10);

        when(servicioSala.obtenerSalaPorId(1)).thenReturn(sala1);

        ModelAndView modelAndView = controladorInicio.verSala(1);

        Sala salaObtenida = (Sala) modelAndView.getModel().get("SalaObtenida");

        assertThat(salaObtenida.getDuracion(), equalTo(10));
        verify(servicioSala).obtenerSalaPorId(1);
    }

    @Test
    public void deberiaDevolverLasSalasPrincipiantesCuandoLasFiltroPorDificultad() {
        // Simula no logueado
        when(session.getAttribute("id_usuario")).thenReturn(null);

        Sala sala1 = new Sala(1, "SALA 1", Dificultad.PRINCIPIANTE, "", "", null, null, null);
        Sala sala3 = new Sala(3, "SALA 3", Dificultad.PRINCIPIANTE, "", "", null, null, null);

        List<Sala> salas = new ArrayList<>();
        salas.add(sala1);
        salas.add(sala3);

        when(servicioSala.obtenerSalaPorDificultad(Dificultad.PRINCIPIANTE)).thenReturn(salas);

        List<Sala> salasObtenidas = servicioSala.obtenerSalaPorDificultad(Dificultad.PRINCIPIANTE);

        verify(servicioSala).obtenerSalaPorDificultad(Dificultad.PRINCIPIANTE);
        assertThat(salasObtenidas, equalTo(salas));
    }

    @Test
    public void deberiaDevolverTodasLasSalasCuandoLasFiltroPorTodasLasDificultades() {
        // Simula no logueado
        when(session.getAttribute("id_usuario")).thenReturn(null);

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

    @Test
    public void dadoQueHayUsuarioLogueadoCuandoPidoInicioVerificaSalasDesbloqueadas() {
        Long idUsuario = 1L;  // Cambiado a Long
        Usuario usuario = new Usuario();
        usuario.setId(idUsuario);  // Ya es Long, sin .longValue()
        Sala sala1 = new Sala();
        sala1.setId(1);
        // Cambia List.of() por ArrayList para evitar ClassCastException
        List<Sala> salas = new ArrayList<>(Arrays.asList(sala1));
        when(session.getAttribute("id_usuario")).thenReturn(idUsuario);
        when(repositorioUsuario.obtenerUsuarioPorId(idUsuario)).thenReturn(usuario);  // Ya espera Long
        when(servicioSala.traerSalas()).thenReturn((ArrayList<Sala>) salas);
        when(servicioCompra.salaDesbloqueadaParaUsuario(usuario, sala1)).thenReturn(true);
        ModelAndView modelAndView = controladorInicio.verInicio(request);
        verify(repositorioUsuario).obtenerUsuarioPorId(idUsuario);  // Sin .longValue()
        verify(servicioCompra).salaDesbloqueadaParaUsuario(usuario, sala1);
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("inicio"));
    }

}
