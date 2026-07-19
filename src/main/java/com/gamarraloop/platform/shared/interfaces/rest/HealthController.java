package com.gamarraloop.platform.shared.interfaces.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Endpoints livianos de salud/keep-alive.
 *
 * Con el context-path {@code /api/v1}, quedan expuestos en:
 *   - GET /api/v1/        → info del servicio
 *   - GET /api/v1/health  → status UP (usado por Render healthCheckPath y el keep-alive)
 *
 * No tocan la base de datos: responden en microsegundos para no gastar recursos
 * en cada ping del bot que evita que Render duerma el servicio.
 */
@RestController
public class HealthController {

    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> root() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "online");
        response.put("service", "GamarraLoop Backend API");
        response.put("version", "1.0.0");
        response.put("timestamp", LocalDateTime.now());
        response.put("documentation", "/api/v1/swagger-ui/index.html");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("timestamp", LocalDateTime.now().toString());
        return ResponseEntity.ok(response);
    }
}
