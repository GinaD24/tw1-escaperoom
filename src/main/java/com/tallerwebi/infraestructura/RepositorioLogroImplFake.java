package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.interfaz.repositorio.RepositorioLogro;
import com.tallerwebi.dominio.entidad.Logro;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;

@Repository
public class RepositorioLogroImplFake implements RepositorioLogro {


    @Override
    public Logro buscar(Long id) {
        return null;
    }

    @Override
    public void guardar(Logro logro) {
    }



    @Override
    public List<Logro> obtenerTodosLosLogros() {
        //  prueba
        return Arrays.asList(
                new Logro( "Primera victoria", "Ganaste tu primera partida"),
                new Logro( "Velocidad", "Terminaste una sala en menos de 5 minutos"),
                new Logro( "Sin ayuda", "Ganaste la partida sin pistas"),
                new Logro( "Gran observador", "Ganaste la partida sin equivocarte")
        );}



}
