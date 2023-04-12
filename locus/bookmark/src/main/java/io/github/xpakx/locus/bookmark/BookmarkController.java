package io.github.xpakx.locus.bookmark;

import io.github.xpakx.locus.bookmark.dto.BookmarkDto;
import io.github.xpakx.locus.bookmark.dto.BookmarkRequest;
import io.github.xpakx.locus.bookmark.dto.BooleanResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

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
    public List<BookmarkDto> getBookmark(Principal principal) {
        return bookmarkService.getBookmarks(0, 20, principal.getName()).getContent();
    }

    @GetMapping("/check")
    @ResponseBody
    public BooleanResponse checkBookmarkExistence(@PathVariable String url, Principal principal) {
        return bookmarkService.checkBookmarkForUrl(url, principal.getName());
    }
}
