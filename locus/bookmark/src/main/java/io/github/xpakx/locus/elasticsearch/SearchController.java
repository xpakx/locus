package io.github.xpakx.locus.elasticsearch;

import io.github.xpakx.locus.bookmark.BookmarkData;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/bookmarks")
@RequiredArgsConstructor
public class SearchController {
    private final SearchService service;

    @GetMapping
    @ResponseBody
    public List<BookmarkData> searchContent(@RequestParam String searchString) {
        return service.searchForBookmark(searchString);
    }
}
