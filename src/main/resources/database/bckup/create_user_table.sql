-- Database: eco_ufpel

-- DROP DATABASE IF EXISTS eco_ufpel;

CREATE DATABASE eco_ufpel
    WITH
    OWNER = postgres
    ENCODING = 'UTF8'
    LC_COLLATE = 'Portuguese_Brazil.1252'
    LC_CTYPE = 'Portuguese_Brazil.1252'
    LOCALE_PROVIDER = 'libc'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1
    IS_TEMPLATE = False;

CREATE SCHEMA users
    AUTHORIZATION admin;

-- Table: users.users

-- DROP TABLE IF EXISTS users.users;

CREATE TABLE IF NOT EXISTS users.users
(
    cpf varchar(11) UNIQUE NOT NULL,
    name varchar(256) NOT NULL,
    email varchar(128) NOT NULL,
    registration varchar(10) UNIQUE NOT NULL,
    image varchar(256) NOT NULL,
    password varchar(128) NOT NULL,
    role smallint NOT NULL DEFAULT 0,   -- 0 is user, 1 is admin
    enabled boolean NOT NULL DEFAULT true,
    CONSTRAINT users_pkey PRIMARY  KEY (cpf)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS users.users
    OWNER to admin;