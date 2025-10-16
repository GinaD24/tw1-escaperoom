package com.tallerwebi.dominio.excepcion;

public class ValidacionInvalidaException extends RuntimeException {
    public ValidacionInvalidaException(String mensaje) {
        super(mensaje);
    }
}
