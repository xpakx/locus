package io.github.xpakx.locus.elasticsearch;

import io.github.xpakx.locus.bookmark.BookmarkData;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository //TODO: eliminate
public interface BookmarkElasticsearchRepository extends ElasticsearchRepository<BookmarkData, String> {
}

