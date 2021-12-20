using System.ComponentModel.DataAnnotations;

namespace InventarisPro.Modelo.ViewModels.Trabajador
{
    public class TrabajadorLoginViewModel
    {
        [Required]
        [EmailAddress(ErrorMessage = "Correo invalido.")]
        public string Correo { get; set; }

        [Required]
        public string Contrasena { get; set; }
    }
}
