package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.excepcion.IDUsuarioInvalido;
import com.tallerwebi.dominio.interfaz.servicio.ServicioPerfil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;


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
            if(idUsuario == null) {
                return new ModelAndView("redirect:/login");
            }

            Usuario usuario = servicioPerfil.obtenerPerfil(idUsuario);

            if (usuario.getFotoPerfil() != null &&
                    !usuario.getFotoPerfil().startsWith("/img/") &&
                    !usuario.getFotoPerfil().contains("/uploads/")) {

                usuario.setFotoPerfil("/img/" + usuario.getFotoPerfil());
            }

            modelo.put("usuario", usuario);
            return new ModelAndView("perfil-jugador", modelo);
        } catch (IDUsuarioInvalido e) {
            return new ModelAndView("redirect:/inicio/");
        }
    }
}