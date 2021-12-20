-- Table: public.Trabajador

-- DROP TABLE public."Trabajador";

CREATE TABLE IF NOT EXISTS public."Trabajador"
(
    "Rut" character varying(10) COLLATE pg_catalog."default" NOT NULL,
    "IdNegocio" character varying(40) COLLATE pg_catalog."default" NOT NULL,
    "IdRol" character varying(30) COLLATE pg_catalog."default" NOT NULL,
    "Correo" character varying(320) COLLATE pg_catalog."default" NOT NULL,
    "Contrasena" text COLLATE pg_catalog."default" NOT NULL,
    "Nombre" character varying(25) COLLATE pg_catalog."default" NOT NULL,
    "Apellido" character varying(25) COLLATE pg_catalog."default",
    "FechaNacimiento" date NOT NULL,
    "Foto" character varying(40) COLLATE pg_catalog."default",
    "Telefono" character varying(15) COLLATE pg_catalog."default",
    "SuperRut" character varying(10) COLLATE pg_catalog."default",
    CONSTRAINT "Trabajador_pkey" PRIMARY KEY ("Rut"),
    CONSTRAINT "NegocioFK" FOREIGN KEY ("IdNegocio")
        REFERENCES public."Negocio" ("IdNegocio") MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT "RolFK" FOREIGN KEY ("IdRol")
        REFERENCES public."Rol" ("IdRol") MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

TABLESPACE pg_default;

ALTER TABLE public."Trabajador"
    OWNER to postgres;