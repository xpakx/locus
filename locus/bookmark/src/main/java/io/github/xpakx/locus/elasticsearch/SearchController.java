package io.github.xpakx.locus.elasticsearch;

import io.github.xpakx.locus.bookmark.BookmarkData;
import io.github.xpakx.locus.bookmark.dto.BookmarkDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/bookmarks")
@RequiredArgsConstructor
public class SearchController {
    private final SearchService service;

    @GetMapping
    @ResponseBody
    public List<BookmarkDto> searchContent(@RequestParam String searchString, Principal principal) {
        return service.searchForUserBookmark(searchString, principal.getName());
    }
}
