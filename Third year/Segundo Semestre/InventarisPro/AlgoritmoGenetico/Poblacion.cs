namespace InventarisPro.AlgoritmoGenetico
{
    public class Poblacion
    {
        public ProductoGen[] Productos;
        public int[] individuo { set; get; }
        public int fitness { set; get; }

        public Poblacion(){}

        public Poblacion(ProductoGen[] Productos, int[] indi, int fit)
        {
            this.Productos = Productos;
            individuo = indi;
            fitness = fit;
        }
    }
}
