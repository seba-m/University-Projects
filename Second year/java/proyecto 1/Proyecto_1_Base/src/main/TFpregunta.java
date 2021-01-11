package main;

/**
 * Esta es una clase que se encarga de hacer todo lo relacionado con
 * TFpregunta, como por ejemplo: crear la pregunta de tipo
 * TFpregunta usando constructores, como tambien preguntar, comparar y
 * retornar true/false si el usuario ha respondido correctamente.
 * 
 * @author Sebastian Morgado
 * @version 1.0.0
 */
class TFpregunta extends Pregunta {

	/**
	 * Esta variable privada tiene la respuesta almacenada.
	 */
	private boolean respuesta;

	/**
	 * este es el constructor que toma la pregunta, la respuesta y el peso, y la
	 * convierte a tipo verdaderofalso.
	 * 
	 * @param Pregunta  es la pregunta que se desea guardar en verdadero falso.
	 * @param respuesta esta puede ser solo tipo booleano ({@code true o false})
	 * @param peso      es el peso de la pregunta (o tambien cuanto vale esta), solo
	 *                  acepta valores de tipo int.
	 */
	TFpregunta(String Pregunta, boolean respuesta, int peso) {
		super(Pregunta, peso);
		this.respuesta = respuesta;
	}

	/**
	 * esta funcion se encarga de consultarle al alumno.
	 * 
	 * @return retorna true si el alumno respondio bien, false si respondio mal.
	 */
	public boolean buscar() {

		String Respuesta_usuario = null;
		boolean respuesta_final = false;

		while (true) {
			System.out.println("\n" + getPregunta() + "\n\n\nsu respuesta:");
			Respuesta_usuario = leer_teclado();

			if (Respuesta_usuario.substring(0).equalsIgnoreCase("t") || Respuesta_usuario.substring(0).equalsIgnoreCase("v")) {
				respuesta_final = true;
				break;
			} else if (Respuesta_usuario.substring(0).equalsIgnoreCase("f")) {
				respuesta_final = false;
				break;
			} else {
				System.out.println("\n\nrespuesta no valida, por favor intentelo nuevamente (intente usar v o f), intentos restantes: " + intentos + "\n\n");
				intentos--;
				if (intentos < 0) {
					System.out.println("\n\nse acabaron los intentos. vaya a estudiar e intentelo nuevamente.\n\n");
					System.exit(0);
				}
			}
		}

		if (respuesta_final == this.respuesta) {
			System.out.println("\n\nCorrecto!, la respuesta era " + this.respuesta + "!!!\n\n\n");
			return true;
		} else {
			System.out.println("\n\nIncorrecto :/, la respuesta era " + this.respuesta + "\n\n");
			return false;
		}
	}

}
