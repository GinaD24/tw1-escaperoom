package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidad.*;
import com.tallerwebi.dominio.enums.TipoAcertijo;
import com.tallerwebi.dominio.excepcion.EtapaInexistente;
import com.tallerwebi.dominio.excepcion.SalaInexistente;
import com.tallerwebi.dominio.excepcion.SesionDeUsuarioExpirada;
import com.tallerwebi.dominio.excepcion.UsuarioInexistente;
import com.tallerwebi.dominio.interfaz.servicio.ServicioDatosPartida;
import com.tallerwebi.dominio.interfaz.servicio.ServicioPartida;
import com.tallerwebi.dominio.interfaz.servicio.ServicioSala;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/partida")
public class ControladorPartida {

    private final ServicioSala servicioSala;
    private final ServicioPartida servicioPartida;
    private final ServicioDatosPartida servicioDatosPartida;
    private final DatosPartidaSesion datosPartidaSesion;


    @Autowired
    public ControladorPartida(ServicioSala servicioSala, ServicioPartida servicioPartida, ServicioDatosPartida servicioDatosPartida, DatosPartidaSesion datosPartidaSesion) {
        this.servicioSala = servicioSala;
        this.servicioPartida = servicioPartida;
        this.servicioDatosPartida = servicioDatosPartida;
        this.datosPartidaSesion = datosPartidaSesion;
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
                Long id_usuario = (Long) request.getSession().getAttribute("id_usuario");
                this.servicioPartida.guardarPartida(partida, id_usuario, idSala);
            } catch(SesionDeUsuarioExpirada | UsuarioInexistente e){
                return new ModelAndView("redirect:/login");
            }

            datosPartidaSesion.setIdSalaActual(idSala);
            datosPartidaSesion.setNumeroEtapaActual(1);

