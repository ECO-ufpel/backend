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
);