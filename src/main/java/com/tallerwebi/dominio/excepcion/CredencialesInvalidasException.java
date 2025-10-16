package com.tallerwebi.dominio.excepcion;

public class CredencialesInvalidasException extends RuntimeException {

    public CredencialesInvalidasException() {
        super("Las credenciales ingresadas son inválidas.");
    }

    public CredencialesInvalidasException(String message) {
        super(message);
    }

}
