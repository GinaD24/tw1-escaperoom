package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidad.PuestoRankingDTO;
import com.tallerwebi.dominio.excepcion.SalaSinRanking;
import com.tallerwebi.dominio.interfaz.servicio.ServicioRanking;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

public class ControladorPuestoRankingTest {

    ControladorRanking controladorRanking;
    ServicioRanking servicioRanking;

    @BeforeEach
    public void init() {
        this.servicioRanking = mock(ServicioRanking.class);
        this.controladorRanking = new ControladorRanking(servicioRanking);
    }

    @Test
    public void deberiaMostrarLaVistaDelRanking3Puestos(){

        List<PuestoRankingDTO> puestosRankings  = new ArrayList<>();
        PuestoRankingDTO puestoRankingDTO1 = new PuestoRankingDTO();
        PuestoRankingDTO puestoRankingDTO2 = new PuestoRankingDTO();
        PuestoRankingDTO puestoRankingDTO3 = new PuestoRankingDTO();
        puestosRankings.add(puestoRankingDTO1);
        puestosRankings.add(puestoRankingDTO2);
        puestosRankings.add(puestoRankingDTO3);

        when(servicioRanking.obtenerRanking()).thenReturn(puestosRankings);

        ModelAndView modelAndView = controladorRanking.verRankings();

        assertThat(modelAndView.getViewName(), equalTo("ranking-sala"));
        assertThat(modelAndView.getModel().get("rankings"), equalTo(puestosRankings));
        verify(servicioRanking).obtenerRanking();

    }


    @Test
    public void deberiaDevolverUnMensajeDeError_NoHayPartidasJugadasAun_CuandoNoHayNingunPuestoEnLosRankings(){

        doThrow(SalaSinRanking.class).when(servicioRanking).obtenerRanking();


        ModelAndView modelAndView = controladorRanking.verRankings();

        assertThat(modelAndView.getModel().get("error"), equalTo("No hay partidas jugadas aún."));
        verify(servicioRanking).obtenerRanking();

    }

}