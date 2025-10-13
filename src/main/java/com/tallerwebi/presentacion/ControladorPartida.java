package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.*;
import com.tallerwebi.dominio.excepcion.SalaInexistente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

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
    public ModelAndView mostrarPartida(@PathVariable Integer idSala, @PathVariable Integer numeroEtapa,
                                       @SessionAttribute("id_usuario") Long id_usuario, HttpServletRequest request) {
        ModelMap modelo = new ModelMap();
        Sala sala = this.servicioSala.obtenerSalaPorId(idSala);
        Etapa etapa = null;
        Acertijo acertijo;

        Long idEtapaSesion = (Long) request.getSession().getAttribute("id_etapa");

        if (idEtapaSesion == null) {
            etapa = this.servicioPartida.obtenerEtapaPorNumero(idSala, numeroEtapa);
            request.getSession().setAttribute("id_etapa", etapa.getId());
        } else {
            etapa = this.servicioPartida.obtenerEtapaPorId(idEtapaSesion);

            if (!etapa.getNumero().equals(numeroEtapa)) {
                request.getSession().removeAttribute("id_etapa");
                request.getSession().removeAttribute("id_acertijo");
                etapa = this.servicioPartida.obtenerEtapaPorNumero(idSala, numeroEtapa);
                request.getSession().setAttribute("id_etapa", etapa.getId());
            }
        }


        Long idAcertijoSesion = (Long) request.getSession().getAttribute("id_acertijo");
        if (idAcertijoSesion == null) {
            acertijo = this.servicioPartida.obtenerAcertijo(etapa.getId(), id_usuario);
            request.getSession().setAttribute("id_acertijo", acertijo.getId());
        } else {
            acertijo = this.servicioPartida.buscarAcertijoPorId(idAcertijoSesion);
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


    @PostMapping("/validar/{idSala}/{numeroEtapa}")
    public ModelAndView validarRespuesta(
            @PathVariable Integer idSala,
            @PathVariable Integer numeroEtapa,
            @SessionAttribute("id_acertijo") Long id_acertijo,
            @RequestParam String respuesta
    ) {
        ModelMap modelo = new ModelMap();

        Sala sala = this.servicioSala.obtenerSalaPorId(idSala);
        Etapa etapa = this.servicioPartida.obtenerEtapaPorNumero(idSala, numeroEtapa);
        Acertijo acertijo = this.servicioPartida.buscarAcertijoPorId(id_acertijo);
        modelo.put("salaElegida", sala);
        modelo.put("etapa", etapa);
        modelo.put("acertijo", acertijo);

        if (respuesta.isEmpty()) {
            modelo.put("error", "Completa este campo para continuar.");
        } else if (this.servicioPartida.validarRespuesta(id_acertijo, respuesta).equals(false)) {
            modelo.put("error", "Respuesta incorrecta. Intenta nuevamente.");
        } else {
            return new ModelAndView("redirect:/partida/sala" + idSala + "/etapa" + (numeroEtapa + 1));
        }

        return new ModelAndView("partida", modelo);
    }




}
