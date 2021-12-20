-- PROCEDURE: public.eliminaventa(character varying, character varying)

-- DROP PROCEDURE public.eliminaventa(character varying, character varying);

CREATE OR REPLACE PROCEDURE public.eliminaventa(
	_rut character varying,
	_idventa character varying)
LANGUAGE 'sql'
AS $BODY$
delete from "Venta" using "Trabajador" 
	where "IdTrabajador"="Rut" 
	and "Trabajador"."IdNegocio" = (SELECT "IdNegocio" FROM "Trabajador" WHERE "Rut"= _Rut) 
	and "IdVenta"= _IdVenta;
$BODY$;
