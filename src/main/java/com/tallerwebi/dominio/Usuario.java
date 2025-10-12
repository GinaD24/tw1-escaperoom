package com.tallerwebi.dominio;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;
import java.util.List;

@Entity
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String nombre;
    private String apellido;
    private String password;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate fechaNacimiento;
    private String nombreUsuario;
    private String rol;
    private Boolean activo = false;
    private String fotoPerfil;
    private List<Logro> logrosFavoritos;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getNombre() {return nombre;}
    public void setNombre(String nombre) {this.nombre = nombre;}
    public String getApellido() {return apellido;}
    public void setApellido(String apellido) {this.apellido = apellido;}
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public LocalDate getFechaNacimiento() {return  fechaNacimiento;}
    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }
    public String getNombreUsuario() {return nombreUsuario;}
    public void setNombreUsuario(String nombreUsuario) {this.nombreUsuario = nombreUsuario;}
    public String getRol() {
        return rol;
    }
    public void setRol(String rol) {
        this.rol = rol;
    }
    public Boolean getActivo() {
        return activo;
    }
    public void setActivo(Boolean activo) {
        this.activo = activo;
    }


    public boolean activo() {
        return activo;
    }

    public void activar() {
        activo = true;
    }

    public String getFotoPerfil() {
        return fotoPerfil;
    }

    public void setFotoPerfil(String fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }

    public List<Logro> getLogrosFavoritos() {
        return logrosFavoritos;
    }

    public void setLogrosFavoritos(List<Logro> logrosFavoritos) {
        this.logrosFavoritos = logrosFavoritos;
    }
}
