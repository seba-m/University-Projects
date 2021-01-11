package base;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

import org.javatuples.Triplet;

import utilidadesSeguridad.AES;
import utilidadesSeguridad.BASE64;
import utilidadesSeguridad.MD5;
import utilidadesSeguridad.RSA;

/**
 * Esta clase se encarga de hacer todo lo relacionado con los archivos (crear
 * archivos, escribir en los archivos e interpretar el archivo).
 * 
 * @author Sebastian Morgado
 * @version 2.0.0
 * @since 1.1.5
 */
public class Archivos {

	/**
	 * Esta funcion se encarga de ofuscar un archivo, y su resultado es un texto
	 * dificil de leer.
	 * 
	 * @param nombreArchivo         Es el nombre del archivo.
	 * @param rutaExamen            es la ubicacion en donde se guardará el archivo.
	 * @param texto                 es el texto que se escribirá en el archivo.
	 * @param contrasena            es la contraseña que se usará al proteger el
	 *                              archivo.
	 * @param contrasenaEstablecida si es {@code true}, y hay una contraseña valida
	 *                              (distinto de vacio y de null), protegerá el
	 *                              examen con una contraseña.
	 */
	public static void crearYCodificarArchivo(String rutaExamen, String nombreArchivo, String texto, String contrasena,
			boolean contrasenaEstablecida) {
		try {
			if (contrasenaEstablecida && contrasena != null && !contrasena.isBlank()) {
				String textoEncriptado = AES.encriptar(BASE64.ProtegerTextoBase64(texto), contrasena);
				Archivos.escribirEnArchivoDirectorio(rutaExamen + "\\examen generado\\", nombreArchivo, textoEncriptado
						+ "\n\nESTIMAD@ ALUMN@, POR FAVOR NO MODIFICAR/BORRAR ESTE ARCHIVO, YA QUE AQUÍ ESTÁ EL EXAMEN.\nESPERA A QUE EL PROFESOR TE DE LA LLAVE PARA PODER EMPEZAR A RESOLVER EL EXAMEN");
			} else {
				Archivos.escribirEnArchivoDirectorio(rutaExamen + "\\examen generado\\", nombreArchivo, BASE64
						.ProtegerTextoBase64(texto)
						+ "\n\nESTIMAD@ ALUMN@, POR FAVOR NO MODIFICAR/BORRAR ESTE ARCHIVO, YA QUE AQUÍ ESTÁ EL EXAMEN.\nESPERA A QUE EL PROFESOR TE DE LA LLAVE PARA PODER EMPEZAR A RESOLVER EL EXAMEN");
			}
		} catch (Exception e) {
			throw new IllegalArgumentException(
					"\n\nha ocurrido un error al codificar el archivo, intentelo nuevamente.\n");
		}
	}

	/**
	 * Esta funcion se encarga de deofuscar un archivo, para poder crear un arreglo
	 * de strings con todo el contenido del archivo. A diferencia de
	 * {@link #leerArchivoAlterado Leer Archivo Alterado}, esta función tiene de
	 * prioridad el de deofuscar el archivo de preguntas del profesor, mientras que
	 * el otro tiene de prioridad deofuscar las respuestas del alumno.
	 * 
	 * @param archivo    Es el archivo ya abierto.
	 * @param contrasena es la contraseña del examen, si es que la tiene.
	 * @return retorna un arreglo de strings a partir del contenido del archivo
	 * 
	 */
	private static String[] decodificarArchivo(File archivo, String contrasena) {
		try {
			Scanner leerArchivo = new Scanner(archivo);
			String textoArchivoCodificado = leerArchivo.nextLine();
			String texto = null;

			if (BASE64.esBase64(textoArchivoCodificado) && (contrasena == null || contrasena.isBlank())) {
				try {
					texto = BASE64.DesprotegerTextoBase64(textoArchivoCodificado);
				} catch (Exception e) {
					leerArchivo.close();
					throw new IllegalArgumentException(
							"<html><p align = \"center\">ERROR FATAL: El archivo ha sido adulterado o puede que tenga contraseña.<br>por favor contacte a su profesor, para que le envie la prueba de nuevo o le de la contraseña del examen.</p></html>");
				}
			} else {
				try {
					texto = AES.desencriptar(textoArchivoCodificado, contrasena);
					texto = BASE64.DesprotegerTextoBase64(texto);
				} catch (Exception e) {
					leerArchivo.close();
					throw new IllegalArgumentException("<html><p align = \"center\">" + e.getMessage() + "</p></html>");
				}

			}

			String[] arr = texto.split("\n");
			leerArchivo.close();
			for (String lineas : arr) {
				if (!Pattern.compile("[+|^|$|\\-|%|#|@|~|&|!]+").matcher(lineas).find()) {
					throw new IllegalArgumentException(
							"<html><p align = \"center\">ERROR FATAL: El archivo ha sido adulterado o puede que tenga contraseña.<br>por favor contacte a su profesor, para que le envie la prueba de nuevo o le de la contraseña del examen.</p></html>");
				}
			}
			return arr;
		} catch (Exception e) {
			throw new IllegalArgumentException(
					"<html><p align = \"center\">ERROR FATAL: El archivo ha sido adulterado o puede que tenga contraseña.<br>por favor contacte a su profesor, para que le envie la prueba de nuevo o le de la contraseña del examen.</p></html>");
		}
	}