            return new ModelAndView("redirect:/partida/sala" + idSala + "/etapa1");
        } catch (SalaInexistente e) {
            return new ModelAndView("redirect:/inicio/");
        }
    }

    @GetMapping("/sala{idSala}/etapa{numeroEtapa}")
    public ModelAndView mostrarPartida(
            @PathVariable Integer idSala,
            @PathVariable Integer numeroEtapa,
            @SessionAttribute("id_usuario") Long idUsuario) {

        ModelMap modelo = new ModelMap();

        try {
            if (!esSesionValida(datosPartidaSesion, idSala, numeroEtapa)) {
                return redirigirASalaYEtapaActual();
            }

            DatosPartidaDTO dtoDatosPartida = servicioDatosPartida.obtenerDatosDePartida(idSala, numeroEtapa, idUsuario);

            if(datosPartidaSesion.getIdAcertijo() != null && datosPartidaSesion.getIdEtapa().equals(dtoDatosPartida.getEtapa().getId())){
                validarAcertijoEnSesion(dtoDatosPartida);
            }

            actualizarSesion(datosPartidaSesion, dtoDatosPartida);

            if(dtoDatosPartida.getAcertijo().getTipo().equals(TipoAcertijo.DRAG_DROP)){
                List<String> categorias = this.servicioPartida.obtenerCategoriasDelAcertijoDragDrop(dtoDatosPartida.getAcertijo().getId());
                modelo.put("categorias", categorias);
            }

            Partida partida = servicioPartida.obtenerPartidaActivaPorIdUsuario(idUsuario);
            modelo.put("partida", partida);
            modelo.put("salaElegida", dtoDatosPartida.getSala());
            modelo.put("etapa", dtoDatosPartida.getEtapa());
            modelo.put("acertijo", dtoDatosPartida.getAcertijo());

            return new ModelAndView("partida", modelo);

        } catch (SalaInexistente | EtapaInexistente e) {
            return redirigirASalaYEtapaActual();
        }
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
            @SessionAttribute("id_acertijo") Long id_acertijo, @RequestParam String respuesta, @SessionAttribute("id_usuario") Long id_usuario) {
        ModelMap modelo = new ModelMap();

        Sala sala = this.servicioSala.obtenerSalaPorId(idSala);
        Etapa etapa = this.servicioPartida.obtenerEtapaPorNumero(idSala, numeroEtapa);
        Acertijo acertijo = this.servicioPartida.buscarAcertijoPorId(id_acertijo);

        if(acertijo.getTipo().equals(TipoAcertijo.DRAG_DROP)){
            List<String> categorias = this.servicioPartida.obtenerCategoriasDelAcertijoDragDrop(id_acertijo);
            modelo.put("categorias", categorias);
        }

        Partida partida = servicioPartida.obtenerPartidaActivaPorIdUsuario(id_usuario);
        modelo.put("partida", partida);
        modelo.put("salaElegida", sala);
        modelo.put("etapa", etapa);
        modelo.put("acertijo", acertijo);

        if (respuesta.isEmpty()) {
            modelo.put("error", "Completa este campo para continuar.");

        } else if (this.servicioPartida.validarRespuesta(id_acertijo, respuesta, id_usuario).equals(false)) {
            modelo.put("error", "Respuesta incorrecta. Intenta nuevamente.");

        } else {
            Integer cantidadDeEtapasTotales = sala.getCantidadDeEtapas();
            if (cantidadDeEtapasTotales.equals(numeroEtapa)) {
                datosPartidaSesion.setPartidaGanada(true);
                return new ModelAndView("redirect:/partida/finalizarPartida");
            }

            datosPartidaSesion.setNumeroEtapaActual(numeroEtapa + 1);
            return new ModelAndView("redirect:/partida/sala" + idSala + "/etapa" + (numeroEtapa + 1));
        }
        return new ModelAndView("partida", modelo);
    }



    @GetMapping("/finalizarPartida")
    public ModelAndView finalizarPartida(HttpServletRequest request) {
        Integer idSala = datosPartidaSesion.getIdSalaActual();
        Long idUsuario = (Long) request.getSession().getAttribute("id_usuario");

        Boolean ganada = datosPartidaSesion.getPartidaGanada();
        if (ganada == null) ganada = false;

        this.servicioPartida.finalizarPartida(idUsuario, ganada);

        ModelMap modelo = new ModelMap();
        Sala sala = this.servicioSala.obtenerSalaPorId(idSala);
        modelo.put("sala", sala);

        if (ganada) {
            return new ModelAndView("partidaGanada", modelo);

        } else {
            return new ModelAndView("partidaPerdida", modelo);
        }
    }


    @GetMapping("/validarTiempo")
    private ModelAndView validarTiempo(@SessionAttribute("id_usuario") Long id_usuario) {
        datosPartidaSesion.setPartidaGanada(false);
        servicioPartida.validarTiempo(id_usuario);
        return new ModelAndView("redirect:/partida/finalizarPartida");
    }


    @GetMapping("/validar/{idSala}/{numeroEtapa}")
    public ModelAndView accesoInvalido(@PathVariable Integer idSala,
                                       @PathVariable Integer numeroEtapa) {
        return new ModelAndView("redirect:/partida/sala" + idSala + "/etapa" + numeroEtapa);
    }


    private ModelAndView redirigirASalaYEtapaActual() {
        Integer idSalaSesion = datosPartidaSesion.getIdSalaActual();
        Integer numeroEtapaSesion = datosPartidaSesion.getNumeroEtapaActual();
        return new ModelAndView("redirect:/partida/sala" + idSalaSesion + "/etapa" + numeroEtapaSesion);
    }


    private boolean esSesionValida(DatosPartidaSesion datosPartida, Integer idSala, Integer numeroEtapa) {
        Integer idSalaSesion = datosPartida.getIdSalaActual();
        Integer numeroEtapaSesion = datosPartida.getNumeroEtapaActual();

        return idSalaSesion != null
                && idSalaSesion.equals(idSala)
                && numeroEtapaSesion != null
                && numeroEtapaSesion.equals(numeroEtapa);
    }

    private void actualizarSesion(DatosPartidaSesion datosPartidaSesion, DatosPartidaDTO dto) {
        if (datosPartidaSesion.getIdEtapa() == null ||
                !dto.getEtapa().getId().equals(datosPartidaSesion.getIdEtapa())) {
            datosPartidaSesion.limpiarSesionIdEtapaAcertijo();
            datosPartidaSesion.setIdEtapa(dto.getEtapa().getId());
        }

        if (datosPartidaSesion.getIdAcertijo() == null ||
                !dto.getAcertijo().getId().equals(datosPartidaSesion.getIdAcertijo())) {
            datosPartidaSesion.setIdAcertijo(dto.getAcertijo().getId());
        }


    }

    private void validarAcertijoEnSesion(DatosPartidaDTO dtoDatosPartida) {
        Long idAcertijoObtenido = dtoDatosPartida.getAcertijo().getId();
        Long idAcertijoEnSesion = datosPartidaSesion.getIdAcertijo();
        Acertijo acertijoEnSesion = servicioPartida.buscarAcertijoPorId(idAcertijoEnSesion);
        if(!idAcertijoObtenido.equals(idAcertijoEnSesion)){
            dtoDatosPartida.setAcertijo(acertijoEnSesion);
        }
    }
}
