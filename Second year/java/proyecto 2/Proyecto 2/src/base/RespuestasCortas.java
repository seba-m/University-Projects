package base;

/**
 * Esta es una clase que se encarga de hacer todo lo relacionado con
 * RespuestasCortas, como por ejemplo: crear la pregunta de tipo
 * RespuestasCortas usando constructores, como tambien preguntar, comparar y
 * retornar true/false si el usuario ha respondido correctamente.
 * 
 * @author Sebastian Morgado
 * @version 2.0.0
 * @since 0.0.5
 */
public class RespuestasCortas extends Pregunta {

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
	@Deprecated
	private boolean mostrar_respuestas;

	/**
	 * la variable numero_pregunta sirve para saber el numero de la pregunta.
	 */
	@Deprecated
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
	public RespuestasCortas(String pregunta, String respuesta, int peso) {
		super(pregunta, peso);
		this.respuesta = respuesta;
	}

	/**
	 * esta funcion se encarga de obtener la respuesta correcta.
	 * 
	 * @return retorna la respuesta.
	 */
	public String getRespuesta() {
		return respuesta;
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
	 * 
	 */
	public void setRespuestaAlumno(String respuesta) {
		this.respuestas_usuario = respuesta;
	}
	
	/**
	 * esta funcion se encarga de consultarle al alumno.
	 * 
	 * @return retorna true si el alumno respondio bien, false si respondio mal.
	 */
	@Deprecated
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
	@Deprecated
	public void mostrar() {
		System.out.println("- " + getPregunta() + "\n");
	}
	
}