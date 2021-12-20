using Microsoft.AspNetCore.Mvc;
using InventarisPro.Proveedores.Repositorios;
using Microsoft.AspNetCore.Authorization;

namespace InventarisPro.Controllers
{
    [Authorize]
    public class HistorialController : Controller
    {
        private readonly IHistorialRepository historialRepository;
        private readonly IProductoRepository productoRepository;

        public HistorialController(IHistorialRepository historialRepository, IProductoRepository productoRepository)
        {
            this.historialRepository = historialRepository;
            this.productoRepository = productoRepository;
        }

        // GET: Historial
        public async Task<IActionResult> Index()
        {
            try
            {
                ViewBag.CantidadAlarmas = (await productoRepository.GetCantidadAlarmas()).Cantidad;
                return View(await historialRepository.GetAll());
            }
            catch (Exception e)
            {
                return StatusCode((int)System.Net.HttpStatusCode.Unauthorized, e.Message);
                //return Unauthorized(e.Message);
            }
        }
    }
}
