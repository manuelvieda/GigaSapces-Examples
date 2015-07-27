-- -------------------------------------------------
-- CREATE USERS
-- -------------------------------------------------

DROP ROLE IF EXISTS gigaspaces_example_user;

CREATE ROLE gigaspaces_example_user LOGIN
 	ENCRYPTED PASSWORD 'md5f03915e8e117bce75860bc6bd6a291b9'
  	NOSUPERUSER INHERIT NOCREATEDB NOCREATEROLE NOREPLICATION;
	VALID UNTIL 'infinity';
	
-- -------------------------------------------------
-- CREATE DATABASE
-- -------------------------------------------------
	
DROP DATABASE IF EXISTS gigaspaces_example;

CREATE DATABASE gigaspaces_example
  WITH ENCODING='UTF8'
       TABLESPACE = pg_default
       OWNER=gigaspaces_example_user
       CONNECTION LIMIT=-1;
       
GRANT CONNECT, TEMPORARY ON DATABASE gigaspaces_example TO public;
GRANT ALL ON DATABASE pol_v4 TO gigaspaces_example_user;

CREATE SCHEMA gs
       AUTHORIZATION gigaspaces_example_user;


-- -------------------------------------------------
-- CREATE TABLES
-- -------------------------------------------------

       
CREATE TABLE gs.message
(
   id character varying(36) NOT NULL, 
   state character varying(64) NOT NULL,
   content character varying(255) NOT NULL,
   new_state_date timestamp with time zone,
   in_validation_state_date timestamp with time zone,
   submitted_state_date timestamp with time zone,
   processed_state_date timestamp with time zone,
   lease bigint NOT NULL, 
   CONSTRAINT message_pk PRIMARY KEY (id)
) 
WITH (
  OIDS = FALSE
);
ALTER TABLE gs.message OWNER TO gigaspaces_example_user;
GRANT ALL ON TABLE gs.message TO gigaspaces_example_user;

CREATE UNIQUE INDEX message_id_idx
   ON gs.message USING btree (id ASC NULLS LAST);
   
CREATE INDEX message_lease_idx
   ON gs.message USING btree (ABS(lease) ASC NULLS LAST);