package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Acertijo;
import com.tallerwebi.dominio.Etapa;
import com.tallerwebi.dominio.Pista;
import com.tallerwebi.dominio.RepositorioPartida;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RepositorioPartidaImpl implements RepositorioPartida {


    @Override
    public void guardarPartida(Integer id) {

    }

    @Override
    public Etapa obtenerEtapaPorNumero(Integer idSala, Integer numero) {
        return null;
    }

    @Override
    public List<Acertijo> obtenerListaDeAcertijos(Long id) {
        return List.of();
    }

    @Override
    public List<Pista> obtenerListaDePistas(Long id) {
        return List.of();
    }

    @Override
    public Boolean obtenerRespuesta(Long id, String respuesta) {
        return null;
    }


}
