package com.tallerwebi.dominio;

import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.excepcion.UsuarioExistente;
import com.tallerwebi.dominio.interfaz.repositorio.RepositorioUsuario;
import com.tallerwebi.dominio.interfaz.servicio.ServicioEditarPerfil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ServicioEditarPerfilImpl implements ServicioEditarPerfil {

    private final RepositorioUsuario repositorioUsuario;

    @Autowired
    public ServicioEditarPerfilImpl(RepositorioUsuario repositorioUsuario) {
        this.repositorioUsuario = repositorioUsuario;
    }

    @Override
    public DatosEdicionPerfilDTO obtenerDatosPerfil(Long usuarioId) {
        Usuario usuario = buscarUsuarioPorId(usuarioId);
        if (usuario == null) {
            throw new RuntimeException("Usuario no encontrado.");
        }
        return new DatosEdicionPerfilDTO(usuario);
    }

    @Override
    public void actualizarPerfil(DatosEdicionPerfilDTO datos) throws UsuarioExistente {
        Usuario usuarioAEditar = buscarUsuarioPorId(datos.getId());
        if (usuarioAEditar == null) {
            throw new RuntimeException("Usuario a editar no encontrado.");
        }

        // Solo verificar si el nombre de usuario cambia y ya existe
        if (!datos.getNombreUsuario().equalsIgnoreCase(usuarioAEditar.getNombreUsuario())) {
            Usuario usuarioConMismoNombre = repositorioUsuario.buscarPorNombreUsuario(datos.getNombreUsuario());
            if (usuarioConMismoNombre != null) {
                throw new UsuarioExistente("El nombre de usuario ya está en uso.");
            }
        }

        usuarioAEditar.setNombreUsuario(datos.getNombreUsuario());
        usuarioAEditar.setFotoPerfil(datos.getUrlFotoPerfil());

        repositorioUsuario.modificar(usuarioAEditar);
    }

    @Override
    public Usuario buscarUsuarioPorId(Long usuarioId) {
        return repositorioUsuario.obtenerUsuarioPorId(usuarioId);
    }
}