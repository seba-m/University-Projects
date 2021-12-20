using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace InventarisPro.Modelo.Entidades
{
    [Table("Trabajador")]
    public partial class Trabajador
    {
        public Trabajador()
        {
            Historials = new HashSet<Historial>();
            Ventas = new HashSet<Venta>();
        }

        [Key]
        [StringLength(10)]
        [Column("Rut")]
        public string Rut { get; set; } = null!;
        [StringLength(40)]
        [Column("IdNegocio")]
        public string IdNegocio { get; set; } = null!;
        [StringLength(30)]
        [Column("IdRol")]
        public string IdRol { get; set; } = null!;
        [StringLength(320)]
        [Column("Correo")]
        [EmailAddress(ErrorMessage = "Correo invalido.")]
        public string Correo { get; set; } = null!;
        [Column("Contrasena")]
        public string Contrasena { get; set; } = null!;
        [StringLength(25)]
        [Column("Nombre")]
        public string Nombre { get; set; } = null!;
        [StringLength(25)]
        [Column("Apellido")]
        public string? Apellido { get; set; }
        [Column("FechaNacimiento")]
        public DateOnly FechaNacimiento { get; set; }
        [StringLength(40)]
        [Column("Foto")]
        public string? Foto { get; set; }
        [StringLength(9)]
        [Column("Telefono")]
        public string? Telefono { get; set; }
        [StringLength(10)]
        [Column("SuperRut")]
        public string? SuperRut { get; set; }

        [ForeignKey("IdNegocio")]
        [InverseProperty(nameof(Negocio.Trabajadors))]
        public virtual Negocio IdNegocioNavigation { get; set; } = null!;
        [ForeignKey("IdRol")]
        [InverseProperty(nameof(Rol.Trabajadors))]
        public virtual Rol IdRolNavigation { get; set; } = null!;
        [InverseProperty(nameof(Historial.IdTrabajadorNavigation))]
        public virtual ICollection<Historial> Historials { get; set; }
        [InverseProperty(nameof(Venta.IdTrabajadorNavigation))]
        public virtual ICollection<Venta> Ventas { get; set; }
    }
}
