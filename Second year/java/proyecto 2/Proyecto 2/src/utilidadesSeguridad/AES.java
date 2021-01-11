package utilidadesSeguridad;

import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * Esta clase se encarga de hacer todo lo relacionado con AES (proteger
 * texto y desproteger texto)
 * 
 * @author <a href="https://github.com/HashRaygoza/EncriptadoAES">HashRaygoza</a>
 * @version 2.0.0
 * @since 2.0.0
 */
public class AES {

	/**
	 * Crea la clave de encriptacion usada internamente.
	 * 
	 * @param clave Clave que se usara para encriptar
	 * @return Clave de encriptacion
	 */
	private static SecretKeySpec crearClave(String clave) {
		SecretKeySpec secretKey = null;
		try {
			byte[] claveEncriptacion = clave.getBytes("UTF-8");

			MessageDigest sha = MessageDigest.getInstance("SHA-1");

			claveEncriptacion = sha.digest(claveEncriptacion);
			claveEncriptacion = Arrays.copyOf(claveEncriptacion, 16);

			secretKey = new SecretKeySpec(claveEncriptacion, "AES");
		} catch (Exception e) {
			throw new IllegalArgumentException(
					"ERROR FATAL: ha fallado el programa al generar sus resultados. por favor actualize su version de java y contacte a su profesor.");
		}

		return secretKey;
	}

	/**
	 * Desencripta la cadena de texto indicada usando la clave de encriptacion
	 * 
	 * @param datosEncriptados Datos encriptados
	 * @param claveSecreta     Clave de encriptacion
	 * @return Informacion desencriptada
	 */
	public static String desencriptar(String datosEncriptados, String claveSecreta) {

		String datos = null;

		try {
			SecretKeySpec secretKey = crearClave(claveSecreta);
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
			cipher.init(Cipher.DECRYPT_MODE, secretKey);

			byte[] bytesEncriptados = Base64.getDecoder().decode(datosEncriptados);
			byte[] datosDesencriptados = cipher.doFinal(bytesEncriptados);
			datos = new String(datosDesencriptados);

		} catch (Exception e) {
			throw new IllegalArgumentException(
					"ERROR FATAL: ha fallado el programa al generar sus resultados. por favor actualize su version de java y contacte a su profesor.");
		}

		return datos;
	}

	/**
	 * Aplica la encriptacion AES a la cadena de texto usando la clave indicada
	 * 
	 * @param datos        Cadena a encriptar
	 * @param claveSecreta Clave para encriptar
	 * @return Información encriptada
	 */
	public static String encriptar(String datos, String claveSecreta) {
		String encriptado = null;
		try {
			SecretKeySpec secretKey = crearClave(claveSecreta);

			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);

			byte[] datosEncriptar = datos.getBytes("UTF-8");
			byte[] bytesEncriptados = cipher.doFinal(datosEncriptar);
			encriptado = Base64.getEncoder().encodeToString(bytesEncriptados);
		} catch (Exception e) {
			throw new IllegalArgumentException(
					"ERROR FATAL: ha fallado el programa al generar sus resultados. por favor actualize su version de java y contacte a su profesor.");
		}

		return encriptado;
	}
}
