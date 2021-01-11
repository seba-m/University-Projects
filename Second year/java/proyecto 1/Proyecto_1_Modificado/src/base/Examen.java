package base;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Esta clase se encarga de hacer todo lo relacionado con el examen (agregar
 * preguntas, responder la prueba, etc).
 * 
 * @author Sebastian Morgado
 * @version 1.6.1
 * @since 0.5.0
 */

class Examen {
	
	/**
	 * la variable preguntas_SM (preguntas seleccion multiple) almacena todas las
	 * preguntas de tipo seleccion multiple.
	 */
	private List<SeleccionMultiple> preguntas_SM = new ArrayList<SeleccionMultiple>();

	/**
	 * la variable preguntas_RC (preguntas Respuestas Cortas) almacena todas las
	 * preguntas de tipo Respuestas Cortas.
	 */
	private List<RespuestasCortas> preguntas_RC = new ArrayList<RespuestasCortas>();

	/**
	 * la variable preguntas_VF (preguntas Verdadero Falso) almacena todas las
	 * preguntas de tipo Verdadero Falso.
	 */
	private List<VerdaderoFalso> preguntas_VF = new ArrayList<VerdaderoFalso>();

	/**
	 * la variable preguntas_SM_Backup (preguntas seleccion multiple copia de seguridad) almacena todas las
	 * preguntas de tipo seleccion multiple, con el fin de que estas no seran desordenadas.
	 */
	private List<SeleccionMultiple> preguntas_SM_Backup;

	/**
	 * la variable preguntas_RC_Backup (preguntas Respuestas Cortas copia de seguridad) almacena todas las
	 * preguntas de tipo Respuestas Cortas, con el fin de que estas no seran desordenadas.
	 */
	private List<RespuestasCortas> preguntas_RC_Backup;

	/**
	 * la variable preguntas_VF_Backup (preguntas Verdadero Falso copia de seguridad) almacena todas las
	 * preguntas de tipo Verdadero Falso, con el fin de que estas no seran desordenadas.
	 */
	private List<VerdaderoFalso> preguntas_VF_Backup;

	/**
	 * esta variable es la que verifica si el profesor ha elegido que se desordenen
	 * las preguntas.
	 */
	private boolean Desordenar = false;

	/**
	 * esta variable es la que verifica si el profesor ha elegido que se muestren
	 * las respuestas cuando el alumno se equivoca.
	 */
	private boolean MostrarRespuestas = false;
	
	/**
	 * esta variable sirve para activar o desactivar el limite de tiempo en el
	 * examen.
	 */
	private boolean TiempoLimite = false;

	/**
	 * esta variable sirve para almacenar el tiempo maximo que tendra el alumno
	 * para responder el examen.
	 */
	private long TiempoMaximo = 0;
	
	/**
	 * Esta funcion se agregar preguntas de tipo seleccion multiple al examen.
	 * 
	 * @param Pregunta_Seleccion_Multiple es la pregunta que se desea agregar (esta
	 *                                    debe tener la pregunta, las opciones, la
	 *                                    respuesta y el puntaje que vale)
	 */
	void agregaPregunta(SeleccionMultiple Pregunta_Seleccion_Multiple) {
		preguntas_SM.add(Pregunta_Seleccion_Multiple);
		return;
	}

	/**
	 * Esta funcion se agregar preguntas de tipo respuesta corta al examen.
	 * 
	 * @param Pregunta_Respuesta_Corta es la pregunta que se desea agregar (esta
	 *                                 debe tener la pregunta, la respuesta y el
	 *                                 puntaje que vale)
	 */
	void agregaPregunta(RespuestasCortas Pregunta_Respuesta_Corta) {
		preguntas_RC.add(Pregunta_Respuesta_Corta);
		return;
	}

	/**
	 * Esta funcion se agregar preguntas de tipo respuesta verdadero y falso al
	 * examen.
	 * 
	 * @param Pregunta_Verdadero_Falso es la pregunta que se desea agregar (esta
	 *                                 debe tener la pregunta, la respuesta y el
	 *                                 puntaje que vale)
	 */
	void agregaPregunta(VerdaderoFalso Pregunta_Verdadero_Falso) {
		preguntas_VF.add(Pregunta_Verdadero_Falso);
		return;
	}

	/**
	 * Esta funcion se encarga de setear la opcion desordenar.
	 * 
	 * @param opcion si es {@code true}, desordenara todas las preguntas, como
	 *               tambien las opciones, si es {@code false}, no hara ningun
	 *               cambio.
	 */
	public void setDesordenar(boolean opcion) {
		this.Desordenar = opcion;
	}
	
	/**
	 * este metodo es el setter de la cantidad de intentos.
	 * @param cantidad es la cantidad de intentos que tiene el alumno.
	 */
	public void setCantidadIntentos(int cantidad) {
		Pregunta.setCantidadIntentos(cantidad);
	}

	/**
	 * Esta funcion se encarga de setear la opcion MostrarRespuestas.
	 * 
	 * @param opcion si es {@code true}, mostrara las respuestas cada vez que el
	 * 				 alumno responda. si es {@code false}, no hara mostrara nada.
	 */
	public void setMostrarRespuestas(boolean opcion) {
		this.MostrarRespuestas = opcion;
	}
	
