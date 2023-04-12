CREATE TABLE tag (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
   name VARCHAR(255) NOT NULL,
   CONSTRAINT pk_tag PRIMARY KEY (id)
);

ALTER TABLE tag ADD CONSTRAINT uc_tag_name UNIQUE (name);