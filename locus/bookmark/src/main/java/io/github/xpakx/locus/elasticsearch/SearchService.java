package io.github.xpakx.locus.elasticsearch;

import io.github.xpakx.locus.bookmark.BookmarkData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchService {
    private final BookmarkCustomRepository bookmarkCustomRepository;


    public List<BookmarkData> searchForBookmark(String searchString) {
        return bookmarkCustomRepository.searchForBookmark(searchString);
    }
}
