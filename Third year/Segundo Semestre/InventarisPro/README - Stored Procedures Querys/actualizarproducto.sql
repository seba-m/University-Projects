-- PROCEDURE: public.actualizarproducto(character varying, character varying, character varying, character varying, character varying, character varying, integer, integer, integer, integer, character varying, character varying, boolean, boolean, timestamp without time zone, character varying)

-- DROP PROCEDURE public.actualizarproducto(character varying, character varying, character varying, character varying, character varying, character varying, integer, integer, integer, integer, character varying, character varying, boolean, boolean, timestamp without time zone, character varying);

CREATE OR REPLACE PROCEDURE public.actualizarproducto(
	_idproducto character varying,
	_idcategoria character varying,
	_idnegocio character varying,
	_nombre character varying,
	_descripcion character varying,
	_foto character varying,
	_stock integer,
	_cantidadminima integer,
	_preciocosto integer,
	_precioventa integer,
	_fechavencimiento character varying,
	_unidadmedida character varying,
	_alarmaactivada boolean,
	_alarmaaccionada boolean,
	_fechaalarma timestamp without time zone,
	_rut character varying)
LANGUAGE 'sql'
AS $BODY$
UPDATE "Producto" SET
    "IdCategoria" = _IdCategoria,
    "Nombre" = _Nombre,
    "Descripcion" = _Descripcion,
    "Foto" = _Foto,
    "CantidadMinima" = _CantidadMinima,
    "PrecioCosto" = _PrecioCosto,
    "PrecioVenta" = _PrecioVenta,
    "Stock" = _Stock,
    "FechaVencimiento" = to_date(_FechaVencimiento, 'DDMMYY'),
    "UnidadMedida" = _UnidadMedida,
    "AlarmaActivada" = _alarmaactivada,
    "AlarmaAccionada" = _alarmaaccionada,
    "FechaAlarma" = _fechaalarma
    WHERE "IdProducto" = _IdProducto AND "IdNegocio" = _IdNegocio;
            
    SET TIMEZONE='America/Santiago';
    INSERT INTO "Historial"(
        "IdTrabajador",
        "IdProducto",
        "IdAccion",
        "FechaModificacion")
    VALUES(_rut,
           _IdProducto,
           '2',
           (select now()::timestamp(0)));
$BODY$;
