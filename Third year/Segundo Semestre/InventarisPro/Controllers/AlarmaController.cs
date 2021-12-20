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
    public class AlarmaController : Controller
    {
        private readonly IProductoRepository productoRepository;

        public AlarmaController(IProductoRepository productoRepository)
        {
            this.productoRepository = productoRepository;
        }

        // GET: Alarma
        public async Task<IActionResult> Index()
        {
            try
            {
                ViewBag.CantidadAlarmas = (await productoRepository.GetCantidadAlarmas()).Cantidad;
                return View(await productoRepository.GetAllAlarma());
            }
            catch (Exception e)
            {
                return StatusCode((int)System.Net.HttpStatusCode.Unauthorized, e.Message);
                //return Unauthorized(e.Message);
            }
        }
    }
}
