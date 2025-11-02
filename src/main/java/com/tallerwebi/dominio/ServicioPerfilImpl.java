package com.tallerwebi.dominio;

import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.interfaz.repositorio.RepositorioUsuario;
import com.tallerwebi.dominio.interfaz.servicio.ServicioPerfil;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class ServicioPerfilImpl implements ServicioPerfil {

    private final RepositorioUsuario repositorioUsuario;

    @Autowired
    public ServicioPerfilImpl(RepositorioUsuario repositorioUsuario) {
        this.repositorioUsuario = repositorioUsuario;
    }

    public Usuario obtenerPerfil(Long idUsuario) {
        Usuario usuario = repositorioUsuario.obtenerUsuarioPorId(idUsuario);
        if (usuario == null) {
            throw new RuntimeException("Usuario no encontrado con ID: " + idUsuario);
        }
        return usuario;
    }


}

