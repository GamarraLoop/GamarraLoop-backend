package com.gamarraloop.platform.classification.application.ports.input;

/**
 * Capacidad expuesta por el contexto de clasificación: dada la URL de una imagen,
 * llama a Vision e infiere el tipo de textil. La consume el contexto de lotes para
 * auto-clasificar al publicar.
 */
public interface TextileInferenceService {

    /**
     * @param imageUrl URL de la imagen del lote.
     * @return el tipo de textil inferido (es), o {@code null} si no hay imagen que analizar.
     */
    String inferTextileType(String imageUrl);
}
