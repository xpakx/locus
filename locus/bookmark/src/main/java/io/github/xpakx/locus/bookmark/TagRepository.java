package io.github.xpakx.locus.bookmark;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findByNameAndOwner(String name, String owner);

    @Modifying
    @Query(nativeQuery = true, value = "INSERT INTO bookmark_tags(bookmark_id, tag_id) VALUES (:bookmarkId, :tagId);")
    void tagBookmark(Long bookmarkId, Long tagId);

    @Modifying
    @Query(
            nativeQuery = true,
            value = "DELETE FROM bookmark_tags AS bt WHERE bt.bookmark_id = :bookmarkId AND bt.tag_id = :tagId ;"
    )
    void untagBookmark(Long bookmarkId, Long tagId);
}