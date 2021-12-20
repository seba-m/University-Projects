-- Table: public.Negocio

-- DROP TABLE public."Negocio";

CREATE TABLE IF NOT EXISTS public."Negocio"
(
    "IdNegocio" character varying(100) COLLATE pg_catalog."default" NOT NULL,
    "Nombre" character varying(50) COLLATE pg_catalog."default" NOT NULL,
    "Direccion" character varying(50) COLLATE pg_catalog."default",
    "Telefono" character varying(9) COLLATE pg_catalog."default",
    "TipoNegocio" integer,
    CONSTRAINT "Negocio_pkey" PRIMARY KEY ("IdNegocio")
)

TABLESPACE pg_default;

ALTER TABLE public."Negocio"
    OWNER to postgres;