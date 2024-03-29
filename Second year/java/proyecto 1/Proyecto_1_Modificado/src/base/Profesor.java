package base;

/**
 * Esta clase tiene el menu de Profesor, ademas de algunas caracteristicas que este usa.
 * @author Sebastian Morgado
 * @version 1.6.1
 * @since 1.0.1
 */
public class Profesor {

	/**
	<h1>esta funcion se encarga de preguntarle al profesor que accion desea realizar.</h1>
	
	<h2>las opciones son:</h2>
	<ol>
		<li>agregar preguntas</li>
		<li>eliminar preguntas</li>
		<li>manejar los resultados de los alumnos</li>
		<li>salir al menu principal</li>
	</ol>
	 */
	public static void menu() {
		boolean continuar = true;
		do {
			System.out.println("\nBienvenido profesor, por favor indique la accion que desea realizar:\n\n1) agregar preguntas \n2) eliminar preguntas\n3) manejar los resultados de los alumnos\n4) salir al menu principal\n");
			
			String respuesta = Texto.leer_teclado();
			switch (respuesta) {
			case "1":// agregar preguntas
				Examen.Agregar_Preguntas_Examen();
				break;
			case "2":// eliminar preguntas
				Examen.Eliminar_Preguntas_Examen();
				break;
			case "3":// Manejar los resultados de los alumnos.
				RevisionAlumno.Menu_Acciones_Resultados_Alumnos();
				break;
			case "4":// salir
				continuar = false;
				break;
			default:
				System.err.println("opcion invalida, intentelo de nuevo.");
			}	
		} while (continuar);
	}
}