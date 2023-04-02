package io.github.xpakx.locus.elasticsearch;

import io.github.xpakx.locus.bookmark.Bookmark;
import io.github.xpakx.locus.bookmark.BookmarkData;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class ElasticSearchAspect {
    private final BookmarkCustomRepository bookmarkEsRepository;

    @AfterReturning(value="@annotation(SaveToElasticSearch)", returning = "response")
    public void saveObject(Object response) {
        if(response instanceof Bookmark bookmark) {
            saveBookmark(bookmark);
        }
    }

    private void saveBookmark(Bookmark bookmark) {
        BookmarkData data = new BookmarkData();
        data.setContent(bookmark.getContent());
        data.setDbId(bookmark.getId());
        data.setUrl(bookmark.getUrl());
        bookmarkEsRepository.saveBookmark(data);
    }
}
