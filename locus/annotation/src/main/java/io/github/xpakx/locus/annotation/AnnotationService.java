package io.github.xpakx.locus.annotation;

import io.github.xpakx.locus.annotation.dto.HighlightRequest;
import io.github.xpakx.locus.annotation.dto.VideoHighlightRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AnnotationService {
    private final HighlightRepository highlightRepository;

    public Highlight addHighlight(HighlightRequest request, String username) {
        Highlight highlight = new Highlight();
        highlight.setAnnotation(request.annotation().orElse(null));
        highlight.setText(request.highlightedText());
        highlight.setOwner(username);
        highlight.setType(HighlightType.TEXT);
        highlight.setCreatedAt(LocalDateTime.now());
        return highlightRepository.save(highlight);
    }
    public Highlight addTimestampedHighlight(VideoHighlightRequest request, String username) {
        Highlight highlight = new Highlight();
        highlight.setAnnotation(request.annotation().orElse(null));
        highlight.setText(request.highlightedText());
        highlight.setOwner(username);
        highlight.setType(HighlightType.VIDEO);
        highlight.setTimestamp(request.timestamp());
        highlight.setCreatedAt(LocalDateTime.now());
        return highlightRepository.save(highlight);
    }



}
