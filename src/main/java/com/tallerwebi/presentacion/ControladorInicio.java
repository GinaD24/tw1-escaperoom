package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.Sala;
import com.tallerwebi.dominio.ServicioSala;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

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
        modelo.put("salas", servicioSala.getSalas());
        return new ModelAndView("inicio", modelo);
    }

    @GetMapping("/sala/{id}")
    public ModelAndView verSala(@PathVariable Integer id) {
        ModelMap modelo = new ModelMap();
        List<Sala> salasObtenidas = servicioSala.getSalas();
        
        Sala sala = servicioSala.obtenerSalaPorId(id);
       
        if(sala != null){
            modelo.put("SalaObtenida", sala);
        }else{
            modelo.put("error", "Sala no encontrada");
        }


        return new ModelAndView("sala", modelo);
    }


}
