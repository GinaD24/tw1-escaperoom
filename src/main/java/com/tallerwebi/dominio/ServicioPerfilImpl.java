package com.tallerwebi.dominio;

import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.interfaz.repositorio.RankingRepository;
import com.tallerwebi.dominio.interfaz.repositorio.RepositorioLogro;
import com.tallerwebi.dominio.interfaz.repositorio.RepositorioPerfil;
import com.tallerwebi.dominio.interfaz.repositorio.RepositorioUsuario;
import com.tallerwebi.dominio.interfaz.servicio.ServicioPerfil;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class ServicioPerfilImpl implements ServicioPerfil {

    private final RepositorioUsuario repositorioUsuario;
    private final RankingRepository rankingRepository;
    private final RepositorioLogro repositorioLogro;
    private final RepositorioPerfil repositorioPerfil;

    @Autowired
    public ServicioPerfilImpl(RepositorioUsuario repositorioUsuario, RankingRepository rankingRepository,
                              RepositorioLogro repositorioLogro, RepositorioPerfil repositorioPerfil) {
        this.repositorioUsuario = repositorioUsuario;
        this.rankingRepository = rankingRepository;
        this.repositorioLogro = repositorioLogro;
        this.repositorioPerfil = repositorioPerfil;
    }

    public Usuario obtenerPerfil(Long idUsuario) {
        Usuario usuario = repositorioUsuario.obtenerUsuarioPorId(idUsuario);
        if (usuario == null) {
            throw new RuntimeException("Usuario no encontrado con ID: " + idUsuario);
        }
        return usuario;
    }

    }

