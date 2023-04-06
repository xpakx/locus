package io.github.xpakx.locus.bookmark.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.xpakx.locus.bookmark.Bookmark;
import io.github.xpakx.locus.elasticsearch.BookmarkData;

import java.time.LocalDate;

public record BookmarkDto(Long id, String url, LocalDate date, String content, @JsonIgnore String owner) {
    public static BookmarkDto of(Bookmark bookmark) {
        return new BookmarkDto(
                bookmark.getId(),
                bookmark.getUrl(),
                bookmark.getDate(),
                bookmark.getContent(),
                bookmark.getOwner()
        );
    }

    public static BookmarkDto of(BookmarkData bookmark) {
        return new BookmarkDto(
                bookmark.getDbId(),
                bookmark.getUrl(),
                bookmark.getDate(),
                bookmark.getContent(),
                ""
        );
    }
}
