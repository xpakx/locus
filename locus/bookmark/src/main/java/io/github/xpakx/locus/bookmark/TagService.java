package io.github.xpakx.locus.bookmark;

import io.github.xpakx.locus.bookmark.dto.TagDto;
import io.github.xpakx.locus.bookmark.dto.TagRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TagService {
    private final TagRepository tagRepository;

    public TagDto addTag(TagRequest request, String username) {
        Tag tag = new Tag();
        tag.setName(request.name());
        tag.setOwner(username);
        return TagDto.of(tagRepository.save(tag));
    }
}
