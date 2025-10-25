package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidad.*;
import com.tallerwebi.dominio.excepcion.SalaInexistente;
import com.tallerwebi.dominio.interfaz.servicio.ServicioHistorial;
import com.tallerwebi.dominio.interfaz.servicio.ServicioPartida;
import com.tallerwebi.dominio.interfaz.servicio.ServicioPerfil;
import com.tallerwebi.dominio.entidad.Historial;
import com.tallerwebi.dominio.interfaz.servicio.ServicioSala;
import com.tallerwebi.dominio.interfaz.servicio.ServicioHistorial;
import com.tallerwebi.dominio.excepcion.SalaInexistente;
import org.dom4j.rule.Mode;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.util.List;

@Controller
@RequestMapping("/historial")
public class ControladorHistorial {

    private final ServicioPartida servicioPartida;
    private final ServicioSala servicioSala;
    private final ServicioHistorial servicioHistorial;

    public ControladorHistorial(ServicioPartida servicioPartida, ServicioSala servicioSala, ServicioHistorial servicioHistorial) {
        this.servicioPartida = servicioPartida;
        this.servicioSala = servicioSala;
        this.servicioHistorial = servicioHistorial;
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

    @GetMapping("/filtrarPorSala")
    public ModelAndView filtrarHistorial(@RequestParam(value = "filtroSalas", required = false) String filtroSalas) {
        ModelMap modelo = new ModelMap();
        Sala sala = null;
        Integer idSala = Integer.valueOf(filtroSalas);


        try{
            sala = servicioSala.obtenerSalaPorId(idSala);
        }
        catch(SalaInexistente e){
            sala = servicioSala.obtenerSalaPorId(1);
        }
        List<Sala> salas = servicioSala.traerSalas();

        List<Historial> historials = servicioHistorial.obtenerHistorialPorSala(sala.getId());
        modelo.put("historials", historials);
        modelo.put("salas", salas);
        modelo.put("sala", sala);

        return new ModelAndView("ranking-sala", modelo);
    }
}