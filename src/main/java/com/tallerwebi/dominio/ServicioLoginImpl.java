package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.CredencialesInvalidasException;
import com.tallerwebi.dominio.excepcion.DatosIncompletosException;
import com.tallerwebi.dominio.excepcion.EdadInvalidaException;
import com.tallerwebi.dominio.excepcion.UsuarioExistente;
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
            throw new CredencialesInvalidasException();
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
    public void registrar(Usuario usuario) throws UsuarioExistente, EdadInvalidaException, DatosIncompletosException, CredencialesInvalidasException {
//        Usuario usuarioEncontrado = repositorioUsuario.buscarUsuario(usuario.getEmail(), usuario.getPassword());
        Usuario usuarioEncontrado = repositorioUsuario.buscar(usuario.getEmail());
        if(usuarioEncontrado != null){
            throw new UsuarioExistente();
        }

        // VERIFICACION EDAD

        LocalDate fechaNacimiento = usuario.getFechaNacimiento();

        if (fechaNacimiento == null) {
            throw new DatosIncompletosException("La fecha de nacimiento es obligatoria.");
        }

        Integer edad = Period.between(fechaNacimiento, LocalDate.now()).getYears();

        if(edad < 7){
            throw new EdadInvalidaException();
        }

        // VERIFICACION CONTRASENIA

        String contraseniaUsuario = usuario.getPassword();

        if(contraseniaUsuario == null || contraseniaUsuario.isEmpty() || contraseniaUsuario.length() < 8){
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

}

