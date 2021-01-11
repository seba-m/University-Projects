package utilidadesSeguridad;

import java.io.File;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Scanner;

import javax.crypto.Cipher;

/**
 * Esta clase se encarga de hacer todo lo relacionado con rsa (encriptar y
 * desencriptar archivos protegidos con RSA).
 * 
 * @author Sebastian Morgado
 * @version 2.0.0
 * @since 2.0.0
 */
public class RSA {

	/**
	 * Este metodo se encarga de crear una llave publica y una privada. La llave
	 * publica será la que se le dará al alumno, con el fin de encriptar sus
	 * resultados. por otro lado, la llave privada será del profesor, con el
	 * objetivo de poder desencriptar los resultados de los alumnos, y poder revisar
	 * los resultados.
	 * 
	 * @return retorna la llave publica y la llave privada convertidas en strings.
	 */
	public static String[] crearLlavesRSA() {
		String[] llaveStrings = null;
		try {
			llaveStrings = new String[2];

			KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
			kpg.initialize(2048);
			KeyPair kp = kpg.generateKeyPair();

			StringBuilder llaveProfesor = new StringBuilder("");

			llaveProfesor.append(Base64.getEncoder().encodeToString(kp.getPrivate().getEncoded()));

			StringBuilder llaveAlumno = new StringBuilder("");

			llaveAlumno.append(Base64.getEncoder().encodeToString(kp.getPublic().getEncoded()));

			llaveStrings[0] = llaveProfesor.toString();
			llaveStrings[1] = llaveAlumno.toString();
		} catch (Exception e) {
			throw new IllegalArgumentException(
					"ERROR FATAL: ha fallado el programa al decodificar los resultados. por favor actualize su version de java y contacte a su profesor.");
		}

		return llaveStrings;
	}

	/**
	 * Este metodo se encarga de des encriptar el texto con la llave privada.
	 * 
	 * @param llave es la llave que se utilizara para desencriptar el texto.
	 * @param texto es el texto el cual se desencriptara.
	 * 
	 * @return retorna el texto desencriptado.
	 */
	public static String desEncriptarTexto(String llave, String texto) {
		String textoDesEncriptado = null;
		try {
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipher.init(Cipher.DECRYPT_MODE, stringAPrivateKey(llave));

			cipher.update(Base64.getDecoder().decode(texto));

			byte[] textoDesCifrado = cipher.doFinal();

			textoDesEncriptado = Base64.getEncoder().encodeToString(textoDesCifrado);
		} catch (Exception e) {
			throw new IllegalArgumentException(
					"ERROR FATAL: ha fallado el programa al decodificar los resultados. por favor actualize su version de java y contacte a su profesor.");
		}
		return textoDesEncriptado;
	}

	/**
	 * Este metodo se encarga de encriptar el texto con la llave publica.
	 * 
	 * @param llave es la llave que se utilizara para encriptar el texto.
	 * @param texto es el texto el cual se encriptara.
	 * 
	 * @return retorna el texto encriptado.
	 */
	public static String encriptarTexto(String llave, String texto) {
		String textoEncriptado = null;
		try {
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipher.init(Cipher.ENCRYPT_MODE, stringAPublicKey(llave));

			cipher.update(texto.getBytes());

			byte[] textoCifrado = cipher.doFinal();

			textoEncriptado = Base64.getEncoder().encodeToString(textoCifrado);
		} catch (Exception e) {
			throw new IllegalArgumentException(
					"ERROR FATAL: ha fallado el programa al generar sus resultados. por favor actualize su version de java y contacte a su profesor.");
		}
		return textoEncriptado;
	}

	/**
	 * Este metodo se encarga de obtener la llave del profesor, a travez del archivo
	 * generado al crear el examen.
	 * 
	 * @param rutaArchivo es la ruta en donde se encuentra el archivo.
	 * @return retorna la llave privada del examen (para poder revisar los
	 *         resultados de los alumnos), y también el id del examen (para evitar
	 *         revisar examenes que no son los que usted evaluó)
	 */
	public static String[] leerLlavesRSAProfesor(String rutaArchivo) {
		String llave = null;
		String idExamen = null;
		String respuestas = null;
		try {
			File archivo = new File(rutaArchivo);
			Scanner leerArchivo = new Scanner(archivo);

			StringBuilder sb = new StringBuilder();
			sb.append(leerArchivo.nextLine());
			llave = sb.toString();

			sb = new StringBuilder();
			sb.append(leerArchivo.nextLine());
			idExamen = sb.toString();

			sb = new StringBuilder();
			sb.append(leerArchivo.nextLine());
			respuestas = sb.toString();

			leerArchivo.close();

		} catch (Exception e) {
			throw new IllegalArgumentException(
					"ERROR FATAL: ha fallado el programa al decodificar los resultados. por favor actualize su version de java y contacte a su profesor.");
		}

		return new String[] { llave, idExamen, respuestas };
	}

	/**
	 * Este metodo convierte un string a una llave privada (del profesor).
	 * 
	 * @param texto es el texto el cual contiene la llave.
	 * 
	 * @return retorna la llave del profesor. esta se usara para desencriptar los
	 *         resultados de los alumnos.
	 */
	private static PrivateKey stringAPrivateKey(String texto) {
		PrivateKey key = null;
		try {
			byte[] keyBytes = Base64.getDecoder().decode(texto);

			PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
			KeyFactory kf = KeyFactory.getInstance("RSA");

			key = kf.generatePrivate(spec);
		} catch (Exception e) {
			throw new IllegalArgumentException(
					"ERROR FATAL: ha fallado el programa al decodificar los resultados. por favor actualize su version de java y contacte a su profesor.");
		}
		return key;
	}

	/**
	 * Este metodo convierte un string a una llave publica (del alumno).
	 * 
	 * @param texto es el texto el cual contiene la llave.
	 * 
	 * @return retorna la llave del alumno. esta se usara para encriptar el
	 *         resultado del alumno.
	 */
	private static PublicKey stringAPublicKey(String texto) {
		PublicKey key = null;
		try {
			byte[] keyBytes = Base64.getDecoder().decode(texto);

			X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
			KeyFactory kf = KeyFactory.getInstance("RSA");

			key = kf.generatePublic(spec);
		} catch (Exception e) {
			throw new IllegalArgumentException(
					"ERROR FATAL: ha fallado el programa al decodificar los resultados. por favor actualize su version de java y contacte a su profesor.");
		}

		return key;
	}

}
