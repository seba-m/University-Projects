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

namespace InventarisPro.Controllers
{
    [Authorize]
    public class ProductoController : Controller
    {
        private readonly IProductoRepository productoRepository;
        private readonly IAdministradorSesion administradorSesion;
        private readonly ICategoriaRepository categoriaRepository;
        private readonly ITrabajadorRepository trabajadorRepository;
        private readonly IConfiguration config;
        private readonly ServicesAWSS3 servicesAWSS3;

        public ProductoController(IConfiguration configuration, ITrabajadorRepository trabajadorRepository, IProductoRepository productoRepository, IAdministradorSesion administradorSesion, ICategoriaRepository categoria, ServicesAWSS3 servicesAWSS)
        {
            config = configuration;
            this.productoRepository = productoRepository;
            this.administradorSesion = administradorSesion;
            this.trabajadorRepository = trabajadorRepository;
            categoriaRepository = categoria;
            servicesAWSS3 = servicesAWSS;
        }

        // GET: Producto
        public async Task<IActionResult> Index()
        {
            try
            {
                ViewBag.CantidadAlarmas = (await productoRepository.GetCantidadAlarmas()).Cantidad;
                return View(await productoRepository.GetAll());
            }
            catch (Exception e)
            {
                return StatusCode((int)System.Net.HttpStatusCode.Unauthorized, e.Message);
                //return Unauthorized(e.Message);
            }
        }

        // GET: Producto/Details/5
        public async Task<IActionResult> Details(string id)
        {
            try
            {
                if (id == null)
                {
                    return NotFound();
                }

                var producto = await productoRepository.GetById(id);
                if (producto == null)
                {
                    return NotFound();
                }

                ViewBag.CantidadAlarmas = (await productoRepository.GetCantidadAlarmas()).Cantidad;
                if (producto.Foto != null)
                {
                    ViewData["img"] = string.Format(config["AWSS3:UrlProduct"], producto.IdNegocio, producto.IdProducto);
                }
                return View(producto);
            }
            catch (Exception e)
            {
                return StatusCode((int)System.Net.HttpStatusCode.Unauthorized, e.Message);
                //return Unauthorized(e.Message);
            }
        }

        // GET: Producto/Create
        public async Task<IActionResult> Create()
        {
            try
            {
                if (!(await trabajadorRepository.EsJefe()).Item2) return Unauthorized("No tiene permisos para ver este contenido.");
                ViewBag.CantidadAlarmas = (await productoRepository.GetCantidadAlarmas()).Cantidad;
                ViewData["IdCategoria"] = new SelectList(await categoriaRepository.GetAll(), "IdCategoria", "Nombre");
                return View();
            }
            catch (Exception e)
            {
                return StatusCode((int)System.Net.HttpStatusCode.Unauthorized, e.Message);
                //return Unauthorized(e.Message);
            }
        }

        // POST: Producto/Create
        // To protect from overposting attacks, enable the specific properties you want to bind to.
        // For more details, see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> Create([Bind("IdCategoria,Nombre,Descripcion,Foto,Stock,CantidadMinima,PrecioCosto,PrecioVenta,FechaVencimiento,UnidadMedida,AlarmaActivada")] ProductoViewModel producto)
        {
            if (!(await trabajadorRepository.EsJefe()).Item2) return Unauthorized("No tiene permisos para ver este contenido.");
            if (ModelState.IsValid)
            {
                var p = await productoRepository.Add(producto);

                if (producto.Foto != null)
                {
                    using (MemoryStream m = new())
                    {
                        producto.Foto.CopyTo(m);
                        await servicesAWSS3.SubirFotoProducto(m, p);
                    }
                }

                return RedirectToAction(nameof(Index));
            }
            ViewData["IdCategoria"] = new SelectList(await categoriaRepository.GetAll(), "IdCategoria", "Nombre", producto.IdCategoria);
            return View(producto);
        }

