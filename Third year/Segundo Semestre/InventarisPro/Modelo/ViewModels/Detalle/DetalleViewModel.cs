using System.ComponentModel;
using System.ComponentModel.DataAnnotations;

namespace InventarisPro.Modelo.ViewModels.Detalle
{
    public partial class DetalleViewModel
    {
        [DisplayName("Nombre Producto")]
        [Required(ErrorMessage = "* Obligatorio")]
        public string IdProducto { get; set; } = null!;
        [DisplayName("Cantidad Vendida")]
        [Required(ErrorMessage = "* Obligatorio")]
        [VerificarNumeroValido(ErrorMessage = "El valor debe ser mayor a cero.")]
        public int CantidadVendida { get; set; }
        [DisplayName("Precio de Venta")]
        [Required(ErrorMessage = "* Obligatorio")]
        [VerificarNumeroValido(ErrorMessage = "El valor debe ser mayor a cero.")]
        public int PrecioVenta { get; set; }
        [DisplayName("Importe Total")]
        [Required(ErrorMessage = "* Obligatorio")]
        public int Importe { get; set; }
    }
}
