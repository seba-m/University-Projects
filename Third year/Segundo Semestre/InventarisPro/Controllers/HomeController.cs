using InventarisPro.Modelo.ViewModels;
using Microsoft.AspNetCore.Mvc;
using System.Diagnostics;

namespace InventarisPro.Controllers
{
    public class HomeController : Controller
    {
        private readonly ILogger<HomeController> _logger;

        public HomeController(ILogger<HomeController> logger)
        {
            _logger = logger;
        }

        public IActionResult Index()
        {
            return View();
        }

        public IActionResult Privacy()
        {
            return View();
        }

        public IActionResult Error(int? statusCode = null)
        {
            if (statusCode.HasValue)
            {
                var viewName = statusCode.ToString();
                switch (statusCode)
                {
                    case 404:
                        ViewData["ErrorMessage"] = "404, No existe la pagina que buscabas.";
                        break;
                    case 403:
                        ViewData["ErrorMessage"] = "403, No tienes acceso a la pagina que buscabas.";
                        break;
                    default:
                        ViewData["ErrorMessage"] = "No conocemos el error, pero apenas lo descubramos, lo arreglaremos :D.";
                        break;
                }
                Console.WriteLine($"error {statusCode}");
                return View(viewName);
            }
            return View();
        }
    }
}