package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.SalaInexistente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.ArrayList;
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
        salas.add(new  Sala(1, "SALA 1", "Principiante", "", "", null, null, null));
        when(repositorioSala.obtenerSalas()).thenReturn(salas);

        List<Sala> salasObtenidas = servicioSala.traerSalas();

        verify(repositorioSala).obtenerSalas();
    }

    @Test void dadoQueExistenSalasCuandoLasConsultoUnaPorIdEntoncesSeInvocaRepositorioSalaObtenerSalas() {

        List<Sala> salas = new ArrayList<>();
        salas.add(new Sala(1, "La Mansión Misteriosa", "Principiante", "Mansión", "Una noche tormentosa te encuentras atrapado en una vieja mansión llena de acertijos.", true, null, null));
        salas.add(new Sala(2, "El Laboratorio Secreto", "Intermedio", "Laboratorio", "Un científico desaparecido dejó pistas en su laboratorio. ¿Podrás descubrir qué tramaba?", true, null, null));
        salas.add(new Sala(3, "La Cárcel Abandonada", "Avanzado", "Prisión", "Despiertas en una celda oxidada. Solo resolviendo complejos acertijos podrás escapar.", false, null, null));
        salas.add(new Sala(4, "Hospital Embrujado", "Principiante", "Hospital", "Explora un hospital abandonado donde cada pieza es un fragmento de la trágica historia de sus antiguos pacientes", true, null, null));

        when(repositorioSala.obtenerSalas()).thenReturn(salas);

        Sala salasObtenida = servicioSala.obtenerSalaPorId(2);

        verify(repositorioSala).obtenerSalas();
    }

    @Test void dadoQueExistenSalasCuandoSeConsultanLasPrincipiantesEntoncesSeInvocaRepositorioSalaObtenerSalas() {

        List<Sala> salas = new ArrayList<>();
        salas.add(new Sala(1, "La Mansión Misteriosa", "Principiante", "Mansión", "Una noche tormentosa te encuentras atrapado en una vieja mansión llena de acertijos.", true, null, null));
        salas.add(new Sala(2, "El Laboratorio Secreto", "Intermedio", "Laboratorio", "Un científico desaparecido dejó pistas en su laboratorio. ¿Podrás descubrir qué tramaba?", true, null, null));
        salas.add(new Sala(3, "La Cárcel Abandonada", "Avanzado", "Prisión", "Despiertas en una celda oxidada. Solo resolviendo complejos acertijos podrás escapar.", false, null, null));
        salas.add(new Sala(4, "Hospital Embrujado", "Principiante", "Hospital", "Explora un hospital abandonado donde cada pieza es un fragmento de la trágica historia de sus antiguos pacientes", true, null, null));

        when(repositorioSala.obtenerSalas()).thenReturn(salas);

        List<Sala> salasObtenidas = servicioSala.obtenerSalaPorDificultad("Principiante");

        verify(repositorioSala).obtenerSalas();
    }

    @Test
    public void deberiaDevolverLaSala1CuandoLePasoElId1(){

        Sala sala1 = new  Sala(1, "SALA 1", "Principiante", "", "", null, null, null);
        List<Sala> salas = new ArrayList<>();
        salas.add(sala1);

        when(repositorioSala.obtenerSalas()).thenReturn(salas);
        Sala salaObtenida = servicioSala.obtenerSalaPorId(1);

        assertThat(salaObtenida, is(sala1));
        verify(repositorioSala).obtenerSalas();
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
        verify(repositorioSala).obtenerSalas();

    }

    @Test
    public void deberiaLanzarSalaInexistenteCuandoLaSalaNoExiste(){
        Sala sala1 = new  Sala(1, "SALA 1", "Principiante", "", "", null, null, null);
        List<Sala> salas = new ArrayList<>();
        salas.add(sala1);

        when(repositorioSala.obtenerSalas()).thenReturn(salas);

        assertThrows(SalaInexistente.class, () -> {servicioSala.obtenerSalaPorId(5);});
        verify(repositorioSala).obtenerSalas();


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

        verify(repositorioSala).obtenerSalas();

    }

    @Test
    public void dadoQueExistenSalasCuandoConsultoYObtengoLasSalasPrincipiantesPuedoConsultarElEscenarioDeAlguna(){
        List<Sala> salas = new ArrayList<>();
        salas.add(new Sala(1, "La Mansión Misteriosa", "Principiante", "Mansión", "Una noche tormentosa te encuentras atrapado en una vieja mansión llena de acertijos.", true, null, null));
        salas.add(new Sala(2, "El Laboratorio Secreto", "Intermedio", "Laboratorio", "Un científico desaparecido dejó pistas en su laboratorio. ¿Podrás descubrir qué tramaba?", true, null, null));
        salas.add(new Sala(3, "La Cárcel Abandonada", "Avanzado", "Prisión", "Despiertas en una celda oxidada. Solo resolviendo complejos acertijos podrás escapar.", false, null, null));
        salas.add(new Sala(4, "Hospital Embrujado", "Principiante", "Hospital", "Explora un hospital abandonado donde cada pieza es un fragmento de la trágica historia de sus antiguos pacientes", true, null, null));

        when(repositorioSala.obtenerSalas()).thenReturn(salas);

        List<Sala> salasDeNivelPrincipiante = this.servicioSala.obtenerSalaPorDificultad("Principiante");

        Sala sala = salasDeNivelPrincipiante.get(1);
        String escenarioObtenido = sala.getEscenario();

        assertThat(salasDeNivelPrincipiante.size(), is(2));
        assertThat(escenarioObtenido, is("Hospital"));

        verify(repositorioSala).obtenerSalas();

    }

    @Test
    public void dadoQueExistenSalasCuandoUnaEstaDeshabilitadaPuedoHabilitarlaBuscandolaPorId(){

        List<Sala> salas = new ArrayList<>();
        salas.add(new Sala(1, "La Mansión Misteriosa", "Principiante", "Mansión", "Una noche tormentosa te encuentras atrapado en una vieja mansión llena de acertijos.", true, null, null));
        salas.add(new Sala(2, "El Laboratorio Secreto", "Intermedio", "Laboratorio", "Un científico desaparecido dejó pistas en su laboratorio. ¿Podrás descubrir qué tramaba?", true, null, null));
        salas.add(new Sala(3, "La Cárcel Abandonada", "Avanzado", "Prisión", "Despiertas en una celda oxidada. Solo resolviendo complejos acertijos podrás escapar.", false, null, null));
        salas.add(new Sala(4, "Hospital Embrujado", "Principiante", "Hospital", "Explora un hospital abandonado donde cada pieza es un fragmento de la trágica historia de sus antiguos pacientes", true, null, null));

        when(repositorioSala.obtenerSalas()).thenReturn(salas);

        Sala salaDeshabilitada = this.servicioSala.obtenerSalaPorId(3);
        Boolean estadoInicial = salaDeshabilitada.getEsta_habilitada();

        this.servicioSala.habilitarSalaPorId(3);

        Boolean estadoFinal = salaDeshabilitada.getEsta_habilitada();

        assertThat(estadoInicial, is(false));
        assertThat(estadoFinal, is(true));

        verify(repositorioSala).obtenerSalas();

    }

