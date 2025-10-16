package com.tallerwebi.dominio.excepcion;

public class UsuarioExistente extends RuntimeException {
    public UsuarioExistente() {
        super("El usuario ya existe.");
    }
    public UsuarioExistente(String mensaje) {
        super(mensaje);
    }
}

