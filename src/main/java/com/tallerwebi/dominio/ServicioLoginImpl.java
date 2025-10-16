package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.Period;
import java.util.regex.Pattern;

@Service("servicioLogin")
@Transactional
public class ServicioLoginImpl implements ServicioLogin {

    private RepositorioUsuario repositorioUsuario;

    @Autowired
    public ServicioLoginImpl(RepositorioUsuario repositorioUsuario){
        this.repositorioUsuario = repositorioUsuario;
    }

    @Override
    public Usuario consultarUsuario (String email, String password) throws CredencialesInvalidasException {

        Usuario usuarioBuscado = repositorioUsuario.buscarUsuario(email, password);

        if(usuarioBuscado == null){
            throw new CredencialesInvalidasException("Las credenciales ingresadas son inválidas.");
        }

        // VERIFICACION CREDENCIALES VACIAS
        String emailUsuario = usuarioBuscado.getEmail();
        String passwordUsuario = usuarioBuscado.getPassword();

        if((passwordUsuario == null || passwordUsuario.isEmpty()) || (emailUsuario == null || emailUsuario.isEmpty())){
            throw new CredencialesInvalidasException("Se debe completar las credenciales");
        }

        return usuarioBuscado;
    }

    @Override
    public void registrar(Usuario usuario) throws UsuarioExistente, EdadInvalidaException, DatosIncompletosException, ValidacionInvalidaException{

        validarCamposObligatorios(usuario);
        validarEmail(usuario.getEmail());
        validarPassword(usuario.getPassword());
        validarNombreUsuario(usuario.getNombreUsuario());
        validarFechaNacimiento(usuario.getFechaNacimiento());

        Usuario usuarioEmailExistente = repositorioUsuario.buscar(usuario.getEmail());
        if (usuarioEmailExistente != null) {
            throw new UsuarioExistente("Ya existe un usuario con ese Email");
        }

        Usuario usuarioNombreExistente = repositorioUsuario.buscarPorNombreUsuario(usuario.getNombreUsuario());
        if (usuarioNombreExistente != null) {
            throw new ValidacionInvalidaException("El nombre de usuario ya está registrado.");
        }

        LocalDate fechaNacimiento = usuario.getFechaNacimiento();
        Integer edad = Period.between(fechaNacimiento, LocalDate.now()).getYears();
        if (edad < 7) {
            throw new EdadInvalidaException();
        }

        if (usuario.getRol() == null || usuario.getRol().trim().isEmpty()) {
            usuario.setRol("USUARIO");
        }

        // VERIFICACION CONTRASENIA

        String contraseniaUsuario = usuario.getPassword();

        if(contraseniaUsuario == null || contraseniaUsuario.length() < 8){
            throw new DatosIncompletosException("La contraseña debe tener al menos 8 caracteres");
        }

        // VERIFICACION FORMATO EMAIL

        String emailUsuario = usuario.getEmail();
        String regexPattern = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";


        if(emailUsuario == null || !Pattern.matches(regexPattern, emailUsuario)){
            throw new DatosIncompletosException("El email ingresado no es valido");
        }

        repositorioUsuario.guardar(usuario);
    }

    //VALIDAICONES
    private void validarCamposObligatorios(Usuario usuario) throws DatosIncompletosException {
        if (usuario.getNombre() == null || usuario.getNombre().trim().isEmpty()) {
            throw new DatosIncompletosException("El nombre es obligatorio");
        }
        if (usuario.getApellido() == null || usuario.getApellido().trim().isEmpty()) {
            throw new DatosIncompletosException("El apellido es obligatorio");
        }
        if (usuario.getEmail() == null || usuario.getEmail().trim().isEmpty()) {
            throw new DatosIncompletosException("El email es obligatorio");
        }
        if (usuario.getPassword() == null || usuario.getPassword().trim().isEmpty()) {
            throw new DatosIncompletosException("La contraseña es obligatoria");
        }
        if (usuario.getNombreUsuario() == null || usuario.getNombreUsuario().trim().isEmpty()) {
            throw new DatosIncompletosException("El nombre de usuario es obligatorio");
        }
        if (usuario.getFechaNacimiento() == null) {
            throw new DatosIncompletosException("La fecha de nacimiento es obligatoria.");
        }
    }

    private void validarEmail(String email) throws ValidacionInvalidaException {
        if (email == null || email.trim().isEmpty()) {
            throw new ValidacionInvalidaException("El email es inválido.");
        }
        // Regex simple para email (contiene @ y .)
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        if (!Pattern.matches(emailRegex, email)) {
            throw new ValidacionInvalidaException("El formato del email es inválido (debe contener @ y dominio).");
        }
    }

    private void validarPassword(String password) throws ValidacionInvalidaException {
        if (password == null || password.length() < 8) {
            throw new ValidacionInvalidaException("La contraseña debe tener al menos 8 caracteres.");
        }
    }

    private void validarNombreUsuario(String nombreUsuario) throws ValidacionInvalidaException {
        if (nombreUsuario == null || nombreUsuario.length() < 4) {
            throw new ValidacionInvalidaException("El nombre de usuario debe tener al menos 4 caracteres.");
        }
    }

    private void validarFechaNacimiento(LocalDate fecha) throws ValidacionInvalidaException {
        if (fecha == null || fecha.isAfter(LocalDate.now())) {
            throw new ValidacionInvalidaException("La fecha de nacimiento no puede ser futura.");
        }
    }
}
