package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidad.Compra;
import com.tallerwebi.dominio.entidad.Sala;
import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.interfaz.repositorio.RepositorioUsuario;
import com.tallerwebi.dominio.interfaz.servicio.ServicioCompra;
import com.tallerwebi.dominio.interfaz.servicio.ServicioSala;
import com.tallerwebi.dominio.interfaz.repositorio.RepositorioCompra;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/compra")
public class ControladorCompra {

    private ServicioCompra servicioCompra;
    private ServicioSala servicioSala;
    private RepositorioUsuario repositorioUsuario;

    @Autowired
    public ControladorCompra(ServicioCompra servicioCompra, ServicioSala servicioSala, RepositorioUsuario repositorioUsuario) {
        this.servicioCompra = servicioCompra;
        this.servicioSala = servicioSala;
        this.repositorioUsuario = repositorioUsuario;
    }

    @PostMapping("/iniciar/{idSala}")
    public ModelAndView iniciarCompra(@PathVariable Integer idSala, HttpServletRequest request) {
        Long idUsuario = (Long) request.getSession().getAttribute("id_usuario");
        if (idUsuario == null) {
            return new ModelAndView("redirect:/login");
        }
        Usuario usuario = repositorioUsuario.obtenerUsuarioPorId(idUsuario);
        Sala sala = servicioSala.obtenerSalaPorId(idSala);

        if (servicioCompra.salaDesbloqueadaParaUsuario(usuario, sala)) {
            return new ModelAndView("redirect:/inicio/sala/" + idSala);
        }

        String initPoint = servicioCompra.iniciarCompra(usuario, sala);

        return new ModelAndView("redirect:" + initPoint);
    }

    @GetMapping("/exito")
    public ModelAndView exito(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("mensaje", "¡Pago exitoso! La sala será desbloqueada en breve.");
        return new ModelAndView("redirect:/inicio/");
    }

    @GetMapping("/fallo")
    public ModelAndView fallo(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("mensaje", "El pago falló. Inténtalo de nuevo.");
        return new ModelAndView("redirect:/inicio/");
    }

    @GetMapping("/pendiente")
    public ModelAndView pendiente(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("mensaje", "El pago está pendiente de aprobación.");
        return new ModelAndView("redirect:/inicio/");
    }

    @GetMapping("/confirmacion")
    public RedirectView confirmarPago(
            @RequestParam(name = "payment_id") String paymentId,
            @RequestParam(name = "status") String status,
            @RequestParam(name = "external_reference") String externalReference,
            RedirectAttributes redirectAttributes) {

        try {
            System.out.println("=== CONFIRMACIÓN DE PAGO ===");
            System.out.println("Payment ID: " + paymentId);
            System.out.println("Status: " + status);
            System.out.println("External Reference: " + externalReference);

            if ("approved".equals(status)) {
                try {
                    servicioCompra.confirmarCompraPorExternalReference(externalReference, paymentId);
                    redirectAttributes.addFlashAttribute("mensaje", "¡Pago exitoso! La sala ha sido desbloqueada.");
                    return new RedirectView("/inicio/", true);
                } catch (RuntimeException e) {
                    System.err.println("✗ " + e.getMessage());
                    redirectAttributes.addFlashAttribute("mensaje", "Error: No se encontró la compra.");
                    return new RedirectView("/inicio/", true);
                }
            } else {
                System.out.println("✗ Pago no aprobado. Status: " + status);
                redirectAttributes.addFlashAttribute("mensaje", "El pago no fue aprobado.");
                return new RedirectView("/inicio/", true);
            }

        } catch (Exception e) {
            System.err.println("✗ Error al procesar la confirmación: " + e.getMessage());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("mensaje", "Error al procesar el pago.");
            return new RedirectView("/inicio/", true);
        }
    }
}
