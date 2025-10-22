package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidad.Sala;
import com.tallerwebi.dominio.entidad.Etapa;
import com.tallerwebi.dominio.entidad.Acertijo;

public class DatosPartidaDTO {

    private final Sala sala;
    private final Etapa etapa;
    private Acertijo acertijo;

    public DatosPartidaDTO(Sala sala, Etapa etapa, Acertijo acertijo) {
            this.sala = sala;
            this.etapa = etapa;
            this.acertijo = acertijo;
        }

        public Sala getSala() {
            return sala;
        }

        public Etapa getEtapa() {
            return etapa;
        }

        public Acertijo getAcertijo() {
            return acertijo;
        }

    public void setAcertijo(Acertijo acertijo) {
        this.acertijo = acertijo;
    }
}

