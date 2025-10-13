package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.*;
import com.tallerwebi.dominio.excepcion.EtapaInexistente;
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
    public ModelAndView iniciarPartida(@PathVariable Integer idSala, Partida partida, HttpServletRequest request) {
        try {
            Sala sala = this.servicioSala.obtenerSalaPorId(idSala);
            partida.setSala(sala);

            this.servicioPartida.guardarPartida(partida, (Long) request.getSession().getAttribute("id_usuario"));
            request.getSession().setAttribute("id_sala_actual", sala.getId());
            request.getSession().setAttribute("numero_etapa_actual", 1);

            return new ModelAndView("redirect:/partida/sala" + idSala + "/etapa1");
        } catch (SalaInexistente e) {
            return new ModelAndView("redirect:/inicio/");
        }
    }


    @GetMapping("/sala{idSala}/etapa{numeroEtapa}")
    public ModelAndView mostrarPartida(@PathVariable Integer idSala, @PathVariable Integer numeroEtapa,
                                       @SessionAttribute("id_usuario") Long id_usuario, HttpServletRequest request) {

        ModelMap modelo = new ModelMap();
        Integer idSalaSesion = (Integer) request.getSession().getAttribute("id_sala_actual");
        Integer numeroEtapaSesion = (Integer) request.getSession().getAttribute("numero_etapa_actual");
        Sala sala = null;
        try {
             sala = this.servicioSala.obtenerSalaPorId(idSala);
        }catch (SalaInexistente e){
            modelo.put("error", "Sala en construcción!");
            return new ModelAndView("redirect:/partida/sala" + idSalaSesion + "/etapa" + numeroEtapaSesion);
        }
        Etapa etapa = null;
        Acertijo acertijo;

        if (idSalaSesion == null || !idSalaSesion.equals(idSala) ||
                numeroEtapaSesion == null ||  !numeroEtapaSesion.equals(numeroEtapa)) {
            return new ModelAndView("redirect:/partida/sala" + idSalaSesion + "/etapa" + numeroEtapaSesion);
        }

        Long idEtapaSesion = (Long) request.getSession().getAttribute("id_etapa");

        try {
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
        }catch (EtapaInexistente e) {
            modelo.put("SalaObtenida", sala);
            modelo.put("error", "Sala en construcción!");
            return new ModelAndView("sala", modelo);
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
            @PathVariable Integer idSala, @PathVariable Integer numeroEtapa,
            @SessionAttribute("id_acertijo") Long id_acertijo, @RequestParam String respuesta, HttpServletRequest request) {
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
            Integer cantidadDeEtapasTotales = this.servicioPartida.obtenerCantidadDeEtapas(idSala);
            if (cantidadDeEtapasTotales.equals(numeroEtapa)) {
                this.servicioPartida.finalizarPartida((Long) request.getSession().getAttribute("id_usuario"));
                return new ModelAndView("partidaGanada", modelo);
            }
            request.getSession().setAttribute("numero_etapa_actual", numeroEtapa + 1);
            return new ModelAndView("redirect:/partida/sala" + idSala + "/etapa" + (numeroEtapa + 1));
        }

        return new ModelAndView("partida", modelo);
    }

    @GetMapping("/validar/{idSala}/{numeroEtapa}")
    public ModelAndView accesoInvalido(@PathVariable Integer idSala,
                                       @PathVariable Integer numeroEtapa) {
        return new ModelAndView("redirect:/partida/sala" + idSala + "/etapa" + numeroEtapa);
    }


}
