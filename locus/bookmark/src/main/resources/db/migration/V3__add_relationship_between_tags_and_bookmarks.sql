CREATE TABLE bookmark_tags (
  bookmark_id BIGINT NOT NULL,
   tag_id BIGINT NOT NULL,
   CONSTRAINT pk_bookmark_tags PRIMARY KEY (bookmark_id, tag_id)
);

ALTER TABLE bookmark_tags ADD CONSTRAINT fk_bootag_on_bookmark FOREIGN KEY (bookmark_id) REFERENCES bookmark (id);

ALTER TABLE bookmark_tags ADD CONSTRAINT fk_bootag_on_tag FOREIGN KEY (tag_id) REFERENCES tag (id);