namespace InventarisPro.AlgoritmoGenetico
{

    public class AlgoritmoGeneticoImpl
    {
        /// <summary>
        /// 
        /// Se ejecuta la funcion donde se pasa el inventario
        /// de productos como parámetro, tambien las generaciones
        /// que se quieren iterar, ademas de la cantidad de
        /// poblacion por generacion, la Probabilidad de mutación
        /// y finalmente el porcentaje de Elitismo en la poblacion.
        /// 
        /// </summary>
        /// <param name="Productos"></param>
        /// <param name="generaciones"></param>
        /// <param name="individuos"></param>
        /// <param name="probMutacion"></param>
        /// <param name="porcElitismo"></param>
        /// <returns></returns>
        public static Poblacion Calcular(List<ProductoGen> Productos, int generaciones, int individuos, double probMutacion, double porcElitismo)
        {
            int n = Productos.Count; //Se obtiene la cantidad de productos de la lista
            double Nelite = Math.Ceiling((individuos * porcElitismo) / 100.0); //se calcula el numero de individuos
                                                                               //que se considerarán para el elitismo
            List<int> T = Tgenerar(Productos);      //Se obtiene el arreglo del valor de utilidad que generan los productos
            int[] HQ = HQgenerate(Productos);       //Se obtiene el arreglo de cantidad de veces vendidas
            int cromTam = T.Count;                  //Tamaño del cromosoma
            double Cmut = (HQ.Average() * n) / getPatron(n);
            int[,] PopIni = getPoblInic(new int[individuos, Productos.Count], individuos, cromTam, Cmut); //Se genera la
                                                                                                          //poblacion inicial
            int[,] PopOld = PopIni;                 // Se guarda la poblacion inicial como antigua
            int[] fitness = fitnes(PopOld, T);      // Se calcula el fitnes
            var generacion = 0;                     // Contador de generaciones

            Poblacion final = new Poblacion();      //poblacion que será retornada

            while (generacion < generaciones)       // Mientras no se llegue a la generación final
            {
                List<int[]> NewPob = new List<int[]>(); //Nueva poblacion

                cruce(PopOld, NewPob, fitness, individuos, Nelite, cromTam); //funcion cruce
                mutacion(PopOld, NewPob, probMutacion, Cmut, individuos, cromTam); //funcion mutación

                int[,] aux = convertMatrix(NewPob);
                int[] fit = fitnes(PopOld, T);      // Se calcula el fitnes de la poblacion antigua
                List<Poblacion> pNew = new List<Poblacion>();
                pNew = getPoblacion(Productos, aux, fit);
                pNew.Sort((x, y) => x.fitness.CompareTo(y.fitness)); //ordena la poblacion por el mejor fitness
                PopOld = aux;
                generacion++;
                if (generacion == generaciones) //generacion que retorna
                {
                    final = pNew[0];
                }

            }
            return final;
        }

        /// <summary>
        /// 
        /// Esta funcion realiza una mutacion a la poblacion
        /// nueva tomando a cada cromosoma de la poblacion 
        /// antigua.
        /// Se toma un valor random entre 0 y 1, si el valor es
        /// menor a la probabilidad de mutacion se realiza una 
        /// mutacion A, sino se realiza una mutacion B.
        /// 
        /// </summary>
        /// <param name="popOld"></param>
        /// <param name="PopNew"></param>
        /// <param name="probMutacion"></param>
        /// <param name="cmut"></param>
        /// <param name="individuos"></param>
        /// <param name="cromTam"></param>
        /// <returns></returns>
        private static List<int[]> mutacion(int[,] popOld, List<int[]> PopNew, double probMutacion, double cmut, int individuos, int cromTam)
        {
            Random r = new Random();
            for (var i = 0; i < individuos; i++)
            {
                for (var j = 0; j < cromTam; j++)
                {
                    var p = r.NextDouble();
                    if (p < probMutacion)
                    {
                        var x = (r.NextDouble() - .5) * cmut;
                        PopNew[i][j] = Convert.ToInt32(popOld[i, j] + x);
                    }
                    else
                    {
                        PopNew[i][j] = popOld[i, j];
                    }
                }
            }
            return PopNew;
        }


        /// <summary>
        /// 
        /// Realiza el cruce comparando dos individuos en posiciones aleatorias por medio
        /// del fitnes para que luego, el que tenga mayor fitnes se pueda reproducir
        /// dando como resultado un hijo1, este mismo proceso se repite una segunda vez
        /// para dar un hijo2, estos finalmente se agregan a la nueva población.
        /// 
        /// </summary>
        /// <param name="popOld"></param>
        /// <param name="PopNew"></param>
        /// <param name="fitness"></param>
        /// <param name="individuos"></param>
        /// <param name="nelite"></param>
        /// <param name="cromTam"></param>
        /// <returns></returns>
        private static List<int[]> cruce(int[,] popOld, List<int[]> PopNew, int[] fitness, int individuos, double nelite, int cromTam)
        {
            var Nijos = 0;
            Random r = new Random();

            while (Nijos + nelite < individuos)
            {
                int r1 = r.Next(individuos);
                int r2 = r.Next(individuos);
                int r3 = r.Next(individuos);
                int r4 = r.Next(individuos);
                int[] patron = new int[cromTam];
                int[] padre1;
                int[] padre2;
                int[] hijo1 = new int[cromTam];
                int[] hijo2 = new int[cromTam];
                for (int i = 0; i < patron.Length; i++)
                {
                    patron[i] = r.Next(1);
                }
                if (fitness[r1] > fitness[r2])
                {
                    padre1 = GetRow(popOld, r1);
                }
                else
                {
                    padre1 = GetRow(popOld, r2);
                }
                if (fitness[r3] > fitness[r4])
                {
                    padre2 = GetRow(popOld, r3);
                }
                else
                {
                    padre2 = GetRow(popOld, r4);
                }
                for (var i = 0; i < cromTam; i++)
                {
                    hijo1[i] = patron[i] * padre1[i] + (1 - patron[i]) * padre2[i];
                    hijo2[i] = (1 - patron[i]) * padre1[i] + patron[i] * padre2[i];
                }
                PopNew.Add(hijo1);
                PopNew.Add(hijo2);
                Nijos += 2;
            }
            return PopNew;
        }


