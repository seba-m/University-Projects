using InventarisPro.Modelo;
using InventarisPro.Modelo.Entidades;
using InventarisPro.Modelo.ViewModels.Categoria;
using Microsoft.EntityFrameworkCore;
using Npgsql;
using System.Security.Claims;

namespace InventarisPro.Proveedores.Repositorios
{
    public class VentaRepository : IVentaRepository
    {
        private readonly ApplicationDbContext context;
        private readonly IAdministradorSesion administradorSesion;
        private readonly ITrabajadorRepository repTrabajador;
        public VentaRepository(ApplicationDbContext context, IAdministradorSesion administradorSesion, ITrabajadorRepository repTrabajador)
        {
            this.context = context;
            this.administradorSesion = administradorSesion;
            this.repTrabajador = repTrabajador;
        }

        public async Task<Venta?> Add(int importeTotal)
        {
            var trabajador = await repTrabajador.GetCurrentTrabajador();

            Venta venta = new Venta
            {
                IdVenta = Guid.NewGuid().ToString(),
                IdTrabajador = trabajador!.Rut,
                FechaVenta = DateTime.Now,
                PrecioTotal = Convert.ToInt32(importeTotal)
            };

            NpgsqlParameter param1 = new("@p0", venta.IdVenta);
            NpgsqlParameter param2 = new("@p1", venta.IdTrabajador);
            NpgsqlParameter param3 = new("@p2", venta.PrecioTotal);
            NpgsqlParameter param4 = new("@p3", venta.FechaVenta.ToString("ddMMyyyy"));

            /*
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
            */

            var resultado = await context.Database.ExecuteSqlRawAsync("CALL AgregaVenta(@p0,@p1,@p2,@p3)", param1, param2, param3, param4);

            if (resultado == 0) return null;

            return venta;
        }

        public async Task<int> Delete(Venta venta)
        {
            var trabajador = await repTrabajador.GetCurrentTrabajador();

            NpgsqlParameter rut = new("@p0", trabajador!.Rut);
            NpgsqlParameter idVenta = new("@p1", venta.IdVenta);
            /*
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
            */
            return await context.Database.ExecuteSqlRawAsync("CALL eliminaventa(@p0,@p1);", rut, idVenta);
        }

        public async Task<Venta?> GetById(string id)
        {
            var trabajador = await repTrabajador.GetCurrentTrabajador();
            NpgsqlParameter idNegocio = new NpgsqlParameter("@p0", trabajador!.IdNegocio);
            NpgsqlParameter idVenta = new NpgsqlParameter("@p1", id);

            var resultado = context.Venta.FromSqlRaw("SELECT \"Venta\".\"IdVenta\", \"Venta\".\"IdTrabajador\", \"Venta\".\"PrecioTotal\", \"Venta\".\"FechaVenta\" FROM \"Venta\" INNER JOIN \"Trabajador\" AS \"T\" ON \"Venta\".\"IdTrabajador\" = \"T\".\"Rut\" WHERE \"T\".\"IdNegocio\" = @p0 and \"Venta\".\"IdVenta\"=@p1", idNegocio, idVenta).ToList();

            if (resultado == null || !resultado.Any()) return null;

            return resultado.First();
        }

        public async Task<List<Venta>> GetAll()
        {
            string? rut = administradorSesion.UsuarioActual()?.FindFirst(ClaimTypes.NameIdentifier)?.Value;
            NpgsqlParameter param1 = new NpgsqlParameter("@p0", rut);

            return await context.Venta.FromSqlRaw("SELECT \"V\".\"IdVenta\", \"V\".\"IdTrabajador\", \"T\".\"Nombre\" , \"V\".\"PrecioTotal\", \"V\".\"FechaVenta\" FROM \"Venta\" AS \"V\",\"Trabajador\" AS \"T\" WHERE \"V\".\"IdTrabajador\" = \"T\".\"Rut\" AND \"T\".\"IdNegocio\" = (SELECT \"IdNegocio\" FROM \"Trabajador\" WHERE \"Rut\"= @p0)", param1).ToListAsync();
        }

        public async Task<List<VentasViewModel>> GetVentasEntreFechas(DateTime date1, DateTime date2)
        {
            var trabajador = await repTrabajador.GetCurrentTrabajador();

            if (trabajador == null) return new List<VentasViewModel>();

            NpgsqlParameter idNegocio = new NpgsqlParameter("@p0", trabajador.IdNegocio);
            NpgsqlParameter fecha1 = new NpgsqlParameter("@p1", date1.ToShortDateString().Replace("-", ""));
            NpgsqlParameter fecha2 = new NpgsqlParameter("@p2", date2.ToShortDateString().Replace("-", ""));

            return await context.Ventas.FromSqlRaw
                //("select \"c\".\"Nombre\" as \"NombreCategoria\", count(\"d\".\"IdProducto\") as \"CantidadVendida\" from \"Venta\" as \"v\" inner join \"Detalle\" as \"d\" on \"v\".\"IdVenta\" = \"d\".\"IdVenta\" inner join \"Producto\" as \"p\" on \"d\".\"IdProducto\" = \"p\".\"IdProducto\" right join \"Categoria\" as \"c\" on \"p\".\"IdCategoria\" = \"c\".\"IdCategoria\" and \"p\".\"IdNegocio\" = @p0 and \"v\".\"FechaVenta\" between to_date(@p1,'DDMMYY') and to_date(@p2,'DDMMYY') group by (\"c\".\"Nombre\")", idNegocio,fecha1,fecha2).ToListAsync();
                ("select \"v\".\"IdVenta\",\"v\".\"IdTrabajador\",\"v\".\"PrecioTotal\",\"v\".\"FechaVenta\" from \"Venta\" as \"v\" inner join \"Detalle\" as \"d\" on \"v\".\"IdVenta\" = \"d\".\"IdVenta\" inner join \"Producto\" as \"p\" on \"d\".\"IdProducto\" = \"p\".\"IdProducto\" and \"p\".\"IdNegocio\" = @p0 and \"v\".\"FechaVenta\" between to_date(@p1,'DDMMYY') and to_date(@p2,'DDMMYY')", idNegocio, fecha1, fecha2).ToListAsync();
        }
    }
    public interface IVentaRepository
    {
        Task<Venta?> Add(int importeTotal);
        Task<List<Venta>> GetAll();
        Task<Venta?> GetById(string id);
        Task<int> Delete(Venta venta);
        Task<List<VentasViewModel>> GetVentasEntreFechas(DateTime date1, DateTime date2);
    }
}
