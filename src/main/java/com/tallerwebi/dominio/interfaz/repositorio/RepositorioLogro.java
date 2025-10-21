package com.tallerwebi.dominio.interfaz.repositorio;

import com.tallerwebi.dominio.entidad.Logro;

import java.util.List;

public interface RepositorioLogro {
    Logro buscar(Long id);
    void guardar(Logro logro);
    List<Logro> obtenerTodosLosLogros();
}
