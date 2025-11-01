package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidad.*;
import com.tallerwebi.dominio.interfaz.servicio.ServicioHistorial;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.util.List;

@Controller
@RequestMapping("/historial")
public class ControladorHistorial {


    private final ServicioHistorial servicioHistorial;


    public ControladorHistorial( ServicioHistorial servicioHistorial) {
        this.servicioHistorial = servicioHistorial;

    }

    @GetMapping("/")
    public ModelAndView verHistorial(@SessionAttribute Long id_usuario) {
        ModelMap modelo = new ModelMap();
        try {

            List <Partida> historial = servicioHistorial.traerHistorialDeJugador(id_usuario);
            modelo.put("historial", historial);

            return new ModelAndView("historial", modelo);

        } catch (RuntimeException e) {
            modelo.put("error", "Error al cargar historial: " + e.getMessage());
            return new ModelAndView("perfil-jugador", modelo);
        }
    }

}