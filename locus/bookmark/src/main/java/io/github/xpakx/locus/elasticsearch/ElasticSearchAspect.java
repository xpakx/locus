package io.github.xpakx.locus.elasticsearch;

import io.github.xpakx.locus.bookmark.Bookmark;
import io.github.xpakx.locus.bookmark.BookmarkData;
import io.github.xpakx.locus.bookmark.dto.BookmarkDto;
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
        if(response instanceof BookmarkDto bookmark) {
            saveBookmark(bookmark);
        }
    }

    private void saveBookmark(BookmarkDto bookmark) {
        BookmarkData data = new BookmarkData();
        data.setContent(bookmark.content());
        data.setDbId(bookmark.id());
        data.setUrl(bookmark.url());
        bookmarkEsRepository.saveBookmark(data);
    }
}
