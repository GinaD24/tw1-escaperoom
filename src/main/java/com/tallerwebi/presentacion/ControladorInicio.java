package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidad.Sala;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/inicio")
public class ControladorInicio {

    private ServicioSala servicioSala;
    private ServicioCompra servicioCompra;
    private RepositorioUsuario repositorioUsuario;

    @Autowired
    public ControladorInicio(ServicioSala servicioSala, ServicioCompra servicioCompra, RepositorioUsuario repositorioUsuario) {
        this.servicioSala = servicioSala;
        this.servicioCompra = servicioCompra;
        this.repositorioUsuario = repositorioUsuario;
    }

    @GetMapping("/")
    public ModelAndView verInicio(HttpServletRequest request) {
        ModelMap modelo = new ModelMap();

        try {
            List<Sala> salas = servicioSala.traerSalas();
            List<Map<String, Object>> salasConEstado = new ArrayList<>();

            Long idUsuario = (Long) request.getSession().getAttribute("id_usuario");  // Cambiado a Long
            Usuario usuario = null;
            if (idUsuario != null) {
                usuario = repositorioUsuario.obtenerUsuarioPorId(idUsuario);
            }

            for (Sala s : salas) {
                Map<String, Object> mapa = new HashMap<>();
                mapa.put("id", s.getId());
                mapa.put("nombre", s.getNombre());
                mapa.put("dificultad", s.getDificultad());
                mapa.put("escenario", s.getEscenario());
                mapa.put("historia", s.getHistoria());
                mapa.put("esta_habilitada", s.getEsta_habilitada());
                mapa.put("duracion", s.getDuracion());
                mapa.put("imagen", s.getImagen());
                mapa.put("imagenSala", s.getImagenSala());
                mapa.put("cantidadDeEtapas", s.getCantidadDeEtapas());

                boolean desbloqueada = false;
                if (s.getId() <= 3) {
                    desbloqueada = true;  // Salas originales siempre desbloqueadas
                } else if (usuario != null) {
                    desbloqueada = servicioCompra.salaDesbloqueadaParaUsuario(usuario, s);
                }
                mapa.put("desbloqueada", desbloqueada);

                salasConEstado.add(mapa);
            }

            modelo.put("salas", salasConEstado);
        } catch (NoHaySalasExistentes e) {
            modelo.put("error", "No hay salas existentes.");
        }
        return new ModelAndView("inicio", modelo);
    }

    @GetMapping("/sala/{id}")
    public ModelAndView verSala(@PathVariable Integer id) {
        ModelMap modelo = new ModelMap();

        try {
            Sala sala = servicioSala.obtenerSalaPorId(id);
            modelo.put("SalaObtenida", sala);
        } catch (SalaInexistente e) {
            return new ModelAndView("redirect:/inicio/");
        }

        return new ModelAndView("sala", modelo);
    }

    @GetMapping("/filtrar-salas")
    public ModelAndView filtrarSalas(@RequestParam(value = "filtroDificultad", required = false) String dificultadStr, HttpServletRequest request) {
        ModelMap modelo = new ModelMap();

        Dificultad dificultad = null;
        if (!dificultadStr.isEmpty()) {
            dificultad = Dificultad.valueOf(dificultadStr);
        } else {
            return new ModelAndView("redirect:/inicio/");
        }

        List<Sala> salasPorDificultad = servicioSala.obtenerSalaPorDificultad(dificultad);

        List<Map<String, Object>> salasConEstado = new ArrayList<>();
        Long idUsuario = (Long) request.getSession().getAttribute("id_usuario");  // Cambiado a Long
        Usuario usuario = null;
        if (idUsuario != null) {
            usuario = repositorioUsuario.obtenerUsuarioPorId(idUsuario);
        }

        for (Sala s : salasPorDificultad) {
            Map<String, Object> mapa = new HashMap<>();
            mapa.put("id", s.getId());
            mapa.put("nombre", s.getNombre());
            mapa.put("dificultad", s.getDificultad());
            mapa.put("escenario", s.getEscenario());
            mapa.put("historia", s.getHistoria());
            mapa.put("esta_habilitada", s.getEsta_habilitada());
            mapa.put("duracion", s.getDuracion());
            mapa.put("imagen", s.getImagen());
            mapa.put("imagenSala", s.getImagenSala());
            mapa.put("cantidadDeEtapas", s.getCantidadDeEtapas());

            boolean desbloqueada = false;
            if (s.getId() <= 3) {
                desbloqueada = true;  // Salas originales siempre desbloqueadas
            } else if (usuario != null) {
                desbloqueada = servicioCompra.salaDesbloqueadaParaUsuario(usuario, s);
            }
            mapa.put("desbloqueada", desbloqueada);

            salasConEstado.add(mapa);
        }

        modelo.put("salas", salasConEstado);
        return new ModelAndView("inicio", modelo);
    }
}