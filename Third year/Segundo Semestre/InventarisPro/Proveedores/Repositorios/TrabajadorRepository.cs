using InventarisPro.Modelo.ViewModels.Negocio;
using InventarisPro.Modelo.ViewModels.Trabajador;
using InventarisPro.Modelo;
using InventarisPro.Modelo.Entidades;
using Microsoft.EntityFrameworkCore;
using Npgsql;
using System.Security.Claims;
using InventarisPro.Services;

namespace InventarisPro.Proveedores.Repositorios
{
    public class TrabajadorRepository : ITrabajadorRepository
    {
        private readonly ApplicationDbContext context;
        private readonly INegocioRepository negocioRepository;
        private readonly IAdministradorSesion administradorSesion;
        private readonly ServicesAWSS3 servicesAWSS3;

        public TrabajadorRepository(ApplicationDbContext context, INegocioRepository negocioRepository, IAdministradorSesion administradorSesion, ServicesAWSS3 servicesAWSS3)
        {
            this.context = context;
            this.administradorSesion = administradorSesion;
            this.negocioRepository = negocioRepository;
            this.servicesAWSS3 = servicesAWSS3;
        }

        public async Task<int> Add(Trabajador trabajador)
        {

            NpgsqlParameter param1 = new NpgsqlParameter("@p0", trabajador.Rut.Replace(".", "").Replace("-", "").Trim());
            NpgsqlParameter param2 = new NpgsqlParameter("@p1", trabajador.IdNegocio);
            NpgsqlParameter param3 = new NpgsqlParameter("@p2", trabajador.IdRol);
            NpgsqlParameter param4 = new NpgsqlParameter("@p3", trabajador.Correo);
            NpgsqlParameter param5 = new NpgsqlParameter("@p4", trabajador.Contrasena);
            NpgsqlParameter param6 = new NpgsqlParameter("@p5", trabajador.Nombre);
            NpgsqlParameter param7 = new NpgsqlParameter("@p6", trabajador.Apellido);
            NpgsqlParameter param8 = new NpgsqlParameter("@p7", trabajador.FechaNacimiento);
            NpgsqlParameter param9 = new NpgsqlParameter("@p8", trabajador.Telefono);

            return await context.Database
                .ExecuteSqlRawAsync("INSERT INTO \"Trabajador\" (\"Rut\", \"IdNegocio\", \"IdRol\", \"Correo\", \"Contrasena\", \"Nombre\", \"Apellido\", \"FechaNacimiento\", \"Telefono\") values (@p0, @p1, @p2, @p3, @p4, @p5, @p6, @p7, @p8)", param1, param2, param3, param4, param5, param6, param7, param8, param9);
        }

        public async Task<int> Employ(Trabajador trabajador)
        {
            string? rut = administradorSesion.UsuarioActual()?.FindFirst(ClaimTypes.NameIdentifier)?.Value;

            NpgsqlParameter param1 = new NpgsqlParameter("@p0", trabajador.Rut.Replace(".", "").Replace("-", "").Trim());
            NpgsqlParameter param2 = new NpgsqlParameter("@p1", trabajador.IdNegocio);
            NpgsqlParameter param3 = new NpgsqlParameter("@p2", trabajador.IdRol);
            NpgsqlParameter param4 = new NpgsqlParameter("@p3", trabajador.Correo);
            NpgsqlParameter param5 = new NpgsqlParameter("@p4", trabajador.Contrasena);
            NpgsqlParameter param6 = new NpgsqlParameter("@p5", trabajador.Nombre);
            NpgsqlParameter param7 = new NpgsqlParameter("@p6", trabajador.Apellido);
            NpgsqlParameter param8 = new NpgsqlParameter("@p7", trabajador.FechaNacimiento);
            NpgsqlParameter param9 = new NpgsqlParameter("@p8", trabajador.Foto);
            NpgsqlParameter param10 = new NpgsqlParameter("@p9", trabajador.Telefono);
            NpgsqlParameter param11 = new NpgsqlParameter("@p10", rut);

            return await context.Database
                .ExecuteSqlRawAsync("INSERT INTO \"Trabajador\" (\"Rut\", \"IdNegocio\", \"IdRol\", \"Correo\", \"Contrasena\", \"Nombre\", \"Apellido\", \"FechaNacimiento\", \"Telefono\", \"Foto\", \"SuperRut\") values (@p0, @p1, @p2, @p3, @p4, @p5, @p6, @p7, @p8, @p9, @p10)", param1, param2, param3, param4, param5, param6, param7, param8, param9, param10, param11);
        }

        public async Task<Trabajador?> GetByEmail(string email)
        {
            NpgsqlParameter param1 = new NpgsqlParameter("@p0", email);
            var resultado = await context.Trabajadors.FromSqlRaw("SELECT * FROM \"Trabajador\" where \"Correo\" = @p0", param1).ToListAsync();

            if (!resultado.Any()) return null;

            return resultado.First();
        }

