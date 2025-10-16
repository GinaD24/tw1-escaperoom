package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.CredencialesInvalidasException;
import com.tallerwebi.dominio.excepcion.DatosIncompletosException;
import com.tallerwebi.dominio.excepcion.EdadInvalidaException;
import com.tallerwebi.dominio.excepcion.UsuarioExistente;
import com.tallerwebi.dominio.excepcion.ValidacionInvalidaException;

public interface ServicioLogin {
    Usuario consultarUsuario(String email, String password);
    void registrar(Usuario usuario) throws UsuarioExistente, EdadInvalidaException, DatosIncompletosException, CredencialesInvalidasException, ValidacionInvalidaException;
}