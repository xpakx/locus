package io.github.xpakx.locus.bookmark;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findByName(String name);

    @Modifying
    @Query(nativeQuery = true, value = "INSERT INTO bookmark_tags(bookmark_id, tag_id) VALUES (:bookmarkId, :tagId);")
    void tagBookmark(Long bookmarkId, Long tagId);
}