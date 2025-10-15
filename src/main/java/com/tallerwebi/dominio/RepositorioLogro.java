package com.tallerwebi.dominio;

import java.util.List;

public interface RepositorioLogro {
    Logro buscar(Long id);
    void guardar(Logro logro);
    List<Logro> obtenerTodosLosLogros();
}
