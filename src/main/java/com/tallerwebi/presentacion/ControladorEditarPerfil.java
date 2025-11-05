package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.DatosEdicionPerfilDTO;
import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.excepcion.ContraseniaInvalidaException;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/configuracion")
public class ControladorEditarPerfil {

    private final ServicioEditarPerfil servicioEditarPerfil;

    @Autowired
    public ControladorEditarPerfil(ServicioEditarPerfil servicioEditarPerfil) {
        this.servicioEditarPerfil = servicioEditarPerfil;
    }

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

    @RequestMapping(path = "/editar", method = RequestMethod.POST)
    public ModelAndView guardarCambios(@ModelAttribute("datosPerfil") DatosEdicionPerfilDTO datos,
                                       HttpSession session,
                                       RedirectAttributes atributos) {

        Long idUsuario = (Long) session.getAttribute("id_usuario");

        if (idUsuario == null) {
            return new ModelAndView("redirect:/login");
        }

        // bloque try-catch para validaciones DTO
        try {
            datos.validarDatos();
        } catch (DatosIncompletosException | ValidacionInvalidaException e) {
            atributos.addFlashAttribute("error", "Error de validación: " + e.getMessage());
            atributos.addFlashAttribute("datosPerfil", datos);
            return new ModelAndView("redirect:/configuracion/editar");
        } catch (com.tallerwebi.dominio.excepcion.ContraseniaInvalidaException e) {
            atributos.addFlashAttribute("error", "Error de contraseña: " + e.getMessage());
            atributos.addFlashAttribute("datosPerfil", datos);
            return new ModelAndView("redirect:/configuracion/editar");
        }


        //  bloque try-catch para actualizar el perfil
        try {
            datos.setId(idUsuario);
            servicioEditarPerfil.actualizarPerfil(datos);

            Usuario usuarioActualizado = servicioEditarPerfil.buscarUsuarioPorId(idUsuario);

            session.setAttribute("id_usuario", usuarioActualizado.getId());
            session.setAttribute("nombreUsuario", usuarioActualizado.getNombreUsuario());

            session.setAttribute("urlFotoPerfil", usuarioActualizado.getFotoPerfil());

            atributos.addFlashAttribute("mensaje", "Perfil actualizado exitosamente.");
            return new ModelAndView("redirect:/perfil/verPerfil");

        } catch (UsuarioExistente e) {
            atributos.addFlashAttribute("error", "Error de actualización: " + e.getMessage());
            atributos.addFlashAttribute("datosPerfil", datos);
            return new ModelAndView("redirect:/configuracion/editar");

        } catch (ContraseniaInvalidaException e) {
            atributos.addFlashAttribute("error", "Error de contraseña: " + e.getMessage());
            atributos.addFlashAttribute("datosPerfil", datos);
            return new ModelAndView("redirect:/configuracion/editar");

        } catch (RuntimeException e) {
            atributos.addFlashAttribute("error", "Error interno al actualizar: " + e.getMessage());
            atributos.addFlashAttribute("datosPerfil", datos);
            return new ModelAndView("redirect:/configuracion/editar");
        }
    }
}
