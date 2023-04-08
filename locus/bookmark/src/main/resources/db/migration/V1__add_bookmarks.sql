CREATE TABLE bookmark (
  id BIGINT AUTO_INCREMENT NOT NULL,
   url VARCHAR(255) NULL,
   date date NULL,
   content TEXT NULL,
   owner VARCHAR(255) NULL,
   CONSTRAINT pk_bookmark PRIMARY KEY (id)
);