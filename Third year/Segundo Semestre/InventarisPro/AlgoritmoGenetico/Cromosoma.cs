using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace InventarisPro.AlgoritmoGenetico
{
    internal class Cromosoma
    {
        public List<ProductoGen> productos;
        
        public int SumaTotalVentas { get; set; }

        public Cromosoma(List<ProductoGen> productos)
        {
            this.productos = productos;

            for(int i = 0; i< productos.Count; i++)
            {
                this.SumaTotalVentas += productos[i].CantidadVendida;
            }
        }

        public string getGen()
        {
            string prod = "";
            foreach (ProductoGen item in productos)
            {
                prod += item.IdProducto;
            }
            return prod;
        }
    }
}
