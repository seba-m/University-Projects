package utilidadesSeguridad;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * Esta clase se encarga de hacer todo lo relacionado con md5 (crear
 * identificadores para el examen).
 * 
 * @author Sebastian Morgado
 * @version 2.0.0
 * @since 2.0.0
 */
public class MD5 {

	/**
	 * Este metodo se encarga de generar un token. puede ser tanto aleatorio, como
	 * no aleatorio (un valor fijo, definido como "" (o vacio)). El objetivo de este
	 * token es especificar el id de un examen (con el fin de que no hayan 2
	 * examenes del mismo tipo), y de poder establecer contraseñas aleatorias.
	 * 
	 * @param tipo         es el tipo de token a generar. puede ser random, o uno
	 *                     especificado por el usuario.
	 * @param valorUsuario es el texto del cual se desea crear un token.
	 * 
	 * @return retorna un token (o identificador), para saber a que examen pertenece
	 *         el metodo que lo llama.
	 */
	public static String generarTokenExamen(String tipo, String valorUsuario) {

		String valor = (!tipo.equals("random")) ? valorUsuario : String.valueOf(new SecureRandom().nextFloat());

		String valorRetorno = null;

		try {

			byte[] arr = MessageDigest.getInstance("MD5").digest(valor.getBytes());

			StringBuilder sb = new StringBuilder();

			for (byte b : arr) {
				sb.append(String.format("%02X", b));
			}

			valorRetorno = sb.toString();

		} catch (NoSuchAlgorithmException e) {
			return "";
		}
		return valorRetorno;
	}
}
