package com.tallerwebi.dominio.interfaz.servicio;

import com.tallerwebi.dominio.entidad.Usuario;

public interface ServicioPerfil {
    Usuario obtenerPerfil(Long idUsuario);
    Usuario buscarPorEmail(String email);
//    void actualizarNombreUsuario(Long idUsuario, String nuevoNombre);
//    void actualizarFotoPerfil(Long idUsuario, String nuevaFoto);

}
