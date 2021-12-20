using System.ComponentModel.DataAnnotations;

namespace InventarisPro.Modelo.ViewModels.Trabajador
{
    public class TrabajadorUpdateViewModel
    {
        public string? Correo { get; set; }
        [Required(ErrorMessage = "* Obligatorio")]
        public string? Contrasena { get; set; }
        public string? Rut { get; set; }
        public string? Nombre { get; set; }
        public string? Apellido { get; set; }
        [DataType(DataType.Date)]
        [DisplayFormat(DataFormatString = "{0:yyyy-MM-dd}", ApplyFormatInEditMode = true)]
        public DateOnly? FechaNacimiento { get; set; }
        public string? Telefono { get; set; }
        public string? NombreNegocio { get; set; }
        public string? FotoUrl { get; set; }
        public IFormFile? Foto { get; set; }
    }
}
