package base;

/**
 * esta clase tiene el menu de Alumno.
 * @author Sebastian Morgado
 * @version 1.6.1
 * @since 1.0.1
 */
public class Alumno {

	/**
	 * este metodo solo se encarga de mostrar el menu, por lo que no debe retornar nada.
	 * @param Nombre_Alumno es el nombre del alumno. algunos metodos lo usan.
	 */
	public static void menu(String Nombre_Alumno) {
		Boolean continuar = true;
		do {
			System.out.print("\nBienvenido " + Nombre_Alumno + ", por favor indique la accion que desea realizar:\n\n1) Responder preguntas por defecto (generadas por el codigo). \n2) Responder preguntas generadas por su profesor.\n3) salir al menu principal\n\nsu respuesta: ");
			switch (Texto.leer_teclado()) {
			case "1": // responder preguntas generadas por mi
				ExamenDemo.ExamenBase(Nombre_Alumno);
				return;
			case "2": // responder preguntas del profesor
				Examen.ResponderPreguntasProfesor(Nombre_Alumno);
				return;
			case "3": // salir
				return;
			default:
				System.err.println("opcion invalida, intentelo de nuevo.");
			}
		} while (continuar);
	}

}