	/**
	 * Esta funcion se encarga de setear la opcion TiempoLimite.
	 * 
	 * @param opcion si es {@code true}, iniciara un contador, que a medida que 
	 * 				 responda el alumno, ira verificando si esta dentro del limite 
	 * 				 de tiempo que se le dio. si es {@code false}, el examen no 
	 * 				 tendra ningun limite de tiempo.
	 * 
	 * @param tiempo si el tiempo es distinto de cero y se ha activado el limite de
	 * 				 tiempo, iniciara un cronometro, el cual cuando llegue a su 
	 * 				 limite, detendra la prueba.
	 */
	public void setTiempoLimite(boolean opcion, int tiempo) {
		this.TiempoLimite = opcion;
		if (opcion == true && tiempo != 0) {
			this.TiempoMaximo = tiempo*60000;
		}
	}	
	
	/**
	 * Esta funcion se encarga de borrar preguntas del examen.
	 * 
	 * @param index es la posicion de la pregunta. como listas inicia en cero, la
	 *              primera pregunta siempre sera esa.
	 * @param tipo  es el tipo de la funcion, tiene sm (seleccion multiple), vf
	 *              (verdadero falso) y rc (respuesta corta).
	 */
	private void eliminaPregunta(int index, String tipo) {
		if (tipo.equalsIgnoreCase("sm")) {
			preguntas_SM.remove(index);
		} else if (tipo.equalsIgnoreCase("vf")) {
			preguntas_VF.remove(index);
		} else if (tipo.equalsIgnoreCase("rc")) {
			preguntas_RC.remove(index);
		} else {
			System.err.println("\n\nopcion no valida.\n\n");
		}
		return;
	}

	/**
	 * Esta funcion la hice solo para (tal como lo dice su nombre) verificar si el
	 * examen tiene alguna pregunta o no.
	 * 
	 * @param tipo es el tipo de pregunta que se desea verificar si tiene preguntas
	 *             o no. 1 es seleccion multiple, 2 es verdadero falso, 3 es
	 *             respuesta corta.
	 * @return retorna verdadero si la lista de preguntas no esta vacia, si se da el
	 *         caso contrario, retorna falso.
	 * 
	 */
	private boolean verificar_examen(int tipo) {
		if (tipo == 1) {
			return (preguntas_SM != null && !preguntas_SM.isEmpty());
		}
		if (tipo == 2) {
			return (preguntas_VF != null && !preguntas_VF.isEmpty());
		}
		if (tipo == 3) {
			return (preguntas_RC != null && !preguntas_RC.isEmpty());
		}
		return false;
	}

	/**
	 * Esta funcion la hice solo para (tal como lo dice su nombre) obtener la
	 * cantidad de preguntas que cada lista de preguntas tiene.
	 * 
	 * @param tipo es el tipo de pregunta que se desea verificar si tiene preguntas
	 *             o no. 1 es seleccion multiple, 2 es verdadero falso, 3 es
	 *             respuesta corta.
	 * @return retorna la cantidad de datos dentro de la pregunta, caso contrario,
	 *         retorna 0.
	 * 
	 */
	private int obtener_longitud(int tipo) {
		if (tipo == 1) {
			return (preguntas_SM.size());
		}
		if (tipo == 2) {
			return (preguntas_VF.size());
		}
		if (tipo == 3) {
			return (preguntas_RC.size());
		}
		return 0;
	}

	/**
	 * Esta funcion la hice solo para (tal como lo dice su nombre) obtener el
	 * puntaje total de la prueba.
	 * 
	 * @return retorna la cantidad de datos dentro de la pregunta, caso contrario,
	 *         retorna 0.
	 * 
	 */
	int obtener_puntaje_total() {

		int puntaje_total = 0;

		if (preguntas_SM != null) {
			for (SeleccionMultiple preguntasSeleccionMultiple : preguntas_SM) {
				puntaje_total += preguntasSeleccionMultiple.getPeso();
			}
		}

		if (preguntas_VF != null) {
			for (VerdaderoFalso preguntasVerdaderoFalso : preguntas_VF) {
				puntaje_total += preguntasVerdaderoFalso.getPeso();
			}
		}

		if (preguntas_RC != null) {
			for (RespuestasCortas preguntasRespuestasCortas : preguntas_RC) {
				puntaje_total += preguntasRespuestasCortas.getPeso();
			}
		}

		return puntaje_total;
	}

