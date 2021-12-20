-- PROCEDURE: public.agregaventa(character varying, character varying, integer, character varying)

-- DROP PROCEDURE public.agregaventa(character varying, character varying, integer, character varying);

CREATE OR REPLACE PROCEDURE public.agregaventa(
	_idventa character varying,
	_idtrabajador character varying,
	_preciototal integer,
	_fechaventa character varying)
LANGUAGE 'sql'
AS $BODY$
INSERT INTO "Venta" (
		"IdVenta",
		"IdTrabajador" ,
		"PrecioTotal",
		"FechaVenta"
		)
    VALUES (
			_idventa,
			_idtrabajador,
			_preciototal,
			to_date(_fechaventa,'DDMMYY')
			);
$BODY$;
