package com.tallerwebi.dominio.excepcion;

public class DatosIncompletosException extends RuntimeException {
    public DatosIncompletosException(String mensaje) {
        super(mensaje);
    }
}