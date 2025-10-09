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
        Etapa etapaHardcodeada = new Etapa("Lobby", 1, "La puerta hacia la siguiente habitación está bloqueada por un candado, busca la clave en este acertijo.", "candado.png");
        etapaHardcodeada.setId(1L);
        return etapaHardcodeada;
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
