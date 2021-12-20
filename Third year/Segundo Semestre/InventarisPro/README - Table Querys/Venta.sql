-- Table: public.Venta

-- DROP TABLE public."Venta";

CREATE TABLE IF NOT EXISTS public."Venta"
(
    "IdVenta" character varying(40) COLLATE pg_catalog."default" NOT NULL,
    "IdTrabajador" character varying(10) COLLATE pg_catalog."default" NOT NULL,
    "PrecioTotal" integer NOT NULL,
    "FechaVenta" date NOT NULL,
    CONSTRAINT "VentaPK" PRIMARY KEY ("IdVenta"),
    CONSTRAINT "TrabajadorFK" FOREIGN KEY ("IdTrabajador")
        REFERENCES public."Trabajador" ("Rut") MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID
)

TABLESPACE pg_default;

ALTER TABLE public."Venta"
    OWNER to postgres;