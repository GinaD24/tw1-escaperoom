package com.tallerwebi.dominio;

import com.tallerwebi.dominio.entidad.Acertijo;
import com.tallerwebi.dominio.entidad.Etapa;
// Importaciones añadidas
import com.tallerwebi.dominio.entidad.Pista;
import com.tallerwebi.dominio.entidad.Sala;
import com.tallerwebi.dominio.enums.TipoAcertijo;
import com.tallerwebi.dominio.interfaz.servicio.ServicioDatosPartida;
// Importación añadida
import com.tallerwebi.dominio.interfaz.servicio.ServicioGeneradorIA;
import com.tallerwebi.dominio.interfaz.servicio.ServicioPartida;
import com.tallerwebi.dominio.interfaz.servicio.ServicioSala;
import com.tallerwebi.presentacion.DatosPartidaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
// Importación añadida
import java.util.stream.Collectors;


@Service
public class ServicioDatosPartidaImpl implements ServicioDatosPartida {

    private final ServicioSala servicioSala;
    private final ServicioPartida servicioPartida;
    private final ServicioGeneradorIA servicioGeneradorIA;

    @Autowired
    public ServicioDatosPartidaImpl(ServicioSala servicioSala, ServicioPartida servicioPartida, ServicioGeneradorIA servicioGeneradorIA) {
        this.servicioSala = servicioSala;
        this.servicioPartida = servicioPartida;
        this.servicioGeneradorIA = servicioGeneradorIA;
    }

    @Override
    public DatosPartidaDTO obtenerDatosDePartida(Integer idSala, Integer numeroEtapa, Long idUsuario) {

        Sala sala = servicioSala.obtenerSalaPorId(idSala);

        Etapa etapa = servicioPartida.obtenerEtapaPorNumero(idSala, numeroEtapa);

        Acertijo acertijo = servicioPartida.obtenerAcertijo(etapa.getId(), idUsuario);

        if (acertijo != null &&
                acertijo.getTipo().equals(TipoAcertijo.ADIVINANZA) &&
                acertijo.getRespuesta() != null) {

            try {
                String descripcion = acertijo.getDescripcion();
                String respuesta = acertijo.getRespuesta().getRespuesta();

                List<String> descripcionesDePistas = servicioGeneradorIA.generarPistas(descripcion, respuesta);

                if (descripcionesDePistas != null && !descripcionesDePistas.isEmpty()) {

                    acertijo.getPistas().clear();

                    int nroPista = 1;
                    for (String descPista : descripcionesDePistas) {
                        Pista nuevaPista = new Pista(descPista, nroPista++);
                        acertijo.getPistas().add(nuevaPista);
                    }
                }
            } catch (Exception e) {
                System.err.println("FALLO LA API DE IA para generar pistas. El acertijo no tendrá pistas: " + e.getMessage());
            }
        }

        return new DatosPartidaDTO(sala, etapa, acertijo);
    }
}