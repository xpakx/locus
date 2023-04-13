package io.github.xpakx.locus.annotation;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum HighlightType {
    TEXT("Text"),
    VIDEO("Video");

    private final String type;
}
