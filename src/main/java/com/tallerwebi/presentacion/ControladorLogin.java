package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.interfaz.servicio.ServicioLogin;
import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.excepcion.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ControladorLogin {

    private ServicioLogin servicioLogin;

    @Autowired
    public ControladorLogin(ServicioLogin servicioLogin){
        this.servicioLogin = servicioLogin;
    }

    @RequestMapping("/login")
    public ModelAndView irALogin() {

        ModelMap modelo = new ModelMap();
        modelo.put("datosLogin", new DatosLogin());
        return new ModelAndView("login", modelo);
    }

    @RequestMapping(path = "/validar-login", method = RequestMethod.POST)
    public ModelAndView validarLogin(@ModelAttribute("datosLogin") DatosLogin datosLogin, HttpServletRequest request) {
        ModelMap model = new ModelMap();

        try{
            Usuario usuarioBuscado = servicioLogin.consultarUsuario(datosLogin.getEmail(), datosLogin.getPassword());
            request.getSession().setAttribute("id_usuario", usuarioBuscado.getId());
            return new ModelAndView("redirect:/inicio/");
        }catch(CredencialesInvalidasException e){
            model.put("error", "Usuario o clave incorrecta");
        }

        return new ModelAndView("login", model);
    }

    @RequestMapping(path = "/registrarme", method = RequestMethod.POST)
    public ModelAndView registrarme(@ModelAttribute("usuario") Usuario usuario, HttpServletRequest request) {
        ModelMap model = new ModelMap();
        try {

            String confirmPassword = request.getParameter("confirmPassword");
            if (!usuario.getPassword().equals(confirmPassword)) {
                model.put("errorPassword", "Las contraseñas no coinciden.");
                return new ModelAndView("registro", model);
            }
            servicioLogin.registrar(usuario);
            return new ModelAndView("redirect:/login", model);

        } catch (UsuarioExistente | ValidacionInvalidaException | DatosIncompletosException | EdadInvalidaException e) {
            model.put("error", e.getMessage());
            return new ModelAndView("registro", model);

        } catch (Exception e) {
            model.put("error", "Error inesperado al registrar el usuario");
            return new ModelAndView("registro", model);

        }
    }

    @RequestMapping(path = "/irARegistro", method = RequestMethod.GET)
    public ModelAndView registrarse() {
        ModelMap model = new ModelMap();
        model.put("usuario", new Usuario());
        return new ModelAndView("registro", model);
    }

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public ModelAndView inicio() {
        return new ModelAndView("redirect:/login");
    }
}

