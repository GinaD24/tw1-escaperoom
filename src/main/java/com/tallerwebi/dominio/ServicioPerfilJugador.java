package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.ValidacionInvalidaException;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;
import java.util.regex.Pattern;
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
            throw new RuntimeException("Usuario no encontrado con ID: " + idUsuario);
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
    public void actualizarPerfil(Long idUsuario,
                                 String nuevoNombre,
                                 String nombreArchivo,
                                 List<Long> idsLogrosFavoritos) {

        Usuario usuario = repositorioUsuario.buscarPorId(idUsuario);

        if (usuario == null) {
            throw new RuntimeException("Usuario no encontrado con ID: " + idUsuario);
        }

        if (nuevoNombre != null && !nuevoNombre.isBlank()) {
            validarNombreUsuario(nuevoNombre);
            usuario.setNombreUsuario(nuevoNombre);
        }

        if (idsLogrosFavoritos != null) {
            List<Logro> logros = idsLogrosFavoritos.stream()
                    .map(id -> {
                        Logro logro = repositorioLogro.buscar(id);
                        if (logro == null) {
                            throw new RuntimeException("Logro no encontrado con ID: " + id);
                        }
                        return logro;
                    })
                    .collect(Collectors.toList());

            usuario.setLogrosFavoritos(logros);
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


    ///-------------------------
    public List<Logro> obtenerTodosLosLogros() {
        List<Logro> logros = repositorioLogro.obtenerTodosLosLogros();
        if (logros == null || logros.isEmpty()) {
            throw new RuntimeException("No hay logros disponibles actualmente.");
        }
        return logros;
    }

    private void validarNombreUsuario(String nombre) {
        String patron = "^[A-Za-z0-9._-]{6,}$";
        if (!nombre.matches(patron)) {
            throw new IllegalArgumentException(
                    "El nombre de usuario no es válido. " +
                            "Debe tener al menos 6 caracteres y solo puede contener letras, números, puntos, guiones y guiones bajos."
            );
        }
    }








    }

