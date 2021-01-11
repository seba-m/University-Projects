package base;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Esta clase se encarga de hacer todo lo relacionado con el examen (agregar
 * preguntas, responder la prueba, etc).
 * 
 * @author Sebastian Morgado
 * @version 2.0.0
 * @since 0.5.0
 */

public class Examen {

	/**
	 * la variable preguntas_SM (preguntas seleccion multiple) almacena todas las
	 * preguntas de tipo seleccion multiple.
	 */
	public List<SeleccionMultiple> preguntas_SM = new ArrayList<SeleccionMultiple>();

	/**
	 * la variable preguntas_RC (preguntas Respuestas Cortas) almacena todas las
	 * preguntas de tipo Respuestas Cortas.
	 */
	public List<RespuestasCortas> preguntas_RC = new ArrayList<RespuestasCortas>();

	/**
	 * la variable preguntas_VF (preguntas Verdadero Falso) almacena todas las
	 * preguntas de tipo Verdadero Falso.
	 */
	public List<VerdaderoFalso> preguntas_VF = new ArrayList<VerdaderoFalso>();

	/**
	 * la variable preguntas_RM (preguntas Respuestas Numericas) almacena todas las
	 * preguntas de tipo Respuestas Numericas.
	 */
	public List<RespuestaNumerica> preguntas_RM = new ArrayList<RespuestaNumerica>();

	/**
	 * la variable preguntas_SM_Backup (preguntas seleccion multiple copia de
	 * seguridad) almacena todas las preguntas de tipo seleccion multiple, con el
	 * fin de que estas no seran desordenadas.
	 */
	private List<SeleccionMultiple> preguntas_SM_Backup;

	/**
	 * la variable preguntas_RC_Backup (preguntas Respuestas Cortas copia de
	 * seguridad) almacena todas las preguntas de tipo Respuestas Cortas, con el fin
	 * de que estas no seran desordenadas.
	 */
	private List<RespuestasCortas> preguntas_RC_Backup;

	/**
	 * la variable preguntas_VF_Backup (preguntas Verdadero Falso copia de
	 * seguridad) almacena todas las preguntas de tipo Verdadero Falso, con el fin
	 * de que estas no seran desordenadas.
	 */
	private List<VerdaderoFalso> preguntas_VF_Backup;

	/**
	 * la variable preguntas_RM_Backup (preguntas Respuestas Numericas copia de
	 * seguridad) almacena todas las preguntas de tipo Respuestas Numericas, con el
	 * fin de que estas no seran desordenadas.
	 */
	private List<RespuestaNumerica> preguntas_RM_Backup = new ArrayList<RespuestaNumerica>();

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
	public boolean TiempoLimite = false;

	/**
	 * esta variable es el orden por defecto en que se mostrarán los items. si el
	 * profesor elije desordenar todo, el orden en este arreglo igual cambiará.
	 */
	private String[] ordenPreguntas = { "Selección Multiple", "Verdadero Falso", "Respuestas Cortas",
			"Respuestas Numericas" };

	/**
	 * esta variable sirve para almacenar el tiempo maximo que tendra el alumno para
	 * responder el examen.
	 */
	private long TiempoMaximo = 0;

	/**
	 * Esta llave es la que se usara al momento de guardar los resultados de los
	 * alumnos.
	 */
	private String llave;

	/**
	 * Este hash, es el que sirve para identificar un examen de otro examen.
	 */
	private String hashExamen;

	/**
	 * Esta funcion se agregar preguntas de tipo respuesta corta al examen.
	 * 
	 * @param Pregunta_Respuesta_Corta es la pregunta que se desea agregar (esta
	 *                                 debe tener la pregunta, la respuesta y el
	 *                                 puntaje que vale)
	 */
	public void agregaPregunta(RespuestasCortas Pregunta_Respuesta_Corta) {
		preguntas_RC.add(Pregunta_Respuesta_Corta);
		return;
	}

