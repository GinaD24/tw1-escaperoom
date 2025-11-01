package com.tallerwebi.dominio;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;

import com.tallerwebi.dominio.entidad.Partida;
import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.interfaz.repositorio.RepositorioHistorial;
import com.tallerwebi.dominio.interfaz.servicio.ServicioHistorial;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ServicioHistorialImplTest {

    RepositorioHistorial repositorio;
    ServicioHistorial servicioHistorial;

    @BeforeEach
    public void init() {
        repositorio = mock(RepositorioHistorial.class);
        servicioHistorial = new ServicioHistorialImpl(repositorio);
    }


    @Test
    void deberiaDevolverLasPartidasJugadasPorUusario() {

        Usuario usuario = new Usuario();

        Partida partida1 = new Partida();
        partida1.setUsuario(usuario);
        partida1.setEsta_activa(false);
        partida1.setInicio(LocalDateTime.now());
        partida1.setPuntaje(100);

        Partida partida2 = new Partida();
        partida2.setUsuario(usuario);
        partida2.setEsta_activa(false);
        partida2.setInicio(LocalDateTime.now());
        partida2.setPuntaje(100);

        List<Partida> historial = new ArrayList<>();
        historial.add(partida1);
        historial.add(partida2);

        when(repositorio.obtenerPartidasPorJugador(usuario.getId())).thenReturn(historial);

        List<Partida> historialObtenido = servicioHistorial.traerHistorialDeJugador(usuario.getId());

        verify(repositorio).obtenerPartidasPorJugador(usuario.getId());
        assertThat(historialObtenido.size(), equalTo(2));
        assertThat(historialObtenido.get(0), equalTo(partida1));
        assertThat(historialObtenido.get(1), equalTo(partida2));
    }
}