        /// <summary>
        /// 
        /// Calcula el fitnes multiplicando cada cromosoma del individuo
        /// por el valor de utilidad que posee cada producto, para luego 
        /// sumarse y almacenarse en un arreglo con todos los valores fitness
        /// de los individuos.
        /// 
        /// </summary>
        /// <param name="popOld"></param>
        /// <param name="t"></param>
        /// <returns></returns>
        private static int[] fitnes(int[,] popOld, List<int> t)
        {
            int[] fitness = new int[popOld.GetLength(0)];
            for (int i = 0; i < popOld.GetLength(0); i++)
            {
                var sumatoria = 0;
                for (int j = 0; j < popOld.GetLength(1); j++)
                {
                    sumatoria += popOld[i, j] * t[j];
                }
                fitness[i] = sumatoria;
            }
            return fitness;
        }



        /// <summary>
        /// 
        /// Genera la poblacion inicial teniendo
        /// con valores random de entre el minimo del tamaño del
        /// cromosoma y la candidad de indiviuos y el maximo de estos
        /// ultimos.
        /// 
        /// </summary>
        /// <param name="poblacion"></param>
        /// <param name="individuos"></param>
        /// <param name="cromTam"></param>
        /// <param name="Cmut"></param>
        /// <returns></returns>
        public static int[,] getPoblInic(int[,] poblacion, int individuos, int cromTam, double Cmut)
        {
            Random rand = new Random();
            for (int i = 0; i < poblacion.GetLength(0); i++)
            {
                for (int j = 0; j < poblacion.GetLength(1); j++)
                {
                    poblacion[i, j] = Convert.ToInt32(rand.Next(Math.Min(individuos, cromTam), Math.Max(individuos, cromTam)) * Cmut);
                }
            }

            return poblacion;
        }


        /// <summary>
        /// 
        /// Genera el vector que contiene la utilidad que
        /// produce cada producto.
        /// 
        /// </summary>
        /// <param name="productos"></param>
        /// <returns></returns>
        public static List<int> Tgenerar(List<ProductoGen> productos)
        {
            List<int> Coeficientes = new List<int>();
            foreach (ProductoGen p in productos)
            {
                Coeficientes.Add(p.getUtilidad());
            }
            return Coeficientes;
        }



        /// <summary>
        /// 
        /// Genera el vector que contiene la cantidad de veces que se ha
        /// vendido un producto en un plazo dado por el usuario
        /// 
        /// </summary>
        /// <param name="productos"></param>
        /// <returns></returns>
        public static int[] HQgenerate(List<ProductoGen> productos)
        {
            int[] Resultados = new int[productos.Count];

            for (int i = 0; i < Resultados.Length; i++)
            {
                Resultados[i] = productos[i].CantidadVendida;
            }
            return Resultados;
        }





        /// <summary>
        /// 
        /// Se genera el patron para el Cmut teniendo en cuenta la cantidad
        /// de productos que posee el inventario.
        /// 
        /// </summary>
        /// <param name="n"></param>
        /// <returns></returns>
        public static int getPatron(int n)
        {
            return (((n * (n + 1)) / 2) * 6) / 2;
        }

        public static List<int[]> ConvertMatrixList(int[,] arr)
        {
            List<int[]> list = new List<int[]>();
            for (var i = 0; i < arr.GetLength(0); i++)
            {
                list.Add(GetRow(arr, i));
            }
            return list;
        }
        private static List<Poblacion> getPoblacion(List<ProductoGen> Productos, int[,] aux, int[] fit)
        {
            List<Poblacion> pop = new List<Poblacion>();
            for (var i = 0; i < aux.GetLength(0); i++)
            {
                pop.Add(new Poblacion(Productos.ToArray(), GetRow(aux, i), fit[i]));
            }
            return pop;
        }
        private static int[,] convertMatrix(List<int[]> newPob)
        {
            int[,] M = new int[newPob.Count, newPob[0].Length];
            for (int i = 0; i < M.GetLength(0); i++)
            {
                for (int j = 0; j < M.GetLength(1); j++)
                {
                    M[i, j] = newPob[i][j];
                }
            }
            return M;
        }
        public static int[] GetRow(int[,] matrix, int rowNumber)
        {
            return Enumerable.Range(0, matrix.GetLength(1))
                    .Select(x => matrix[rowNumber, x])
                    .ToArray();
        }
    }
}