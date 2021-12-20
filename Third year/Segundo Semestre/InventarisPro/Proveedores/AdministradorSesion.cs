using System.Security.Claims;
using InventarisPro.Modelo.ViewModels.Trabajador;
using Microsoft.AspNetCore.Authentication;
using Microsoft.AspNetCore.Authentication.Cookies;

namespace InventarisPro.Proveedores
{
    public interface IAdministradorSesion
    {
        Task IniciarSesion(HttpContext httpContext, TrabajadorCookie user);
        Task CerrarSesion(HttpContext httpContext);

        ClaimsPrincipal? UsuarioActual();
    }

    public class AdministradorSesion : IAdministradorSesion
    {
        private readonly IHttpContextAccessor accessor;

        public AdministradorSesion(IHttpContextAccessor accessor)
        {
            this.accessor = accessor;
        }

        public async Task IniciarSesion(HttpContext httpContext, TrabajadorCookie user)
        {
            string authenticationScheme = CookieAuthenticationDefaults.AuthenticationScheme;

            var claims = GetUserClaims(user);

            ClaimsIdentity claimsIdentity = new ClaimsIdentity(claims, authenticationScheme);
            ClaimsPrincipal claimsPrincipal = new ClaimsPrincipal(claimsIdentity);

            await httpContext.SignInAsync(authenticationScheme, claimsPrincipal);
        }

        public async Task CerrarSesion(HttpContext httpContext)
        {
            await httpContext.SignOutAsync(CookieAuthenticationDefaults.AuthenticationScheme);
        }

        private List<Claim> GetUserClaims(TrabajadorCookie user)
        {
            List<Claim> claims = new List<Claim> {
                new Claim(ClaimTypes.NameIdentifier, Convert.ToString(user.Rut)),
                new Claim(ClaimTypes.Name, Convert.ToString(user.Nombre)),
                new Claim(ClaimTypes.Email, Convert.ToString(user.Correo)),
                new Claim(ClaimTypes.Role, Convert.ToString(user.Rol))
            };
            return claims;
        }

        public ClaimsPrincipal? UsuarioActual()
        {
            return accessor?.HttpContext?.User as ClaimsPrincipal;
        }
    }
}