        public async Task<Trabajador?> GetCurrentTrabajador()
        {
            string? rut = administradorSesion.UsuarioActual()?.FindFirst(ClaimTypes.NameIdentifier)?.Value;

            //if (rut == null) throw new AuthenticationException("No se encuentra la sesión activa");

            NpgsqlParameter param1 = new NpgsqlParameter("@p0", rut);
            var resultado = await context.Trabajadors.FromSqlRaw("SELECT * FROM \"Trabajador\" where \"Rut\" = @p0", param1).ToListAsync();

            if (!resultado.Any()) return null;//throw new AuthenticationException("Usuario Actual Desconocido");

            return resultado.First();
        }

        public async Task<TrabajadorCookie?> Validar(TrabajadorLoginViewModel model)
        {
            if (model == null || model.Correo == null || model.Contrasena == null) return null;

            var trabajador = await GetByEmail(model.Correo);

            if (trabajador == null) return null;

            bool verified = BCrypt.Net.BCrypt.Verify(model.Contrasena, trabajador.Contrasena);

            if (!verified) return null;

            return new TrabajadorCookie
            {
                Rut = trabajador.Rut,
                Correo = trabajador.Correo,
                Nombre = trabajador.Nombre,
                Rol = trabajador.IdRol
            };
        }

        public async Task Registrar(TrabajadorRegisterViewModel model)
        {
            var negocio = new NegocioViewModel
            {
                IdNegocio = Guid.NewGuid().ToString(),
                Nombre = model.NombreNegocio,
                TipoNegocio = model.TipoNegocio
            };

            await negocioRepository.Add(negocio);

            string contrasenaEncriptada = BCrypt.Net.BCrypt.HashPassword(model.Contrasena.Trim());

            var trabajador = new Trabajador
            {
                Rut = model.Rut.Replace(".", "").Replace("-", "").Trim(),
                IdNegocio = negocio.IdNegocio.ToString(),
                IdRol = "1",
                Correo = model.Correo.Trim(),
                Contrasena = contrasenaEncriptada,
                Nombre = model.Nombre.Trim(),
                Apellido = model.Apellido?.Trim(),
                FechaNacimiento = DateOnly.FromDateTime(model.FechaNacimiento),
                Telefono = model.Telefono?.Trim()
            };

            await Add(trabajador);
        }

        public async Task<Trabajador> Employ(TrabajadorEmployCreateViewModel model)
        {
            var jefe = await GetCurrentTrabajador();

            string contrasenaEncriptada = BCrypt.Net.BCrypt.HashPassword(model.Contrasena.Trim());

            var trabajador = new Trabajador
            {
                Rut = model.Rut.Replace(".", "").Replace("-", "").Trim(),
                IdNegocio = jefe!.IdNegocio,
                IdRol = "2",
                Correo = model.Correo.Trim(),
                Contrasena = contrasenaEncriptada,
                Nombre = model.Nombre.Trim(),
                Apellido = (model.Apellido != null) ? model.Apellido.Trim() : null,
                FechaNacimiento = DateOnly.FromDateTime(model.FechaNacimiento),
                Telefono = (model.Telefono != null) ? model.Telefono.Trim() : null,
                Foto = (model.Foto != null) ? "foto.png" : null
            };

            await Employ(trabajador);

            return trabajador;
        }
        public async Task<Trabajador?> GetByRut(string rut, bool permitido = false)
        {
            if (permitido == false)
            {
                (Trabajador?, bool) jefe = await EsJefe();
                if (!jefe.Item2) throw new Exception("No puedes ver este contenido.");

                NpgsqlParameter param1 = new NpgsqlParameter("@p0", rut);
                NpgsqlParameter param2 = new NpgsqlParameter("@p1", jefe.Item1!.IdNegocio);

                var resultado = await context.Trabajadors.FromSqlRaw("SELECT * FROM \"Trabajador\" where \"Rut\" = @p0 and \"IdNegocio\" = @p1", param1, param2).ToListAsync();

                if (!resultado.Any()) return null;

                return resultado.First();
            }
            else {
                NpgsqlParameter param1 = new NpgsqlParameter("@p0", rut);
                var resultado = await context.Trabajadors.FromSqlRaw("SELECT * FROM \"Trabajador\" where \"Rut\" = @p0", param1).ToListAsync();

                if (!resultado.Any()) return null;

                return resultado.First();
            }

        }
        public async Task<List<TrabajadorViewModel>?> GetAllTrabajadorNegocio() {
            (Trabajador?, bool) jefe = await EsJefe();
            if (!jefe.Item2) throw new Exception("No puedes ver este contenido.");

            NpgsqlParameter param1 = new NpgsqlParameter("@p0", jefe.Item1!.IdNegocio);
            var resultado = await context.Empleado.FromSqlRaw("SELECT \"Rut\", \"Nombre\",\"Apellido\",\"FechaNacimiento\" FROM \"Trabajador\" where \"IdNegocio\" = @p0 and \"IdRol\" = '2'", param1).ToListAsync();

            if (!resultado.Any()) return null;

            return resultado;
        }

