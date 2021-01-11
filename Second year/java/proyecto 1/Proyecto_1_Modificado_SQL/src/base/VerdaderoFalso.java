package base;

/**
 * Esta es una clase que se encarga de hacer todo lo relacionado con
 * VerdaderoFalso, como por ejemplo: crear la pregunta de tipo VerdaderoFalso
 * usando constructores, como tambien preguntar, comparar y retornar true/false
 * si el usuario ha respondido correctamente.
 * 
 * @author Sebastian Morgado
 * @version 1.6.1
 * @since 0.0.5
 */

class VerdaderoFalso extends Pregunta {

	/**
	 * la variable respuesta almacena la respuesta correcta de la pregunta.
	 */
	private boolean respuesta;
	
	/**
	 * la variable respuestas_usuario almacena la respuesta del alumno.
	 */
	private String respuestas_usuario;
	
	/**
	 * la variable mostrar_respuestas sirve para mostrar las respuestas 
	 * si el alumno se equivoca.
	 */
	private boolean mostrar_respuestas;
	
	/**
	 * la variable numero_pregunta sirve para saber el numero de la pregunta.
	 */
	private int numero_pregunta;
	
	/**
	 * este es el constructor que toma la pregunta, la respuesta y el peso, y la
	 * convierte a tipo verdaderofalso.
	 * 
	 * @param pregunta  es la pregunta que se desea guardar en verdadero falso.
	 * @param respuesta esta puede ser solo tipo booleano ({@code true o false})
	 * @param peso      es el peso de la pregunta (o tambien cuanto vale esta), solo
	 *                  acepta valores de tipo int.
	 */
	VerdaderoFalso(String pregunta, boolean respuesta, int peso) {
		super(pregunta, peso);
		this.respuesta = respuesta;
	}

	/**
	 * esta funcion se encarga de obtener y generar las preguntas del profesor.
	 * 
	 * @return retorna la pregunta en el tipo verdaderofalso
	 * @throws NumberFormatException si el usuario ingresa letras en vez de numeros.
	 */
	static VerdaderoFalso crearPreguntaVF() {
		int peso = 0;
		boolean respuesta_usuario_vf = false;
		String pregunta_usuario = null;
		String respuesta_usuario = null;
		
		boolean continuar = true;

		System.out.println("\nprimero necesito que ingreses la pregunta:\n");
		pregunta_usuario = Texto.leer_teclado();

		do {
			System.out.println("\nahora necesito la respuesta correcta que tiene tu pregunta. (ingrese v o f)\n");
			respuesta_usuario = Texto.leer_teclado();
			if (respuesta_usuario.substring(0).equalsIgnoreCase("t") || respuesta_usuario.substring(0).equalsIgnoreCase("v") || respuesta_usuario.substring(0).equalsIgnoreCase("f")) {
				if (respuesta_usuario.substring(0).equalsIgnoreCase("t") || respuesta_usuario.substring(0).equalsIgnoreCase("v")) {
					respuesta_usuario_vf = true;
				} else {
					respuesta_usuario_vf = false;
				}
				continuar = false;
			} else {
				System.err.println("\n\npor favor intente escribir v o f\n\n");
			}
		} while (continuar);

		continuar = true;
		
		do {
			try {
				System.out.println("\ny por ultimo necesito el valor de la pregunta (ej: 10 pts.) \n");
				peso = Integer.parseInt(Texto.leer_teclado());
				if (peso <= 0) {
					System.out.println("\n\nestas seguro de eso?, estas a punto de asignar " + peso + " a una pregunta,\nlo que podria bajar puntaje..., si estas de acuerdo, escribe 'SI', si no, escribe cualquier cosa.");
					if (Texto.leer_teclado().equalsIgnoreCase("si")) {
						continuar = false;
					}
				} else {
					continuar = false;
				}
			} catch (Exception e) {
				System.err.println("\n\npor favor, ingrese NUMEROS, las letras u otros caracteres NO sirven.\n\n");
			}
		} while (continuar);

		return new VerdaderoFalso(pregunta_usuario, respuesta_usuario_vf, peso);
	}

	/**
	 * esta funcion se encarga de consultarle al alumno.
	 * 
	 * @return retorna true si el alumno respondio bien, false si respondio mal.
	 */
	public boolean buscar() {
		String Respuesta_usuario = null;
		boolean Respuesta_usuario_boolean = false;
		
		boolean continuar = true;
		
		do {
			System.out.print("\n" + this.numero_pregunta + ") " + getPregunta() + "\n\nsu respuesta: ");
			Respuesta_usuario = Texto.leer_teclado();

			if (Respuesta_usuario.substring(0,1).equalsIgnoreCase("t") || Respuesta_usuario.substring(0,1).equalsIgnoreCase("v")) {
				Respuesta_usuario_boolean = true;
				respuestas_usuario = (String.valueOf(Respuesta_usuario_boolean));
				continuar = false;
			} else if (Respuesta_usuario.substring(0,1).equalsIgnoreCase("f")) {
				Respuesta_usuario_boolean = false;
				respuestas_usuario = (String.valueOf(Respuesta_usuario_boolean));
				continuar = false;
			} else {
				System.err.println("\n\npor favor intente escribir v o f. intentos restantes: " + cantidad_intentos + "\n\n");
				cantidad_intentos--;
			}
		} while (cantidad_intentos > -1 && continuar);

		if (cantidad_intentos < 0) {
			System.err.println("\n\nse acabaron los intentos. vaya a estudiar e intentelo nuevamente.\n\n");
			System.exit(0);
		}
		
		if (Respuesta_usuario_boolean == this.respuesta) {
			if (mostrar_respuestas == true) {
				System.out.println("\n\nCorrecto!, la respuesta era " + this.respuesta + "!!!\n\n\n");
			}
			return true;
		} else {
			if (mostrar_respuestas == true) {
				System.out.println("\n\nIncorrecto :/, la respuesta era " + this.respuesta + "\n\n");
			}
			return false;
		}
	}

	/**
	 * esta funcion se encarga de setear la opcion de mostrar la
	 * respuesta.
	 * 
	 * @param respuesta si esta opcion es {@code true}, cuando el
	 * alumno se equivoque al responder, mostrara la respuesta 
	 * correcta. si es {@code false}, no mostrara nada.
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
	 * esta funcion se encarga de imprimir las preguntas por pantalla en la consola.
	 */
	public void mostrar() {
		System.out.println("- " + getPregunta() + "\n");
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
	 * esta funcion se encarga de obtener la respuesta correcta.
	 * 
	 * @return retorna la respuesta de verdaderofalso (puede ser
	 *         {@code true o false})
	 */
	public boolean getRespuesta() {
		return this.respuesta;
	}
}