        // GET: Producto/Edit/5
        public async Task<IActionResult> Edit(string id)
        {
            try
            {
                if (id == null)
                {
                    return NotFound();
                }

                if (!(await trabajadorRepository.EsJefe()).Item2) return Unauthorized("No tiene permisos para ver este contenido.");

                var producto = await productoRepository.GetById(id);

                if (producto == null)
                {
                    return NotFound();
                }

                ViewData["IdCategoria"] = new SelectList(await categoriaRepository.GetAll(), "IdCategoria", "Nombre", producto.IdCategoria);

                if (producto.Foto != null)
                {
                    ViewData["img"] = string.Format(config["AWSS3:UrlProduct"], producto.IdNegocio, producto.IdProducto);
                }
                ViewBag.CantidadAlarmas = (await productoRepository.GetCantidadAlarmas()).Cantidad;
                return View(new ProductoViewModelEdit
                {
                    IdCategoria = producto.IdCategoria,
                    Nombre = producto.Nombre,
                    Descripcion = producto.Descripcion,
                    Stock = producto.Stock,
                    CantidadMinima = producto.CantidadMinima,
                    PrecioCosto = producto.PrecioCosto,
                    PrecioVenta = producto.PrecioVenta,
                    FechaVencimiento = producto.FechaVencimiento,
                    UnidadMedida = producto.UnidadMedida,
                    AlarmaActivada = producto.AlarmaActivada,
                });
            }
            catch (Exception e)
            {
                return StatusCode((int)System.Net.HttpStatusCode.Unauthorized, e.Message);
                //return Unauthorized(e.Message);
            }
        }

        // POST: Producto/Edit/5
        // To protect from overposting attacks, enable the specific properties you want to bind to.
        // For more details, see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> Edit(string id, [Bind("IdCategoria,Nombre,Descripcion,Foto,Stock,CantidadMinima,PrecioCosto,PrecioVenta,FechaVencimiento,UnidadMedida,AlarmaActivada")] ProductoViewModelEdit producto)
        {
            if (id == null)
            {
                return NotFound();
            }

            if (!(await trabajadorRepository.EsJefe()).Item2) return Unauthorized("No tiene permisos para ver este contenido.");

            if (ModelState.IsValid)
            {
                Producto? p = await productoRepository.GetById(id);
                if (p == null) return NotFound();

                if (producto.Foto != null)
                {
                    if (p.Foto != null) await servicesAWSS3.EliminarFoto(p);

                    using (MemoryStream m = new())
                    {
                        producto.Foto.CopyTo(m);
                        await servicesAWSS3.SubirFotoProducto(m, p);
                        producto.FotoUrl = "foto.png";
                    }
                }
                else if (producto.Foto == null && p.Foto != null)
                {
                    producto.FotoUrl = "foto.png";
                }
                
                await productoRepository.Update(id, producto);
                return RedirectToAction(nameof(Index));

            }
            ViewData["IdCategoria"] = new SelectList(await categoriaRepository.GetAll(), "IdCategoria", "Nombre", producto.IdCategoria);
            return View(producto);
        }

        // GET: Producto/Delete/5
        public async Task<IActionResult> Delete(string id)
        {
            try
            {
                if (id == null)
                {
                    return NotFound();
                }

                if (!(await trabajadorRepository.EsJefe()).Item2) return Unauthorized("No tiene permisos para ver este contenido.");

                var producto = await productoRepository.GetById(id);

                if (producto == null)
                {
                    return NotFound();
                }
                ViewBag.CantidadAlarmas = (await productoRepository.GetCantidadAlarmas()).Cantidad;
                if (producto.Foto != null)
                {
                    ViewData["img"] = string.Format(config["AWSS3:UrlProduct"], producto.IdNegocio, producto.IdProducto);
                }
                return View(producto);
            }
            catch (Exception e)
            {
                return StatusCode((int)System.Net.HttpStatusCode.Unauthorized, e.Message);
                //return Unauthorized(e.Message);
            }
        }

        // POST: Producto/Delete/5
        [HttpPost, ActionName("Delete")]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> DeleteConfirmed(string id)
        {
            if (id == null)
            {
                return NotFound();
            }

            if (!(await trabajadorRepository.EsJefe()).Item2) return Unauthorized("No tiene permisos para ver este contenido.");

            var producto = await productoRepository.GetById(id);

            if (producto == null)
            {
                return NotFound();
            }

            await servicesAWSS3.EliminarFoto(producto);
            await productoRepository.Delete(producto);

            return RedirectToAction(nameof(Index));
        }
    }
}
