package io.github.xpakx.locus.bookmark;

import io.github.xpakx.locus.bookmark.dto.BookmarkDto;
import io.github.xpakx.locus.bookmark.dto.TagDto;
import io.github.xpakx.locus.bookmark.dto.TagRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/bookmarks")
@RequiredArgsConstructor
public class TagController {
    private final TagService service;

    @PostMapping("/{bookmarkId}/tags")
    @ResponseBody
    public BookmarkDto addTag(@RequestBody TagRequest request, @PathVariable Long bookmarkId, Principal principal) {
        return service.addTag(request, principal.getName(), bookmarkId);
    }

    @GetMapping("/tagged/{tagName}")
    @ResponseBody
    public List<BookmarkDto> getBookmark(@PathVariable String tagName, Principal principal) {
        return service.getBookmarksTaggedAs(0, 20, tagName, principal.getName()).getContent();
    }

}
