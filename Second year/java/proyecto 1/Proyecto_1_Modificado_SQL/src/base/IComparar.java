package base;

import java.util.*;

/**
 * Esta interface tiene por objetivo poder ordenar los datos obtenidos de los
 * archivos creados por los alumnos.
 * 
 * @author Sebastian Morgado
 * @version 1.6.1
 * @since 1.5.0
 */
interface IComparar extends Comparator<RevisionAlumno> {

	/**
	 * Esta clase comparadora se encarga de ordenar las notas de menor a mayor.
	 */
	class Comparador_Nota_Menor_Mayor implements Comparator<RevisionAlumno> {
		@Override
		public int compare(RevisionAlumno e1, RevisionAlumno e2) {
			return Integer.parseInt(e1.getNota().replaceAll(",", "")) - Integer.parseInt(e2.getNota().replaceAll(",", ""));
		}
	}

	/**
	 * Esta clase comparadora se encarga de ordenar las notas de mayor a menor.
	 */
	class Comparador_Nota_Mayor_Menor implements Comparator<RevisionAlumno> {
		@Override
		public int compare(RevisionAlumno e1, RevisionAlumno e2) {
			return Integer.parseInt(e2.getNota().replaceAll(",", ""))
					- Integer.parseInt(e1.getNota().replaceAll(",", ""));
		}
	}

	/**
	 * Esta clase comparadora se encarga de ordenar los nombres de mayor a menor.
	 */
	class Comparador_Nombre_Mayor_Menor implements Comparator<RevisionAlumno> {
		@Override
		public int compare(RevisionAlumno e1, RevisionAlumno e2) {
			return e1.getNombre_Alumno().compareTo(e2.getNombre_Alumno());
		}
	}

	/**
	 * Esta clase comparadora se encarga de ordenar los nombres de menor a mayor.
	 */
	class Comparador_Nombre_Menor_Mayor implements Comparator<RevisionAlumno> {

		@Override
		public int compare(RevisionAlumno e1, RevisionAlumno e2) {
			return e2.getNombre_Alumno().compareTo(e1.getNombre_Alumno());
		}
	}

	/**
	 * Esta clase comparadora se encarga de ordenar los puntajes de menor a mayor.
	 */
	class Comparador_Puntaje_Menor_Mayor implements Comparator<RevisionAlumno> {
		@Override
		public int compare(RevisionAlumno e1, RevisionAlumno e2) {
			return Integer.parseInt(e1.getPuntaje_Total()) - Integer.parseInt(e2.getPuntaje_Total());
		}
	}

	/**
	 * Esta clase comparadora se encarga de ordenar los puntajes de mayor a menor.
	 */
	class Comparador_Puntaje_Mayor_Menor implements Comparator<RevisionAlumno> {
		@Override
		public int compare(RevisionAlumno e1, RevisionAlumno e2) {
			return Integer.parseInt(e2.getPuntaje_Total()) - Integer.parseInt(e1.getPuntaje_Total());
		}
	}

}
