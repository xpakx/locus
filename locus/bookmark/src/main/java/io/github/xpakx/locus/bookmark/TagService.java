package io.github.xpakx.locus.bookmark;

import io.github.xpakx.locus.bookmark.dto.BookmarkDto;
import io.github.xpakx.locus.bookmark.dto.BookmarkSummary;
import io.github.xpakx.locus.bookmark.dto.TagRequest;
import io.github.xpakx.locus.bookmark.error.NotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TagService {
    private final TagRepository tagRepository;
    private final BookmarkRepository bookmarkRepository;

    @Transactional
    public BookmarkDto addTag(TagRequest request, String username, Long bookmarkId) {
        Bookmark bookmark = bookmarkRepository.findById(bookmarkId)
                .orElseThrow(NotFoundException::new);
        if(!bookmark.getOwner().equals(username)) {
            throw new NotFoundException();
        }
        Tag tag = tagRepository.findByNameAndOwner(request.name(), username)
                .orElse(createNewTag(request, username));
        tagRepository.tagBookmark(bookmark.getId(), tag.getId());
        return BookmarkDto.of(bookmark);
    }

    private Tag createNewTag(TagRequest request, String username) {
        Tag tag = new Tag();
        tag.setName(request.name());
        tag.setOwner(username);
        return tagRepository.save(tag);
    }

    public Page<BookmarkSummary> getBookmarksTaggedAs(int page, int amount, String tagName, String username) {
        return bookmarkRepository.findByOwnerAndTagsName(
                username,
                tagName,
                PageRequest.of(
                        page,
                        amount,
                        Sort.by(Sort.Order.asc("date"))
                )
        );
    }


    @Transactional
    public BookmarkDto untag(String tagName, String username, Long bookmarkId) {
        Bookmark bookmark = bookmarkRepository.findById(bookmarkId)
                .orElseThrow(NotFoundException::new);
        if(!bookmark.getOwner().equals(username)) {
            throw new NotFoundException();
        }
        Tag tag = tagRepository.findByNameAndOwner(tagName, username)
                .orElseThrow(() -> new NotFoundException("There is no such tag"));
        tagRepository.untagBookmark(bookmark.getId(), tag.getId());
        return BookmarkDto.of(bookmark);
    }
}
