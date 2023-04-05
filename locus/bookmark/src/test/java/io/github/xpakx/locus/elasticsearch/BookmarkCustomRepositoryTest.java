package io.github.xpakx.locus.elasticsearch;

import io.github.xpakx.locus.bookmark.BookmarkData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BookmarkCustomRepositoryTest {

    @Autowired
    BookmarkCustomRepository bookmarkRepository;

    @AfterEach
    void cleanUp() throws IOException {
        bookmarkRepository.deleteAll();
    }

    @BeforeEach
    void setUp() throws IOException {
        try {
            bookmarkRepository.createIndex();
        } catch(Exception e) {
            System.out.println("TEST" + e);
        }
    }

    @Test
    void shouldSaveBookmark() {
        boolean result = bookmarkRepository.saveBookmark(getBookmark("content"));
        assertTrue(result);
    }
    @Test
    void shouldFindBookmarks() throws InterruptedException {
        bookmarkRepository.saveBookmark(getBookmark("content"));
        bookmarkRepository.saveBookmark(getBookmark("conent"));
        bookmarkRepository.saveBookmark(getBookmark("content"));
        bookmarkRepository.saveBookmark(getBookmark("something"));
        Thread.sleep(2000);
        List<BookmarkData> result = bookmarkRepository.searchForBookmark("content");
        for(var data : result) {
            System.out.println(data.getContent());
        }
        assertThat(result, hasSize(3));
    }

    private BookmarkData getBookmark(String content) {
        BookmarkData bookmark = new BookmarkData();
        bookmark.setUrl("https://example.com");
        bookmark.setContent(content);
        return bookmark;
    }
}