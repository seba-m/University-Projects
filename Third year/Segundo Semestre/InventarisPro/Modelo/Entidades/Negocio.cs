using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace InventarisPro.Modelo.Entidades
{
    [Table("Negocio")]
    public partial class Negocio
    {
        public Negocio()
        {
            Detalles = new HashSet<Detalle>();
            Productos = new HashSet<Producto>();
            Trabajadors = new HashSet<Trabajador>();
        }
        [NotMapped]
        [Key]
        [StringLength(40)]
        [Column("IdNegocio")]
        public string IdNegocio { get; set; } = null!;
        [StringLength(50)]
        [Column("Nombre")]
        public string Nombre { get; set; } = null!;
        [StringLength(50)]
        [Column("Direccion")]
        public string? Direccion { get; set; }
        [StringLength(9)]
        [Column("Telefono")]
        public string? Telefono { get; set; }
        public int TipoNegocio { set; get; }

        [InverseProperty(nameof(Detalle.IdNegocioNavigation))]
        public virtual ICollection<Detalle> Detalles { get; set; }
        [InverseProperty(nameof(Producto.IdNegocioNavigation))]
        public virtual ICollection<Producto> Productos { get; set; }
        [InverseProperty(nameof(Trabajador.IdNegocioNavigation))]
        public virtual ICollection<Trabajador> Trabajadors { get; set; }
    }
}
