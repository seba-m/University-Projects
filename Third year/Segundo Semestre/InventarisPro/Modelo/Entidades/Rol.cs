using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace InventarisPro.Modelo.Entidades
{
    [Table("Rol")]
    public partial class Rol
    {
        public Rol()
        {
            Trabajadors = new HashSet<Trabajador>();
        }

        [Key]
        [StringLength(30)]
        public string IdRol { get; set; } = null!;
        [StringLength(20)]
        public string Nombre { get; set; } = null!;

        [InverseProperty(nameof(Trabajador.IdRolNavigation))]
        public virtual ICollection<Trabajador> Trabajadors { get; set; }
    }
}
