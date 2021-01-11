package main;

/**
 * Esta es una clase que se encarga de hacer todo lo relacionado con
 * Resp_Cortas_Pregunta, como por ejemplo: crear la pregunta de tipo
 * Resp_Cortas_Pregunta usando constructores, como tambien preguntar, comparar y
 * retornar true/false si el usuario ha respondido correctamente.
 * 
 * @author Sebastian Morgado
 * @version 1.0.0
 */
class Resp_Cortas_Pregunta extends Pregunta {

	/**
	 * Esta variable privada tiene la respuesta almacenada.
	 */
	private String respuesta;

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
	Resp_Cortas_Pregunta(String pregunta, String respuesta, int peso) {
		super(pregunta, peso);
		this.respuesta = respuesta;
	}

	/**
	 * esta funcion se encarga de consultarle al alumno.
	 * 
	 * @return retorna true si el alumno respondio bien, o false si respondio mal.
	 */
	public boolean buscar() {
		System.out.println("\n" + getPregunta() + "\n");
		System.out.print("\n\nsu respuesta:");

		if (leer_teclado().equalsIgnoreCase(this.respuesta) == true) {
			System.out.println("\n\nCorrecto!, la respuesta era " + this.respuesta + "!!!\n\n\n");
			return true;
		} else {
			System.out.println("\n\nIncorrecto :/, la respuesta era " + this.respuesta + "\n\n");
			return false;
		}
	}

}
