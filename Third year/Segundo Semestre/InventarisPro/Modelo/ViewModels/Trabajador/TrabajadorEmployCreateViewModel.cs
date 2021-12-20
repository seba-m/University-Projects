using System.ComponentModel.DataAnnotations;

namespace InventarisPro.Modelo.ViewModels.Trabajador
{
    public class TrabajadorEmployCreateViewModel
    {
        [Required]
        [VerificarRut(ErrorMessage = "Rut invalido.")]
        public string Rut { get; set; } = null!;
        [Required]
        [EmailAddress(ErrorMessage = "Correo invalido.")]
        public string Correo { get; set; } = null!;
        [Required(ErrorMessage = "La contraseña es obligatoria.")]
        public string Contrasena { get; set; } = null!;
        [Required(ErrorMessage = "El Nombre es obligatorio.")]
        public string Nombre { get; set; } = null!;
        public string? Apellido { get; set; }
        [Required]
        [VerificarEdad(18)]
        public DateTime FechaNacimiento { get; set; }
        public string? Telefono { get; set; }
        public IFormFile? Foto { get; set; }
        public string? FotoUrl { get; set; }
    }
}
