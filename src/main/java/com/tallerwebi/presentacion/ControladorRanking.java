package com.tallerwebi.presentacion;

import java.util.List;

import com.tallerwebi.dominio.Sala;
import com.tallerwebi.dominio.ServicioSala;
import com.tallerwebi.dominio.excepcion.SalaInexistente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.tallerwebi.dominio.Ranking;
import com.tallerwebi.dominio.ServicioRanking;

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
            List<Ranking> rankings = servicioRanking.obtenerRankingPorSala(idSala);
            List<Sala> salas = servicioSala.traerSalas();
            Sala sala = servicioSala.obtenerSalaPorId(idSala);
            modelo.put("sala", sala);
            modelo.put("salas", salas);
            modelo.put("rankings", rankings);
            modelo.put("idSala", idSala);
            return new ModelAndView("ranking-sala", modelo);

        } catch(SalaInexistente e) {
            modelo.put("error", "Sala no encontrada.");
            return new ModelAndView("ranking-sala", modelo);
        }

    }

    @GetMapping("/filtrarPorSala")
    public ModelAndView filtrarRanking(@RequestParam(value = "filtroSalas", required = false) String filtroSalas) {
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

        List<Ranking> rankings = servicioRanking.obtenerRankingPorSala(sala.getId());
        modelo.put("rankings", rankings);
        modelo.put("salas", salas);
        modelo.put("sala", sala);

        return new ModelAndView("ranking-sala", modelo);
    }


}