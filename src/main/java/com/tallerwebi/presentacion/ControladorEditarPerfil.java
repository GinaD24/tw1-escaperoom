package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.DatosEdicionPerfilDTO;
import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.excepcion.DatosIncompletosException;
import com.tallerwebi.dominio.excepcion.ValidacionInvalidaException;
import com.tallerwebi.dominio.excepcion.UsuarioExistente;
import com.tallerwebi.dominio.interfaz.servicio.ServicioEditarPerfil; // Asegúrate de que el paquete sea correcto
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/configuracion")
public class ControladorEditarPerfil {

    private final ServicioEditarPerfil servicioEditarPerfil;

    @Autowired
    public ControladorEditarPerfil(ServicioEditarPerfil servicioEditarPerfil) {
        this.servicioEditarPerfil = servicioEditarPerfil;
    }

    // MUESTRA LA VISTA DE EDICIÓN (GET)
    @RequestMapping(path = "/editar", method = RequestMethod.GET)
    public ModelAndView vistaEditarPerfil(HttpSession session) {
        ModelMap modelo = new ModelMap();
        Long idUsuario = (Long) session.getAttribute("id_usuario"); // Obtener ID de la sesión

        if (idUsuario == null) {
            // Si no hay ID en la sesión, redirigir a login
            return new ModelAndView("redirect:/login");
        }

        try {
            // Carga los datos actuales del usuario al DTO para que se muestren en el formulario
            DatosEdicionPerfilDTO datosActuales = servicioEditarPerfil.obtenerDatosPerfil(idUsuario);
            modelo.put("datosPerfil", datosActuales);
            return new ModelAndView("editar-perfil", modelo);

        } catch (RuntimeException e) {
            // Manejo de error si el servicio no encuentra al usuario
            modelo.put("error", "Error al cargar perfil: " + e.getMessage());
            return new ModelAndView("redirect:/perfil/verPerfil");
        }
    }

    // PROCESA EL FORMULARIO DE EDICIÓN (POST)
    @RequestMapping(path = "/editar", method = RequestMethod.POST)
    public ModelAndView guardarCambios(@ModelAttribute("datosPerfil") DatosEdicionPerfilDTO datos,
                                       HttpSession session) {
        ModelMap modelo = new ModelMap();
        Long idUsuario = (Long) session.getAttribute("id_usuario");

        if (idUsuario == null) {
            return new ModelAndView("redirect:/login");
        }

        // 1. VALIDACIÓN MANUAL CON EL DTO
        try {
            datos.validarDatos();

        } catch (DatosIncompletosException e) {
            modelo.put("error", "Datos incompletos: " + e.getMessage());
            // Recargar el DTO para que se muestre en el formulario y no se pierdan otros datos
            modelo.put("datosPerfil", datos);
            return new ModelAndView("editar-perfil", modelo);
        } catch (ValidacionInvalidaException e) {
            modelo.put("error", "Validación inválida: " + e.getMessage());
            modelo.put("datosPerfil", datos);
            return new ModelAndView("editar-perfil", modelo);
        }

        // 2. LÓGICA DE NEGOCIO (Actualizar)
        try {
            // Asigna el ID del usuario logueado al DTO antes de enviarlo al servicio
            datos.setId(idUsuario);
            servicioEditarPerfil.actualizarPerfil(datos);

            modelo.put("mensaje", "Perfil actualizado exitosamente.");

            // Redirige al perfil de visualización
            return new ModelAndView("redirect:/perfil/verPerfil");

        } catch (UsuarioExistente e) {
            // Error de negocio: Email o nombre de usuario ya existe
            modelo.put("error", "Error de actualización: " + e.getMessage());
            modelo.put("datosPerfil", datos);
            return new ModelAndView("editar-perfil", modelo);

        } catch (RuntimeException e) {
            // Otros errores del servicio (ej. no encontrado)
            modelo.put("error", "Error interno al actualizar: " + e.getMessage());
            modelo.put("datosPerfil", datos);
            return new ModelAndView("editar-perfil", modelo);
        }
    }
}