package com.gamarraloop.platform.classification.domain.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Infiere el tipo de textil (en español, para mostrar en la app) a partir de las
 * etiquetas que devuelve Google Cloud Vision (en inglés). Lógica de dominio
 * compartida por la auto-clasificación de lotes y el ensamblador de clasificación.
 */
public final class TextileTypeInference {

    private static final Logger logger = LoggerFactory.getLogger(TextileTypeInference.class);
    private static final ObjectMapper MAPPER = new ObjectMapper();
    public static final String UNKNOWN = "MIXTO";

    /** Orden importa: tipos específicos (denim) antes que genéricos. */
    private static final Map<String, String> TEXTILE_KEYWORDS = new LinkedHashMap<>();
    static {
        TEXTILE_KEYWORDS.put("denim", "Mezclilla");
        TEXTILE_KEYWORDS.put("jean", "Mezclilla");
        TEXTILE_KEYWORDS.put("cotton", "Algodón");
        TEXTILE_KEYWORDS.put("wool", "Lana");
        TEXTILE_KEYWORDS.put("linen", "Lino");
        TEXTILE_KEYWORDS.put("flax", "Lino");
        TEXTILE_KEYWORDS.put("polyester", "Poliéster");
        TEXTILE_KEYWORDS.put("silk", "Seda");
        TEXTILE_KEYWORDS.put("leather", "Cuero");
        TEXTILE_KEYWORDS.put("nylon", "Nylon");
        TEXTILE_KEYWORDS.put("knit", "Tejido de punto");
        TEXTILE_KEYWORDS.put("fleece", "Polar");
    }

    private TextileTypeInference() {
    }

    /** Devuelve el tipo de textil inferido, o {@link #UNKNOWN} si no hay coincidencia. */
    public static String fromLabelsJson(String labelsJson) {
        if (labelsJson == null || labelsJson.isBlank()) {
            return UNKNOWN;
        }
        try {
            JsonNode arr = MAPPER.readTree(labelsJson);
            if (arr.isArray()) {
                for (JsonNode label : arr) {
                    String description = label.path("description").asText("").toLowerCase();
                    for (Map.Entry<String, String> entry : TEXTILE_KEYWORDS.entrySet()) {
                        if (description.contains(entry.getKey())) {
                            return entry.getValue();
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.warn("No se pudieron parsear las labels de Vision: {}", e.getMessage());
        }
        return UNKNOWN;
    }
}
