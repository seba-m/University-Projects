-- Table: public.Categoria

-- DROP TABLE public."Categoria";

CREATE TABLE IF NOT EXISTS public."Categoria"
(
    "IdCategoria" character varying(30) COLLATE pg_catalog."default" NOT NULL,
    "Nombre" character varying(30) COLLATE pg_catalog."default" NOT NULL,
    "TipoNegocio" integer,
    CONSTRAINT "Categoria_pkey" PRIMARY KEY ("IdCategoria")
)

TABLESPACE pg_default;

ALTER TABLE public."Categoria"
    OWNER to postgres;