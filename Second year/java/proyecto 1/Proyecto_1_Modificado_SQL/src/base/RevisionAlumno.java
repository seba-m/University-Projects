package base;

import java.util.*;

/**
 * Esta clase se encarga de tomar todos los resultados de los alumnos, y poder
 * manejar la informacion de acuerdo a las necesidades.
 * 
 * @author Sebastian Morgado
 * @version 1.6.1
 * @since 1.5.0
 */
class RevisionAlumno {

	/**
	 * la variable Nombre_Alumno almacena el nombre del alumno.
	 */
	private String Nombre_Alumno;
	
	/**
	 * la variable Respuestas almacena en un arreglo todas las respuestas del
	 * alumno.
	 */
	private String[] Respuestas;
	
	/**
	 * la variable Puntaje_Total almacena el puntaje total del alumno.
	 */
	private String Puntaje_Total;
	
	/**
	 * la variable Nota almacena la nota del alumno.
	 */
	private String Nota;

	/**
	 * este es el constructor que toma el nombre, las respuestas, el puntaje y la
	 * nota, y lo convierte a tipo RevisionAlumno
	 * 
	 * @param nombre     es el nombre del alumno.
	 * @param Respuestas es un array que contiene todas las respuestas del alumno.
	 * @param puntaje    es el puntaje que obtuvo el alumno.
	 * @param nota       es la nota final del alumno.
	 */
	private RevisionAlumno(String nombre, String[] Respuestas, String puntaje, String nota) {
		this.Nombre_Alumno = nombre;
		this.Respuestas = Respuestas;
		this.Puntaje_Total = puntaje;
		this.Nota = nota;
	}

	/**
	 Este es un getter, el cual retorna el nombre del alumno. 
	 @return retorna el nombre del alumno.
	*/
	public String getNombre_Alumno() {
		return this.Nombre_Alumno;
	}

	/**
	 Este es un getter, el cual retorna el puntaje total. 
	 @return retorna el puntaje total.
	*/
	public String[] getRespuestas() {
		return this.Respuestas;
	}

	/**
	 Este es un getter, el cual retorna el puntaje total. 
	 @return retorna el puntaje total.
	*/
	public String getPuntaje_Total() {
		return this.Puntaje_Total;
	}

	/**
	 Este es un getter, el cual retorna la nota. 
	 @return retorna la nota.
	*/
	public String getNota() {
		return this.Nota;
	}

	/**
	 Este metodo se encarga de ordenar los datos, en el sentido que los toma todos los archivos,
	 extrae los datos, y los guarda en una variable de tipo "RevisionAlumno", para mas adelante 
	 poder manejar la informacion extraida.
	 
	 @param Ubicacion es la ubicacion de la carpeta con todos los archivos.
	 @return retorna una lista con los datos extraidos de los archivos de los alumnos.
	 */
	private static List<RevisionAlumno> OrdenarDatosAlumnos(String Ubicacion){
		
		List<RevisionAlumno> todoAlumno = new ArrayList<>();
		
		List<String[]> DatosAlumno = new ArrayList<>();
		
		try {
			DatosAlumno = Archivos.LeerDatosDeCarpeta(Ubicacion);
		} catch (Exception e) {
			throw new IllegalArgumentException(e.getMessage());
		}
		
		for (int i = 0; i < DatosAlumno.size(); i++) {
			String[] respuestas_usuario = new String[DatosAlumno.get(i).length-2];
			for (int j = 1; j < DatosAlumno.get(i).length-2; j++) {
				respuestas_usuario[j-1] = DatosAlumno.get(i)[j];
			}
			todoAlumno.add(new RevisionAlumno(DatosAlumno.get(i)[0],respuestas_usuario, DatosAlumno.get(i)[DatosAlumno.get(i).length-2],DatosAlumno.get(i)[DatosAlumno.get(i).length-1]));
		}
		
		return todoAlumno;
		
	}
	
	/**
	 Esta funcion se encarga de mostrar en pantalla los datos.
	 @param todoAlumno es una lista en donde estan almacenados los datos (nombre, respuestas, puntaje y nota)
	 @param Cantidad_Datos es la cantidad de datos que se desea mostrar de la lista.
	 */
	private static void Mostrar_Datos(List<RevisionAlumno> todoAlumno, int Cantidad_Datos) {
		
		for (int i = 0; i < Cantidad_Datos; i++) {
			System.out.println("\nnombre alumno: " + todoAlumno.get(i).getNombre_Alumno() + "\nrespuestas:");
			for (int j = 0; j < todoAlumno.get(i).getRespuestas().length - 1; j++) {
				System.out.print(todoAlumno.get(i).getRespuestas()[j] + " ");
			}
			System.out.println("\npuntaje total: " + todoAlumno.get(i).getPuntaje_Total() + " nota final: " + todoAlumno.get(i).getNota());
		}
	}
	
