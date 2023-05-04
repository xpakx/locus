package io.github.xpakx.locus.annotation;

import io.github.xpakx.locus.annotation.dto.BasicAnnotationRequest;
import io.github.xpakx.locus.annotation.dto.HighlightRequest;
import io.github.xpakx.locus.annotation.dto.VideoHighlightRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AnnotationService {
    private final HighlightRepository highlightRepository;

    public Highlight addHighlight(HighlightRequest request, String username) {
        Highlight highlight = createHighlightFromRequest(request, username, HighlightType.TEXT);
        highlight.setText(request.highlightedText());
        highlight.setSelectionStart(request.selectionStart());
        highlight.setSelectionEnd(request.selectionEnd());
        highlight.setStartElement(request.startElement());
        highlight.setEndElement(request.endElement());
        return highlightRepository.save(highlight);
    }

    public Highlight addTimestampedHighlight(VideoHighlightRequest request, String username) {
        Highlight highlight = createHighlightFromRequest(request, username, HighlightType.VIDEO);
        highlight.setTimestamp(request.timestamp());
        return highlightRepository.save(highlight);
    }

    private static Highlight createHighlightFromRequest(BasicAnnotationRequest request, String username, HighlightType type) {
        Highlight highlight = new Highlight();
        highlight.setAnnotation(request.annotation().orElse(null));
        highlight.setOwner(username);
        highlight.setType(type);
        highlight.setCreatedAt(LocalDateTime.now());
        highlight.setUrl(request.url());
        return highlight;
    }

    public List<Highlight> getAnnotationsForUrl(String url, String username) {
        return highlightRepository.findByUrlAndOwner(url, username);
    }

    public void deleteAnnotation(Long id, String username) {
        Highlight highlight = highlightRepository.findById(id)
                .orElseThrow();
        if(!highlight.getOwner().equals(username)) {
            return;
        }
        highlightRepository.delete(highlight);
    }
}
