CREATE TABLE highlight (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
   text VARCHAR(255) NOT NULL,
   annotation VARCHAR(255),
   timestamp INTEGER,
   type INTEGER,
   CONSTRAINT pk_highlight PRIMARY KEY (id)
);