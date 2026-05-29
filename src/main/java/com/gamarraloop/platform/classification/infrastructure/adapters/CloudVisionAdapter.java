package com.gamarraloop.platform.classification.infrastructure.adapters;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gamarraloop.platform.classification.application.ports.output.VisionApiPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Service
public class CloudVisionAdapter implements VisionApiPort {

    private final WebClient webClient;
    private final ObjectMapper objectMapper;
    private final String apiKey;

    public CloudVisionAdapter(WebClient.Builder webClientBuilder,
                              ObjectMapper objectMapper,
                              @Value("${google.cloud.vision.api-key}") String apiKey) {
        this.webClient = webClientBuilder
                .baseUrl("https://vision.googleapis.com")
                .build();
        this.objectMapper = objectMapper;
        this.apiKey = apiKey;
    }

    @Override
    public String analyzeImage(String imageUrl) {
        Map<String, Object> requestBody = Map.of(
                "requests", List.of(
                        Map.of(
                                "image", Map.of(
                                        "source", Map.of("imageUri", imageUrl)
                                ),
                                "features", List.of(
                                        Map.of("type", "LABEL_DETECTION", "maxResults", 15),
                                        Map.of("type", "IMAGE_PROPERTIES", "maxResults", 1)
                                )
                        )
                )
        );

        String responseBody = webClient.post()
                .uri("/v1/images:annotate?key={apiKey}", apiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        try {
            JsonNode root = objectMapper.readTree(responseBody);
            JsonNode responses = root.path("responses");

            if (responses.isEmpty() || responses.get(0).has("error")) {
                String errorMessage = responses.isEmpty()
                        ? "Empty response from Cloud Vision API"
                        : responses.get(0).path("error").path("message").asText("Unknown Vision API error");
                throw new RuntimeException("Cloud Vision API error: " + errorMessage);
            }

            JsonNode labelAnnotations = responses.get(0).path("labelAnnotations");
            return objectMapper.writeValueAsString(labelAnnotations);

        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse Cloud Vision API response: " + e.getMessage(), e);
        }
    }
}
