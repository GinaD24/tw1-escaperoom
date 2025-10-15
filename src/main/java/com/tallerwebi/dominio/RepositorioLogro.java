package com.tallerwebi.dominio;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepositorioLogro {
    Logro buscar(Long id);
    void guardar(Logro logro);
    List<Logro> obtenerTodosLosLogros();
}
