package base;

/**
 * Esta es una clase abstracta tiene la base de la mayoria de las preguntas.
 * 
 * @author Sebastian Morgado
 * @version 2.0.0
 * @since 0.0.5
 */
abstract class Pregunta {

	/**
	 * Esta variable privada tiene el peso almacenado.
	 */
	private int Peso = 1;

	/**
	 * Esta variable privada tiene la pregunta almacenada.
	 */
	private String Pregunta;

	/**
	 * este metodo constructor se encarga de almacenar las preguntas y los pesos
	 * 
	 * @param preguntaIngresada es la pregunta la cual se desea almacenar.
	 * @param peso              es el puntaje que vale la pregunta.
	 */
	Pregunta(String preguntaIngresada, int peso) {
		if (preguntaIngresada != null && !preguntaIngresada.isBlank()) {
			this.Pregunta = preguntaIngresada;
		} else {
			this.Pregunta = "No se ha ingresado una pregunta, lamento las molestias.";
		}
		setPeso(peso);
	}

	/**
	 * este metodo abstracto se encarga de buscar la pregunta, luego imprimir la
	 * pregunta, y despues obtener la respuesta del usuario.
	 * 
	 * @return retorna true si el alumno respondio bien, false si respondio mal.
	 */
	@Deprecated
	public abstract boolean buscar();

	/**
	 * Este metodo abstracto se encarga de mostrar las preguntas, y si es que tiene
	 * las opciones.
	 */
	@Deprecated
	public abstract void mostrar();
	
	/**
	 * Esta variable tiene la cantidad de intentos que tiene el alumno.
	 */
	@Deprecated
	static int cantidad_intentos = 7;
	
	
	/**
	 * este metodo es el getter del peso.
	 * 
	 * @return retorna el peso que tiene la pregunta.
	 */
	public int getPeso() {
		return this.Peso;
	}

	/**
	 * este metodo es el getter de las preguntas.
	 * 
	 * @return retorna la pregunta que tiene.
	 */
	public String getPregunta() {
		return this.Pregunta;
	}

	/**
	 * este metodo es el setter del peso.
	 * 
	 * @param peso es el peso que se desea settear.
	 */
	public void setPeso(int peso) {
		this.Peso = peso;
	}
}
