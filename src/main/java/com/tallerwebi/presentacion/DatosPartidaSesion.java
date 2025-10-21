package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.interfaz.servicio.ServicioPartida;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import javax.servlet.http.HttpSession;

@Component
@SessionScope
public class DatosPartidaSesion {
    private final ServicioPartida servicioPartida;
    private final HttpSession session;

    private Integer idSalaActual;
    private Integer numeroEtapaActual;
    private Long idEtapa;
    private Long idAcertijo;
    private Boolean partidaGanada;

    @Autowired
    public DatosPartidaSesion(ServicioPartida servicioPartida, HttpSession session) {
        this.servicioPartida = servicioPartida;
        this.session = session;
    }

    public Long getIdAcertijo() {
        return (Long) session.getAttribute("id_acertijo");
    }

    public void setIdAcertijo(Long idAcertijo) {
        session.setAttribute("id_acertijo", idAcertijo);
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

    public Boolean getPartidaGanada() {
        return (Boolean) session.getAttribute("partida_ganada");
    }

    public void setPartidaGanada(Boolean partidaGanada) {
        session.setAttribute("partida_ganada", partidaGanada);
    }

    public void limpiarSesionPartida() {
        session.removeAttribute("partida_ganada");
        session.removeAttribute("id_sala_actual");
        session.removeAttribute("numero_etapa_actual");
        session.removeAttribute("id_acertijo");
        session.removeAttribute("id_etapa");
    }

    public void limpiarSesionIdEtapaAcertijo() {
        session.removeAttribute("id_acertijo");
        session.removeAttribute("id_etapa");
    }
}