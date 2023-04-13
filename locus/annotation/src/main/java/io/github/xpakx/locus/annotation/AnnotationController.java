package io.github.xpakx.locus.annotation;

import io.github.xpakx.locus.annotation.dto.HighlightRequest;
import io.github.xpakx.locus.annotation.dto.VideoHighlightRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/annotations")
public class AnnotationController {
    private final AnnotationService service;

    @PostMapping
    @ResponseBody
    public Highlight annotatePage(@RequestBody HighlightRequest request, Principal principal) {
        return service.addHighlight(request, principal.getName());
    }
    @PostMapping("/video")
    @ResponseBody
    public Highlight annotateVideo(@RequestBody VideoHighlightRequest request, Principal principal) {
        return service.addTimestampedHighlight(request, principal.getName());
    }
}
