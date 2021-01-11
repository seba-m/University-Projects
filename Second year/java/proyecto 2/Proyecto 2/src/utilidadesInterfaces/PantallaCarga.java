package utilidadesInterfaces;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Image;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.border.EmptyBorder;

/**
 * Esta clase se encarga de crear la pantalla de carga, la cual tiene por
 * objetivo obtener todas las imagenes de fondo.
 * 
 * @author Sebastian Morgado
 * @version 2.0.0
 * @since 2.0.0
 */
public class PantallaCarga extends JFrame {

	private static final long serialVersionUID = -5806570168784953355L;
	public JProgressBar barraProgreso;
	public JLabel lblPorcentaje;

	/**
	 * Este es el panel que muestra una barra de progreso, junto con un gif de
	 * carga, y con el porcentaje de progreso.
	 * 
	 * @return retorna el panel generado.
	 */
	private JPanel crearPantallaCarga() {
		JPanel panel = new JPanel(new BorderLayout(0, 10));
		panel.setBorder(new EmptyBorder(5, 5, 5, 5));
		panel.setBackground(Color.decode("#0E111F"));

		ImageIcon icono = null;

		JLabel gif = new JLabel();

		try {
			// primero verifico si existe el gif
			ImageIO.read(new File(new File(".").getAbsolutePath() + "\\res\\carga.gif"));
			icono = new ImageIcon((new ImageIcon(new File(".").getAbsolutePath() + "\\res\\carga.gif")).getImage()
					.getScaledInstance(400, 300, Image.SCALE_REPLICATE));
			// si existe, lo asigno al label
			gif.setIcon(icono);
			panel.add(gif, BorderLayout.CENTER);
		} catch (Exception e) {
			// si no existe, muestro un mensaje de error.
			gif.setText("error 404, no hay gif que mostrar :(");
			panel.add(gif, BorderLayout.NORTH);
		}

		JPanel porcentaje = new JPanel(new BorderLayout());
		porcentaje.setBackground(Color.decode("#0E111F"));
		panel.add(porcentaje, BorderLayout.SOUTH);

		lblPorcentaje = new JLabel();
		lblPorcentaje.setForeground(Color.WHITE);
		lblPorcentaje.setHorizontalAlignment(JLabel.CENTER);
		porcentaje.add(lblPorcentaje, BorderLayout.NORTH);

		barraProgreso = new JProgressBar();
		barraProgreso.setForeground(new Color(0, 128, 128));
		porcentaje.add(barraProgreso, BorderLayout.CENTER);

		return panel;
	}

	/**
	 * Constructor de la pantalla de carga. Con esto, se da inicio a la pantalla de
	 * carga.
	 * 
	 */
	public PantallaCarga() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setContentPane(crearPantallaCarga());
		setResizable(false);
		setTitle("TestMaker - Cargando...");

		try {
			Image im = ImageIO.read(new File(new File(".").getCanonicalPath() + "\\res\\icono.png"));
			setIconImage(im);
		} catch (Exception ex) {
		}
		pack();
		setLocationRelativeTo(null);
	}
}