	/**
	 * Esta función se encarga de crear y escribir en los archivos.
	 * 
	 * @param nombreArchivo es el nombre del archivo, ademas debe incluir su
	 *                      extension.
	 * @param texto         es el texto a escribir en el archivo.
	 * 
	 * @param i             permite crear nuevos archivos sin tener que sobre
	 *                      escribirlo (por ejemplo: si hay un archivo que se llame
	 *                      Hola.txt, creará un archivo llamado Hola(0).txt).
	 * 
	 */
	public static void crearYEscribirEnArchivo(String nombreArchivo, String texto, int i) {
		if (nombreArchivo == null || nombreArchivo.isEmpty() || texto == null || texto.isEmpty()) {
			throw new IllegalArgumentException("Ha ocurrido un error al crear el archivo.");
		}
		try {
			File archivo = new File(nombreArchivo);
			if (!archivo.createNewFile()) { // si el archivo ya existe
				nombreArchivo = archivo.toString();
				int indexPunto = nombreArchivo.lastIndexOf('.');
				if (indexPunto > 0) {
					String extension = nombreArchivo.substring(indexPunto + 1);
					if (Pattern.compile("[()]").matcher(nombreArchivo).find()) {
						int indexParentesis = nombreArchivo.lastIndexOf('(');
						crearYEscribirEnArchivo(
								nombreArchivo.substring(0, indexParentesis) + "(" + (i) + ")." + extension, texto, ++i);
					} else {
						crearYEscribirEnArchivo(nombreArchivo.substring(0, indexPunto) + "(" + (i) + ")." + extension,
								texto, ++i);
					}
				}
			} else {
				FileWriter fwArchivo = new FileWriter(nombreArchivo, true);
				fwArchivo.write(texto);
				fwArchivo.close();
			}
		} catch (IOException excepcion) {
			throw new IllegalArgumentException("Ha ocurrido un error al crear el archivo.");
		}
	}

	/**
	 * Esta funcion se encarga de crear directorios, para poder despues crear
	 * archivos dentro de este.
	 * 
	 * @param ubicacion     es la ubicación donde se desea guardar el archivo.
	 * @param nombreArchivo es el nombre del archivo, ademas debe incluir su
	 *                      extension.
	 * @param texto         es el texto el cual se desea escribir en el archivo.
	 * 
	 */
	public static void escribirEnArchivoDirectorio(String ubicacion, String nombreArchivo, String texto) {
		try {
			File ubicacionCarpeta = new File(ubicacion + "\\");
			if (!ubicacionCarpeta.exists()) {
				ubicacionCarpeta.mkdirs();
				crearYEscribirEnArchivo(ubicacionCarpeta.getPath() + "\\" + nombreArchivo, texto, 0);
			} else {
				crearYEscribirEnArchivo(ubicacionCarpeta.getPath() + "\\" + nombreArchivo, texto, 0);
			}
		} catch (Exception excepcion) {
			throw new IllegalArgumentException("\nha ocurrido un error al crear el archivo.\n");
		}
	}

