namespace InventarisPro.Modelo.ViewModels.Alarma
{
    public partial class Alarma
    {
        public string IdProducto { get; set; } = null!;
        public string NombreProducto { get; set; } = null!;
        public string NombreCategoria { get; set; } = null!;
        public DateTime? FechaVencimiento { get; set; }
        public DateTime? FechaAlarma { get; set; }
    }
}
