package io.github.xpakx.locus.bookmark;

import io.github.xpakx.locus.bookmark.dto.BookmarkDto;
import io.github.xpakx.locus.bookmark.dto.BookmarkSummary;
import io.github.xpakx.locus.bookmark.dto.TagRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

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
    public List<BookmarkSummary> getBookmarksTaggedAs(@RequestParam Optional<Integer> page,
                                                      @RequestParam Optional<Integer> amount,
                                                      @PathVariable String tagName,
                                                      Principal principal) {
        return service.getBookmarksTaggedAs(
                page.orElse(0),
                amount.orElse(20),
                tagName,
                principal.getName()
        ).getContent();
    }

    @DeleteMapping("/{bookmarkId}/tags/{tag}")
    @ResponseBody
    public BookmarkDto untag(@PathVariable Long bookmarkId, @PathVariable String tag, Principal principal) {
        return service.untag(tag, principal.getName(), bookmarkId);
    }
}
