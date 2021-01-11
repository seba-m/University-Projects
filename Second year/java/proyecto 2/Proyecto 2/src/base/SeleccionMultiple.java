package base;

/**
 * Esta es una clase que se encarga de hacer todo lo relacionado con
 * SeleccionMultiple, como por ejemplo: crear la pregunta de tipo
 * SeleccionMultiple usando constructores, como tambien preguntar, comparar y
 * retornar true/false si el usuario ha respondido correctamente.
 * 
 * @author Sebastian Morgado
 * @version 2.0.0
 * @since 0.0.5
 */
public class SeleccionMultiple extends Pregunta { // NO_UCD (use default)

	/**
	 * la variable opciones almacena las opciones de la pregunta.
	 */
	private String opciones[];

	/**
	 * la variable respuesta almacena la respuesta correcta de la pregunta.
	 */
	private int respuesta;

	/**
	 * la variable respuestas_usuario almacena la respuesta del alumno.
	 */
	private String respuestas_usuario;

	/**
	 * la variable mostrar_respuestas sirve para mostrar las respuestas 
	 * si el alumno se equivoca.
	 */
	@Deprecated
	private boolean mostrar_respuestas;
	
	/**
	 * la variable numero_pregunta sirve para saber el numero de la pregunta.
	 */
	@Deprecated
	private int numero_pregunta;
	
	/**
	 * este es el constructor que toma la pregunta, las opciones, la respuesta y el
	 * peso, y la convierte a tipo seleccionmultiple
	 * 
	 * @param pregunta  es la pregunta que se desea guardar en verdadero falso.
	 * @param Opciones  es el array de strings en donde se guardan todas las
	 *                  opciones.
	 * @param respuesta es la opcion correcta. al ser el "index", solo acepta tipos
	 *                  enteros, y parte de cero.
	 * @param peso      es el peso de la pregunta (o tambien cuanto vale esta), solo
	 *                  acepta valores de tipo int.
	 */
	public SeleccionMultiple(String pregunta, String[] Opciones, int respuesta, int peso) {
		super(pregunta, peso);
		this.opciones = Opciones;
		this.respuesta = respuesta;
	}

	/**
	 * esta funcion se encarga de obtener las opciones de la pregunta.
	 * 
	 * @return retorna el arreglo de opciones.
	 */
	public String[] getOpciones() {
		return this.opciones;
	}

	/**
	 * esta funcion se encarga de obtener la respuesta correcta.
	 * 
	 * @return retorna la respuesta correcta.
	 */
	public int getRespuesta() {
		return this.respuesta;
	}

	/**
	 * esta funcion se encarga de obtener la respuesta del alumno.
	 * 
	 * @return retorna la respuesta.
	 */
	public String getRespuestaAlumno() {
		return this.respuestas_usuario;
	}

	/**
	 * esta funcion se encarga de setear la respuesta del alumno.
	 * 
	 * @param respuesta es la respuesta del alumno.
	 */
	public void setRespuestaAlumno(String respuesta) {
		this.respuestas_usuario = respuesta;
	}

	/**
	 * esta funcion se encarga de consultarle al alumno.
	 * 
	 * @return retorna true si el alumno respondio bien, false si respondio mal.
	 * @throws NumberFormatException si el usuario ingresa letras en vez de numeros.
	 */
	@Deprecated
	public boolean buscar() {

		// estas condiciones sirven para que, bajo ninguna circunstancia, se puedan
		// responder preguntas invalidas.
		boolean verificarPregunta = ((getPregunta() == null) || getPregunta().isBlank() || getPregunta().isEmpty());
		boolean verificarRespuesta = (getRespuesta() < 0 || getRespuesta() > getOpciones().length);
		boolean verificarOpciones = ((getOpciones() == null) || getOpciones().length < 2);

		// este if es para evitar mostrarle al alumno preguntas invalidas (sin
		// preguntas, sin opciones, sin respuestas o con respuesta fuera de rango)

		if (!verificarPregunta && !verificarRespuesta && !verificarOpciones) {
			int respuesta_usuario = 0;
			boolean continuar = true;

			do {
				try {
					System.out.println("\n" + this.numero_pregunta + ") " + getPregunta() + "\n");

					/*
					 * for (int k = 0; k < opciones.length; k++) { System.out.print(k + 1 + ") " +
					 * opciones[k] + "\t"); }
					 */

					for (int k = 0; k < this.opciones.length; k++) {
						System.out.println("\t" + ((char) (97 + k)) + ") " + this.opciones[k]); // 97 es la 'a'
																								// minuscula
					}

					System.out.print("\nsu respuesta: ");
					respuesta_usuario = ((int) Texto.leer_teclado().toLowerCase().charAt(0)) - 96;
					if ((respuesta_usuario > 0) && (respuesta_usuario <= opciones.length)) {
						continuar = false;
					} else {
						System.err.println(
								"\n\npor favor ingrese una letra valida (entre a y " + ((char) ((opciones.length) + 96))
										+ "). intentos restantes: " + cantidad_intentos + "\n\n");
						cantidad_intentos--;
					}

				} catch (Exception e) {
					System.err.println("\n\npor favor ingresa LETRAS, otros caracteres no sirven. intentos restantes: "
							+ cantidad_intentos + "\n\n");
					cantidad_intentos--;
				}
			} while (cantidad_intentos > -1 && continuar);

			if (cantidad_intentos < 0) {
				System.err.println("\n\nse acabaron los intentos. vaya a estudiar e intentelo nuevamente.\n\n");
				System.exit(0);
			}

			respuestas_usuario = (String.valueOf((respuesta_usuario - 1)));

			if (respuesta_usuario == respuesta + 1) {
				if (mostrar_respuestas == true) {
					System.out.println("\n\nCorrecto!, la respuesta era la letra " + respuesta_usuario + " ("
							+ opciones[respuesta] + ")!!!\n\n\n");
				}
				return true;
			} else {
				if (mostrar_respuestas == true) {
					System.out.println("\n\nIncorrecto :/, la respuesta era la letra " + respuesta_usuario + " ("
							+ opciones[respuesta] + ")\n\n");
				}
				return false;
			}
		} else {
			return false;
		}
	}

	/**
	 * esta funcion se encarga de imprimir las preguntas por pantalla en la consola.
	 */
	@Deprecated
	public void mostrar() {
		System.out.println(") " + getPregunta() + "\n");
		for (int k = 0; k < this.opciones.length; k++) {
			System.out.println("\t" + ((char) (97 + k)) + ") " + this.opciones[k]); // 97 es la 'a' minuscula
		}
		System.out.println("\n\n");
	}
}
