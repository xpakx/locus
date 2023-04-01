package io.github.xpakx.locus.bookmark;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import io.github.xpakx.locus.bookmark.error.ESException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class BookmarkCustomRepository {
    private final ElasticsearchClient elasticsearchClient;
    public List<BookmarkData> searchForBookmark(String searchString) {
        try {
            SearchResponse<BookmarkData> response = doFuzzySearch(searchString, "content");
            return response.hits().hits().stream().map(Hit::source).toList();
        } catch(IOException exception) {
            throw new ESException();
        }
    }

    private SearchResponse<BookmarkData> doFuzzySearch(String searchString, String field) throws IOException {
        return elasticsearchClient.search(s -> s
                        .index("bookmarks")
                        .query(q -> q
                                .fuzzy(f -> f
                                        .field(field)
                                        .value(searchString)
                                )
                        ),
                BookmarkData.class
        );
    }
}
