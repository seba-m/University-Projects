package utilidadesInterfaces;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Random;

import javax.imageio.ImageIO;

/**
 * Esta clase se encarga de crear y retornar imagenes.
 * 
 * @author Sebastian Morgado
 * @version 2.0.0
 * @since 2.0.0
 */
public class ImagenesAleatorias {
	private static BufferedImage[] imagenes = new BufferedImage[100];
	private static Random rand = new Random();

	/**
	 * Este metodo se encarga de retornar la imagen creada, de manera aleatoria.
	 * 
	 * @return retorna la imagen aleatoria.
	 */
	public static BufferedImage getImagen() {
		return imagenes[rand.nextInt(100)];
	}

	/**
	 * Este metodo se encarga de tomar las imagenes, para luego guardarlas en un
	 * bufferedimage.
	 * 
	 * @param index es la posicion en donde se guardará la imagen.
	 */
	public static void crearImagenes(int index) {
		try {
			imagenes[index] = ImageIO
					.read(new File(new File(".").getAbsolutePath() + "\\res\\fondos\\" + (index + 1) + ".jpg"));
		} catch (Exception e) {
			imagenes[index] = null;
		}
	}
}
