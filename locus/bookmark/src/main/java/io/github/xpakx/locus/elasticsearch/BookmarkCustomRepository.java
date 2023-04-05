package io.github.xpakx.locus.elasticsearch;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import io.github.xpakx.locus.bookmark.BookmarkData;
import io.github.xpakx.locus.elasticsearch.error.ESException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.List;

@Repository
public class BookmarkCustomRepository extends GenericESRepository<BookmarkData> {

    @Autowired
    public BookmarkCustomRepository(ElasticsearchClient elasticsearchClient) {
        super(elasticsearchClient, BookmarkData.class, "bookmarks");
    }

    public List<BookmarkData> searchForBookmark(String searchString) {
        try {
            SearchResponse<BookmarkData> response = doFuzzySearch(searchString, "content");
            return response.hits().hits().stream().map(Hit::source).toList();
        } catch(IOException exception) {
            throw new ESException();
        }
    }

    public void saveBookmark(BookmarkData bookmark) {
        try {
            super.create(bookmark);
        } catch(IOException exception) {
            System.out.println(exception);
            throw new ESException();
        }
    }
}
