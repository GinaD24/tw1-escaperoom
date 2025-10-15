package com.tallerwebi.dominio.excepcion;

public class UsuarioExistente extends Exception {
    public UsuarioExistente() {
        super("El usuario ya existe.");
    }
    public UsuarioExistente(String mensaje) {
        super(mensaje);
    }
}

