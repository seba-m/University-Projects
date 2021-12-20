-- PROCEDURE: public.buscarproducto(character varying, character varying)

-- DROP PROCEDURE public.buscarproducto(character varying, character varying);

CREATE OR REPLACE PROCEDURE public.buscarproducto(
	_idproducto character varying,
	_rut character varying)
LANGUAGE 'sql'
AS $BODY$
SELECT "Producto"."IdProducto",
	"Producto"."IdCategoria",
	"Producto"."IdNegocio",
	"Producto"."Nombre",
	"Producto"."Descripcion",
	"Producto"."Foto",
	"Producto"."Stock",
	"Producto"."CantidadMinima",
	"Producto"."PrecioCosto",
	"Producto"."PrecioVenta",
	"Producto"."FechaVencimiento",
	"Producto"."UnidadMedida" 
	FROM "Producto" INNER JOIN "Trabajador" 
	ON "Trabajador"."IdNegocio"="Producto"."IdNegocio" 
	AND "Trabajador"."Rut" = _rut 
	where "Producto"."IdProducto" = _IdProducto;
$BODY$;
