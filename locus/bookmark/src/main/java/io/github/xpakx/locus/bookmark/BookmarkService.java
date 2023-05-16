package io.github.xpakx.locus.bookmark;

import io.github.xpakx.locus.bookmark.dto.BookmarkDto;
import io.github.xpakx.locus.bookmark.dto.BookmarkRequest;
import io.github.xpakx.locus.bookmark.dto.BookmarkSummary;
import io.github.xpakx.locus.bookmark.dto.BooleanResponse;
import io.github.xpakx.locus.bookmark.error.NotFoundException;
import io.github.xpakx.locus.clients.GetContentFromWorker;
import io.github.xpakx.locus.elasticsearch.SaveToElasticSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class BookmarkService {
    private final BookmarkRepository bookmarkRepository;

    @SaveToElasticSearch
    @GetContentFromWorker
    public BookmarkDto addBookmark(BookmarkRequest request, String username) {
        Bookmark bookmark = new Bookmark();
        bookmark.setUrl(request.url());
        bookmark.setDate(LocalDate.now());
        bookmark.setOwner(username);
        return BookmarkDto.of(bookmarkRepository.save(bookmark));
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
