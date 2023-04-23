package io.github.xpakx.locus.annotation.dto;

import java.util.Optional;

public record VideoHighlightRequest(String url, Integer timestamp, Optional<String> annotation) implements BasicAnnotationRequest {
}
