package base;

/**
 * Esta es una clase que se encarga de hacer todo lo relacionado con
 * VerdaderoFalso, como por ejemplo: crear la pregunta de tipo VerdaderoFalso
 * usando constructores, como tambien preguntar, comparar y retornar true/false
 * si el usuario ha respondido correctamente.
 * 
 * @author Sebastian Morgado
 * @version 2.0.0
 * @since 0.0.5
 */

public class VerdaderoFalso extends Pregunta { // NO_UCD (use default)

	/**
	 * la variable respuesta almacena la respuesta correcta de la pregunta.
	 */
	private boolean respuesta;

	/**
	 * la variable respuestasUsuario almacena la respuesta del alumno.
	 */
	private String respuestasUsuario;

	/**
	 * la variable numero_pregunta sirve para saber el numero de la pregunta.
	 */
	@Deprecated
	private int numero_pregunta;
	
	/**
	 * la variable mostrar_respuestas sirve para mostrar las respuestas 
	 * si el alumno se equivoca.
	 */
	@Deprecated
	private boolean mostrar_respuestas;
	
	/**
	 * este es el constructor que toma la pregunta, la respuesta y el peso, y la
	 * convierte a tipo verdaderofalso.
	 * 
	 * @param pregunta  es la pregunta que se desea guardar en verdadero falso.
	 * @param respuesta esta puede ser solo tipo booleano ({@code true o false})
	 * @param peso      es el peso de la pregunta (o tambien cuanto vale esta), solo
	 *                  acepta valores de tipo int.
	 */
	public VerdaderoFalso(String pregunta, boolean respuesta, int peso) {
		super(pregunta, peso);
		this.respuesta = respuesta;
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

	/**
	 * esta funcion se encarga de obtener la respuesta del alumno.
	 * 
	 * @return retorna la respuesta.
	 */
	public String getRespuestaAlumno() {
		return this.respuestasUsuario;
	}

	/*
	 * esta funcion se encarga de setear la respuesta del alumno.
	 * 
	 * @param respuesta es la respuesta del alumno.
	 * 
	 */
	public void setRespuestaAlumno(String respuesta) {
		this.respuestasUsuario = respuesta;
	}
	
	/**
	 * esta funcion se encarga de consultarle al alumno.
	 * 
	 * @return retorna true si el alumno respondio bien, false si respondio mal.
	 */
	@Deprecated
	public boolean buscar() {
		String Respuesta_usuario = null;
		boolean Respuesta_usuario_boolean = false;
		
		boolean continuar = true;
		
		do {
			System.out.print("\n" + this.numero_pregunta + ") " + getPregunta() + "\n\nsu respuesta: ");
			Respuesta_usuario = Texto.leer_teclado();

			if (Respuesta_usuario.substring(0,1).equalsIgnoreCase("t") || Respuesta_usuario.substring(0,1).equalsIgnoreCase("v")) {
				Respuesta_usuario_boolean = true;
				respuestasUsuario = (String.valueOf(Respuesta_usuario_boolean));
				continuar = false;
			} else if (Respuesta_usuario.substring(0,1).equalsIgnoreCase("f")) {
				Respuesta_usuario_boolean = false;
				respuestasUsuario = (String.valueOf(Respuesta_usuario_boolean));
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
	 * esta funcion se encarga de imprimir las preguntas por pantalla en la consola.
	 */
	@Deprecated
	public void mostrar() {
		System.out.println("- " + getPregunta() + "\n");
	}

	
}
