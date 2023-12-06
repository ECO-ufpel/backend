-- Database: eco_ufpel

-- DROP DATABASE IF EXISTS eco_ufpel;

-- CREATE DATABASE test_eco_ufpel;

CREATE SCHEMA IF NOT EXISTS users;

-- Table: users.users

-- DROP TABLE IF EXISTS users.users;

CREATE TABLE IF NOT EXISTS users.users( cpf varchar(11) UNIQUE NOT NULL, name varchar(256) NOT NULL, email varchar(128) NOT NULL, registration varchar(10) UNIQUE NOT NULL, image varchar(256) NOT NULL, password varchar(128) NOT NULL, role smallint NOT NULL DEFAULT 0, enabled boolean NOT NULL DEFAULT true, CONSTRAINT users_pkey PRIMARY  KEY (cpf));

INSERT INTO users.users (cpf, name, email, registration, password, role, enabled, image) VALUES ('00000000000', 'Batman', 'bruce@wayne.corp', '00000', '{noop}Eu sou o Batman!', 0, true, '');
INSERT INTO users.users (cpf, name, email, registration, password, role, enabled, image) VALUES ('01001001010', 'Superman', 'clark@kent.farm', '01010', '{noop}Save Martha!', 0, true, '');