package com.tallerwebi.presentacion;

import java.util.List;

import com.tallerwebi.dominio.entidad.PuestoRanking;
import com.tallerwebi.dominio.entidad.Sala;
import com.tallerwebi.dominio.excepcion.SalaSinRanking;
import com.tallerwebi.dominio.interfaz.servicio.ServicioSala;
import com.tallerwebi.dominio.excepcion.SalaInexistente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.tallerwebi.dominio.interfaz.servicio.ServicioRanking;

@Controller
@RequestMapping("/ranking")
public class ControladorRanking {

    private ServicioRanking servicioRanking;
    private ServicioSala servicioSala;

    @Autowired
    public ControladorRanking(ServicioRanking servicioRanking, ServicioSala servicioSala) {

        this.servicioRanking = servicioRanking;
        this.servicioSala = servicioSala;
    }

    @GetMapping("/")
    public ModelAndView verRankings(@RequestParam(defaultValue = "1") Integer idSala){
        ModelMap modelo = new ModelMap();

        try {
            List<PuestoRanking> puestoRankings = servicioRanking.obtenerRankingPorSala(idSala);
            List<Sala> salas = servicioSala.traerSalas();
            Sala sala = servicioSala.obtenerSalaPorId(idSala);
            modelo.put("sala", sala);
            modelo.put("salas", salas);
            modelo.put("rankings", puestoRankings);
            modelo.put("idSala", idSala);
            return new ModelAndView("ranking-sala", modelo);

        } catch(SalaSinRanking e) {
            modelo.put("error", "No hay partidas jugadas aún.");
            return new ModelAndView("ranking-sala", modelo);
        }

    }

    @GetMapping("/filtrarPorSala")
    public ModelAndView filtrarRanking(@RequestParam(value = "filtroSalas", required = false) String filtroSalas) {
        ModelMap modelo = new ModelMap();
        Sala sala = null;
        Integer idSala = Integer.valueOf(filtroSalas);
        List<PuestoRanking> puestoRankings = null;
        List<Sala> salas = servicioSala.traerSalas();

        try{
            sala = servicioSala.obtenerSalaPorId(idSala);
            puestoRankings = servicioRanking.obtenerRankingPorSala(sala.getId());
        }
        catch(SalaInexistente e){
            return new ModelAndView("redirect:/ranking/");
        }
        catch(SalaSinRanking e) {
            sala = servicioSala.obtenerSalaPorId(idSala);
            modelo.put("sala", sala);
            modelo.put("error", "No hay partidas jugadas en esa sala.");
            modelo.put("salas", salas);
            return new ModelAndView("ranking-sala", modelo);
        }

        modelo.put("rankings", puestoRankings);
        modelo.put("salas", salas);
        modelo.put("sala", sala);

        return new ModelAndView("ranking-sala", modelo);
    }


}