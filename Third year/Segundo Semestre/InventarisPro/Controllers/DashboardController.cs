using InventarisPro.Modelo.Entidades;
using InventarisPro.Modelo.ViewModels;
using InventarisPro.Modelo.ViewModels.Categoria;
using InventarisPro.Modelo.ViewModels.Dashboard;
using InventarisPro.Proveedores.Repositorios;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using System.Diagnostics;

namespace InventarisPro.Controllers
{
    [Authorize]
    public class DashboardController : Controller
    {
        private readonly ILogger<DashboardController> _logger;
        private readonly ICategoriaRepository categoria;
        private readonly IProductoRepository productoRepository;

        public DashboardController(ILogger<DashboardController> logger, ICategoriaRepository categ, IProductoRepository productoRepository)
        {
            _logger = logger;
            categoria = categ; 
            this.productoRepository = productoRepository;
        }

        public async Task<IActionResult> Index()
        {
            try
            {
                ViewBag.CantidadAlarmas = (await productoRepository.GetCantidadAlarmas()).Cantidad;
                ViewBag.CantidadProdCate = await CantProdCate();
                var cant = await CantTotalDashboard();
                if (cant.cantcate != null && cant.cantprod != null && cant.preciototal != null)
                {
                    ViewBag.TotalDashboard = cant;
                }
                else
                {
                    cant.cantcate = 0;
                    cant.cantprod = 0;
                    cant.preciototal = 0;
                    ViewBag.TotalDashboard = cant;
                }
                return View();
            }
            catch (Exception e)
            {
                return StatusCode((int)System.Net.HttpStatusCode.Unauthorized, e.Message);
                //return Unauthorized(e.Message);
            }
        }

        [HttpGet]
        public async Task<List<VentaViewModel>> VentaPorCategorias(DateTime? date1, DateTime? date2)
        {
            if (date1 == null || date2 == null) return new List<VentaViewModel>();

            return await categoria.GetVentasEntreFechas(date1.Value, date2.Value);  
        }
        public async Task<List<CategoriaCantProdCateViewModel>> CantProdCate()
        {
            return await categoria.GetCantidadProductoCategoria();
        }
        public async Task<DashboardViewModel> CantTotalDashboard()
        {
            return await categoria.GetTotalDashboard();
        }
    }
}