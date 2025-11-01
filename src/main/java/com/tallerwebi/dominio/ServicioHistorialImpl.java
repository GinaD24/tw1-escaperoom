package com.tallerwebi.dominio;
import com.tallerwebi.dominio.entidad.Partida;
import com.tallerwebi.dominio.interfaz.repositorio.RepositorioHistorial;
import com.tallerwebi.dominio.interfaz.servicio.ServicioHistorial;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Service
public class ServicioHistorialImpl implements ServicioHistorial {

    private RepositorioHistorial repositorioHistorial;

    public ServicioHistorialImpl(RepositorioHistorial repositorio) {
        this.repositorioHistorial = repositorio;
    }

    @Override
    public List<Partida> traerHistorialDeJugador(Long idUsuario) {
        return repositorioHistorial.obtenerPartidasPorJugador(idUsuario);
    }



}