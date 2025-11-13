package com.tallerwebi.dominio;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ServicioGeneradorIAImplTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private Environment environment;

    @InjectMocks
    private ServicioGeneradorIAImpl servicioGeneradorIA;

    @Test
    public void queAlGenerarPistasLaApiRespondeOkYSeParseanLasPistasCorrectamente() throws Exception {
        String descripcion = "Soy algo que vuela pero no tengo alas.";
        String respuesta = "El tiempo";

        String jsonRespuestaApi = "{\n" +
                "  \"choices\": [\n" +
                "    {\n" +
                "      \"message\": {\n" +
                "        \"content\": \"{ \\\"pistas\\\": [\\\"Pista 1: No me puedes ver\\\", \\\"Pista 2: Corro sin parar\\\", \\\"Pista 3: Todos me usan\\\"] }\"\n" +
                "      }\n" +
                "    }\n" +
                "  ]\n" +
                "}";


        when(restTemplate.postForObject(
                anyString(),
                any(HttpEntity.class),
                eq(String.class))
        ).thenReturn(jsonRespuestaApi);

        List<String> pistasGeneradas = servicioGeneradorIA.generarPistas(descripcion, respuesta);

        assertNotNull(pistasGeneradas);
        assertEquals(3, pistasGeneradas.size());
        assertEquals("Pista 1: No me puedes ver", pistasGeneradas.get(0));
    }

    @Test
    public void queSiLaApiLanzaUnErrorHttpSeLanzaRuntimeException() {
        String descripcion = "Test";
        String respuesta = "Test";

        when(restTemplate.postForObject(
                anyString(),
                any(HttpEntity.class),
                eq(String.class))
        ).thenThrow(new HttpClientErrorException(HttpStatus.UNAUTHORIZED, "Token inválido"));


        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            servicioGeneradorIA.generarPistas(descripcion, respuesta);
        });

        assertTrue(exception.getMessage().contains("Error al llamar a la IA"));
    }
}