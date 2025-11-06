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
import java.util.stream.Collectors;
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
                datosPartidaSesion.limpiarSesionPartida();
                this.servicioPartida.guardarPartida(partida, id_usuario, idSala);
                datosPartidaSesion.setIdPartida(partida.getId());
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

            if(datosPartidaSesion.getAcertijoActual() != null && datosPartidaSesion.getIdEtapa().equals(dtoDatosPartida.getEtapa().getId())){
                validarAcertijoEnSesion(dtoDatosPartida);
            }

            actualizarSesion(datosPartidaSesion, dtoDatosPartida);

            if(dtoDatosPartida.getAcertijo().getTipo().equals(TipoAcertijo.DRAG_DROP) && dtoDatosPartida.getAcertijo().getId() != null){
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

    @GetMapping("/pista")
    @ResponseBody
    public String obtenerPista(@SessionAttribute("id_usuario") Long id_usuario) {

        AcertijoActualDTO acertijoActual = datosPartidaSesion.getAcertijoActual();

        String pistaTexto = acertijoActual.obtenerSiguientePista();

        if (pistaTexto == null) {
            return "Ya no quedan pistas.";
        } else {
            this.servicioPartida.registrarUsoDePista(id_usuario);

            return pistaTexto;
        }
    }
    /*
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
*/
    @PostMapping("/validar/{idSala}/{numeroEtapa}")
    public ModelAndView validarRespuesta(@PathVariable Integer idSala, @PathVariable Integer numeroEtapa,
                                         @RequestParam String respuesta,
                                         @SessionAttribute("id_usuario") Long id_usuario) {

        ModelMap modelo = new ModelMap();

        Partida partida = servicioPartida.obtenerPartidaActivaPorIdUsuario(id_usuario);
        if(tiempoValido(partida) == false){
            return new ModelAndView("redirect:/partida/finalizarPartida");
        }

        AcertijoActualDTO acertijoActual = datosPartidaSesion.getAcertijoActual();

        Acertijo acertijoParaLaVista = obtenerAcertijoDesdeSesion(acertijoActual);

        Sala sala = this.servicioSala.obtenerSalaPorId(idSala);
        Etapa etapa = this.servicioPartida.obtenerEtapaPorNumero(idSala, numeroEtapa);

        if(acertijoParaLaVista.getTipo().equals(TipoAcertijo.DRAG_DROP) && acertijoParaLaVista.getId() != null){
            List<String> categorias = this.servicioPartida.obtenerCategoriasDelAcertijoDragDrop(acertijoParaLaVista.getId());
            modelo.put("categorias", categorias);
        }

        modelo.put("partida", partida);
        modelo.put("salaElegida", sala);
        modelo.put("etapa", etapa);
        modelo.put("acertijo", acertijoParaLaVista);

        Boolean esCorrecta = this.servicioPartida.validarRespuesta(acertijoActual, respuesta, id_usuario);

        if (respuesta.isEmpty()) {
            modelo.put("error", "Completa este campo para continuar.");

        } else if (esCorrecta == false) { // <-- 6. Usamos la variable booleana
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

/*
    @PostMapping("/validar/{idSala}/{numeroEtapa}")
    public ModelAndView validarRespuesta(@PathVariable Integer idSala, @PathVariable Integer numeroEtapa,
            @SessionAttribute("id_acertijo") Long id_acertijo, @RequestParam String respuesta, @SessionAttribute("id_usuario") Long id_usuario) {
        ModelMap modelo = new ModelMap();

        Partida partida = servicioPartida.obtenerPartidaActivaPorIdUsuario(id_usuario);
        if(tiempoValido(partida) == false){
            return new ModelAndView("redirect:/partida/finalizarPartida");
        }

        Sala sala = this.servicioSala.obtenerSalaPorId(idSala);
        Etapa etapa = this.servicioPartida.obtenerEtapaPorNumero(idSala, numeroEtapa);
        Acertijo acertijo = this.servicioPartida.buscarAcertijoPorId(id_acertijo);

        if(acertijo.getTipo().equals(TipoAcertijo.DRAG_DROP)){
            List<String> categorias = this.servicioPartida.obtenerCategoriasDelAcertijoDragDrop(id_acertijo);
            modelo.put("categorias", categorias);
        }

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
*/

    @GetMapping("/finalizarPartida")
    public ModelAndView finalizarPartida(HttpServletRequest request) {
        Integer idSala = datosPartidaSesion.getIdSalaActual();
        Long idUsuario = (Long) request.getSession().getAttribute("id_usuario");

        Boolean ganada = datosPartidaSesion.getPartidaGanada();
        if (ganada == null) ganada = false;

        this.servicioPartida.finalizarPartida(idUsuario, ganada);
        Partida partida = this.servicioPartida.buscarPartidaPorId(datosPartidaSesion.getIdPartida());

        ModelMap modelo = new ModelMap();
        Sala sala = this.servicioSala.obtenerSalaPorId(idSala);
        modelo.put("sala", sala);
        modelo.put("partida", partida);

        if (ganada) {
            return new ModelAndView("partidaGanada", modelo);

        } else {
            return new ModelAndView("partidaPerdida", modelo);
        }
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

    private Boolean tiempoValido(Partida partida) {

        if (partida == null) {
            return false;
        }

        boolean tiempoValido = true;
        if (servicioPartida.tiempoExpirado(partida)) {
            tiempoValido = false;
            datosPartidaSesion.setPartidaGanada(false);
        }
        return tiempoValido;
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
        Etapa etapaActual = dto.getEtapa();
        Acertijo acertijo = dto.getAcertijo();

        if (acertijo == null) {
            return;
        }

        if (datosPartidaSesion.getIdEtapa() == null ||
                !etapaActual.getId().equals(datosPartidaSesion.getIdEtapa())) {

            datosPartidaSesion.limpiarAcertijoActual();
            datosPartidaSesion.setIdEtapa(etapaActual.getId());
        }

        AcertijoActualDTO acertijoActualDTO = datosPartidaSesion.getAcertijoActual();

        if (acertijoActualDTO == null ||
                (acertijo.getId() != null && !acertijo.getId().equals(acertijoActualDTO.getId())) ||
                (acertijo.getId() == null && acertijoActualDTO.getId() != null)) {

            AcertijoActualDTO acertijoNuevoDTO = new AcertijoActualDTO();

            acertijoNuevoDTO.setId(acertijo.getId());
            acertijoNuevoDTO.setDescripcion(acertijo.getDescripcion());
            acertijoNuevoDTO.setTipo(acertijo.getTipo());

            if (acertijo.getTipo() == TipoAcertijo.ADIVINANZA && acertijo.getRespuesta() != null) {
                String respuestaCorrecta = acertijo.getRespuesta().getRespuesta();
                acertijoNuevoDTO.setRespuestaCorrecta(respuestaCorrecta);
            }

            List<String> descPistas = acertijo.getPistas().stream()
                    .map(Pista::getDescripcion)
                    .collect(Collectors.toList());
            acertijoNuevoDTO.setPistas(descPistas);

            datosPartidaSesion.setAcertijoActual(acertijoNuevoDTO);
        }
    }

    private void validarAcertijoEnSesion(DatosPartidaDTO dtoDatosPartida) {
        AcertijoActualDTO acertijoActual = datosPartidaSesion.getAcertijoActual();

        if (acertijoActual == null) {
            return;
        }

        Acertijo acertijoFabricado = new Acertijo();
        acertijoFabricado.setId(acertijoActual.getId());
        acertijoFabricado.setTipo(acertijoActual.getTipo());
        acertijoFabricado.setDescripcion(acertijoActual.getDescripcion());

        Respuesta respuestaFabricada = new Respuesta();
        respuestaFabricada.setRespuesta(acertijoActual.getRespuestaCorrecta());
        respuestaFabricada.setEs_correcta(true);
        respuestaFabricada.setAcertijo(acertijoFabricado);
        acertijoFabricado.setRespuesta(respuestaFabricada);

        int numeroPista = 1;

        for (String descPista : acertijoActual.getPistas()) {
            Pista pistaFabricada = new Pista();
            pistaFabricada.setDescripcion(descPista);
            pistaFabricada.setNumero(numeroPista++);
            pistaFabricada.setAcertijo(acertijoFabricado);
            acertijoFabricado.getPistas().add(pistaFabricada);
        }

        if (acertijoFabricado.getId() != null &&
                (acertijoFabricado.getTipo().equals(TipoAcertijo.DRAG_DROP) ||
                        acertijoFabricado.getTipo().equals(TipoAcertijo.ORDENAR_IMAGEN) ||
                        acertijoFabricado.getTipo().equals(TipoAcertijo.SECUENCIA)))
        {
            Acertijo acertijoConItems = servicioPartida.buscarAcertijoPorId(acertijoFabricado.getId());
            acertijoFabricado.setDragDropItems(acertijoConItems.getDragDropItems());
            acertijoFabricado.setImagenes(acertijoConItems.getImagenes());
        }

        dtoDatosPartida.setAcertijo(acertijoFabricado);
    }

    private Acertijo obtenerAcertijoDesdeSesion(AcertijoActualDTO acertijoActual) {
        if (acertijoActual == null) {
            return null;
        }

        Acertijo acertijoFabricado = new Acertijo();
        acertijoFabricado.setId(acertijoActual.getId());
        acertijoFabricado.setTipo(acertijoActual.getTipo());
        acertijoFabricado.setDescripcion(acertijoActual.getDescripcion());

        Respuesta respuestaFabricada = new Respuesta();
        respuestaFabricada.setRespuesta(acertijoActual.getRespuestaCorrecta());
        respuestaFabricada.setEs_correcta(true);
        respuestaFabricada.setAcertijo(acertijoFabricado);
        acertijoFabricado.setRespuesta(respuestaFabricada);

        int numeroPista = 1;
        for (String descPista : acertijoActual.getPistas()) {
            Pista pistaFabricada = new Pista();
            pistaFabricada.setDescripcion(descPista);
            pistaFabricada.setNumero(numeroPista++);
            pistaFabricada.setAcertijo(acertijoFabricado);
            acertijoFabricado.getPistas().add(pistaFabricada);
        }

        if (acertijoFabricado.getId() != null &&
                (acertijoFabricado.getTipo().equals(TipoAcertijo.DRAG_DROP) ||
                        acertijoFabricado.getTipo().equals(TipoAcertijo.ORDENAR_IMAGEN) ||
                        acertijoFabricado.getTipo().equals(TipoAcertijo.SECUENCIA)))
        {
            Acertijo acertijoConItems = servicioPartida.buscarAcertijoPorId(acertijoFabricado.getId());
            acertijoFabricado.setDragDropItems(acertijoConItems.getDragDropItems());
            acertijoFabricado.setImagenes(acertijoConItems.getImagenes());
        }

        return acertijoFabricado;
    }
}
