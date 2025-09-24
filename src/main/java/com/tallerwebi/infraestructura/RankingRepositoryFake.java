package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Ranking;
import com.tallerwebi.dominio.RankingRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Repository
public class RankingRepositoryFake implements RankingRepository {

    @Override
    public void guardar(Ranking ranking) {
        // No hace nada, solo para pruebas
    }

    @Override
    public Ranking buscarPorIdDeSalaYNombreDeUsuario(Integer idSala, String nombreUsuario) {
        // Retorna null o algún objeto dummy si querés
        return null;
    }

    @Override
    public List<Ranking> obtenerRankingPorSala(Integer idSala) {
        // Hardcodeamos algunos rankings de prueba
        return Arrays.asList(
                new Ranking(idSala, 1500L, "Usuario1", 32.5, 3, LocalDate.now(), Arrays.asList("Primera victoria")),
                new Ranking(idSala, 1200L, "Usuario2", 28.0, 2, LocalDate.now(), Arrays.asList("Velocidad")),
                new Ranking(idSala, 1000L, "Usuario3", 40.0, 4, LocalDate.now(), Arrays.asList("Paciencia", "Observador"))
        );
    }
}
