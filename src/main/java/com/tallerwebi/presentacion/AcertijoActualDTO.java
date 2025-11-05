package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.enums.TipoAcertijo;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AcertijoActualDTO implements Serializable {

    private Long id;

    private String descripcion;

    private TipoAcertijo tipo;

    private String respuestaCorrecta;

    private List<String> pistas = new ArrayList<>();

    private int pistasUsadas = 0;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public TipoAcertijo getTipo() {
        return tipo;
    }

    public void setTipo(TipoAcertijo tipo) {
        this.tipo = tipo;
    }

    public String getRespuestaCorrecta() {
        return respuestaCorrecta;
    }

    public void setRespuestaCorrecta(String respuestaCorrecta) {
        this.respuestaCorrecta = respuestaCorrecta;
    }

    public List<String> getPistas() {
        return pistas;
    }

    public void setPistas(List<String> pistas) {
        this.pistas = pistas;
    }

    public int getPistasUsadas() {
        return pistasUsadas;
    }

    public void setPistasUsadas(int pistasUsadas) {
        this.pistasUsadas = pistasUsadas;
    }

    public String obtenerSiguientePista() {
        if (pistasUsadas < pistas.size()) {
            String pista = pistas.get(pistasUsadas);
            pistasUsadas++;
            return pista;
        }
        return null;
    }
}