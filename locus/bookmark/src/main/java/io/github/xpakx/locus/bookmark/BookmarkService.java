package io.github.xpakx.locus.bookmark;

import io.github.xpakx.locus.bookmark.dto.BookmarkRequest;
import io.github.xpakx.locus.bookmark.error.NotFoundException;
import io.github.xpakx.locus.downloader.WebpageDownloader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookmarkService {
    private final BookmarkRepository bookmarkRepository;
    private final BookmarkElasticsearchRepository bookmarkEsRepository;
    private final List<WebpageDownloader> downloaders;

    public Bookmark addBookmark(BookmarkRequest request) {
        Bookmark bookmark = new Bookmark();
        bookmark.setUrl(request.url());
        bookmark.setDate(LocalDate.now());
        bookmark.setContent(extractContent(request.url()));
        bookmark = bookmarkRepository.save(bookmark);
        saveToES(bookmark); // TODO: extract to aspect
        return bookmark;
    }

    private void saveToES(Bookmark bookmark) {
        BookmarkData data = new BookmarkData();
        data.setContent(bookmark.getContent());
        data.setDbId(bookmark.getId());
        bookmarkEsRepository.save(data);
    }

    private String extractContent(String url) {
        return downloaders.stream()
                .filter((a) -> a.isApplicable(url))
                .map((a) -> a.getContentForUrl(url))
                .findFirst()
                .orElse("");
    }

    public Bookmark getBookmarkById(Long id) {
        return bookmarkRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("No bookmark with id %s".formatted(id)));
    }
}
