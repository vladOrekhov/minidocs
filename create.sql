CREATE DATABASE documents1
    WITH 
    OWNER = postgres
    ENCODING = 'UTF8'
    LC_COLLATE = 'English_United States.1252'
    LC_CTYPE = 'English_United States.1252'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1;
    
CREATE SEQUENCE public.document_id_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

ALTER SEQUENCE public.document_id_seq
    OWNER TO postgres;
    
    CREATE SEQUENCE public.reg_card_id_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

ALTER SEQUENCE public.reg_card_id_seq
    OWNER TO postgres;
    
    CREATE TABLE IF NOT EXISTS public.document
(
    document_id integer NOT NULL,
    document_name character varying COLLATE pg_catalog."default",
    author character varying COLLATE pg_catalog."default",
    CONSTRAINT document_pkey PRIMARY KEY (document_id)
)

TABLESPACE pg_default;

ALTER TABLE public.document
    OWNER to postgres;
    
    CREATE TABLE IF NOT EXISTS public.document_version
(
    document_version_id integer NOT NULL,
    version_author character varying COLLATE pg_catalog."default",
    document_id integer NOT NULL,
    content bytea,
    CONSTRAINT "documentVersion_pkey" PRIMARY KEY (document_version_id, document_id),
    CONSTRAINT "documentId" FOREIGN KEY (document_id)
        REFERENCES public.document (document_id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE
)

TABLESPACE pg_default;

ALTER TABLE public.document_version
    OWNER to postgres;
    
    -- Table: public.reg_card

-- DROP TABLE public.reg_card;

CREATE TABLE IF NOT EXISTS public.reg_card
(
    reg_card_id integer NOT NULL,
    document_id integer,
    document_intro_number character varying COLLATE pg_catalog."default",
    document_extern_number character varying COLLATE pg_catalog."default",
    date_intro timestamp without time zone,
    date_extern timestamp without time zone,
    CONSTRAINT "regCard_pkey" PRIMARY KEY (reg_card_id),
    CONSTRAINT "documentId" FOREIGN KEY (document_id)
        REFERENCES public.document (document_id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE
)

TABLESPACE pg_default;

ALTER TABLE public.reg_card
    OWNER to postgres;