package io.github.xpakx.locus.bookmark;

import io.github.xpakx.locus.bookmark.dto.BookmarkDto;
import io.github.xpakx.locus.bookmark.dto.BookmarkRequest;
import io.github.xpakx.locus.bookmark.dto.BookmarkSummary;
import io.github.xpakx.locus.bookmark.dto.BooleanResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/bookmarks")
public class BookmarkController {
    private final BookmarkService bookmarkService;

    @PostMapping
    @ResponseBody
    public BookmarkDto saveBookmark(@RequestBody BookmarkRequest request, Principal principal) {
        return bookmarkService.addBookmark(request, principal.getName());
    }

    @GetMapping("/{bookmarkId}")
    @ResponseBody
    @PostAuthorize("returnObject.owner == authentication.principal.username")
    public BookmarkDto getBookmarkById(@PathVariable Long bookmarkId) {
        return bookmarkService.getBookmarkById(bookmarkId);
    }

    @GetMapping("/all")
    @ResponseBody
    public List<BookmarkSummary> getAllBookmarks(@RequestParam Optional<Integer> page,
                                                 @RequestParam Optional<Integer> amount,
                                                 Principal principal) {
        return bookmarkService.getBookmarks(
                page.orElse(0),
                amount.orElse(20),
                principal.getName()
        ).getContent();
    }

    @GetMapping("/check")
    @ResponseBody
    public BooleanResponse checkBookmarkExistence(@RequestParam String url, Principal principal) {
        return bookmarkService.checkBookmarkForUrl(url, principal.getName());
    }

    @DeleteMapping("/{bookmarkId}")
    public void deleteBookmark(@PathVariable Long bookmarkId, Principal principal) {
        bookmarkService.deleteBookmark(bookmarkId, principal.getName());
    }
}