	/**
	 * Este metodo se encarga de pedirle al alumno la ubicacion del archivo que le
	 * paso el profesor, para poder responder las preguntas, y asi generar un
	 * archivo .nam con los resultados del alumno.
	 * 
	 * @param Nombre_Alumno es el nombre del alumno. se usara para crear el archivo
	 *                      .nam (nombre examen . nombre alumno .nam)
	 */
	static void ResponderPreguntasProfesor(String Nombre_Alumno) {
		Examen examenAlumno = null;
		String Nombre_Archivo = null;
		Boolean continuar = true;
		
		do {
			try {
				String ubicacion_programa = new File(".").getCanonicalPath();
				System.out.println("\n\nantes de empezar, necesito que me digas donde esta \nla prueba (o el archivo) que te dio el profesor.\n(para que te sea mas facil, deja el archivo en " + ubicacion_programa + ")\npara cancelar escribe 'salir'\n");
				Nombre_Archivo = Texto.leer_teclado();
				if (Nombre_Archivo.equalsIgnoreCase("salir")) {
					return;
				}					
				examenAlumno = Archivos.Interpretar_Archivo(Nombre_Archivo);
				continuar = false;
			} catch (IllegalArgumentException | IOException e) {
				System.err.println(e.getMessage()); // en caso de que no encuentre el archivo, lanza un mensaje de error
			}
		} while (continuar);

		int puntaje_prueba = examenAlumno.darExam();

		String Nota_Final = String.format("%.1f", ((float)examenAlumno.calcular_nota(puntaje_prueba, 60)/10));
		
		System.out.println("tu puntaje fue: " + puntaje_prueba + ", lo que significa que obtuviste un " + ((int)(((float) puntaje_prueba / examenAlumno.obtener_puntaje_total()) * 100)) + "% de preguntas correctas respondidas. tu nota final es un " + Nota_Final + " .");
		
		try {
			Archivos.Guardar_Resultados(Nombre_Archivo, Nombre_Alumno, examenAlumno.obtener_Respuestas_Usuario(),String.valueOf(puntaje_prueba), Nota_Final);
		} catch (IllegalArgumentException e) {
			System.err.println(e.getMessage());
		}
	}
	
	/**
	 * Esta funcion la hice solo para (tal como lo dice su nombre) obtener la nota
	 * de la prueba.
	 * 
	 * @param puntaje    es el puntaje que se obtuvo en la prueba.
	 * @param porcentaje es el porcentaje de exigencia (por lo general es 60%).
	 * @return retorna la nota que se obtuvo en el examen. en caso de que el alumno
	 *         haya obtenido mas puntaje que el total de la prueba, obtendra un 1.0
	 */
	int calcular_nota(double puntaje, double porcentaje) {
		double nota_minima = 10;
		double nota_aprobacion = 40;
		double nota_maxima = 70;
		double puntaje_maximo = obtener_puntaje_total();
		double puntaje_minimo = 0;
		double puntaje_aprobacion = (puntaje_maximo * porcentaje) / 100;

		double nota_final = 0;

		//para obtener la nota, uso la ecuacion de la resta (y-y1 = m*(x-x1) , donde 'y' es la nota, y 'x' es el puntaje obtenido).
		if (puntaje >= puntaje_aprobacion && puntaje <= puntaje_maximo) {
			nota_final = ((nota_aprobacion - nota_maxima) / (puntaje_aprobacion - puntaje_maximo)) * (puntaje - puntaje_maximo) + nota_maxima;
		} else if (puntaje <= puntaje_maximo) {
			nota_final = ((nota_minima - nota_aprobacion) / (puntaje_minimo - puntaje_aprobacion)) * (puntaje - puntaje_aprobacion) + nota_aprobacion;
		} else {
			// caso imposible en el que el alumno haya obtenido mas puntaje que el puntaje total de la prueba.
			return 10;
		}

		return (int) (Math.round(nota_final));
	}

