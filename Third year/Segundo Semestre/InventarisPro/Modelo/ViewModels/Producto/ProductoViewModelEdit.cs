using System.ComponentModel;
using System.ComponentModel.DataAnnotations;

namespace InventarisPro.Modelo.ViewModels.Producto
{
    public partial class ProductoViewModelEdit
    {
        [DisplayName("Categoría")]
        [Required(ErrorMessage = "* Obligatorio")]
        public string IdCategoria { get; set; } = null!;
        [DisplayName("Nombre")]
        [Required(ErrorMessage = "* Obligatorio")]
        [StringLength(100, MinimumLength = 3, ErrorMessage = "* El nombre debe tener entre 3 y 100 caracteres.")]
        public string Nombre { get; set; } = null!;
        [DisplayName("Descripción")]
        public string? Descripcion { get; set; }
        public IFormFile? Foto { get; set; }
        public string? FotoUrl { get; set; }
        [DisplayName("Stock de Productos")]
        [VerificarNumeroValido(ErrorMessage = "El valor debe ser mayor a cero.")]
        public int? Stock { get; set; }
        [DisplayName("Cantidad Mínima")]
        public int? CantidadMinima { get; set; }
        [DisplayName("Precio de Compra")]
        [VerificarNumeroValido(ErrorMessage = "El valor debe ser mayor a cero.")]
        public int? PrecioCosto { get; set; }
        [DisplayName("Precio de Venta")]
        [VerificarNumeroValido(ErrorMessage = "El valor debe ser mayor a cero.")]
        public int? PrecioVenta { get; set; }
        [DisplayName("Fecha de Vencimiento")]
        [DataType(DataType.Date)]
        [DisplayFormat(DataFormatString = "{0:yyyy-MM-dd}", ApplyFormatInEditMode = true)]
        public DateTime? FechaVencimiento { get; set; }
        [DisplayName("Unidad de Medida")]
        [StringLength(20, ErrorMessage = "* La unidad de medida no debe exceder los 20 caracteres.")]
        public string? UnidadMedida { get; set; }
        public bool AlarmaActivada { get; set; }
    }
}
