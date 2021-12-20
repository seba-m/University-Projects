using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace InventarisPro.Modelo.Entidades
{
    [Table("Accion")]
    public partial class Accion
    {
        public Accion()
        {
            Historials = new HashSet<Historial>();
        }

        [Key]
        [StringLength(30)]
        public string IdAccion { get; set; } = null!;
        [StringLength(20)]
        public string Nombre { get; set; } = null!;

        [InverseProperty(nameof(Historial.IdAccionNavigation))]
        public virtual ICollection<Historial> Historials { get; set; }
    }
}
