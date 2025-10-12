package com.tallerwebi.dominio;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ServicioPerfilJugador {

    private final RepositorioUsuario repositorioUsuario;
    private final RankingRepository rankingRepository;
    private final RepositorioLogro repositorioLogro;

    @Autowired
    public ServicioPerfilJugador(RepositorioUsuario repositorioUsuario, RankingRepository rankingRepository, RepositorioLogro repositorioLogro) {
        this.repositorioUsuario = repositorioUsuario;
        this.rankingRepository = rankingRepository;
        this.repositorioLogro = repositorioLogro;
    }


    //--------------------------------
    public PerfilJugador obtenerPerfil(Long idUsuario) {
        Usuario usuario = repositorioUsuario.buscarPorId(idUsuario);

        if (usuario == null) {
            throw new RuntimeException("Usuario no encontrado");
        }
        PerfilJugador perfil = new PerfilJugador(
                usuario.getId(),
                usuario.getNombreUsuario(),
                usuario.getFotoPerfil(),
                usuario.getLogrosFavoritos()
        );

        return perfil;
    }

   /// /---------------------------
    public void actualizarPerfil(Long idUsuario, String nuevoNombre, String nombreArchivo, List<Long> idsLogrosFavoritos) {
        Usuario usuario = repositorioUsuario.buscarPorId(idUsuario);

        if (usuario == null) {
            throw new RuntimeException("Usuario no encontrado");
        }

        if (nuevoNombre != null && !nuevoNombre.isBlank()) {
            usuario.setNombre(nuevoNombre);
        }

        if (idsLogrosFavoritos != null && !idsLogrosFavoritos.isEmpty()) {
            List<Logro> logrosSeleccionados = idsLogrosFavoritos.stream()
                    .map(repositorioLogro::buscar)
                    .collect(Collectors.toList());

            usuario.setLogrosFavoritos(logrosSeleccionados);
        }

        repositorioUsuario.modificar(usuario);
    }

    //--------
    public void actualizarFotoPerfil(Long idUsuario, String nuevaFoto) {
        Usuario usuario = repositorioUsuario.buscarPorId(idUsuario);

        if (usuario == null) {
            throw new RuntimeException("Usuario no encontrado");
        }

        usuario.setFotoPerfil(nuevaFoto);
        repositorioUsuario.modificar(usuario);
    }


    public List<Logro> obtenerTodosLosLogros() {
        return repositorioLogro.obtenerTodosLosLogros();
    }


}