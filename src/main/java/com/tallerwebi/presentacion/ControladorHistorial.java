package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidad.Historial;
import com.tallerwebi.dominio.entidad.Partida;
import com.tallerwebi.dominio.interfaz.servicio.ServicioHistorial;
import com.tallerwebi.dominio.interfaz.servicio.ServicioPartida;
import com.tallerwebi.dominio.interfaz.servicio.ServicioPerfil;
import com.tallerwebi.dominio.entidad.Usuario;
import org.dom4j.rule.Mode;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.util.List;

@Controller
@RequestMapping("/historial")
public class ControladorHistorial {

    private final ServicioPartida servicioPartida;

    public ControladorHistorial(ServicioPartida servicioPartida) {
        this.servicioPartida = servicioPartida;
    }

    @GetMapping("/verHistorial")
    public ModelAndView obtenerHistorial(@SessionAttribute(value = "idUsuario", required = false) Long idUsuario) {

        if (idUsuario == null) {
            return new ModelAndView("redirect:/login");
        }

        List<Partida> historialPartidas = servicioPartida.obtenerHistorialDePartida(idUsuario);

        ModelAndView modelAndView = new ModelAndView("historial");
        modelAndView.addObject("historial", historialPartidas);

        return modelAndView;
    }
}