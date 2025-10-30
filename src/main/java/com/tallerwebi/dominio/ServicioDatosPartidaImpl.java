package com.tallerwebi.dominio;

import com.tallerwebi.dominio.entidad.Acertijo;
import com.tallerwebi.dominio.entidad.Etapa;
import com.tallerwebi.dominio.entidad.ImagenAcertijo;
import com.tallerwebi.dominio.entidad.Sala;
import com.tallerwebi.dominio.enums.TipoAcertijo;
import com.tallerwebi.dominio.interfaz.servicio.ServicioDatosPartida;
import com.tallerwebi.dominio.interfaz.servicio.ServicioPartida;
import com.tallerwebi.dominio.interfaz.servicio.ServicioSala;
import com.tallerwebi.presentacion.DatosPartidaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServicioDatosPartidaImpl implements ServicioDatosPartida {

    private final ServicioSala servicioSala;
    private final ServicioPartida servicioPartida;

    @Autowired
    public ServicioDatosPartidaImpl(ServicioSala servicioSala, ServicioPartida servicioPartida) {
        this.servicioSala = servicioSala;
        this.servicioPartida = servicioPartida;
    }

    @Override
    public DatosPartidaDTO obtenerDatosDePartida(Integer idSala, Integer numeroEtapa, Long idUsuario) {

        Sala sala = servicioSala.obtenerSalaPorId(idSala);

        Etapa etapa = servicioPartida.obtenerEtapaPorNumero(idSala, numeroEtapa);

        Acertijo acertijo = servicioPartida.obtenerAcertijo(etapa.getId(), idUsuario);

        return new DatosPartidaDTO(sala, etapa, acertijo);
    }
}
