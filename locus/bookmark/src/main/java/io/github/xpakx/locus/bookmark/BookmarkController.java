package io.github.xpakx.locus.bookmark;

import io.github.xpakx.locus.bookmark.dto.BookmarkRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/bookmarks")
public class BookmarkController {
    private final BookmarkService bookmarkService;

    @PostMapping
    @ResponseBody
    public Bookmark saveBookmark(@RequestBody BookmarkRequest request) {
        return bookmarkService.addBookmark(request);
    }

    @GetMapping("/{bookmarkId}")
    @ResponseBody
    public Bookmark getBookmarkById(@PathVariable Long bookmarkId) {
        return bookmarkService.getBookmarkById(bookmarkId);
    }
}
