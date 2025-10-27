package com.tallerwebi.punta_a_punta.vistas;

import com.microsoft.playwright.Page;

public class VistaSala extends VistaWeb {

    public VistaSala(Page page) {
        super(page);
        // quitar la navegación automática
        // page.navigate("localhost:8080/spring/inicio/sala/1");
    }

    public void irASala1() {
        page.navigate("http://localhost:8080/spring/inicio/sala/1");
    }

    public void darClickEnEstoyListo(){
        this.darClickEnElElemento("#btn-estoyListo");
    }
}