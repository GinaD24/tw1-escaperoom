package com.tallerwebi.dominio;

import com.tallerwebi.dominio.enums.Dificultad;
import com.tallerwebi.dominio.excepcion.SalaInexistente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class ServicioSalaImplTest {


    RepositorioSala repositorioSala;
    ServicioSalaImpl servicioSala;

    @BeforeEach
    public void init() {
        repositorioSala = mock(RepositorioSala.class);
        servicioSala = new ServicioSalaImpl(repositorioSala);
    }


    @Test void dadoQueExistenSalasCuandoLasConsultoEntoncesSeInvocaRepositorioSalaObtenerSalas() {

        List<Sala> salas = new ArrayList<>();

        salas.add(new  Sala(1, "SALA 1", Dificultad.PRINCIPIANTE, "", "", null, null, null));
        when(repositorioSala.obtenerSalas()).thenReturn(salas);

        List<Sala> salasObtenidas = servicioSala.traerSalas();

        verify(repositorioSala).obtenerSalas();
    }

    @Test void dadoQueExistenSalasCuandoLasConsultoUnaPorIdEntoncesSeInvocaRepositorioSalaObtenerSalas() {

        List<Sala> salas = new ArrayList<>();
        salas.add(new Sala(1, "La Mansión Misteriosa", Dificultad.PRINCIPIANTE, "Mansión", "Una noche tormentosa te encuentras atrapado en una vieja mansión llena de acertijos.", true, null, null));
        salas.add(new Sala(2, "El Laboratorio Secreto", Dificultad.INTERMEDIO, "Laboratorio", "Un científico desaparecido dejó pistas en su laboratorio. ¿Podrás descubrir qué tramaba?", true, null, null));
        salas.add(new Sala(3, "La Cárcel Abandonada", Dificultad.AVANZADO, "Prisión", "Despiertas en una celda oxidada. Solo resolviendo complejos acertijos podrás escapar.", false, null, null));
        salas.add(new Sala(4, "Hospital Embrujado", Dificultad.PRINCIPIANTE, "Hospital", "Explora un hospital abandonado donde cada pieza es un fragmento de la trágica historia de sus antiguos pacientes", true, null, null));

        when(repositorioSala.obtenerSalaPorId(2)).thenReturn(salas.get(1));

        Sala salaObtenida = servicioSala.obtenerSalaPorId(2);

        verify(repositorioSala).obtenerSalaPorId(2);
    }

    @Test void dadoQueExistenSalasCuandoSeConsultanLasPrincipiantesEntoncesSeInvocaRepositorioSalaObtenerSalas() {

        List<Sala> salas = new ArrayList<>();
        salas.add(new Sala(1, "La Mansión Misteriosa", Dificultad.PRINCIPIANTE, "Mansión", "Una noche tormentosa te encuentras atrapado en una vieja mansión llena de acertijos.", true, null, null));
        salas.add(new Sala(2, "El Laboratorio Secreto", Dificultad.INTERMEDIO, "Laboratorio", "Un científico desaparecido dejó pistas en su laboratorio. ¿Podrás descubrir qué tramaba?", true, null, null));
        salas.add(new Sala(3, "La Cárcel Abandonada", Dificultad.INTERMEDIO, "Prisión", "Despiertas en una celda oxidada. Solo resolviendo complejos acertijos podrás escapar.", false, null, null));
        salas.add(new Sala(4, "Hospital Embrujado", Dificultad.PRINCIPIANTE, "Hospital", "Explora un hospital abandonado donde cada pieza es un fragmento de la trágica historia de sus antiguos pacientes", true, null, null));

        when(repositorioSala.obtenerSalasPorDificultad(Dificultad.PRINCIPIANTE)).thenReturn(Arrays.asList(salas.get(0), salas.get(3)));

        List<Sala> salasObtenidas = servicioSala.obtenerSalaPorDificultad(Dificultad.PRINCIPIANTE);

        verify(repositorioSala).obtenerSalasPorDificultad(Dificultad.PRINCIPIANTE);
    }

    @Test
    public void deberiaDevolverLaSala1CuandoLePasoElId1(){

        Sala sala1 = new  Sala(1, "SALA 1", Dificultad.PRINCIPIANTE, "", "", null, null, null);

        when(repositorioSala.obtenerSalaPorId(1)).thenReturn(sala1);
        Sala salaObtenida = servicioSala.obtenerSalaPorId(1);

        assertThat(salaObtenida, is(sala1));
        verify(repositorioSala).obtenerSalaPorId(1);
    }

    @Test
    public void deberiaDevolverUnaListaCon3SalasCuandoLasPido(){
        Sala sala1 = new  Sala(1, "SALA 1", Dificultad.PRINCIPIANTE, "", "", null, null, null);
        Sala sala2 = new  Sala(2, "SALA 2", Dificultad.INTERMEDIO, "", "", null, null, null);
        Sala sala3 = new  Sala(3, "SALA 3", Dificultad.AVANZADO, "", "", null, null, null);

        List<Sala> salas = new ArrayList<>();
        salas.add(sala1);
        salas.add(sala2);
        salas.add(sala3);

        when(repositorioSala.obtenerSalas()).thenReturn(salas);
        List<Sala> salasObtenidas = servicioSala.traerSalas();

        assertThat(salasObtenidas.size(), is(3));
        verify(repositorioSala).obtenerSalas();

    }

    @Test
    public void deberiaLanzarSalaInexistenteCuandoLaSalaNoExiste(){

        when(repositorioSala.obtenerSalaPorId(5)).thenReturn(null);

        assertThrows(SalaInexistente.class, () -> {servicioSala.obtenerSalaPorId(5);});
        verify(repositorioSala).obtenerSalaPorId(5);


    }

    @Test
    public void deberiaDevolverSalasPrincipiantesCuandoLeEspecificoQueSeanDeNivelPrincipiante(){
        Sala sala1 = new  Sala(1, "SALA 1", Dificultad.PRINCIPIANTE, "", "", null, null, null);
        Sala sala2 = new  Sala(2, "SALA 2", Dificultad.INTERMEDIO, "", "", null, null, null);
        Sala sala3 = new  Sala(3, "SALA 3", Dificultad.PRINCIPIANTE, "", "", null, null, null);
        Sala sala4 = new  Sala(4, "SALA 4", Dificultad.AVANZADO, "", "", null, null, null);

        List<Sala> salas = new ArrayList<>();
        salas.add(sala1);
        salas.add(sala2);
        salas.add(sala3);
        salas.add(sala4);

        when(repositorioSala.obtenerSalasPorDificultad(Dificultad.PRINCIPIANTE)).thenReturn(Arrays.asList(salas.get(0), salas.get(2)));
        List<Sala> salasObtenidas =  servicioSala.obtenerSalaPorDificultad(Dificultad.PRINCIPIANTE);

        assertThat(salasObtenidas.size(), is(2));
        assertThat(salasObtenidas.get(0), is(sala1));
        assertThat(salasObtenidas.get(1), is(sala3));

        verify(repositorioSala).obtenerSalasPorDificultad(Dificultad.PRINCIPIANTE);

    }

}
