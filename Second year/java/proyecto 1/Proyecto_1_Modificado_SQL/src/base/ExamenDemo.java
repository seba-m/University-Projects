package base;

/**
 * Esta clase tiene las preguntas que por defecto se solicitan.
 * @author Sebastian Morgado
 * @version 1.6.1
 * @since 0.0.5
 */
class ExamenDemo {

	/**
	 * Esta funcion contiene las preguntas que por defecto trae el examen.
	 * @param Nombre_Alumno es el nombre del alumno.
	 */
	static void ExamenBase(String Nombre_Alumno) {
		// crea un examen con un total de 10 puntos: 8 preguntas de un punto y una de
		// dos puntos
		Examen miExam = new Examen();
		/*
		 miExam.agregaPregunta(new RespuestasCortas("los puntos A y B se encuentran en un circulo con radio 1, y el arco AB tiene una longitud de PI/3, \nque fraccion de la circunferencia del circulo es la longitud del arco AB?" ,"1/6",1)); 
		 miExam.agregaPregunta(new RespuestasCortas("tenemos la siguiente ecuacion:\n\n(8-i)/(3-2i)\n\nsi esta expresion es reescrita en la forma a+bi, \ndonde a y b son numeros reales, cual es el valor de a? (considere i = Sqrt(-1))","2+i",1)); 
		 miExam.agregaPregunta(new RespuestasCortas("En el triangulo ABC, la medida de angulo B es 90 grados, BC = 16, y AC = 20. \nel triangulo DEF es similar al triángulo ABC, donde los vértices D, E y F \ncorresponden a los vértices A, B y C, respectivamente, y cada lado del triángulo DEF es 1/3 de la longitud \ndel lado correspondiente del triangulo ABC. ¿cual es el valor de Sen(F)?","3/5",1)); 
		 miExam.agregaPregunta(new RespuestasCortas("Si 'x' es el promedio (media aritmética) de m y 9, 'y' es el promedio de 2m y 15,\ny 'z' es el promedio de 3m y 18, ¿cuál es el promedio de x, y, y z en términos de m?","m+7",1));
		 
		 miExam.agregaPregunta(new VerdaderoFalso("Para un polinomio p(x), el valor de p(3) es -2, con lo anterior,\\n¿es verdad que el resto cuando p(x) se divide por x-3 es siempre -2?",true,1)); 
		 miExam.agregaPregunta(new VerdaderoFalso("q = (1/2)*n*v^2\nLa presión dinámica q generada por un fluido que se mueve a una velocidad v\nse puede encontrar usando la fórmula anterior, donde n es la densidad constante del fluido.\nUn ingeniero aeronáutico utiliza la fórmula para encontrar la presión dinámica de un fluido \nque se mueve con velocidad v y el mismo fluido que se mueve con velocidad 1,5v.\n\nusando lo anterior, podremos obtener la relacion entre la presion\ndinamica del fluido más rápido y la presión dinámica del fluido más lento, de la siguiente manera:\n\nDejemos que q1 sea la presión dinámica del fluido más lento que se mueve con la velocidad (v1),\ny dejemos que q2 sea la presión dinámica del fluido más rápido que se mueve con la velocidad (v2). Entonces\n\n(v2)=1.5(v1)\n\nDada la ecuación q = (1/2) * n * (v^2), sustituyendo la presión dinámica\ny la velocidad del fluido más rápido se obtiene q2=(1/2)n(v2)^2. Como v2=1,5*v1,\nla expresión 1,5*v1 puede sustituirse por v2 en esta ecuación, dando q2 = (1/2) * n * (1,5(v1))^2.\nAl reescribir la ecuación anterior tenemos:\n\nq2=(1.5)(1/2)n(v1)^2=(1.5)q1\n\nPor lo tanto, la relación de la presión dinámica del fluido más rápido es\n\nq2/q1=(1.5*q1)/q1 = 1.5\n\npor lo que la respuesta es 1.5, o 3/2.\n\nes correcta esta respuesta?\n",false,1)); 
		 miExam.agregaPregunta(new VerdaderoFalso("Alma compró un ordenador portátil en una tienda que le hizo un 20 por ciento de descuento de su precio original. \nLa cantidad total que pagó a la cajera fue de p dólares, incluyendo un 8 por ciento de impuesto sobre las ventas en el precio de descuento.\n\ncon lo anterior, podriamos plantear en terminos de p, el precio original de lo que pago por la computadora, de la siguiente manera: \n\ntenemos que el precio original vale el 100%, si le aplicamos el 20%, quedaria en 80% (o 0.8).\ny el precio por el que pago fue 100% (o 1), y si le sumamos el 8% (o 0.08), quedaria un total de 108% (o tambien 1.08).\n\ncon los datos anteriores, podemos plantear la ecuacion para obtener el precio de la siguiente manera:\n\n(0.8)*(1.08)*p \n\ndonde: \n\n0.8 es el descuento.\n1.08 precio por el que pago.\np precio original en dolares.\n\ndespues de haber resuelto el ejercicio, ¿es correcta la formula '(0.8)*(1.08)*p\'?",false,1)); 
		 miExam.agregaPregunta(new VerdaderoFalso("si a es un número entero impar y b es un número entero par, ¿la formula a+2b da como resultado un numero entero par?",false,1));

		 miExam.agregaPregunta(new SeleccionMultiple("C = (5/9)*(F-32)\n\nla ecuacion de arriba nos muestra como la temperatura F (medida en grados Farenheit),\nse relaciona con una temperatura C (medida en grados celcius). Basandonos en la ecuacion,\n¿cual de las siguientes respuestas es verdadera?\n\nI) Un aumento de la temperatura de 1 grado Farenheit equivale a un aumento\nde la temperatura de 5/9 grados celcius\n\nII) Un aumento de la temperatura de 1 grado celsius equivale a un aumento\nde la temperatura de 1,8 grados Fahrenheit\n\nIII) Un aumento de la temperatura de 5/9 grados Fahrenheit equivale a un aumento\nde la temperatura de 1 grado celsius.\n\n",new String[] {"Solo I","Solo II","Solo III","Solo I y II"},3,1));
		 miExam.agregaPregunta(new SeleccionMultiple("La ecuacion ((24x^2+25x-47)/(ax-2)) = (-8x-3-(53/(ax-2))) es verdadero para todos los valores de x distinto de 2/a,\ndonde a es una constante. cual es el valor de a?",new String[] {"-16","-3","3","16"},1,1)); 
		 miExam.agregaPregunta(new SeleccionMultiple("si 3x-y = 12, cual es el valor de ((8^x)/(2^y))?",new String[] {"2^12", "4^4", "8^2","El valor no se puede determinar con la informacion dada"},0,1));
		 miExam.agregaPregunta(new SeleccionMultiple("En el plano xy, el punto (p,r) se encuentra en la línea con la ecuación y=x+b, donde b es una constante. \nEl punto con coordenadas (2p,5r) se encuentra en la línea con la ecuación y=2x+b. Si p!=0, ¿cuál es el valor de (r/p)?" ,new String[] {"2/5","3/4","4/3","5/2"},1,1));
		 */

		miExam.agregaPregunta(new VerdaderoFalso("La capital de Chile es Santiago", true, 1));

		String santiagoElec[] = { "Santiago", "Valparaiso", "Concepcion", "Magallanes", "Arica" };
		miExam.agregaPregunta(new SeleccionMultiple("Cuál es la capital de Region Metropolitana", santiagoElec, 0, 1));

		miExam.agregaPregunta(new RespuestasCortas("Cuál es la capital de IV Region", "Coquimbo", 1));
		miExam.agregaPregunta(new VerdaderoFalso("La capital de Alberta es Calgary", false, 1));

		String BCElec[] = { "Victoria", "Vancouver", "Nanaimo" };
		miExam.agregaPregunta(new SeleccionMultiple("Cuál es la capital de British Columbia", BCElec, 0, 1));

		miExam.agregaPregunta(new RespuestasCortas("Cuál es la capital de Argentina", "Buenos Aires", 1));
		miExam.agregaPregunta(new RespuestasCortas("Cuál es la capital de Canada", "Ottawa", 2));
		miExam.agregaPregunta(new VerdaderoFalso("La capital de La Serena es Illapel", false, 1));
		
		String PElec[] = { "Brasilia", "Rio de Janeiro", "Sao Paulo", "Blumenau" };
		
		miExam.agregaPregunta(new SeleccionMultiple("Cuál es la capital de Brasil?", PElec, 0, 1));
		
		miExam.setDesordenar(true);
		miExam.setMostrarRespuestas(true);
		miExam.setTiempoLimite(false, 0);
		miExam.setCantidadIntentos(8);
		
		// dar examen y reportar el puntaje
		int puntaje_prueba = miExam.darExam();

		float Nota_Final = ((float)miExam.calcular_nota(puntaje_prueba, 60)/10);
		
		String Nota_Final_String = String.format("%.1f", Nota_Final);
		
		System.out.println("tu puntaje fue: " + puntaje_prueba + ", lo que significa que obtuviste un " + (int) (((float) puntaje_prueba / miExam.obtener_puntaje_total()) * 100) + "% de preguntas correctas respondidas. tu nota final es un " + Nota_Final_String + " " + ((Nota_Final >= 4) ? ", felicidades, aprobaste." : ", te falta estudiar... reprobaste."));
		
		try {
			Archivos.Guardar_Resultados("preguntas_base", Nombre_Alumno, miExam.obtener_Respuestas_Usuario(),String.valueOf(puntaje_prueba), Nota_Final_String);
		} catch (IllegalArgumentException e) {
			System.err.println(e.getMessage());
		}
		
		return;
	}
}