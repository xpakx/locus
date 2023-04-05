package io.github.xpakx.locus.elasticsearch;

import io.github.xpakx.locus.bookmark.BookmarkData;
import io.github.xpakx.locus.bookmark.BookmarkRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.when;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@SpringBootTest
class BookmarkCustomRepositoryTest {

    @Autowired
    BookmarkCustomRepository bookmarkRepository;


    @Test
    void shouldSaveBookmark() {
        bookmarkRepository.saveBookmark(getBookmark("content"));
    }

    private BookmarkData getBookmark(String content) {
        BookmarkData bookmark = new BookmarkData();
        bookmark.setUrl("https://example.com");
        bookmark.setContent(content);
        return bookmark;
    }
}