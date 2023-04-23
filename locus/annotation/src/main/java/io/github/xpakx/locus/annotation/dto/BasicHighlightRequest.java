package io.github.xpakx.locus.annotation.dto;

import java.util.Optional;

public interface BasicHighlightRequest {
    String url();
    String highlightedText();
    Optional<String> annotation();
}
