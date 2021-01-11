package base;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.regex.Pattern;

/**
 * Esta clase tiene por objetivo poder facilitar la lectura de las preguntas
 * creadas por el profesor.
 * 
 * @author Sebastian Morgado
 * @version 2.0.0
 * @since 1.2.0
 */
public class Texto {

	/**
	 * Esta funcion se encarga de borrar la extension del nombre de un archivo.
	 * 
	 * @param s es el nombre del archivo con la extension.
	 * @return retorna el nombre del archivo sin su extension (por ej, sin ".txt")
	 */
	public static String borrarExtension(String s) {
		String nombreArchivo = null;

		int ultimaPosicionSeparador = s.lastIndexOf(System.getProperty("file.separator"));
		if (ultimaPosicionSeparador == -1) {
			nombreArchivo = s;
		} else {
			nombreArchivo = s.substring(ultimaPosicionSeparador + 1);
		}

		int posicionExtension = nombreArchivo.lastIndexOf(".");
		if (posicionExtension == -1) {
			return nombreArchivo;
		}

		return nombreArchivo.substring(0, posicionExtension);
	}

	/**
	 * Este metodo se encarga de verificar si la respuesta del usuario coincide con
	 * la respuesta del examen. Solo debe ser llamado por la función similitud, caso
	 * contrario no sirve.
	 * 
	 * @param s1 es la respuesta mas larga.
	 * @param s2 es la respuesta mas corta.
	 * 
	 * @return retorna la distancia de edit entre las 2 palabras.
	 * 
	 * @see <a href = "https://stackoverflow.com/a/36566052"> Distancia de
	 *      Levenshtein</a>
	 */
	private static double distanciaDeEditYLevenshtein(String s1, String s2) {
		String textoLargo = s1.toLowerCase();
		String textoCorto = s2.toLowerCase();

		int[] coste = new int[textoLargo.length() + 1];

		for (int i = 0; i <= textoLargo.length(); i++) {

			int ultimoValor = i;

			for (int j = 0; j <= textoCorto.length(); j++) {

				if (i == 0) {

					coste[j] = j;

				} else {

					if (j > 0) {
						int nuevoValor = coste[j - 1];

						if (textoLargo.charAt(i - 1) != textoCorto.charAt(j - 1)) {
							nuevoValor = Math.min(Math.min(nuevoValor, ultimoValor), coste[j]) + 1;
						}

						coste[j - 1] = ultimoValor;
						ultimoValor = nuevoValor;
					}
				}
			}
			if (i > 0) {
				coste[textoCorto.length()] = ultimoValor;
			}
		}

		return coste[textoCorto.length()];
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
	 * Esta funcion se encarga de insertar texto en el string cada (x &gt; 1)
	 * cantidad de palabras.
	 * 
	 * @param texto          este es el string en el cual usted desea agregar el
	 *                       texto de textoAInsertar.
	 * @param textoAInsertar como dice el nombre de la variable, es el texto que se
	 *                       insertara en el arreglo.
	 * @param cadaCuanto     es la cantidad de palabras que tienen que pasar para
	 *                       que se inserte el salto de linea.
	 * @return retorna el string modificado con los saltos de lineas. en el caso que
	 *         se den situaciones especiales, retornara null.
	 */
	static String InsertarTextoEnString(String texto, String textoAInsertar, int cadaCuanto) {

		if (texto == null || textoAInsertar == null || cadaCuanto < 1) {
			return null;
		} else if (texto.length() < 2) {
			return texto;
		}

		String[] arr = texto.split(" ");
		int contador = 0;

		for (int i = 1; i < arr.length; i++) {

			if (cadaCuanto == contador) {
				arr = InsertarPosX(arr, textoAInsertar, i);
				contador = 0;
			}
			contador++;
		}

		return String.join(" ", arr);
	}

	/**
	 * Este metodo se encarga de verificar si la respuesta del usuario coincide con
	 * la respuesta del examen.
	 * 
	 * @param respuestaCorrecta es la respuesta correcta del examen.
	 * @param respuestaAlumno   es la respuesta del alumno.
	 * 
	 * @return retorna el porcentaje (%) de similitud entre las respuestas a
	 *         comparar. siendo 0% ninguna coincidencia, y 100% palabras iguales.
	 * 
	 * @see <a href=
	 *      "https://docs.informatica.com/es_es/data-quality-and-governance/data-quality/10-2-hotfix-2/developer-transformation-guide/transformacion-de-comparacion/estrategias-para-la-coincidencia-de-campos/distancia-de-hamming.html">Fórmula
	 *      de hamming</a>
	 * 
	 */
	public static int metricaHamming(String respuestaCorrecta, String respuestaAlumno) { // NO_UCD (unused code)
		if (respuestaAlumno == null || respuestaCorrecta == null) {
			throw new IllegalArgumentException("Respuestas invalidas.");
		}

		// convierto los strings a un arreglo de chars, ademas borro los espacios.
		char[] s1 = respuestaCorrecta.toLowerCase().toCharArray();
		char[] s2 = respuestaAlumno.toLowerCase().toCharArray();

		// obtengo la longitud del string mas largo y del mas corto
		int distanciaCorta = Math.min(s1.length, s2.length);
		int distanciaLarga = Math.max(s1.length, s2.length);

		// busco los caracteres similares
		float caracteresSimilares = 0;
		for (int i = 0; i < distanciaCorta; i++) {
			// verifico caracter por caracter si es similar
			if (s1[i] == s2[i]) {
				caracteresSimilares++;
			}
		}
		return (int) Math.ceil((caracteresSimilares / distanciaLarga) * 100);
	}

	/**
	 * Este metodo se encarga de verificar si la respuesta del usuario coincide con
	 * la respuesta del examen.
	 * 
	 * @param respuestaCorrecta es la respuesta correcta del examen.
	 * @param respuestaAlumno   es la respuesta del alumno.
	 * 
	 * @return retorna el porcentaje (%) de similitud entre las respuestas a
	 *         comparar. siendo 0% ninguna coincidencia, y 100% palabras iguales.
	 * 
	 * @see <a href = "https://stackoverflow.com/a/36566052"> Distancia de
	 *      Levenshtein</a>
	 */
	public static int similitud(String respuestaCorrecta, String respuestaAlumno) {

		if (respuestaAlumno == null || respuestaCorrecta == null) {
			throw new IllegalArgumentException("Respuestas invalidas.");
		}

		String masLargo = respuestaCorrecta;
		String masCorto = respuestaAlumno;

		if (respuestaCorrecta.length() < respuestaAlumno.length()) {
			masLargo = respuestaAlumno;
			masCorto = respuestaCorrecta;
		}

		int longitudMasLarga = masLargo.length();

		if (longitudMasLarga == 0) {
			return 1;
		}

		return (int) Math.ceil(
				((longitudMasLarga - distanciaDeEditYLevenshtein(masLargo, masCorto)) / Float.valueOf(longitudMasLarga))
						* 100);
	}

	/**
	 * Esta funcion se encarga de verificar si el texto dado tiene un numero.
	 * 
	 * @param texto es el texto que se desea analizar.
	 * @return retorna {@code true} si el texto tiene un numero, retorna
	 *         {@code false} si no tiene numeros.
	 */
	public static boolean TieneNumeros(String texto) {

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
	 * Esta funcion se encarga de verificar si el texto dado tiene un numero.
	 * 
	 * @param texto es el texto que se desea analizar.
	 * @return retorna {@code true} si el texto tiene un numero, retorna
	 *         {@code false} si no tiene numeros.
	 */
	public static boolean TieneLetras(String texto) {

		if (texto != null) {
			char[] letras = texto.toCharArray();

			for (char c : letras) {
				if (Character.isAlphabetic(c)) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Esta funcion se encarga de validar el rut.
	 * 
	 * @param rut es el rut el cual se desea verificar si es valido o no.
	 * 
	 * @return retornara {@code true} si el rut es valido. retornara {@code false}
	 *         si no.
	 */
	public static boolean validarRut(String rut) {

		try {
			String rutAVerificar = rut.toUpperCase().replace(".", "").replace("-", "");
			int rutAux = Integer.parseInt(rutAVerificar.substring(0, rutAVerificar.length() - 1));

			char numeroGuion = rutAVerificar.charAt(rutAVerificar.length() - 1);

			int multiplicador = 0, suma = 1;
			while (rutAux != 0) {
				suma = (suma + rutAux % 10 * (9 - multiplicador++ % 6)) % 11;
				rutAux /= 10;
			}

			if (numeroGuion == (char) (suma != 0 ? suma + 47 : 75)) {
				return true;
			}
		} catch (Exception e) {
			// el rutAVerificar ingresado es invalido o si ingreso texto.
			return false;
		}
		return false;
	}

	/**
	 * Este metodo se encarga de verificar si el nombre del archivo es valido. o
	 * sea, que no tenga simbolos reservados de WINDOWS, como lo son: [ : * &lt; &gt;
	 * \ / | ? " ]
	 * 
	 * @param nombreArchivo es el nombre del archivo a analizar.
	 * 
	 * @return retorna un {@code true} si hay un simbolo especial en el nombre del
	 *         archivo, caso contrario retorna {@code false} si no hay ninguno.
	 * 
	 */
	public static boolean verificarNombreArchivo(String nombreArchivo) {
		/*-
		 [] = inicio y fin del regex.
		 :*<>\/|?" = caracteres a verificar.
		 + = para todo el string.
		 
		 Pattern.compile("[:*<>\\/|?\"]+") es una clase la cual compila el regex que se le da.
		 .matcher(String).find() estas funciones se encargan de verificar si el string encaja 
		 						 dentro de las caracteristicas del regex.
		 */
		return Pattern.compile("[:*<>\\/|?\"]+").matcher(nombreArchivo).find();
	}

	/**
	 * Esta funcion se encarga de leer del teclado. todo esto con el fin de ahorrar
	 * codigo innecesario en las funciones.
	 * 
	 * @return retorna el texto que ha sido escrito por el usuario.
	 */
	@Deprecated
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

			} catch (Exception e) {
				System.err.println(
						"\n\nsi ves este error, significa que intentaste\nescribir algo que no debias, o fue un fallo culpa de java, \npor favor intentalo de nuevo.\n\n");
			}
		} while (continuar); // este while es innecesario, pero esta por si es que se llegase a dar el caso
								// de que el reader falle.
		return texto;
	}
}
