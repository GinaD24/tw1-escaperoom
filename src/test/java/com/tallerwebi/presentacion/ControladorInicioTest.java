package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.Sala;
import com.tallerwebi.dominio.ServicioSala;
import com.tallerwebi.dominio.enums.Dificultad;
import com.tallerwebi.dominio.excepcion.NoHaySalasExistentes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;
import static org.mockito.Mockito.*;

public class ControladorInicioTest {

    private ControladorInicio controladorInicio;
    private ServicioSala servicioSala;

    @BeforeEach
    public void init(){
        this.servicioSala = mock(ServicioSala.class);
        this.controladorInicio = new ControladorInicio(servicioSala);

    }

    @Test
    public void dadoQueExisteUnaVistaDeInicioCuandoPidoQueLaMuestreLaDevuelve() {

        //preparacion

        //ejecucion
        ModelAndView modelAndView = controladorInicio.verInicio();
        //verificacion
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("inicio"));
    }

    @Test
    public void dadoQueExisteUnaListaDeSalasCuandoPidoQueLasMuestreDevuelveLaVistaDeInicioCon3Salas() {

        //preparacion
        Sala sala1 = new Sala();
        Sala sala2 = new Sala();
        Sala sala3 = new Sala();

        //ejecucion
        List<Sala> salas = new ArrayList<Sala>();
        salas.add(sala1);
        salas.add(sala2);
        salas.add(sala3);

        when(servicioSala.traerSalas()).thenReturn((ArrayList<Sala>) salas);

        ModelAndView modelAndView = controladorInicio.verInicio();

        List<Sala> salasObtenidas = (List<Sala>) modelAndView.getModel().get("salas");

        //verificacion

        assertThat(modelAndView.getViewName(), equalToIgnoringCase("inicio"));
        assertThat(salasObtenidas.size(), equalTo(3));

        verify(servicioSala).traerSalas();
    }

    @Test
    public void dadoQueExistenSalasCuandoQuieroVerUnaEnEspecificoMeDevuelveLaVistaDeEsaSala() {

        //preparacion
        Sala sala1 = new Sala();
        sala1.setId(1);

        when(servicioSala.obtenerSalaPorId(1)).thenReturn(sala1);

        //ejecucion
        ModelAndView modelAndView = controladorInicio.verSala(sala1.getId());

        //verificacion
        assertThat(modelAndView.getModel().get("SalaObtenida"), equalTo(sala1));
        verify(servicioSala).obtenerSalaPorId(1);


    }


    @Test
    public void dadoQueExistenSalasCuandoQuieroVerUnaQueNoExisteDevuelveMensajeDeError_SalaNoEncontrada() {

        //preparacion
        when(servicioSala.obtenerSalaPorId(5)).thenReturn(null);

        //ejecucion
        ModelAndView modelAndView = controladorInicio.verSala(5);

        //verificacion
        assertThat(modelAndView.getModel().get("error").toString(), equalToIgnoringCase("Sala no encontrada"));
        verify(servicioSala).obtenerSalaPorId(5);
    }

    @Test
    public void dadoQueNOExisteUnaListaDeSalasCuandoPidoQueLasMuestreDevuelveUnMensajeDeError_NoHaySalasExistentes() {

        //preparacion
        doThrow(NoHaySalasExistentes.class).when(servicioSala).traerSalas();

        //ejecucion
        ModelAndView modelAndView = controladorInicio.verInicio();

        //verificacion
        assertThat(modelAndView.getModel().get("error").toString(), equalToIgnoringCase("No hay salas existentes."));
    }

    @Test
    public void dadoQueExisteUnaListaDeSalasCuandoPidoQueMuestreUnaSalaPrincipianteEstaTieneUnaDuracionDe10Minutos() {
        //preparacion
        Sala sala1 = new Sala();
        sala1.setId(1);
        sala1.setDuracion(10);

        when(servicioSala.obtenerSalaPorId(1)).thenReturn(sala1);

        //ejecucion
        ModelAndView modelAndView = controladorInicio.verSala(sala1.getId());

        Sala salaObtenida = (Sala) modelAndView.getModel().get("SalaObtenida");

        //verificacion
        assertThat(salaObtenida.getDuracion(), equalTo(10));
        verify(servicioSala).obtenerSalaPorId(1);

    }

    @Test
    public void deberiaDevolverLasSalasPrincipiantesCuandoLasFiltroPorDificultad(){

        Sala sala1 = new  Sala(1, "SALA 1", Dificultad.PRINCIPIANTE, "", "", null, null, null);
        Sala sala2 = new  Sala(2, "SALA 2", Dificultad.INTERMEDIO, "", "", null, null, null);
        Sala sala3 = new  Sala(3, "SALA 3", Dificultad.PRINCIPIANTE, "", "", null, null, null);
        Sala sala4 = new  Sala(4, "SALA 4", Dificultad.AVANZADO, "", "", null, null, null);

        List<Sala> salas = new ArrayList<>();
        salas.add(sala1);
        salas.add(sala3);


        when(servicioSala.obtenerSalaPorDificultad(Dificultad.PRINCIPIANTE)).thenReturn(salas);
        List<Sala> salasObtenidas =  servicioSala.obtenerSalaPorDificultad(Dificultad.PRINCIPIANTE);

        verify(servicioSala).obtenerSalaPorDificultad(Dificultad.PRINCIPIANTE);
        assertThat(salasObtenidas, equalTo(salas));
    }

    @Test
    public void deberiaDevolverTodasLasSalasCuandoLasFiltroPorTodasLasDificultades(){

        Sala sala1 = new  Sala(1, "SALA 1", Dificultad.PRINCIPIANTE, "", "", null, null, null);
        Sala sala2 = new  Sala(2, "SALA 2", Dificultad.INTERMEDIO, "", "", null, null, null);
        Sala sala3 = new  Sala(3, "SALA 3", Dificultad.PRINCIPIANTE, "", "", null, null, null);
        Sala sala4 = new  Sala(4, "SALA 4", Dificultad.AVANZADO, "", "", null, null, null);

        List<Sala> salas = new ArrayList<>();
        salas.add(sala1);
        salas.add(sala2);
        salas.add(sala3);
        salas.add(sala4);


        when(servicioSala.obtenerSalaPorDificultad(null)).thenReturn(salas);
        List<Sala> salasObtenidas =  servicioSala.obtenerSalaPorDificultad(null);

        verify(servicioSala).obtenerSalaPorDificultad(null);
        assertThat(salasObtenidas, equalTo(salas));
    }

}
