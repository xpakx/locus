package io.github.xpakx.locus.elasticsearch;

import io.github.xpakx.locus.bookmark.BookmarkData;
import io.github.xpakx.locus.bookmark.dto.BookmarkDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchService {
    private final BookmarkCustomRepository bookmarkCustomRepository;


    public List<BookmarkDto> searchForBookmark(String searchString) {
        return bookmarkCustomRepository.searchForBookmark(searchString).stream().map(BookmarkDto::of).toList();
    }
    public List<BookmarkDto> searchForUserBookmark(String searchString, String username) {
        return bookmarkCustomRepository.searchForUserBookmark(searchString, username).stream().map(BookmarkDto::of).toList();
    }
}
