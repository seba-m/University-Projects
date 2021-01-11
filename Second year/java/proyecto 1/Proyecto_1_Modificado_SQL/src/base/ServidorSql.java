package base;

import java.sql.*;
import java.util.*;

/**
 * Esta clase tiene todo lo que sea relacionado con sql. como tambien
 * el inicio de sesion, el cual redirige a las opciones que tiene el
 * usuario.
 * 
 * @author Sebastian Morgado
 * @version 1.6.1
 * @since 1.6.0
 */
public class ServidorSql {

	/**
	 * Esta funcion se encarga de conectarse a la base de datos.
	 * 
	 * @param url        link del servidor.
	 * @param NombreDB   nombre de la base de datos.
	 * @param driver     nombre del driver (por lo general es
	 *                   "com.mysql.jdbc.Driver").
	 * @param Usuario    nombre de usuario del servidor (por lo general es "root").
	 * @param Contrasena contraseña de la base de datos (por lo general no hay, por
	 *                   lo que puedes poner "").
	 * 
	 * @return en este caso se conecta a la base de datos, y retorna la conexion que
	 *         se realiza.
	 * 
	 * @throws IllegalArgumentException en caso que no haya internet, o no se pueda conectar a la
	 *                   base de datos o falle al consultar, mandara un mensaje de
	 *                   error.
	 */
	private static Connection conectarse_a_server(final String url, final String NombreDB, final String driver, final String Usuario, final String Contrasena) {
		Connection conexion = null;
		try {
			Class.forName(driver);
			conexion = DriverManager.getConnection(url + NombreDB, Usuario, Contrasena);
		} catch (Exception e) {
			throw new IllegalArgumentException("este error no lo deberias ver... verifica si estas conectado a internet... intentalo nuevamente.");
		}
		return conexion;
	}

	/**
	 * Esta funcion se encarga de verificar las cuentas de los usuarios.
	 * 
	 * @param Servidor   este parametro se obtuvo de {@link #conectarse_a_server},
	 *                   el cual tiene la conexion hacia la base de datos.
	 * @param rut        es el rut del usuario.
	 * @param contrasena es la contraseña del usuario.
	 * 
	 * @return retorna el rango del usuario (1 para profesor, 0 para alumno), y en
	 *         caso de que falle la consulta, retornara un -1.
	 * 
	 * @throws IllegalArgumentException en caso que no haya internet, o no se pueda conectar a la
	 *                   base de datos o falle al consultar, mandara un mensaje de
	 *                   error.
	 */
	private static int verificar_usuario(Connection Servidor, String rut, String contrasena) {
		try {
			PreparedStatement st = (PreparedStatement) Servidor.prepareStatement("Select rango from usuarios where rut = ? and contrasena = ?");

			st.setString(1, rut);
			st.setString(2, contrasena);

			ResultSet rs = st.executeQuery();

			if (rs.next()) {
				ResultSetMetaData metadata = rs.getMetaData();
				return Integer.parseInt(rs.getString(metadata.getColumnName(1)));
			}
		} catch (Exception e) {
			throw new IllegalArgumentException("este error no lo deberias ver... verifica si estas conectado a internet... intentalo nuevamente.");
		}
		return -1;
	}

