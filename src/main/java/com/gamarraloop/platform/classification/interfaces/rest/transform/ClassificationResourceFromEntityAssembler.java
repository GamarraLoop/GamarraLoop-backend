package com.gamarraloop.platform.classification.interfaces.rest.transform;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gamarraloop.platform.classification.domain.model.aggregate.ClassificationRequest;
import com.gamarraloop.platform.classification.domain.services.TextileTypeInference;
import com.gamarraloop.platform.classification.interfaces.rest.resources.ClassificationResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClassificationResourceFromEntityAssembler {

    private static final Logger logger = LoggerFactory.getLogger(ClassificationResourceFromEntityAssembler.class);
    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static ClassificationResource toResourceFromEntity(ClassificationRequest entity) {
        String aiDesc = "Retazos de tela";
        // Tipo inferido con la lógica de dominio compartida.
        String aiType = TextileTypeInference.fromLabelsJson(entity.getLabels());
        double aiConfidence = 0.0;

        try {
            if (entity.getLabels() != null) {
                JsonNode node = MAPPER.readTree(entity.getLabels());
                if (node.isArray() && node.size() > 0) {
                    // Descripción: concatena las 3 etiquetas con mayor confianza.
                    StringBuilder desc = new StringBuilder();
                    for (int i = 0; i < Math.min(3, node.size()); i++) {
                        if (i > 0) desc.append(", ");
                        desc.append(node.get(i).path("description").asText());
                    }
                    aiDesc = desc.toString();
                    // Confianza de la etiqueta principal (Vision las ordena desc).
                    aiConfidence = node.get(0).path("score").asDouble(0.0);
                }
            }
        } catch (Exception e) {
            logger.warn("No se pudieron parsear las labels de la clasificación {}: {}",
                    entity.getId(), e.getMessage());
        }

        return new ClassificationResource(
                entity.getId(),
                entity.getImageUrl(),
                entity.getStatus().name(),
                aiDesc,
                aiType,
                aiConfidence
        );
    }
}
