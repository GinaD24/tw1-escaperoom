package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.DatosEdicionPerfilDTO;
import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.excepcion.DatosIncompletosException;
import com.tallerwebi.dominio.excepcion.ValidacionInvalidaException;
import com.tallerwebi.dominio.excepcion.UsuarioExistente;
import com.tallerwebi.dominio.interfaz.servicio.ServicioEditarPerfil;
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
        Long idUsuario = (Long) session.getAttribute("id_usuario");

        if (idUsuario == null) {
            return new ModelAndView("redirect:/login");
        }

        try {
            DatosEdicionPerfilDTO datosActuales = servicioEditarPerfil.obtenerDatosPerfil(idUsuario);
            modelo.put("datosPerfil", datosActuales);
            return new ModelAndView("editar-perfil", modelo);
        } catch (RuntimeException e) {
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

        System.out.println("Iniciando proceso de guardar cambios para usuario ID: " + idUsuario);
        System.out.println("Datos recibidos - NombreUsuario: " + datos.getNombreUsuario() + ", UrlFotoPerfil: " + datos.getUrlFotoPerfil());

        // 1. VALIDACIÓN MANUAL CON EL DTO
        try {
            System.out.println("Validando datos...");
            datos.validarDatos();
            System.out.println("Validación exitosa.");
        } catch (DatosIncompletosException e) {
            System.out.println("Excepción DatosIncompletosException: " + e.getMessage());
            modelo.put("error", "Datos incompletos: " + e.getMessage());
            modelo.put("datosPerfil", datos);
            return new ModelAndView("editar-perfil", modelo);
        } catch (ValidacionInvalidaException e) {
            System.out.println("Excepción ValidacionInvalidaException: " + e.getMessage());
            modelo.put("error", "Validación inválida: " + e.getMessage());
            modelo.put("datosPerfil", datos);
            return new ModelAndView("editar-perfil", modelo);
        }

        // 2. LÓGICA DE NEGOCIO (Actualizar)
        try {
            System.out.println("Asignando ID y actualizando perfil...");
            datos.setId(idUsuario);
            servicioEditarPerfil.actualizarPerfil(datos);
            System.out.println("Perfil actualizado exitosamente.");
            modelo.put("mensaje", "Perfil actualizado exitosamente.");
            return new ModelAndView("redirect:/perfil/verPerfil");
        } catch (UsuarioExistente e) {
            System.out.println("Excepción UsuarioExistente: " + e.getMessage());
            modelo.put("error", "Error de actualización: " + e.getMessage());
            modelo.put("datosPerfil", datos);
            return new ModelAndView("editar-perfil", modelo);
        } catch (RuntimeException e) {
            System.out.println("Excepción RuntimeException: " + e.getMessage());
            modelo.put("error", "Error interno al actualizar: " + e.getMessage());
            modelo.put("datosPerfil", datos);
            return new ModelAndView("editar-perfil", modelo);
        }
    }
}
