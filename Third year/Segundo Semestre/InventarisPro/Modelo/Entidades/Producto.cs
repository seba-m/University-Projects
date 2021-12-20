using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace InventarisPro.Modelo.Entidades
{
    [Table("Producto")]
    public partial class Producto
    {
        public Producto()
        {
            Detalles = new HashSet<Detalle>();
            Historials = new HashSet<Historial>();
        }

        [Key]
        [StringLength(30)]
        public string IdProducto { get; set; } = null!;
        [StringLength(30)]
        public string IdCategoria { get; set; } = null!;
        [StringLength(30)]
        public string IdNegocio { get; set; } = null!;
        [StringLength(100)]
        public string Nombre { get; set; } = null!;
        public string? Descripcion { get; set; }
        [StringLength(40)]
        public string? Foto { get; set; }
        public int? Stock { get; set; }
        private int? _CantidadMinima { get; set; }
        public int? CantidadMinima { 
            get => _CantidadMinima;
            set => SetCantidadMinima(value);
        }
        private bool _AlarmaActivada { get; set; }
        public bool AlarmaActivada
        {
            get => _AlarmaActivada;
            set => SetAlarmaActivada(value);
        }
        public bool AlarmaAccionada { get; set; }
        public DateTime? FechaAlarma { get; set; }
        public int? PrecioCosto { get; set; }
        public int? PrecioVenta { get; set; }
        [Column(TypeName = "Date")]
        public DateTime? FechaVencimiento { get; set; }
        [StringLength(20)]
        public string? UnidadMedida { get; set; }

        [ForeignKey(nameof(IdCategoria))]
        [InverseProperty(nameof(Categoria.Productos))]
        public virtual Categoria IdCategoriaNavigation { get; set; } = null!;
        [ForeignKey(nameof(IdNegocio))]
        [InverseProperty(nameof(Negocio.Productos))]
        public virtual Negocio IdNegocioNavigation { get; set; } = null!;
        [InverseProperty(nameof(Detalle.IdProductoNavigation))]
        public virtual ICollection<Detalle> Detalles { get; set; }
        [InverseProperty(nameof(Historial.IdProductoNavigation))]
        public virtual ICollection<Historial> Historials { get; set; }

        private void SetCantidadMinima(int? valor) {
            if (EsValido(valor) && AlarmaActivada)
            {
                // tiene activada la alarma

                _CantidadMinima = valor;
                AlarmaAccionada = Stock < CantidadMinima;

                FechaAlarma = (AlarmaAccionada) ? DateTime.Now : null;
            }
            else if (!AlarmaActivada)
            {
                // segundo caso, tiene desactivada la alarma

                _CantidadMinima = null; //OJO
                FechaAlarma = null;
            }
            else if (!EsValido(valor) && AlarmaActivada)
            {
                // tercer caso, valor actual es invalido y alarma esta activada

                _AlarmaActivada = false;
            }
            else
            {
                //checkAlarm();
                Console.WriteLine("Entre al else del producto...");
            }
        }

        private void SetAlarmaActivada(bool valor) {
            _AlarmaActivada = valor;
            if (AlarmaActivada is false) {
                AlarmaAccionada = false;
                FechaAlarma = null;
            };
        }

        private static bool EsValido(int? valor) {
            return valor != null && 0 <= valor && valor <= 999_999_999;
        }
        public override string ToString()
        {
            return "\nProducto: \n" +
                    "IdProducto '" + IdProducto + "'\n"
                    + "IdCategoria '" + IdCategoria + "'\n"
                    + "IdNegocio '" + IdNegocio + "'\n"
                    + "Nombre '" + Nombre + "'\n"
                    + "Descripcion '" + Descripcion + "'\n"
                    + "Foto '" + Foto + "'\n"
                    + "Stock '" + Stock + "'\n"
                    + "CantidadMinima '" + CantidadMinima + "'\n"
                    + "AlarmaActivada '" + AlarmaActivada + "'\n"
                    + "AlarmaAccionada '" + AlarmaAccionada + "'\n"
                    + "FechaAlarma '" + FechaAlarma + "'\n"
                    + "PrecioCosto '" + PrecioCosto + "'\n"
                    + "PrecioVenta '" + PrecioVenta + "'\n"
                    + "FechaVencimiento '" + FechaVencimiento + "'\n"
                    + "UnidadMedida '" + UnidadMedida + "'\n";
        }
    }
}
