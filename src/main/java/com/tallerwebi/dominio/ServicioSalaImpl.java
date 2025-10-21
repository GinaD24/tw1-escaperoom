package com.tallerwebi.dominio;

import com.tallerwebi.dominio.entidad.Sala;
import com.tallerwebi.dominio.enums.Dificultad;
import com.tallerwebi.dominio.excepcion.SalaInexistente;
import com.tallerwebi.dominio.interfaz.repositorio.RepositorioSala;
import com.tallerwebi.dominio.interfaz.servicio.ServicioSala;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ServicioSalaImpl implements ServicioSala {

    private RepositorioSala repositorioSala;

    @Autowired
    public ServicioSalaImpl(RepositorioSala repositorioSala) {

        this.repositorioSala = repositorioSala;
    }


    @Override
    @Transactional
    public ArrayList<Sala> traerSalas() {

        return (ArrayList<Sala>) repositorioSala.obtenerSalas();
    }

    @Override
    @Transactional
    public Sala obtenerSalaPorId(Integer id){
        Sala sala = repositorioSala.obtenerSalaPorId(id);

        if(sala == null){
            throw new SalaInexistente();
        }

        return sala;
    }

    @Override
    @Transactional
    public List<Sala> obtenerSalaPorDificultad(Dificultad dificultad) {
        if(dificultad == null){
            return repositorioSala.obtenerSalas(); // trae todas si no hay filtro
        }
        return repositorioSala.obtenerSalasPorDificultad(dificultad);
    }

}


