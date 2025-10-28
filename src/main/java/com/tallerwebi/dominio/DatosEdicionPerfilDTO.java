package com.tallerwebi.dominio;

import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.excepcion.DatosIncompletosException;
import com.tallerwebi.dominio.excepcion.ValidacionInvalidaException;

import java.time.LocalDate;
import java.time.Period;
import java.util.regex.Pattern;

public class DatosEdicionPerfilDTO {

    private Long id;
    private String nombreUsuario;
    private String email;
    private LocalDate fechaNacimiento;
    private String urlFotoPerfil;

    public DatosEdicionPerfilDTO() {}

    public DatosEdicionPerfilDTO(Usuario usuario) {
        this.id = usuario.getId();
        this.nombreUsuario = usuario.getNombreUsuario();
        this.email = usuario.getEmail();
        this.fechaNacimiento = usuario.getFechaNacimiento();
        this.urlFotoPerfil = usuario.getFotoPerfil();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombreUsuario() { return nombreUsuario; }
    public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }

    public String getUrlFotoPerfil() { return urlFotoPerfil; }
    public void setUrlFotoPerfil(String urlFotoPerfil) { this.urlFotoPerfil = urlFotoPerfil; }


    public void validarDatos() throws DatosIncompletosException, ValidacionInvalidaException {


        if (nombreUsuario == null || nombreUsuario.trim().isEmpty()) {
            throw new DatosIncompletosException("El nombre de usuario es obligatorio.");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new DatosIncompletosException("El email es obligatorio.");
        }
        if (fechaNacimiento == null) {
            throw new DatosIncompletosException("La fecha de nacimiento es obligatoria.");
        }


        if (nombreUsuario.length() < 4) {
            throw new ValidacionInvalidaException("El nombre de usuario debe tener al menos 4 caracteres.");
        }


        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        if (!Pattern.matches(emailRegex, email)) {
            throw new ValidacionInvalidaException("El formato del email es inválido (debe contener @ y dominio).");
        }


        if (fechaNacimiento.isAfter(LocalDate.now())) {
            throw new ValidacionInvalidaException("La fecha de nacimiento no puede ser futura.");
        }

        int edad = Period.between(fechaNacimiento, LocalDate.now()).getYears();
        if (edad < 7) {
            throw new ValidacionInvalidaException("La edad mínima permitida es 7 años.");
        }
    }
}