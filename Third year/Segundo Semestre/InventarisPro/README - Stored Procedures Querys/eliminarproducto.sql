-- PROCEDURE: public.eliminarproducto(character varying, character varying)

-- DROP PROCEDURE public.eliminarproducto(character varying, character varying);

CREATE OR REPLACE PROCEDURE public.eliminarproducto(
	_idproducto character varying,
	_rut character varying)
LANGUAGE 'sql'
AS $BODY$
DELETE FROM "Producto" WHERE "IdProducto" = _idproducto 
AND "IdNegocio"= (SELECT "IdNegocio" FROM "Trabajador" where "Rut"=_rut);
$BODY$;
