package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/perfil")
public class ControladorPerfilJugador {

    private final ServicioPerfil servicioPerfil;

    @Autowired
    public ControladorPerfilJugador(ServicioPerfil servicioPerfil) {
        this.servicioPerfil = servicioPerfil;
    }

    //  Mostrar perfil del jugador
    @GetMapping("/{id}/ver")
    public ModelAndView verPerfil(@PathVariable Long id) {
        ModelMap modelo = new ModelMap();
        try {
            Usuario usuario = servicioPerfil.obtenerPerfil(id);
            modelo.put("perfil", usuario);
            return new ModelAndView("perfil-jugador", modelo);
        } catch (RuntimeException e) {
            modelo.put("error", e.getMessage());
            return new ModelAndView("/inicio/", modelo);
        }
    }

}



