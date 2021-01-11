package base;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

/**
 * Esta clase tiene todo lo que sea relacionado con sql. como tambien el inicio
 * de sesion, el cual redirige a las opciones que tiene el usuario.
 * 
 * @author Sebastian Morgado
 * @version 2.0.0
 * @since 1.6.0
 */
public class ServidorSql {

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
	 *         {@code false} si no.
	 * 
	 * @throws IllegalArgumentException en caso que no haya internet, o no se pueda
	 *                                  conectar a la base de datos o falle al
	 *                                  consultar, mandara un mensaje de error.
	 */
	public static boolean cambiar_contrasena(Connection conexion, String rut, String contrasena_nueva) {
		try {
			PreparedStatement st = (PreparedStatement) conexion
					.prepareStatement("UPDATE usuarios SET contrasena = ? WHERE rut = ?");

			st.setString(1, contrasena_nueva);
			st.setString(2, rut);

			if (st.executeUpdate() == 1) {
				return true;
			}
		} catch (Exception e) {
			throw new IllegalArgumentException(
					"este error no lo deberias ver... verifica si estas conectado a internet... intentalo nuevamente.");
		}
		return false;
	}

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
	 * @throws IllegalArgumentException en caso que no haya internet, o no se pueda
	 *                                  conectar a la base de datos o falle al
	 *                                  consultar, mandara un mensaje de error.
	 */
	public static Connection conectarse_a_server(final String url, final String NombreDB, final String driver,
			final String Usuario, final String Contrasena) {
		Connection conexion = null;
		try {
			Class.forName(driver);
			conexion = DriverManager.getConnection(url + NombreDB, Usuario, Contrasena);
		} catch (Exception e) {
			throw new IllegalArgumentException(
					"este error no lo deberias ver... verifica si estas conectado a internet... intentalo nuevamente.");
		}
		return conexion;
	}

	/**
	 * Esta funcion se encarga de crear cuentas de usuarios.
	 * 
	 * @param Servidor  este parametro se obtuvo de {@link #conectarse_a_server}, el
	 *                  cual tiene la conexion hacia la base de datos.
	 * @param rango     es el rango de la cuenta, 0 es el alumno, 1 es el profesor.
	 * @param nombres   es el nombre del alumno.
	 * @param apellidos es el apellido del alumno.
	 * @param Rut       es el rut del alumno.
	 * @param pass      es la contraseña del alumno.
	 * 
	 * @return retorna {@code true} si se ha creado correctamente la cuenta, si
	 *         retorna {@code false}, ha fallado al crearla.
	 */
	public static boolean crear_cuenta(Connection Servidor, String nombres, String apellidos, String Rut, String pass,
			String rango) {
		try {
			PreparedStatement st = (PreparedStatement) Servidor.prepareStatement(
					"INSERT INTO usuarios (apellidos,nombres,contrasena,rango,rut) VALUES (?, ?, ?, ?, ?)");

			st.setString(1, apellidos);
			st.setString(2, nombres);
			st.setString(3, pass);
			st.setInt(4, Integer.parseInt(rango));
			st.setString(5, Rut);

			if (st.executeUpdate() == 1) {
				return true;
			}
		} catch (Exception e) {
			throw new IllegalArgumentException(
					"este error no lo deberias ver... verifica si estas conectado a internet... intentalo nuevamente.");
		}
		return false;
	}

	/**
	 * Esta funcion se encarga de obtener los datos del usuario.
	 *
	 * @param conexion este parametro se obtuvo de {@link #conectarse_a_server}, el
	 *                 cual tiene la conexion hacia la base de datos.
	 * @param rut      es el rut del cual se le desea cambiar la contraseña.
	 * 
	 * @return retornara un arreglo de strings con todos los datos del alumno, los
	 *         cuales incluyen nombre, apellidos y rut.
	 * 
	 * @throws IllegalArgumentException en caso que no haya internet, o no se pueda
	 *                                  conectar a la base de datos o falle al
	 *                                  consultar, mandara un mensaje de error.
	 */
	public static String[] ObtenerDatos(Connection conexion, String rut) { // NO_UCD (unused code)
		try {
			String[] datos = null;
			PreparedStatement st = (PreparedStatement) conexion
					.prepareStatement("Select nombres, apellidos from usuarios where rut = ?");

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
			throw new IllegalArgumentException(
					"este error no lo deberias ver... verifica si estas conectado a internet.");
		}
		return new String[] { "// si ves este error, ", "significa que no tienes nombre ni apellido... //" };
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
	 * @throws IllegalArgumentException en caso que no haya internet, o no se pueda
	 *                                  conectar a la base de datos o falle al
	 *                                  consultar, mandara un mensaje de error.
	 */
	public static int verificar_usuario(Connection Servidor, String rut, String contrasena) {
		try {
			PreparedStatement st = (PreparedStatement) Servidor
					.prepareStatement("Select rango from usuarios where rut = ? and contrasena = ?");

			st.setString(1, rut);
			st.setString(2, contrasena);

			ResultSet rs = st.executeQuery();

			if (rs.next()) {
				ResultSetMetaData metadata = rs.getMetaData();
				return Integer.parseInt(rs.getString(metadata.getColumnName(1)));
			}
		} catch (Exception e) {
			throw new IllegalArgumentException(
					"este error no lo deberias ver... verifica si estas conectado a internet... intentalo nuevamente.");
		}
		return -1;
	}

	/**
	 * Esta funcion se encarga de verificar si el usuario existe.
	 *
	 * @param conexion este parametro se obtuvo de {@link #conectarse_a_server}, el
	 *                 cual tiene la conexion hacia la base de datos.
	 * @param rut      es el rut del cual se le desea cambiar la contraseña.
	 * 
	 * @return retornara {@code true} si el usuario existe. retornara {@code false}
	 *         si no.
	 * 
	 * @throws IllegalArgumentException en caso que no haya internet, o no se pueda
	 *                                  conectar a la base de datos o falle al
	 *                                  consultar, mandara un mensaje de error.
	 */
	public static boolean verificar_usuario_existente(Connection conexion, String rut) {
		try {
			PreparedStatement st = (PreparedStatement) conexion
					.prepareStatement("Select rut from usuarios where rut = ?");

			st.setString(1, rut);

			ResultSet rs = st.executeQuery();

			if (rs.next()) {
				return true;
			}
		} catch (Exception e) {
			throw new IllegalArgumentException(
					"este error no lo deberias ver... verifica si estas conectado a internet... intentalo nuevamente.");
		}
		return false;
	}
}
