package base;

/**
 * Esta es una clase que se encarga de hacer todo lo relacionado con
 * RespuestasCortas, como por ejemplo: crear la pregunta de tipo
 * RespuestasCortas usando constructores, como tambien preguntar, comparar y
 * retornar true/false si el usuario ha respondido correctamente.
 * 
 * @author Sebastian Morgado
 * @version 1.6.1
 * @since 0.0.5
 */
class RespuestasCortas extends Pregunta {

	/**
	 * la variable respuesta almacena la respuesta del alumno.
	 */
	private String respuesta;

	/**
	 * la variable respuestas_usuario almacena la respuesta del alumno.
	 */
	private String respuestas_usuario;

	/**
	 * la variable mostrar_respuestas sirve para mostrar las respuestas si el alumno
	 * se equivoca.
	 */
	private boolean mostrar_respuestas;

	/**
	 * la variable numero_pregunta sirve para saber el numero de la pregunta.
	 */
	private int numero_pregunta;

	/**
	 * este es el constructor que toma la pregunta, la respuesta y el peso, y la
	 * convierte a tipo respuestascortas.
	 * 
	 * @param pregunta  es la pregunta que se desea guardar en verdadero falso.
	 * @param respuesta es la opcion correcta. al ser el "index", solo acepta tipos
	 *                  enteros, y parte de cero.
	 * @param peso      es el peso de la pregunta (o tambien cuanto vale esta), solo
	 *                  acepta valores de tipo int.
	 */

	RespuestasCortas(String pregunta, String respuesta, int peso) {
		super(pregunta, peso);
		this.respuesta = respuesta;
	}

	/**
	 * esta funcion se encarga de obtener y generar las preguntas del profesor.
	 * 
	 * @return retorna la pregunta en el tipo respuestascortas
	 * @throws NumberFormatException si el usuario ingresa letras en vez de numeros.
	 */
	static RespuestasCortas crearPreguntaRC() {

		int peso = 0;
		String pregunta_usuario = null;
		String respuesta_usuario = null;

		boolean continuar = true;

		System.out.println("\nprimero necesito que ingreses la pregunta:\n");
		pregunta_usuario = Texto.leer_teclado();

		System.out.println("\nahora necesito la respuesta correcta que tiene tu pregunta.\n");
		respuesta_usuario = Texto.leer_teclado();

		do {
			try {
				System.out.println("\npor ultimo, necesito el puntaje que vale tu pregunta. (ej: 10 pts.)\n");
				peso = Integer.parseInt(Texto.leer_teclado());
				if (peso <= 0) {
					System.out.println("\n\nestas seguro de eso?, estas a punto de asignar " + peso
							+ " a una pregunta,\nlo que podria bajar puntaje..., si estas de acuerdo, escribe 'SI', si no, escribe cualquier cosa.\n");
					if (Texto.leer_teclado().equalsIgnoreCase("si")) {
						continuar = false;
					}
				} else {
					continuar = false;
				}
			} catch (Exception e) {
				System.err.println("\n\npor favor, ingrese un numero. intentelo nuevamente.\n\n");
			}
		} while (continuar);
		return new RespuestasCortas(pregunta_usuario, respuesta_usuario, peso);
	}

	/**
	 * esta funcion se encarga de consultarle al alumno.
	 * 
	 * @return retorna true si el alumno respondio bien, false si respondio mal.
	 */
	public boolean buscar() {
		System.out.print("\n" + this.numero_pregunta + ") " + getPregunta() + "\n\nsu respuesta: ");

		String Respuesta_Alumno = Texto.leer_teclado();

		this.respuestas_usuario = (Respuesta_Alumno);

		if (Respuesta_Alumno.equalsIgnoreCase(respuesta)) {
			if (mostrar_respuestas == true) {
				System.out.println("\n\nCorrecto!, la respuesta era " + respuesta + "!!!\n\n");
			}
			return true;
		} else {
			if (mostrar_respuestas == true) {
				System.out.println("\n\nIncorrecto :/, la respuesta era " + respuesta + "\n\n");
			}
			return false;
		}
	}

	/**
	 * esta funcion se encarga de imprimir las preguntas por pantalla en la consola.
	 */
	public void mostrar() {
		System.out.println("- " + getPregunta() + "\n");
	}

	/**
	 * esta funcion se encarga de setear la respuesta del alumno.
	 * 
	 * @param respuesta es la respuesta del alumno.
	 * 
	 */
	public void setRespuestaAlumno(String respuesta) {
		this.respuestas_usuario = respuesta;
	}

	/**
	 * esta funcion se encarga de setear el numero de la pregunta.
	 * 
	 * @param numero_pregunta es el numero de la pregunta, por ejemplo: numero_pregunta) cuanto es
	 *                 1 + 1? -> 1) cuanto es
	 *                 1 + 1? , como vemos, numero_pregunta seria esta variable que
	 *                 seteamos.
	 */
	public void setNumeroPregunta(int numero_pregunta) {
		this.numero_pregunta = numero_pregunta;
	}

	/**
	 * esta funcion se encarga de setear la opcion de mostrar la respuesta.
	 * 
	 * @param respuesta si esta opcion es {@code true}, cuando el alumno se
	 *                  equivoque al responder, mostrara la respuesta correcta. si
	 *                  es {@code false}, no mostrara nada.
	 */
	public void setMostrarRespuestas(boolean respuesta) {
		this.mostrar_respuestas = respuesta;
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
	 * esta funcion se encarga de obtener la respuesta correcta.
	 * 
	 * @return retorna la respuesta.
	 */
	public String getRespuesta() {
		return respuesta;
	}
}