package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.Historial;
import com.tallerwebi.dominio.ServicioHistorial;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/historial")
public class ControladorHistorial {

    private final ServicioHistorial servicio;

    // 3. Spring inyectará la implementación de ServicioHistorial
    public ControladorHistorial(ServicioHistorial servicio) {
        this.servicio = servicio;
    }

    /**
     * POST /historial/registrar
     * Registra una nueva partida en el historial (idealmente usando @RequestBody si viene de un JSON/API)
     */
    @PostMapping("/registrar")
    public String registrar(@ModelAttribute Historial historial) {
        servicio.registrarPartida(historial);
        return "redirect:/historial/ver";
    }


    @GetMapping("/ver")
    public ModelAndView verHistorial() {
        List<Historial> historiales = servicio.traerHistorial();

        ModelAndView modelAndView = new ModelAndView("historial-lista");
        modelAndView.addObject("historiales", historiales);

        return modelAndView;
    }

    @GetMapping("/jugador/{nombreJugador}")
    // @PathVariable mapea la parte de la URL {nombreJugador} a la variable String jugador
    public ModelAndView verHistorialJugador(@PathVariable("nombreJugador") String jugador) {
        List<Historial> historialesJugador = servicio.traerHistorialDeJugador(jugador);

        ModelAndView modelAndView = new ModelAndView("historial-jugador");
        modelAndView.addObject("jugador", jugador);
        modelAndView.addObject("historiales", historialesJugador);

        return modelAndView;
    }
}