	/**
	 * Esta funcion se agregar preguntas de tipo seleccion multiple al examen.
	 * 
	 * @param Pregunta_Seleccion_Multiple es la pregunta que se desea agregar (esta
	 *                                    debe tener la pregunta, las opciones, la
	 *                                    respuesta y el puntaje que vale)
	 */
	public void agregaPregunta(SeleccionMultiple Pregunta_Seleccion_Multiple) {
		preguntas_SM.add(Pregunta_Seleccion_Multiple);
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
	public void agregaPregunta(VerdaderoFalso Pregunta_Verdadero_Falso) {
		preguntas_VF.add(Pregunta_Verdadero_Falso);
		return;
	}

	/**
	 * Esta funcion se agregar preguntas de tipo respuesta numerica al examen.
	 * 
	 * @param Pregunta_Respuesta_Numerica es la pregunta que se desea agregar (esta
	 *                                 debe tener la pregunta, la respuesta y el
	 *                                 puntaje que vale)
	 */
	public void agregaPregunta(RespuestaNumerica Pregunta_Respuesta_Numerica) {
		preguntas_RM.add(Pregunta_Respuesta_Numerica);
		return;
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
	public int calcular_nota(double puntaje, double porcentaje) {
		double nota_minima = 10;
		double nota_aprobacion = 40;
		double nota_maxima = 70;
		double puntaje_maximo = obtener_puntaje_total();
		double puntaje_minimo = 0;
		double puntaje_aprobacion = (puntaje_maximo * porcentaje) / 100;

		double nota_final = 0;

		// para obtener la nota, uso la ecuacion de la resta (y-y1 = m*(x-x1) , donde
		// 'y' es la nota, y 'x' es el puntaje obtenido).
		if (puntaje >= puntaje_aprobacion && puntaje <= puntaje_maximo) {
			nota_final = ((nota_aprobacion - nota_maxima) / (puntaje_aprobacion - puntaje_maximo))
					* (puntaje - puntaje_maximo) + nota_maxima;
		} else if (puntaje <= puntaje_maximo) {
			nota_final = ((nota_minima - nota_aprobacion) / (puntaje_minimo - puntaje_aprobacion))
					* (puntaje - puntaje_aprobacion) + nota_aprobacion;
		} else {
			// caso imposible en el que el alumno haya obtenido mas puntaje que el puntaje
			// total de la prueba.
			return 10;
		}

		return (int) (Math.round(nota_final));
	}

	/**
	 * Esta funcion se encarga de desordenar las preguntas del examen.
	 */
	private void desordenar_preguntas() {
		if (verificarExamen(1)) {
			Collections.shuffle(preguntas_RC);
		}
		if (verificarExamen(2)) {
			Collections.shuffle(preguntas_VF);
		}
		if (verificarExamen(3)) {
			Collections.shuffle(preguntas_SM);
		}
		if (verificarExamen(4)) {
			Collections.shuffle(preguntas_RM);
		}
	}

	/**
	 * Esta función se encarga de desordenar los 4 ítems del examen y preguntar.
	 */
	void desordenar_todo() {

		if (Desordenar) {
			if (verificarExamen(1)) {
				preguntas_SM_Backup = new ArrayList<SeleccionMultiple>(preguntas_SM);
			}
			if (verificarExamen(2)) {
				preguntas_VF_Backup = new ArrayList<VerdaderoFalso>(preguntas_VF);
			}
			if (verificarExamen(3)) {
				preguntas_RC_Backup = new ArrayList<RespuestasCortas>(preguntas_RC);
			}
			if (verificarExamen(4)) {
				preguntas_RM_Backup = new ArrayList<RespuestaNumerica>(preguntas_RM);
			}

			// primero desordeno las preguntas que tienen las listas
			desordenar_preguntas();

			// luego desordeno el orden de los items

			List<String> orden = new ArrayList<String>();
			for (int i = 0; i < ordenPreguntas.length; i++) {
				orden.add(ordenPreguntas[i]);
			}

			Collections.shuffle(orden);

			for (int i = 0; i < orden.size(); i++) {
				ordenPreguntas[i] = orden.get(i);
			}
		}
	}

	/**
	 * esta funcion se encarga de verificar si se deben o no desordenar los items,
	 * preguntas y opciones.
	 * 
	 * @return retorna {@code true} si el profesor ha elegido que se desordene todo,
	 *         caso contrario, retorna {@code false}.
	 */
	public boolean getDesordenar() {
		return Desordenar;
	}

	/**
	 * esta funcion se encarga de obtener el hash (o identificador) del examen (la
	 * cual se usara para identificar a los resultados de los alumnos).
	 * 
	 * @return retorna el hash del examen.
	 */
	public String getHash() {
		return hashExamen;
	}

	/**
	 * esta funcion se encarga de obtener la llave del examen (la cual se usara para
	 * ofuscar los resultados de los alumnos).
	 * 
	 * @return retorna la llave del examen.
	 */
	public String getLlave() {
		return llave;
	}

	/**
	 * esta funcion se encarga de obtener el tiempo limite del examen.
	 * 
	 * @return retorna el tiempo limite.
	 */
	public boolean getMostrarRespuestas() {
		return MostrarRespuestas;
	}

	/**
	 * esta funcion se encarga de obtener el orden de los tipos de pregunta del
	 * examen.
	 * 
	 * @return retorna el orden en que se mostraran las preguntas.
	 */
	public String[] getOrdenPreguntas() {
		return ordenPreguntas;
	}

	/**
	 * esta funcion se encarga de obtener el tiempo limite del examen.
	 * 
	 * @return retorna el tiempo limite.
	 */
	public long getTiempoLimite() {
		return TiempoMaximo;
	}

	/**
	 * Esta funcion la hice solo para (tal como lo dice su nombre) obtener el
	 * puntaje total de la prueba.
	 * 
	 * @return retorna la cantidad de datos dentro de la pregunta, caso contrario,
	 *         retorna 0.
	 * 
	 */
	public int obtener_puntaje_total() {
		return (obtenerPuntajeTotalSeleccionMultiple() + obtenerPuntajeTotalVerdaderoFalso()
				+ obtenerPuntajeTotalRespuestasCortas() + obtenerPuntajeTotalRespuestaNumerica());
	}

	/**
	 * Esta funcion se encarga de obtener las respuestas del usuario.
	 * 
	 * @return retorna el puntaje total obtenido por el alumno.
	 */
	public String[] obtener_Respuestas_Usuario() {
		List<String> respuesta = new ArrayList<>();

		if (Desordenar == false) {

			if (verificarExamen(1)) {
				for (int i = 0; i < preguntas_SM.size(); i++) {
					if (preguntas_SM.get(i) != null) {
						respuesta.add(preguntas_SM.get(i).getRespuestaAlumno());
					}
				}
			}

			if (verificarExamen(2)) {
				for (int i = 0; i < preguntas_VF.size(); i++) {
					if (preguntas_VF.get(i) != null) {
						respuesta.add(preguntas_VF.get(i).getRespuestaAlumno());
					}
				}
			}

			if (verificarExamen(3)) {
				for (int i = 0; i < preguntas_RC.size(); i++) {
					if (preguntas_RC.get(i) != null) {
						respuesta.add(preguntas_RC.get(i).getRespuestaAlumno());
					}
				}
			}

			if (verificarExamen(4)) {
				for (int i = 0; i < preguntas_RM.size(); i++) {
					if (preguntas_RM.get(i) != null) {
						respuesta.add(preguntas_RM.get(i).getRespuestaAlumno());
					}
				}
			}
		} else {

			// pd: intente ordenar todo esto usando CompareTo(), pero no sirvió.

			if (verificarExamen(1)) {
				for (int i = 0; i < preguntas_SM_Backup.size(); i++) {
					for (int j = 0; j < preguntas_SM_Backup.size(); j++) {
						if (preguntas_SM_Backup.get(i).getPregunta()
								.equalsIgnoreCase(preguntas_SM.get(j).getPregunta())) {
							if (preguntas_SM.get(j) != null) {
								respuesta.add(preguntas_SM.get(j).getRespuestaAlumno());
								break;
							}
						}
					}

				}
			}

			if (verificarExamen(2)) {
				for (int i = 0; i < preguntas_VF_Backup.size(); i++) {
					for (int j = 0; j < preguntas_VF_Backup.size(); j++) {
						if (preguntas_VF_Backup.get(i).getPregunta()
								.equalsIgnoreCase(preguntas_VF.get(j).getPregunta())) {
							if (preguntas_VF.get(j) != null) {
								respuesta.add(preguntas_VF.get(j).getRespuestaAlumno());
								break;
							}
						}
					}
				}
			}

			if (verificarExamen(3)) {
				for (int i = 0; i < preguntas_RC_Backup.size(); i++) {
					for (int j = 0; j < preguntas_RC_Backup.size(); j++) {
						if (preguntas_RC_Backup.get(i).getPregunta()
								.equalsIgnoreCase(preguntas_RC.get(j).getPregunta())) {
							if (preguntas_RC.get(j) != null) {
								respuesta.add(preguntas_RC.get(j).getRespuestaAlumno());
								break;
							}
						}
					}
				}
			}

			if (verificarExamen(4)) {
				for (int i = 0; i < preguntas_RM_Backup.size(); i++) {
					for (int j = 0; j < preguntas_RM_Backup.size(); j++) {
						if (preguntas_RM_Backup.get(i).getPregunta().equalsIgnoreCase(preguntas_RM.get(j).getPregunta())) {
							if (preguntas_RM.get(j) != null) {
								respuesta.add(preguntas_RM.get(j).getRespuestaAlumno());
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
	 * Esta funcion se obtener los tipos de preguntas que tiene el examen.
	 * 
	 * @return retorna todos los tipos de preguntas validas que tiene el examen. si
	 *         no tiene, retornara un arreglo vacio.
	 */
	public String[] obtenerItems() {

		List<String> tiposPreguntas = new ArrayList<>();

		if (preguntas_SM != null && !preguntas_SM.isEmpty()) {
			tiposPreguntas.add("Selección Multiple");
		}

		if (preguntas_VF != null && !preguntas_VF.isEmpty()) {
			tiposPreguntas.add("Verdadero Falso");
		}

		if (preguntas_RC != null && !preguntas_RC.isEmpty()) {
			tiposPreguntas.add("Respuestas Cortas");
		}

		if (preguntas_RM != null && !preguntas_RM.isEmpty()) {
			tiposPreguntas.add("Respuestas Numericas");
		}

		return tiposPreguntas.toArray(new String[0]);
	}

	/**
	 * Esta funcion se obtener la cantidad total de preguntas de tipo respuestas
	 * cortas que tiene el examen.
	 * 
	 * @return retorna la cantidad total de preguntas. Si no tiene, retornará 0.
	 */
	public int obtenerPuntajeTotalRespuestasCortas() {

		int puntaje_total = 0;

		if (preguntas_RC != null) {
			for (RespuestasCortas preguntasRespuestasCortas : preguntas_RC) {
				puntaje_total += preguntasRespuestasCortas.getPeso();
			}
		}

		return puntaje_total;
	}

	/**
	 * Esta funcion se obtener la cantidad total de preguntas de tipo selección
	 * multiple que tiene el examen.
	 * 
	 * @return retorna la cantidad total de preguntas. Si no tiene, retornará 0.
	 */
	public int obtenerPuntajeTotalSeleccionMultiple() {

		int puntaje_total = 0;

		if (preguntas_SM != null) {
			for (SeleccionMultiple preguntasSeleccionMultiple : preguntas_SM) {
				puntaje_total += preguntasSeleccionMultiple.getPeso();
			}
		}

		return puntaje_total;
	}

	/**
	 * Esta funcion se obtener la cantidad total de preguntas de tipo verdadero
	 * falso que tiene el examen.
	 * 
	 * @return retorna la cantidad total de preguntas. Si no tiene, retornará 0.
	 */
	public int obtenerPuntajeTotalVerdaderoFalso() {

		int puntaje_total = 0;

		if (preguntas_VF != null) {
			for (VerdaderoFalso preguntasVerdaderoFalso : preguntas_VF) {
				puntaje_total += preguntasVerdaderoFalso.getPeso();
			}
		}

		return puntaje_total;
	}

	/**
	 * Esta funcion se obtener la cantidad total de preguntas de tipo respuestas
	 * numericas que tiene el examen.
	 * 
	 * @return retorna la cantidad total de preguntas. Si no tiene, retornará 0.
	 */
	public int obtenerPuntajeTotalRespuestaNumerica() {

		int puntaje_total = 0;

		if (preguntas_RM != null) {
			for (RespuestaNumerica preguntasRespuestaNumerica : preguntas_RM) {
				puntaje_total += preguntasRespuestaNumerica.getPeso();
			}
		}

		return puntaje_total;
	}

	/**
	 * Esta funcion se obtener la cantidad total de preguntas que tiene el examen.
	 * 
	 * @return retorna la cantidad total de preguntas.
	 */
	public int obtenerTotalPreguntas() {

		int total = 0;

		if (verificarExamen(1)) {
			total += preguntas_SM.size();
		}

		if (verificarExamen(2)) {
			total += preguntas_VF.size();
		}

		if (verificarExamen(3)) {
			total += preguntas_RC.size();
		}

		if (verificarExamen(4)) {
			total += preguntas_RM.size();
		}

		return total;
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
	 * Esta funcion se encarga de setear el hash del examen.
	 * 
	 * @param hash es la identificación del examen. Esto se hace con el fin de que
	 *             al enviar los resultados del alumno al profesor, no se mezclen
	 *             resultados de diferentes examenes.
	 */
	public void setHash(String hash) {
		hashExamen = hash;
	}

	/**
	 * Esta funcion se encarga de setear la llave del examen. (para ofuscar los
	 * resultados de los alumnos)
	 * 
	 * @param key es la llave obtenida del examen.
	 */
	public void setLlave(String key) {
		llave = key;
	}

	/**
	 * Esta funcion se encarga de setear la opcion MostrarRespuestas.
	 * 
	 * @param opcion si es {@code true}, mostrara las respuestas cada vez que el
	 *               alumno responda. si es {@code false}, no hara mostrara nada.
	 */
	public void setMostrarRespuestas(boolean opcion) {
		this.MostrarRespuestas = opcion;
	}

	/**
	 * Esta funcion se encarga de setear la opcion TiempoLimite.
	 * 
	 * @param opcion si es {@code true}, iniciara un contador, que a medida que
	 *               responda el alumno, ira verificando si esta dentro del limite
	 *               de tiempo que se le dio. si es {@code false}, el examen no
	 *               tendra ningun limite de tiempo.
	 * 
	 * @param tiempo si el tiempo es distinto de cero y se ha activado el limite de
	 *               tiempo, iniciara un cronometro, el cual cuando llegue a su
	 *               limite, detendra la prueba.
	 */
	public void setTiempoLimite(boolean opcion, int tiempo) { // NO_UCD (use default)
		this.TiempoLimite = opcion;
		if (opcion == true && tiempo != 0) {
			this.TiempoMaximo = tiempo * 60000;
		}
	}

	/**
	 * Esta funcion la hice solo para (tal como lo dice su nombre) verificar si el
	 * examen tiene alguna pregunta o no.
	 * 
	 * @param tipo es el tipo de pregunta que se desea verificar si tiene preguntas
	 *             o no. 1 es seleccion multiple, 2 es verdadero falso, 3 es
	 *             respuesta corta y 4 es respuesta numerica.
	 * @return retorna verdadero si la lista de preguntas no esta vacia, si se da el
	 *         caso contrario, retorna falso.
	 * 
	 */
	public boolean verificarExamen(int tipo) {
		if (tipo == 1) {
			return (preguntas_SM != null && !preguntas_SM.isEmpty());
		}
		if (tipo == 2) {
			return (preguntas_VF != null && !preguntas_VF.isEmpty());
		}
		if (tipo == 3) {
			return (preguntas_RC != null && !preguntas_RC.isEmpty());
		}
		if (tipo == 4) {
			return (preguntas_RM != null && !preguntas_RM.isEmpty());
		}
		return false;
	}
}
