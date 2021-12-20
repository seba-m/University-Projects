using InventarisPro.Modelo.ViewModels.Negocio;
using InventarisPro.Modelo.ViewModels.Categoria;
using InventarisPro.Modelo;
using InventarisPro.Modelo.Entidades;
using Microsoft.EntityFrameworkCore;
using Npgsql;
using System.Security.Claims;
using InventarisPro.Modelo.ViewModels.Dashboard;

namespace InventarisPro.Proveedores.Repositorios
{
    public class CategoriaRepository : ICategoriaRepository
    {
        private readonly ApplicationDbContext context;
        private readonly ITrabajadorRepository tRepository;
        private readonly IAdministradorSesion administradorSesion;
        private readonly INegocioRepository negocioRepository;

        public CategoriaRepository(ApplicationDbContext context, IAdministradorSesion adminstradorRepository, INegocioRepository negocioRepository, ITrabajadorRepository repTrab)
        {
            this.context = context;
            this.administradorSesion = adminstradorRepository;
            this.negocioRepository = negocioRepository;
            this.tRepository = repTrab;
        }

        public async Task<List<Categoria>> GetAll()
        {
            Trabajador? t = await tRepository.GetCurrentTrabajador();

            Negocio? n = await negocioRepository.GetById(t!.IdNegocio);

            NpgsqlParameter TipoNegocio = new NpgsqlParameter("@p0", n!.TipoNegocio);

            return await context.Categoria.FromSqlRaw("SELECT \"Categoria\".\"IdCategoria\",\"Categoria\".\"Nombre\" FROM \"Categoria\" where \"Categoria\".\"TipoNegocio\" = @p0", TipoNegocio).ToListAsync();
        }

        public async Task<List<VentaViewModel>> GetVentasEntreFechas(DateTime date1, DateTime date2)
        {
            var trabajador = await tRepository.GetCurrentTrabajador();

            if (trabajador == null) return new List<VentaViewModel>();

            NpgsqlParameter idNegocio = new NpgsqlParameter("@p1", trabajador.IdNegocio);
            NpgsqlParameter fecha1 = new NpgsqlParameter("@p2", date1.ToShortDateString().Replace("-", ""));
            NpgsqlParameter fecha2 = new NpgsqlParameter("@p3", date2.ToShortDateString().Replace("-", ""));

            return await context.CategoriaQuery.FromSqlRaw
                //("select \"c\".\"Nombre\" as \"NombreCategoria\", count(\"d\".\"IdProducto\") as \"CantidadVendida\" from \"Venta\" as \"v\" inner join \"Detalle\" as \"d\" on \"v\".\"IdVenta\" = \"d\".\"IdVenta\" inner join \"Producto\" as \"p\" on \"d\".\"IdProducto\" = \"p\".\"IdProducto\" right join \"Categoria\" as \"c\" on \"p\".\"IdCategoria\" = \"c\".\"IdCategoria\" and \"p\".\"IdNegocio\" = @p0 and \"v\".\"FechaVenta\" between to_date(@p1,'DDMMYY') and to_date(@p2,'DDMMYY') group by (\"c\".\"Nombre\")", idNegocio,fecha1,fecha2).ToListAsync();
                ("select \"c\".\"Nombre\" as \"NombreCategoria\", count(\"d\".\"IdProducto\") as \"CantidadVendida\" from \"Venta\" as \"v\" inner join \"Detalle\" as \"d\" on \"v\".\"IdVenta\" = \"d\".\"IdVenta\" inner join \"Producto\" as \"p\" on \"d\".\"IdProducto\" = \"p\".\"IdProducto\" inner join \"Categoria\" as \"c\" on \"p\".\"IdCategoria\" = \"c\".\"IdCategoria\" and \"p\".\"IdNegocio\" = @p1 and \"v\".\"FechaVenta\" between to_date(@p2,'DDMMYY') and to_date(@p3,'DDMMYY') group by (\"c\".\"Nombre\")", idNegocio,fecha1,fecha2).ToListAsync();
        }

        public async Task<List<CategoriaCantProdCateViewModel>> GetCantidadProductoCategoria()
        {
            string? rut = administradorSesion.UsuarioActual()?.FindFirst(ClaimTypes.NameIdentifier)?.Value;

            NpgsqlParameter param1 = new NpgsqlParameter("@p0", rut);
            return await context.CantProdCateQuery.FromSqlRaw
                ("SELECT \"Categoria\".\"Nombre\" as \"NombreCategoria\", count(\"Producto\".\"Nombre\") AS \"CantidadVendida\" FROM \"Categoria\" inner JOIN \"Producto\" ON \"Producto\".\"IdCategoria\"=\"Categoria\".\"IdCategoria\" and \"Producto\".\"IdNegocio\"=(select \"IdNegocio\" from \"Trabajador\" where \"Rut\"=@p0) GROUP BY (\"Categoria\".\"IdCategoria\")", param1).ToListAsync();
        }

        public async Task<DashboardViewModel> GetTotalDashboard()
        {
            string? rut = administradorSesion.UsuarioActual()?.FindFirst(ClaimTypes.NameIdentifier)?.Value;

            NpgsqlParameter rutUser = new NpgsqlParameter("@p0", rut);
            NpgsqlParameter rutUser2 = new NpgsqlParameter("@p1", rut);
            NpgsqlParameter rutUser3 = new NpgsqlParameter("@p2", rut);

            return (await context.CantTotalCateQuery.FromSqlRaw
                //("select (SELECT COUNT(*) FROM \"Producto\" where \"IdNegocio\" = (select \"IdNegocio\" from \"Trabajador\" where \"Rut\" = @p0)) as \"cantprod\",(SELECT COUNT(*) FROM \"Categoria\" where \"TipoNegocio\" = @p1) as \"cantcate\",(select sum(\"Stock\"*\"PrecioVenta\") from \"Producto\" where \"IdNegocio\" = (select \"IdNegocio\" from \"Trabajador\" where \"Rut\" = @p2)) as \"preciototal\"", rutUser, tipoNegocio,rutUser2).ToListAsync()).First();
                ("select(SELECT COUNT(*) FROM \"Producto\" where \"IdNegocio\" = (select \"IdNegocio\" from \"Trabajador\" where \"Rut\" = @p0)) as \"cantprod\",(SELECT COUNT(*) FROM \"Categoria\" where \"TipoNegocio\" = (select \"TipoNegocio\" from \"Negocio\" where \"IdNegocio\" = (select \"IdNegocio\" from \"Trabajador\" where \"Rut\"= @p1))) as \"cantcate\",(select sum(\"Stock\"*\"PrecioVenta\") from \"Producto\" where \"IdNegocio\" = (select \"IdNegocio\" from \"Trabajador\" where \"Rut\" = @p2)) as \"preciototal\"", rutUser, rutUser2, rutUser3).ToListAsync()).First();
            
        }

    }
    public interface ICategoriaRepository
    {
        Task<List<Categoria>> GetAll();
        Task<List<VentaViewModel>> GetVentasEntreFechas(DateTime date1, DateTime date2);
        Task<List<CategoriaCantProdCateViewModel>> GetCantidadProductoCategoria();
        Task<DashboardViewModel> GetTotalDashboard();

    }
}
