package base;

import java.util.ArrayList;
import java.util.List;

import org.javatuples.Triplet;

import utilidadesSeguridad.BASE64;

/**
 * Esta clase se encarga de tomar todos los resultados de los alumnos, y poder
 * manejar la informacion de acuerdo a las necesidades.
 * 
 * @author Sebastian Morgado
 * @version 2.0.0
 * @since 1.5.0
 */
public class RevisionAlumno {

	/**
	 * la variable nombreAlumno almacena el nombre del alumno.
	 */
	private String nombreAlumno;

	/**
	 * la variable apellidoAlumno almacena el apellido del alumno.
	 */
	private String apellidoAlumno;

	/**
	 * la variable rut almacena el rut del alumno.
	 */
	private String rut;

	/**
	 * la variable respuestas almacena en un arreglo todas las respuestas del
	 * alumno.
	 */
	private String[] respuestas;

	/**
	 * la variable Puntaje_Total almacena el puntaje total del alumno.
	 */
	private String Puntaje_Total;

	/**
	 * la variable Nota almacena la nota del alumno.
	 */
	private String Nota;

	/**
	 * la variable TiempoInicio almacena el tiempo en que el alumno inició el
	 * examen.
	 */
	private String TiempoInicio;

	/**
	 * la variable TiempoFinal almacena el tiempo en que el alumno finalizó el
	 * examen.
	 */
	private String TiempoFinal;

	/**
	 * la variable TiempoDesarrollo almacena el tiempo de desarrollo del alumno.
	 */
	private String TiempoDesarrollo;

	/**
	 * este es el constructor que toma el nombre, las respuestas, el puntaje y la
	 * nota, y lo convierte a tipo RevisionAlumno
	 * 
	 * @param nombre           es el nombre del alumno.
	 * @param apellido         es el apellido del alumno.
	 * @param rut              es el rut del alumno.
	 * @param respuestas       es un array que contiene todas las respuestas del
	 *                         alumno.
	 * @param puntaje          es el puntaje que obtuvo el alumno.
	 * @param nota             es la nota final del alumno.
	 * @param tiempoInicio     es el tiempo en el que el alumno inició el examen.
	 * @param tiempoFinal      es el tiempo en el que el alumno finalizó el examen.
	 * @param tiempoDesarrollo es el tiempo en el que el alumno desarrolló el
	 *                         examen.
	 */
	private RevisionAlumno(String nombre, String apellido, String rut, String[] respuestas, String puntaje, String nota,
			String tiempoInicio, String tiempoFinal, String tiempoDesarrollo) {
		this.nombreAlumno = nombre;
		this.apellidoAlumno = apellido;
		this.rut = rut;
		this.respuestas = respuestas;
		this.Puntaje_Total = puntaje;
		this.Nota = nota;
		this.TiempoInicio = tiempoInicio;
		this.TiempoFinal = tiempoFinal;
		this.TiempoDesarrollo = tiempoDesarrollo;
	}

	/**
	 * Este es un getter, el cual retorna la nota.
	 * 
	 * @return retorna la nota.
	 */
	public String getApellido() {
		return this.apellidoAlumno;
	}

	/**
	 * Este es un getter, el cual retorna el nombre del alumno.
	 * 
	 * @return retorna el nombre del alumno.
	 */
	public String getNombreAlumno() {
		return this.nombreAlumno;
	}

	/**
	 * Este es un getter, el cual retorna la nota.
	 * 
	 * @return retorna la nota.
	 */
	public String getNota() {
		return this.Nota;
	}

	/**
	 * Este es un getter, el cual retorna el puntaje total.
	 * 
	 * @return retorna el puntaje total.
	 */
	public String getPuntaje_Total() {
		return this.Puntaje_Total;
	}

	/**
	 * Este es un getter, el cual retorna el puntaje total.
	 * 
	 * @return retorna el puntaje total.
	 */
	public String[] getRespuestas() {
		return this.respuestas;
	}

