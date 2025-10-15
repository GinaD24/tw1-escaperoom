package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.ValidacionInvalidaException;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class ServicioPerfilJugador implements ServicioPerfil{

    private final RepositorioUsuario repositorioUsuario;
    private final RankingRepository rankingRepository;
    private final RepositorioLogro repositorioLogro;
    private final RepositorioPerfil repositorioPerfil;

    @Autowired
    public ServicioPerfilJugador(RepositorioUsuario repositorioUsuario, RankingRepository rankingRepository,
                                 RepositorioLogro repositorioLogro, RepositorioPerfil repositorioPerfil) {
        this.repositorioUsuario = repositorioUsuario;
        this.rankingRepository = rankingRepository;
        this.repositorioLogro = repositorioLogro;
        this.repositorioPerfil = repositorioPerfil;
    }

    public Usuario obtenerPerfil(Long idUsuario) {
        Usuario usuario = repositorioUsuario.buscarPorId(idUsuario);
        if (usuario == null) {
            throw new RuntimeException("Usuario no encontrado con ID: " + idUsuario);
        }
        return usuario;
    }

    @Override
    public Usuario buscarPorEmail(String email) {

        Usuario usuario = repositorioUsuario.buscarPorEmail(email);
        return usuario;
    }
}

