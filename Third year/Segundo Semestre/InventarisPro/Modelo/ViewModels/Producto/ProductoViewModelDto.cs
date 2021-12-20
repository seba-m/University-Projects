using System.ComponentModel;
using System.ComponentModel.DataAnnotations;

namespace InventarisPro.Modelo.ViewModels.Producto
{
    public partial class ProductoViewModelDto
    {
        public string IdProducto { get; set; } = null!;
        public string Nombre { get; set; } = null!;
        public int? Stock { get; set; }
        public int? PrecioVenta { get; set; }
    }
}
