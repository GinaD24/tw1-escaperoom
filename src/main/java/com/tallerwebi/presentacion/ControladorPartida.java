package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidad.*;
import com.tallerwebi.dominio.excepcion.EtapaInexistente;
import com.tallerwebi.dominio.excepcion.SalaInexistente;
import com.tallerwebi.dominio.excepcion.SesionDeUsuarioExpirada;
import com.tallerwebi.dominio.excepcion.UsuarioInexistente;
import com.tallerwebi.dominio.interfaz.servicio.ServicioPartida;
import com.tallerwebi.dominio.interfaz.servicio.ServicioSala;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/partida")
public class ControladorPartida {

    private final ServicioSala servicioSala;
    private final ServicioPartida servicioPartida;
    private final DatosPartidaSesion datosPartida;

    @Autowired
    public ControladorPartida(ServicioSala servicioSala, ServicioPartida servicioPartida, DatosPartidaSesion datosPartida) {
        this.servicioSala = servicioSala;
        this.servicioPartida = servicioPartida;
        this.datosPartida = datosPartida;
    }

    @GetMapping("/sala_{idSala}")
    public ModelAndView iniciarPartida(@PathVariable Integer idSala, Partida partida, HttpServletRequest request) {
        try {

            try{
                this.servicioPartida.obtenerEtapaPorNumero(idSala, 1);

            }catch(EtapaInexistente e){
                ModelMap modelo = new ModelMap();
                Sala sala = this.servicioSala.obtenerSalaPorId(idSala);

                modelo.put("error", "Sala en construcción!");
                modelo.put("SalaObtenida", sala);
                return new ModelAndView("sala", modelo);
            }

            try{
                Long idUsuarioSesion = (Long) request.getSession().getAttribute("id_usuario");
                this.servicioPartida.guardarPartida(partida, idUsuarioSesion, idSala);
            } catch(SesionDeUsuarioExpirada | UsuarioInexistente e){
                return new ModelAndView("redirect:/login");
            }

            datosPartida.setIdSalaActual(idSala);
            datosPartida.setNumeroEtapaActual(1);

            return new ModelAndView("redirect:/partida/sala" + idSala + "/etapa1");
        } catch (SalaInexistente e) {
            return new ModelAndView("redirect:/inicio/");
        }
    }


    @GetMapping("/sala{idSala}/etapa{numeroEtapa}")
    public ModelAndView mostrarPartida(@PathVariable Integer idSala, @PathVariable Integer numeroEtapa, @SessionAttribute("id_usuario") Long id_usuario) {

        ModelMap modelo = new ModelMap();
        Integer idSalaSesion = datosPartida.getIdSalaActual();
        Integer numeroEtapaSesion = datosPartida.getNumeroEtapaActual();
        Sala sala = null;
        Etapa etapa;
        Acertijo acertijo;

        try{
        sala = this.servicioSala.obtenerSalaPorId(idSala);
        }catch (SalaInexistente e){
            return redirigirASalaYEtapaActual();
        }

        if (idSalaSesion == null || !idSalaSesion.equals(idSala) ||
                numeroEtapaSesion == null ||  !numeroEtapaSesion.equals(numeroEtapa)) {
            return redirigirASalaYEtapaActual();
        }

        Long idEtapaSesion = datosPartida.getIdEtapa();
        try {
            if (idEtapaSesion == null) {
                etapa = this.servicioPartida.obtenerEtapaPorNumero(idSala, numeroEtapa);
                datosPartida.setIdEtapa(etapa.getId());

            } else {
                etapa = this.servicioPartida.obtenerEtapaPorId(idEtapaSesion);
                if (!etapa.getNumero().equals(numeroEtapa)) {
                    datosPartida.limpiarSesionIdEtapaAcertijo();
                    etapa = this.servicioPartida.obtenerEtapaPorNumero(idSala, numeroEtapa);
                    datosPartida.setIdEtapa(etapa.getId());
                }
            }

        }catch (EtapaInexistente e) {
            return redirigirASalaYEtapaActual();}

        Long idAcertijoSesion = datosPartida.getIdAcertijo();
        if (idAcertijoSesion == null) {
            acertijo = this.servicioPartida.obtenerAcertijo(etapa.getId(), id_usuario);
            datosPartida.setIdAcertijo(acertijo.getId());
        } else {
            acertijo = this.servicioPartida.buscarAcertijoPorId(idAcertijoSesion);}

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
    public ModelAndView validarRespuesta(@PathVariable Integer idSala, @PathVariable Integer numeroEtapa,
            @SessionAttribute("id_acertijo") Long id_acertijo, @RequestParam String respuesta) {
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
            Integer cantidadDeEtapasTotales = sala.getCantidadDeEtapas();
            if (cantidadDeEtapasTotales.equals(numeroEtapa)) {
                Boolean ganada = this.servicioPartida.validarRespuesta(id_acertijo, respuesta);
                datosPartida.setPartidaGanada(ganada);
                return new ModelAndView("redirect:/partida/finalizarPartida");
            }

            datosPartida.setNumeroEtapaActual(numeroEtapa + 1);
            return new ModelAndView("redirect:/partida/sala" + idSala + "/etapa" + (numeroEtapa + 1));
        }
        return new ModelAndView("partida", modelo);
    }



    @GetMapping("/finalizarPartida")
    public ModelAndView finalizarPartida(HttpServletRequest request) {
        Integer idSala = datosPartida.getIdSalaActual();
        Long idUsuario = (Long) request.getSession().getAttribute("id_usuario");

        Boolean ganada = datosPartida.getPartidaGanada();
        if (ganada == null) ganada = false;

        this.servicioPartida.finalizarPartida(idUsuario, ganada);
        datosPartida.limpiarSesionPartida();

        if (ganada) {
            ModelMap modelo = new ModelMap();
            Sala sala = this.servicioSala.obtenerSalaPorId(idSala);
            modelo.put("sala", sala);
            return new ModelAndView("partidaGanada", modelo);

        } else {
            return new ModelAndView("redirect:/inicio/");
        }
    }

    private ModelAndView redirigirASalaYEtapaActual() {
        Integer idSalaSesion = datosPartida.getIdSalaActual();
        Integer numeroEtapaSesion = datosPartida.getNumeroEtapaActual();
        return new ModelAndView("redirect:/partida/sala" + idSalaSesion + "/etapa" + numeroEtapaSesion);
    }

    @GetMapping("/validar/{idSala}/{numeroEtapa}")
    public ModelAndView accesoInvalido(@PathVariable Integer idSala,
                                       @PathVariable Integer numeroEtapa) {
        return new ModelAndView("redirect:/partida/sala" + idSala + "/etapa" + numeroEtapa);
    }


}
