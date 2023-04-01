package io.github.xpakx.locus.bookmark;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookmarkElasticsearchRepository extends ElasticsearchRepository<BookmarkData, String> {
}

