package main;

import javax.swing.SwingUtilities;

import com.formdev.flatlaf.FlatDarculaLaf;

import utilidadesInterfaces.ImagenesAleatorias;
import utilidadesInterfaces.PantallaCarga;
import ventanas.InicioSesion;

/**
 * Esta es una clase principal que dirige todo el proyecto :)
 * 
 * @author Sebastian Morgado
 * @version 2.0.0
 * @since 1.0.0
 */

public class Principal { // NO_UCD (unused code)
	/**
	 * Este es el main. dado a como diseñe el codigo, solo es necesario llamar a una
	 * funcion ({@link ventanas.InicioSesion#InicioSesion inicio sesion}).
	 * 
	 * @param args son los argumentos que se le pasan al main.
	 */

	public static void main(String... args) {

		FlatDarculaLaf.install();

		PantallaCarga ventana = new PantallaCarga();
		ventana.setVisible(true);
		for (int i = 0; i < 100; i++) {
			ventana.barraProgreso.setValue(i + 1);
			ImagenesAleatorias.crearImagenes(i);
			ventana.lblPorcentaje.setText(Integer.toString(i + 1) + " %");
			if (i == 99) {
				ventana.dispose();
			}
		}

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new InicioSesion();
			}
		});
	}
}