        public async Task<int> DespedirEmpleado(Trabajador trabajador)
        {
            (Trabajador?, bool) jefe = await EsJefe();
            if (!jefe.Item2) throw new Exception("No puedes ver este contenido.");

            NpgsqlParameter param1 = new NpgsqlParameter("@p0", trabajador.Rut.Replace(".", "").Replace("-", "").Trim());
            NpgsqlParameter param2 = new NpgsqlParameter("@p1", jefe.Item1!.IdNegocio);

            return await context.Database
                .ExecuteSqlRawAsync("DELETE FROM \"Trabajador\" WHERE \"Rut\" = @p0 AND \"IdRol\"='2' AND \"IdNegocio\" = @p1;", param1, param2);
        }

        public async Task<int> Actualizar(TrabajadorUpdateViewModel trabajador)
        {
            var t = await GetCurrentTrabajador();

            if (t == null) return -1;

            bool entro = false;

            if (trabajador.Foto != null)
            {
                if (t.Foto != null) await servicesAWSS3.EliminarFoto(t);

                using (MemoryStream m = new())
                {
                    trabajador.Foto.CopyTo(m);
                    await servicesAWSS3.SubirFotoTrabajador(m, t);
                    entro = true;
                    trabajador.FotoUrl = "foto.png";
                }
            }
            else if (trabajador.Foto == null && t.Foto != null)
            {
                entro = true;
                trabajador.FotoUrl = "foto.png";
            }

            Console.WriteLine("UPDATE \"Trabajador\" SET \"Nombre\" = " 
                + ((!string.IsNullOrWhiteSpace(trabajador.Nombre)) ? trabajador.Nombre : t.Nombre) +
                ", \"Apellido\" = " + ((!string.IsNullOrWhiteSpace(trabajador.Apellido)) ? trabajador.Apellido : t.Apellido) + 
                ", \"Correo\" = "+((!string.IsNullOrWhiteSpace(trabajador.Correo)) ? trabajador.Correo : t.Correo) + 
                ", \"Contrasena\" = " + (BCrypt.Net.BCrypt.HashPassword(trabajador.Contrasena)) + 
                ", \"Foto\" = " + ((object)trabajador.FotoUrl! ?? DBNull.Value) + 
                ", \"Telefono\" = " + ((!string.IsNullOrWhiteSpace(trabajador.Telefono)) ? trabajador.Telefono : t.Telefono) + 
                " WHERE \"Rut\" = " + (t!.Rut.Replace(".", "").Replace("-", "").Trim()));

            NpgsqlParameter param1 = new NpgsqlParameter("@p0", (!string.IsNullOrWhiteSpace(trabajador.Nombre)) ? trabajador.Nombre : t.Nombre);
            NpgsqlParameter param2 = new NpgsqlParameter("@p1", (!string.IsNullOrWhiteSpace(trabajador.Apellido)) ? trabajador.Apellido : t.Apellido);
            NpgsqlParameter param3 = new NpgsqlParameter("@p2", (!string.IsNullOrWhiteSpace(trabajador.Correo)) ? trabajador.Correo : t.Correo);
            NpgsqlParameter param4 = new NpgsqlParameter("@p3", BCrypt.Net.BCrypt.HashPassword(trabajador.Contrasena));
            NpgsqlParameter param6 = new NpgsqlParameter("@p4", (object)trabajador.FotoUrl! ?? DBNull.Value);
            NpgsqlParameter param7 = new NpgsqlParameter("@p5", (!string.IsNullOrWhiteSpace(trabajador.Telefono)) ? trabajador.Telefono : t.Telefono);
            NpgsqlParameter param8 = new NpgsqlParameter("@p6", t!.Rut);

            return await context.Database
                .ExecuteSqlRawAsync("UPDATE \"Trabajador\" SET \"Nombre\" = @p0, \"Apellido\" = @p1, \"Correo\" = @p2, \"Contrasena\" = @p3, \"Foto\" = @p4, \"Telefono\" = @p5 WHERE \"Rut\" = @p6", param1, param2, param3, param4, param6, param7, param8);
        }

        public async Task<(Trabajador?, bool)> EsJefe() {
            var jefe = await GetCurrentTrabajador();

            return (jefe, !(jefe == null || jefe.IdRol == "2"));
        }
    }
    public interface ITrabajadorRepository
    {
        Task<int> Add(Trabajador trabajador);
        Task<Trabajador?> GetByEmail(string email);
        Task<Trabajador?> GetCurrentTrabajador();
        Task<int> Actualizar(TrabajadorUpdateViewModel productoCambiado);
        Task<TrabajadorCookie?> Validar(TrabajadorLoginViewModel model);
        Task Registrar(TrabajadorRegisterViewModel model);
        Task<Trabajador> Employ(TrabajadorEmployCreateViewModel model);
        Task<List<TrabajadorViewModel>?> GetAllTrabajadorNegocio();
        Task<Trabajador?> GetByRut(string rut, bool permitido = false);
        Task<int> DespedirEmpleado(Trabajador trabajador);
        Task<(Trabajador?, bool)> EsJefe();
    }
}