	/**
	<h1>este menu se encarga de preguntarle al profesor que accion desea realizar.</h1>
	
	 <h2>las opciones son:</h2>
	 <ol>
		<li>ir al menu ordenar datos de mayor a menor</li>
		<li>ir al menu ordenar datos de menor a mayor</li>
	 	<li>ir al menu datos especificos</li>
	 	<li>cancelar y salir</li>
	 </ol>
	 */
	static void Menu_Acciones_Resultados_Alumnos() {
		boolean continuar = true;
		try {
			System.out.print("\n\nAntes de explorar esta opcion, necesito que me digas la ubicacion de la carpeta\nen la cual estan TODOS los resultados de los alumnos: \n\nubicacion: ");
			
			List<RevisionAlumno> DatosArchivos = OrdenarDatosAlumnos(Texto.leer_teclado());
			
			if (DatosArchivos != null && DatosArchivos.size() > 0) {
				do {
					System.out.print("\nhay un total de " + DatosArchivos.size() + " archivos. que desea hacer?\n1) menu ordenar datos de mayor a menor\n2) menu ordenar datos de menor a mayor\n3) menu datos especificos.\n4) cancelar y salir.\n\nopcion: ");
					String respuesta = Texto.leer_teclado();
					switch (respuesta) {
					case "1":
						MenuMayorMenor(DatosArchivos);
						break;
					case "2":
						MenuMenorMayor(DatosArchivos);
						break;
					case "3":
						MenuDatosEspecificos(DatosArchivos);
						break;
					case "4":
						continuar = false;
						break;
					default:
						System.err.println("\ndato invalido, por favor ingrese un digito del 1 al 4.");
					}
				} while (continuar);
			} else {
				System.err.println("\npor favor intentelo nuevamente, no hay datos que utilizar");
			}
		} catch (IllegalArgumentException e) {
			System.err.println(e.getMessage());
		}
		return;
	}
	
	/**
	<h1>este menu se encarga de preguntarle al profesor que accion desea realizar.</h1>
	
	 <h2>las opciones son:</h2>
	 <ol>
		<li>ordenar por las notas</li>
		<li>ordenar por el puntaje</li>
	 	<li>ordenar por nombre</li>
	 	<li>cancelar y salir</li>
	 </ol>
	 
	 @param DatosArchivos son los datos obtenidos de los archivos entregados por los alumnos.
	 */
	private static void MenuMayorMenor(List<RevisionAlumno> DatosArchivos) {
		boolean continuar = true;
		do {
			System.out.print("\n\n**menu mayor a menor**\n\neste menu se encarga de ordenar los datos de los alumnos de mayor a menor, segun sea su eleccion.\npuede ordenar por las notas, puntaje o nombre.\n\nque accion desea realizar?\n\n1) ordenar por las notas\n2) ordenar por el puntaje\n3) ordenar por nombre\n4) cancelar y salir\n\nopcion: ");
			String respuesta = Texto.leer_teclado();
			switch (respuesta) {
			case "1":
				Collections.sort(DatosArchivos, new IComparar.Comparador_Nota_Mayor_Menor());
				Mostrar_Datos(DatosArchivos, DatosArchivos.size());
				break;
			case "2":
				Collections.sort(DatosArchivos, new IComparar.Comparador_Puntaje_Mayor_Menor());
				Mostrar_Datos(DatosArchivos, DatosArchivos.size());
				break;
			case "3":
				Collections.sort(DatosArchivos, new IComparar.Comparador_Nombre_Mayor_Menor());
				Mostrar_Datos(DatosArchivos, DatosArchivos.size());
				break;
			case "4":
				continuar = false;
				break;
			default:
				System.err.println("\ndato invalido, por favor ingrese un digito del 1 al 4.");
			} 
		} while (continuar);
	}

	/**
	<h1>este menu se encarga de preguntarle al profesor que accion desea realizar.</h1>

	<h2>las opciones son:</h2>
	<ol>
		<li>ordenar por las notas</li>
		<li>ordenar por el puntaje</li>
		<li>ordenar por nombre</li>
		<li>cancelar y salir</li>
	</ol>
	
	@param DatosArchivos son los datos obtenidos de los archivos entregados por los alumnos.
	*/
	private static void MenuMenorMayor(List<RevisionAlumno> DatosArchivos) {
		boolean continuar = true;
		do {
			System.out.print("\n\n**menu menor a mayor **\n\neste menu se encarga de ordenar los datos de los alumnos de menor a mayor, segun sea su eleccion.\npuede ordenar por las notas, puntaje o nombre.\n\nque accion desea realizar?\n\n1) ordenar por las notas\n2) ordenar por el puntaje\n3) ordenar por nombre\n4) cancelar y salir\n\nopcion: ");
			String respuesta = Texto.leer_teclado();
			switch (respuesta) {
			case "1":
				Collections.sort(DatosArchivos, new IComparar.Comparador_Nota_Menor_Mayor());
				Mostrar_Datos(DatosArchivos, DatosArchivos.size());
				break;
			case "2":
				Collections.sort(DatosArchivos, new IComparar.Comparador_Puntaje_Menor_Mayor());
				Mostrar_Datos(DatosArchivos, DatosArchivos.size());
				break;
			case "3":
				Collections.sort(DatosArchivos, new IComparar.Comparador_Nombre_Menor_Mayor());
				Mostrar_Datos(DatosArchivos, DatosArchivos.size());
				break;
			case "4":
				continuar = false;
				break;
			default:
				System.err.println("\ndato invalido, por favor ingrese un digito del 1 al 4.");
			} 
		} while (continuar);
	}
	
