using System.ComponentModel;
using System.ComponentModel.DataAnnotations;

namespace InventarisPro.Modelo.ViewModels.Historial
{
    public partial class HistorialViewModel
    {
        [DisplayName("Rut Trabajador")]
        public string IdTrabajador { get; set; } = null!;
        [DisplayName("Id Producto")]
        public string IdProducto { get; set; } = null!;
        [DisplayName("Acción Realizada")]
        public string IdAccion { get; set; } = null!;
        [DisplayName("Fecha de Modificación")]
        [DataType(DataType.Date)]
        public DateTime? FechaModificacion { get; set; }
    }
}