	/**
	 * Este es un getter, el cual retorna la nota.
	 * 
	 * @return retorna la nota.
	 */
	public String getRut() {
		return this.rut;
	}

	/**
	 * Este es un getter, el cual retorna el tiempo inicial.
	 * 
	 * @return retorna el tiempo inicial.
	 */
	public String getTiempoInicio() {
		return this.TiempoInicio;
	}

	/**
	 * Este es un getter, el cual retorna el tiempo final.
	 * 
	 * @return retorna el tiempo final.
	 */
	public String getTiempoFinal() {
		return this.TiempoFinal;
	}

	/**
	 * Este es un getter, el cual retorna el tiempo de desarrollo.
	 * 
	 * @return retorna el tiempo de desarrollo.
	 */
	public String getTiempoDesarrollo() {
		return this.TiempoDesarrollo;
	}

	/**
	 * Este metodo se encarga de ordenar los datos, en el sentido que los toma todos
	 * los archivos, extrae los datos, y los guarda en una variable de tipo
	 * "RevisionAlumno", para mas adelante poder manejar la informacion extraida.
	 * 
	 * @param Ubicacion      es la ubicacion de la carpeta con todos los archivos.
	 * @param ubicacionLlave es la ubicación en donde se encuentra la llave del
	 *                       examen.
	 * @return retorna una lista con los datos extraidos de los archivos de los
	 *         alumnos.
	 */
	public static Triplet<List<RevisionAlumno>, Integer, String> obtenerDatosAlumnos(String Ubicacion,
			String ubicacionLlave) {

		List<RevisionAlumno> todoAlumno = new ArrayList<>();

		List<String[]> DatosAlumno = null;

		String idExamen = null;

		String respuestas = null;

		int cantidadPreguntas = 0;

		try {
			Triplet<List<String[]>, String, String> datosCarpeta = Archivos.LeerDatosDeCarpeta(Ubicacion,
					ubicacionLlave);

			DatosAlumno = datosCarpeta.getValue0();

			String valoresLlaveProfesor[] = BASE64.DesprotegerTextoBase64(datosCarpeta.getValue1()).split(" ");

			idExamen = valoresLlaveProfesor[0];

			cantidadPreguntas = Integer.parseInt(valoresLlaveProfesor[1]);

			respuestas = valoresLlaveProfesor[2];

		} catch (Exception e) {
			throw new IllegalArgumentException(
					"No se ha podido abrir/leer la llave. ¿Está seguro que no la ha modificado, o es valida?");
		}

		for (int i = 0; i < DatosAlumno.size(); i++) {

			String[] respuestas_usuario = new String[DatosAlumno.get(i).length - 8];

			if (DatosAlumno.get(i)[1].equals(idExamen)) {

				for (int j = 5; j < DatosAlumno.get(i).length - 6; j++) {
					respuestas_usuario[j - 5] = DatosAlumno.get(i)[j];
				}

				// todoAlumno.add(nombre, apellido, rut, respuestas[], puntaje, nota, tiempo
				// inicio, tiempo final, tiempo desarrollo);
				todoAlumno.add(new RevisionAlumno(DatosAlumno.get(i)[2], DatosAlumno.get(i)[3], DatosAlumno.get(i)[4],
						respuestas_usuario, DatosAlumno.get(i)[DatosAlumno.get(i).length - 6],
						DatosAlumno.get(i)[DatosAlumno.get(i).length - 5],
						DatosAlumno.get(i)[DatosAlumno.get(i).length - 4],
						DatosAlumno.get(i)[DatosAlumno.get(i).length - 3],
						DatosAlumno.get(i)[DatosAlumno.get(i).length - 2]));
			} else {
				// añadir a un nuevo logger los examenes que no son correctos, o en su defecto,
				// moverlos a otra carpeta.
			}
		}
		return new Triplet<List<RevisionAlumno>, Integer, String>(todoAlumno, cantidadPreguntas, respuestas);
	}
}