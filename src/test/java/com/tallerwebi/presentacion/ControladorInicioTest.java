package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.Sala;
import com.tallerwebi.dominio.ServicioSala;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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

    Sala sala1 = new Sala();
    Sala sala2 = new Sala();
    Sala sala3 = new Sala();
    //ejecucion
    List<Sala> salas = new ArrayList<Sala>();
    salas.add(sala1);
    salas.add(sala2);
    salas.add(sala3);
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

        when(servicioSala.getSalas()).thenReturn((ArrayList<Sala>) salas);

        ModelAndView modelAndView = controladorInicio.verInicio();

       List<Sala> salasObtenidas = (List<Sala>) modelAndView.getModel().get("salas");

        //verificacion

        assertThat(modelAndView.getViewName(), equalToIgnoringCase("inicio"));
        assertThat(salasObtenidas.size(), equalTo(3));
    }

    @Test
    public void dadoQueExistenSalasCuandoQuieroVerUnaEnEspecificoMeDevuelveLaVistaDeEsaSala() {

        //preparacion
        Sala sala1 = new Sala();
        sala1.setId(1);
        Sala sala2 = new Sala();
        Sala sala3 = new Sala();
        List<Sala> salas = new ArrayList<Sala>();
        salas.add(sala1);
        salas.add(sala2);
        salas.add(sala3);

        when(servicioSala.getSalas()).thenReturn((ArrayList<Sala>) salas);
        when(servicioSala.obtenerSalaPorId(1)).thenReturn(sala1);

        //ejecucion

        ModelAndView modelAndView = controladorInicio.verSala(sala1.getId());

        List<Sala> salasObtenidas = (List<Sala>) modelAndView.getModel().get("salas");
        //verificacion

        assertThat(modelAndView.getModel().get("SalaObtenida"), equalTo(sala1));


    }


    @Test
    public void dadoQueExistenSalasCuandoQuieroVerUnaQueNoExisteDevuelveMensajeDeError_SalaNoEncontrada() {

        //preparacion
        Sala sala1 = new Sala();
        Sala sala2 = new Sala();
        Sala sala3 = new Sala();

        List<Sala> salas = new ArrayList<Sala>();
        salas.add(sala1);
        salas.add(sala2);
        salas.add(sala3);

        when(servicioSala.getSalas()).thenReturn((ArrayList<Sala>) salas);
        when(servicioSala.obtenerSalaPorId(5)).thenReturn(sala1);

        //ejecucion

        ModelAndView modelAndView = controladorInicio.verSala(sala1.getId());

        List<Sala> salasObtenidas = (List<Sala>) modelAndView.getModel().get("salas");
        //verificacion

        assertThat(modelAndView.getModel().get("error").toString(), equalToIgnoringCase("Sala no encontrada"));



    }
}
