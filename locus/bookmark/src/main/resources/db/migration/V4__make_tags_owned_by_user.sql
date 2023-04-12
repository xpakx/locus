ALTER TABLE tag DROP CONSTRAINT uc_tag_name;
ALTER TABLE tag ADD owner VARCHAR(255);
ALTER TABLE tag ADD CONSTRAINT uc_tag_name_owner UNIQUE (name, owner);