package com.tallerwebi.dominio;

import java.util.List;

public interface ServicioPerfil {
    Usuario obtenerPerfil(Long idUsuario);
    Usuario buscarPorEmail(String email);
//    void actualizarNombreUsuario(Long idUsuario, String nuevoNombre);
//    void actualizarFotoPerfil(Long idUsuario, String nuevaFoto);

}
