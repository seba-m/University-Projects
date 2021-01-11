package utilidadesSeguridad;

import java.util.Base64;

/**
 * Esta clase se encarga de hacer todo lo relacionado con base64 (proteger
 * texto, desproteger texto y verificar si es valido)
 * 
 * @author Sebastian Morgado
 * @version 2.0.0
 * @since 2.0.0
 */
public class BASE64 {
	/**
	 * Esta funcion se encarga de desproteger el texto usando base64.
	 * 
	 * @param texto es el texto que se desea proteger.
	 * @return retorna el string codificado en base64.
	 */
	public static String DesprotegerTextoBase64(String texto) {
		return new String(Base64.getDecoder().decode(texto));
	}

	/**
	 * Esta funcion se encarga de verificar si el texto usa base64 y no esta vacio.
	 * 
	 * @param texto es el texto que se desea verificar.
	 * @return retorna {@code true} si el texto esta codificado en base64, retornara
	 *         {@code false} si no lo esta.
	 */
	public static boolean esBase64(String texto) {
		return (texto).matches("^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)?$");
	}

	/**
	 * Esta funcion se encarga de proteger el texto usando base64.
	 * 
	 * @param texto es el texto que se desea proteger.
	 * @return retorna el string codificado en base64.
	 */
	public static String ProtegerTextoBase64(boolean texto) {
		return Base64.getEncoder().encodeToString(String.valueOf(texto).getBytes());
	}

	/**
	 * Esta funcion se encarga de proteger el texto usando base64.
	 * 
	 * @param texto es el texto que se desea proteger.
	 * @return retorna el string codificado en base64.
	 */
	public static String ProtegerTextoBase64(int texto) {
		return Base64.getEncoder().encodeToString(String.valueOf(texto).getBytes());
	}

	/**
	 * Esta funcion se encarga de proteger el texto usando base64.
	 * 
	 * @param texto es el texto que se desea proteger.
	 * @return retorna el string codificado en base64.
	 */
	public static String ProtegerTextoBase64(String texto) {
		return Base64.getEncoder().encodeToString(texto.getBytes());
	}
}
