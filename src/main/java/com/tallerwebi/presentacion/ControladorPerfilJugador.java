package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.*;
import com.tallerwebi.dominio.excepcion.IDUsuarioInvalido;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
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

    @GetMapping("/verPerfil")
    public ModelAndView verPerfil(HttpSession session) {
        ModelMap modelo = new ModelMap();
        try {
            Long idUsuario = (Long) session.getAttribute("id_usuario");
            Usuario usuario = servicioPerfil.obtenerPerfil(idUsuario);
            modelo.put("perfil", usuario);
            return new ModelAndView("perfil-jugador", modelo);
        } catch (IDUsuarioInvalido e) {
            return new ModelAndView("redirect:/inicio/");
        }
    }

}



