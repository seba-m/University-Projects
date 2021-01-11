package main;

import base.*;

/**
 * Esta es una clase principal que dirige todo el proyecto :)
 * 
 * @author Sebastian Morgado
 * @version 1.5.1
 * @since 1.0.0
 */

public class Principal { // NO_UCD (unused code)
	/**
	 * este es el menu en donde se separaran las acciones que el usuario podra
	 * tomar. puede elegir entre profesor o alumno, las cuales cada una tendra
	 * acciones diferentes.
	 * 
	 * @param args son los argumentos que se le pasan al main.
	 */
	public static void main(String[] args) {
		boolean continuar = true;
		
		do {
			String respuesta = null;
			
			do {
				System.out.print("\n¡Hola!, Bienvenid@ a mi programa, soy Sebastian Morgado, por favor necesito que me indiques que eres (profesor/maestro o alumno/estudiante):\n");
				respuesta = Texto.leer_teclado();
				
				if (respuesta.substring(0,1).equalsIgnoreCase("p") || respuesta.substring(0,1).equalsIgnoreCase("m")) {
					
					Profesor.menu();
					continuar = false;
					
				} else if ((respuesta.substring(0,1).equalsIgnoreCase("e") || respuesta.substring(0,1).equalsIgnoreCase("a"))) {
					
					System.out.print("\nAntes de continuar, necesito que me digas tu nombre: ");
					respuesta = Texto.leer_teclado();
					
					if (!Texto.TieneNumeros(respuesta)) {
						Alumno.menu(respuesta);
						continuar = false;
						
					} else {
						System.err.println("\n\npor favor no ingrese numeros en su nombre.\n");
					}
					
				} else {
					System.err.println("\nIntentelo de nuevo!, opcion incorrecta... (intenta con profesor/maestro o estudiante/alumno)\n");
				}
			} while (continuar);
			
			System.out.print("\n\ndesea realizar otra accion? 1 = si, 2 = no.\n\nsu respuesta: ");
			respuesta = Texto.leer_teclado();
			
			continuar = true;
			
			if (respuesta.equalsIgnoreCase("2")) {
				System.out.println("\nprograma finalizado.");
				continuar = false;
			}
			
		} while (continuar);
	}
}
