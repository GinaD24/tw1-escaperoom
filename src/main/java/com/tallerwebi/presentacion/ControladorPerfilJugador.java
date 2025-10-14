package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.Logro;
import com.tallerwebi.dominio.PerfilJugador;
import com.tallerwebi.dominio.ServicioPerfilJugador;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/perfil")
public class ControladorPerfilJugador {

    private final ServicioPerfilJugador servicioPerfilJugador;

    @Autowired
    public ControladorPerfilJugador(ServicioPerfilJugador servicioPerfilJugador) {
        this.servicioPerfilJugador = servicioPerfilJugador;
    }

    //  Mostrar perfil del jugador
    @GetMapping("/{id}/ver")
    public ModelAndView verPerfil(@PathVariable Long id) {
        ModelMap modelo = new ModelMap();
        try {
            PerfilJugador perfil = servicioPerfilJugador.obtenerPerfil(id);
            modelo.put("perfil", perfil);
            return new ModelAndView("perfil-jugador", modelo);
        } catch (RuntimeException e) {
            modelo.put("error", e.getMessage());
            return new ModelAndView("error", modelo);
        }
    }

    // Mostrar formulario de edición de perfil
    @GetMapping("/{id}/editar")
    public ModelAndView editarPerfil(@PathVariable Long id) {
        ModelMap modelo = new ModelMap();
        try {
            PerfilJugador perfil = servicioPerfilJugador.obtenerPerfil(id);
            List<Logro> logros = servicioPerfilJugador.obtenerTodosLosLogros();
            modelo.put("perfil", perfil);
            modelo.put("logros", logros);
            return new ModelAndView("editar-perfil", modelo);
        } catch (RuntimeException e) {
            modelo.put("error", e.getMessage());
            return new ModelAndView("error", modelo);
        }
    }

    // Guardar cambios de nombre y logros
    @PostMapping("/{id}/actualizar")
    public ModelAndView actualizarPerfil(@PathVariable Long id,
                                         @RequestParam(required = false) String nombre,
                                         @RequestParam(required = false) String nombreArchivo,
                                         @RequestParam(required = false) List<Long> logrosFavoritos) {

        ModelMap modelo = new ModelMap();
        try {
            servicioPerfilJugador.actualizarPerfil(id, nombre, nombreArchivo, logrosFavoritos);
            return new ModelAndView("redirect:/perfil/" + id + "/ver");
        } catch (IllegalArgumentException e) {
            PerfilJugador perfil = servicioPerfilJugador.obtenerPerfil(id);
            List<Logro> logros = servicioPerfilJugador.obtenerTodosLosLogros();
            modelo.put("perfil", perfil);
            modelo.put("logros", logros);
            modelo.put("errorNombre", e.getMessage());
            return new ModelAndView("editar-perfil", modelo);
        } catch (RuntimeException e) {
            modelo.put("error", e.getMessage());
            return new ModelAndView("error", modelo);
        }



    }

    //  Mostrar formulario para cambiar foto de perfil
    @GetMapping("/{id}/editar-foto")
    public ModelAndView editarFoto(@PathVariable Long id) {
        ModelMap modelo = new ModelMap();
        try {
            PerfilJugador perfil = servicioPerfilJugador.obtenerPerfil(id);
            modelo.put("perfil", perfil);
            return new ModelAndView("editar-foto", modelo);
        } catch (RuntimeException e) {
            modelo.put("error", e.getMessage());
            return new ModelAndView("error", modelo);
        }
    }

    // Subir nueva foto de perfil
    @PostMapping("/{id}/actualizar-foto")
    public ModelAndView actualizarFoto(@PathVariable Long id,
                                       @RequestParam("foto") MultipartFile foto) {
        ModelMap modelo = new ModelMap();
        try {
            if (foto.isEmpty()) {
                modelo.put("error", "Debe seleccionar una imagen válida");
                return new ModelAndView("error", modelo);
            }


            String nombreArchivo = foto.getOriginalFilename();
            String rutaDestino = "src/main/webapp/resources/img/" + nombreArchivo;
            foto.transferTo(new File(rutaDestino));

            // Actualiza en la base de datos el nombre del archivo
            servicioPerfilJugador.actualizarFotoPerfil(id, nombreArchivo);

            return new ModelAndView("redirect:/perfil/" + id + "/ver");

        } catch (IOException e) {
            modelo.put("error", "Error al guardar la imagen: " + e.getMessage());
            return new ModelAndView("error", modelo);
        }


    }

    //servi
    @GetMapping("/4/ver")
    public ModelAndView verPerfilPrueba() {
        ModelMap modelo = new ModelMap();
        PerfilJugador perfil = new PerfilJugador(4l,"JugadorPrueba", "pruebafoto.jpg", null );
        Logro logro1 = new Logro("Escapista Novato", "Completó su primera sala");
        Logro logro2 = new Logro("Velocista", "Terminó una sala en menos de 10 minutos");
        perfil.setLogrosFavoritos(List.of(logro1, logro2));
        modelo.put("perfil", perfil);
        return new ModelAndView("perfil-jugador", modelo);
    }
}



