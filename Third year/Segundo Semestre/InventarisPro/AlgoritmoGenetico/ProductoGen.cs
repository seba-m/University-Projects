namespace InventarisPro.AlgoritmoGenetico
{
    public class ProductoGen
    {
        public string IdProducto { get; set; }
        public string Nombre { get; set; }
        public int CantidadVendida { get; set; }
        public int PrecioCosto { get; set; }
        public int PrecioVenta { get; set; }
        public int Stock { get; set; }

        public ProductoGen(string IdProducto, string Nombre, int CantidadVendida, int PrecioCosto, int PrecioVenta)
        {
            this.Nombre = Nombre;
            this.IdProducto = IdProducto;
            this.CantidadVendida = CantidadVendida;
            this.PrecioCosto = PrecioCosto;
            this.PrecioVenta = PrecioVenta;
        }

        public int getPrecioTotal()
        {
            return (Stock > 0) ? PrecioCosto * Stock : 0;
        }

        public int getUtilidad()
        {
            return PrecioVenta - PrecioCosto;
        }
        public override string ToString()
        {
            return $"ProductoGen IdProducto: {IdProducto}, nombre:{Nombre}, CantidadVendida: {CantidadVendida}, PrecioCosto: {PrecioCosto}, PrecioVenta: {PrecioVenta} ";
        }
    }
}
