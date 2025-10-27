package com.tallerwebi.punta_a_punta.vistas;

import com.microsoft.playwright.Page;

public class VistaInicio extends VistaWeb{

    public VistaInicio(Page page) {
        super(page);
        page.navigate("localhost:8080/spring/inicio/");
    }

    public void darClickEnLaSala1(){
        this.darClickEnElElemento("#sala-1");
    }


}
