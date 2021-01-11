package main;

/**
 * Esta es una clase que se encarga de hacer todo lo relacionado con
 * Selec_Mul_Pregunta, como por ejemplo: crear la pregunta de tipo
 * Selec_Mul_Pregunta usando constructores, como tambien preguntar, comparar y
 * retornar true/false si el usuario ha respondido correctamente.
 * 
 * @author Sebastian Morgado
 * @version 1.0.0
 */
class Selec_Mul_Pregunta extends Pregunta {

	/**
	 * Esta variable privada tiene las opciones almacenadas.
	 */
	private String[] Opciones;
	/**
	 * Esta variable privada tiene la posicion de la respuesta correcta.
	 */
	private int index;
	
	/**
	 * este es el constructor que toma la pregunta, las opciones, la respuesta y el peso, y la convierte a tipo seleccionmultiple
	 * 
	 * @param pregunta es la pregunta que se desea guardar en verdadero falso.
	 * @param Opciones es el array de strings en donde se guardan todas las opciones.
	 * @param index es la opcion correcta. al ser el "index", solo acepta tipos enteros, y parte de cero.
	 * @param peso es el peso de la pregunta (o tambien cuanto vale esta), solo acepta valores de tipo int.
	 * */
	Selec_Mul_Pregunta(String pregunta, String[] Opciones, int index, int peso) {
		super(pregunta, peso);
		this.Opciones = Opciones;
		this.index = index;
	}

	
	/**
	 * esta funcion se encarga de consultarle al alumno.
	 * @return retorna true si el alumno respondio bien, false si respondio mal.
	 * @throws NumberFormatException si el usuario ingresa letras en vez de numeros.
	 */
	public boolean buscar() {
		int respuesta_usuario = 0;

		while (true) {
			System.out.println("\n - " + getPregunta() + "\n");
			
			/*for (int k = 0; k < opciones.length; k++) {
				System.out.print(k + 1 + ") " + opciones[k] + "\t");
			}*/

			for (int k = 0; k < this.Opciones.length ; k++) {
				System.out.println("\t" + ((char)(97 + k)) + ") " + this.Opciones[k]); //97 es la 'a' minuscula
			}
			
			System.out.print("\nsu respuesta: ");
			try {
				respuesta_usuario = ((int)leer_teclado().toLowerCase().charAt(0))-96;
				if ((respuesta_usuario > 0) && (respuesta_usuario <= Opciones.length)) {
					break;
				} else {
					System.err.println("\n\npor favor ingrese una letra valida (entre a y " + ((char)((Opciones.length)+96)) + "). intentos restantes: " + intentos + "\n\n");
					intentos--;
					if (intentos < 0) {
						System.err.println("\n\nse acabaron los intentos. vaya a estudiar e intentelo nuevamente.\n\n");
						System.exit(0);
					}
				}

			} catch (Exception e) {
				System.err.println("\n\npor favor ingresa LETRAS, otros caracteres no sirven. intentos restantes: " + intentos + "\n\n");
				intentos--;
				if (intentos < 0) {
					System.err.println("\n\nse acabaron los intentos. vaya a estudiar e intentelo nuevamente.\n\n");
					System.exit(0);
				}
			}
		}
		
		if (respuesta_usuario == index + 1) {
				System.out.println("\n\nCorrecto!, la respuesta era la letra " + ((char)(index+97)) + "!!!\n\n\n");
			return true;
		} else {
				System.out.println("\n\nIncorrecto :/, la respuesta era la letra " + ((char)(index+97)) + "\n\n");
			return false;
		}
	}
}
