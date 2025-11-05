package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidad.Sala;
import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.interfaz.repositorio.RepositorioUsuario;
import com.tallerwebi.dominio.interfaz.servicio.ServicioCompra;
import com.tallerwebi.dominio.interfaz.servicio.ServicioSala;
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
            @RequestParam(name = "payment_id", required = false) String paymentId,
            @RequestParam(name = "status", required = false) String status,
            @RequestParam(name = "external_reference", required = false) String externalReference) {

        try {
            if (paymentId != null && !paymentId.isEmpty()) {
                servicioCompra.confirmarPago(paymentId);
                return new RedirectView("/inicio/?pago=exitoso", true);
            } else {
                // No vino payment_id: puede que Mercado Pago todavía no lo pase.
                // El webhook confirmará el pago en breve; mostramos mensaje intermedio.
                return new RedirectView("/inicio/?pago=en_proceso", true);
            }
        } catch (Exception e) {
            return new RedirectView("/inicio/?pago=error", true);
        }
    }
}