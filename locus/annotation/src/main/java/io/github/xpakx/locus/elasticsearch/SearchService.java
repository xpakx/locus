package io.github.xpakx.locus.elasticsearch;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchService {
    private final HighlightESRepository bookmarkCustomRepository;

    public List<HighlightData> searchForUserHighlights(String searchString, String username, Integer page, Integer amount) {
        return bookmarkCustomRepository
                .searchForUserHighlight(searchString, username, page*amount, amount);
    }
}
