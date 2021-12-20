-- PROCEDURE: public.agregarproducto(character varying, character varying, character varying, character varying, character varying, character varying, integer, integer, integer, integer, character varying, character varying, boolean, boolean, timestamp without time zone, character varying)

-- DROP PROCEDURE public.agregarproducto(character varying, character varying, character varying, character varying, character varying, character varying, integer, integer, integer, integer, character varying, character varying, boolean, boolean, timestamp without time zone, character varying);

CREATE OR REPLACE PROCEDURE public.agregarproducto(
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
INSERT INTO "Producto" (
        "IdProducto",
        "IdCategoria" ,
        "IdNegocio",
        "Nombre",
        "Descripcion",
        "Foto",
        "Stock",
        "CantidadMinima",
        "PrecioCosto",
        "PrecioVenta",
        "FechaVencimiento",
        "UnidadMedida",
        "AlarmaActivada",
        "AlarmaAccionada",
        "FechaAlarma") 
    VALUES (_idproducto,
            _idcategoria,
            _idnegocio,
            _nombre,
            _descripcion,
            _foto,
            _stock,
            _cantidadminima,
            _preciocosto,
            _precioventa,
            to_date(_fechavencimiento,'DDMMYY'),
            _unidadmedida,
            _alarmaactivada,
            _alarmaaccionada,
            _fechaalarma);
            
    SET TIMEZONE='America/Santiago';
    INSERT INTO "Historial"(
        "IdTrabajador",
        "IdProducto",
        "IdAccion",
        "FechaModificacion")
    VALUES(_rut,
           _idproducto,
           '1',
           (select now()::timestamp(0)))
$BODY$;
