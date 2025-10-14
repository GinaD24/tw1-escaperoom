package com.tallerwebi.presentacion;


import com.tallerwebi.dominio.RepositorioUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ControladorDebug {

    private final RepositorioUsuario repositorioUsuario;

    @Autowired
    public ControladorDebug(RepositorioUsuario repositorioUsuario) {
        this.repositorioUsuario = repositorioUsuario;
    }

    @GetMapping("/crear-usuario-prueba")
    @Transactional
    public ModelAndView crearUsuarioDePrueba() {
        // Cast manual, solo si necesitás usar métodos propios del impl
        if (repositorioUsuario instanceof com.tallerwebi.infraestructura.RepositorioUsuarioImpl) {
            com.tallerwebi.infraestructura.RepositorioUsuarioImpl impl =
                    (com.tallerwebi.infraestructura.RepositorioUsuarioImpl) repositorioUsuario;
            impl.crearUsuarioDePrueba();
        }

        return new ModelAndView("debug-creado");
    }
}