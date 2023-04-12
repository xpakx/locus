package io.github.xpakx.locus.bookmark;

import io.github.xpakx.locus.bookmark.dto.TagDto;
import io.github.xpakx.locus.bookmark.dto.TagRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/tags/bookmarks")
@RequiredArgsConstructor
public class TagController {
    private final TagService service;

    @PostMapping
    @ResponseBody
    public TagDto addTag(@RequestBody TagRequest request, Principal principal) {
        return service.addTag(request, principal.getName());
    }

}
