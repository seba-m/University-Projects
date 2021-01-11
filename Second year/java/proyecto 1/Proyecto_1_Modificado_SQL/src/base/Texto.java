package base;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Base64;

/**
 * Esta clase tiene por objetivo poder facilitar la lectura de las preguntas
 * creadas por el profesor.
 * 
 * @author Sebastian Morgado
 * @version 1.6.1
 * @since 1.2.0
 */
class Texto {

	/**
	 * Esta funcion se encarga de insertar texto en el string cada (x &gt; 1)
	 * cantidad de palabras.
	 * 
	 * @param texto            este es el string en el cual usted desea agregar el
	 *                         texto de texto_a_insertar.
	 * @param texto_a_insertar como dice el nombre de la variable, es el texto que
	 *                         se insertara en el arreglo.
	 * @param cada_cuanto      es la cantidad de palabras que tienen que pasar para
	 *                         que se inserte el salto de linea.
	 * @return retorna el string modificado con los saltos de lineas. en el caso que
	 *         se den situaciones especiales, retornara null.
	 */
	static String InsertarTextoEnString(String texto, String texto_a_insertar, int cada_cuanto) {

		if (texto == null || texto_a_insertar == null || cada_cuanto < 1) {
			return null;
		} else if (texto.length() < 2) {
			return texto;
		}

		String[] arr = texto.split(" ");
		int contador = 0;

		for (int i = 1; i < arr.length; i++) {

			if (cada_cuanto == contador) {
				arr = InsertarPosX(arr, texto_a_insertar, i);
				contador = 0;
			}
			contador++;
		}

		return String.join(" ", arr);
	}

	/**
	 * Esta funcion se encarga de crear un nuevo array de strings, donde se guardara
	 * el nuevo array de strings modificado.
	 * 
	 * @param array    en este array de strings esta almacenada todas las palabras.
	 * @param texto    es el texto que se agregara en el array de strings.
	 * @param posicion es la posicion en la cual se desea insertar el texto.
	 * @return retorna el array de strings modificado.
	 */
	private static String[] InsertarPosX(String array[], String texto, int posicion) {
		String ArrayModificado[] = new String[array.length + 1];

		for (int i = 0; i < array.length + 1; i++) {
			if (i < posicion - 1)
				ArrayModificado[i] = array[i];
			else if (i == posicion - 1)
				ArrayModificado[i] = texto;
			else
				ArrayModificado[i] = array[i - 1];
		}
		return ArrayModificado;
	}

	/**
	 * Esta funcion se encarga de borrar la extension del nombre de un archivo.
	 * 
	 * @param s es el nombre del archivo con la extension.
	 * @return retorna el nombre del archivo sin su extension (por ej, sin ".txt")
	 */
	static String Borrar_Extension(String s) {
		String separador = System.getProperty("file.separator");
		String Nombre_Archivo = null;

		int ultima_pos_separador = s.lastIndexOf(separador);
		if (ultima_pos_separador == -1) {
			Nombre_Archivo = s;
		} else {
			Nombre_Archivo = s.substring(ultima_pos_separador + 1);
		}

		int posicion_Extension = Nombre_Archivo.lastIndexOf(".");
		if (posicion_Extension == -1) {
			return Nombre_Archivo;
		}

		return Nombre_Archivo.substring(0, posicion_Extension);
	}

	/**
	 * Esta funcion se encarga de proteger el texto usando base64.
	 * 
	 * @param texto es el texto que se desea proteger.
	 * @return retorna el string codificado en base64.
	 */
	static String ProtegerTexto(String texto) {
		return Base64.getEncoder().encodeToString(texto.getBytes());
	}

	/**
	 * Esta funcion se encarga de proteger el texto usando base64.
	 * 
	 * @param texto es el texto que se desea proteger.
	 * @return retorna el string codificado en base64.
	 */
	static String ProtegerTexto(int texto) {
		return Base64.getEncoder().encodeToString(String.valueOf(texto).getBytes());
	}

	/**
	 * Esta funcion se encarga de proteger el texto usando base64.
	 * 
	 * @param texto es el texto que se desea proteger.
	 * @return retorna el string codificado en base64.
	 */
	static String ProtegerTexto(boolean texto) {
		return Base64.getEncoder().encodeToString(String.valueOf(texto).getBytes());
	}

	/**
	 * Esta funcion se encarga de proteger el texto usando base64.
	 * 
	 * @param textoEnBytes es un arreglo de bytes, que contiene el texto a proteger.
	 * @return retorna el string codificado en base64.
	 */
	static String ProtegerTexto(byte[] textoEnBytes) {
		return Base64.getEncoder().encodeToString(textoEnBytes);
	}

	/**
	 * Esta funcion se encarga de desproteger el texto usando base64.
	 * 
	 * @param texto es el texto que se desea proteger.
	 * @return retorna el string codificado en base64.
	 */
	static String DesprotegerTexto(String texto) {
		return new String(Base64.getDecoder().decode(texto));
	}

	/**
	 * Esta funcion se encarga de verificar si el texto usa base64 y no esta vacio.
	 * 
	 * @param texto es el texto que se desea verificar.
	 * @return retorna {@code true} si el texto esta codificado en base64, retornara
	 *         {@code false} si no lo esta.
	 */
	static boolean esValido(String texto) {
		return (texto).matches("^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)?$");
	}

	/**
	 * Esta funcion se encarga de verificar si el texto dado tiene un numero.
	 * 
	 * @param texto es el texto que se desea analizar.
	 * @return retorna {@code true} si el texto tiene un numero, retorna
	 *         {@code false} si no tiene numeros.
	 */
	static boolean TieneNumeros(String texto) {

		if (texto != null) {
			char[] letras = texto.toCharArray();

			for (char c : letras) {
				if (Character.isDigit(c)) {
					return true;
				}
			}
		}
		return false;
	}

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
				System.err.println(
						"\n\nsi ves este error, significa que intentaste\nescribir algo que no debias, o fue un fallo culpa de java, \npor favor intentalo de nuevo.\n\n");
			}
		} while (continuar); // este while es innecesario, pero esta por si es que se llegase a dar el caso
								// de que el reader falle.
		return texto;
	}
}