//    @Test
//    public void dadoQueExistenSalasDeTodosLosNivelesCuandoSolicitoLasSalasDeNivelExpertoObtengoUnaListaVacia(){
//        Sala sala1 = new  Sala(1, "SALA 1", "Principiante", "", "", null, null, null);
//        Sala sala2 = new  Sala(2, "SALA 2", "Intermedio", "", "", null, null, null);
//        Sala sala3 = new  Sala(4, "SALA 4", "Avanzado", "", "", null, null, null);
//
//        List<Sala> salas = new ArrayList<>();
//        salas.add(sala1);
//        salas.add(sala2);
//        salas.add(sala3);
//
//        when(repositorioSala.obtenerSalas()).thenReturn(salas);
//
//        List<Sala> salasDeNivelAvanzado = this.servicioSala.obtenerSalaPorDificultad("Experto");
//
//        assertThat(salasDeNivelAvanzado.size(), is(0));
//
//    }

    @Test
    public void dadoQueExistenCuatroSalasHabilitadasCuandoAUnaSalaLeSolicitoDescontarUnAcertijoObtengoUnResultadoExitoso() {
        List<Sala> salas = new ArrayList<>();
        salas.add(new Sala(1, "La Mansión Misteriosa", "Principiante", "Mansión", "Una noche tormentosa te encuentras atrapado en una vieja mansión llena de acertijos.", true, 5, Duration.ofHours(1)));
        salas.add(new Sala(2, "El Laboratorio Secreto", "Intermedio", "Laboratorio", "Un científico desaparecido dejó pistas en su laboratorio. ¿Podrás descubrir qué tramaba?", true, 5, Duration.ofHours(1)));
        salas.add(new Sala(3, "La Cárcel Abandonada", "Avanzado", "Prisión", "Despiertas en una celda oxidada. Solo resolviendo complejos acertijos podrás escapar.", false, 5, Duration.ofHours(1)));
        salas.add(new Sala(4, "Hospital Embrujado", "Principiante", "Hospital", "Explora un hospital abandonado donde cada pieza es un fragmento de la trágica historia de sus antiguos pacientes", true, 5, Duration.ofHours(1)));

        when(repositorioSala.obtenerSalas()).thenReturn(salas);

        Sala salaConCincoAcertijos = this.servicioSala.obtenerSalaPorId(2);

        Integer cantidadInicialDeAcertijos = salaConCincoAcertijos.getCantidadAcertijos();

        this.servicioSala.descontarAcertijo(salaConCincoAcertijos);

        Integer cantidadFinalDeAcertijos = salaConCincoAcertijos.getCantidadAcertijos();

        assertThat(cantidadInicialDeAcertijos, is(5));
        assertThat(cantidadFinalDeAcertijos, is(4));

    }

    @Test
    public void dadoQueExistenCuatroSalasHabilitadasCuandoAUnaSalaLeDescuentoUnAcertijoEntoncesSeDescuentanTresMinutosDeSuDuracion() {
        List<Sala> salas = new ArrayList<>();
        salas.add(new Sala(1, "La Mansión Misteriosa", "Principiante", "Mansión", "Una noche tormentosa te encuentras atrapado en una vieja mansión llena de acertijos.", true, 5, Duration.ofHours(1)));
        salas.add(new Sala(2, "El Laboratorio Secreto", "Intermedio", "Laboratorio", "Un científico desaparecido dejó pistas en su laboratorio. ¿Podrás descubrir qué tramaba?", true, 5, Duration.ofHours(1)));
        salas.add(new Sala(3, "La Cárcel Abandonada", "Avanzado", "Prisión", "Despiertas en una celda oxidada. Solo resolviendo complejos acertijos podrás escapar.", false, 5, Duration.ofHours(1)));
        salas.add(new Sala(4, "Hospital Embrujado", "Principiante", "Hospital", "Explora un hospital abandonado donde cada pieza es un fragmento de la trágica historia de sus antiguos pacientes", true, 5, Duration.ofHours(1)));

        when(repositorioSala.obtenerSalas()).thenReturn(salas);

        Sala salaConCincoAcertijos = this.servicioSala.obtenerSalaPorId(2);
        Duration duracionInicial = salaConCincoAcertijos.getDuracion();
        this.servicioSala.descontarAcertijo(salaConCincoAcertijos);
        Duration duracionFinal = salaConCincoAcertijos.getDuracion();

        assertThat(duracionInicial, is(equalTo(Duration.ofHours(1))));
        assertThat(duracionFinal,  is(equalTo(Duration.ofMinutes(57))));

    }

}
