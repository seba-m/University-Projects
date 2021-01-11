package ventanas;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.sql.Connection;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

import base.ServidorSql;
import base.Texto;
import utilidadesInterfaces.ImagenesAleatorias;
import utilidadesInterfaces.Mensajes;
import utilidadesInterfaces.TextPrompt;

/**
 * Esta clase se encarga de hacer todo lo relacionado con el inicio de sesión.
 * 
 * @author Sebastian orgado
 * @version 2.0.0
 * @since 2.0.0
 */
public class InicioSesion extends JFrame {

	private final static String url = "jdbc:mysql://localhost:3306/";
	private final static String NombreDB = "proyecto_db";
	private final static String Driver = "com.mysql.cj.jdbc.Driver";
	private final static String Usuario = "seba";
	private final static String Contrasena = "seba123";

	private JPanel PanelPrincipal;
	private JTextField txtFieldNombres;
	private JTextField txtFieldApellidos;
	private JTextField txtFieldRut;
	private String rut;
	private JPasswordField contrasena;
	private JPasswordField contrasenaRep;
	private JRadioButton rdBotonAlumno;
	private JRadioButton rdBotonProfesor;
	private Connection conexion;

	private static final long serialVersionUID = 5962255892409092153L;

	// region del panel del rut.

	/**
	 * Este panel le pide al usuario que ingrese su rut.
	 * 
	 * @return retorna el panel generado.
	 */
	private JPanel panelRut() {

		setTitle("TestMaker - Ingrese Su Rut");

		JPanel Panel = new JPanel(new BorderLayout()) {
			private static final long serialVersionUID = 8535815456803816207L;

			@Override
			protected void paintComponent(Graphics g) {
				BufferedImage img = ImagenesAleatorias.getImagen();
				if (img != null) {
					super.paintComponent(g);
					g.drawImage(img, 0, 0, 1000, 669, 0, 0, img.getWidth(), img.getHeight(), this);
				} else {
					super.paintComponent(g);
					g.setColor(Color.decode("#0D5B8C"));
				}
			}
		};
		Panel.setBackground(Color.decode("#0D5B8C"));
		Panel.setBorder(new EmptyBorder(new Insets(182, 300, 181, 300)));

		JPanel panel = new JPanel(new BorderLayout(0, 20));
		panel.setBorder(new EmptyBorder(new Insets(20, 30, 20, 30)));
		panel.setBackground(Color.decode("#FFFFFF"));
		Panel.add(panel, BorderLayout.CENTER);

		// titulo
		JLabel lblIniciarSesin = new JLabel("Iniciar Sesión");
		lblIniciarSesin.setForeground(Color.decode("#707070"));
		lblIniciarSesin.setFont(new Font("Segoe UI", Font.PLAIN, 35));
		panel.add(lblIniciarSesin, BorderLayout.NORTH);

		// panel del centro

		JPanel panelCentral = new JPanel();
		panelCentral.setBackground(Color.decode("#ffffff"));
		panelCentral.setLayout(new BoxLayout(panelCentral, BoxLayout.Y_AXIS));
		panel.add(panelCentral, BorderLayout.CENTER);

		// panel central superior
		JPanel panelCentralSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panelCentralSuperior.setBackground(Color.decode("#ffffff"));
		panelCentral.add(panelCentralSuperior);

		txtFieldRut = new JTextField(12);
		txtFieldRut.setBackground(Color.decode("#ffffff"));
		txtFieldRut.setFont(new Font("Segoe UI", Font.PLAIN, 30));
		txtFieldRut.setForeground(Color.decode("#707070"));
		txtFieldRut.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
		txtFieldRut.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				int cantidadMaximaCaracteres = 12;

				char letraIngresada = e.getKeyChar();

				if (!((letraIngresada >= '0') && (letraIngresada <= '9') || (letraIngresada == '-')
						|| (letraIngresada == '.') || (letraIngresada == 'k')
						|| (letraIngresada == KeyEvent.VK_BACK_SPACE) || (letraIngresada == KeyEvent.VK_DELETE))) {
					e.consume();
				}

				if ((letraIngresada == 'k') && txtFieldRut.getText().contains("k")) {
					e.consume();
				}

				if ((letraIngresada == '-') && txtFieldRut.getText().contains("-")) {
					e.consume();
				}

				if (txtFieldRut.getText().length() >= cantidadMaximaCaracteres) {
					e.consume();
				}
			}
		});
		txtFieldRut.addActionListener(new botonSiguientePanelRut_Listener());

		TextPrompt textoPantalla = new TextPrompt("Rut", txtFieldRut);
		textoPantalla.setFont(new Font("Segoe UI", Font.PLAIN, 25));
		panelCentralSuperior.add(txtFieldRut);

		// panel central inferior

		JPanel panelCentralInferior = new JPanel(new BorderLayout());
		panelCentralInferior.setBackground(Color.decode("#ffffff"));
		panelCentral.add(panelCentralInferior);

		JLabel lblCuenta = new JLabel("¿No tiene una cuenta?");
		lblCuenta.setForeground(Color.decode("#707070"));
		lblCuenta.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		panelCentralInferior.add(lblCuenta, BorderLayout.WEST);

		JButton botonCrearCuenta = new JButton("Crear una");
		botonCrearCuenta.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				cambiarPanel(panelCrearCuenta());
			}
		});
		botonCrearCuenta.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		botonCrearCuenta.setForeground(Color.decode("#0067B8"));
		botonCrearCuenta.setContentAreaFilled(false);
		panelCentralInferior.add(botonCrearCuenta, BorderLayout.CENTER);

		// panel inferior

		JPanel panelInferior = new JPanel(new BorderLayout());
		panelInferior.setBackground(Color.decode("#ffffff"));
		panel.add(panelInferior, BorderLayout.SOUTH);

		JButton botonSiguientePanelRut = new JButton("Siguiente");
		botonSiguientePanelRut.addActionListener(new botonSiguientePanelRut_Listener());
		botonSiguientePanelRut.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		botonSiguientePanelRut.setBackground(Color.decode("#0077FF"));
		panelInferior.add(botonSiguientePanelRut, BorderLayout.EAST);

		return Panel;
	}

	/**
	 * Este panel le pide al usuario que ingrese su rut nuevamente. Además le
	 * muestra un mensaje con el error que hubo.
	 * 
	 * @param mensajeError en caso de que el usuario falle al ingresar su rut,
	 *                     mostrara un mensaje de error distinto en el panel.
	 * 
	 * @return retorna el panel generado.
	 */
	private JPanel panelRutIncorrecto(String mensajeError) {

		setTitle("TestMaker - Ingrese Su Rut");

		JPanel Panel = new JPanel(new BorderLayout()) {

			private static final long serialVersionUID = 6151834627187758643L;

			@Override
			protected void paintComponent(Graphics g) {
				BufferedImage img = ImagenesAleatorias.getImagen();
				if (img != null) {
					super.paintComponent(g);
					g.drawImage(img, 0, 0, 1000, 669, 0, 0, img.getWidth(), img.getHeight(), this);
				} else {
					super.paintComponent(g);
					g.setColor(Color.decode("#0D5B8C"));
				}
			}
		};
		Panel.setBackground(Color.decode("#0D5B8C"));
		Panel.setBorder(new EmptyBorder(new Insets(152, 300, 151, 300)));

		JPanel panel = new JPanel(new BorderLayout(0, 20));
		panel.setBorder(new EmptyBorder(new Insets(20, 20, 20, 20)));
		panel.setBackground(Color.decode("#FFFFFF"));
		Panel.add(panel, BorderLayout.CENTER);

		// titulo
		JLabel lblIniciarSesin = new JLabel("Iniciar Sesión");
		lblIniciarSesin.setForeground(Color.decode("#707070"));
		lblIniciarSesin.setFont(new Font("Segoe UI", Font.PLAIN, 35));
		panel.add(lblIniciarSesin, BorderLayout.NORTH);

		// panel del centro

		JPanel panelCentral = new JPanel();
		panelCentral.setBackground(Color.decode("#ffffff"));
		panelCentral.setLayout(new BoxLayout(panelCentral, BoxLayout.Y_AXIS));
		panel.add(panelCentral, BorderLayout.CENTER);

		// panel central superior
		JPanel panelCentralSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panelCentralSuperior.setBackground(Color.decode("#ffffff"));
		panelCentral.add(panelCentralSuperior);

		txtFieldRut = new JTextField(12);
		txtFieldRut.setBackground(Color.decode("#ffffff"));
		txtFieldRut.setFont(new Font("Segoe UI", Font.PLAIN, 30));
		txtFieldRut.setForeground(Color.decode("#707070"));
		txtFieldRut.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
		txtFieldRut.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				int cantidadMaximaCaracteres = 12;

				char letraIngresada = e.getKeyChar();

				if (!((letraIngresada >= '0') && (letraIngresada <= '9') || (letraIngresada == '-')
						|| (letraIngresada == '.') || (letraIngresada == 'k')
						|| (letraIngresada == KeyEvent.VK_BACK_SPACE) || (letraIngresada == KeyEvent.VK_DELETE))) {
					e.consume();
				}

				if ((letraIngresada == 'k') && txtFieldRut.getText().contains("k")) {
					e.consume();
				}

				if ((letraIngresada == '-') && txtFieldRut.getText().contains("-")) {
					e.consume();
				}

				if (txtFieldRut.getText().length() >= cantidadMaximaCaracteres) {
					e.consume();
				}
			}
		});
		txtFieldRut.addActionListener(new botonSiguientePanelRut_Listener());

		TextPrompt textoPantalla = new TextPrompt("Rut", txtFieldRut);
		textoPantalla.setFont(new Font("Segoe UI", Font.PLAIN, 25));
		panelCentralSuperior.add(txtFieldRut);

		// panel central central (?)

		JPanel panelCentralCentral = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panelCentralCentral.setBackground(Color.decode("#ffffff"));
		panelCentral.add(panelCentralCentral);

		JLabel lblError = new JLabel("<html>" + mensajeError + "</html>");
		lblError.setForeground(Color.decode("#FF0000"));
		lblError.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		panelCentralCentral.add(lblError);

		// panel central inferior

		JPanel panelCentralInferior = new JPanel(new BorderLayout());
		panelCentralInferior.setBackground(Color.decode("#ffffff"));
		panelCentral.add(panelCentralInferior);

		JLabel lblCuenta = new JLabel("¿No tiene una cuenta?");
		lblCuenta.setForeground(Color.decode("#707070"));
		lblCuenta.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		panelCentralInferior.add(lblCuenta, BorderLayout.WEST);

		JButton botonCrearCuenta = new JButton("Crear una");
		botonCrearCuenta.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				cambiarPanel(panelCrearCuenta());
			}
		});
		botonCrearCuenta.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		botonCrearCuenta.setForeground(Color.decode("#0067B8"));
		botonCrearCuenta.setContentAreaFilled(false);
		panelCentralInferior.add(botonCrearCuenta, BorderLayout.CENTER);

		// panel inferior

		JPanel panelInferior = new JPanel(new BorderLayout());
		panelInferior.setBackground(Color.decode("#ffffff"));
		panel.add(panelInferior, BorderLayout.SOUTH);

		JButton botonSiguientePanelRut = new JButton("Siguiente");
		botonSiguientePanelRut.addActionListener(new botonSiguientePanelRut_Listener());
		botonSiguientePanelRut.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		botonSiguientePanelRut.setBackground(Color.decode("#0077FF"));
		panelInferior.add(botonSiguientePanelRut, BorderLayout.EAST);

		return Panel;
	}

	/**
	 * Esta clase, es el actionlistener del boton siguiente, del panel de ingresar
	 * rut.
	 */
	private class botonSiguientePanelRut_Listener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			try {
				conexion = ServidorSql.conectarse_a_server(url, NombreDB, Driver, Usuario, Contrasena);
				if (txtFieldRut.getText().isBlank()) {
					cambiarPanel(panelRutIncorrecto("Por favor ingrese su rut."));
					return;
				}

				String rutUsuario = txtFieldRut.getText().replace(".", "").replace("-", "");

				if (!Texto.validarRut(rutUsuario)) {
					cambiarPanel(panelRutIncorrecto("Por favor ingrese un rut valido."));
					return;
				}

				if (!ServidorSql.verificar_usuario_existente(conexion, rutUsuario)) {
					cambiarPanel(panelRutIncorrecto("No existe una cuenta con ese rut,<br>intenta crear una cuenta."));
					return;
				}

				rut = rutUsuario;

				cambiarPanel(panelContrasena());
			} catch (Exception e2) {
				Mensajes.mensaje(e2.getMessage());
				return;
			}
		}
	}

	// endregion

	// region del panel de cambiar contraseña.

	/**
	 * Este panel le pide al usuario que ingrese la nueva contraseña, para poder
	 * cambiarla.
	 * 
	 * @return retorna el panel generado.
	 */
	private JPanel panelCambiarContrasena() {

		setTitle("TestMaker - Cambiar Contraseña");

		JPanel Panel = new JPanel(new BorderLayout()) {

			private static final long serialVersionUID = -8455145623267209986L;

			@Override
			protected void paintComponent(Graphics g) {
				BufferedImage img = ImagenesAleatorias.getImagen();
				if (img != null) {
					super.paintComponent(g);
					g.drawImage(img, 0, 0, 1000, 669, 0, 0, img.getWidth(), img.getHeight(), this);
				} else {
					super.paintComponent(g);
					g.setColor(Color.decode("#252463"));
				}
			}
		};
		Panel.setBackground(Color.decode("#252463"));
		Panel.setBorder(new EmptyBorder(new Insets(150, 150, 150, 150)));

		JPanel panel = new JPanel(new BorderLayout(0, 20));
		panel.setBorder(new EmptyBorder(new Insets(20, 30, 20, 30)));
		panel.setBackground(Color.decode("#FFFFFF"));
		Panel.add(panel, BorderLayout.CENTER);

		// titulo

		JLabel lblCambiarContra = new JLabel("Cambiar Contraseña");
		lblCambiarContra.setForeground(Color.decode("#707070"));
		lblCambiarContra.setFont(new Font("Segoe UI", Font.PLAIN, 35));
		panel.add(lblCambiarContra, BorderLayout.NORTH);

		// panel central

		JPanel panelCentral = new JPanel();
		panelCentral.setBackground(Color.decode("#ffffff"));
		panelCentral.setLayout(new BoxLayout(panelCentral, BoxLayout.Y_AXIS));
		panel.add(panelCentral, BorderLayout.CENTER);

		// panel rut

		JPanel panelRut = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
		panelRut.setBackground(Color.decode("#ffffff"));
		panelCentral.add(panelRut);

		txtFieldRut = new JTextField(21);
		txtFieldRut.setFont(new Font("Segoe UI", Font.PLAIN, 30));
		txtFieldRut.setBackground(Color.decode("#ffffff"));
		txtFieldRut.setForeground(Color.decode("#707070"));
		txtFieldRut.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
		new TextPrompt("Rut", txtFieldRut);
		panelRut.add(txtFieldRut);

		// panel contraseña y confirmacion contraseña

		panelCentral.add(Box.createRigidArea(new Dimension(0, 25)));

		JPanel panelContrasena = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
		panelContrasena.setBackground(Color.decode("#ffffff"));
		panelCentral.add(panelContrasena);

		contrasena = new JPasswordField(10);
		contrasena.setFont(new Font("Segoe UI", Font.PLAIN, 30));
		contrasena.setBackground(Color.decode("#ffffff"));
		contrasena.setForeground(Color.decode("#707070"));
		contrasena.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
		new TextPrompt("Contraseña", contrasena);
		panelContrasena.add(contrasena);

		contrasenaRep = new JPasswordField(10);
		contrasenaRep.setFont(new Font("Segoe UI", Font.PLAIN, 30));
		contrasenaRep.setBackground(Color.decode("#ffffff"));
		contrasenaRep.setForeground(Color.decode("#707070"));
		contrasenaRep.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
		new TextPrompt("Confirmación", contrasenaRep);
		panelContrasena.add(contrasenaRep);

		// panel inferior

		JPanel panelInferior = new JPanel(new BorderLayout());
		panelInferior.setBackground(Color.decode("#ffffff"));
		panel.add(panelInferior, BorderLayout.SOUTH);

		JButton botonCancelar = new JButton("Cancelar");
		botonCancelar.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		botonCancelar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				cambiarPanel(panelContrasena());
			}
		});
		panelInferior.add(botonCancelar, BorderLayout.WEST);

		JButton botonFinalizar = new JButton("Finalizar");
		botonFinalizar.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		botonFinalizar.setBackground(Color.decode("#0077FF"));
		botonFinalizar.setForeground(Color.decode("#FFFFFF"));
		botonFinalizar.addActionListener(new botonFinalizarPanelCambiarContrasena_Listener());
		panelInferior.add(botonFinalizar, BorderLayout.EAST);

		return Panel;
	}

	/**
	 * Esta clase, es el actionlistener del boton finalizar, del panel de cambiar
	 * contraseña.
	 */
	private class botonFinalizarPanelCambiarContrasena_Listener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			if (!Texto.validarRut(txtFieldRut.getText().replace(".", "").replace("-", ""))) {
				Mensajes.mensaje("El rut ingresado es invalido, intentelo nuevamente.");
				return;
			}

			String tmp1 = "";
			String tmp2 = "";

			for (char c : contrasena.getPassword()) {
				tmp1 += c;
			}

			for (char c : contrasenaRep.getPassword()) {
				tmp2 += c;
			}

			if (!tmp1.equals(tmp2)) {
				Mensajes.mensaje("Las contraseñas no coinciden, intentalo nuevamente.");
				return;
			}

			try {
				ServidorSql.cambiar_contrasena(conexion, rut, tmp2);
			} catch (Exception e2) {
				Mensajes.mensaje(e2.getMessage());
				return;
			}

			cambiarPanel(panelRut());
		}
	}

	// endregion

	// region del panel de ingresar contraseña.

	/**
	 * Este panel le pide al usuario que ingrese su contraseña.
	 * 
	 * @return retorna el panel generado.
	 */
	private JPanel panelContrasena() {

		setTitle("TestMaker - Ingrese Su Contraseña");

		JPanel Panel = new JPanel(new BorderLayout()) {

			private static final long serialVersionUID = 3993221281408001297L;

			@Override
			protected void paintComponent(Graphics g) {
				BufferedImage img = ImagenesAleatorias.getImagen();
				if (img != null) {
					super.paintComponent(g);
					g.drawImage(img, 0, 0, 1000, 669, 0, 0, img.getWidth(), img.getHeight(), this);
				} else {
					super.paintComponent(g);
					g.setColor(Color.decode("#048ABF"));
				}
			}
		};
		Panel.setBackground(Color.decode("#048ABF"));
		Panel.setBorder(new EmptyBorder(new Insets(182, 290, 181, 290)));

		JPanel panel = new JPanel(new BorderLayout(0, 20));
		panel.setBorder(new EmptyBorder(new Insets(20, 30, 20, 30)));
		panel.setBackground(Color.decode("#FFFFFF"));
		Panel.add(panel, BorderLayout.CENTER);

		// titulo

		JLabel lblIniciarSesion = new JLabel("Iniciar Sesión");
		lblIniciarSesion.setForeground(Color.decode("#707070"));
		lblIniciarSesion.setFont(new Font("Segoe UI", Font.PLAIN, 35));
		panel.add(lblIniciarSesion, BorderLayout.NORTH);

		// panel del centro

		JPanel panelCentral = new JPanel();
		panelCentral.setBackground(Color.decode("#ffffff"));
		panelCentral.setLayout(new BoxLayout(panelCentral, BoxLayout.Y_AXIS));
		panel.add(panelCentral, BorderLayout.CENTER);

		// panel central superior
		JPanel panelCentralSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panelCentralSuperior.setBackground(Color.decode("#ffffff"));
		panelCentral.add(panelCentralSuperior);

		contrasena = new JPasswordField(15);
		contrasena.setBackground(Color.decode("#ffffff"));
		contrasena.setFont(new Font("Segoe UI", Font.PLAIN, 25));
		contrasena.setForeground(Color.decode("#707070"));
		contrasena.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
		contrasena.addActionListener(new botonSiguientePanelContrasena_Listener());
		new TextPrompt("Contraseña", contrasena);

		panelCentralSuperior.add(contrasena);

		// panel central inferior

		JPanel panelCentralInferior = new JPanel(new BorderLayout());
		panelCentralInferior.setBackground(Color.decode("#ffffff"));
		panelCentral.add(panelCentralInferior);

		JLabel lblContraPerdida = new JLabel("¿Olvidaste la contraseña?");
		lblContraPerdida.setForeground(Color.decode("#707070"));
		lblContraPerdida.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		panelCentralInferior.add(lblContraPerdida, BorderLayout.WEST);

		JButton botonRecuperar = new JButton("Recuperar");
		botonRecuperar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				cambiarPanel(panelCambiarContrasena());
			}
		});
		botonRecuperar.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		botonRecuperar.setForeground(Color.decode("#0067B8"));
		botonRecuperar.setContentAreaFilled(false);
		panelCentralInferior.add(botonRecuperar, BorderLayout.CENTER);

		// panel inferior

		JPanel panelInferior = new JPanel(new BorderLayout());
		panelInferior.setBackground(Color.decode("#ffffff"));
		panel.add(panelInferior, BorderLayout.SOUTH);

		JButton botonSiguiente = new JButton("Siguiente");
		botonSiguiente.setForeground(Color.decode("#FFFFFF"));
		botonSiguiente.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		botonSiguiente.addActionListener(new botonSiguientePanelContrasena_Listener());
		botonSiguiente.setBackground(Color.decode("#0077FF"));
		panelInferior.add(botonSiguiente, BorderLayout.EAST);

		return Panel;
	}

	/**
	 * Este panel le pide al usuario que ingrese su contraseña nuevamente. Además le
	 * muestra un mensaje con el error que hubo.
	 * 
	 * @param mensajeError en caso de que el usuario falle al ingresar su
	 *                     contraseña, mostrara un mensaje de error distinto en el
	 *                     panel.
	 * 
	 * @return retorna el panel generado.
	 */
	private JPanel panelContrasenaIncorrecta(String mensajeError) {

		setTitle("TestMaker - Ingrese Su Contraseña");

		JPanel Panel = new JPanel(new BorderLayout()) {

			private static final long serialVersionUID = 5373669032578714792L;

			@Override
			protected void paintComponent(Graphics g) {
				BufferedImage img = ImagenesAleatorias.getImagen();
				if (img != null) {
					super.paintComponent(g);
					g.drawImage(img, 0, 0, 1000, 669, 0, 0, img.getWidth(), img.getHeight(), this);
				} else {
					super.paintComponent(g);
					g.setColor(Color.decode("#252463"));
				}
			}
		};
		Panel.setBackground(Color.decode("#252463"));
		Panel.setBorder(new EmptyBorder(new Insets(152, 290, 151, 290)));

		JPanel panel = new JPanel(new BorderLayout(0, 20));
		panel.setBorder(new EmptyBorder(new Insets(20, 30, 20, 30)));
		panel.setBackground(Color.decode("#FFFFFF"));
		Panel.add(panel, BorderLayout.CENTER);

		// titulo

		JLabel lblIniciarSesion = new JLabel("Iniciar Sesión");
		lblIniciarSesion.setForeground(Color.decode("#707070"));
		lblIniciarSesion.setFont(new Font("Segoe UI", Font.PLAIN, 35));
		panel.add(lblIniciarSesion, BorderLayout.NORTH);

		// panel del centro

		JPanel panelCentral = new JPanel();
		panelCentral.setBackground(Color.decode("#ffffff"));
		panelCentral.setLayout(new BoxLayout(panelCentral, BoxLayout.Y_AXIS));
		panel.add(panelCentral, BorderLayout.CENTER);

		// panel central superior
		JPanel panelCentralSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panelCentralSuperior.setBackground(Color.decode("#ffffff"));
		panelCentral.add(panelCentralSuperior);

		contrasena = new JPasswordField(15);
		contrasena.setBackground(Color.decode("#ffffff"));
		contrasena.setFont(new Font("Segoe UI", Font.PLAIN, 25));
		contrasena.setForeground(Color.decode("#707070"));
		contrasena.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
		contrasena.addActionListener(new botonSiguientePanelContrasena_Listener());
		new TextPrompt("Contraseña", contrasena);

		panelCentralSuperior.add(contrasena);

		// panel central central (?)

		JPanel panelCentralCentral = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panelCentralCentral.setBackground(Color.decode("#ffffff"));
		panelCentral.add(panelCentralCentral);

		JLabel lblError = new JLabel("<html>" + mensajeError + "</html>");
		lblError.setForeground(Color.decode("#FF0000"));
		lblError.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		panelCentralCentral.add(lblError);

		// panel central inferior

		JPanel panelCentralInferior = new JPanel(new BorderLayout());
		panelCentralInferior.setBackground(Color.decode("#ffffff"));
		panelCentral.add(panelCentralInferior);

		JLabel lblContraPerdida = new JLabel("¿Olvidaste la contraseña?");
		lblContraPerdida.setForeground(Color.decode("#707070"));
		lblContraPerdida.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		panelCentralInferior.add(lblContraPerdida, BorderLayout.WEST);

		JButton botonRecuperar = new JButton("Recuperar");
		botonRecuperar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				cambiarPanel(panelCambiarContrasena());
			}
		});
		botonRecuperar.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		botonRecuperar.setForeground(Color.decode("#0067B8"));
		botonRecuperar.setContentAreaFilled(false);
		panelCentralInferior.add(botonRecuperar, BorderLayout.CENTER);

		// panel inferior

		JPanel panelInferior = new JPanel(new BorderLayout());
		panelInferior.setBackground(Color.decode("#ffffff"));
		panel.add(panelInferior, BorderLayout.SOUTH);

		JButton botonSiguiente = new JButton("Siguiente");
		botonSiguiente.setForeground(Color.decode("#FFFFFF"));
		botonSiguiente.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		botonSiguiente.addActionListener(new botonSiguientePanelContrasena_Listener());
		botonSiguiente.setBackground(Color.decode("#0077FF"));
		panelInferior.add(botonSiguiente, BorderLayout.EAST);

		return Panel;

	}

	/**
	 * Esta clase, es el actionlistener del boton siguiente, del panel de contraseña
	 * incorrecta.
	 */
	private class botonSiguientePanelContrasena_Listener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			char[] passArr = contrasena.getPassword();

			String pass = "";

			for (char c : passArr) {
				pass += c;
			}

			if (pass.isBlank()) {
				cambiarPanel(panelContrasenaIncorrecta("por favor ingrese alguna contraseña."));
				return;
			}

			try {

				conexion = ServidorSql.conectarse_a_server(url, NombreDB, Driver, Usuario, Contrasena);

				int usuario_valido = ServidorSql.verificar_usuario(conexion, rut, pass);

				if (usuario_valido != -1) {
					if (usuario_valido == 1) {

						setTitle("TestMaker - Menú profesor");

						String[] resultados = ServidorSql.ObtenerDatos(conexion,
								txtFieldRut.getText().replace(".", "").replace("-", ""));

						cambiarPanel(new Profesor(resultados[0], resultados[1]));

					} else if (usuario_valido == 0) {

						setTitle("TestMaker - Menú alumno");

						String[] resultados = ServidorSql.ObtenerDatos(conexion, rut);

						cambiarPanel(new Alumno(resultados[0], resultados[1], resultados[2]));

					} else if (usuario_valido == 2) {
						String[] resultados = ServidorSql.ObtenerDatos(conexion, rut);
						Object[] options = { "Ir al menu del profesor", "Ir al menu del Alumno", "Cerrar sesión" };

						int n = JOptionPane.showOptionDialog(null, "¿Qué acción desea realizar?", "Administrador",
								JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options,
								options[2]);

						if (n == 0) {
							setTitle("TestMaker - Menú profesor");
							cambiarPanel(new Profesor(resultados[0], resultados[1]));
							return;
						} else if (n == 1) {
							setTitle("TestMaker - Menú alumno");
							cambiarPanel(new Alumno(resultados[0], resultados[1], resultados[2]));
							return;
						} else if (n == 2) {
							dispose();
							new InicioSesion();
						} else {
							dispose();
							System.exit(0);
						}

					} else {
						cambiarPanel(panelRutIncorrecto(
								"Tu cuenta ha sido cancelada. contacta al<br>administrador para resolver tu situación."));
						return;
					}

				} else if (!ServidorSql.verificar_usuario_existente(conexion, rut)) {
					Mensajes.mensaje("No existe una cuenta con ese rut, intenta crear una cuenta.");
				} else {
					cambiarPanel(panelContrasenaIncorrecta(
							"Contraseña incorrecta, si no la recuerdas,<br>puedes recuperarla."));
				}
			} catch (Exception e1) {
				cambiarPanel(panelContrasenaIncorrecta(e1.getMessage()));
			}
		}
	}

	// endregion

	// region del panel de crear cuenta.

	/**
	 * Este panel le pide al usuario que ingrese los datos requeridos, para poder
	 * crear una cuenta.
	 * 
	 * @return retorna el panel generado.
	 */
	private JPanel panelCrearCuenta() {

		setTitle("TestMaker - Crear Cuenta");

		JPanel Panel = new JPanel(new BorderLayout()) {

			private static final long serialVersionUID = -5610145350291649947L;

			@Override
			protected void paintComponent(Graphics g) {
				BufferedImage img = ImagenesAleatorias.getImagen();
				if (img != null) {
					super.paintComponent(g);
					g.drawImage(img, 0, 0, 1000, 669, 0, 0, img.getWidth(), img.getHeight(), this);
				} else {
					super.paintComponent(g);
					g.setColor(Color.decode("#252463"));
				}
			}
		};
		Panel.setBackground(Color.decode("#252463"));
		Panel.setBorder(new EmptyBorder(new Insets(50, 150, 50, 150)));

		JPanel panel = new JPanel(new BorderLayout(0, 20));
		panel.setBorder(new EmptyBorder(new Insets(20, 30, 20, 30)));
		panel.setBackground(Color.decode("#FFFFFF"));
		Panel.add(panel, BorderLayout.CENTER);

		// titulo

		JLabel lblIniciarSesin = new JLabel("Crear Cuenta");
		lblIniciarSesin.setForeground(Color.decode("#707070"));
		lblIniciarSesin.setFont(new Font("Segoe UI", Font.PLAIN, 35));
		panel.add(lblIniciarSesin, BorderLayout.NORTH);

		// panel del centro

		JPanel panelCentral = new JPanel();
		panelCentral.setBackground(Color.decode("#ffffff"));
		panelCentral.setLayout(new BoxLayout(panelCentral, BoxLayout.Y_AXIS));
		panel.add(panelCentral, BorderLayout.CENTER);

		// panel nombres y apellidos

		JPanel panelNombresYApellidos = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
		panelNombresYApellidos.setBackground(Color.decode("#ffffff"));
		panelCentral.add(panelNombresYApellidos);

		txtFieldNombres = new JTextField(10);
		txtFieldNombres.setFont(new Font("Segoe UI", Font.PLAIN, 30));
		txtFieldNombres.setBackground(Color.decode("#ffffff"));
		txtFieldNombres.setForeground(Color.decode("#707070"));
		txtFieldNombres.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
		new TextPrompt("Nombres", txtFieldNombres);
		panelNombresYApellidos.add(txtFieldNombres);

		txtFieldApellidos = new JTextField(10);
		txtFieldApellidos.setFont(new Font("Segoe UI", Font.PLAIN, 30));
		txtFieldApellidos.setBackground(Color.decode("#ffffff"));
		txtFieldApellidos.setForeground(Color.decode("#707070"));
		txtFieldApellidos.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
		new TextPrompt("Apellidos", txtFieldApellidos);
		panelNombresYApellidos.add(txtFieldApellidos);

		// panel rut

		panelCentral.add(Box.createRigidArea(new Dimension(0, 25)));

		JPanel panelRut = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
		panelRut.setBackground(Color.decode("#ffffff"));
		panelCentral.add(panelRut);

		txtFieldRut = new JTextField(21);
		txtFieldRut.setFont(new Font("Segoe UI", Font.PLAIN, 30));
		txtFieldRut.setBackground(Color.decode("#ffffff"));
		txtFieldRut.setForeground(Color.decode("#707070"));
		txtFieldRut.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
		new TextPrompt("Rut", txtFieldRut);
		panelRut.add(txtFieldRut);

		// panel contraseña y confirmacion contraseña

		panelCentral.add(Box.createRigidArea(new Dimension(0, 25)));

		JPanel panelContrasena = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
		panelContrasena.setBackground(Color.decode("#ffffff"));
		panelCentral.add(panelContrasena);

		contrasena = new JPasswordField(10);
		contrasena.setFont(new Font("Segoe UI", Font.PLAIN, 30));
		contrasena.setBackground(Color.decode("#ffffff"));
		contrasena.setForeground(Color.decode("#707070"));
		contrasena.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
		new TextPrompt("Contraseña", contrasena);
		panelContrasena.add(contrasena);

		contrasenaRep = new JPasswordField(10);
		contrasenaRep.setFont(new Font("Segoe UI", Font.PLAIN, 30));
		contrasenaRep.setBackground(Color.decode("#ffffff"));
		contrasenaRep.setForeground(Color.decode("#707070"));
		contrasenaRep.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
		new TextPrompt("Confirmación", contrasenaRep);
		panelContrasena.add(contrasenaRep);

		// consulta tipo usuario

		panelCentral.add(Box.createRigidArea(new Dimension(0, 25)));

		JPanel panelTipoUsuario = new JPanel();
		panelTipoUsuario.setBackground(Color.decode("#ffffff"));
		panelTipoUsuario.setLayout(new BoxLayout(panelTipoUsuario, BoxLayout.Y_AXIS));
		panelCentral.add(panelTipoUsuario);

		JLabel lblProfesorOAlumno = new JLabel("¿Es usted un alumno o un profesor?");
		lblProfesorOAlumno.setFont(new Font("Segoe UI", Font.PLAIN, 17));
		lblProfesorOAlumno.setForeground(Color.decode("#000000"));
		panelTipoUsuario.add(lblProfesorOAlumno);

		panelCentral.add(Box.createRigidArea(new Dimension(0, 10)));

		// panel radio botones

		JPanel panelBotones = new JPanel(new GridLayout(1, 2, 20, 0));
		panelBotones.setBackground(Color.decode("#ffffff"));
		panelCentral.add(panelBotones);

		rdBotonAlumno = new JRadioButton("Alumno");
		rdBotonAlumno.setHorizontalAlignment(SwingConstants.RIGHT);
		rdBotonAlumno.setFont(new Font("Segoe UI", Font.PLAIN, 17));
		rdBotonAlumno.setForeground(Color.decode("#000000"));
		rdBotonAlumno.setSelected(true);
		panelBotones.add(rdBotonAlumno);

		rdBotonProfesor = new JRadioButton("Profesor");
		rdBotonProfesor.setFont(new Font("Segoe UI", Font.PLAIN, 17));
		rdBotonProfesor.setForeground(Color.decode("#000000"));
		panelBotones.add(rdBotonProfesor);

		ButtonGroup botones = new ButtonGroup();
		botones.add(rdBotonAlumno);
		botones.add(rdBotonProfesor);

		// panel inferior

		JPanel panelInferior = new JPanel(new BorderLayout());
		panelInferior.setBackground(Color.decode("#ffffff"));
		panel.add(panelInferior, BorderLayout.SOUTH);

		JButton botonCancelar = new JButton("Cancelar");
		botonCancelar.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		botonCancelar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				cambiarPanel(panelRut());
			}
		});
		panelInferior.add(botonCancelar, BorderLayout.WEST);

		JButton botonFinalizar = new JButton("Finalizar");
		botonFinalizar.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		botonFinalizar.setForeground(Color.decode("#FFFFFF"));
		botonFinalizar.setBackground(Color.decode("#0077FF"));
		botonFinalizar.addActionListener(new botonFinalizarPanelCrearCuenta_Listener());
		panelInferior.add(botonFinalizar, BorderLayout.EAST);

		return Panel;
	}

	/**
	 * Esta clase, es el actionlistener del boton finalizar, del panel de crear
	 * cuenta.
	 */
	private class botonFinalizarPanelCrearCuenta_Listener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			try {
				String rutFinal = txtFieldRut.getText().replace(".", "").replace("-", "");
				if (comprobarDatosIngresados(txtFieldNombres.getText(), txtFieldApellidos.getText(), rutFinal,
						contrasena.getPassword(), contrasenaRep.getPassword())) {

					String contrasenaString = "";

					for (char c : contrasena.getPassword()) {
						contrasenaString += c;
					}

					if (!ServidorSql.verificar_usuario_existente(conexion, rutFinal)) {

						if (rdBotonAlumno.isSelected()) {
							if (ServidorSql.crear_cuenta(conexion, txtFieldNombres.getText(),
									txtFieldApellidos.getText(), rutFinal, contrasenaString, "0")) {
								Mensajes.mensaje("Cuenta creada correctamente");
								return;
							} else {
								Mensajes.error("Cuenta creada incorrectamente");
								return;
							}
						} else if (rdBotonProfesor.isSelected()) {
							if (ServidorSql.crear_cuenta(conexion, txtFieldNombres.getText(),
									txtFieldApellidos.getText(), rutFinal, contrasenaString, "1")) {
								Mensajes.mensaje("Cuenta creada correctamente");
								return;
							} else {
								Mensajes.error("Cuenta creada incorrectamente");
								return;
							}
						} else {
							Mensajes.mensaje("Por favor seleccione el rango que desea obtener.");
							System.exit(0);
						}
					} else {
						Mensajes.error("ya hay un usuario con esos datos, intente ingresar o cambie su contraseña!");
						return;
					}
				}
			} catch (Exception e2) {
				Mensajes.mensaje(e2.getMessage());
				return;
			}

			cambiarPanel(panelRut());
		}
	}

	// endregion

	// region de metodos.

	/**
	 * Este es el constructor de la clase inicio sesión.
	 */
	public InicioSesion() {

		Timer timer = new Timer(10000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				repaint();
			}
		});
		timer.start();

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("TestMaker - Inicio sesión");
		setResizable(false);

		try {
			Image im = ImageIO.read(new File(new File(".").getCanonicalPath() + "\\res\\icono.png"));
			setIconImage(im);
		} catch (Exception ex) {
		}

		PanelPrincipal = new JPanel(new BorderLayout());
		PanelPrincipal.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(PanelPrincipal);

		PanelPrincipal.add(panelRut());

		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	/**
	 * Este metodo permite cambiar el panel actual, por el otro que se desea usar.
	 * 
	 * @param panel es el panela mostrar.
	 */
	private void cambiarPanel(JPanel panel) {

		getContentPane().removeAll();
		PanelPrincipal.add(panel);
		setContentPane(PanelPrincipal);
	}

	/**
	 * Este metodo comprueba si los datos ingresados por el usuario son validos.
	 * 
	 * @param Nombre         es el nombre del usuario a verificar.
	 * @param Apellidos      es el apellido del usuario a verificar.
	 * @param Rut            es el rut del usuario a verificar.
	 * @param Contra         es la contraseña del usuario a verificar.
	 * @param ContraRepetida es la contraseña repetida del usuario a verificar.
	 * 
	 * @return retorna {@code true} si los datos son validos, caso contrario
	 *         retornará {@code false}.
	 */
	private boolean comprobarDatosIngresados(String Nombre, String Apellidos, String Rut, char[] Contra,
			char[] ContraRepetida) {
		// primera parte: Nombre
		if (Nombre == null || Nombre.isBlank()) {
			throw new IllegalArgumentException("Por favor ingrese sus nombres.");
		} else if (Texto.TieneNumeros(Nombre)) {
			throw new IllegalArgumentException("Por favor sus nombres no debe llevar numeros.");
		}

		// segunda parte: Apellidos
		if (Apellidos.isBlank()) {
			throw new IllegalArgumentException("Por favor ingrese sus apellidos.");
		} else if (Texto.TieneNumeros(Apellidos)) {
			throw new IllegalArgumentException("Por favor sus apellidos no debe llevar numeros.");
		}

		// tercera parte: Rut
		if (!Texto.validarRut(Rut)) {
			throw new IllegalArgumentException("Por favor revise si el rut esta escrito correctamente.");
		}

		// cuarta parte: contraseña y contraseña repetida
		String tmp = "";
		String tmp1 = "";

		for (char c : Contra) {
			tmp += c;
		}

		if (tmp.isBlank()) {
			throw new IllegalArgumentException("Por favor ingrese una contraseña valida al repetir su contraseña.");
		}

		for (char c : ContraRepetida) {
			tmp1 += c;
		}

		if (tmp1.isBlank()) {
			throw new IllegalArgumentException("Por favor ingrese una contraseña valida.");
		}

		if (!tmp.equals(tmp1)) {
			throw new IllegalArgumentException("Las contraseñas no coinciden, intentelo nuevamente.");
		}
		return true;
	}

	// endregion

}
