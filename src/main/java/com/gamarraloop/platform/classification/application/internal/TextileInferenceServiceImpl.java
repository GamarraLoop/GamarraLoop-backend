package com.gamarraloop.platform.classification.application.internal;

import com.gamarraloop.platform.classification.application.ports.input.TextileInferenceService;
import com.gamarraloop.platform.classification.application.ports.output.VisionApiPort;
import com.gamarraloop.platform.classification.domain.services.TextileTypeInference;
import org.springframework.stereotype.Service;

@Service
public class TextileInferenceServiceImpl implements TextileInferenceService {

    private final VisionApiPort visionApiPort;

    public TextileInferenceServiceImpl(VisionApiPort visionApiPort) {
        this.visionApiPort = visionApiPort;
    }

    @Override
    public String inferTextileType(String imageUrl) {
        if (imageUrl == null || imageUrl.isBlank()) {
            return null;
        }
        String labelsJson = visionApiPort.analyzeImage(imageUrl);
        return TextileTypeInference.fromLabelsJson(labelsJson);
    }
}
