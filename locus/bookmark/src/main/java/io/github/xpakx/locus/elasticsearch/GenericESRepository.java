package io.github.xpakx.locus.elasticsearch;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import lombok.RequiredArgsConstructor;

import java.io.IOException;

@RequiredArgsConstructor
public abstract class GenericESRepository<T> {
    private final ElasticsearchClient elasticsearchClient;
    private final Class<T> clazz;
    private final String index;

    protected SearchResponse<T> doFuzzySearch(String searchString, String field) throws IOException {
        return elasticsearchClient.search(s -> s
                        .index(index)
                        .query(q -> q
                                .fuzzy(f -> f
                                        .field(field)
                                        .value(searchString)
                                )
                        ),
                clazz
        );
    }

    protected SearchResponse<T> doFuzzySearchWithRequiredField(String searchString, String searchField, String requiredField, String requiredValue) throws IOException {
        return elasticsearchClient.search(s -> s
                        .index(index)
                        .query(q -> q
                                .bool(b -> b
                                        .must(m -> m
                                                .term(t -> t
                                                        .field(requiredField)
                                                        .value(requiredValue)
                                                )
                                        )
                                        .should(m -> m
                                                .fuzzy(f -> f
                                                        .field(searchField)
                                                        .value(searchString)
                                                )
                                        )
                                )
                        ),
                clazz
        );
    }

    protected boolean create(T object) throws IOException {
        return elasticsearchClient.index(i -> i
                        .index(index)
                        .document(object)
                )
                .result()
                .name()
                .equals("Created");
    }

    public void deleteAll() throws IOException {
        elasticsearchClient.indices().delete(d -> d
                .index(index)
        );
    }

    public void createIndex() throws IOException {
        elasticsearchClient.indices().create(d -> d
                .index(index)
        );
    }
}
