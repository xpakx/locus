package io.github.xpakx.locus.elasticsearch;

import io.github.xpakx.locus.annotation.Highlight;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class ElasticSearchAspect {
    private final HighlightESRepository highlightESRepository;

    @AfterReturning(value="@annotation(SaveToElasticSearch)", returning = "response")
    public void saveObject(Object response) {
        if(response instanceof Highlight highlight) {
            saveHighlight(highlight);
        }
    }
    private void saveHighlight(Highlight bookmark) {
        highlightESRepository.saveHighlight(toHighlightData(bookmark));
    }

    private HighlightData toHighlightData(Highlight bookmark) {
        HighlightData data = new HighlightData();
        data.setDbId(bookmark.getId());
        data.setText(bookmark.getText());
        data.setAnnotation(bookmark.getAnnotation());
        data.setOwner(bookmark.getOwner());
        data.setUrl(bookmark.getUrl());
        return data;
    }
}
