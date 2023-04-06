package io.github.xpakx.locus.bookmark;

import io.github.xpakx.locus.bookmark.dto.BookmarkDto;
import io.github.xpakx.locus.bookmark.dto.BookmarkRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

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
}
