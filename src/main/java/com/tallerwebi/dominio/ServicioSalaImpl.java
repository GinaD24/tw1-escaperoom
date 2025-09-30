package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.SalaInexistente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ServicioSalaImpl implements ServicioSala {

    private RepositorioSala repositorioSala;

    @Autowired
    public ServicioSalaImpl(RepositorioSala repositorioSala) {

        this.repositorioSala = repositorioSala;
    }

    private List<Sala> salas;

    @Override
    public ArrayList<Sala> traerSalas() {

        return (ArrayList<Sala>) repositorioSala.obtenerSalas();
    }

    public Sala obtenerSalaPorId(Integer id){
        Sala sala = null;
        List<Sala> salas = repositorioSala.obtenerSalas();

        if(salas != null) {
            for (Sala s : salas) {
                if (s.getId().equals(id)) {
                    sala = s;
                }
            }
        }

        if(sala == null){
            throw new SalaInexistente();
        }

        return sala;
    }

    @Override
    public List<Sala> obtenerSalaPorDificultad(String dificultad) {
        List<Sala> salasPorDificultad = new ArrayList<>();
        List<Sala> salas = repositorioSala.obtenerSalas();

        if(salas != null) {
            for (Sala s : salas) {
                if (s.getDificultad().equalsIgnoreCase(dificultad)) {
                    salasPorDificultad.add(s);
                }
            }
        }
        return salasPorDificultad;
    }

    @Override
    public void habilitarSala(Sala sala) {
        sala.setEsta_habilitada(true);
    }

    @Override
    public void descontarAcertijo(Sala salaConCincoAcertijos) {
        salaConCincoAcertijos.setCantidadAcertijos(salaConCincoAcertijos.getCantidadAcertijos() - 1);
        salaConCincoAcertijos.descontarTiempo();

    }
}


