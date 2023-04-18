package io.github.xpakx.locus.elasticsearch;

import io.github.xpakx.locus.annotation.HighlightType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Document(indexName = "highlights")
@Getter
@Setter

public class HighlightData {
    @Id
    private String id;
    @Field(type = FieldType.Long, name = "db_id")
    private Long dbId;

    @Field(type = FieldType.Text, name = "content")
    private String text;

    @Field(type = FieldType.Text, name = "content")
    private String annotation;

    @Field(type = FieldType.Text, name = "url")
    private String url;

    @Field(type = FieldType.Text, name = "owner")
    private String owner ;
}
