package base;

/**
 * esta clase tiene el menu de Alumno.
 * 
 * @author Sebastian Morgado
 * @version 1.6.1
 * @since 1.0.1
 */
class Alumno {

	/**
	 * este metodo solo se encarga de mostrar el menu, por lo que no debe retornar
	 * nada.
	 * 
	 * @param datos son los datos del alumno. incluye sus nombres, apellidos y rut.
	 */
	static void menu(String[] datos) {
		Boolean continuar = true;
		String Nombre_Alumno = datos[0] + " " + datos[1];

		do {
			System.out.print("\nBienvenid@ " + Nombre_Alumno + ", por favor indique la accion que desea realizar:\n\n1) Responder preguntas por defecto (generadas por el codigo). \n2) Responder preguntas generadas por su profesor.\n3) salir del programa y cerrar sesion.\n\nsu respuesta: ");
			switch (Texto.leer_teclado()) {
			case "1": // responder preguntas generadas por mi
				ExamenDemo.ExamenBase(Nombre_Alumno);
				return;
			case "2": // responder preguntas del profesor
				Examen.ResponderPreguntasProfesor(Nombre_Alumno);
				return;
			case "3": // salir
				continuar = false;
				break;
			default:
				System.err.println("opcion invalida, intentelo de nuevo.");
			}
		} while (continuar);
	}
}