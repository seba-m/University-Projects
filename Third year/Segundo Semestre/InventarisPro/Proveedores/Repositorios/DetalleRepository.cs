using InventarisPro.Modelo.ViewModels.Negocio;
using InventarisPro.Modelo.ViewModels.Detalle;
using InventarisPro.Modelo;
using InventarisPro.Modelo.Entidades;
using Microsoft.EntityFrameworkCore;
using Npgsql;
using System.Security.Claims;

namespace InventarisPro.Proveedores.Repositorios
{
    public class DetalleRepository : IDetalleRepository
    {
        private readonly ApplicationDbContext context;
        private readonly ITrabajadorRepository repTrabajador;
        public DetalleRepository(ApplicationDbContext context, ITrabajadorRepository repTrabajador)
        {
            this.context = context;
            this.repTrabajador = repTrabajador;
        }

        public async Task<int> Add(DetalleViewModel detalle, Venta venta, Trabajador trabajador, List<Producto> productos)
        {
            NpgsqlParameter param1 = new("@p0", detalle.IdProducto);
            NpgsqlParameter param2 = new("@p1", venta.IdVenta);
            NpgsqlParameter param3 = new("@p2", trabajador.IdNegocio);
            NpgsqlParameter param4 = new("@p3", detalle.Importe);
            NpgsqlParameter param5 = new("@p4", detalle.CantidadVendida);
            /*NpgsqlParameter param6 = new("@p5", detalle.Importe);*/

            Producto prod = productos.First(s => s.IdProducto == detalle.IdProducto);

            if (detalle.CantidadVendida < prod.Stock)
            {
                var nuevoStock = prod.Stock - detalle.CantidadVendida;
                NpgsqlParameter param6 = new("@p5", nuevoStock);
                await context.Database.ExecuteSqlRawAsync("update \"Producto\" set \"Stock\" = @p5 where \"IdProducto\" = @p0", param6, param1);
                /*
                    CREATE OR REPLACE PROCEDURE agregadetalleventa(
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

                 */
                return await context.Database.ExecuteSqlRawAsync("CALL AgregaDetalleVenta(@p0,@p1,@p2,@p3,@p4)", param1, param2, param3, param4, param5);
            }
            else
            {
                throw new Exception("No hay stock para la cantidad que se quiere vender");
            }
        }

        public async Task<List<Detalle>> GetAll(string idVenta)
        {
            NpgsqlParameter param1 = new NpgsqlParameter("@p0", idVenta);

            return await context.Detalles.FromSqlRaw("SELECT \"Producto\".\"Nombre\", \"Detalle\".\"IdProducto\", \"Detalle\".\"IdVenta\", \"Detalle\".\"IdNegocio\", \"Detalle\".\"PrecioTotalVentaProducto\", \"Detalle\".\"CantidadVendida\", \"Detalle\".\"PrecioVenta\" FROM \"Detalle\", \"Producto\" where \"Producto\".\"IdProducto\" = \"Detalle\".\"IdProducto\" and \"IdVenta\" = @p0", param1).ToListAsync();
        }
    }
    public interface IDetalleRepository
    {
        Task<int> Add(DetalleViewModel detalle, Venta venta, Trabajador trabajador, List<Producto> productos);
        Task<List<Detalle>> GetAll(string idVenta);
    }
}
