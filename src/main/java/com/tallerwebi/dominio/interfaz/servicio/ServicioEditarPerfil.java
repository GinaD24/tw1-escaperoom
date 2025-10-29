package com.tallerwebi.dominio.interfaz.servicio; // Cambia a com.tallerwebi.dominio si no usas subpaquete 'interfaz.servicio'

import com.tallerwebi.dominio.DatosEdicionPerfilDTO;
import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.excepcion.ContraseniaInvalidaException;
import com.tallerwebi.dominio.excepcion.UsuarioExistente; // Crear esta excepción

public interface ServicioEditarPerfil {

    DatosEdicionPerfilDTO obtenerDatosPerfil(Long usuarioId);

    void actualizarPerfil(DatosEdicionPerfilDTO datos) throws UsuarioExistente, ContraseniaInvalidaException;

    Usuario buscarUsuarioPorId(Long usuarioId);

}