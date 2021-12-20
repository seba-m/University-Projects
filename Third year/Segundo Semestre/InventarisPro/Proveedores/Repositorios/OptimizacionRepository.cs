using InventarisPro.Modelo;
using Microsoft.EntityFrameworkCore;
using Npgsql;

using InventarisPro.AlgoritmoGenetico;

namespace InventarisPro.Proveedores.Repositorios
{
    public class OptimizacionRepository : IOptimizacionRepository
    {
        private readonly ApplicationDbContext context;
        private readonly ITrabajadorRepository tRepository;

        public OptimizacionRepository(ApplicationDbContext context, ITrabajadorRepository repTrab)
        {
            this.context = context;
            this.tRepository = repTrab;
        }
        public async Task<List<ProductoGen>> GetVentasEntreFechas(DateTime date1, DateTime date2)
        {
            var trabajador = await tRepository.GetCurrentTrabajador();

            if (trabajador == null) return new List<ProductoGen>();

            NpgsqlParameter idNegocio = new NpgsqlParameter("@p0", trabajador.IdNegocio);
            NpgsqlParameter fecha1 = new NpgsqlParameter("@p1", date1.ToShortDateString().Replace("-", ""));
            NpgsqlParameter fecha2 = new NpgsqlParameter("@p2", date2.ToShortDateString().Replace("-", ""));

            return await context.ProductoGens.FromSqlRaw("select \"p\".\"IdProducto\" as \"IdProducto\",\"p\".\"Nombre\" as \"Nombre\" , \"p\".\"Stock\" as \"Stock\", \"p\".\"PrecioVenta\" as \"PrecioVenta\", \"p\".\"PrecioCosto\",sum(\"d\".\"CantidadVendida\") as \"CantidadVendida\" from \"Venta\" as \"v\" inner join \"Detalle\" as \"d\" on \"v\".\"IdVenta\" = \"d\".\"IdVenta\" inner join \"Producto\" as \"p\" on \"d\".\"IdProducto\" = \"p\".\"IdProducto\" and \"p\".\"IdNegocio\" = @p0 and \"v\".\"FechaVenta\" between to_date(@p1,'DDMMYY') and to_date(@p2,'DDMMYY') group by (\"p\".\"IdProducto\",\"p\".\"Nombre\", \"p\".\"Stock\", \"p\".\"PrecioVenta\", \"p\".\"PrecioCosto\");", idNegocio,fecha1,fecha2).ToListAsync();
        }
    }
    public interface IOptimizacionRepository
    {
        Task<List<ProductoGen>> GetVentasEntreFechas(DateTime date1, DateTime date2);
    }
}
