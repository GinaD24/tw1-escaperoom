package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidad.Historial;
import com.tallerwebi.dominio.interfaz.servicio.ServicioHistorial;
import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.excepcion.IDUsuarioInvalido;
import com.tallerwebi.dominio.interfaz.servicio.ServicioPerfil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/perfil")
public class ControladorPerfilJugador {

    private final ServicioPerfil servicioPerfil;
    private final ServicioHistorial servicioHistorial;

    @Autowired
    public ControladorPerfilJugador(ServicioPerfil servicioPerfil, ServicioHistorial servicioHistorial) { // <-- 2. Inyección en Constructor
        this.servicioPerfil = servicioPerfil;
        this.servicioHistorial = servicioHistorial;
    }

    @GetMapping("/verPerfil")
    public ModelAndView verPerfil(HttpSession session) {
        ModelMap modelo = new ModelMap();
        try {
            Long idUsuario = (Long) session.getAttribute("id_usuario");
            Usuario usuario = servicioPerfil.obtenerPerfil(idUsuario);
            modelo.put("perfil", usuario);
            return new ModelAndView("perfil-jugador", modelo);
        } catch (IDUsuarioInvalido e) {
            return new ModelAndView("redirect:/inicio/");
        }
    }
    @GetMapping("/{id}/historial")
    public ModelAndView verHistorial(@PathVariable("id") Long id) {
        ModelMap modelo = new ModelMap();
        try {

            Usuario usuario = servicioPerfil.obtenerPerfil(id);
            String identificadorJugador = usuario.getEmail();

            List<Historial> historiales = servicioHistorial.traerHistorialDeJugador(identificadorJugador);

            modelo.put("perfil", usuario);
            modelo.put("historiales", historiales);


            return new ModelAndView("historial", modelo);

        } catch (RuntimeException e) {
            modelo.put("error", "Error al cargar historial: " + e.getMessage());
            return new ModelAndView("perfil-jugador", modelo);
        }
    }
}



