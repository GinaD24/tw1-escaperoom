package com.tallerwebi.punta_a_punta.vistas;

import com.microsoft.playwright.Page;

public class VistaPartida extends VistaWeb{

    public VistaPartida(Page page) {
        super(page);
    }

    public void irAEtapa(Integer id_sala, Integer numero_etapa) {
        page.navigate("http://localhost:8080/spring/partida/sala" + id_sala + "/etapa" + numero_etapa);
    }


    public void escribirRespuestaAcertijo(String respuesta){
        this.escribirEnElElemento("#input-respuesta", respuesta);
    }

    public void darClickEnEnviar() {
        this.darClickEnElElemento("#btn-enviar");
    }

    public String obtenerTextoAcertijo() {
        return page.textContent("#texto-acertijo").trim();
    }
}
