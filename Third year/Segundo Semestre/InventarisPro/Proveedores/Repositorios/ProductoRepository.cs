using InventarisPro.Modelo.ViewModels.Producto;
using InventarisPro.Modelo;
using InventarisPro.Modelo.Entidades;
using Microsoft.EntityFrameworkCore;
using Npgsql;
using System.Diagnostics;
using InventarisPro.Modelo.ViewModels.Alarma;

namespace InventarisPro.Proveedores.Repositorios
{
    public class ProductoRepository : IProductoRepository
    {
        private readonly ApplicationDbContext context;
        private readonly ITrabajadorRepository repTrabajador;
        public ProductoRepository(ApplicationDbContext context, ITrabajadorRepository repTrabajador)
        {
            this.context = context;
            this.repTrabajador = repTrabajador;
        }

        public async Task<Producto> Add(ProductoViewModel p)
        {
            var trabajador = await repTrabajador.GetCurrentTrabajador();

            var producto = new Producto
            {
                IdProducto = Guid.NewGuid().ToString(),
                IdCategoria = p.IdCategoria,
                IdNegocio = trabajador!.IdNegocio,
                Nombre = p.Nombre,
                Descripcion = p.Descripcion,
                Foto = p.Foto?.FileName,
                Stock = p.Stock,
                PrecioCosto = p.PrecioCosto,
                PrecioVenta = p.PrecioVenta,
                FechaVencimiento = p.FechaVencimiento,
                UnidadMedida = p.UnidadMedida ?? "Unit",
                AlarmaActivada = p.AlarmaActivada,
                CantidadMinima = p.CantidadMinima,
            };

            Console.WriteLine(producto.ToString());

            NpgsqlParameter param1 = new("@p0", producto.IdProducto);
            NpgsqlParameter param2 = new("@p1", producto.IdCategoria);
            NpgsqlParameter param3 = new("@p2", producto.IdNegocio);
            NpgsqlParameter param4 = new("@p3", producto.Nombre);
            NpgsqlParameter param5 = new("@p4", (object)producto.Descripcion! ?? DBNull.Value);
            NpgsqlParameter param6 = new("@p5", (object)producto.Foto! ?? DBNull.Value);
            NpgsqlParameter param7 = new("@p6", producto.Stock);
            NpgsqlParameter param8 = new("@p7", (object)producto.CantidadMinima! ?? DBNull.Value);
            NpgsqlParameter param9 = new("@p8", producto.PrecioCosto);
            NpgsqlParameter param10 = new("@p9", producto.PrecioVenta);
            NpgsqlParameter param11 = new("@p10", (producto.FechaVencimiento != null) ? producto.FechaVencimiento.GetValueOrDefault().ToShortDateString().Replace("-", "") : DBNull.Value);
            NpgsqlParameter param12 = new("@p11", producto.UnidadMedida);
            NpgsqlParameter param13 = new("@p12", producto.AlarmaActivada);
            NpgsqlParameter param14 = new("@p13", producto.AlarmaAccionada);
            NpgsqlParameter param15 = new("@p14", (object)producto.FechaAlarma! ?? DBNull.Value);
            NpgsqlParameter param16 = new("@p15", trabajador.Rut);

            /*
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
             */

            await context.Database.ExecuteSqlRawAsync("CALL agregarproducto(@p0,@p1,@p2,@p3,@p4,@p5,@p6,@p7,@p8,@p9,@p10,@p11,@p12,@p13,@p14,@p15)", param1, param2, param3, param4, param5, param6, param7, param8, param9, param10, param11, param12, param13, param14, param15, param16);

            return producto;
        }
         
        public async Task<int> Delete(Producto p)
        {
            var trabajador = await repTrabajador.GetCurrentTrabajador();
            NpgsqlParameter param1 = new("@p0", p.IdProducto);
            NpgsqlParameter param2 = new("@p1", trabajador!.Rut);
            /*
            CREATE OR REPLACE PROCEDURE public.eliminarproducto(
	            _idproducto character varying,
	            _rut character varying)
            LANGUAGE 'sql'
            AS $BODY$
            DELETE FROM "Producto" WHERE "IdProducto" = _idproducto 
            AND "IdNegocio"= (SELECT "IdNegocio" FROM "Trabajador" where "Rut"=_rut);
            $BODY$;
             */
            return await context.Database.ExecuteSqlRawAsync("CALL eliminarproducto(@p0,@p1);", param1, param2);
        }

