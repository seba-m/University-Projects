using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace InventarisPro.Modelo.ViewModels.Categoria
{
    public partial class VentasViewModel
    {
        public string IdVenta { get; set; } = null!;
        public string IdTrabajador { get; set; } = null!;
        public int PrecioTotal { get; set; }
        public DateTime FechaVenta { get; set; }
    }
}
