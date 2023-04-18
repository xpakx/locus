package io.github.xpakx.locus.elasticsearch;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/annotations")
@RequiredArgsConstructor
public class SearchController {
    private final SearchService service;

    @GetMapping
    @ResponseBody
    public List<HighlightData> searchContent(@RequestParam String searchString,
                                           @RequestParam Optional<Integer> page,
                                           @RequestParam Optional<Integer> amount,
                                           Principal principal) {
        return service.searchForUserHighlights(
                searchString,
                principal.getName(),
                page.orElse(0),
                amount.orElse(20)
        );
    }
}
