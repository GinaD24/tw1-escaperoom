package com.tallerwebi.dominio;

import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.excepcion.ContraseniaInvalidaException;
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
    public void actualizarPerfil(DatosEdicionPerfilDTO datos) throws UsuarioExistente, ContraseniaInvalidaException {
        Usuario usuarioAEditar = buscarUsuarioPorId(datos.getId());
        if (usuarioAEditar == null) {
            throw new RuntimeException("Usuario a editar no encontrado.");
        }

        if (!datos.getNombreUsuario().equalsIgnoreCase(usuarioAEditar.getNombreUsuario())) {
            Usuario usuarioConMismoNombre = repositorioUsuario.buscarPorNombreUsuario(datos.getNombreUsuario());
            if (usuarioConMismoNombre != null) {
                throw new UsuarioExistente("El nombre de usuario ya está en uso.");
            }
        }


        if (datos.getContrasenaNueva() != null && !datos.getContrasenaNueva().trim().isEmpty()) {


            if (!usuarioAEditar.getPassword().equals(datos.getContrasenaActual())) {
                throw new ContraseniaInvalidaException("La contraseña actual es incorrecta.");
            }

            System.out.println("LOG: Contraseña ACTUAL/VIEJA en el objeto antes de setear: " + usuarioAEditar.getPassword()); // LOG 1: Muestra el valor viejo (el de la DB)

            usuarioAEditar.setPassword(datos.getContrasenaNueva());

            System.out.println("LOG: Contraseña NUEVA en el objeto, lista para persistir: " + usuarioAEditar.getPassword()); // LOG 2: Muestra el nuevo valor
        }

        usuarioAEditar.setNombreUsuario(datos.getNombreUsuario());
        usuarioAEditar.setFotoPerfil(datos.getUrlFotoPerfil());

        System.out.println("LOG: Persistiendo cambios en la DB...");
        repositorioUsuario.modificar(usuarioAEditar);
        System.out.println("LOG: ¡Cambios persistidos!");
    }

    @Override
    public Usuario buscarUsuarioPorId(Long usuarioId) {
        return repositorioUsuario.obtenerUsuarioPorId(usuarioId);
    }
}