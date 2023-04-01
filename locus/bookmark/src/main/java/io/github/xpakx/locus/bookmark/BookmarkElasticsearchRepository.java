package io.github.xpakx.locus.bookmark;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookmarkElasticsearchRepository extends ElasticsearchRepository<BookmarkData, String> {
    List<BookmarkData> findByContentContaining(String serachString);
}

