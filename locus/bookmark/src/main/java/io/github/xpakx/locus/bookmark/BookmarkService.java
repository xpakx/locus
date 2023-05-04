package io.github.xpakx.locus.bookmark;

import io.github.xpakx.locus.bookmark.dto.BookmarkDto;
import io.github.xpakx.locus.bookmark.dto.BookmarkRequest;
import io.github.xpakx.locus.bookmark.dto.BookmarkSummary;
import io.github.xpakx.locus.bookmark.dto.BooleanResponse;
import io.github.xpakx.locus.bookmark.error.NotFoundException;
import io.github.xpakx.locus.downloader.WebpageDownloader;
import io.github.xpakx.locus.elasticsearch.SaveToElasticSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookmarkService {
    private final BookmarkRepository bookmarkRepository;
    private final List<WebpageDownloader> downloaders;

    @SaveToElasticSearch
    public BookmarkDto addBookmark(BookmarkRequest request, String username) {
        Bookmark bookmark = new Bookmark();
        bookmark.setUrl(request.url());
        bookmark.setDate(LocalDate.now());
        bookmark.setContent(extractContent(request.url()));
        bookmark.setOwner(username);
        return BookmarkDto.of(bookmarkRepository.save(bookmark));
    }

    private String extractContent(String url) {
        return downloaders.stream()
                .filter((a) -> a.isApplicable(url))
                .map((a) -> a.getContentForUrl(url))
                .findFirst()
                .orElse("");
    }

    public BookmarkDto getBookmarkById(Long id) {
        return BookmarkDto.of(
                bookmarkRepository
                        .findById(id)
                        .orElseThrow(() -> new NotFoundException("No bookmark with id %s".formatted(id)))
        );
    }

    public Page<BookmarkSummary> getBookmarks(Integer page, Integer amount, String username) {
        return bookmarkRepository.findByOwner(
                username,
                PageRequest.of(
                        page,
                        amount,
                        Sort.by(Sort.Order.asc("date"))
                )
        );
    }

    public BooleanResponse checkBookmarkForUrl(String url, String username) {
        return new BooleanResponse(
                bookmarkRepository
                        .existsByUrlAndOwner(url, username)
        );
    }

    public void deleteBookmark(Long id, String username) {
        Bookmark bookmark = bookmarkRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("No bookmark with id %s".formatted(id)));
        if(!bookmark.getOwner().equals(username)) {
            throw new NotFoundException("No bookmark with id %s".formatted(id));
        }
        bookmarkRepository.delete(bookmark);
    }
}