	/**
	<h1>este menu se encarga de preguntarle al profesor que accion desea realizar.</h1>

	<h2>las opciones son:</h2>
	<ol>
		<li>obtener la nota mas alta</li>
		<li>obtener la nota mas baja</li>
		<li>obtener el promedio general de las notas</li>
		<li>obtener los datos del alumno especifico</li>
		<li>cancelar y salir al menu anterior</li>
	</ol>
	
	@param DatosArchivos son los datos obtenidos de los archivos entregados por los alumnos.
	*/
	private static void MenuDatosEspecificos(List<RevisionAlumno> DatosArchivos) {
		boolean continuar = true;
		do {
			System.out.print("\n\n**menu datos especificos **\n\neste menu se encarga de poder obtener datos que usted necesite,\npor ejemplo, puede obtener la nota mas alta, la nota mas baja, entre otros.\n\nque accion desea realizar?\n\n1) obtener nota mas alta\n2) obtener nota mas baja\n3) obtener promedio general de las notas\n4) obtener datos del alumno especifico\n5) cancelar y salir al menu anterior\n\nopcion: ");
			int cantidad_notas = 1;
			String respuesta = Texto.leer_teclado();
			switch (respuesta) {
			case "1":
				continuar = true;
				
				do {
					System.out.print("\ncuantas notas desea ver? (recuerde que puede elegir entre 1 y "+ DatosArchivos.size() +"): ");
					try {
						cantidad_notas = Integer.parseInt(Texto.leer_teclado());
						if ((cantidad_notas > 0) && (cantidad_notas <= DatosArchivos.size())) {
							Collections.sort(DatosArchivos, new IComparar.Comparador_Nota_Mayor_Menor());
							
							Mostrar_Datos(DatosArchivos, cantidad_notas);
						} else {
							System.err.println("\npor favor ingrese un numero entre 1 y " + DatosArchivos.size() +"...\n");
						}
						continuar = false;
					} catch (Exception e) {
						System.err.println("\npor favor ingrese numeros...\n");
					}
				} while(continuar);
				break;
			case "2":
				continuar = true;
				
				do {
					System.out.print("\ncuantas notas desea ver? (recuerde que puede elegir entre 1 y "+ DatosArchivos.size() +"): ");
					try {
						cantidad_notas = Integer.parseInt(Texto.leer_teclado());
						if ((cantidad_notas > 0) && (cantidad_notas <= DatosArchivos.size())) {
							Collections.sort(DatosArchivos, new IComparar.Comparador_Nota_Menor_Mayor());
							
							Mostrar_Datos(DatosArchivos, cantidad_notas);
						} else {
							System.err.println("\npor favor ingrese un numero entre 1 y " + DatosArchivos.size() +"...\n");
						}
						continuar = false;
					} catch (Exception e) {
						System.err.println("\npor favor ingrese numeros...\n");
					}	
				} while(continuar);
				break;
			case "3":
				int i = 0;
				int promedio = 0;
				try {
					for (i = 0; i < DatosArchivos.size(); i++) {
						promedio += Integer.parseInt(DatosArchivos.get(i).getNota().replaceAll(",", ""));
					}
					System.out.println("el promedio general de las notas es: " + (promedio/DatosArchivos.size()));
					continuar = false;
				} catch (Exception e) {
					System.err.println("\nla nota del alumno " + DatosArchivos.get(i).getNombre_Alumno() + " ha sido alterada...");
				}
				break;
			case "4":
				System.out.println("\npor favor ingrese el nombre del alumno: ");
				String nombre_alumno = Texto.leer_teclado();

				int encontro_alumno = 0;

				for (int j = 0; j < DatosArchivos.size(); j++) {
					if (DatosArchivos.get(j).getNombre_Alumno().replaceAll(" ", "").equalsIgnoreCase(nombre_alumno.replaceAll(" ", ""))) {
						System.out.println("\nnombre alumno: " + DatosArchivos.get(j).getNombre_Alumno() + "\nrespuestas:");
						for (int k = 0; k < DatosArchivos.get(j).getRespuestas().length; k++) {
							System.out.print(DatosArchivos.get(j).getRespuestas()[k] + " ");
						}
						System.out.println("\npuntaje total: " + DatosArchivos.get(j).getPuntaje_Total() + " nota final: " + DatosArchivos.get(j).getNota());
						encontro_alumno = 1;
						break;
					}
				}
				if (encontro_alumno == 0) {
					System.err.println("\nno se ha encontrado el alumno. ha escrito bien el nombre?\n");
				}
				break;
			case "5":
				continuar = false;
				break;
			default:
				System.err.println("\nopcion invalida, intentelo de nuevo.");
			}
		} while (continuar);
	}
	
}