	/**
	 * Esta funcion se encarga de crear cuentas de usuarios.
	 * 
	 * @param Servidor este parametro se obtuvo de {@link #conectarse_a_server}, el
	 *                 cual tiene la conexion hacia la base de datos.
	 * @param rango    es el rango de la cuenta, 0 es el alumno, 1 es el profesor.
	 * 
	 * @return retorna {@code true} si se ha creado correctamente la cuenta, si
	 *         retorna {@code false}, ha fallado al crearla.
	 */
	private static boolean crear_cuenta(Connection Servidor, String rango) {
		try {
			PreparedStatement st = (PreparedStatement) Servidor.prepareStatement("INSERT INTO usuarios (apellidos,nombres,contrasena,rango,rut) VALUES (?, ?, ?, ?, ?)");

			boolean continuar = true;
			String nombres = null;
			String apellidos = null;
			String Rut = null;

			do {
				System.out.print("\nnombres: ");
				nombres = Texto.leer_teclado().toLowerCase();

				if (Texto.TieneNumeros(nombres)) {
					System.err.println("\npor favor no ingrese numeros en su nombre.\n");
				} else {
					continuar = false;
				}

			} while (continuar);

			continuar = true;

			do {
				System.out.print("\napellidos: ");
				apellidos = Texto.leer_teclado().toLowerCase();

				if (Texto.TieneNumeros(apellidos)) {
					System.err.println("\npor favor no ingrese numeros en su nombre.\n");
				} else {
					continuar = false;
				}

			} while (continuar);

			continuar = true;

			do {
				System.out.print("\nrut: ");
				Rut = Texto.leer_teclado();
				if (validar_Rut(Rut)) {
					continuar = false;
				} else {
					System.err.println("\npor favor ingrese un rut valido.\n");
				}
			} while (continuar);

			System.out.println("\ncontraseña: ");

			String pass = Texto.leer_teclado();

			st.setString(1, apellidos);
			st.setString(2, nombres);
			st.setString(3, pass);
			st.setInt(4, Integer.parseInt(rango));
			st.setString(5, Rut);

			if (st.executeUpdate() == 1) {
				return true;
			}
		} catch (Exception e) {
			throw new IllegalArgumentException("este error no lo deberias ver... verifica si estas conectado a internet... intentalo nuevamente.");
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
	private static boolean validar_Rut(String rut) {

		boolean validacion = false;
		try {
			rut = rut.toUpperCase();
			rut = rut.replace(".", "").replace("-", "");
			int rut_aux = Integer.parseInt(rut.substring(0, rut.length() - 1));
			
			char numero_guion = rut.charAt(rut.length() - 1);

			int multiplicador = 0, suma = 1;
			while (rut_aux != 0) {
				suma = (suma + rut_aux % 10 * (9 - multiplicador++ % 6)) % 11;
				rut_aux /= 10;
			}

			if (numero_guion == (char) (suma != 0 ? suma + 47 : 75)) {
				validacion = true;
			}
		} catch (Exception e) {
			// el rut ingresado es invalido o si ingreso texto.
			validacion = false;
		}
		return validacion;
	}

	/**
	 * Esta funcion se encarga de cambiar la contraseña.
	 *
	 * @param conexion         este parametro se obtuvo de
	 *                         {@link #conectarse_a_server}, el cual tiene la
	 *                         conexion hacia la base de datos.
	 * @param rut              es el rut del cual se le desea cambiar la contraseña.
	 * @param contrasena_nueva es la nueva contraseña definida por el usuario.
	 * 
	 * @return retornara {@code true} si pudo cambiar la contraseña. retornara 
	 *					 {@code false} si no.
	 * 
	 * @throws IllegalArgumentException en caso que no haya internet, o no se pueda conectar a la
	 *                   base de datos o falle al consultar, mandara un mensaje de
	 *                   error.
	 */
	private static boolean cambiar_contrasena(Connection conexion, String rut, String contrasena_nueva) {
		try {
			PreparedStatement st = (PreparedStatement) conexion.prepareStatement("UPDATE usuarios SET contrasena = ? WHERE rut = ?");

			st.setString(1, contrasena_nueva);
			st.setString(2, rut);

			if (st.executeUpdate() == 1) {
				return true;
			}
		} catch (Exception e) {
			throw new IllegalArgumentException("este error no lo deberias ver... verifica si estas conectado a internet... intentalo nuevamente.");
		}
		return false;
	}

	/**
	 * Esta funcion se encarga de verificar si el usuario existe.
	 *
	 * @param conexion         este parametro se obtuvo de
	 *                         {@link #conectarse_a_server}, el cual tiene la
	 *                         conexion hacia la base de datos.
	 * @param rut              es el rut del cual se le desea cambiar la contraseña.
	 * 
	 * @return retornara {@code true} si el usuario existe. retornara {@code false}
	 *         si no.
	 * 
	 * @throws IllegalArgumentException en caso que no haya internet, o no se pueda conectar a la
	 *                   base de datos o falle al consultar, mandara un mensaje de
	 *                   error.
	 */
	private static boolean verificar_usuario_existente(Connection conexion, String rut) {
		try {
			PreparedStatement st = (PreparedStatement) conexion.prepareStatement("Select rut from usuarios where rut = ?");

			st.setString(1, rut);

			ResultSet rs = st.executeQuery();

			if (rs.next()) {
				return true;
			}
		} catch (Exception e) {
			throw new IllegalArgumentException("este error no lo deberias ver... verifica si estas conectado a internet... intentalo nuevamente.");
		}
		return false;
	}

	/**
	 * Esta funcion se encarga de obtener los datos del usuario.
	 *
	 * @param conexion         este parametro se obtuvo de
	 *                         {@link #conectarse_a_server}, el cual tiene la
	 *                         conexion hacia la base de datos.
	 * @param rut              es el rut del cual se le desea cambiar la contraseña.
	 * 
	 * @return retornara 	   un arreglo de strings con todos los datos del alumno,
	 * 						   los cuales incluyen nombre, apellidos y rut.
	 * 
	 * @throws IllegalArgumentException en caso que no haya internet, o no se pueda conectar a la
	 *                   base de datos o falle al consultar, mandara un mensaje de
	 *                   error.
	 */
	private static String[] ObtenerDatos(Connection conexion, String rut) {
		try {
			String[] datos = null;
			PreparedStatement st = (PreparedStatement) conexion.prepareStatement("Select nombres, apellidos from usuarios where rut = ?");

			st.setString(1, rut);

			ResultSet rs = st.executeQuery();
			if (rs.next()) {

				ResultSetMetaData metadata = rs.getMetaData();

				List<String> list = new ArrayList<String>();

				list.add(String.valueOf(rs.getString(metadata.getColumnName(1))));
				list.add(String.valueOf(rs.getString(metadata.getColumnName(2))));
				list.add(rut);
				datos = list.toArray(new String[0]);
				return datos;

			}

		} catch (Exception e) {
			System.out.println("este error no lo deberias ver... verifica si estas conectado a internet.");
			System.exit(0);
		}
		return new String[] { "// si ves este error, ", "significa que no tienes nombre ni apellido... //" };
	}

	/**
	 * este es el menu en donde se separaran las acciones que el usuario podra
	 * tomar. dependiendo del rango que el rut tenga asignado, sera las opciones que
	 * tendra disponibles.
	 */
	public static void inicio_sesion() {

		boolean continuar = true;

		String url = "jdbc:mysql://localhost:3306/";
		String NombreDB = "proyecto_db";
		String Driver = "com.mysql.cj.jdbc.Driver";
		String Usuario = "root";
		String Contrasena = "";
		String rut = "";
		String pass = "";
		try {

			do {

				Connection conexion = conectarse_a_server(url, NombreDB, Driver, Usuario, Contrasena);

				do {
					System.out.println("\n¡Hola!, Bienvenid@ a mi programa, soy Sebastian Morgado, por favor necesito que me indiques cual es su RUT: ");
					rut = Texto.leer_teclado().replace(".", "").replace("-", "");
					if (validar_Rut(rut)) {

						System.out.println("\ncontraseña: ");
						pass = Texto.leer_teclado();

						continuar = false;
					} else {
						System.err.println("\npor favor ingrese un rut valido.\n");
						continuar = true;
					}
				} while (continuar);

				int usuario_valido = verificar_usuario(conexion, rut, pass);

				continuar = true;

				if (usuario_valido != -1) {
					do {
						if (usuario_valido == 1) {
							Profesor.menu(ObtenerDatos(conexion, rut));
							conexion.close();
							System.out.println("\nprograma finalizado.");
							return;

						} else if (usuario_valido == 0) {
							Alumno.menu(ObtenerDatos(conexion, rut));
							conexion.close();
							System.out.println("\nprograma finalizado.");
							return;

						} else if (usuario_valido == 2) {
							do {
								System.out.print("\nbienvenido administrador, que acción desea realizar?\n\n1) ir al menu del profesor\n2) ir al menu del alumno\n3) cancelar y cerrar sesion\n\nsu opcion: ");
								String opcion = Texto.leer_teclado();
								String[] informacion_admin = { "administrador", "del programa" };
								boolean valor_valido = true;
								switch (opcion) {
								case "1":
									Profesor.menu(informacion_admin);
									continuar = false;
									break;
								case "2":
									Alumno.menu(informacion_admin);
									continuar = false;
									System.exit(0);
									
								case "3":
									System.out.println("\nprograma finalizado.");
									conexion.close();
									System.exit(0);
									
								default:
									System.out.println("\ningrese un valor valido. (1 = menu profesor, 2 = menu alumno, 3 = cancelar y cerrar sesion)\n");
									valor_valido = false;
									break;
								}

								if (!continuar && valor_valido) {
									System.out.print("\n\ndesea realizar otra accion? 1 = si, 2 = no.\n\nsu respuesta: ");
									opcion = Texto.leer_teclado();

									continuar = true;

									if (opcion.equalsIgnoreCase("2")) {
										System.out.println("\nprograma finalizado.");
										System.exit(0);
									}
								}
							} while (continuar);

						} else {
							System.err.println("\ntu cuenta ha sido cancelada, por favor contacta al administrador para resolver tu situacion.\n");
							System.exit(0);
						}
					} while (continuar);

				} else if (verificar_usuario_existente(conexion, rut) == true) {
					System.out.println("\n\ncontraseña incorrecta, desea cambiarla? (si o no)");
					if (Texto.leer_teclado().equalsIgnoreCase("si")) {

						System.out.println("\n\npor favor ingrese la nueva contraseña: ");

						String contrasena_nueva = Texto.leer_teclado();

						if (cambiar_contrasena(conexion, rut, contrasena_nueva)) {
							System.out.println("\n\ncontraseña cambiada correctamente.");
						} else {
							System.out.println("\n\ncontraseña cambiada incorrectamente.");
						}
					}
				} else {
					System.out.println("\n\nel rut no esta registrado. desea crearse una cuenta? (si o no)");
					if (Texto.leer_teclado().equalsIgnoreCase("si")) {
						System.out.println("\n\nEs usted un profesor o un alumno?");
						continuar = true;
						do {
							switch (Texto.leer_teclado().toLowerCase()) {
							case "profesor":
								if (crear_cuenta(conexion, "1")) {
									System.out.println("\n\ncuenta creada correctamente.");
									continuar = false;
								} else {
									System.out.println("\n\nha ocurrido un error al crear la cuenta. intentar de nuevo? (si o no)");
									if (!Texto.leer_teclado().equalsIgnoreCase("si")) {
										continuar = false;
									}
								}
								break;
							case "alumno":
								if (crear_cuenta(conexion, "0")) {
									System.out.println("\n\ncuenta creada correctamente.");
									continuar = false;
								} else {
									System.out.println("\n\nha ocurrido un error al crear la cuenta. intentar de nuevo? (si o no)");
									if (!Texto.leer_teclado().equalsIgnoreCase("si")) {
										continuar = false;
									}
								}
								break;
							default:
								System.err.println("\ndato invalido, por favor intente ingresar ALUMNO o PROFESOR.");
							}
						} while (continuar);

					}
				}

				System.out.print("\n\ndesea realizar otra accion? 1 = si, 2 = no.\n\nsu respuesta: ");
				String opcion = Texto.leer_teclado();

				continuar = true;

				if (!opcion.equalsIgnoreCase("1")) {
					System.out.println("\nprograma finalizado.");
					continuar = false;
				}

			} while (continuar);

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
