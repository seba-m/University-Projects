using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace InventarisPro.Modelo.Entidades
{
    public partial class Categoria
    {
        public Categoria()
        {
            Productos = new HashSet<Producto>();
        }

        [Key]
        [StringLength(30)]
        public string IdCategoria { get; set; } = null!;
        [StringLength(30)]
        public string Nombre { get; set; } = null!;

        [InverseProperty(nameof(Producto.IdCategoriaNavigation))]
        public virtual ICollection<Producto> Productos { get; set; }
    }
}
