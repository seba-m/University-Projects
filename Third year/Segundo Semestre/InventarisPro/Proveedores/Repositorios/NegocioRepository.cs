using InventarisPro.Modelo.ViewModels.Negocio;
using InventarisPro.Modelo;
using InventarisPro.Modelo.Entidades;
using Microsoft.EntityFrameworkCore;
using Npgsql;

namespace InventarisPro.Proveedores.Repositorios
{
    public class NegocioRepository : INegocioRepository
    {
        private readonly ApplicationDbContext context;

        public NegocioRepository(ApplicationDbContext context)
        {
            this.context = context;
        }

        public async Task<int> Add(NegocioViewModel p)
        {
            NpgsqlParameter param1 = new NpgsqlParameter("@p0", p.IdNegocio);
            NpgsqlParameter param2 = new NpgsqlParameter("@p1", p.Nombre);
            NpgsqlParameter param3 = new NpgsqlParameter("@p2", p.TipoNegocio);

            return await context.Database
                .ExecuteSqlRawAsync("insert into \"Negocio\" (\"IdNegocio\", \"Nombre\", \"TipoNegocio\") values (@p0,@p1,@p2);", param1, param2, param3);
        }

        public async Task<Negocio?> GetById(string idNegocio)
        {
            NpgsqlParameter param1 = new NpgsqlParameter("@p0", idNegocio);
            var resultado = await context.Negocios.FromSqlRaw("SELECT \"Negocio\".\"IdNegocio\",\"Negocio\".\"Nombre\",\"Negocio\".\"Direccion\",\"Negocio\".\"Telefono\",\"Negocio\".\"TipoNegocio\" FROM \"Negocio\" where \"IdNegocio\" = @p0", param1).ToListAsync();

            if (!resultado.Any()) return null;

            return resultado.First();
        }

        public void Update(Negocio productoCambiado)
        {
            NpgsqlParameter param1 = new NpgsqlParameter("@p0", productoCambiado.Nombre);
            NpgsqlParameter param2 = new NpgsqlParameter("@p1", productoCambiado.Direccion);
            NpgsqlParameter param3 = new NpgsqlParameter("@p2", productoCambiado.Telefono);
            NpgsqlParameter param4 = new NpgsqlParameter("@p3", productoCambiado.IdNegocio);

            context.Database
                .ExecuteSqlRaw("UPDATE \"Negocio\" SET(\"Nombre\", \"Direccion\", \"Telefono\") VALUES('@p0', '@p1', '@p2') WHERE \"IdNegocio\" = '@p3';", param1, param2, param3, param4);

        }
    }
    public interface INegocioRepository
    {
        Task<int> Add(NegocioViewModel p);
        void Update(Negocio productoCambiado);
        Task<Negocio?> GetById(string idNegocio);

    }
}
