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
    // ¡RECUERDA! Esta ruta debe coincidir con la configurada en SpringWebConfig
    private static final String UPLOAD_DIR = "C:/uploads/imagenes_perfil/";

    @Autowired
    public ServicioEditarPerfilImpl(RepositorioUsuario repositorioUsuario) {
        this.repositorioUsuario = repositorioUsuario;
        // Opcional pero recomendado: Crear el directorio al iniciar el servicio
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

        // --- LÓGICA DE NOMBRE DE USUARIO ---
        if (!datos.getNombreUsuario().equalsIgnoreCase(usuarioAEditar.getNombreUsuario())) {
            Usuario usuarioConMismoNombre = repositorioUsuario.buscarPorNombreUsuario(datos.getNombreUsuario());
            if (usuarioConMismoNombre != null) {
                throw new UsuarioExistente("El nombre de usuario ya está en uso.");
            }
        }
        usuarioAEditar.setNombreUsuario(datos.getNombreUsuario());

        // --- LÓGICA DE CONTRASEÑA ---
        if (datos.getContrasenaNueva() != null && !datos.getContrasenaNueva().trim().isEmpty()) {
            if (!usuarioAEditar.getPassword().equals(datos.getContrasenaActual())) {
                throw new ContraseniaInvalidaException("La contraseña actual es incorrecta.");
            }
            System.out.println("LOG: Contraseña ACTUAL/VIEJA en el objeto antes de setear: " + usuarioAEditar.getPassword());
            usuarioAEditar.setPassword(datos.getContrasenaNueva());
            System.out.println("LOG: Contraseña NUEVA en el objeto, lista para persistir: " + usuarioAEditar.getPassword());
        }

        // --- LÓGICA DE FOTO DE PERFIL (CORREGIDA) ---
        if (datos.getNuevaFoto() != null && !datos.getNuevaFoto().isEmpty()) {
            try {
                String nombreOriginal = datos.getNuevaFoto().getOriginalFilename();
                String nombreLimpio = nombreOriginal.replaceAll("[^a-zA-Z0-9\\.\\-]", "_");

                String nombreArchivo = datos.getId() + "_" + nombreLimpio;

                File archivoGuardado = new File(UPLOAD_DIR + nombreArchivo);

                // Transferir el archivo a la ubicación física
                datos.getNuevaFoto().transferTo(archivoGuardado);

                // Guardar la URL relativa para el acceso web (DB)
                String urlRelativa = "/img/uploads/" + nombreArchivo;
                usuarioAEditar.setFotoPerfil(urlRelativa); // Se actualizará la URL en el objeto

                System.out.println("LOG: Nueva foto guardada en: " + urlRelativa);

            } catch (IOException e) {
                throw new RuntimeException("Error al guardar el archivo de imagen.", e);
            }
        } else if (usuarioAEditar.getFotoPerfil() == null || usuarioAEditar.getFotoPerfil().isEmpty()) {
            // Si no subió foto y no tenía una, asegurar que la URL sea NULL
            usuarioAEditar.setFotoPerfil(null);
        }

        // --- PERSISTENCIA DE CAMBIOS (DEBE IR AL FINAL) ---
        // usuarioAEditar ya tiene actualizados: nombreUsuario, password (si cambió), fotoPerfil (si cambió)

        System.out.println("LOG: Persistiendo cambios en la DB...");
        repositorioUsuario.modificar(usuarioAEditar);
        System.out.println("LOG: ¡Cambios persistidos!");
    }

    @Override
    public Usuario buscarUsuarioPorId(Long usuarioId) {
        return repositorioUsuario.obtenerUsuarioPorId(usuarioId);
    }
}