using InventarisPro.Modelo;
using InventarisPro.Modelo.Entidades;
using Microsoft.EntityFrameworkCore;
using Npgsql;
using System.Security.Claims;

namespace InventarisPro.Proveedores.Repositorios
{
    public class HistorialRepository : IHistorialRepository
    {
        private readonly ApplicationDbContext context;
        private readonly ITrabajadorRepository administradorSesion;
        public HistorialRepository(ApplicationDbContext context, ITrabajadorRepository administradorSesion)
        {
            this.context = context;
            this.administradorSesion = administradorSesion;
        }

        public async Task<List<Historial>> GetAll()
        {
            Trabajador? negocio = await administradorSesion.GetCurrentTrabajador();
            NpgsqlParameter param1 = new NpgsqlParameter("@p0", negocio!.IdNegocio);

           // return await context.Historials.FromSqlRaw("SELECT * FROM \"Historial\" WHERE \"IdTrabajador\" = @p0", param1).ToListAsync();
            return await context.Historials.FromSqlRaw("SELECT \"h\".\"IdTrabajador\",\"h\".\"IdProducto\",\"h\".\"IdAccion\",\"h\".\"FechaModificacion\" FROM \"Historial\" \"h\", \"Trabajador\" \"t\" where \"h\".\"IdTrabajador\" = \"t\".\"Rut\" and \"t\".\"IdNegocio\" = @p0", param1).ToListAsync();
        }
    }
    public interface IHistorialRepository
    {
        Task<List<Historial>> GetAll();
    }
}
