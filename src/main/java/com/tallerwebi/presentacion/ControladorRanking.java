package com.tallerwebi.presentacion;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.tallerwebi.dominio.Ranking;
import com.tallerwebi.dominio.ServicioRanking;

@Controller
@RequestMapping("/ranking")
public class ControladorRanking {

    private ServicioRanking servicioRanking;

    @Autowired
    public ControladorRanking(ServicioRanking servicioRanking){
        this.servicioRanking = servicioRanking;
    }

    @GetMapping("/sala/{id}")
    public ModelAndView verRankings(@PathVariable("id") Integer idSala){
        ModelMap modelo = new ModelMap();
        List<Ranking> rankings = servicioRanking.obtenerRankingPorSala(idSala);

        modelo.put("rankings", rankings);
        modelo.put("idSala", idSala);

        return new ModelAndView("ranking-sala", modelo);


    }



}