	/**
	 * Esta funcion la hice solo para (tal como lo dice su nombre) convertir la
	 * prueba a un formato smr (mi nombre y apellidos).
	 * 
	 * @param NombreArchivo como dice el nombre del parametro, es el nombre de la
	 *                      prueba.
	 */
	private void convertir_a_smr(String NombreArchivo) {

		boolean continuar = true;
		
		String texto = "";
		
		// primero concateno el nombre del archivo con .smr
		NombreArchivo += ".smr";

		// creo el archivo, con el nombre antes dado
		try {
			Archivos.Crear_Archivo(NombreArchivo);
		} catch (IllegalArgumentException e) {
			System.err.println(e.getMessage());
		}
		

		// consulto por opciones adicionales para el archivo
		System.out.println("\n\ndeseas que las preguntas se ordenen en un orden aleatorio (el orden por defecto es Seleccion multiple, verdadero falso y respuesta corta),\ncada vez que se abra el archivo? (si o no):\n\n");
		if (Texto.leer_teclado().equalsIgnoreCase("si")) {
			texto += "-\n";
		}
	
		System.out.println("\n\ndeseas que al estudiante se le diga la respuesta correcta si se equivoca? (si o no):\n\n");
		if (Texto.leer_teclado().equalsIgnoreCase("si")) {
			texto += "+\n";
		}
		
		continuar = true;
		
		System.out.println("\n\ndeseas establecer un limite de tiempo para la prueba? (si o no):\n\n");
		if (Texto.leer_teclado().equalsIgnoreCase("si")) {
			int duracion = 0;
			do {
				System.out.print("\n\nnecesito que me digas EN MINUTOS cuanto durara la prueba (minimo 1 minuto y maximo 525600 minutos (1 año)): ");
				try {
					duracion = Integer.parseInt(Texto.leer_teclado());
					if (duracion > 0 && duracion <= 525600) {
						texto += "^"+ duracion + "\n";
						continuar = false;
					} else {
						System.err.println("\n\npor favor ingrese valores entre 1 y 525600.\n\n");
					}
				} catch (Exception e) {
					System.err.println("\n\npor favor ingrese NUMEROS de tipo entero.\n\n");
				}
			} while (continuar);
		}
		
		continuar = true;
		
		System.out.println("\n\ndeseas establecer un limite de intentos para la prueba? (si o no):\n\n");
		if (Texto.leer_teclado().equalsIgnoreCase("si")) {
			int intentos = 0;
			do {
				System.out.print("\n\nnecesito que me digas cuantos intentos tendra tu prueba (minimo 1 y máximo 10000): ");
				try {
					intentos = Integer.parseInt(Texto.leer_teclado());
					if (intentos >= 1 && intentos <= 10000) {
						//Archivos.Escribir_Archivo(NombreArchivo, "$"+ intentos + "\n");
						texto += "$"+ intentos + "\n";
						continuar = false;
					} else {
						System.err.println("\n\npor favor ingrese valores entre 1 y 525600.\n\n");
					}
				} catch (Exception e) {
					System.err.println("\n\npor favor ingrese NUMEROS de tipo entero.\n\n");
				}
			} while (continuar);
		}

		// SIMBOLOGIA:
		
		// #{tipo_pregunta} {pregunta} = pregunta + el tipo de pregunta.
		// @{numero opciones} {opciones} = opciones de la pregunta (si es que las tiene).
		// ~{respuesta} = respuesta correcta.
		// &{peso} = peso de la pregunta.
		// - = desordenar preguntas al responder el examen.
		// + = permitir al estudiante saber la respuesta despues de equivocarse.
		// ^{tiempo} = tiempo limite de desarrollo que tiene el alumno para resolver el examen.
		// ${intentos} = cantidad de intentos que tiene el alumno para resolver el examen.

		// escribo las preguntas con sus respuestas en el archivo
		System.out.println("\n\nvista previa de las preguntas que son guardadas en el archivo:\n\n");

		if (verificar_examen(1)) {// preguntas seleccion multiple
			mostrarPreguntas(1);
			for (int i = 0; i < preguntas_SM.size(); i++) {
				
				texto += "#1" + Texto.ProtegerTexto(preguntas_SM.get(i).getPregunta()) + "\n";
				texto += "@" + (preguntas_SM.get(i).getOpciones().length) + " ";
				for (int k = 0; k < preguntas_SM.get(i).getOpciones().length; k++) {
					texto += Texto.ProtegerTexto(preguntas_SM.get(i).getOpciones()[k]) + " ";
				}
				texto += "\n~1" + Texto.ProtegerTexto(preguntas_SM.get(i).getRespuesta()) + "\n";
				texto += "&1" + preguntas_SM.get(i).getPeso() + "\n";
			}
		}

		if (verificar_examen(2)) {// preguntas verdadero falso
			mostrarPreguntas(2);
			for (int i = 0; i < preguntas_VF.size(); i++) {

				texto += "#2" + Texto.ProtegerTexto(preguntas_VF.get(i).getPregunta()) + "\n";
				texto += "~2" + Texto.ProtegerTexto(preguntas_VF.get(i).getRespuesta()) + "\n";
				texto += "&2" + preguntas_VF.get(i).getPeso() + "\n";
			}
		}

		if (verificar_examen(3)) {// preguntas respuesta corta
			mostrarPreguntas(3);
			for (int i = 0; i < preguntas_RC.size(); i++) {

				texto += "#3" + Texto.ProtegerTexto(preguntas_RC.get(i).getPregunta()) + "\n";
				texto += "~3" + Texto.ProtegerTexto(preguntas_RC.get(i).getRespuesta()) + "\n";
				texto += "&3" + preguntas_RC.get(i).getPeso() + "\n";
			}
		}
		try {
			Archivos.Escribir_Archivo(NombreArchivo,texto);
			Archivos.codificarArchivo(NombreArchivo);
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		
	}

	/**
	 * Esta funcion se encarga de mostrar las preguntas.
	 * 
	 * @param tipo como dice el nombre del parametro, es el tipo de la prueba. 1 es
	 *             seleccion multiple, 2 es verdadero falso, 3 es respuesta corta.
	 */
	private void mostrarPreguntas(int tipo) {

		if (tipo == 1 && verificar_examen(1)) {
			
			for (int i = 0; i < preguntas_SM.size(); i++) {
				System.out.print(i + 1);
				preguntas_SM.get(i).mostrar();
			}
			return;
		}

		if (tipo == 2 && verificar_examen(2)) {
			for (int i = 0; i < preguntas_VF.size(); i++) {
				System.out.print(i + 1);
				preguntas_VF.get(i).mostrar();
			}
			return;
		}

		if (tipo == 3 && verificar_examen(3)) {
			for (int i = 0; i < preguntas_RC.size(); i++) {
				System.out.print(i + 1);
				preguntas_RC.get(i).mostrar();
			}
			return;
		}

	}

	/**
	 * Esta funcion se encarga de desordenar los items del examen.
	 * @return retorna los items desordenados.
	 */
	private Object[] desordenar_Items() {
		
		// creo este array de tipo Object
		Object[] Items = new ArrayList[3];

		// voy almacenando en el array las listas

		if (verificar_examen(1)) {
			Items[0] = preguntas_SM;
		}

		if (verificar_examen(2)) {
			Items[1] = preguntas_VF;
		}

		if (verificar_examen(3)) {
			Items[2] = preguntas_RC;
		}

		// luego convierto el array en una lista
		List<Object> lista = Arrays.asList(Items);

		// desordeno el contenido que esta dentro de la lista
		Collections.shuffle(lista);
		
		return lista.toArray(new Object[0]);
	}
	
	/**
	 * Esta funcion se encarga de desordenar las preguntas del examen.
	 */
	private void desordenar_preguntas() {
		if (verificar_examen(1)) {
			Collections.shuffle(preguntas_RC);
		}
		if (verificar_examen(2)) {
			Collections.shuffle(preguntas_VF);
		}
		if (verificar_examen(3)) {
			Collections.shuffle(preguntas_SM);
		}
	}

	/**
	 * Esta función se encarga de desordenar los 3 ítems del examen y preguntar.
	 * 
	 * @return retorna el puntaje total obtenido.
	 * 
	 */
	private int desordenar_todo() {

		// primero desordeno las preguntas que tienen las listas
		desordenar_preguntas();
		
		//luego desordeno el orden de los items		
		Object[] Items = desordenar_Items();
		
		int puntaje_total = 0;
		long TiempoExamen = 0;		

		int contador = 1;
		
		if (TiempoLimite == true) {
			TiempoExamen = System.currentTimeMillis();
		}
		
		//aqui hago las preguntas para el alumno.
		for (int i = 0; i < Items.length; i++) {
			// voy verificando si el arreglo en la posicion i no es nulo
			if (Items[i] != null) {
				/**
				 * verifico si el item que esta en la posicion i es de tipo SeleccionMultiple,
				 * convirtiendo el objeto en una lista (List<?>), luego usando el operador
				 * "instanceof" reviso si la lista es de la clase SeleccionMultiple.
				 */
				if (((List<?>) (Items[i])).get(0) instanceof SeleccionMultiple) {
					// si entra aca, preguntara, y sumara el puntaje obtenido por el alumno.
					for (int j = 0; j < preguntas_SM.size(); j++) {
						
						if ((TiempoLimite == true) && ((System.currentTimeMillis() - TiempoExamen) > TiempoMaximo)) {
							System.err.println("\n\nSE ACABO EL TIEMPO!\n\n");
							return puntaje_total;
						}
						
						preguntas_SM.get(j).setNumeroPregunta(contador++);
						preguntas_SM.get(j).setMostrarRespuestas(MostrarRespuestas);
						if (preguntas_SM.get(j).buscar() == true) {
							puntaje_total += preguntas_SM.get(j).getPeso();
						}
					}
				}
				/**
				 * verifico si el item que esta en la posicion i es de tipo VerdaderoFalso,
				 * convirtiendo el objeto en una lista (List<?>), luego usando el operador
				 * "instanceof" reviso si la lista es de la clase VerdaderoFalso.
				 */
				if (((List<?>) (Items[i])).get(0) instanceof VerdaderoFalso) {
					// si entra aca, preguntara, y sumara el puntaje obtenido por el alumno.
					for (int j = 0; j < preguntas_RC.size(); j++) {
						
						if ((TiempoLimite == true) && ((System.currentTimeMillis() - TiempoExamen) > TiempoMaximo)) {
							System.err.println("\n\nSE ACABO EL TIEMPO!\n\n");
							return puntaje_total;
						}

						preguntas_RC.get(j).setNumeroPregunta(contador++);
						preguntas_RC.get(j).setMostrarRespuestas(MostrarRespuestas);
						if (preguntas_RC.get(j).buscar() == true) {
							puntaje_total += preguntas_RC.get(j).getPeso();
						}
					}
				}
				/**
				 * verifico si el item que esta en la posicion i es de tipo RespuestasCortas,
				 * convirtiendo el objeto en una lista (List<?>), luego usando el operador
				 * "instanceof" reviso si la lista es de la clase RespuestasCortas.
				 */
				if (((List<?>) (Items[i])).get(0) instanceof RespuestasCortas) {
					// si entra aca, preguntara, y sumara el puntaje obtenido por el alumno.
					for (int j = 0; j < preguntas_VF.size(); j++) {
						
						if ((TiempoLimite == true) && ((System.currentTimeMillis() - TiempoExamen) > TiempoMaximo)) {
							System.err.println("\n\nSE ACABO EL TIEMPO!\n\n");
							return puntaje_total;
						}
						
						preguntas_VF.get(j).setNumeroPregunta(contador++);
						preguntas_VF.get(j).setMostrarRespuestas(MostrarRespuestas);
						if (preguntas_VF.get(j).buscar() == true) {
							puntaje_total += preguntas_VF.get(j).getPeso();
						}
					}
				}
			}
		}

		/*
		 * pd: la razon por la cual no inicialize el objeto como tipo lista, fue por que
		 * al tratar de obtener de vuelta las listas, no las reconocia como tipo
		 * VerdaderoFalso o RespuestasCortas o SeleccionMultiple, por lo que intentando,
		 * me termine decantando por la opcion de array, la cual me ha funcionado hasta
		 * ahora.
		 * 
		 * pd2: y el porqué del "List<?>", fue por que el IDE me arrojaba error si no
		 * usaba de esta forma al convertir a tipo lista.
		 */

		return puntaje_total;
	}
	
	/**
	 * Esta funcion se encarga de obtener las respuestas del usuario.
	 * 
	 * @return retorna el puntaje total obtenido por el alumno.
	 */
	String[] obtener_Respuestas_Usuario() {
		List<String> respuesta = new ArrayList<>();

		if (Desordenar == false) {
			
			if (verificar_examen(1)) {
				for (int i = 0; i < preguntas_SM.size(); i++) {
					if (preguntas_SM.get(i) != null) {
						respuesta.add(preguntas_SM.get(i).getRespuestaAlumno());
					}
				}
			}
			
			if (verificar_examen(2)) {
				for (int i = 0; i < preguntas_VF.size(); i++) {
					if (preguntas_VF.get(i) != null) {
						respuesta.add(preguntas_VF.get(i).getRespuestaAlumno());
					}
				}
			}
			
			if (verificar_examen(3)) {
				for (int i = 0; i < preguntas_RC.size(); i++) {
					if (preguntas_RC.get(i) != null) {
						respuesta.add(preguntas_RC.get(i).getRespuestaAlumno());
					}
				}
			}
		} else {
			
			//pd: intente ordenar todo esto usando CompareTo(), pero no sirvió.
			
			if (verificar_examen(1)) {
				for (int i = 0; i < preguntas_SM_Backup.size(); i++) {
					for (int j = 0; j < preguntas_SM_Backup.size(); j++) {
						if (preguntas_SM_Backup.get(i).getPregunta().equalsIgnoreCase(preguntas_SM.get(j).getPregunta())) {
							if (preguntas_SM.get(j) != null) {
								respuesta.add(preguntas_SM.get(j).getRespuestaAlumno());
								break;
							}
						}
					}

				}
			}
			
			if (verificar_examen(2)) {
				for (int i = 0; i < preguntas_VF_Backup.size(); i++) {
					for (int j = 0; j < preguntas_VF_Backup.size(); j++) {
						if (preguntas_VF_Backup.get(i).getPregunta().equalsIgnoreCase(preguntas_VF.get(j).getPregunta())) {
							if (preguntas_VF.get(j) != null) {
								respuesta.add(preguntas_VF.get(j).getRespuestaAlumno());
								break;
							}
						}
					}
				}
			}
			
			if (verificar_examen(3)) {
				for (int i = 0; i < preguntas_RC_Backup.size(); i++) {
					for (int j = 0; j < preguntas_RC_Backup.size(); j++) {
						if (preguntas_RC_Backup.get(i).getPregunta().equalsIgnoreCase(preguntas_RC.get(j).getPregunta())) {
							if (preguntas_RC.get(j) != null) {
								respuesta.add(preguntas_RC.get(j).getRespuestaAlumno());
								break;
							}
						}
					}
				}
			}
		}
		return respuesta.toArray(new String[0]);
	}

	/**
	 * esta funcion se encarga de preguntarle al profesor que tipo de preguntas
	 * desea agregar al examen.
	 */
	static void Agregar_Preguntas_Examen() {

		Examen miexamen = new Examen();
		Boolean continuar = true;

		do {
			System.out.println("\nPreguntas de que tipo desea ingresar?, por ahora solo tengo disponible las siguientes: \nSeleccion Multiple (si quieres usar esta opcion usa SM) \nVerdadero y Falso (si quieres usar esta opcion usa VF) \nRespuestas Cortas (si quieres usar esta opcion usa RC)\n\nsi deseas volver, solo escribe 'volver' (sin las '')\n");
			switch (Texto.leer_teclado().toLowerCase()) {

			case "sm":// agregar pregunta seleccion multiple
				do {

					miexamen.agregaPregunta(SeleccionMultiple.crearPreguntaSM());

					System.out.println("\n¿deseas seguir agregando preguntas?, 1 = si, 0 = no\n");

					if (!Texto.leer_teclado().equals("1")) {
						continuar = false;
					}
				} while (continuar);

				System.out.println("\n\n\nvista previa de las preguntas: ");

				miexamen.mostrarPreguntas(1);
				continuar = true;
				break;
				
			case "vf":// agregar pregunta verdadero falso
				do {

					miexamen.agregaPregunta(VerdaderoFalso.crearPreguntaVF());

					System.out.println("\n¿deseas seguir agregando preguntas?, 1 = si, 0 = no\n");
					if (!Texto.leer_teclado().equals("1")) {
						continuar = false;
					}
				}while (continuar);

				System.out.println("\n\n\nvista previa de las preguntas: ");

				miexamen.mostrarPreguntas(2);
				continuar = true;
				break;

			case "rc":// agregar pregunta respuesta corta
				do {

					miexamen.agregaPregunta(RespuestasCortas.crearPreguntaRC());

					System.out.println("\n¿deseas seguir agregando preguntas?, 1 = si, 0 = no\n");
					if (!Texto.leer_teclado().equals("1")) {
						continuar = false;
					} 
				} while (continuar);

				System.out.println("\n\n\nvista previa de las preguntas: ");

				miexamen.mostrarPreguntas(3);
				continuar = true;
				break;

			case "volver":// salir del menu
				String nombre_archivo = null;
				if (miexamen.verificar_examen(1) == true || miexamen.verificar_examen(2) == true || miexamen.verificar_examen(3) == true) {
					System.out.println("\n\nuna cosa antes de salir!, necesito que me digas el nombre del archivo de la prueba. \n(si no deseas guardar nada, escribe \"salir\")\n\n");
					nombre_archivo = Texto.leer_teclado();
					if (!nombre_archivo.equalsIgnoreCase("salir")) {
						miexamen.convertir_a_smr(nombre_archivo);
					}
				}
				return;
			default:
				System.out.println("opcion invalida, intentelo de nuevo.");
				continuar = true;
			}
		} while (continuar);
	}

	/**
	 * esta funcion se encarga de eliminar las preguntas elegidas por el profesor
	 * del examen.
	 */
	static void Eliminar_Preguntas_Examen() {
		
		boolean continuar = true;
		boolean borrado_correcto = true;
		
		System.out.println("\n\nAntes de borrar preguntas, necesito que me digas si estas seguro de que quieres borrar preguntas (si o no):");

		if (Texto.leer_teclado().equalsIgnoreCase("si")) { // dejo todo metido dentro de un if, para evitar posibles errores al borrar preguntas.
			try {
				System.out.println("\n\nahora necesito que me digas la ubicacion en donde dejaste la prueba:");
				String respuesta = Texto.leer_teclado();
				Examen miExamen = new Examen();
				int index = 0;
				
				miExamen = Archivos.Interpretar_Archivo(respuesta);
				String Texto_Ingresado = null;
				if (miExamen != null) {
					if (miExamen.verificar_examen(1) == true) {
						do {
							System.out.println("\n\nse mostraran las preguntas de tipo Seleccion multiple.\n");
							miExamen.mostrarPreguntas(1);
							System.out.print("\n\nahora necesito que me digas que pregunta quieres eliminar (si no quieres borrar nada, solo escribe 'salir'): ");

							try {
								
								Texto_Ingresado = Texto.leer_teclado();
								
								if (Texto_Ingresado.equalsIgnoreCase("salir")) {
									continuar = false;
								}
								
								if (continuar) {
									index = Integer.parseInt(Texto_Ingresado)-1;
									// mientras haya una pregunta y esté dentro del rango
									if ((miExamen.obtener_longitud(1) > 0) && (index >= 0 && index < miExamen.obtener_longitud(1))) {
										miExamen.eliminaPregunta(index, "sm");
										borrado_correcto = true;
									} else {
										System.err.println("\n\npor favor use valores entre (1 y " + (miExamen.obtener_longitud(1)) + ")\n");
										borrado_correcto = false;
									}

									if (miExamen.obtener_longitud(1) < 1) {
										System.out.println("\n\nno hay mas preguntas que eliminar.");
										continuar = false;
									}
									
									//si se puede continuar y puede seguir borrando...
									if (continuar && borrado_correcto) {
										System.out.println("\n\ndeseas borrar otra pregunta de seleccion multiple?, escribe 'seguir' para continuar borrando,\no cualquier otra cosa para salir de borrar seleccion multiple.");
										if (!Texto.leer_teclado().equalsIgnoreCase("seguir")) {
											continuar = false;
										}
									}
								}
							} catch (Exception e) {
								System.err.println("\n\npor favor, ingrese numeros, las letras no sirven.\n\n");
							}
						} while (continuar);
					}
					
					continuar = true;
					borrado_correcto = true;
					
					if (miExamen.verificar_examen(2) == true) {
						do {
							System.out.println("\n\nse mostraran las preguntas de tipo verdadero falso.");
							miExamen.mostrarPreguntas(2);
							System.out.print("\n\nahora necesito que me digas que pregunta quieres eliminar (si no quieres borrar nada, solo escribe 'salir'): ");
							
							try {
								
								Texto_Ingresado = Texto.leer_teclado();
								
								if (Texto_Ingresado.equalsIgnoreCase("salir")) {
									continuar = false;
								}
								
								if (continuar) {
									index = Integer.parseInt(Texto_Ingresado) - 1;
									
									// mientras haya una pregunta y este dentro del rango
									if ((miExamen.obtener_longitud(2) > 0) && (index >= 0 && index < miExamen.obtener_longitud(2))) { 
										miExamen.eliminaPregunta(index, "vf");
										borrado_correcto = true;
									} else {
										System.err.println("\n\npor favor use valores entre (1 y " + (miExamen.obtener_longitud(2)) + ")\n");
										borrado_correcto = false;
									}

									if (miExamen.obtener_longitud(2) < 1) {
										System.out.println("\n\nno hay mas preguntas que eliminar.\n\n");
										continuar = false;
									}

									if (continuar && borrado_correcto) {
										System.out.println("\n\ndeseas borrar otra pregunta de verdadero falso?, escribe 'seguir' para continuar borrando,\no cualquier otra cosa para salir de borrar verdadero falso.");
										if (!Texto.leer_teclado().equalsIgnoreCase("seguir")) {
											continuar = false;
										}	
									}
								}
							} catch (Exception e) {
								System.err.println("\n\npor favor, ingrese numeros, las letras no sirven.\n\n");
							}
						} while (continuar);
					}
					
					continuar = true;
					borrado_correcto = true;
					
					if (miExamen.verificar_examen(3) == true) {
						do {
							System.out.println("\n\nse mostraran las preguntas de tipo respuesta corta.");
							miExamen.mostrarPreguntas(3);
							System.out.print("\n\nahora necesito que me digas que pregunta quieres eliminar (si no quieres borrar nada, solo escribe 'salir'): ");
							
							try {
								
								Texto_Ingresado = Texto.leer_teclado();
								
								if (Texto_Ingresado.equalsIgnoreCase("salir")) {
									continuar = false;
								}
								
								if (continuar) {
									index = Integer.parseInt(Texto_Ingresado)-1;
									if ((miExamen.obtener_longitud(3) > 0) && (index >= 0 && index <= miExamen.obtener_longitud(3))) { // mientras haya una pregunta y esté dentro del rango
										miExamen.eliminaPregunta(index, "rc");
										borrado_correcto = true;
									} else {
										System.err.println("\n\npor favor use valores entre (0 y " + (miExamen.obtener_longitud(3)) + ")\n");
										borrado_correcto = false;
									}

									if (miExamen.obtener_longitud(3) < 1) {
										System.out.println("\n\nno hay mas preguntas que eliminar.\n\n");
										continuar = false;
									}

									if (continuar && borrado_correcto) {
										System.out.println("\n\ndeseas borrar otra pregunta de respuesta corta?, escribe 'seguir' para continuar borrando,\no cualquier otra cosa para salir de borrar respuesta corta.");
										if (!Texto.leer_teclado().equalsIgnoreCase("seguir")) {
											continuar = false;
										}	
									}
								}
							} catch (Exception e) {
								System.err.println("\n\npor favor, ingrese numeros, las letras no sirven.\n\n");
							}
						} while (continuar);
					}

					if (miExamen.obtener_longitud(1) > 0 || miExamen.obtener_longitud(2) > 0 || miExamen.obtener_longitud(3) > 0) {
						System.out.println("\n\nuna cosa antes de salir!, necesito que me digas el nombre del archivo de la prueba. \n(si no deseas guardar nada, escribe \"salir\")\n\n");
						String nombre_archivo = Texto.leer_teclado();
						if (!nombre_archivo.equalsIgnoreCase("salir")) {
							miExamen.convertir_a_smr(nombre_archivo);
						}
					} else {
						System.err.println("\n\ntodas las preguntas han sido borradas!, no se guardara un documento vacio!\n\n");
					}
				} else {
					System.err.println("\n\nno hay ninguna pregunta en el archivo!\n\n");
				}
			} catch (IllegalArgumentException e) {
				System.err.println(e.getMessage());
			}
		}
		return;
	}
		
	/** 
	 * Este es el metodo que da inicio al examen.
	 * @return retorna el puntaje total de alumno.
	 */	
	int darExam() {
		int puntaje_total = 0;

		if (Desordenar == true) {
			
			//hago una 'copia de seguridad', para no perder el orden de las preguntas.			

			if (verificar_examen(1)) {
				preguntas_SM_Backup = new ArrayList<SeleccionMultiple>(preguntas_SM);
			}
			if (verificar_examen(2)) {
				preguntas_VF_Backup = new ArrayList<VerdaderoFalso>(preguntas_VF);
			}
			if (verificar_examen(3)) {
				preguntas_RC_Backup = new ArrayList<RespuestasCortas>(preguntas_RC);
			}

			puntaje_total = desordenar_todo();

		} else {

			long TiempoExamen = 0;
			
			int contador = 0;
			
			if (TiempoLimite == true) {
				TiempoExamen = System.currentTimeMillis();
			}
			
			if (verificar_examen(1)) {
				for (int i = 0; i < preguntas_SM.size(); i++) {
					
					if ((TiempoLimite == true) && ((System.currentTimeMillis() - TiempoExamen) > TiempoMaximo)) {
						System.err.println("\n\nSE ACABO EL TIEMPO!\n\n");
						return puntaje_total;
					}

					preguntas_SM.get(i).setNumeroPregunta(contador++);
					preguntas_SM.get(i).setMostrarRespuestas(MostrarRespuestas);
					if (preguntas_SM.get(i).buscar() == true) {
						puntaje_total += preguntas_SM.get(i).getPeso();
					}
				}
			}

			if (verificar_examen(2)) {
				for (int i = 0; i < preguntas_VF.size(); i++) {
					
					if ((TiempoLimite == true) && ((System.currentTimeMillis() - TiempoExamen) > TiempoMaximo)) {
						System.err.println("\n\nSE ACABO EL TIEMPO!\n\n");
						return puntaje_total;
					}
					
					preguntas_VF.get(i).setNumeroPregunta(contador++);
					preguntas_VF.get(i).setMostrarRespuestas(MostrarRespuestas);
					if (preguntas_VF.get(i).buscar() == true) {
						puntaje_total += preguntas_VF.get(i).getPeso();
					}
				}
			}

			if (verificar_examen(3)) {
				for (int i = 0; i < preguntas_RC.size(); i++) {
					
					if ((TiempoLimite == true) && ((System.currentTimeMillis() - TiempoExamen) > TiempoMaximo)) {
						System.err.println("\n\nSE ACABO EL TIEMPO!\n\n");
						return puntaje_total;
					}
					
					preguntas_RC.get(i).setNumeroPregunta(contador++);
					preguntas_RC.get(i).setMostrarRespuestas(MostrarRespuestas);
					if (preguntas_RC.get(i).buscar() == true) {
						puntaje_total += preguntas_RC.get(i).getPeso();
					}
				}
			}
		}
		return puntaje_total;
	}
}
