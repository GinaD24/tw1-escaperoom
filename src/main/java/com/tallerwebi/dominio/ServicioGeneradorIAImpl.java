package com.tallerwebi.dominio;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
// Importaciones de Acertijo y Pista eliminadas
import com.tallerwebi.dominio.interfaz.servicio.ServicioGeneradorIA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ServicioGeneradorIAImpl implements ServicioGeneradorIA {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final String apiKey;
    private final String apiUrl;

    @Autowired
    public ServicioGeneradorIAImpl(RestTemplate restTemplate, Environment environment) {
        this.restTemplate = restTemplate;
        this.apiKey = environment.getProperty("HUGGINGFACE_API_TOKEN");
        this.apiUrl = "https://router.huggingface.co/v1/chat/completions";
        this.objectMapper = new ObjectMapper();
        this.objectMapper.enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS);
    }

    @Override
    public List<String> generarPistas(String descripcionAcertijo, String respuestaCorrecta) throws Exception {

        String prompt = String.format(
                "Eres un asistente para un juego de escape room. Te daré un acertijo y su respuesta. "
                        + "Acertijo: \"%s\". "
                        + "Respuesta: \"%s\". "
                        + "Tu tarea es generar 3 pistas creativas en español para ese acertijo. "
                        + "RESPONDE SOLO CON UN JSON EXACTO, con una única clave \"pistas\" que sea un array de 3 strings. "
                        + "Ejemplo: {\"pistas\":[\"pista 1\",\"pista 2\",\"pista 3\"]}",
                descripcionAcertijo, respuestaCorrecta
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(this.apiKey);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "MiniMaxAI/MiniMax-M2:novita");

        Map<String, String> userMessage = new HashMap<>();
        userMessage.put("role", "user");
        userMessage.put("content", prompt);

        requestBody.put("messages", new Object[]{userMessage});
        requestBody.put("max_tokens", 1024);
        requestBody.put("temperature", 0.7);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        try {
            String rawResponse = restTemplate.postForObject(this.apiUrl, requestEntity, String.class);

            JsonNode root = objectMapper.readTree(rawResponse);
            String content = root.path("choices").get(0).path("message").path("content").asText();

            String json = extractJsonFromText(content);
            if (json.isEmpty()) {
                throw new RuntimeException("La IA no devolvió un JSON válido: " + rawResponse);
            }

            JsonNode pistasJsonNode = objectMapper.readTree(json);
            List<String> nuevasPistas = new ArrayList<>();

            JsonNode pistasNode = pistasJsonNode.path("pistas");
            if (pistasNode.isArray()) {
                for (JsonNode pistaNode : pistasNode) {
                    nuevasPistas.add(pistaNode.asText());
                }
            }

            if (nuevasPistas.isEmpty()) {
                throw new RuntimeException("La IA devolvió un JSON pero sin la clave 'pistas' o estaba vacía.");
            }

            return nuevasPistas;

        } catch (HttpClientErrorException e) {
            throw new RuntimeException("Error al llamar a la IA: revisa el token o el modelo.", e);
        } catch (Exception e) {
            throw new Exception("Error inesperado en ServicioGeneradorIA: " + e.getMessage(), e);
        }
    }

    private String extractJsonFromText(String text) {
        int start = text.indexOf('{');
        int end = text.lastIndexOf('}');
        if (start != -1 && end != -1 && end > start) {
            return text.substring(start, end + 1);
        }
        return "";
    }
}