        public async Task<List<Producto>> GetAll()
        {
            var trabajador = await repTrabajador.GetCurrentTrabajador();
            NpgsqlParameter rutTrabajador = new NpgsqlParameter("@p0", trabajador!.Rut);

            return await context.Productos.FromSqlRaw("SELECT \"Producto\".\"IdProducto\",\"Producto\".\"IdCategoria\",\"Producto\".\"IdNegocio\",\"Producto\".\"Nombre\",\"Producto\".\"Descripcion\",\"Producto\".\"Foto\",\"Producto\".\"Stock\",\"Producto\".\"CantidadMinima\",\"Producto\".\"PrecioCosto\",\"Producto\".\"PrecioVenta\",\"Producto\".\"FechaVencimiento\", \"Producto\".\"UnidadMedida\", \"Producto\".\"AlarmaActivada\", \"Producto\".\"AlarmaAccionada\", \"Producto\".\"FechaAlarma\" FROM \"Producto\" INNER JOIN \"Trabajador\" ON \"Trabajador\".\"IdNegocio\"=\"Producto\".\"IdNegocio\" AND \"Trabajador\".\"Rut\" = @p0", rutTrabajador).ToListAsync();
        }
        public async Task<List<Alarma>> GetAllAlarma()
        {
            var trabajador = await repTrabajador.GetCurrentTrabajador();
            NpgsqlParameter rutTrabajador = new NpgsqlParameter("@p0", trabajador!.Rut);

            return await context.Alarma.FromSqlRaw("select \"IdProducto\",(select \"Nombre\" from \"Categoria\" \"c\"  where \"c\".\"IdCategoria\" = \"p\".\"IdCategoria\") as \"NombreCategoria\",\"Nombre\" as \"NombreProducto\",\"FechaVencimiento\",\"FechaAlarma\" from \"Producto\" \"p\" where \"AlarmaAccionada\" = true and \"IdNegocio\" = (select \"IdNegocio\" from \"Trabajador\" where \"Rut\" = @p0);", rutTrabajador).ToListAsync();
        }

        public async Task<CantidadAlarmas> GetCantidadAlarmas()
        {
            var trabajador = await repTrabajador.GetCurrentTrabajador();

            if (trabajador == null) throw new Exception("No Hay un trabajador logueado.");
            NpgsqlParameter rutTrabajador = new NpgsqlParameter("@p0", trabajador!.Rut);

            return (await context.CantidadAlarmas.FromSqlRaw("select count(*) as \"Cantidad\" from \"Producto\" \"p\" where \"AlarmaAccionada\" = true and \"IdNegocio\" = (select \"IdNegocio\" from \"Trabajador\" where \"Rut\" = @p0);", rutTrabajador).ToListAsync()).First();
        }

        public async Task<Producto?> GetById(string id)
        {
            var trabajador = await repTrabajador.GetCurrentTrabajador();
            NpgsqlParameter rutTrabajador = new NpgsqlParameter("@p0", trabajador!.Rut);
            NpgsqlParameter idProducto = new NpgsqlParameter("@p1", id);

            //var resultado = context.Productos.FromSqlRaw("SELECT \"Producto\".\"IdProducto\",\"Producto\".\"IdCategoria\",\"Producto\".\"IdNegocio\",\"Producto\".\"Nombre\",\"Producto\".\"Descripcion\",\"Producto\".\"Foto\",\"Producto\".\"Stock\",\"Producto\".\"CantidadMinima\",\"Producto\".\"PrecioCosto\",\"Producto\".\"PrecioVenta\",\"Producto\".\"FechaVencimiento\", \"Producto\".\"UnidadMedida\" FROM \"Producto\" INNER JOIN \"Trabajador\" ON \"Trabajador\".\"IdNegocio\"=\"Producto\".\"IdNegocio\" AND \"Trabajador\".\"Rut\" = @p0 where \"Producto\".\"IdProducto\" = @p1", rutTrabajador, idProducto).ToList();
            var resultado = context.Productos.FromSqlRaw("SELECT \"Producto\".\"IdProducto\",\"Producto\".\"IdCategoria\",\"Producto\".\"IdNegocio\",\"Producto\".\"Nombre\",\"Producto\".\"Descripcion\",\"Producto\".\"Foto\",\"Producto\".\"Stock\",\"Producto\".\"CantidadMinima\",\"Producto\".\"PrecioCosto\",\"Producto\".\"PrecioVenta\",\"Producto\".\"FechaVencimiento\", \"Producto\".\"UnidadMedida\", \"Producto\".\"AlarmaActivada\", \"Producto\".\"AlarmaAccionada\", \"Producto\".\"FechaAlarma\" FROM \"Producto\" INNER JOIN \"Trabajador\" ON \"Trabajador\".\"IdNegocio\"=\"Producto\".\"IdNegocio\" INNER JOIN \"Categoria\" ON \"Categoria\".\"IdCategoria\" = \"Producto\".\"IdCategoria\" AND \"Trabajador\".\"Rut\" = @p0 where \"Producto\".\"IdProducto\" = @p1", rutTrabajador, idProducto).ToList();

            if (resultado == null || !resultado.Any()) return null;

            return resultado.First();
        }

