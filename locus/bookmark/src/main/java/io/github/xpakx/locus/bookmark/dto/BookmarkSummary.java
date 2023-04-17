package io.github.xpakx.locus.bookmark.dto;

import io.github.xpakx.locus.bookmark.Bookmark;

import java.time.LocalDate;

/**
 * A Projection for the {@link Bookmark} entity
 */
public interface BookmarkSummary {
    Long getId();

    String getUrl();

    LocalDate getDate();
}