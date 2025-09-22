package com.tallerwebi.dominio;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ServicioSalaImplTest {


    RepositorioSala repositorioSala;
    ServicioSalaImpl servicioSala;

    @BeforeEach
    public void init() {
        repositorioSala = mock(RepositorioSala.class);
        servicioSala = new ServicioSalaImpl(repositorioSala);
    }

    @Test
    public void deberiaDevolverLaSala1CuandoLePasoElId1(){

        Sala sala1 = new  Sala(1, "SALA 1", "Principiante", "", "", null, null, null);
        List<Sala> salas = new ArrayList<>();
        salas.add(sala1);

        when(repositorioSala.obtenerSalas()).thenReturn(salas);
        Sala salaObtenida = servicioSala.obtenerSalaPorId(1);

        assertThat(salaObtenida, is(sala1));
    }

    @Test
    public void deberiaDevolverUnaListaCon3SalasCuandoLasPido(){
        Sala sala1 = new  Sala(1, "SALA 1", "Principiante", "", "", null, null, null);
        Sala sala2 = new  Sala(2, "SALA 2", "Intermedio", "", "", null, null, null);
        Sala sala3 = new  Sala(3, "SALA 3", "Avanzado", "", "", null, null, null);

        List<Sala> salas = new ArrayList<>();
        salas.add(sala1);
        salas.add(sala2);
        salas.add(sala3);

        when(repositorioSala.obtenerSalas()).thenReturn(salas);
        List<Sala> salasObtenidas = servicioSala.traerSalas();

        assertThat(salasObtenidas.size(), is(3));

    }

    @Test
    public void deberiaDevolverNullCuandoLaSalaNoExiste(){
        Sala sala1 = new  Sala(1, "SALA 1", "Principiante", "", "", null, null, null);
        List<Sala> salas = new ArrayList<>();
        salas.add(sala1);

        when(repositorioSala.obtenerSalas()).thenReturn(salas);
        Sala salaObtenida = servicioSala.obtenerSalaPorId(5);

        assertThat(salaObtenida, is((Sala)null));

    }

    @Test
    public void deberiaDevolverSalasPrincipiantesCuandoLeEspecificoQueSeanDeNivelPrincipiante(){
        Sala sala1 = new  Sala(1, "SALA 1", "Principiante", "", "", null, null, null);
        Sala sala2 = new  Sala(2, "SALA 2", "Intermedio", "", "", null, null, null);
        Sala sala3 = new  Sala(3, "SALA 3", "Principiante", "", "", null, null, null);
        Sala sala4 = new  Sala(4, "SALA 4", "Avanzado", "", "", null, null, null);

        List<Sala> salas = new ArrayList<>();
        salas.add(sala1);
        salas.add(sala2);
        salas.add(sala3);
        salas.add(sala4);

        when(repositorioSala.obtenerSalas()).thenReturn(salas);
        List<Sala> salasObtenidas =  servicioSala.obtenerSalaPorDificultad("Principiante");

        assertThat(salasObtenidas.size(), is(2));
        assertThat(salasObtenidas.get(0), is(sala1));
        assertThat(salasObtenidas.get(1), is(sala3));

    }


}
