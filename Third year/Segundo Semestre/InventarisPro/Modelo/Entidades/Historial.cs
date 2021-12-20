using System.ComponentModel;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace InventarisPro.Modelo.Entidades
{
    [Table("Historial")]
    public partial class Historial
    {
        [Key]
        [StringLength(10)]
        [DisplayName("Rut Trabajador")]
        public string IdTrabajador { get; set; } = null!;
        [Key]
        [StringLength(30)]
        [DisplayName("Id Producto")]
        public string IdProducto { get; set; } = null!;
        [Key]
        [StringLength(30)]
        [DisplayName("Acción Realizada")]
        public string IdAccion { get; set; } = null!;
        [Key]
        [Column(TypeName = "timestamp(0) without time zone")]
        [DisplayName("Fecha de Modificación")]
        public DateTime FechaModificacion { get; set; }

        [ForeignKey(nameof(IdAccion))]
        [InverseProperty(nameof(Accion.Historials))]
        public virtual Accion IdAccionNavigation { get; set; } = null!;
        [ForeignKey(nameof(IdProducto))]
        [InverseProperty(nameof(Producto.Historials))]
        public virtual Producto IdProductoNavigation { get; set; } = null!;
        [ForeignKey(nameof(IdTrabajador))]
        [InverseProperty(nameof(Trabajador.Historials))]
        public virtual Trabajador IdTrabajadorNavigation { get; set; } = null!;
    }
}