	/**
	 * Esta funcion se encarga de crear un archivo, para almacenar todo lo que hizo
	 * el alumno.
	 * 
	 * @param codigoExamen     es el identificador del examen. sirve para cuando
	 *                         revise los resultados, no se mezclen los resultados
	 *                         de diferentes examenes.
	 * @param nombreArchivo    es el nombre del archivo, ademas debe incluir su
	 *                         extension.
	 * @param nombreAlumno     es el nombre del alumno.
	 * @param apellidoAlumno   es el nombre del alumno.
	 * @param rutAlumno        es el rut del alumno.
	 * @param respuestas       es un arreglo con todas las respuestas del alumno.
	 * @param puntajeAlumno    es el puntaje total del alumno.
	 * @param notaFinal        es la nota final del alumno.
	 * @param key              es la contraseña que el profesor le estableció al
	 *                         archivo. si es vacia o es null, simplemente creara un
	 *                         archivo menos seguro.
	 * @param tiempoInicio     es el tiempo en el que el alumno inició el examen.
	 * @param tiempoFinal      es el tiempo en el que el alumno finalizó el examen.
	 * @param tiempoDesarrollo es el tiempo en el que el alumno desarrolló el
	 *                         examen.
	 */
	public static void guardarResultados(String codigoExamen, String nombreArchivo, String nombreAlumno,
			String apellidoAlumno, String rutAlumno, String[] respuestas, String puntajeAlumno, String notaFinal,
			String key, String tiempoInicio, String tiempoFinal, String tiempoDesarrollo) {
		try {
			if (key != null) {
				String nombreFinal = Texto.borrarExtension(nombreArchivo) + "." + nombreAlumno + ".nam";
				String keyRandom = MD5.generarTokenExamen("random", null);

				StringBuilder textoFinal = new StringBuilder();

				textoFinal.append(" " + BASE64.ProtegerTextoBase64(codigoExamen));
				textoFinal.append(" " + BASE64.ProtegerTextoBase64(nombreAlumno));
				textoFinal.append(" " + BASE64.ProtegerTextoBase64(apellidoAlumno));
				textoFinal.append(" " + BASE64.ProtegerTextoBase64(rutAlumno));

				for (String respuestaUsuario : respuestas) {
					if (respuestaUsuario != null) {
						textoFinal.append(" " + BASE64.ProtegerTextoBase64(respuestaUsuario));
					} else {
						textoFinal.append(" " + BASE64.ProtegerTextoBase64("N/A"));
					}
				}

				textoFinal.append(" " + BASE64.ProtegerTextoBase64(puntajeAlumno));
				textoFinal.append(" " + BASE64.ProtegerTextoBase64(notaFinal));
				textoFinal.append(" " + BASE64.ProtegerTextoBase64(tiempoInicio));
				textoFinal.append(" " + BASE64.ProtegerTextoBase64(tiempoFinal));
				textoFinal.append(" " + BASE64.ProtegerTextoBase64(tiempoDesarrollo));
				textoFinal.append(" " + BASE64.ProtegerTextoBase64(keyRandom));

				StringBuilder textoEncriptado = new StringBuilder();
				textoEncriptado.append(RSA.encriptarTexto(key, BASE64.ProtegerTextoBase64(keyRandom)));
				textoEncriptado.append("\n" + AES.encriptar(BASE64.ProtegerTextoBase64(textoFinal.toString()),
						BASE64.ProtegerTextoBase64(keyRandom)));
				textoEncriptado.append(
						"\n\nESTIMAD@ ALUMN@, POR FAVOR HACER ENTREGA DE ESTE ARCHIVO HACIA SU PROFESOR.\nCUALQUIER MODIFICACIÓN O INTENTO DE MODIFICACIÓN DE ESTE ARCHIVO SE CONSIDERARÁ TRAMPA, POR LO QUE SE LE EVALUARÁ CON LA NOTA MINIMA.");

				escribirEnArchivoDirectorio(new File(".").getAbsolutePath() + "\\resultados examen\\", nombreFinal,
						textoEncriptado.toString());

			} else {
				String nombreFinal = Texto.borrarExtension(nombreArchivo) + "." + nombreAlumno + ".nam";
				String keyRandom = MD5.generarTokenExamen("random", null);

				StringBuilder textoFinal = new StringBuilder();

				textoFinal.append(" " + BASE64.ProtegerTextoBase64(codigoExamen));
				textoFinal.append(" " + BASE64.ProtegerTextoBase64(nombreAlumno));
				textoFinal.append(" " + BASE64.ProtegerTextoBase64(apellidoAlumno));
				textoFinal.append(" " + BASE64.ProtegerTextoBase64(rutAlumno));

				for (String respuestaUsuario : respuestas) {
					if (respuestaUsuario != null) {
						textoFinal.append(" " + BASE64.ProtegerTextoBase64(respuestaUsuario));
					} else {
						textoFinal.append(" " + BASE64.ProtegerTextoBase64("N/A"));
					}
				}

				textoFinal.append(" " + BASE64.ProtegerTextoBase64(puntajeAlumno));
				textoFinal.append(" " + BASE64.ProtegerTextoBase64(notaFinal));
				textoFinal.append(" " + BASE64.ProtegerTextoBase64(tiempoInicio));
				textoFinal.append(" " + BASE64.ProtegerTextoBase64(tiempoFinal));
				textoFinal.append(" " + BASE64.ProtegerTextoBase64(tiempoDesarrollo));
				textoFinal.append(" " + BASE64.ProtegerTextoBase64(keyRandom));

				escribirEnArchivoDirectorio(new File(".").getAbsolutePath() + "\\resultados examen\\", nombreFinal,
						BASE64.ProtegerTextoBase64(textoFinal.toString())
								+ "\n\nESTIMAD@ ALUMN@, POR FAVOR HACER ENTREGA DE ESTE ARCHIVO HACIA SU PROFESOR.\nCUALQUIER MODIFICACIÓN O INTENTO DE MODIFICACIÓN DE ESTE ARCHIVO SE CONSIDERARÁ TRAMPA, POR LO QUE SE LE EVALUARÁ CON LA NOTA MINIMA.");
			}

		} catch (Exception excepcion) {
			throw new IllegalArgumentException(
					"<html>Ha ocurrido un error al crear el archivo con los resultados.<bt>intenta ejecutar la aplicacion en el escritorio, o revisa si tienes espacio en el disco duro.</html>");
		}
	}

