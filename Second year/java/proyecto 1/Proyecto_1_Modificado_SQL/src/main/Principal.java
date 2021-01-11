package main;

import base.ServidorSql;

/**
 * Esta es una clase principal que dirige todo el proyecto :)
 * 
 * @author Sebastian Morgado
 * @version 1.6.1
 * @since 1.0.0
 */

public class Principal { // NO_UCD (unused code)
	/**
	 * Este es el main. dado a como diseñe el codigo, solo es necesario llamar a una
	 * funcion ({@link base.ServidorSql#inicio_sesion inicio sesion}).
	 * 
	 * @param args son los argumentos que se le pasan al main.
	 */
	public static void main(String[] args) {
		ServidorSql.inicio_sesion();
	}
}
