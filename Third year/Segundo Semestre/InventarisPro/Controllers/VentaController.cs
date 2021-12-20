using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.Rendering;
using InventarisPro.Modelo;
using InventarisPro.Modelo.Entidades;
using InventarisPro.Modelo.ViewModels.Detalle;
using System.Diagnostics;
using InventarisPro.Proveedores.Repositorios;
using InventarisPro.Modelo.ViewModels.Producto;
using Microsoft.AspNetCore.Authorization;
using InventarisPro.Modelo.ViewModels.Categoria;

namespace InventarisPro.Controllers
{
    [Authorize]
    public class VentaController : Controller
    {
        private readonly ApplicationDbContext _context;
        private readonly IDetalleRepository detalleRepository;
        private readonly IVentaRepository ventaRepository;
        private readonly IProductoRepository productoRepository;
        private readonly ITrabajadorRepository repositorioTrabajadores;
        private readonly IDetalleRepository repositorioDetalle;

        public VentaController(ApplicationDbContext context, IDetalleRepository detalle, IProductoRepository producto, ITrabajadorRepository usuario, IVentaRepository venta, IDetalleRepository Detalle)
        {
            _context = context;
            detalleRepository = detalle;
            productoRepository = producto;
            repositorioTrabajadores = usuario;
            ventaRepository = venta;
            repositorioDetalle = detalle;
        }

        // GET: Venta
        public async Task<IActionResult> Index()
        {
            try
            {
                ViewBag.CantidadAlarmas = (await productoRepository.GetCantidadAlarmas()).Cantidad;
                return View(await ventaRepository.GetAll());
            }
            catch (Exception e)
            {
                return StatusCode((int)System.Net.HttpStatusCode.Unauthorized, e.Message);
                //return Unauthorized(e.Message);
            }
        }

        // GET: Venta/Details/5
        public async Task<IActionResult> Details(string id)
        {
            try
            {
                if (id == null)
                {
                    return NotFound();
                }

                var venta = await detalleRepository.GetAll(id);

                if (venta == null || venta.Count < 1)
                {
                    return NotFound();
                }
                ViewBag.CantidadAlarmas = (await productoRepository.GetCantidadAlarmas()).Cantidad;
                return View(venta);
            }
            catch (Exception e)
            {
                return StatusCode((int)System.Net.HttpStatusCode.Unauthorized, e.Message);
                //return Unauthorized(e.Message);
            }
        }

        // GET: Venta/Create
        public async Task<IActionResult> Create()
        {
            try
            {
                ViewBag.CantidadAlarmas = (await productoRepository.GetCantidadAlarmas()).Cantidad;
                List<Producto> productos = await productoRepository.GetAll();
                ViewData["IdProducto"] = new SelectList(productos, "IdProducto", "Nombre");

                var productosDto = productos.Select(x =>
                        new ProductoViewModelDto()
                        {
                            IdProducto = x.IdProducto,
                            Nombre = x.Nombre,
                            PrecioVenta = x.PrecioVenta,
                            Stock = x.Stock
                        }).ToList();

                ViewBag.Productos = productosDto;
                return View();
            }
            catch (Exception e)
            {
                return StatusCode((int)System.Net.HttpStatusCode.Unauthorized, e.Message);
                //return Unauthorized(e.Message);
            }
        }

        // POST: Venta/Create
        // To protect from overposting attacks, enable the specific properties you want to bind to.
        // For more details, see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> Create([Bind("IdProducto,CantidadVendida,PrecioVenta,Importe")] List<DetalleViewModel> detalle)
        {
            if (detalle == null)
            {
                detalle = new List<DetalleViewModel>();
                return View(detalle);
            }

            if (ModelState.IsValid && detalle.Count >= 1)
            {
                var trabajador = await repositorioTrabajadores.GetCurrentTrabajador();
                var venta = await ventaRepository.Add(detalle.Sum(item => item.Importe));
                var productos = await productoRepository.GetAll();

                if (venta == null || trabajador == null) return StatusCode(StatusCodes.Status500InternalServerError, new { message = "ha ocurrido un error, intente crear la boleta nuevamente." });

                Debug.WriteLine(detalle.Sum(item => item.Importe));
                foreach (DetalleViewModel d in detalle)
                {
                      await repositorioDetalle.Add(d, venta, trabajador, productos);
                }
                return RedirectToAction(nameof(Index));
            }

            ViewData["IdProducto"] = new SelectList(await productoRepository.GetAll(), "IdProducto", "Nombre");

            return View(detalle);
        }

        // GET: Venta/Delete/5
        public async Task<IActionResult> Delete(string id)
        {
            try
            {
                if (id == null)
                {
                    return NotFound();
                }

                var venta = await ventaRepository.GetById(id);
                if (venta == null)
                {
                    return NotFound();
                }
                ViewBag.CantidadAlarmas = (await productoRepository.GetCantidadAlarmas()).Cantidad;
                return View(venta);
            }
            catch (Exception e)
            {
                return StatusCode((int)System.Net.HttpStatusCode.Unauthorized, e.Message);
                //return Unauthorized(e.Message);
            }

        }

        // POST: Venta/Delete/5
        [HttpPost, ActionName("Delete")]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> DeleteConfirmed(string id)
        {
            if (id == null)
            {
                return NotFound();
            }

            var venta = await ventaRepository.GetById(id);

            if (venta == null)
            {
                return NotFound();
            }
            await ventaRepository.Delete(venta);

            return RedirectToAction(nameof(Index));
        }

        [HttpGet]
        public async Task<List<VentasViewModel>?> VentaPorFechas(DateTime? date1, DateTime? date2)
        {
            if (date1 == null || date2 == null) return null;

            List<VentasViewModel> ventas = await ventaRepository.GetVentasEntreFechas(date1.Value, date2.Value);

            if (!ventas.Any()) return null;

            return ventas.GroupBy(x => x.IdVenta).Select(y => y.First()).ToList();
        }
    }
}