	/**
	 * Esta funcion se encarga de interpretar el archivo.
	 * 
	 * @param nombreArchivo es el nombre del archivo, ademas debe incluir su
	 *                      extension.
	 * @param contrasena    si el archivo tiene contraseña, esta permitira
	 *                      desencriptar el contenido, para que se pueda convertir
	 *                      en un examen, el cual esta funcion retornará.
	 * @return retorna el exagen generado al interpretar el archivo.
	 * 
	 */
	public static Examen interpretarArchivo(String nombreArchivo, String contrasena) {

		if (nombreArchivo == null) {
			throw new IllegalArgumentException("Nombre de archivo invalido, intentelo nuevamente.");
		}

		// asignaciones
		String Pregunta = null;
		String Respuesta = null;
		String[] Opciones = null;
		int Puntaje = 0;

		boolean examenVacio = true;

		boolean RespuestaTF = false;

		int TipoPregunta = 0;
		int CrearPregunta = 0; // con esto me aseguro que no se creen preguntas que no tengan respuesta u
								// opciones.
		int TipoRespuesta = 0;
		boolean opcionesValidas = false;
		int TipoPeso = 0;

		Examen miExamen = new Examen();

		if (!contrasena.isBlank()) {
			contrasena = MD5.generarTokenExamen("no random", contrasena);
		} else {
			contrasena = null;
		}

		// SIMBOLOGIA:

		// #{tipo_pregunta} {pregunta} = pregunta + el tipo de pregunta.
		// @{numero opciones} {opciones} = opciones de la pregunta (si es que las
		// tiene).
		// ~{respuesta} = respuesta correcta.
		// &{peso} = peso de la pregunta.
		// - = desordenar preguntas al responder el examen.
		// + = permitir al estudiante saber la respuesta despues de equivocarse.
		// ^{tiempo} = tiempo limite de desarrollo que tiene el alumno para resolver el
		// examen.

		File archivo = new File(nombreArchivo);
		if (archivo.exists() && archivo.isFile() && archivo.length() > 0) {

			String[] textoArchivo = decodificarArchivo(archivo, contrasena);

			for (String texto : textoArchivo) {

				if (!texto.isEmpty() && !texto.isBlank()) {
					// con esto obtengo el "id" del examen.
					if (texto.charAt(0) == '%')
						miExamen.setHash(texto.substring(1));

					// con esto obtengo la llave publica del examen.
					if (texto.charAt(0) == '!')
						miExamen.setLlave(texto.substring(1));

					// verifico si el profesor desordeno las preguntas
					if (texto.charAt(0) == '-')
						miExamen.setDesordenar(true);

					// verifico si el profesor permitio mostrar las respuestas
					if (texto.charAt(0) == '+')
						miExamen.setMostrarRespuestas(true);

					// verifico si el profesor permitio mostrar las respuestas
					if (texto.charAt(0) == '^') {
						int tiempo = 0;
						try {
							tiempo = Integer.parseInt(texto.substring(1));
							miExamen.setTiempoLimite(true, tiempo);
						} catch (Exception e) {
							throw new IllegalArgumentException(
									"\n\nERROR FATAL: El archivo ha sido adulterado. \npor favor contacte a su profesor, para que le envie la prueba de nuevo.\n\n");
						}
					}

					// verifico si es la pregunta lo que esta en la linea
					if (texto.charAt(0) == '#') {
						if (texto.charAt(1) == '1') {
							// pregunta tipo seleccion multiple
							TipoPregunta = 1;
							CrearPregunta++;
							Pregunta = texto.substring(2); // corto el string, ignorando los simbolos "#{numero}"
						} else if (texto.charAt(1) == '2') {
							// pregunta tipo verdadero falso
							TipoPregunta = 2;
							CrearPregunta++;
							Pregunta = texto.substring(2); // corto el string, ignorando los simbolos "#{numero}"
						} else if (texto.charAt(1) == '3') {
							// pregunta tipo Respuesta Corta
							TipoPregunta = 3;
							CrearPregunta++;
							Pregunta = texto.substring(2); // corto el string, ignorando los simbolos "#{numero}"
						} else if (texto.charAt(1) == '4') {
							// pregunta tipo Respuesta Corta
							TipoPregunta = 4;
							CrearPregunta++;
							Pregunta = texto.substring(2); // corto el string, ignorando los simbolos "#{numero}"
						} else {
							throw new IllegalArgumentException(
									"\n\nERROR FATAL: El archivo ha sido adulterado. \npor favor contacte a su profesor, para que le envie la prueba de nuevo.\n\n");
						}

						try {
							Pregunta = BASE64.DesprotegerTextoBase64(Pregunta);
						} catch (Exception e) {
							throw new IllegalArgumentException(
									"\n\nERROR FATAL: El archivo ha sido adulterado. \npor favor contacte a su profesor, para que le envie la prueba de nuevo.\n\n");
						}

					}
					if (texto.charAt(0) == '@') {

						// lo que hice aca fue:

						// 1- tomo la linea y separo cada palabra

						String[] arr = texto.split(" ");

						try {
							// esto esta para poder obtener la cantidad total de preguntas

							String tmp = arr[0].substring(1);
							Opciones = new String[Integer.parseInt(tmp)];
						} catch (Exception e) {
							throw new IllegalArgumentException(
									"\n\nERROR FATAL: El archivo ha sido adulterado. \npor favor contacte a su profesor, para que le envie la prueba de nuevo.\n\n");
						}

						try { // en caso de que esten ofuscada las opciones.

							// 2- convertir el array en una lista
							List<String> list = new ArrayList<String>(Arrays.asList(arr));

							// 3- eliminar la primera palabra (es el @numero)
							list.remove(0);

							// 4- convertir la lista de vuelta en un array
							arr = list.toArray(new String[0]);

							// 5- asignar un nuevo array de strings para las opciones, de tamaño arr.length
							Opciones = new String[arr.length];

							for (int i = 0; i < arr.length; ++i) {

								// 6- decodificar las opciones y asignar el array decodificado a la variable
								// opciones[i]

								Opciones[i] = BASE64.DesprotegerTextoBase64(arr[i]);

							}
							// 8- fin.
						} catch (Exception e) {
							throw new IllegalArgumentException(
									"\n\nERROR FATAL: El archivo ha sido adulterado. \npor favor contacte a su profesor, para que le envie la prueba de nuevo.\n\n");
						}

						opcionesValidas = true;
						CrearPregunta++;
					}

					// verifico si es la respuesta lo que esta en la linea

					if (texto.charAt(0) == '~') {
						if (texto.charAt(1) == '1') {
							TipoRespuesta = 1;
						} else if (texto.charAt(1) == '2') {
							TipoRespuesta = 2;
						} else if (texto.charAt(1) == '3') {
							TipoRespuesta = 3;
						} else if (texto.charAt(1) == '4') {
							TipoRespuesta = 4;
						} else {
							throw new IllegalArgumentException(
									"\n\nERROR FATAL: El archivo ha sido adulterado. \npor favor contacte a su profesor, para que le envie la prueba de nuevo.\n\n");
						}

						try {
							Respuesta = BASE64.DesprotegerTextoBase64(texto.substring(2));
						} catch (Exception e) {
							throw new IllegalArgumentException(
									"\n\nERROR FATAL: El archivo ha sido adulterado. \npor favor contacte a su profesor, para que le envie la prueba de nuevo.\n\n");
						}

						CrearPregunta++;
					}

					// verifico si es el peso lo que esta en la linea
					if (texto.charAt(0) == '&') {
						if (texto.charAt(1) == '1') {
							TipoPeso = 1;
						} else if (texto.charAt(1) == '2') {
							TipoPeso = 2;
						} else if (texto.charAt(1) == '3') {
							TipoPeso = 3;
						} else if (texto.charAt(1) == '4') {
							TipoPeso = 4;
						} else {
							throw new IllegalArgumentException(
									"\n\nERROR FATAL: El archivo ha sido adulterado. \npor favor contacte a su profesor, para que le envie la prueba de nuevo.\n\n");
						}

						try {
							Puntaje = Integer.parseInt(texto.substring(2));
						} catch (Exception e) {
							throw new IllegalArgumentException(
									"\n\nERROR FATAL: El archivo ha sido adulterado. \npor favor contacte a su profesor, para que le envie la prueba de nuevo.\n\n");
						}
						CrearPregunta++;
					}

					if (CrearPregunta == 4 && TipoPregunta == 1 && opcionesValidas && TipoPeso == 1
							&& TipoRespuesta == 1) {
						int resp = 0;
						try {
							resp = Integer.parseInt(Respuesta);
						} catch (Exception e) {
							throw new IllegalArgumentException(
									"\n\nERROR FATAL: El archivo ha sido adulterado. \npor favor contacte a su profesor, para que le envie la prueba de nuevo.\n\n");
						}
						/*
						 * con esto, hago mas "comoda" la lectura de las preguntas pues, inserto saltos
						 * de linea cada 50 palabras (incluye simbolos).
						 */
						Pregunta = Texto.InsertarTextoEnString(Pregunta, "\n", 50);

						miExamen.agregaPregunta(new SeleccionMultiple(Pregunta, Opciones, resp, Puntaje));
						CrearPregunta = 0;
						TipoPregunta = 0;
						opcionesValidas = false;
						TipoPeso = 0;
						TipoRespuesta = 0;
						examenVacio = false;

					} else if (CrearPregunta == 3 && TipoPregunta == 2 && !opcionesValidas && TipoPeso == 2
							&& TipoRespuesta == 2) {
						if (Respuesta.equalsIgnoreCase("true")) {
							RespuestaTF = true;
						} else if (Respuesta.equalsIgnoreCase("false")) {
							RespuestaTF = false;
						} else {
							throw new IllegalArgumentException(
									"\n\nERROR FATAL: El archivo ha sido adulterado. \npor favor contacte a su profesor, para que le envie la prueba de nuevo.\n\n");
						}
						/*
						 * con esto, hago mas "comoda" la lectura de las preguntas pues, inserto saltos
						 * de linea cada 50 palabras (incluye simbolos).
						 */
						Pregunta = Texto.InsertarTextoEnString(Pregunta, "\n", 50);

						miExamen.agregaPregunta(new VerdaderoFalso(Pregunta, RespuestaTF, Puntaje));
						CrearPregunta = 0;
						TipoPregunta = 0;
						opcionesValidas = false;
						TipoPeso = 0;
						TipoRespuesta = 0;
						examenVacio = false;

					} else if (CrearPregunta == 3 && TipoPregunta == 3 && !opcionesValidas && TipoPeso == 3
							&& TipoRespuesta == 3) {
						/*
						 * con esto, hago mas "comoda" la lectura de las preguntas pues, inserto saltos
						 * de linea cada 50 palabras (incluye simbolos).
						 */
						Pregunta = Texto.InsertarTextoEnString(Pregunta, "\n", 50);
						miExamen.agregaPregunta(new RespuestasCortas(Pregunta, Respuesta, Puntaje));
						CrearPregunta = 0;
						TipoPregunta = 0;
						opcionesValidas = false;
						TipoPeso = 0;
						TipoRespuesta = 0;
						examenVacio = false;

					} else if (CrearPregunta == 3 && TipoPregunta == 4 && !opcionesValidas && TipoPeso == 4
							&& TipoRespuesta == 4) {
						/*
						 * con esto, hago mas "comoda" la lectura de las preguntas pues, inserto saltos
						 * de linea cada 50 palabras (incluye simbolos).
						 */
						Pregunta = Texto.InsertarTextoEnString(Pregunta, "\n", 50);
						String[] tmp = Respuesta.split(" ");

						Float respuesta = null;
						Float minimo = null;
						Float maximo = null;

						if ("null".equals(BASE64.DesprotegerTextoBase64(tmp[0]))) {
							// si la respuesta es nula, tiene que lanzar una excepcion.
							throw new IllegalArgumentException(
									"ERROR FATAL: El archivo ha sido adulterado. por favor contacte a su profesor, para que le envie la prueba de nuevo.");
						} else {
							try {
								respuesta = Float.valueOf(BASE64.DesprotegerTextoBase64(tmp[0]));
							} catch (Exception e) {
								// si la respuesta fue modificada, tiene que lanzar una excepcion.
								throw new IllegalArgumentException(
										"ERROR FATAL: El archivo ha sido adulterado. por favor contacte a su profesor, para que le envie la prueba de nuevo.");
							}
						}

						if (!"null".equals(BASE64.DesprotegerTextoBase64(tmp[1]))) {
							try {
								minimo = Float.valueOf(BASE64.DesprotegerTextoBase64(tmp[1]));
							} catch (Exception e) {
								throw new IllegalArgumentException(
										"ERROR FATAL: El archivo ha sido adulterado. por favor contacte a su profesor, para que le envie la prueba de nuevo.");
							}
						}

						if (!"null".equals(BASE64.DesprotegerTextoBase64(tmp[2]))) {
							try {
								maximo = Float.valueOf(BASE64.DesprotegerTextoBase64(tmp[2]));
							} catch (Exception e) {
								throw new IllegalArgumentException(
										"ERROR FATAL: El archivo ha sido adulterado. por favor contacte a su profesor, para que le envie la prueba de nuevo.");
							}
						}

						miExamen.agregaPregunta(new RespuestaNumerica(Pregunta, respuesta, minimo, maximo, Puntaje));

						CrearPregunta = 0;
						TipoPregunta = 0;
						opcionesValidas = false;
						TipoPeso = 0;
						TipoRespuesta = 0;
						examenVacio = false;

					} else if (CrearPregunta > 4) {
						throw new IllegalArgumentException(
								"ERROR FATAL: El archivo ha sido adulterado. por favor contacte a su profesor, para que le envie la prueba de nuevo.");
					}
				}
			}

			if (examenVacio) {
				throw new IllegalArgumentException(
						"no se ha agregado ninguna pregunta!, por favor intente usar un archivo valido.");
			}

		} else {
			if (archivo.length() == 0) {
				throw new IllegalArgumentException("\n\nel archivo esta vacio, intente nuevamente.\n");
			} else {
				throw new IllegalArgumentException(
						"\n\nel archivo no existe, por favor revise el nombre, e intentelo nuevamente.\n");
			}
		}

		if (miExamen.getDesordenar()) {
			miExamen.desordenar_todo();
		}

		return miExamen;
	}