        public async Task<int> Update(string id, ProductoViewModelEdit p)
        {

            Producto? producto = await GetById(id);
            if (producto == null) throw new Exception("No existe el producto");

            var trabajador = await repTrabajador.GetCurrentTrabajador();

            Producto productoEditado = new()
            {
                IdProducto = id,
                IdCategoria = p.IdCategoria,
                IdNegocio = producto.IdNegocio,
                Nombre = p.Nombre,
                Descripcion = p.Descripcion,
                Stock = p.Stock,
                PrecioCosto = p.PrecioCosto,
                PrecioVenta = p.PrecioVenta,
                FechaVencimiento = p.FechaVencimiento,
                UnidadMedida = p.UnidadMedida ?? "Unit",
                AlarmaActivada = p.AlarmaActivada,
                CantidadMinima = p.CantidadMinima
            };

            NpgsqlParameter param1 = new("@p0", id);
            NpgsqlParameter param2 = new("@p1", productoEditado.IdCategoria);
            NpgsqlParameter param3 = new("@p2", productoEditado.IdNegocio);
            NpgsqlParameter param4 = new("@p3", productoEditado.Nombre);
            NpgsqlParameter param5 = new("@p4", productoEditado.Descripcion! as object ?? DBNull.Value);
            NpgsqlParameter param6 = new("@p5", (object)p.FotoUrl! ?? DBNull.Value);
            NpgsqlParameter param7 = new("@p6", productoEditado.Stock);
            NpgsqlParameter param8 = new("@p7", (object)productoEditado.CantidadMinima! ?? DBNull.Value);
            NpgsqlParameter param9 = new("@p8", productoEditado.PrecioCosto);
            NpgsqlParameter param10 = new("@p9", productoEditado.PrecioVenta);
            NpgsqlParameter param11 = new("@p10", (productoEditado.FechaVencimiento != null) ? productoEditado.FechaVencimiento.GetValueOrDefault().ToShortDateString().Replace("-", "") : DBNull.Value);
            NpgsqlParameter param12 = new("@p11", productoEditado.UnidadMedida);
            NpgsqlParameter param13 = new("@p12", productoEditado.AlarmaActivada);
            NpgsqlParameter param14 = new("@p13", productoEditado.AlarmaAccionada);
            NpgsqlParameter param15 = new("@p14", (object)productoEditado.FechaAlarma! ?? DBNull.Value);
            NpgsqlParameter param16 = new("@p15", trabajador!.Rut);
            /*
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
             */
            return await context.Database.ExecuteSqlRawAsync("CALL actualizarproducto(@p0, @p1, @p2, @p3, @p4, @p5, @p6, @p7, @p8, @p9, @p10, @p11, @p12, @p13, @p14, @p15)", param1, param2, param3, param4, param5, param6, param7, param8, param9, param10, param11, param12, param13, param14, param15, param16);
        }
    }
    public interface IProductoRepository
    {
        Task<Producto> Add(ProductoViewModel p);
        Task<int> Delete(Producto id);
        Task<List<Producto>> GetAll();
        Task<CantidadAlarmas> GetCantidadAlarmas();
        Task<Producto?> GetById(string id);
        Task<int> Update(string id, ProductoViewModelEdit productoCambiado);
        Task<List<Alarma>> GetAllAlarma();
    }
}
