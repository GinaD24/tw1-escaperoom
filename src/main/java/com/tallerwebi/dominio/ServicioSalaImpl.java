package com.tallerwebi.dominio;

import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class ServicioSalaImpl implements ServicioSala{

    private List<Sala> salas;

    @Override
    public ArrayList<Sala> getSalas() {
        ArrayList<Sala> salas = new ArrayList<>();

        Sala sala1 = new Sala(
                1,
                "La Mansión Misteriosa",
                "Principiante",
                "Mansión",
                "Una noche tormentosa te encuentras atrapado en una vieja mansión llena de acertijos.",
                true,
                null,
                null
        );

        Sala sala2 = new Sala(
                2,
                "El Laboratorio Secreto",
                "Intermedio",
                "Laboratorio",
                "Un científico desaparecido dejó pistas en su laboratorio. ¿Podrás descubrir qué tramaba?",
                true,
                null,
                null
        );


                Sala sala3 = new Sala(
                3,
                "La Cárcel Abandonada",
                "Avanzado",
                "Prisión",
                "Despiertas en una celda oxidada. Solo resolviendo complejos acertijos podrás escapar.",
                false,
                        null,
                        null
        );

        salas.add(sala1);
        salas.add(sala2);
        salas.add(sala3);

        return salas;
    }


    @Override
    public Sala obtenerSalaPorId(Integer id) {
        Sala sala = null;
        List<Sala> salas = getSalas();

        for (Sala s : salas) {
            if(s.getId().equals(id)){
                sala = s;
            }
        }
        return sala;
    }
}
