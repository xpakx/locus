package io.github.xpakx.locus.elasticsearch;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public abstract class GenericESRepository<T> {
    private final ElasticsearchClient elasticsearchClient;
    private final Class<T> clazz;
    private final String index;
    Logger logger = LoggerFactory.getLogger(GenericESRepository.class);

    public GenericESRepository(ElasticsearchClient elasticsearchClient, Class<T> clazz, String index) {
        this.elasticsearchClient = elasticsearchClient;
        this.clazz = clazz;
        this.index = index;
        try {
            createIndex();
        } catch(IOException ex) {
            logger.info("Elasticsearch index already created");
        }
    }

    protected SearchResponse<T> doFuzzySearch(String searchString, String field) throws IOException {
        return doFuzzySearch(searchString, field, 0, 20);
    }

    protected SearchResponse<T> doFuzzySearch(String searchString, String field, int from, int size) throws IOException {
        return elasticsearchClient.search(s -> s
                        .index(index)
                        .from(from)
                        .size(size)
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
        return doFuzzySearchWithRequiredField(searchString, searchField, requiredField, requiredValue, 0, 20);
    }

    protected SearchResponse<T> doFuzzySearchWithRequiredField(String searchString, String searchField, String requiredField, String requiredValue, int from, int size) throws IOException {
        return elasticsearchClient.search(s -> s
                        .index(index)
                        .from(from)
                        .size(size)
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
