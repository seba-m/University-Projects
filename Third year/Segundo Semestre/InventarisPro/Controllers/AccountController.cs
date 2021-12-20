using InventarisPro.Modelo.ViewModels.Negocio;
using InventarisPro.Modelo.ViewModels.Trabajador;
using InventarisPro.Proveedores;
using InventarisPro.Proveedores.Repositorios;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.Rendering;
using System.Security.Claims;

namespace InventarisPro.Controllers
{
    public class AccountController : Controller
    {
        private readonly ITrabajadorRepository repositorioTrabajadores;
        private readonly IAdministradorSesion administradorSesion;
        private readonly INegocioRepository repNegocio;
        private readonly IProductoRepository productoRepository;
        private readonly IConfiguration config;

        public AccountController(IConfiguration configuration, ITrabajadorRepository userRepository, IAdministradorSesion administradorSesion, INegocioRepository repNegocio, IProductoRepository productoRepository)
        {
            config = configuration;
            this.administradorSesion = administradorSesion;
            repositorioTrabajadores = userRepository;
            this.repNegocio = repNegocio;
            this.productoRepository = productoRepository;
        }

        public IActionResult Login()
        {
            return View();
        }

        [Authorize]
        public async Task<IActionResult> Profile()
        {
            try
            {
                ViewBag.CantidadAlarmas = (await productoRepository.GetCantidadAlarmas()).Cantidad;
                var trabajador = await repositorioTrabajadores.GetCurrentTrabajador();

                if (trabajador == null) await LogoutAsync();

                var negocio = await repNegocio.GetById(trabajador!.IdNegocio);

                if (trabajador.Foto != null)
                {
                    ViewData["img"] = string.Format(config["AWSS3:UrlUser"], trabajador.IdNegocio, trabajador.Rut);
                }

                return View(new TrabajadorUpdateViewModel
                {
                    Correo = trabajador.Correo,
                    Rut = trabajador.Rut,
                    Nombre = trabajador.Nombre,
                    Apellido = trabajador.Apellido,
                    FechaNacimiento = trabajador.FechaNacimiento,
                    Telefono = trabajador.Telefono,
                    FotoUrl = trabajador.Foto,
                    NombreNegocio = negocio.Nombre
                });
            }
            catch (Exception e)
            {
                return StatusCode((int)System.Net.HttpStatusCode.Unauthorized, e.Message);
                //return Unauthorized(e.Message);
            }
        }

        [Authorize]
        [HttpPost]
        public async Task<IActionResult> Profile(TrabajadorUpdateViewModel model)
        {
            if (!ModelState.IsValid)
                return View(model);

            if (model == null)
            {
                return LocalRedirect("~/Producto");
            }

            if (string.IsNullOrWhiteSpace(model.Correo))
            {
                ModelState.AddModelError(nameof(model.Correo), "El correo ingresado es invalido.");
                return View(model);
            }

            if (string.IsNullOrWhiteSpace(model.Contrasena))
            {
                ModelState.AddModelError(nameof(model.Contrasena), "La contraseña no puede estar vacia.");
                return View(model);
            }

            var t = await repositorioTrabajadores.GetByEmail(model.Correo);
            var rut = administradorSesion.UsuarioActual()?.FindFirst(ClaimTypes.NameIdentifier)?.Value;

            if (t != null && t.Rut != rut)
            {
                ModelState.AddModelError(nameof(model.Correo), "El correo ingresado ya existe.");
                return View(model);
            }

            await repositorioTrabajadores.Actualizar(model);

            return LocalRedirect("~/Producto");
        }

        [HttpPost]
        public async Task<IActionResult> LoginAsync(TrabajadorLoginViewModel model)
        {
            if (!ModelState.IsValid)
                return View(model);

            var user = await repositorioTrabajadores.Validar(model);

            if (user == null) return View(model);

            await administradorSesion.IniciarSesion(HttpContext, user);


            return LocalRedirect("~/Producto");
        }

        public IActionResult Register()
        {
            return View();
        }
 

        [HttpPost]
        public async Task<IActionResult> RegisterAsync(TrabajadorRegisterViewModel model)
        {
            if (!ModelState.IsValid)
                return View(model);

            if (model.TipoNegocio > 3 || model.TipoNegocio < 1)
            {
                ModelState.AddModelError(nameof(model.Correo), "El tipo de negocio es invalido.");
                return View(model);
            }
            if ((await repositorioTrabajadores.GetByEmail(model.Correo)) != null)
            {
                ModelState.AddModelError(nameof(model.Correo), "El correo ingresado ya existe.");
                return View(model);
            }
            else if ((await repositorioTrabajadores.GetByRut(model.Rut, true)) != null)
            {
                ModelState.AddModelError(nameof(model.Rut), "El rut ingresado ya existe.");
                return View(model);
            }
            else if (string.IsNullOrWhiteSpace(model.Contrasena))
            {
                ModelState.AddModelError(nameof(model.Contrasena), "La contraseña no puede estar vacia.");
                return View(model);
            }
            else if (string.IsNullOrWhiteSpace(model.Correo))
            {
                ModelState.AddModelError(nameof(model.Correo), "El correo no puede estar vacio.");
                return View(model);
            }

            await repositorioTrabajadores.Registrar(model);

            return LocalRedirect("~/Account/Login");
        }
        
        public async Task<IActionResult> LogoutAsync()
        {
            await administradorSesion.CerrarSesion(this.HttpContext);
            return RedirectPermanent("~/Home/Index");
        }
    }
}