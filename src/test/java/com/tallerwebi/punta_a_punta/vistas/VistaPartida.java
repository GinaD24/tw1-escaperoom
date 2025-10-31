package com.tallerwebi.punta_a_punta.vistas;

import com.microsoft.playwright.Page;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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

    public void darClickEnBoton(Long idBoton){
        this.darClickEnElElemento("#btn-" + idBoton);
    }

    public void darClickEnMostrarSecuencia() {
        this.darClickEnElElemento("#btnMostrarSecuencia");
    }

    public void esperarSecuenciaMostrada() {
        page.waitForSelector("#btnMostrarSecuencia:disabled");
    }

    public String obtenerSecuenciaMostrada() {
        return page.inputValue("#ordenCorrecto").trim();
    }

    public void darClickEnBotones(String secuenciaMostrada) {
        List<Long> ordenSecuencia = Arrays.stream(secuenciaMostrada.split(","))
                .map(Long::valueOf)
                .collect(Collectors.toList());
        this.darClickEnElElemento("#btn-" + ordenSecuencia.get(0));
        this.darClickEnElElemento("#btn-" + ordenSecuencia.get(1));
        this.darClickEnElElemento("#btn-" + ordenSecuencia.get(2));
        this.darClickEnElElemento("#btn-" + ordenSecuencia.get(3));
        this.darClickEnElElemento("#btn-" + ordenSecuencia.get(4));
    }
}
