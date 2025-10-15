package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.Historial;
import com.tallerwebi.dominio.ServicioHistorial;
import com.tallerwebi.dominio.ServicioPerfil;
import com.tallerwebi.dominio.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/historial")
public class ControladorHistorial {

    private final ServicioHistorial servicioHistorial;
    private final ServicioPerfil servicioPerfil;

    // 3. Spring inyectará la implementación de ServicioHistorial
    public ControladorHistorial(ServicioHistorial servicioHistorial, ServicioPerfil servicioPerfil) {
        this.servicioHistorial = servicioHistorial;
        this.servicioPerfil = servicioPerfil;
    }

    /**
     * POST /historial/registrar
     * Registra una nueva partida en el historial (idealmente usando @RequestBody si viene de un JSON/API)
     */
    @PostMapping("/registrar")
    public String registrar(@ModelAttribute Historial historial) {
        servicioHistorial.registrarPartida(historial);

        // 1. Obtener el identificador String del objeto Historial
        String emailJugador = historial.getJugador();

        // 2. Necesitamos obtener el ID (Long) del usuario para la redirección.
        // **NOTA:** Tu ServicioPerfil debe tener un método para buscar Usuario por email.
        // Asumiendo que agregaste un método: 'Usuario buscarPorEmail(String email)'

        Usuario usuario = servicioPerfil.buscarPorEmail(emailJugador);

        if (usuario != null && usuario.getId() != null) {
            // 3. Redirigir al perfil/historial usando el ID del usuario
            return "redirect:/perfil/" + usuario.getId() + "/historial";
        } else {
            // Manejo de error o caso donde el usuario no se encuentra
            // Podrías redirigir a una página de éxito genérica o al login
            return "redirect:/inicio";
        }
    }
}