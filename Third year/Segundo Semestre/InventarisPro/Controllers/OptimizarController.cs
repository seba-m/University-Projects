using Microsoft.AspNetCore.Mvc;
using InventarisPro.Proveedores.Repositorios;
using Microsoft.AspNetCore.Authorization;
using InventarisPro.AlgoritmoGenetico;
using Newtonsoft.Json;

namespace InventarisPro.Controllers
{
    [Authorize]
    public class OptimizarController : Controller
    {
        private readonly IOptimizacionRepository repOptimizacion;
        private readonly IProductoRepository producto;
        private readonly ITrabajadorRepository trabajadorRepository;

        public OptimizarController(IOptimizacionRepository repOptimizacion, ITrabajadorRepository trabajadorRepository, IProductoRepository producto)
        {
            this.trabajadorRepository = trabajadorRepository;
            this.repOptimizacion = repOptimizacion;
            this.producto = producto;
        }

        // GET: Optimizar
        public async Task<IActionResult> Index()
        {
            try
            {
                if (!(await trabajadorRepository.EsJefe()).Item2) return Unauthorized("No tiene permisos para ver este contenido.");

                ViewBag.CantidadAlarmas = (await producto.GetCantidadAlarmas()).Cantidad;

                return View();
            }
            catch (Exception e)
            {
                return StatusCode((int)System.Net.HttpStatusCode.Unauthorized, e.Message);
                //return Unauthorized(e.Message);
            }
        }

        [HttpGet]
        public async Task<string?> VentaPorCategorias(DateTime? date1, DateTime? date2)
        {
            if (!(await trabajadorRepository.EsJefe()).Item2) return "No tiene permisos para ver este contenido.";

            if (date1 == null || date2 == null) return null;

            List<ProductoGen> productos = await repOptimizacion.GetVentasEntreFechas(date1.Value, date2.Value);

            if (!productos.Any()) return null;

            return JsonConvert.SerializeObject(AlgoritmoGeneticoImpl.Calcular(productos, 200, 50, 0.5, 0.4));
        }
    }
}
