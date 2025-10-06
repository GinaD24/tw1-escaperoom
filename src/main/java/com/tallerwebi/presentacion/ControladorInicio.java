package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.Sala;
import com.tallerwebi.dominio.ServicioSala;
import com.tallerwebi.dominio.enums.Dificultad;
import com.tallerwebi.dominio.excepcion.NoHaySalasExistentes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;


@Controller
@RequestMapping("/inicio")
public class ControladorInicio {

    private ServicioSala servicioSala;
    
    @Autowired
    public ControladorInicio(ServicioSala servicioSala) {
        this.servicioSala = servicioSala;
    }

    @GetMapping("/")
    public ModelAndView verInicio() {
        ModelMap modelo = new ModelMap();

        try{
            modelo.put("salas", servicioSala.traerSalas());
        }catch(NoHaySalasExistentes e){
            modelo.put("error", "No hay salas existentes.");
        }
        return new ModelAndView("inicio", modelo);
    }

    @GetMapping("/sala/{id}")
    public ModelAndView verSala(@PathVariable Integer id) {
        ModelMap modelo = new ModelMap();

        Sala sala = servicioSala.obtenerSalaPorId(id);
       
        if(sala != null){
            modelo.put("SalaObtenida", sala);
        }else{
            modelo.put("error", "Sala no encontrada");
        }


        return new ModelAndView("sala", modelo);
    }

    @GetMapping("/filtrar-salas")
    public ModelAndView filtrarSalas(@RequestParam(value = "filtroDificultad", required = false) String dificultadStr) {
        ModelMap modelo = new ModelMap();

        Dificultad dificultad = null;

        if (!dificultadStr.isEmpty()) {
            dificultad = Dificultad.valueOf(dificultadStr);
        }else{
            return new ModelAndView("redirect:/inicio/");
        }

        List<Sala> salasPorDificultad = servicioSala.obtenerSalaPorDificultad(dificultad);

        modelo.put("salas", salasPorDificultad);
        return new ModelAndView("inicio", modelo);
    }

}
