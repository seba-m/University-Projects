-- Table: public.Rol

-- DROP TABLE public."Rol";

CREATE TABLE IF NOT EXISTS public."Rol"
(
    "IdRol" character varying(30) COLLATE pg_catalog."default" NOT NULL,
    "Nombre" character varying(20) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT "Rol_pkey" PRIMARY KEY ("IdRol")
)

TABLESPACE pg_default;

ALTER TABLE public."Rol"
    OWNER to postgres;