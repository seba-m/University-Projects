using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace InventarisPro.Modelo.Entidades
{
    [Table("Detalle")]
    public partial class Detalle
    {
        [StringLength(30)]
        public string Nombre { get; set; } = null!;

        [Key]
        [StringLength(30)]
        public string IdProducto { get; set; } = null!;
        [Key]
        [StringLength(30)]
        public string IdVenta { get; set; } = null!;
        [Key]
        [StringLength(30)]
        public string IdNegocio { get; set; } = null!;
        /*cantidad * precio venta*/
        public int PrecioTotalVentaProducto { get; set; }
        public int CantidadVendida { get; set; }
        public int PrecioVenta { get; set; }

        [ForeignKey(nameof(IdNegocio))]
        [InverseProperty(nameof(Negocio.Detalles))]
        public virtual Negocio IdNegocioNavigation { get; set; } = null!;
        [ForeignKey(nameof(IdProducto))]
        [InverseProperty(nameof(Producto.Detalles))]
        public virtual Producto IdProductoNavigation { get; set; } = null!;
        [ForeignKey(nameof(IdVenta))]
        [InverseProperty(nameof(Venta.Detalles))]
        public virtual Venta IdVentaNavigation { get; set; } = null!;
    }
}
