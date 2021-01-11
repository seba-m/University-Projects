package main;

import java.util.*;

/**
 * Esta clase se encarga de hacer todo lo relacionado con el examen (agregar preguntas, responder la prueba, etc).
 * @author Sebastian Morgado
 *
 */
class Exam {

	/**
	 * la variable preguntas_SM (preguntas seleccion multiple) almacena
	 * todas las preguntas de tipo seleccion multiple.
	 */
	private List<Selec_Mul_Pregunta> preguntas_SM  = new ArrayList<Selec_Mul_Pregunta>(5);
	
	/**
	 * la variable preguntas_RC (preguntas respuesta corta) almacena
	 * todas las preguntas de tipo respuesta corta.
	 */
	private List<Resp_Cortas_Pregunta> preguntas_RC  = new ArrayList<Resp_Cortas_Pregunta>(5);
	
	/**
	 * la variable preguntas_TF (preguntas veradero falso) almacena
	 * todas las preguntas de tipo veradero falso.
	 */
	private List<TFpregunta> preguntas_TF  = new ArrayList<TFpregunta>(5);
	
	/**
	 * Esta funcion se agregar preguntas de tipo seleccion multiple al examen.
	 * 
	 * @param selec_Mul_Pregunta es la pregunta que se desea agregar.
	 */
	void agregaPregunta(Selec_Mul_Pregunta selec_Mul_Pregunta) {
			preguntas_SM.add(selec_Mul_Pregunta);
			return;
	}
	
	/**
	 * Esta funcion se agregar preguntas de tipo respuesta corta al examen.
	 * 
	 * @param resp_Cortas_Pregunta es la pregunta que se desea agregar.
	 */
	void agregaPregunta(Resp_Cortas_Pregunta resp_Cortas_Pregunta) {
			preguntas_RC.add(resp_Cortas_Pregunta);
			return;
	}
	
	/**
	 * Esta funcion se agregar preguntas de tipo respuesta verdadero y falso al examen.
	 * 
	 * @param tFpregunta es la pregunta que se desea agregar.
	 */
	void agregaPregunta(TFpregunta tFpregunta) {
			preguntas_TF.add(tFpregunta);
			return;
	}
	
	/**
	 * Esta funcion se encarga de dar el examen.
	 * 
	 * @return retorna el puntaje total obtenido por el alumno.
	 */
	int darExam() {
		int puntaje_total = 0;
		
		for (int i = 0; i < preguntas_SM.size(); i++) {
				if (preguntas_SM.get(i).buscar() == true) {
					puntaje_total += preguntas_SM.get(i).getPeso();
				}
		}

		for (int i = 0; i < preguntas_RC.size(); i++) {
				if (preguntas_RC.get(i).buscar() == true) {
					puntaje_total += preguntas_RC.get(i).getPeso();
				}
		}

		for (int i = 0; i < preguntas_TF.size(); i++) {
				if (preguntas_TF.get(i).buscar() == true) {
					puntaje_total += preguntas_TF.get(i).getPeso();
				}
		}

		return puntaje_total;
	}
}