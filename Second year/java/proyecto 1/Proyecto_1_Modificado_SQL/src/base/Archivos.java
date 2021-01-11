package base;

import java.io.*;
import java.nio.file.Files;
import java.util.*;

/**
 * Esta clase se encarga de hacer todo lo relacionado con los archivos (crear
 * archivos, escribir en los archivos e interpretar el archivo).
 * 
 * @author Sebastian Morgado
 * @version 1.6.1
 * @since 1.1.5
 */
class Archivos {

	/**
	 * Esta funcion se encarga de escribir en los archivos.
	 * 
	 * @param nombre_archivo es el nombre del archivo, ademas debe incluir su
	 *                       extension.
	 * @param texto          es el texto que se desea escribir en el archivo.
	 */
	static void Escribir_Archivo(String nombre_archivo, String texto) {
		try {
			FileWriter archivo = new FileWriter(nombre_archivo, true);
			archivo.write(texto);
			archivo.close();

		} catch (IOException excepcion) {
			throw new IllegalArgumentException("\nha ocurrido un error al escribir en el archivo.\n");
		}
	}

	/**
	 * Esta funcion se encarga de crear los archivos
	 * 
	 * @param nombre_archivo es el nombre del archivo, ademas debe incluir su
	 *                       extension.
	 * 
	 */
	static void Crear_Archivo(String nombre_archivo) {
		try {
			File archivo = new File(nombre_archivo);

			if (!archivo.createNewFile()) {
				System.out.println("\n\nel archivo ya existe, desea sobre escribirlo?, si o no?\n");
				if (Texto.leer_teclado().equalsIgnoreCase("si")) {
					archivo.delete();
					Crear_Archivo(nombre_archivo);
				} else {
					throw new IllegalArgumentException("\nse ha cancelado la creacion del archivo.\n");
				}
			}
		} catch (IOException excepcion) {
			throw new IllegalArgumentException("\nha ocurrido un error al crear el archivo.\n");
		}
		return;
	}

