package io.github.xpakx.locus.annotation.dto;

import java.util.Optional;

public record HighlightRequest(String url, String highlightedText, Optional<String> annotation) {
}
