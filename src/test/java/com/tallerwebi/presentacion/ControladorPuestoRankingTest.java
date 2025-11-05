package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidad.PuestoRanking;
import com.tallerwebi.dominio.entidad.Sala;
import com.tallerwebi.dominio.excepcion.SalaSinRanking;
import com.tallerwebi.dominio.interfaz.servicio.ServicioRanking;
import com.tallerwebi.dominio.interfaz.servicio.ServicioSala;
import com.tallerwebi.dominio.excepcion.SalaInexistente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

public class ControladorPuestoRankingTest {

    ControladorRanking controladorRanking;
    ServicioRanking servicioRanking;
    ServicioSala servicioSala;

    @BeforeEach
    public void init() {
        this.servicioRanking = mock(ServicioRanking.class);
        this.servicioSala = mock(ServicioSala.class);
        this.controladorRanking = new ControladorRanking(servicioRanking, servicioSala);
    }

@Test
    public void deberiaMostrarLaVistaDelRankingDeLaSala1Con2Puesto(){
        Integer idSala = 1;
        List<Sala> salas = new ArrayList<>();
        Sala sala = new Sala();
        sala.setId(idSala);
        salas.add(sala);

        List<PuestoRanking> puestosRankings  = new ArrayList<>();
        PuestoRanking puestoRanking1 = new PuestoRanking();
        PuestoRanking puestoRanking2 = new PuestoRanking();
        puestosRankings.add(puestoRanking1);
        puestosRankings.add(puestoRanking2);

        when(servicioSala.traerSalas()).thenReturn((ArrayList<Sala>) salas);
        when(servicioSala.obtenerSalaPorId(idSala)).thenReturn(sala);
        when(servicioRanking.obtenerRankingPorSala(idSala)).thenReturn(puestosRankings);
        ModelAndView modelAndView = controladorRanking.verRankings();

        assertThat(modelAndView.getViewName(), equalTo("ranking-sala"));
        assertThat(modelAndView.getModel().get("sala"), equalTo(sala));
        assertThat(modelAndView.getModel().get("rankings"), equalTo(puestosRankings));
        verify(servicioSala).traerSalas();
        verify(servicioSala).obtenerSalaPorId(idSala);
        verify(servicioRanking).obtenerRankingPorSala(idSala);

}


    @Test
    public void deberiaDevolverUnMensajeDeError_NoHayPartidasJugadasAun_CuandoNoHayNingunPuestoEnLosRankings(){
        Integer idSala = 1;
        List<Sala> salas = new ArrayList<>();
        Sala sala = new Sala();
        sala.setId(idSala);
        salas.add(sala);

        List<PuestoRanking> puestosRankings  = new ArrayList<>();

        doThrow(SalaSinRanking.class).when(servicioRanking).obtenerRankingPorSala(idSala);
        ModelAndView modelAndView = controladorRanking.verRankings();

        assertThat(modelAndView.getModel().get("error"), equalTo("No hay partidas jugadas aún."));
        verify(servicioRanking).obtenerRankingPorSala(idSala);

    }

}
