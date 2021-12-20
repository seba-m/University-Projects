using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace InventarisPro.Modelo.Entidades
{
    public partial class Venta
    {
        public Venta()
        {
            Detalles = new HashSet<Detalle>();
        }

        [Key]
        [StringLength(30)]
        public string IdVenta { get; set; } = null!;
        [StringLength(10)]
        public string IdTrabajador { get; set; } = null!;
        public int PrecioTotal { get; set; }
        [Column(TypeName = "Date")]
        [DisplayFormat(DataFormatString = "{0:dd/MM/yyyy}")]
        public DateTime FechaVenta { get; set; }

        [ForeignKey(nameof(IdTrabajador))]
        [InverseProperty(nameof(Trabajador.Ventas))]
        public virtual Trabajador IdTrabajadorNavigation { get; set; } = null!;
        [InverseProperty(nameof(Detalle.IdVentaNavigation))]
        public virtual ICollection<Detalle> Detalles { get; set; }
    }
}
