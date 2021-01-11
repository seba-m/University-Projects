package main;

/**
 * Un programa principal para testear.
 * 
 * @version 1.0.0
 */
public class ExamDemo { // NO_UCD (unused code)
	/**
	 * Este programa principal crea un pequeño examen y lo administra, tal como se
	 * espera.
	 * 
	 * @param args son los argumentos con los cuales se le desea inicializar el
	 *             programa, aunque por ahora no se usan.
	 */
	public static void main(String args[]) {
		// crea un examen con un total de 10 puntos: 8 preguntas de un punto y una de dos puntos
		Exam miExam = new Exam();
		miExam.agregaPregunta(new TFpregunta("La capital de Chile es Santiago", true, 1));

		String santiagoElec[] = { "Santiago", "Valparaiso", "Concepcion", "Magallanes", "Arica" };
		miExam.agregaPregunta(new Selec_Mul_Pregunta("Cuál es la capital de Region Metropolitana", santiagoElec, 0, 1));

		miExam.agregaPregunta(new Resp_Cortas_Pregunta("Cuál es la capital de IV Region", "Coquimbo", 1));
		miExam.agregaPregunta(new TFpregunta("La capital de Alberta es Calgary", false, 1));

		String BCElec[] = { "Victoria", "Vancouver", "Nanaimo" };
		miExam.agregaPregunta(new Selec_Mul_Pregunta("Cuál es la capital de British Columbia", BCElec, 0, 1));

		miExam.agregaPregunta(new Resp_Cortas_Pregunta("Cuál es la capital de Argentina", "Buenos Aires", 1));
		miExam.agregaPregunta(new Resp_Cortas_Pregunta("Cuál es la capital de Canada", "Ottawa", 2));
		miExam.agregaPregunta(new TFpregunta("La capital de La Serena es Illapel", false, 1));

		String PElec[] = { "Brasilia", "Rio de Janeiro", "Sao Paulo", "Blumenau" };
		miExam.agregaPregunta(new Selec_Mul_Pregunta("Cuál es la capital de Brasil?", PElec, 0, 1));

		// dar examen y reportar el puntaje
		int puntaje = miExam.darExam();
		// System.out.println("su resultado es " + puntaje + "%");
		System.out.println("\nBien, tu puntaje fue: " + puntaje + " lo cual supone que tuviste un " + (int) (((float) puntaje / 10) * 100) + "% (necesitas minimo 5 puntos o un 50% para aprobar)");
	} // end main
} // end class ExamDemo
