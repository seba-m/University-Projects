package base;

/**
 * Esta es una clase abstracta tiene la base de la mayoria de las preguntas, por
 * ejemplo, tiene el metodo buscar y el de mostrar, los cuales las clases
 * {@link VerdaderoFalso}, {@link SeleccionMultiple} y {@link RespuestasCortas}
 * usan.
 * 
 * @author Sebastian Morgado
 * @version 1.6.1
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
	 * Esta variable tiene la cantidad de intentos que tiene el alumno.
	 */
	 static int cantidad_intentos = 7;

	/**
	 * este metodo constructor se encarga de almacenar las preguntas y los pesos
	 * 
	 * @param preguntaIngresada es la pregunta la cual se desea almacenar.
	 * @param peso     es el puntaje que vale la pregunta.
	 */
	Pregunta(String preguntaIngresada, int peso) {
		if (!(preguntaIngresada == null) && !preguntaIngresada.isBlank()) {
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
	public abstract boolean buscar();

	/**
	 * Este metodo abstracto se encarga de mostrar las preguntas, y si es que tiene
	 * las opciones.
	 */
	public abstract void mostrar();

	/**
	 * este metodo es el setter del peso.
	 * @param peso es el peso que se desea settear.
	 */
	public void setPeso(int peso) {
		this.Peso = peso;
	}
	
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
	 * Este setter se encarga de setear la cantidad de intentos.
	 * 
	 * @param cantidad es la cantidad de intentos que se desea setear. el valor
	 *                 tiene que estar entre (1 &lt;= cantidad &lt;= 10000), de no ser
	 *                 asi, se setea 7.
	 */
	static void setCantidadIntentos(int cantidad) {
		if (cantidad > 0 && cantidad < 10001) {
			cantidad_intentos = cantidad;
		} else {
			cantidad_intentos = 7;
		}
	}
}
