package com.tallerwebi.dominio;

import java.time.LocalDate;
import java.util.List;

public class Ranking {

    private Integer idSala;
    private Long puntaje;
    private String nombreUsuario;
    private Double tiempoFinalizacion;
    private Integer cantidadPistas;
    private LocalDate fechaHora;
    private List<String> logrosDesbloqueados;

    public Ranking(Integer idSala,Long puntaje,String nombreUsuario,Double tiempoFinalizacion,Integer cantidadPistas, LocalDate fechaHora, List<String> logrosDesbloqueados){
        this.idSala = idSala;
        this.puntaje = puntaje;
        this.nombreUsuario = nombreUsuario;
        this.tiempoFinalizacion = tiempoFinalizacion;
        this.cantidadPistas = cantidadPistas;
        this.fechaHora = fechaHora;
        this.logrosDesbloqueados = logrosDesbloqueados;
    }

    public Integer getIdSala() {
        return idSala;
    }

    public void setIdSala(Integer idSala) {
        this.idSala = idSala;
    }

    public Long getPuntaje() {
        return puntaje;
    }

    public void setPuntaje(Long puntaje) {
        this.puntaje = puntaje;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public Double getTiempoFinalizacion() {
        return tiempoFinalizacion;
    }

    public void setTiempoFinalizacion(Double tiempoFinalizacion) {
        this.tiempoFinalizacion = tiempoFinalizacion;
    }

    public Integer getCantidadPistas() {
        return cantidadPistas;
    }

    public void setCantidadPistas(Integer cantidadPistas) {
        this.cantidadPistas = cantidadPistas;
    }

    public LocalDate getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDate fechaHora) {
        this.fechaHora = fechaHora;
    }

    public List<String> getLogrosDesbloqueados() {
        return logrosDesbloqueados;
    }

    public void setLogrosDesbloqueados(List<String> logrosDesbloqueados) {
        this.logrosDesbloqueados = logrosDesbloqueados;
    }

    


}
