using System.ComponentModel.DataAnnotations;

namespace InventarisPro.Modelo.ViewModels.Trabajador
{
    public class TrabajadorRegisterViewModel
    {
        [Required(ErrorMessage = "El rut es obligatorio.")]
        [VerificarRut(ErrorMessage = "Rut invalido.")]
        public string Rut { get; set; } = null!;
        [Required(ErrorMessage = "El correo es obligatorio.")]
        [EmailAddress(ErrorMessage = "Correo invalido.")]
        public string Correo { get; set; } = null!;
        [Required(ErrorMessage = "La contraseña es obligatoria.")]
        public string Contrasena { get; set; } = null!;
        [Required(ErrorMessage = "El nombre es obligatorio.")]
        public string Nombre { get; set; } = null!;
        public string? Apellido { get; set; }
        [Required(ErrorMessage = "Su fecha de nacimiento es obligatoria.")]
        [VerificarEdad(18)]
        public DateTime FechaNacimiento { get; set; }
        public string? Telefono { get; set; }
        [Required(ErrorMessage = "El nombre de su negocio es obligatorio.")]
        public string NombreNegocio { get; set; } = null!;
        [Required(ErrorMessage = "El tipo de su negocio es obligatorio.")]
        public int TipoNegocio { get; set; }
    }
}
