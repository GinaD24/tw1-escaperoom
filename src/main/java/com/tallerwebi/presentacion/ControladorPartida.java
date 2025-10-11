package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.*;
import com.tallerwebi.dominio.excepcion.SalaInexistente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/partida")
public class ControladorPartida {

    ServicioSala servicioSala;
    ServicioPartida servicioPartida;

    @Autowired
    public ControladorPartida(ServicioSala servicioSala, ServicioPartida servicioPartida) {
        this.servicioSala = servicioSala;
        this.servicioPartida = servicioPartida;
    }

    @GetMapping("/sala_{idSala}")
    public ModelAndView iniciarPartida(@PathVariable Integer idSala, Partida partida) {
        try {
            Sala sala = this.servicioSala.obtenerSalaPorId(idSala);
            partida.setSala(sala);
            this.servicioPartida.guardarPartida(partida);

            return new ModelAndView("redirect:/partida/sala" + idSala + "/etapa1");
        } catch (SalaInexistente e) {
            return new ModelAndView("redirect:/inicio/");
        }
    }


    @GetMapping("/sala{idSala}/etapa{numeroEtapa}")
    public ModelAndView mostrarPartida(@PathVariable Integer idSala, @PathVariable Integer numeroEtapa, @SessionAttribute("id_usuario") Long id_usuario) {
        ModelMap modelo = new ModelMap();

        Sala sala = this.servicioSala.obtenerSalaPorId(idSala);
        Etapa etapa = this.servicioPartida.obtenerEtapaPorNumero(idSala, numeroEtapa);
        Acertijo acertijo = null;
        if (etapa != null) {
            acertijo = this.servicioPartida.obtenerAcertijo(etapa.getId(), id_usuario);

        }

        modelo.put("salaElegida", sala);
        modelo.put("etapa", etapa);
        modelo.put("acertijo", acertijo);

        return new ModelAndView("partida", modelo);
    }

    @GetMapping("/acertijo/{idAcertijo}/pista")
    @ResponseBody
    public String obtenerPista(@PathVariable Long idAcertijo, @SessionAttribute("id_usuario") Long id_usuario) {
        Pista pista = this.servicioPartida.obtenerSiguientePista(idAcertijo, id_usuario);
        String pistaTexto = "";

        if(pista == null){
            pistaTexto = "Ya no quedan pistas.";
        }else{
           pistaTexto = pista.getDescripcion();
        }

        return pistaTexto;
    }


    @PostMapping("/validar/{idSala}/{numeroEtapa}/{idAcertijo}")
    public ModelAndView validarRespuesta(
            @PathVariable Integer idSala,
            @PathVariable Integer numeroEtapa,
            @PathVariable Long idAcertijo,
            @RequestParam String respuesta,
            @SessionAttribute("id_usuario") Long id_usuario
    ) {

        if (respuesta.isEmpty()) {
            ModelAndView modelAndView = mostrarPartida(idSala, numeroEtapa, id_usuario);
            modelAndView.addObject("error", "Completa este campo para continuar.");
            return modelAndView;
        }else{


            boolean esCorrecta = this.servicioPartida.validarRespuesta(idAcertijo, respuesta);


            if (esCorrecta) {
                //Pasar a la siguiente etapa
                return new ModelAndView("redirect:/partida/sala" + idSala + "/etapa" + (numeroEtapa + 1));
            } else {
                ModelAndView modelAndView = mostrarPartida(idSala, numeroEtapa, id_usuario);
                modelAndView.addObject("error", "Respuesta incorrecta. Intenta nuevamente.");
                return modelAndView;
            }
        }


    }



}
