-- Table: public.Detalle

-- DROP TABLE public."Detalle";

CREATE TABLE IF NOT EXISTS public."Detalle"
(
    "IdProducto" character varying(40) COLLATE pg_catalog."default" NOT NULL,
    "IdVenta" character varying(40) COLLATE pg_catalog."default" NOT NULL,
    "IdNegocio" character varying(40) COLLATE pg_catalog."default" NOT NULL,
    "PrecioTotalVentaProducto" integer NOT NULL,
    "CantidadVendida" integer NOT NULL,
    "PrecioVenta" integer NOT NULL,
    CONSTRAINT "Detalle_pkey" PRIMARY KEY ("IdProducto", "IdVenta", "IdNegocio"),
    CONSTRAINT "NegocioFK" FOREIGN KEY ("IdNegocio")
        REFERENCES public."Negocio" ("IdNegocio") MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT "ProductoFK" FOREIGN KEY ("IdProducto")
        REFERENCES public."Producto" ("IdProducto") MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE
        NOT VALID,
    CONSTRAINT "VentaFK" FOREIGN KEY ("IdVenta")
        REFERENCES public."Venta" ("IdVenta") MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE
        NOT VALID
)

TABLESPACE pg_default;

ALTER TABLE public."Detalle"
    OWNER to postgres;