	/**
	 * Esta funcion se encarga de interpretar el archivo.
	 * 
	 * @param nombre_archivo es el nombre del archivo, ademas debe incluir su
	 *                       extension.
	 * @return retorna el exagen generado al interpretar el archivo.
	 * 
	 */
	static Examen Interpretar_Archivo(String nombre_archivo) {

		// asignaciones
		String Pregunta = null;
		String Respuesta = null;
		String[] Opciones = null;
		int Puntaje = 0;

		boolean examenVacio = true;

		boolean RespuestaTF = false;

		int TipoPregunta = 0;
		int CrearPregunta = 0; // con esto me aseguro que no se creen preguntas que no tengan respuesta u opciones.
		int TipoRespuesta = 0;
		boolean opcionesValidas = false;
		int TipoPeso = 0;

		Examen miExamen = new Examen();

		// SIMBOLOGIA:
		
		// #{tipo_pregunta} {pregunta} = pregunta + el tipo de pregunta.
		// @{numero opciones} {opciones} = opciones de la pregunta (si es que las tiene).
		// ~{respuesta} = respuesta correcta.
		// &{peso} = peso de la pregunta.
		// - = desordenar preguntas al responder el examen.
		// + = permitir al estudiante saber la respuesta despues de equivocarse.
		// ^{tiempo} = tiempo limite de desarrollo que tiene el alumno para resolver el examen.
		// ${intentos} = cantidad de intentos que tiene el alumno para resolver el examen.
		
		File archivo = new File(nombre_archivo);
		if (archivo.exists() && archivo.isFile() && archivo.length() > 0) {

			String[] textoArchivo = decodificarArchivo(archivo);

			for (String texto : textoArchivo) {
				if (!texto.isEmpty() && !texto.isBlank()) {
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
							throw new IllegalArgumentException("\n\nERROR FATAL: El archivo ha sido adulterado. \npor favor contacte a su profesor, para que le envie la prueba de nuevo.\n\n");
						}
					}

					// verifico si el profesor establecion un limite de intentos.
					if (texto.charAt(0) == '$') {
						int cantidadIntentos = 0;
						try {
							cantidadIntentos = Integer.parseInt(texto.substring(1));
							miExamen.setCantidadIntentos(cantidadIntentos);
						} catch (Exception e) {
							throw new IllegalArgumentException("\n\nERROR FATAL: El archivo ha sido adulterado. \npor favor contacte a su profesor, para que le envie la prueba de nuevo.\n\n");
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
						} else {
							throw new IllegalArgumentException("\n\nERROR FATAL: El archivo ha sido adulterado. \npor favor contacte a su profesor, para que le envie la prueba de nuevo.\n\n");
						}

						try {
							Pregunta = Texto.DesprotegerTexto(Pregunta);
						} catch (Exception e) {
							throw new IllegalArgumentException("\n\nERROR FATAL: El archivo ha sido adulterado. \npor favor contacte a su profesor, para que le envie la prueba de nuevo.\n\n");
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
							throw new IllegalArgumentException("\n\nERROR FATAL: El archivo ha sido adulterado. \npor favor contacte a su profesor, para que le envie la prueba de nuevo.\n\n");
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

								Opciones[i] = Texto.DesprotegerTexto(arr[i]);

							}
							// 8- fin.
						} catch (Exception e) {
							throw new IllegalArgumentException("\n\nERROR FATAL: El archivo ha sido adulterado. \npor favor contacte a su profesor, para que le envie la prueba de nuevo.\n\n");
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
						} else {
							throw new IllegalArgumentException("\n\nERROR FATAL: El archivo ha sido adulterado. \npor favor contacte a su profesor, para que le envie la prueba de nuevo.\n\n");
						}

						try {
							Respuesta = Texto.DesprotegerTexto(texto.substring(2));
						} catch (Exception e) {
							throw new IllegalArgumentException("\n\nERROR FATAL: El archivo ha sido adulterado. \npor favor contacte a su profesor, para que le envie la prueba de nuevo.\n\n");
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
						} else {
							throw new IllegalArgumentException("\n\nERROR FATAL: El archivo ha sido adulterado. \npor favor contacte a su profesor, para que le envie la prueba de nuevo.\n\n");
						}

						try {
							Puntaje = Integer.parseInt(texto.substring(2));
						} catch (Exception e) {
							throw new IllegalArgumentException("\n\nERROR FATAL: El archivo ha sido adulterado. \npor favor contacte a su profesor, para que le envie la prueba de nuevo.\n\n");
						}
						CrearPregunta++;
					}

					if (CrearPregunta == 4 && TipoPregunta == 1 && opcionesValidas && TipoPeso == 1 && TipoRespuesta == 1) {
						int resp = 0;
						try {
							resp = Integer.parseInt(Respuesta);
						} catch (Exception e) {
							throw new IllegalArgumentException("\n\nERROR FATAL: El archivo ha sido adulterado. \npor favor contacte a su profesor, para que le envie la prueba de nuevo.\n\n");
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

					} else if (CrearPregunta == 3 && TipoPregunta == 2 && !opcionesValidas && TipoPeso == 2 && TipoRespuesta == 2) {
						if (Respuesta.equalsIgnoreCase("true")) {
							RespuestaTF = true;
						} else if (Respuesta.equalsIgnoreCase("false")) {
							RespuestaTF = false;
						} else {
							throw new IllegalArgumentException("\n\nERROR FATAL: El archivo ha sido adulterado. \npor favor contacte a su profesor, para que le envie la prueba de nuevo.\n\n");
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

					} else if (CrearPregunta == 3 && TipoPregunta == 3 && !opcionesValidas && TipoPeso == 3 && TipoRespuesta == 3) {
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

					} else if (CrearPregunta > 4) {
						throw new IllegalArgumentException("\n\nERROR FATAL: El archivo ha sido adulterado. \npor favor contacte a su profesor, para que le envie la prueba de nuevo.\n\n");
					}
				}
			}

			if (examenVacio) {
				throw new IllegalArgumentException("\n\nno se ha agregado ninguna pregunta!, por favor intente usar un archivo valido.\n");
			}

		} else {
			if (archivo.length() == 0) {
				throw new IllegalArgumentException("\n\nel archivo esta vacio, intente nuevamente.\n");
			} else {
				throw new IllegalArgumentException("\n\nel archivo no existe, por favor revise el nombre, e intentelo nuevamente.\n");
			}
		}

