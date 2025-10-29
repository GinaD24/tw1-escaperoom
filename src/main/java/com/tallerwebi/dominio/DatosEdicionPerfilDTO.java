package com.tallerwebi.dominio;

import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.excepcion.ContraseniaInvalidaException;
import com.tallerwebi.dominio.excepcion.DatosIncompletosException;
import com.tallerwebi.dominio.excepcion.ValidacionInvalidaException;

public class DatosEdicionPerfilDTO {

    private Long id;
    private String nombreUsuario;
    private String contrasenaActual;
    private String contrasenaNueva;
    private String urlFotoPerfil;

    public DatosEdicionPerfilDTO() {}

    public DatosEdicionPerfilDTO(Usuario usuario) {
        this.id = usuario.getId();
        this.nombreUsuario = usuario.getNombreUsuario();
        this.urlFotoPerfil = usuario.getFotoPerfil();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombreUsuario() { return nombreUsuario; }
    public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }

    public String getContrasenaActual() { return contrasenaActual; }
    public void setContrasenaActual(String contrasenaActual) { this.contrasenaActual = contrasenaActual; }

    public String getContrasenaNueva() { return contrasenaNueva; }
    public void setContrasenaNueva(String contrasenaNueva){ this.contrasenaNueva = contrasenaNueva; }


    public String getUrlFotoPerfil() { return urlFotoPerfil; }
    public void setUrlFotoPerfil(String urlFotoPerfil) { this.urlFotoPerfil = urlFotoPerfil; }

    public void validarDatos() throws DatosIncompletosException, ValidacionInvalidaException, ContraseniaInvalidaException {
        if (nombreUsuario == null || nombreUsuario.trim().isEmpty()) {
            throw new DatosIncompletosException("El nombre de usuario es obligatorio.");
        }
        if (nombreUsuario.length() < 4) {
            throw new ValidacionInvalidaException("El nombre de usuario debe tener al menos 4 caracteres.");
        }

        if (contrasenaNueva != null && !contrasenaNueva.trim().isEmpty() && contrasenaNueva.equals(contrasenaActual)) {
            throw new ContraseniaInvalidaException("La contraseña nueva no puede ser igual a la actual.");
        }

        if (contrasenaNueva != null && !contrasenaNueva.trim().isEmpty() &&
                (contrasenaActual == null || contrasenaActual.trim().isEmpty())) {
            throw new DatosIncompletosException("Para cambiar la contraseña, debe ingresar la Contraseña Actual.");
        }


    }
}