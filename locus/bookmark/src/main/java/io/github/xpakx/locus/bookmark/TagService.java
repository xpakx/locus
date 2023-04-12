package io.github.xpakx.locus.bookmark;

import io.github.xpakx.locus.bookmark.dto.BookmarkDto;
import io.github.xpakx.locus.bookmark.dto.TagDto;
import io.github.xpakx.locus.bookmark.dto.TagRequest;
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

    public TagDto addTag(TagRequest request, String username) {
        Tag tag = new Tag();
        tag.setName(request.name());
        tag.setOwner(username);
        return TagDto.of(tagRepository.save(tag));
    }

    public Page<BookmarkDto> getBookmarksTaggedAs(int page, int amount, String tagName, String username) {
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
}
