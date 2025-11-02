package com.tallerwebi.dominio.interfaz.servicio;

import com.tallerwebi.dominio.entidad.Usuario;

public interface ServicioPerfil {
    Usuario obtenerPerfil(Long idUsuario);
}
