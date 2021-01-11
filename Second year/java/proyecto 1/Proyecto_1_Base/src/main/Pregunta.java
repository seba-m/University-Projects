package main;

import java.io.*; 

/**
 * Una clase abstracta que se encarga de muchas cosas, entre las cuales estan
 * la de almacenar preguntas, su peso, entre otros.
 * 
 * @author Sebastian Morgado
 * @version 1.0.0
 */
abstract class Pregunta {
	
	/**
	 * Esta variable privada tiene el peso almacenado.
	 */
	private int Peso;
	/**
	 * Esta variable privada tiene la pregunta almacenada.
	 */
	private String Pregunta;
	
	/**
	 * Esta variable tiene la cantidad de intentos que tiene el alumno.
	 */
	static int intentos = 7;
	
	/**
	 * Esta funcion se encarga de leer del teclado. todo esto con el fin de ahorrar
	 * codigo innecesario en las funciones.
	 * 
	 * @return retorna el texto que ha sido escrito por el usuario.
	 */
	static String leer_teclado() {
		boolean continuar = true;
		String texto = null;
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		do { 
			try {
				texto = reader.readLine();
				if ((texto != null) && !texto.isEmpty()) {
					continuar = false;
				} else if (texto == null) {
					System.err.println("\ningresado comando para salir del programa... saliendo...\n");
					System.exit(0);
				} else {
					System.err.println("\ntexto invalido, intentelo de nuevo.\n");
				}
				
			} catch (IOException e) {
				System.err.println("\n\nsi ves este error, significa que intentaste\nescribir algo que no debias, o fue un fallo culpa de java, \npor favor intentalo de nuevo.\n\n");
			}
		} while (continuar); // este while es innecesario, pero esta por si es que se llegase a dar el caso de que el reader falle.
		return texto;
	}
	
	/**
	 * este metodo abstracto se encarga de buscar la pregunta, luego imprimir la
	 * pregunta, y despues obtener la respuesta del usuario. 
	 * @return retornara true si es que el usuario ha respondido bien, y retornara false 
	 * 		   si pasa lo contrario.
	 */
	abstract boolean buscar();

	/**
	 * este metodo constructor se encarga de almacenar las preguntas y los pesos
	 * 
	 * @param Pregunta es la pregunta la cual se desea almacenar.
	 * @param peso es el peso (o cuanto puntaje vale) de la pregunta.
	 */
	Pregunta(String Pregunta, int peso) {
		this.Pregunta = Pregunta;
		this.Peso = peso;
	}

	/**
	 * este metodo es el getter del peso.
	 * @return retorna el peso que tiene la pregunta.
	 */
    int getPeso() {
        return this.Peso;
    }

	/**
	 * este metodo es el getter de las preguntas.
	 * @return retorna la pregunta que tiene.
	 */
    public String getPregunta() {
        return this.Pregunta;
    }

    
}
