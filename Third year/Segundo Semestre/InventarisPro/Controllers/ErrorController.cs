using Microsoft.AspNetCore.Mvc;

namespace InventarisPro.Controllers
{
    public class ErrorController : Controller
    {
        [Route("/Error/{statusCode}")]
        public IActionResult HttpStatusCodeHandler(int statusCode)
        {
            switch (statusCode)
            {
                case 404:
                    ViewData["ErrorMessage"] = "404, No existe la pagina que buscabas.";
                    break;
                case 403:
                    ViewData["ErrorMessage"] = "403, No tienes acceso a la pagina que buscabas.";
                    break;
            }
            Console.WriteLine($"error {statusCode}");
            return View("Error");
        }
    }
}
