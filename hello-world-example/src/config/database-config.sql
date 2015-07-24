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
 