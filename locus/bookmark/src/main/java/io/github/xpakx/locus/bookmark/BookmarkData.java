package io.github.xpakx.locus.bookmark;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "bookmarks")
@Getter
@Setter

public class BookmarkData {
    @Id
    private String id;
    @Field(type = FieldType.Long, name = "db_id")
    private Long dbId;
    @Field(type = FieldType.Text, name = "content")
    private String content;

    @Field(type = FieldType.Text, name = "url")
    private String url;
}
