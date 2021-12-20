-- Table: public.Producto

-- DROP TABLE public."Producto";

CREATE TABLE IF NOT EXISTS public."Producto"
(
    "IdProducto" character varying(40) COLLATE pg_catalog."default" NOT NULL,
    "IdCategoria" character varying(40) COLLATE pg_catalog."default" NOT NULL,
    "IdNegocio" character varying(40) COLLATE pg_catalog."default" NOT NULL,
    "Nombre" character varying(100) COLLATE pg_catalog."default" NOT NULL,
    "Descripcion" text COLLATE pg_catalog."default",
    "Foto" character varying(40) COLLATE pg_catalog."default",
    "Stock" integer NOT NULL DEFAULT 0,
    "CantidadMinima" integer,
    "PrecioCosto" integer NOT NULL DEFAULT 0,
    "PrecioVenta" integer NOT NULL DEFAULT 0,
    "FechaVencimiento" date,
    "UnidadMedida" character varying(20) COLLATE pg_catalog."default",
    "AlarmaActivada" boolean DEFAULT false,
    "AlarmaAccionada" boolean DEFAULT false,
    "FechaAlarma" timestamp(0) without time zone,
    CONSTRAINT "ProductoPK" PRIMARY KEY ("IdProducto"),
    CONSTRAINT "CategoriaFK" FOREIGN KEY ("IdCategoria")
        REFERENCES public."Categoria" ("IdCategoria") MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT "NegocioFK" FOREIGN KEY ("IdNegocio")
        REFERENCES public."Negocio" ("IdNegocio") MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

TABLESPACE pg_default;

ALTER TABLE public."Producto"
    OWNER to postgres;
-- Index: finf_producto_AlarmaAccionada

-- DROP INDEX public."finf_producto_AlarmaAccionada";

CREATE INDEX "finf_producto_AlarmaAccionada"
    ON public."Producto" USING btree
    ("AlarmaAccionada" ASC NULLS LAST, "IdNegocio" COLLATE pg_catalog."default" ASC NULLS LAST)
    TABLESPACE pg_default;