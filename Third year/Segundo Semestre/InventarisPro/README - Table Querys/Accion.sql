-- Table: public.Accion

-- DROP TABLE public."Accion";

CREATE TABLE IF NOT EXISTS public."Accion"
(
    "IdAccion" character varying(30) COLLATE pg_catalog."default" NOT NULL,
    "Nombre" character varying(20) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT "Accion_pkey" PRIMARY KEY ("IdAccion")
)

TABLESPACE pg_default;

ALTER TABLE public."Accion"
    OWNER to postgres;