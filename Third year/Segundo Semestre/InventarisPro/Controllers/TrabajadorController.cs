using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.Rendering;
using Microsoft.EntityFrameworkCore;
using InventarisPro.Modelo;
using Microsoft.AspNetCore.Authorization;
using InventarisPro.Proveedores.Repositorios;
using InventarisPro.Proveedores;
using InventarisPro.Modelo.Entidades;
using InventarisPro.Modelo.ViewModels.Producto;
using InventarisPro.Services;
using InventarisPro.Modelo.ViewModels.Trabajador;
using System.Net;

namespace InventarisPro.Controllers
{
    [Authorize]
    public class TrabajadorController : Controller
    {
        private readonly ITrabajadorRepository trabajadorRepository;
        private readonly ServicesAWSS3 servicesAWSS3;
        private readonly IAdministradorSesion administradorSesion;
        private readonly IProductoRepository productoRepository;
        private readonly IConfiguration config;
        public TrabajadorController(IConfiguration config, ITrabajadorRepository trabajadorRepository, IProductoRepository productoRepository, IAdministradorSesion administradorSesion, ServicesAWSS3 servicesAWSS)
        {
            this.config = config;
            this.administradorSesion = administradorSesion;
            this.trabajadorRepository = trabajadorRepository;
            servicesAWSS3 = servicesAWSS;
            this.productoRepository = productoRepository;
        }

        // GET: Trabajador
        public async Task<IActionResult> Index()
        {
            try
            {
                List<TrabajadorViewModel>? trabajadores = await trabajadorRepository.GetAllTrabajadorNegocio();
                ViewBag.CantidadAlarmas = (await productoRepository.GetCantidadAlarmas()).Cantidad;
                return View(trabajadores);
            }
            catch (Exception e) {
                return StatusCode((int)System.Net.HttpStatusCode.Unauthorized, e.Message);
                //return Unauthorized(e.Message);
            }
        }

        // GET: Trabajador/Details/5
        public async Task<IActionResult> Details(string id)
        {
            try
            {
                if (id == null)
                {
                    return NotFound();
                }
                Trabajador? trabajador;
                trabajador = await trabajadorRepository.GetByRut(id);

                if (trabajador == null)
                {
                    return NotFound();
                }
                ViewBag.CantidadAlarmas = (await productoRepository.GetCantidadAlarmas()).Cantidad;
                if (trabajador.Foto != null)
                {
                    ViewData["img"] = string.Format(config["AWSS3:UrlUser"], trabajador.IdNegocio, trabajador.Rut);
                }
                return View(trabajador);
            }
            catch (Exception e)
            {
                return StatusCode((int)System.Net.HttpStatusCode.Unauthorized, e.Message);
                //return Unauthorized(e.Message);
            }
        }

        // GET: Trabajador/Create
        public async Task<IActionResult> Create()
        {
            try
            {
                ViewBag.CantidadAlarmas = (await productoRepository.GetCantidadAlarmas()).Cantidad;
                return View();
            }
            catch (Exception e)
            {
                return StatusCode((int)System.Net.HttpStatusCode.Unauthorized, e.Message);
                //return Unauthorized(e.Message);
            }

        }

        // POST: Trabajador/Create
        // To protect from overposting attacks, enable the specific properties you want to bind to.
        // For more details, see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> Create([Bind("Nombre, Apellido, Rut, Correo, Contrasena, FechaNacimiento, Telefono, Foto")] TrabajadorEmployCreateViewModel trabajador)
        {
            try
            {
                if (ModelState.IsValid)
                {
                    if ((await trabajadorRepository.GetByEmail(trabajador.Correo)) != null)
                    {
                        ModelState.AddModelError(nameof(trabajador.Correo), "El correo ingresado ya existe.");
                        return View(trabajador);
                    }
                    else if ((await trabajadorRepository.GetByRut(trabajador.Rut)) != null)
                    {
                        ModelState.AddModelError(nameof(trabajador.Rut), "El rut ingresado ya existe.");
                        return View(trabajador);
                    }

                    var p = await trabajadorRepository.Employ(trabajador);

                    if (trabajador.Foto != null)
                    {
                        using (MemoryStream m = new())
                        {
                            trabajador.Foto.CopyTo(m);
                            await servicesAWSS3.SubirFotoTrabajador(m, p);
                        }
                    }

                    return RedirectToAction(nameof(Index));
                }
                return View(trabajador);
            }
            catch (Exception e)
            {
                return StatusCode((int)System.Net.HttpStatusCode.Unauthorized, e.Message);
                //return Unauthorized(e.Message);
            }
        }

        // GET: Trabajador/Delete/5
        public async Task<IActionResult> Delete(string id)
        {
            try
            {
                if (id == null)
                {
                    return NotFound();
                }
                Trabajador? trabajador = await trabajadorRepository.GetByRut(id);
                if (trabajador == null)
                {
                    return NotFound();
                }
                ViewBag.CantidadAlarmas = (await productoRepository.GetCantidadAlarmas()).Cantidad;
                if (trabajador.Foto != null)
                {
                    ViewData["img"] = string.Format(config["AWSS3:UrlUser"], trabajador.IdNegocio, trabajador.Rut);
                }
                return View(trabajador);
            }
            catch (Exception e)
            {
                return StatusCode((int)System.Net.HttpStatusCode.Unauthorized, e.Message);
                //return Unauthorized(e.Message);
            }
        }

        // POST: Trabajador/Delete/5
        [HttpPost, ActionName("Delete")]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> DeleteConfirmed(string id)
        {
            try
            {
                if (id == null)
                {
                    return NotFound();
                }
                Trabajador? trabajador = await trabajadorRepository.GetByRut(id);

                if (trabajador == null)
                {
                    return NotFound();
                }
                await servicesAWSS3.EliminarFoto(trabajador);
                await trabajadorRepository.DespedirEmpleado(trabajador);

                return RedirectToAction(nameof(Index));
            }
            catch (Exception e)
            {
                return StatusCode((int)System.Net.HttpStatusCode.Unauthorized, e.Message);
                //return Unauthorized(e.Message);
            }
        }
    }
}
