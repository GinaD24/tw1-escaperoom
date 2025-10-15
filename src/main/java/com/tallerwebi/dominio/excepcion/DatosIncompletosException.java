package com.tallerwebi.dominio.excepcion;

public class DatosIncompletosException extends Exception {
    public DatosIncompletosException(String mensaje) {
        super(mensaje);
    }
}