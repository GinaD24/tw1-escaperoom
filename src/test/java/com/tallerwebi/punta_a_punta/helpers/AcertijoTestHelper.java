package com.tallerwebi.punta_a_punta.helpers;

import java.util.Map;

public class AcertijoTestHelper {

    private static final Map<String, String> RESPUESTAS = Map.of(
            "Poseo un rostro sin alma y te devuelvo la mirada sin pestañear. Contengo tu reflejo, pero mi interior es un vacío helado.",
            "Espejo",
            "Mis dos manos recorren mi rostro sin cesar, devorando el tiempo que no volverá. Mi voz es un pulso constante que mide tu estadía en este lugar.",
            "Reloj",
            "Nací de la luz, pero vivo en la penumbra. Soy tu fiel compañera bajo el sol, pero la oscuridad total me hace desaparecer.",
            "Sombra"
    );

    public static String obtenerRespuesta(String acertijo) {
        return RESPUESTAS.get(acertijo);
    }
}
