using System.ComponentModel.DataAnnotations;

namespace InventarisPro.Modelo.ViewModels.Trabajador
{
    public partial class TrabajadorViewModel
    {
        public string Rut { get; set; }
        public string Nombre { get; set; }
        public string Apellido { get; set; }
        [DataType(DataType.Date)]
        [DisplayFormat(DataFormatString = "{0:yyyy-MM-dd}", ApplyFormatInEditMode = true)]
        public DateOnly? FechaNacimiento { get; set; }
    }
}
