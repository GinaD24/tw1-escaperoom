package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidad.Sala;
import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.excepcion.SesionDeUsuarioExpirada;
import com.tallerwebi.dominio.excepcion.UsuarioInexistente;
import com.tallerwebi.dominio.interfaz.servicio.ServicioCompra;
import com.tallerwebi.dominio.interfaz.servicio.ServicioLogin;
import com.tallerwebi.dominio.interfaz.servicio.ServicioSala;

import com.tallerwebi.dominio.enums.Dificultad;
import com.tallerwebi.dominio.excepcion.NoHaySalasExistentes;
import com.tallerwebi.dominio.excepcion.SalaInexistente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/inicio")
public class ControladorInicio {

    private ServicioSala servicioSala;
    private ServicioCompra servicioCompra;
    private ServicioLogin servicioLogin;

    @Autowired
    public ControladorInicio(ServicioSala servicioSala, ServicioCompra servicioCompra, ServicioLogin servicioLogin) {
        this.servicioSala = servicioSala;
        this.servicioCompra = servicioCompra;
        this.servicioLogin = servicioLogin;
    }

    @GetMapping("/")
    public ModelAndView verInicio(HttpServletRequest request) {
        ModelMap modelo = new ModelMap();

        try {
            List<Sala> salas = servicioSala.traerSalas();

            Long idUsuario = (Long) request.getSession().getAttribute("id_usuario");
            Usuario usuario = null;
            if (idUsuario != null) {
                usuario = servicioLogin.buscarUsuarioPorId(idUsuario);
            }

            for (Sala sala : salas) {
                if(sala.getEs_paga()){
                    boolean desbloqueada = (usuario != null) && servicioCompra.salaDesbloqueadaParaUsuario(usuario, sala);
                    sala.setEsta_habilitada(desbloqueada);
                }
            }

            modelo.put("salas", salas);
        } catch (NoHaySalasExistentes e) {
            modelo.put("error", "No hay salas existentes.");
        }
        return new ModelAndView("inicio", modelo);
    }


    @GetMapping("/sala/{id}")
    public ModelAndView verSala(@PathVariable Integer id, HttpServletRequest request) {
        ModelMap modelo = new ModelMap();
        Long id_usuario = null;
        Usuario usuario = null;

        try{
            id_usuario = (Long) request.getSession().getAttribute("id_usuario");
            usuario = servicioLogin.buscarUsuarioPorId(id_usuario);

        } catch(SesionDeUsuarioExpirada e){
            return new ModelAndView("redirect:/login");
        }

        try{
            Sala sala = servicioSala.obtenerSalaPorId(id);

            if(sala.getEs_paga()){
                boolean desbloqueada = (usuario != null) && servicioCompra.salaDesbloqueadaParaUsuario(usuario, sala);
                sala.setEsta_habilitada(desbloqueada);
            }
            modelo.put("SalaObtenida", sala);

        }catch(SalaInexistente e){

            return new ModelAndView("redirect:/inicio/");
        }

        return new ModelAndView("sala", modelo);
    }

    @GetMapping("/filtrar-salas")
    public ModelAndView filtrarSalas(@RequestParam(value = "filtroDificultad", required = false) String dificultadStr, HttpServletRequest request) {  // Agrega HttpServletRequest
        ModelMap modelo = new ModelMap();

        Dificultad dificultad = null;

        if (dificultadStr != null && !dificultadStr.isEmpty()) {
            dificultad = Dificultad.valueOf(dificultadStr);
        } else {
            return new ModelAndView("redirect:/inicio/");
        }

        List<Sala> salasFiltradas = servicioSala.obtenerSalaPorDificultad(dificultad);


        Long idUsuario = (Long) request.getSession().getAttribute("id_usuario");
        Usuario usuario = null;
        if (idUsuario != null) {
            usuario = servicioLogin.buscarUsuarioPorId(idUsuario);
        }

        for (Sala sala : salasFiltradas) {
            if( sala != null && sala.getEs_paga() && sala.getNombre() != null){
                boolean desbloqueada = (usuario != null) && servicioCompra.salaDesbloqueadaParaUsuario(usuario, sala);
                sala.setEsta_habilitada(desbloqueada);
            }

        }

        modelo.put("salas", salasFiltradas);
        return new ModelAndView("inicio", modelo);
    }
}