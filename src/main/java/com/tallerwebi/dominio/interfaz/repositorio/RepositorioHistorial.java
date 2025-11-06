package com.tallerwebi.dominio.interfaz.repositorio;

import com.tallerwebi.dominio.entidad.Partida;

import javax.transaction.Transactional;
import java.util.List;

@Transactional

public interface RepositorioHistorial {
    List<Partida> obtenerPartidasPorJugador(Long idUsuario);

}