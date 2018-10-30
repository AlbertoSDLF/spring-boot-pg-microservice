CREATE SCHEMA IF NOT EXISTS public;

CREATE TABLE IF NOT EXISTS public.user
(
    id bigint NOT NULL auto_increment,
    first_name varchar(150) NOT NULL,
    email varchar(250) NOT NULL,
    CONSTRAINT user_pkey PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS public.company
(
    id bigint NOT NULL auto_increment,
    name varchar(150) NOT NULL,
    CONSTRAINT company_pkey PRIMARY KEY (id)
);
