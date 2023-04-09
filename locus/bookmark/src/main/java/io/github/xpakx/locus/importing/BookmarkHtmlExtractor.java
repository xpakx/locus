package io.github.xpakx.locus.importing;

import io.github.xpakx.locus.bookmark.dto.BookmarkRequest;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookmarkHtmlExtractor {

    public List<BookmarkRequest> extractBookmarks(String html) {
        return Jsoup
                .parse(html)
                .getElementsByTag("a")
                .stream()
                .map(this::toBookmark)
                .toList();
    }

    private BookmarkRequest toBookmark(Element link) {
        return new BookmarkRequest(link.attr("href"));
    }
}
