package io.github.xpakx.locus.importing;

import io.github.xpakx.locus.bookmark.Bookmark;
import io.github.xpakx.locus.bookmark.BookmarkRepository;
import io.github.xpakx.locus.bookmark.dto.BookmarkRequest;
import io.github.xpakx.locus.downloader.WebpageDownloader;
import io.github.xpakx.locus.elasticsearch.BookmarkESRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImportService {
    private final BookmarkHtmlExtractor htmlExtractor;
    private final BookmarkRepository bookmarkRepository;
    private final List<WebpageDownloader> downloaders;
    private final BookmarkESRepository esRepository;

    public boolean saveBookmarksFromHtml(MultipartFile file, String username) {
        try (InputStream inputStream = file.getInputStream()) {
            List<Bookmark> bookmarks = bookmarkRepository.saveAll(
                    htmlExtractor
                            .extractBookmarks(new String(inputStream.readAllBytes(), StandardCharsets.UTF_8))
                            .stream()
                            .map((a) -> toBookmark(a, username))
                            .toList()
            );
            saveToElasticSearch(bookmarks);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void saveToElasticSearch(List<Bookmark> bookmarks) {
        esRepository.saveAll(bookmarks);
    }

    public boolean saveBookmarksFromJson(List<BookmarkRequest> request, String username) {
        List<Bookmark> bookmarks = bookmarkRepository.saveAll(
                request
                        .stream()
                        .map((a) -> toBookmark(a, username))
                        .toList()
        );
        saveToElasticSearch(bookmarks);
        return true;
    }

    private Bookmark toBookmark(BookmarkRequest request, String username) {
        Bookmark bookmark = new Bookmark();
        bookmark.setUrl(request.url());
        bookmark.setDate(LocalDate.now());
        bookmark.setContent(extractContent(request.url()));
        bookmark.setOwner(username);
        return bookmark;
    }

    private String extractContent(String url) {
        return downloaders.stream()
                .filter((a) -> a.isApplicable(url))
                .map((a) -> a.getContentForUrl(url))
                .findFirst()
                .orElse("");
    }
}