	/**
	 * Esta funcion se encarga de deofuscar un archivo, para poder crear un arreglo
	 * de strings con todo el contenido del archivo.
	 * 
	 * @param nombreArchivo es el nombre del archivo.
	 * @param keyProfesor   es la llave del profesor, la cual permite deofuscar los
	 *                      resultados del alumno. sin esta llave, es imposible
	 *                      saber el contenido de las respuestas.
	 * @return retorna un arreglo de strings a partir del contenido del archivo
	 * 
	 */
	public static String[] leerArchivoAlterado(String nombreArchivo, String keyProfesor) {
		if (nombreArchivo == null) {
			throw new IllegalArgumentException("Nombre de archivo invalido, intentelo nuevamente.");
		}

		try {
			File archivo = new File(nombreArchivo);
			Scanner leerArchivo = new Scanner(archivo);
			String[] TextoArchivo = null;
			try {
				String texto = leerArchivo.nextLine();

				if (!texto.isEmpty()) {

					try {
						keyProfesor = RSA.desEncriptarTexto(keyProfesor, texto);
					} catch (Exception e) {
						// agregar al logger que el archivo ha sido modificado
						leerArchivo.close();
						// return null;
						throw new IllegalArgumentException(
								"ERROR FATAL: el resumen del alumno ha sido alterado (" + archivo.getName() + ").");
					}

					texto = leerArchivo.nextLine();

					try {

						texto = AES.desencriptar(texto, BASE64.DesprotegerTextoBase64(keyProfesor));

					} catch (Exception e) {
						// agregar al logger que el archivo ha sido modificado
						leerArchivo.close();
						// return null;
						throw new IllegalArgumentException(
								"ERROR FATAL: el resumen del alumno ha sido alterado (" + archivo.getName() + ").");
					}
					String[] arr = (BASE64.DesprotegerTextoBase64(texto)).split(" ");

					TextoArchivo = new String[arr.length];
					for (int i = 0; i < arr.length; ++i) {
						TextoArchivo[i] = BASE64.DesprotegerTextoBase64(arr[i]);
					}
					leerArchivo.close();
					return TextoArchivo;
				} else if (texto.isEmpty()) {
					leerArchivo.close();
					throw new IllegalArgumentException(
							"ERROR FATAL: el resumen del alumno (" + archivo.getName() + ") esta vacio.");
				} else {
					leerArchivo.close();
					throw new IllegalArgumentException(
							"ERROR FATAL: el resumen del alumno ha sido alterado (" + archivo.getName() + ").");
				}
			} catch (Exception e) {
				leerArchivo.close();
				throw new IllegalArgumentException(
						"ERROR FATAL: el resumen del alumno ha sido alterado (" + archivo.getName() + ").");
			}
		} catch (FileNotFoundException e) {
			throw new IllegalArgumentException("Ha ocurrido un error al leer el archivo " + nombreArchivo + "\n");
		}
	}

