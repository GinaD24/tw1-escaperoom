package com.tallerwebi.presentacion;

import java.util.List;

import com.tallerwebi.dominio.excepcion.NoExisteSala;
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

        if(idSala == null || idSala < 1){
            modelo.put("error", "ID de Sala invalido");
            return new ModelAndView("error-ranking", modelo);
        }

        try {
            List<Ranking> rankings = servicioRanking.obtenerRankingPorSala(idSala);
            modelo.put("rankings", rankings);
            modelo.put("idSala", idSala);

            return new ModelAndView("ranking-sala", modelo);

        } catch(NoExisteSala e) {
            modelo.put("error", "Sala no encontrada.");
            return new ModelAndView("error-ranking", modelo);


        } catch(Exception e) {
        modelo.put("error", "Error interno al cargar los rankings");
        return new ModelAndView("error-ranking", modelo);
    }
    }
}
