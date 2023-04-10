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
public class BookmarkESRepository extends GenericESRepository<BookmarkData> {

    @Autowired
    public BookmarkESRepository(ElasticsearchClient elasticsearchClient) {
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

    public List<BookmarkData> searchForUserBookmark(String searchString, String username) {
        try {
            SearchResponse<BookmarkData> response = doFuzzySearchWithRequiredField(searchString, "content", "owner", username);
            return response.hits().hits().stream().map(Hit::source).toList();
        } catch(IOException exception) {
            throw new ESException();
        }
    }

    public boolean saveBookmark(BookmarkData bookmark) {
        try {
            return super.create(bookmark);
        } catch(IOException exception) {
            System.out.println(exception);
            throw new ESException();
        }
    }

    @Override
    public void saveAll(List<BookmarkData> bookmarks) {
        try {
            super.saveAll(bookmarks);
        } catch(IOException ex) {
            System.out.println(ex);
        }
    }
}
