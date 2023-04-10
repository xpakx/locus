package io.github.xpakx.locus.elasticsearch;

import io.github.xpakx.locus.bookmark.dto.BookmarkDto;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.List;

@Aspect
@Component
@RequiredArgsConstructor
public class ElasticSearchAspect {
    private final BookmarkESRepository bookmarkEsRepository;

    @AfterReturning(value="@annotation(SaveToElasticSearch)", returning = "response")
    public void saveObject(Object response) {
        if(response instanceof BookmarkDto bookmark) {
            saveBookmark(bookmark);
        } else if (response instanceof List list && list.size() > 0 && list.get(0) instanceof BookmarkDto) {
            saveBookmarks(list);
        }
    }

    private void saveBookmarks(List<BookmarkDto> list) {
        bookmarkEsRepository.saveAll(
                list
                        .stream()
                        .map(this::toBookmarkData)
                        .toList()
        );
    }

    private void saveBookmark(BookmarkDto bookmark) {
        bookmarkEsRepository.saveBookmark(toBookmarkData(bookmark));
    }

    private BookmarkData toBookmarkData(BookmarkDto bookmark) {
        BookmarkData data = new BookmarkData();
        data.setContent(bookmark.content());
        data.setDbId(bookmark.id());
        data.setUrl(bookmark.url());
        data.setDate(bookmark.date());
        data.setOwner(bookmark.owner());
        return data;
    }
}
