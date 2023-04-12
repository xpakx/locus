package io.github.xpakx.locus.bookmark.dto;

import io.github.xpakx.locus.bookmark.Tag;

public record TagDto(String name) {
    public static TagDto of(Tag tag) {
        return new TagDto(tag.getName());
    }
}
