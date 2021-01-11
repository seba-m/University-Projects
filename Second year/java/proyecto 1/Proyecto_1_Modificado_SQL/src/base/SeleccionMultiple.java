package base;

/**
 * Esta es una clase que se encarga de hacer todo lo relacionado con
 * SeleccionMultiple, como por ejemplo: crear la pregunta de tipo
 * SeleccionMultiple usando constructores, como tambien preguntar, comparar y
 * retornar true/false si el usuario ha respondido correctamente.
 * 
 * @author Sebastian Morgado
 * @version 1.6.1
 * @since 0.0.5
 */
class SeleccionMultiple extends Pregunta {

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
	private boolean mostrar_respuestas;
	
	/**
	 * la variable numero_pregunta sirve para saber el numero de la pregunta.
	 */
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
	SeleccionMultiple(String pregunta, String[] Opciones, int respuesta, int peso) {
		super(pregunta, peso);
		this.opciones = Opciones;
		this.respuesta = respuesta;
	}

	/**
	 * esta funcion se encarga de obtener y generar las preguntas del profesor.
	 * 
	 * @return retorna la pregunta en el tipo seleccionmultiple
	 * @throws NumberFormatException si el usuario ingresa letras en vez de numeros.
	 */
	static SeleccionMultiple crearPreguntaSM() {

		int peso = 0;
		String respuesta_usuario_sm = null;
		int respuesta_pregunta = 0;
		String pregunta_usuario = null;
		String cantidad_opciones = null;
		String Opciones[] = null;
		
		boolean continuar = true;

		System.out.println("\nprimero necesito que ingreses la pregunta:\n");
		pregunta_usuario = Texto.leer_teclado();
		do {
			try {
				System.out.println("\nahora necesito que ingreses la *cantidad* de opciones que tendra tu pregunta (minimo 2 opciones y maximo 10 opciones):\n");
				cantidad_opciones = Texto.leer_teclado();
				if ((Integer.parseInt(cantidad_opciones) > 1) && (Integer.parseInt(cantidad_opciones) < 11)) { // por ahora limito la cantidad de opciones a 10
					Opciones = new String[Integer.parseInt(cantidad_opciones)];
					continuar = false;
				} else {
					System.err.println("\n(psst, recuerda que un examen de seleccion multiple debe tener minimo 2 opciones y maximo 10 opciones...)\n");	
				}
			} catch (Exception e) {
				System.err.println("\n\npor favor ingresa un numero valido, si ingresas una letra u otro simbolo, el mundo puede explotar.\n\n");
			}
		} while (continuar);

		System.out.println("\nahora necesito que ingreses las opciones que tendra tu pregunta:\n");
		for (int i = 0; i < Integer.parseInt(cantidad_opciones); i++) {
			System.out.print("\n" + ((char)(97 + i)) + ") ");
			Opciones[i] = Texto.leer_teclado();
		}

		continuar = true;
		
		do {
			try {
				// esto solo imprime las opciones
				System.out.println("\nahora necesito la LETRA de la respuesta correcta que tiene tu pregunta.\n");
				
				for (int i = 0; i < Opciones.length; i++) {
					System.out.print(((char)(97 + i)) + ") " + Opciones[i] + "\t");
				}
				System.out.print("\n");
				// esto obtiene el numero de la opcion correcta				
				
				respuesta_usuario_sm = Texto.leer_teclado();

				// con esto me aseguro que no escriba -1 o cualquier valor sobre la cantidad de
				// opciones que tiene disponible.
				// por ej: que ponga como respuesta correcta la opcion 1000, en un arreglo de 3
				// opciones.

				if ((int)respuesta_usuario_sm.charAt(0) > (Integer.parseInt(cantidad_opciones)+97) || ((int)respuesta_usuario_sm.charAt(0) < 97)) {
					System.err.println("\n\npor favor, ingrese una letra valida que esté entre a y " + ((char)(Integer.parseInt(cantidad_opciones)+96)) + " (a <= su opcion <= " + ((char)(Integer.parseInt(cantidad_opciones)+96)) + ")\n\n");
				} else {
					respuesta_pregunta = (respuesta_usuario_sm.charAt(0)-97);
					continuar = false;
				}

			} catch (Exception e) {
				System.err.println("\n\npor favor, ingrese NUMEROS, las letras u otros caracteres NO sirven.\n\n");
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

		return new SeleccionMultiple(pregunta_usuario, Opciones, respuesta_pregunta, peso);
	}

	/**
	 * esta funcion se encarga de consultarle al alumno.
	 * 
	 * @return retorna true si el alumno respondio bien, false si respondio mal.
	 * @throws NumberFormatException si el usuario ingresa letras en vez de numeros.
	 */
	public boolean buscar() {
		
		
		//estas condiciones sirven para que, bajo ninguna circunstancia, se puedan responder preguntas invalidas.
		boolean verificarPregunta = ((getPregunta() == null) || getPregunta().isBlank() || getPregunta().isEmpty());
		boolean verificarRespuesta = (getRespuesta() < 0 || getRespuesta() > getOpciones().length);
		boolean verificarOpciones = ((getOpciones() == null) || getOpciones().length < 2);

		//este if es para evitar mostrarle al alumno preguntas invalidas (sin preguntas, sin opciones, sin respuestas o con respuesta fuera de rango)
		
		if (!verificarPregunta && !verificarRespuesta && !verificarOpciones) {
			int respuesta_usuario = 0;
			boolean continuar = true;

			do {
				try {
					System.out.println("\n" + this.numero_pregunta + ") " + getPregunta() + "\n");
					
					/*for (int k = 0; k < opciones.length; k++) {
						System.out.print(k + 1 + ") " + opciones[k] + "\t");
					}*/

					for (int k = 0; k < this.opciones.length ; k++) {
						System.out.println("\t" + ((char)(97 + k)) + ") " + this.opciones[k]); //97 es la 'a' minuscula
					}
					
					System.out.print("\nsu respuesta: ");
					respuesta_usuario = ((int)Texto.leer_teclado().toLowerCase().charAt(0))-96;
					if ((respuesta_usuario > 0) && (respuesta_usuario <= opciones.length)) {
						continuar = false;
					} else {
						System.err.println("\n\npor favor ingrese una letra valida (entre a y " + ((char)((opciones.length)+96)) + "). intentos restantes: " + cantidad_intentos + "\n\n");
						cantidad_intentos--;
					}

				} catch (Exception e) {
					System.err.println("\n\npor favor ingresa LETRAS, otros caracteres no sirven. intentos restantes: " + cantidad_intentos + "\n\n");
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
					System.out.println("\n\nCorrecto!, la respuesta era la letra " + respuesta_usuario + " (" + opciones[respuesta] + ")!!!\n\n\n");
				}
				return true;
			} else {
				if (mostrar_respuestas == true) {
					System.out.println("\n\nIncorrecto :/, la respuesta era la letra " + respuesta_usuario + " (" + opciones[respuesta] + ")\n\n");
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
	public void mostrar() {
		System.out.println(") " + getPregunta() + "\n");
		for (int k = 0; k < this.opciones.length ; k++) {
			System.out.println("\t" + ((char)(97 + k)) + ") " + this.opciones[k]); //97 es la 'a' minuscula
		}
		System.out.println("\n\n");
	}

	/**
	 * esta funcion se encarga de setear la respuesta del alumno.
	 * @param respuesta es la respuesta del alumno.
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
	 * esta funcion se encarga de obtener la respuesta correcta.
	 * 
	 * @return retorna la respuesta correcta.
	 */
	public int getRespuesta() {
		return this.respuesta;
	}
	
	/**
	 * esta funcion se encarga de obtener las opciones de la pregunta.
	 * 
	 * @return retorna el arreglo de opciones.
	 */
	public String[] getOpciones() {
		return this.opciones;
	}
}
