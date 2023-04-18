package io.github.xpakx.locus.elasticsearch;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import io.github.xpakx.locus.elasticsearch.error.ESException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.List;

@Repository
public class HighlightESRepository extends GenericESRepository<HighlightData> {

    @Autowired
    public HighlightESRepository(ElasticsearchClient elasticsearchClient) {
        super(elasticsearchClient, HighlightData.class, "highlights");
    }

    public List<HighlightData> searchForHighlight(String searchString) {
        try {
            SearchResponse<HighlightData> response = doFuzzySearch(searchString, "content");
            return response.hits().hits().stream().map(Hit::source).toList();
        } catch(IOException exception) {
            throw new ESException();
        }
    }

    public List<HighlightData> searchForUserHighlight(String searchString, String username, int from, int amount) {
        try {
            SearchResponse<HighlightData> response = doFuzzySearchWithRequiredField(
                    searchString,
                    "content",
                    "owner",
                    username,
                    from,
                    amount
            );
            return response.hits().hits().stream().map(Hit::source).toList();
        } catch(IOException exception) {
            throw new ESException();
        }
    }

    public boolean saveHighlight(HighlightData highlight) {
        try {
            return super.create(highlight);
        } catch(IOException exception) {
            System.out.println(exception);
            throw new ESException();
        }
    }
}
