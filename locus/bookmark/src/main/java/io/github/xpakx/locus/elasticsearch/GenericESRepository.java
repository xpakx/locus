package io.github.xpakx.locus.elasticsearch;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import io.github.xpakx.locus.bookmark.BookmarkData;
import lombok.RequiredArgsConstructor;

import java.io.IOException;

@RequiredArgsConstructor
public abstract class GenericESRepository<T> {
    private final ElasticsearchClient elasticsearchClient;
    protected SearchResponse<BookmarkData> doFuzzySearch(String searchString, String index, String field) throws IOException {
        return elasticsearchClient.search(s -> s
                        .index(index)
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
