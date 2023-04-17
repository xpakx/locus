package io.github.xpakx.locus.bookmark;

import io.github.xpakx.locus.bookmark.dto.BookmarkDto;
import io.github.xpakx.locus.bookmark.dto.BookmarkSummary;
import io.github.xpakx.locus.bookmark.dto.BooleanResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    Page<BookmarkSummary> findByOwner(String owner, Pageable pageable);

    Page<BookmarkSummary> findByOwnerAndTagsName(String owner, String tag, Pageable pageable);



    boolean existsByUrlAndOwner(String url, String owner);

}