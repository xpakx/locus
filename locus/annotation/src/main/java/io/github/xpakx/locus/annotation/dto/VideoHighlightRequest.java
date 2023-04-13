package io.github.xpakx.locus.annotation.dto;

import java.util.Optional;

public record VideoHighlightRequest(String url, String highlightedText, Integer timestamp, Optional<String> annotation) {
}
