package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidad.ImagenAcertijo;
import com.tallerwebi.dominio.interfaz.servicio.ServicioPartida;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import javax.servlet.http.HttpSession;
import java.util.List;

@Component
@SessionScope
public class DatosPartidaSesion {
    private final ServicioPartida servicioPartida;
    private final HttpSession session;

    private Integer idSalaActual;
    private Integer numeroEtapaActual;
    private Long idEtapa;
    private AcertijoActualDTO acertijoActualDTO;
    private Boolean partidaGanada;
    private Long idPartida;

    @Autowired
    public DatosPartidaSesion(ServicioPartida servicioPartida, HttpSession session) {
        this.servicioPartida = servicioPartida;
        this.session = session;
    }

    public Long getIdEtapa() {
        return (Long) session.getAttribute("id_etapa");
    }

    public void setIdEtapa(Long idEtapa) {
        session.setAttribute("id_etapa", idEtapa);
    }

    public Integer getIdSalaActual() {
        return (Integer) session.getAttribute("id_sala_actual");
    }

    public void setIdSalaActual(Integer idSalaActual) {
        session.setAttribute("id_sala_actual", idSalaActual);
    }

    public Integer getNumeroEtapaActual() {
        return (Integer) session.getAttribute("numero_etapa_actual");
    }

    public void setNumeroEtapaActual(Integer numeroEtapaActual) {
        session.setAttribute("numero_etapa_actual", numeroEtapaActual);
    }

    public AcertijoActualDTO getAcertijoActual() {
        return (AcertijoActualDTO) session.getAttribute("acertijo_actual");
    }
    public void setAcertijoActual(AcertijoActualDTO estado) {
        session.setAttribute("acertijo_actual", estado);
    }

    public Boolean getPartidaGanada() {
        return (Boolean) session.getAttribute("partida_ganada");
    }

    public void setPartidaGanada(Boolean partidaGanada) {
        session.setAttribute("partida_ganada", partidaGanada);
    }

    public Long getIdPartida() {
        return (Long) session.getAttribute("idPartida");
    }

    public void setIdPartida(Long idPartida) {
        session.setAttribute("idPartida", idPartida);
    }

    public void limpiarSesionPartida() {
        if(session.getAttribute("partida_ganada") != null) {
        session.removeAttribute("partida_ganada");
        }
    }

    public void limpiarSesionIdEtapaAcertijo() {
        limpiarAcertijoActual();
        session.removeAttribute("id_etapa");
    }

    public void limpiarAcertijoActual() {
        session.removeAttribute("acertijo_actual");
    }
    public void guardarSecuencia(List<ImagenAcertijo> imagenesDeSecuencia) {
        session.setAttribute("imagenesDeSecuencia", imagenesDeSecuencia);
    }

    public List<ImagenAcertijo> getSecuencia() {
        return (List<ImagenAcertijo>) session.getAttribute("imagenesDeSecuencia");
    }
}