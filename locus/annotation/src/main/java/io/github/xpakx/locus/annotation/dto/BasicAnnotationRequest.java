package io.github.xpakx.locus.annotation.dto;

import java.util.Optional;

public interface BasicAnnotationRequest {
    String url();
    Optional<String> annotation();
}
