package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.interfaz.servicio.ServicioLogin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
public class ControladorLogout {

    ServicioLogin servicioLogin;

    @Autowired
    public ControladorLogout(ServicioLogin servicioLogin) {
        this.servicioLogin = servicioLogin;
    }

    @RequestMapping(path = "/cerrar-sesion", method = RequestMethod.GET)
    public String elLogout(HttpServletRequest request) {

        HttpSession session = request.getSession(false);
        if (session != null) {
            Usuario usuario = servicioLogin.buscarUsuarioPorId((Long) session.getAttribute("id_usuario"));
            servicioLogin.actualizarUsuarioActivo(usuario, false);
            session.invalidate();
        }

        return "redirect:/login";
    }
}
