package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.RepositorioSala;
import com.tallerwebi.dominio.Sala;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class RepositorioSalaImpl implements RepositorioSala {

    @Override
    public List<Sala> obtenerSalas() {

            List<Sala> salas = new ArrayList<>();
            salas.add(new Sala(1, "La Mansión Misteriosa", "Principiante", "Mansión", "Una noche tormentosa te encuentras atrapado en una vieja mansión llena de acertijos.", true, null, null));
            salas.add(new Sala(2, "El Laboratorio Secreto", "Intermedio", "Laboratorio", "Un científico desaparecido dejó pistas en su laboratorio. ¿Podrás descubrir qué tramaba?", true, null, null));
            salas.add(new Sala(3, "La Cárcel Abandonada", "Avanzado", "Prisión", "Despiertas en una celda oxidada. Solo resolviendo complejos acertijos podrás escapar.", false, null, null));
            return salas;

    }
}
