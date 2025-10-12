package com.tallerwebi.dominio;

import java.time.LocalDate;
import java.util.List;
import javax.persistence.*;



public class PerfilJugador {

    private Long id;
    private String nombre;
    private int partidasGanadas;
    private String fotoPerfil;
    private List<Logro> logrosFavoritos;


    public PerfilJugador(Long id, String nombre, String fotoPerfil, List <Logro> logrosFavoritos){
        this.id =id;
        this.nombre = nombre;
        this.fotoPerfil = fotoPerfil;
        this.logrosFavoritos = logrosFavoritos;
    }

    public void cambiarNombre(String nuevoNombre) {
        if (nuevoNombre == null || !nuevoNombre.matches("^[A-Za-z0-9._-]{6,}$")) {
            throw new IllegalArgumentException("Nombre inválido");
        }
        this.nombre = nuevoNombre;
    }


    public void cambiarFotoPerfil(String nuevaFoto) {
        if (nuevaFoto == null || nuevaFoto.isBlank()) {
            throw new IllegalArgumentException("Foto inválida");
        }
        this.fotoPerfil = nuevaFoto;
    }


    public Long getId() {
        return id; }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre; }


    public String getFotoPerfil() {
        return fotoPerfil; }

    public List<Logro> getLogrosFavoritos() {
        return logrosFavoritos;
    }

    public void setLogrosFavoritos(List<Logro> logrosFavoritos) {
        this.logrosFavoritos = logrosFavoritos;
    }


}


