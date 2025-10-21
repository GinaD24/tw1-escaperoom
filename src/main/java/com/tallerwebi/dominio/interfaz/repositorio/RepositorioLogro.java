package com.tallerwebi.dominio.interfaz.repositorio;

import com.tallerwebi.dominio.entidad.Logro;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepositorioLogro {
    Logro buscar(Long id);
    void guardar(Logro logro);
    List<Logro> obtenerTodosLosLogros();
}
