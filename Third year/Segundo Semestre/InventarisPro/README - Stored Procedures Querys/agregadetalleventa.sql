-- PROCEDURE: public.agregadetalleventa(character varying, character varying, character varying, integer, integer)

-- DROP PROCEDURE public.agregadetalleventa(character varying, character varying, character varying, integer, integer);

CREATE OR REPLACE PROCEDURE public.agregadetalleventa(
	_idproducto character varying,
	_idventa character varying,
	_idnegocio character varying,
	_preciototalventaproducto integer,
	_cantidadvendida integer)
LANGUAGE 'sql'
AS $BODY$
INSERT INTO "Detalle" (
		"IdProducto",
		"IdVenta" ,
		"IdNegocio",
		"PrecioTotalVentaProducto",
		"CantidadVendida",
		"PrecioVenta"
		)
    VALUES (
			_idproducto,
			_idventa,
			_idnegocio,
			_preciototalventaproducto,
			_cantidadvendida,
			(select "PrecioVenta" from "Producto" where "IdProducto"="_idproducto")
			);
$BODY$;
