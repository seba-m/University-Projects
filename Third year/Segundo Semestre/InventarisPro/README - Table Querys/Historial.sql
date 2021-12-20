-- Table: public.Historial

-- DROP TABLE public."Historial";

CREATE TABLE IF NOT EXISTS public."Historial"
(
    "IdTrabajador" character varying(10) COLLATE pg_catalog."default" NOT NULL,
    "IdProducto" character varying(40) COLLATE pg_catalog."default" NOT NULL,
    "IdAccion" character varying(30) COLLATE pg_catalog."default" NOT NULL,
    "FechaModificacion" timestamp(0) without time zone NOT NULL,
    CONSTRAINT "HistorialPK" PRIMARY KEY ("IdTrabajador", "IdProducto", "IdAccion", "FechaModificacion"),
    CONSTRAINT "AccionFK" FOREIGN KEY ("IdAccion")
        REFERENCES public."Accion" ("IdAccion") MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT "ProductoFK" FOREIGN KEY ("IdProducto")
        REFERENCES public."Producto" ("IdProducto") MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE
        NOT VALID,
    CONSTRAINT "TrabajadorFK" FOREIGN KEY ("IdTrabajador")
        REFERENCES public."Trabajador" ("Rut") MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE
        NOT VALID
)

TABLESPACE pg_default;

ALTER TABLE public."Historial"
    OWNER to postgres;