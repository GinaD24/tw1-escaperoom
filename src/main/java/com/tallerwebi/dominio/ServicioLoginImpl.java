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

        return usuarioBuscado;
    }

    @Override
    public void registrar(Usuario usuario) throws UsuarioExistente, EdadInvalidaException, DatosIncompletosException {
//        Usuario usuarioEncontrado = repositorioUsuario.buscarUsuario(usuario.getEmail(), usuario.getPassword());
        Usuario usuarioEncontrado = repositorioUsuario.buscar(usuario.getEmail());
        if(usuarioEncontrado != null){
            throw new UsuarioExistente();
        }

        LocalDate fechaNacimiento = usuario.getFechaNacimiento();

        if (fechaNacimiento == null) {
            throw new DatosIncompletosException("La fecha de nacimiento es obligatoria.");
        }

        Integer edad = Period.between(fechaNacimiento, LocalDate.now()).getYears();

        if(edad < 7){
            throw new EdadInvalidaException();
        }


        repositorioUsuario.guardar(usuario);
    }

}