	/**
	 * Esta funcion se encarga de leer los datos de los archivos contenidos en una
	 * carpeta, para luego crear una lista, la cual tiene el contenido de cada
	 * archivo.
	 * 
	 * @param ubicacion      es la ubicacion de la carpeta en donde se encuentran
	 *                       los archivos.
	 * @param ubicacionLlave es donde se encuentra la llave del examen. se usa para
	 *                       poder desencriptar los resultados.
	 * @return retorna una lista de strings con todo el contenido de cada archivo
	 *         valido, ademas con el id del examen.
	 * 
	 */
	public static Triplet<List<String[]>, String, String> LeerDatosDeCarpeta(String ubicacion, String ubicacionLlave) {

		if (ubicacion == null) {
			throw new IllegalArgumentException("Ubicación invalida, intentelo nuevamente.");
		}

		File Carpeta = new File(ubicacion);
		List<String[]> DatosAlumno = new ArrayList<>();
		String[] tmp = null;

		File[] files = Carpeta.listFiles();

		String llave = null;
		String idExamen = null;
		String respuestas = null;

		String[] datosKeyProfesor;
		try {
			datosKeyProfesor = RSA.leerLlavesRSAProfesor(ubicacionLlave);

			llave = datosKeyProfesor[0];
			idExamen = datosKeyProfesor[1];
			respuestas = datosKeyProfesor[2];

		} catch (Exception e1) {
			throw new IllegalArgumentException(
					"No se ha podido abrir/leer la llave. ¿Está seguro que no la ha modificado, o es valida?");
		}

		if (files != null) {
			for (final File ArchivoEntrada : files) {
				if (!ArchivoEntrada.isDirectory() && ArchivoEntrada.getName().endsWith(".nam")) {
					try {
						tmp = leerArchivoAlterado(ArchivoEntrada.getAbsolutePath(), llave);
						if (tmp != null) {
							DatosAlumno.add(tmp);
						}
					} catch (IllegalArgumentException e) {
						System.err.print(e.getMessage());
					}
				}
			}
		} else {
			throw new IllegalArgumentException(
					"por favor ingrese la ubicacion correcta de la carpeta con los archivos.");
		}

		return new Triplet<List<String[]>, String, String>(DatosAlumno, idExamen, respuestas);
	}
}