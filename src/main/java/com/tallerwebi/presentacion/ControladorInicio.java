package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidad.Sala;
import com.tallerwebi.dominio.entidad.SalaVista;
import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.interfaz.repositorio.RepositorioUsuario;
import com.tallerwebi.dominio.interfaz.servicio.ServicioCompra;
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
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/inicio")
public class ControladorInicio {

    private ServicioSala servicioSala;
    private ServicioCompra servicioCompra;  // Agregar
    private RepositorioUsuario repositorioUsuario;  // Agregar

    @Autowired
    public ControladorInicio(ServicioSala servicioSala, ServicioCompra servicioCompra, RepositorioUsuario repositorioUsuario) {
        this.servicioSala = servicioSala;
        this.servicioCompra = servicioCompra;
        this.repositorioUsuario = repositorioUsuario;
    }

    @GetMapping("/")
    public ModelAndView verInicio(HttpServletRequest request) {  // Agregar HttpServletRequest
        ModelMap modelo = new ModelMap();

        try {
            List<Sala> salas = servicioSala.traerSalas();
            List<SalaVista> salasVista = new ArrayList<>();

            // Obtener usuario de la sesión
            Long idUsuario = (Long) request.getSession().getAttribute("id_usuario");
            Usuario usuario = null;
            if (idUsuario != null) {
                usuario = repositorioUsuario.obtenerUsuarioPorId(idUsuario);
            }

            for (Sala sala : salas) {
                boolean desbloqueada = (usuario != null) && servicioCompra.salaDesbloqueadaParaUsuario(usuario, sala);
                salasVista.add(new SalaVista(sala, desbloqueada));
            }

            modelo.put("salas", salasVista);
        } catch (NoHaySalasExistentes e) {
            modelo.put("error", "No hay salas existentes.");
        }
        return new ModelAndView("inicio", modelo);
    }


    @GetMapping("/sala/{id}")
    public ModelAndView verSala(@PathVariable Integer id) {
        ModelMap modelo = new ModelMap();

        try{
            Sala sala = servicioSala.obtenerSalaPorId(id);
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
        List<SalaVista> salasVista = new ArrayList<>();

        // Obtener usuario de la sesión (igual que verInicio)
        Long idUsuario = (Long) request.getSession().getAttribute("id_usuario");
        Usuario usuario = null;
        if (idUsuario != null) {
            usuario = repositorioUsuario.obtenerUsuarioPorId(idUsuario);
        }

        for (Sala sala : salasFiltradas) {
            if (sala == null || sala.getNombre() == null) {
                continue;  // Salta salas inválidas
            }
            boolean desbloqueada = (usuario != null) && servicioCompra.salaDesbloqueadaParaUsuario(usuario, sala);
            salasVista.add(new SalaVista(sala, desbloqueada));
        }

        modelo.put("salas", salasVista);
        return new ModelAndView("inicio", modelo);
    }
}