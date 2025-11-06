package com.tallerwebi.dominio;

import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.excepcion.ContraseniaInvalidaException;
import com.tallerwebi.dominio.excepcion.UsuarioExistente;
import com.tallerwebi.dominio.interfaz.repositorio.RepositorioUsuario;
import com.tallerwebi.dominio.interfaz.servicio.ServicioEditarPerfil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;

@Service
@Transactional
public class ServicioEditarPerfilImpl implements ServicioEditarPerfil {

    private final RepositorioUsuario repositorioUsuario;
    private static final String UPLOAD_DIR = "C:/uploads/imagenes_perfil/";

    @Autowired
    public ServicioEditarPerfilImpl(RepositorioUsuario repositorioUsuario) {
        this.repositorioUsuario = repositorioUsuario;
        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
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
        usuarioAEditar.setNombreUsuario(datos.getNombreUsuario());

        // --- logica de contrasenia ---
        if (datos.getContrasenaNueva() != null && !datos.getContrasenaNueva().trim().isEmpty()) {
            if (!usuarioAEditar.getPassword().equals(datos.getContrasenaActual())) {
                throw new ContraseniaInvalidaException("La contraseña actual es incorrecta.");
            }
            usuarioAEditar.setPassword(datos.getContrasenaNueva());
        }

        // --- logica de foto de perfil de usuario  ---
        if (datos.getNuevaFoto() != null && !datos.getNuevaFoto().isEmpty()) {
            try {
                String nombreOriginal = datos.getNuevaFoto().getOriginalFilename();
                String nombreLimpio = nombreOriginal.replaceAll("[^a-zA-Z0-9\\.\\-]", "_");

                String nombreArchivo = datos.getId() + "_" + nombreLimpio;

                File archivoGuardado = new File(UPLOAD_DIR + nombreArchivo);

                datos.getNuevaFoto().transferTo(archivoGuardado);

                String urlRelativa = "/img/uploads/" + nombreArchivo;
                usuarioAEditar.setFotoPerfil(urlRelativa);

            } catch (IOException e) {
                throw new RuntimeException("Error al guardar el archivo de imagen.", e);
            }
        } else if (usuarioAEditar.getFotoPerfil() == null || usuarioAEditar.getFotoPerfil().isEmpty()) {
            usuarioAEditar.setFotoPerfil(null);
        }

        repositorioUsuario.modificar(usuarioAEditar);
    }

    @Override
    public Usuario buscarUsuarioPorId(Long usuarioId) {
        return repositorioUsuario.obtenerUsuarioPorId(usuarioId);
    }
}