		return miExamen;
	}

	/**
	 * Esta funcion se encarga de crear un archivo, para almacenar todo lo que hizo
	 * el alumno.
	 * 
	 * @param nombre_archivo es el nombre del archivo, ademas debe incluir su
	 *                       extension.
	 * @param Nombre_Alumno  es el nombre del alumno.
	 * @param respuestas     es un arreglo con todas las respuestas del alumno.
	 * @param Puntaje_Alumno es el puntaje total del alumno.
	 * @param Nota_Final     es la nota final del alumno.
	 */
	static void Guardar_Resultados(String nombre_archivo, String Nombre_Alumno, String[] respuestas, String Puntaje_Alumno, String Nota_Final) {
		try {
			String nombre_final = Texto.Borrar_Extension(nombre_archivo) + "." + Nombre_Alumno + ".nam";
			String texto_final = "";

			File archivo = new File(nombre_final);

			if (!archivo.createNewFile()) {
				archivo.delete();
				Crear_Archivo(nombre_final);
			}

			texto_final += Texto.ProtegerTexto(Nombre_Alumno);

			for (String respuestas_usuario : respuestas) {
				if (respuestas_usuario != null) {
					texto_final += (" " + Texto.ProtegerTexto(respuestas_usuario));
				} else {
					texto_final += (" " + Texto.ProtegerTexto("N/A"));
				}
			}

			texto_final += (" " + Texto.ProtegerTexto(Puntaje_Alumno));

			texto_final += (" " + Texto.ProtegerTexto(Nota_Final));

			Escribir_Archivo(nombre_final, Texto.ProtegerTexto(texto_final));

		} catch (IOException excepcion) {
			throw new IllegalArgumentException("\nha ocurrido un error al crear el archivo con los resultados.\nintenta ejecutar la aplicacion en el escritorio, o revisa si tienes espacio en el disco duro.\n");
		}
	}

	/**
	 * Esta funcion se encarga de leer los datos de los archivos contenidos en una
	 * carpeta, para luego crear una lista, la cual tiene el contenido de cada
	 * archivo.
	 * 
	 * @param ubicacion es la ubicacion de la carpeta en donde se encuentran los
	 *                  archivos.
	 * @return retorna una lista de strings con todo el contenido de cada archivo
	 *         valido.
	 * 
	 */
	static List<String[]> LeerDatosDeCarpeta(String ubicacion) {
		File Carpeta = new File(ubicacion);
		List<String[]> DatosAlumno = new ArrayList<>();
		String[] tmp = null;

		File[] files = Carpeta.listFiles();

		if (files != null) {
			for (final File ArchivoEntrada : files) {
				if (!ArchivoEntrada.isDirectory() && ArchivoEntrada.getName().endsWith(".nam")) {
					try {
						tmp = Leer_Archivo_Alterado(ArchivoEntrada.getAbsolutePath());
						if (tmp != null) {
							DatosAlumno.add(tmp);
						}
					} catch (IllegalArgumentException e) {
						System.err.print(e.getMessage());
					}
				}
			}
		} else {
			throw new IllegalArgumentException("\n\npor favor ingrese la ubicacion correcta de la carpeta con los archivos.\n\n");
		}
		return DatosAlumno;
	}

	/**
	 * Esta funcion se encarga de deofuscar un archivo, para poder crear un arreglo
	 * de strings con todo el contenido del archivo.
	 * 
	 * @param nombre_archivo es el nombre del archivo.
	 * @return retorna un arreglo de strings a partir del contenido del archivo
	 * 
	 */
	private static String[] Leer_Archivo_Alterado(String nombre_archivo) {
		try {
			File archivo = new File(nombre_archivo);
			Scanner leer_archivo = new Scanner(archivo);
			String[] TextoArchivo = null;
			try {
				String texto = leer_archivo.nextLine();
				if (!texto.isEmpty() && Texto.esValido(texto)) {
					String[] arr = (Texto.DesprotegerTexto(texto)).split(" ");
					TextoArchivo = new String[arr.length];
					for (int i = 0; i < arr.length; ++i) {
						TextoArchivo[i] = Texto.DesprotegerTexto(arr[i]);
					}
					leer_archivo.close();
					return TextoArchivo;
				} else if (texto.isEmpty()) {
					leer_archivo.close();
					throw new IllegalArgumentException("\n\nERROR FATAL: el resumen del alumno (" + archivo.getName() + ") esta vacio.\n\n");
				} else {
					leer_archivo.close();
					throw new IllegalArgumentException("\n\nERROR FATAL: el resumen del alumno ha sido alterado (" + archivo.getName() + ").\n\n");
				}
			} catch (Exception e) {
				leer_archivo.close();
				throw new IllegalArgumentException("\n\nERROR FATAL: el resumen del alumno ha sido alterado (" + archivo.getName() + ").\n\n");
			}
		} catch (FileNotFoundException e) {
			throw new IllegalArgumentException("\n\nha ocurrido un error al leer el archivo " + nombre_archivo + "\n");
		}
	}

	/**
	 * Esta funcion se encarga de deofuscar un archivo, para poder crear un arreglo
	 * de strings con todo el contenido del archivo. A diferencia de {@link #Leer_Archivo_Alterado Leer Archivo Alterado},
	 * esta función tiene de prioridad el de deofuscar el archivo de preguntas del profesor,
	 * mientras que el otro tiene de prioridad deofuscar las respuestas del alumno. 
	 * 
	 * @param archivo Es el archivo ya abierto.
	 * @return retorna un arreglo de strings a partir del contenido del archivo
	 * 
	 */
	private static String[] decodificarArchivo(File archivo) {
		try {
			Scanner leer_archivo = new Scanner(archivo);

			String texto = Texto.DesprotegerTexto(leer_archivo.nextLine());

			String[] arr = texto.split("\n");

			leer_archivo.close();

			return arr;
		} catch (Exception e) {
			throw new IllegalArgumentException("\n\nERROR FATAL: El archivo ha sido adulterado. \npor favor contacte a su profesor, para que le envie la prueba de nuevo.\n\n");
		}
	}

	/**
	 * Esta funcion se encarga de ofuscar un archivo, y su resultado
	 * es un texto dificil de leer.
	 * 
	 * @param nombre_archivo Es el nombre del archivo.
	 * 
	 */
	static void codificarArchivo(String nombre_archivo) {
		try {
			File archivo = new File(nombre_archivo);
			String tmp = Texto.ProtegerTexto(Files.readAllBytes(archivo.toPath()));
			archivo.delete();
			Crear_Archivo(nombre_archivo);
			Escribir_Archivo(nombre_archivo, tmp);
		} catch (IOException e) {
			throw new IllegalArgumentException("\n\nha ocurrido un error al codificar el archivo, intentelo nuevamente.\n");
		}
	}

}
