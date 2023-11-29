-- Database: eco_ufpel_users

-- DROP DATABASE IF EXISTS eco_ufpel_users;

CREATE DATABASE IF NOT EXISTS eco_ufpel_users
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
    AUTHORIZATION postgres;

-- Table: users.users

-- DROP TABLE IF EXISTS users.users;

CREATE TABLE IF NOT EXISTS users.users
(
    cpf varchar(11) UNIQUE NOT NULL,
    name varchar(256) NOT NULL,
    email varchar(128) NOT NULL,
    registration varchar(10) UNIQUE NOT NULL,
    password varchar(20) NOT NULL,
    role smallint NOT NULL DEFAULT 0,   -- 0 is user, 1 is admin
    CONSTRAINT users_pkey PRIMARY  KEY (cpf)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS users.users
    OWNER to postgres;

-- Table: users.password

-- DROP TABLE IF EXISTS users.password;

CREATE TABLE IF NOT EXISTS users.password
(
    cpf varchar(11) NOT NULL,
    hash varchar(512) NOT NULL,
    salt varchar(128) NOT NULL,
    CONSTRAINT password_pkey PRIMARY KEY (cpf),
    CONSTRAINT foregin_key_cpf FOREIGN KEY (cpf)
        REFERENCES users.users (cpf) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS users.password
    OWNER to postgres;