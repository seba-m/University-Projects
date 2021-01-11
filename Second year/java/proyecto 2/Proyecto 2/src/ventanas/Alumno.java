package ventanas;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

import base.Archivos;
import base.Examen;
import base.ExamenDemo;
import base.RespuestaNumerica;
import base.RespuestasCortas;
import base.SeleccionMultiple;
import base.Texto;
import base.VerdaderoFalso;
import utilidadesArchivos.ExportarAPDF;
import utilidadesInterfaces.ImagenesAleatorias;
import utilidadesInterfaces.Mensajes;
import utilidadesInterfaces.TextPrompt;

/**
 * Esta clase se encarga de hacer todo lo relacionado con el alumno.
 * 
 * @author Sebastian Morgado
 * @version 2.0.0
 * @since 2.0.0
 */
class Alumno extends JPanel {

	private static final long serialVersionUID = 5444109069928833942L;
	private static int preguntaActual = 0;
	private static int CANTIDADRESPUESTASCORRECTAS = 0;
	private static int CANTIDADRESPUESTASINCORRECTAS = 0;
	private static boolean RESPONDERDEFECTO = false;

	private static boolean TERMINARANTES = false;

	private static boolean VISTAPREVIA = false;
	private static int NUMEROPREGUNTA = 1;
	private static int puntajeTotal = 0;

	private static String[] ordenPreguntas;
	private static int tabPreguntasActual;
	private static String SELECCIONMULTIPLE = "Selección Multiple";
	private static String VERDADEROFALSO = "Verdadero Falso";
	private static String RESPUESTASCORTAS = "Respuestas Cortas";
	private static String RESPUESTASNUMERICAS = "Respuestas Numericas";
	private final static String BIENVENIDA = "Bienvenid@ Alumn@";

	private final static String RESPONDERPREGUNTASDEFECTO = "Responder preguntas por defecto";
	private final static String RESPONDERPREGUNTASPROFESOR = "Responder preguntas del Profesor (solo .smr)";
	private final static String VISUALIZARPDF = "Abrir PDF";
	private final static String CERRARSESION = "Cerrar sesión";

	private final static String VISTAPREVIAPROFESOR = "Vista previa alumno";

	private Examen examenAlumno;

	private JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
	private JRadioButton[] rdBotones;
	private ButtonGroup group;
	private String nombreAlumno = "test";
	private String apellidoAlumno = "test";
	private String rutAlumno = "00.000.000-0";
	private long TiempoExamen = 0;
	private int itemsUsados;

	private boolean detenerAvancePreguntas = false;
	private boolean detenerRetrocesoPreguntas = false;
	private boolean hayPreguntaAnterior = false;

	private JTextField txtFieldNombreExamen;
	private JTextField txtFieldUbicacionPDF;
	private JTextField respuestaAlumnoField;
	private JTextField txtContra;

	private float notaFinal;
	private String notaFinalString;

	private int segundos = 1;
	private Timer espera;

	private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
	private LocalDateTime horaInicio;
	private LocalDateTime horaFinal;

	// region de paneles para responder las preguntas del profesor.

	/**
	 * Este panel consulta por la ubicación del archivo SMR (.smr), para poder luego
	 * iniciar el examen.
	 * 
	 * @return retorna el panel generado.
	 */
	private JPanel panelResponderProfesor() {
		examenAlumno = null;

		JPanel Panel = new JPanel(new BorderLayout()) {

			private static final long serialVersionUID = -8586290404408899112L;

			@Override
			protected void paintComponent(Graphics g) {
				BufferedImage img = ImagenesAleatorias.getImagen();
				if (img != null) {
					super.paintComponent(g);
					g.drawImage(img, 0, 0, 1000, 669, 0, 0, img.getWidth(), img.getHeight(), this);
				} else {
					super.paintComponent(g);
					g.setColor(Color.decode("#B1C9B1"));
				}
			}
		};

		Panel.setBackground(Color.decode("#B1C9B1"));
		Panel.setBorder(new EmptyBorder(new Insets(180, 200, 180, 200)));

		JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder(new EmptyBorder(20, 25, 20, 25));
		Panel.add(panel);

		// titulo arriba

		JLabel lblTitulo = new JLabel(
				"<html><p align = \"center\">Antes de continuar, por favor ingrese la ubicación <br>del archivo con el examen.</p></html>");
		lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
		panel.add(lblTitulo, BorderLayout.NORTH);

		// panel central

		JPanel panelCentral = new JPanel();
		panelCentral.setBorder(new EmptyBorder(0, 0, 0, 0));
		panel.add(panelCentral, BorderLayout.CENTER);
		panelCentral.setLayout(new BoxLayout(panelCentral, BoxLayout.Y_AXIS));

		// panel central superior (ubicación archivo)

		JPanel panelUbicacion = new JPanel();
		panelUbicacion.setLayout(new FlowLayout());
		panelCentral.add(panelUbicacion);

		txtFieldNombreExamen = new JTextField(20);
		txtFieldNombreExamen.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		new TextPrompt("Ubicación del archivo", txtFieldNombreExamen);
		panelUbicacion.add(txtFieldNombreExamen);

		JButton botonAbrirArchivoPanelResponderProfesor = new JButton("Abrir Archivo");
		botonAbrirArchivoPanelResponderProfesor.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		botonAbrirArchivoPanelResponderProfesor.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser Archivo = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
				Archivo.setFileFilter(new FileNameExtensionFilter("Archivos .smr", "smr"));
				int returnVal = Archivo.showOpenDialog(new JFrame());
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					txtFieldNombreExamen.setText(Archivo.getSelectedFile().getAbsolutePath());
				}
			}
		});
		botonAbrirArchivoPanelResponderProfesor.setBackground(Color.decode("#0077ff"));
		panelUbicacion.add(botonAbrirArchivoPanelResponderProfesor);

		// panel central inferior (contraseña archivo)

		JPanel panelContrasena = new JPanel(new FlowLayout());
		panelCentral.add(panelContrasena);

		txtContra = new JTextField(21);
		txtContra.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		txtContra.setEnabled(false);
		new TextPrompt("Contraseña del archivo", txtContra);
		panelContrasena.add(txtContra);

		JToggleButton tglbtnContra = new JToggleButton("Contraseña");
		tglbtnContra.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		tglbtnContra.setBackground(Color.decode("#0077ff"));
		tglbtnContra.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent itemEvent) {
				int estado = itemEvent.getStateChange();
				if (estado == ItemEvent.SELECTED) {
					txtContra.setEnabled(true);
				} else {
					txtContra.setText("");
					txtContra.setEnabled(false);
				}
			}
		});
		panelContrasena.add(tglbtnContra);

		// panel del sur

		JPanel panelBotonesInferiores = new JPanel(new BorderLayout());
		panel.add(panelBotonesInferiores, BorderLayout.SOUTH);

		JButton botonSiguientePanelResponderProfesor = new JButton("Siguiente");
		botonSiguientePanelResponderProfesor.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		botonSiguientePanelResponderProfesor.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					tabActivado(false);
					examenAlumno = (Archivos.interpretarArchivo(txtFieldNombreExamen.getText(), txtContra.getText()));
					tabPreguntasActual = 3;
					RESPONDERDEFECTO = false;
					actualizarPanelTab(panelResponderProfesorDescripcion(), 3);

				} catch (Exception e2) {
					Mensajes.mensaje(e2.getMessage());
				}
			}
		});
		botonSiguientePanelResponderProfesor.setBackground(Color.decode("#0077ff"));
		panelBotonesInferiores.add(botonSiguientePanelResponderProfesor, BorderLayout.EAST);

		JButton botonCancelarPanelResponderProfesor = new JButton("Cancelar");
		botonCancelarPanelResponderProfesor.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		botonCancelarPanelResponderProfesor.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				tabbedPane.setSelectedIndex(0);
			}
		});
		botonCancelarPanelResponderProfesor.setBackground(Color.decode("#00668c"));
		panelBotonesInferiores.add(botonCancelarPanelResponderProfesor, BorderLayout.WEST);

		return Panel;

	}

	/**
	 * Este panel le muestra una pequeña descripción del examen, donde se le muestra
	 * el tipo de preguntas que tiene el examen, además del tiempo limite (si es que
	 * tiene), como también el puntaje total.
	 * 
	 * @return retorna el panel generado.
	 */
	private JPanel panelResponderProfesorDescripcion() {

		JPanel Panel = new JPanel(new BorderLayout()) {

			private static final long serialVersionUID = -2306659183922923192L;

			@Override
			protected void paintComponent(Graphics g) {
				BufferedImage img = ImagenesAleatorias.getImagen();
				if (img != null) {
					super.paintComponent(g);
					g.drawImage(img, 0, 0, 1000, 669, 0, 0, img.getWidth(), img.getHeight(), this);
				} else {
					super.paintComponent(g);
					g.setColor(Color.decode("#B1C9B1"));
				}
			}
		};
		Panel.setBackground(Color.decode("#B1C9B1"));
		Panel.setBorder(new EmptyBorder(new Insets(95, 178, 96, 178)));

		JPanel panel = new JPanel();
		panel.setBorder(new EmptyBorder(5, 25, 20, 25));
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		Panel.add(panel);

		// titulo

		JPanel panelTitulo = new JPanel();
		panel.add(panelTitulo);

		int cantidadItems = ((examenAlumno == null) ? 0 : examenAlumno.obtenerItems().length);

		String mensajeTitulo = "<html><h2>Esta es una prueba que tiene " + cantidadItems
				+ ((cantidadItems == 1) ? " tipo de ítem, el cual es:</h2>"
						: " tipos de ítems, los cuáles son:</h2></html>");

		JLabel lblTitulo = new JLabel(mensajeTitulo);
		lblTitulo.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		panelTitulo.add(lblTitulo);

		// tipo preguntas

		JPanel panelTipoPreguntas = new JPanel();
		panelTipoPreguntas.setLayout(new GridLayout(3, 3, 10, 10));
		panelTipoPreguntas.setBorder(new EmptyBorder(0, 0, 20, 0));
		panel.add(panelTipoPreguntas);

		if (cantidadItems != 0) {
			JLabel[] tipoPreguntas = new JLabel[cantidadItems];

			for (int i = 0; i < cantidadItems; i++) {
				tipoPreguntas[i] = new JLabel(examenAlumno.obtenerItems()[i]);
				tipoPreguntas[i].setFont(new Font("Segoe UI", Font.BOLD, 12));
				tipoPreguntas[i].setHorizontalAlignment(JLabel.CENTER);
				panelTipoPreguntas.add(tipoPreguntas[i]);
			}
		} else {
			JLabel tipoPreguntas = new JLabel(
					"<html><p align = \"center\">Por favor reinicie el programa, o intente volver al panel anterior e intentelo nuevamente. Gracias.</p></html>");
			tipoPreguntas.setFont(new Font("Segoe UI", Font.BOLD, 12));
			panelTipoPreguntas.add(tipoPreguntas);
		}

		// demás texto

		JPanel panelDescripcion = new JPanel(new BorderLayout());
		panelDescripcion.setBorder(new EmptyBorder(10, 0, 0, 0));
		panel.add(panelDescripcion);

		JLabel lblDescripcion = new JLabel(obtenerInformacionExamen());
		lblDescripcion.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		panelDescripcion.add(lblDescripcion);

		// botones inferiores
		JPanel panelBotonesInferiores = new JPanel(new BorderLayout());
		panelBotonesInferiores.setBorder(new EmptyBorder(10, 5, 10, 5));
		panel.add(panelBotonesInferiores);

		if (VISTAPREVIA == false) {
			JButton botonCancelarPanelResponderProfesor = new JButton("Cancelar");
			botonCancelarPanelResponderProfesor.setFont(new Font("Segoe UI", Font.PLAIN, 18));
			botonCancelarPanelResponderProfesor.setBackground(Color.decode("#00668c"));
			panelBotonesInferiores.add(botonCancelarPanelResponderProfesor, BorderLayout.WEST);
			botonCancelarPanelResponderProfesor.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					actualizarPanelTab(panelResponderProfesor(), 3);
				}
			});
		}

		if (examenAlumno != null) {
			JButton botonSiguientePanelResponderProfesor = new JButton("Siguiente");
			botonSiguientePanelResponderProfesor.setFont(new Font("Segoe UI", Font.PLAIN, 18));
			botonSiguientePanelResponderProfesor.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					ordenPreguntas = examenAlumno.getOrdenPreguntas();
					horaInicio = LocalDateTime.now();
					siguientePanel();
				}
			});
			botonSiguientePanelResponderProfesor.setBackground(Color.decode("#0077ff"));
			panelBotonesInferiores.add(botonSiguientePanelResponderProfesor, BorderLayout.EAST);
		}

		return Panel;

	}

	// endregion

	// region de paneles y clases de visualizar examenes de tipo pdf.

	/**
	 * Este panel consulta por la ubicación del archivo pdf, para poder luego
	 * mostrarlo en pantalla en una pestaña nueva.
	 * 
	 * @return retorna el panel generado.
	 */
	private JPanel panelVisualizarPdf() {
		examenAlumno = null;

		JPanel Panel = new JPanel(new BorderLayout()) {

			private static final long serialVersionUID = -501752395530473259L;

			@Override
			protected void paintComponent(Graphics g) {
				BufferedImage img = ImagenesAleatorias.getImagen();
				if (img != null) {
					super.paintComponent(g);
					g.drawImage(img, 0, 0, 1000, 669, 0, 0, img.getWidth(), img.getHeight(), this);
				} else {
					super.paintComponent(g);
					g.setColor(Color.decode("#B1C9B1"));
				}
			}
		};
		Panel.setBackground(Color.decode("#B1C9B1"));
		Panel.setBorder(new EmptyBorder(new Insets(200, 190, 200, 190)));

		JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder(new EmptyBorder(20, 25, 20, 25));
		Panel.add(panel);

		// titulo arriba

		JLabel lblTitulo = new JLabel(
				"<html><p align = \"center\">Antes de continuar, por favor ingrese la ubicación del pdf.</p></html>");
		lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
		panel.add(lblTitulo, BorderLayout.NORTH);

		// panel central

		JPanel panelCentral = new JPanel(new FlowLayout(FlowLayout.CENTER));
		panelCentral.setBorder(new EmptyBorder(20, 0, 0, 0));
		panel.add(panelCentral, BorderLayout.CENTER);

		txtFieldUbicacionPDF = new JTextField(20);
		txtFieldUbicacionPDF.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		new TextPrompt("Ubicación del archivo", txtFieldUbicacionPDF);
		panelCentral.add(txtFieldUbicacionPDF);

		JButton botonAbrirArchivoPanelResponderProfesor = new JButton("Abrir Archivo");
		botonAbrirArchivoPanelResponderProfesor.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		botonAbrirArchivoPanelResponderProfesor.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser Archivo = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
				Archivo.setFileFilter(new FileNameExtensionFilter("Archivos .pdf", "pdf"));
				int returnVal = Archivo.showOpenDialog(new JFrame());
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					txtFieldUbicacionPDF.setText(Archivo.getSelectedFile().getAbsolutePath());
				}
			}
		});
		botonAbrirArchivoPanelResponderProfesor.setBackground(Color.decode("#0077ff"));
		panelCentral.add(botonAbrirArchivoPanelResponderProfesor);

		// panel del sur

		JPanel panelBotonesInferiores = new JPanel(new BorderLayout());
		panel.add(panelBotonesInferiores, BorderLayout.SOUTH);

		JButton botonSiguientePanelVisualizarPdf = new JButton("Siguiente");
		botonSiguientePanelVisualizarPdf.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		botonSiguientePanelVisualizarPdf.addActionListener(new botonSiguientePanelVisualizarPdf());
		botonSiguientePanelVisualizarPdf.setBackground(Color.decode("#0077ff"));
		panelBotonesInferiores.add(botonSiguientePanelVisualizarPdf, BorderLayout.EAST);

		return Panel;

	}

	/**
	 * Esta clase es el action listener del boton siguiente del panel para
	 * visualizar pdf.
	 */
	private class botonSiguientePanelVisualizarPdf implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				if (tabbedPane.getTabCount() > 25) {
					Mensajes.error(
							"<html>Ha llegado al máximo de examenes mostrados.<br>Por favor cierre algunos antes de abrir otro.</html>");
					return;
				}

				JPanel panel = ExportarAPDF.mostrarPDF(txtFieldUbicacionPDF.getText());

				String titulo = "Prueba";

				List<String> nombreTabs = new ArrayList<String>();

				for (int i = 0; i < tabbedPane.getTabCount(); i++) {
					nombreTabs.add(tabbedPane.getTitleAt(i));
				}

				if (nombreTabs.contains(titulo)) {

					for (int i = 0; i < nombreTabs.size(); i++) {
						if (!nombreTabs.contains(titulo + " " + (i + 1))) {
							titulo = titulo + " " + (i + 1);
							break;
						}
					}
				}

				agregarVistaPrevia(panel, titulo);

				tabbedPane.setSelectedIndex(tabbedPane.indexOfTab(titulo));

				txtFieldUbicacionPDF.setText("");

			} catch (Exception e2) {
				Mensajes.mensaje(e2.getMessage());
			}
		}
	}

	// endregion

	// region del panel para responder las preguntas por defecto (o las
	// del programa).

	/**
	 * Este panel le muestra una pequeña descripción del examen, donde se le muestra
	 * todos los tipos de preguntas que tiene el examen, además del tiempo limite,
	 * como también el puntaje total.
	 * 
	 * @return retorna el panel generado.
	 */
	private JPanel panelResponderDefecto() {

		JPanel Panel = new JPanel(new BorderLayout()) {

			private static final long serialVersionUID = 18588851999241749L;

			@Override
			protected void paintComponent(Graphics g) {
				BufferedImage img = ImagenesAleatorias.getImagen();
				if (img != null) {
					super.paintComponent(g);
					g.drawImage(img, 0, 0, 1000, 669, 0, 0, img.getWidth(), img.getHeight(), this);
				} else {
					super.paintComponent(g);
					g.setColor(Color.decode("#C9C9C9"));
				}
			}
		};
		Panel.setBackground(Color.decode("#C9C9C9"));
		Panel.setBorder(new EmptyBorder(new Insets(100, 150, 100, 150)));

		JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder(new EmptyBorder(20, 25, 20, 25));
		Panel.add(panel);

		JLabel lblDescripcion = new JLabel(
				"<html><h2>Esta es una prueba que tiene 4 tipos de ítems, los cuales son:</h2>- Selección multiple<br>- Respuestas cortas<br>- Verdadero falso<br>- Respuesta numerica<br><br><p>Tiene 13 puntos, además de un tiempo limite de 5 minutos.</p><br><p>Puedes terminar la prueba en cualquier momento que desees, pero recuerda que las<br>preguntas sin respuestas se consideraran malas. </p><br><h3>Suerte y vamos por el 7.0 !</h3></html>");
		lblDescripcion.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		panel.add(lblDescripcion, BorderLayout.NORTH);

		JPanel panelBotonesInferiores = new JPanel(new BorderLayout());
		panel.add(panelBotonesInferiores, BorderLayout.SOUTH);

		JButton botonSiguientePanelResponderDefecto = new JButton("Siguiente");
		botonSiguientePanelResponderDefecto.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		botonSiguientePanelResponderDefecto.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				examenAlumno = null;

				tabActivado(false);

				RESPONDERDEFECTO = true;

				examenAlumno = (ExamenDemo.ExamenBase());
				ordenPreguntas = examenAlumno.getOrdenPreguntas();

				itemsUsados = 0;
				preguntaActual = 0;
				tabPreguntasActual = 2;

				horaInicio = LocalDateTime.now();

				siguientePanel();
			}
		});
		botonSiguientePanelResponderDefecto.setBackground(Color.decode("#0077FF"));
		panelBotonesInferiores.add(botonSiguientePanelResponderDefecto, BorderLayout.EAST);

		JButton botonCancelarPanelResponderDefecto = new JButton("Cancelar");
		botonCancelarPanelResponderDefecto.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		botonCancelarPanelResponderDefecto.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				tabbedPane.setSelectedIndex(0);
			}
		});
		botonCancelarPanelResponderDefecto.setBackground(Color.decode("#00668C"));
		panelBotonesInferiores.add(botonCancelarPanelResponderDefecto, BorderLayout.WEST);

		return Panel;

	}

	// endregion

	// region de paneles y clases de finalizar examen.

	/**
	 * Este panel le muestra un breve resumen de lo que fue el examen, donde se le
	 * muestra la cantidad de respuestas buenas, como las malas, además de su
	 * puntaje total y nota.
	 * 
	 * @return retorna el panel generado.
	 */
	private JPanel panelFinalizarExamen() {

		JPanel Panel = new JPanel(new BorderLayout()) {

			private static final long serialVersionUID = 8948089370541495609L;

			@Override
			protected void paintComponent(Graphics g) {
				BufferedImage img = ImagenesAleatorias.getImagen();
				if (img != null) {
					super.paintComponent(g);
					g.drawImage(img, 0, 0, 1000, 669, 0, 0, img.getWidth(), img.getHeight(), this);
				} else {
					super.paintComponent(g);
					g.setColor(Color.decode("#B1C9B1"));
				}
			}
		};
		Panel.setBackground(Color.decode("#B1C9B1"));
		Panel.setBorder(new EmptyBorder(new Insets(129, 215, 128, 215)));

		JPanel panel = new JPanel();
		panel.setBorder(new EmptyBorder(20, 25, 20, 25));
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		Panel.add(panel);

		JPanel panelSuperior = new JPanel(new BorderLayout());
		panel.add(panelSuperior);

		JLabel lblTitulo = new JLabel();
		lblTitulo.setFont(new Font("Segoe UI", Font.PLAIN, 20));
		lblTitulo.setText("<html><h1>" + nombreAlumno + ", has finalizado el examen.</h1></html>");
		panelSuperior.add(lblTitulo);

		JPanel panelCentral = new JPanel(new BorderLayout());
		panel.add(panelCentral);

		JLabel labelResultados = cambiarMensajeFinalAlumno();
		panelCentral.add(labelResultados);

		JPanel panelInferior = new JPanel(new BorderLayout());
		panelInferior.setBorder(new EmptyBorder(20, 25, 20, 25));
		panel.add(panelInferior);

		if (!VISTAPREVIA) {
			JButton botonSiguientePanelFinalizarExamen = new JButton("Finalizar");
			botonSiguientePanelFinalizarExamen.setFont(new Font("Segoe UI", Font.PLAIN, 18));
			botonSiguientePanelFinalizarExamen.addActionListener(new botonFinalizarPanelFinalizarExamen_Listener());
			botonSiguientePanelFinalizarExamen.setBackground(Color.decode("#0077ff"));
			panelInferior.add(botonSiguientePanelFinalizarExamen, BorderLayout.EAST);
		} else {
			JLabel lblVistaFinalizada = new JLabel(
					"<html><p align = \"center\">Ha finalizado el examen, por favor<br>cierre la vista previa.</p></html>");
			lblVistaFinalizada.setFont(new Font("Segoe UI", Font.PLAIN, 30));
			panelInferior.add(lblVistaFinalizada, BorderLayout.CENTER);
		}

		return Panel;
	}

	/**
	 * Esta clase es el action listener del boton finalizar del panel para finalizar
	 * el examen.
	 */
	private class botonFinalizarPanelFinalizarExamen_Listener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			tabActivado(true);

			if (RESPONDERDEFECTO == false) {
				try {
					String nombrePrueba = txtFieldNombreExamen.getText();

					LocalDateTime tempDateTime = LocalDateTime.from(horaInicio);

					long anios = tempDateTime.until(horaFinal, ChronoUnit.YEARS);
					tempDateTime = tempDateTime.plusYears(anios);

					long meses = tempDateTime.until(horaFinal, ChronoUnit.MONTHS);
					tempDateTime = tempDateTime.plusMonths(meses);

					long dias = tempDateTime.until(horaFinal, ChronoUnit.DAYS);
					tempDateTime = tempDateTime.plusDays(dias);

					long horas = tempDateTime.until(horaFinal, ChronoUnit.HOURS);
					tempDateTime = tempDateTime.plusHours(horas);

					long minutos = tempDateTime.until(horaFinal, ChronoUnit.MINUTES);
					tempDateTime = tempDateTime.plusMinutes(minutos);

					long segundos = tempDateTime.until(horaFinal, ChronoUnit.SECONDS);

					String tiempoDesarrolloExamen = anios + " años " + meses + " meses " + dias + " dias " + horas
							+ " horas " + minutos + " minutos " + segundos + " segundos.";

					Archivos.guardarResultados(examenAlumno.getHash(), nombrePrueba, nombreAlumno, apellidoAlumno,
							rutAlumno, examenAlumno.obtener_Respuestas_Usuario(), String.valueOf(puntajeTotal),
							notaFinalString, examenAlumno.getLlave(), dtf.format(horaInicio), dtf.format(horaFinal),
							tiempoDesarrolloExamen);

					Mensajes.mensaje("<html>Estimad@ Alumn@, los resultados de su examen fueron guardados en<br>"
							+ new File(".").getCanonicalPath() + "<br>\\resultados examen\\"
							+ Texto.borrarExtension(nombrePrueba) + "." + nombreAlumno + ".nam" + "</html>");

				} catch (Exception e2) {
					Mensajes.mensaje(e2.getMessage());
					return;
				}
			}

			examenAlumno = null;
			CANTIDADRESPUESTASINCORRECTAS = 0;
			CANTIDADRESPUESTASCORRECTAS = 0;
			puntajeTotal = 0;
			itemsUsados = 0;
			preguntaActual = 0;
			NUMEROPREGUNTA = 1;
			TiempoExamen = 0;
			horaFinal = null;
			horaInicio = null;
			detenerAvancePreguntas = false;
			detenerRetrocesoPreguntas = false;
			if (tabPreguntasActual == 2) {
				examenAlumno = ExamenDemo.ExamenBase();
				actualizarPanelTab(panelResponderDefecto(), tabPreguntasActual);
			} else {
				actualizarPanelTab(panelResponderProfesor(), tabPreguntasActual);
			}
		}
	}

	// endregion

	// region de paneles y clases de cerrar la sesión.

	/**
	 * Este panel le consulta para verificar si está seguro al querer cerrar la
	 * sesión.
	 * 
	 * @return retorna el panel generado.
	 */
	private JPanel panelCerrarSesion() {

		JPanel Panel = new JPanel(new BorderLayout()) {

			private static final long serialVersionUID = -2107772384415040765L;

			@Override
			protected void paintComponent(Graphics g) {
				BufferedImage img = ImagenesAleatorias.getImagen();
				if (img != null) {
					super.paintComponent(g);
					g.drawImage(img, 0, 0, 1000, 669, 0, 0, img.getWidth(), img.getHeight(), this);
				} else {
					super.paintComponent(g);
					g.setColor(Color.decode("#807E7D"));
				}
			}
		};
		Panel.setBackground(Color.decode("#807E7D"));
		Panel.setBorder(new EmptyBorder(new Insets(220, 180, 220, 180)));

		JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder(new EmptyBorder(20, 25, 20, 25));
		Panel.add(panel);

		JLabel lblPregunta = new JLabel("¿Estas segur@ en cerrar la sesión?");
		lblPregunta.setHorizontalAlignment(SwingConstants.CENTER);
		lblPregunta.setFont(new Font("Segoe UI", Font.BOLD, 29));
		panel.add(lblPregunta, BorderLayout.NORTH);

		JPanel panelBotones = new JPanel(new BorderLayout());
		panel.add(panelBotones, BorderLayout.SOUTH);

		JButton botonCerrarPanelCerrarSesion = new JButton("Cancelar");
		botonCerrarPanelCerrarSesion.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		botonCerrarPanelCerrarSesion.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				tabbedPane.setSelectedIndex(0);
			}
		});
		botonCerrarPanelCerrarSesion.setBackground(Color.decode("#00668c"));
		panelBotones.add(botonCerrarPanelCerrarSesion, BorderLayout.WEST);

		JButton botonContinuarPanelCerrarSesion = new JButton("Continuar");
		botonContinuarPanelCerrarSesion.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		botonContinuarPanelCerrarSesion.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				actualizarPanelTab(panelCerrarSesionMensaje(), 4);

				espera = new Timer(1000, new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						if (segundos == 0) {
							espera.stop();
							// cierro todas las ventanas, para volver al iniciar sesion.
							for (Window ventanas : Window.getWindows()) {
								ventanas.dispose();
							}

							new InicioSesion();
						} else {
							segundos--;
						}
					}
				});
				espera.setInitialDelay(0);
				espera.start();

			}
		});
		botonContinuarPanelCerrarSesion.setBackground(Color.decode("#0077ff"));
		panelBotones.add(botonContinuarPanelCerrarSesion, BorderLayout.EAST);

		return Panel;

	}

	/**
	 * Este panel le muestra un mensaje despidiendose de usted.
	 * 
	 * @return retorna el panel generado.
	 */
	private JPanel panelCerrarSesionMensaje() {

		JPanel Panel = new JPanel(new BorderLayout()) {

			private static final long serialVersionUID = -650923401065124487L;

			@Override
			protected void paintComponent(Graphics g) {
				BufferedImage img = ImagenesAleatorias.getImagen();
				if (img != null) {
					super.paintComponent(g);
					g.drawImage(img, 0, 0, 1000, 669, 0, 0, img.getWidth(), img.getHeight(), this);
				} else {
					super.paintComponent(g);
					g.setColor(Color.decode("#807E7D"));
				}
			}
		};
		Panel.setBackground(Color.decode("#807E7D"));
		Panel.setBorder(new EmptyBorder(new Insets(220, 180, 220, 180)));

		JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder(new EmptyBorder(20, 25, 20, 25));
		Panel.add(panel);

		JLabel lblDespedida = new JLabel("¡Hasta luego!");
		lblDespedida.setFont(new Font("Segoe UI", Font.BOLD, 29));
		lblDespedida.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(lblDespedida);

		tabActivado(false);

		return Panel;

	}

	// endregion

	// region de paneles y clases de la pregunta de tipo selección multiple.

	/**
	 * Este panel le muestra la pregunta de tipo selección multiple, además de
	 * mostrar las opciones para que el alumno las pueda responder.
	 * 
	 * @return retorna el panel generado.
	 */
	private JPanel panelSeleccionMultiple() {

		String Pregunta = ((examenAlumno == null) ? "¡Error, no hay preguntas de agregadas que mostrar!"
				: examenAlumno.preguntas_SM.get(preguntaActual).getPregunta());

		List<String> Opciones = ((examenAlumno == null)
				? Arrays.asList(new String[] { "Por favor", "intenta abrir", "de nuevo", "el examen" })
				: Arrays.asList(examenAlumno.preguntas_SM.get(preguntaActual).getOpciones()));

		int Peso = ((examenAlumno == null) ? 0 : examenAlumno.preguntas_SM.get(preguntaActual).getPeso());

		JPanel Panel = new JPanel(new BorderLayout()) {

			private static final long serialVersionUID = -7506071805843113107L;

			@Override
			protected void paintComponent(Graphics g) {
				BufferedImage img = ImagenesAleatorias.getImagen();
				if (img != null) {
					super.paintComponent(g);
					g.drawImage(img, 0, 0, 1000, 669, 0, 0, img.getWidth(), img.getHeight(), this);
				} else {
					super.paintComponent(g);
					g.setColor(Color.decode("#969696"));
				}
			}
		};
		Panel.setBackground(Color.decode("#969696"));
		Panel.setBorder(new EmptyBorder(new Insets(46, 157, 47, 157)));

		JPanel panel = new JPanel(new BorderLayout(0, 14));
		panel.setBorder(new EmptyBorder(new Insets(20, 25, 11, 25)));
		Panel.add(panel, BorderLayout.CENTER);

		// titulo

		JLabel lblNumeroPregunta = new JLabel("Pregunta numero " + NUMEROPREGUNTA + " (" + Peso
				+ (((Peso == 1) || (Peso == -1)) ? " punto)" : " puntos)"));
		lblNumeroPregunta.setFont(new Font("Segoe UI", Font.PLAIN, 25));
		panel.add(lblNumeroPregunta, BorderLayout.NORTH);

		// panel del centro

		JPanel panelCentral = new JPanel(new BorderLayout());
		panelCentral.setLayout(new BoxLayout(panelCentral, BoxLayout.Y_AXIS));
		panel.add(panelCentral, BorderLayout.CENTER);

		// panel central superior

		JPanel panelCentralSuperior = new JPanel(new BorderLayout());
		panelCentral.add(panelCentralSuperior, BorderLayout.CENTER);

		JScrollPane scrollPane = new JScrollPane();
		panelCentral.add(scrollPane);

		JTextArea text = new JTextArea(14, 53);
		text.setLineWrap(true);
		text.setWrapStyleWord(true);
		text.setText(Pregunta);
		text.setEditable(false);
		scrollPane.setViewportView(text);

		// panel central inferior

		JPanel panelCentralInferior = new JPanel(new BorderLayout());
		panelCentral.add(panelCentralInferior, BorderLayout.SOUTH);

		JScrollPane scrllBotones = new JScrollPane();
		scrllBotones.setPreferredSize(
				new Dimension(panelCentral.getPreferredSize().width + 10, panelCentral.getPreferredSize().height / 2));
		panelCentralInferior.add(scrllBotones, BorderLayout.CENTER);

		JPanel panelRdBotones = new JPanel();
		panelRdBotones.setLayout(new BoxLayout(panelRdBotones, BoxLayout.Y_AXIS));
		scrllBotones.setViewportView(panelRdBotones);

		group = new ButtonGroup();

		rdBotones = new JRadioButton[Opciones.size()];

		if (examenAlumno != null && examenAlumno.getDesordenar()) {
			Collections.shuffle(Opciones);
		}

		for (int i = 0; i < Opciones.size(); i++) {
			if (!(Opciones.get(i) == null || Opciones.get(i).isBlank())) {
				rdBotones[i] = new JRadioButton(Opciones.get(i));
				rdBotones[i].setActionCommand(Opciones.get(i));
				group.add(rdBotones[i]);
				panelRdBotones.add(rdBotones[i]);
			}
		}

		// panel inferior

		JPanel panelBotonesInferiores = new JPanel(new BorderLayout());
		panel.add(panelBotonesInferiores, BorderLayout.SOUTH);

		JButton botonTerminarPanelSeleccionMultiple = new JButton("Terminar");
		botonTerminarPanelSeleccionMultiple.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		botonTerminarPanelSeleccionMultiple.addActionListener(new botonTerminarPanelSeleccionMultiple_Listener());
		botonTerminarPanelSeleccionMultiple.setBackground(Color.decode("#00668c"));

		if (examenAlumno != null && !examenAlumno.getMostrarRespuestas() && preguntaActual >= 0) {
			JButton botonVolverPanelSeleccionMultiple = new JButton();
			establecerIcono(botonVolverPanelSeleccionMultiple, "\\res\\izquierda.png", "Volver");
			botonVolverPanelSeleccionMultiple.addActionListener(new botonVolverPanelSeleccionMultiple_Listener());
			botonVolverPanelSeleccionMultiple.setBackground(Color.decode("#0077ff"));
			panelBotonesInferiores.add(botonVolverPanelSeleccionMultiple, BorderLayout.WEST);
			if (examenAlumno != null && NUMEROPREGUNTA < examenAlumno.obtenerTotalPreguntas()) {
				panelBotonesInferiores.add(botonTerminarPanelSeleccionMultiple, BorderLayout.CENTER);
			}

			// para poder mantener la respuesta del alumno al retroceder.
			if (examenAlumno != null && examenAlumno.preguntas_SM != null && examenAlumno.preguntas_SM.size() > 1
					&& examenAlumno.preguntas_SM.get(preguntaActual).getRespuestaAlumno() != null) {

				for (int i = 0; i < Opciones.size(); i++) {
					if (rdBotones[i].getText()
							.equals(examenAlumno.preguntas_SM.get(preguntaActual).getRespuestaAlumno())) {
						rdBotones[i].setSelected(true);
					}
				}
			}

		} else {
			panelBotonesInferiores.add(botonTerminarPanelSeleccionMultiple, BorderLayout.WEST);
		}

		if (examenAlumno != null
				&& (NUMEROPREGUNTA < examenAlumno.obtenerTotalPreguntas() || examenAlumno.getMostrarRespuestas())) {
			JButton botonContinuarPanelSeleccionMultiple = new JButton();
			botonContinuarPanelSeleccionMultiple.addActionListener(new botonContinuarPanelSeleccionMultiple_Listener());
			botonContinuarPanelSeleccionMultiple.setBackground(Color.decode("#0077ff"));
			establecerIcono(botonContinuarPanelSeleccionMultiple, "\\res\\derecha.png", "Continuar");
			panelBotonesInferiores.add(botonContinuarPanelSeleccionMultiple, BorderLayout.EAST);
		} else {
			panelBotonesInferiores.add(botonTerminarPanelSeleccionMultiple, BorderLayout.EAST);
		}

		return Panel;
	}

	/**
	 * Esta clase es el action listener del boton continuar del panel para responder
	 * las preguntas de tipo seleccion multiple.
	 */
	private class botonContinuarPanelSeleccionMultiple_Listener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			if (examenAlumno != null) {
				if ((examenAlumno.TiempoLimite == true)
						&& ((System.currentTimeMillis() - TiempoExamen) > examenAlumno.getTiempoLimite())) {
					Mensajes.mensaje("¡SE ACABÓ EL TIEMPO!");
					revisarExamen();
					actualizarPanelTab(panelFinalizarExamen(), tabPreguntasActual);
					return;
				}

				if (group.getSelection() == null) {
					examenAlumno.preguntas_SM.get(preguntaActual).setRespuestaAlumno(null);
				} else {
					examenAlumno.preguntas_SM.get(preguntaActual)
							.setRespuestaAlumno(group.getSelection().getActionCommand());
				}

				if (examenAlumno.getMostrarRespuestas()) {
					if (group.getSelection() != null) {

						String respuestaAlumno = group.getSelection().getActionCommand();

						if (examenAlumno.preguntas_SM.get(preguntaActual).getOpciones()[examenAlumno.preguntas_SM
								.get(preguntaActual).getRespuesta()].equalsIgnoreCase(respuestaAlumno)) {
							Mensajes.mensaje("Felicidades, la respuesta correcta era "
									+ examenAlumno.preguntas_SM.get(preguntaActual)
											.getOpciones()[examenAlumno.preguntas_SM.get(preguntaActual).getRespuesta()]
									+ " .");
						} else {
							Mensajes.mensaje("La respuesta correcta era "
									+ examenAlumno.preguntas_SM.get(preguntaActual)
											.getOpciones()[examenAlumno.preguntas_SM.get(preguntaActual).getRespuesta()]
									+ " ...");
						}
					} else {
						Mensajes.mensaje("Por favor seleccione una opción.");
						return;
					}
				}
				if (NUMEROPREGUNTA <= examenAlumno.obtenerTotalPreguntas()) {
					NUMEROPREGUNTA++;
				} else {
					detenerAvancePreguntas = true;
				}

				if (!detenerAvancePreguntas) {
					preguntaActual++;
				}
				siguientePanel();
			}
		}

	}

	/**
	 * Esta clase es el action listener del boton terminar del panel para responder
	 * las preguntas de tipo seleccion multiple.
	 */
	private class botonTerminarPanelSeleccionMultiple_Listener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			if (examenAlumno != null) {

				TERMINARANTES = true;

				if ((examenAlumno.TiempoLimite == true)
						&& ((System.currentTimeMillis() - TiempoExamen) > examenAlumno.getTiempoLimite())) {
					Mensajes.mensaje("¡SE ACABÓ EL TIEMPO!");
					horaFinal = LocalDateTime.now();
					revisarExamen();
					actualizarPanelTab(panelFinalizarExamen(), tabPreguntasActual);
					return;
				}

				if (group.getSelection() == null) {
					examenAlumno.preguntas_SM.get(preguntaActual).setRespuestaAlumno(null);
				} else {
					examenAlumno.preguntas_SM.get(preguntaActual)
							.setRespuestaAlumno(group.getSelection().getActionCommand());
				}

				if (Mensajes.consultar(
						"<html>Estas a punto de finalizar la prueba...<br><p align = \"center\">¿Estas segur@?</p></html>",
						"Finalizar")) {
					TERMINARANTES = true;
					horaFinal = LocalDateTime.now();
					revisarExamen();
					if (!VISTAPREVIA) {
						actualizarPanelTab(panelFinalizarExamen(), tabPreguntasActual);
					} else {
						cambiarPanel(panelFinalizarExamen());
					}
				} else {
					return;
				}
			}

		}
	}

	/**
	 * Esta clase es el action listener del boton volver del panel para responder
	 * las preguntas de tipo seleccion multiple.
	 */
	private class botonVolverPanelSeleccionMultiple_Listener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			if (examenAlumno != null) {
				if ((examenAlumno.TiempoLimite == true)
						&& ((System.currentTimeMillis() - TiempoExamen) > examenAlumno.getTiempoLimite())) {
					Mensajes.mensaje("SE ACABO EL TIEMPO!");
					revisarExamen();
					horaFinal = LocalDateTime.now();
					actualizarPanelTab(panelFinalizarExamen(), tabPreguntasActual);
					return;
				}

				if (group.getSelection() == null) {
					examenAlumno.preguntas_SM.get(preguntaActual).setRespuestaAlumno(null);
				} else {
					examenAlumno.preguntas_SM.get(preguntaActual)
							.setRespuestaAlumno(group.getSelection().getActionCommand());
				}

				if (NUMEROPREGUNTA > 1) {
					NUMEROPREGUNTA--;
				}
				if (!detenerRetrocesoPreguntas) {
					preguntaActual--;
				}

				panelAnterior();
			}
		}

	}

	// endregion

	// region de paneles y clases de la pregunta de tipo respuestas cortas.

	/**
	 * Este panel le muestra la pregunta de tipo respuestas cortas, además de
	 * habilitar un espacio para responder la pregunta.
	 * 
	 * @return retorna el panel generado.
	 */
	private JPanel panelRespuestasCortas() {

		String Pregunta = (examenAlumno == null)
				? "Por favor vuelva al panel anterior, o reinicie el programa e intentelo nuevamente."
				: examenAlumno.preguntas_RC.get(preguntaActual).getPregunta();

		int Peso = (examenAlumno == null) ? 0 : examenAlumno.preguntas_RC.get(preguntaActual).getPeso();

		JPanel Panel = new JPanel(new BorderLayout()) {

			private static final long serialVersionUID = 332359481228211887L;

			@Override
			protected void paintComponent(Graphics g) {
				BufferedImage img = ImagenesAleatorias.getImagen();
				if (img != null) {
					super.paintComponent(g);
					g.drawImage(img, 0, 0, 1000, 669, 0, 0, img.getWidth(), img.getHeight(), this);
				} else {
					super.paintComponent(g);
					g.setColor(Color.decode("#969696"));
				}
			}
		};
		Panel.setBackground(Color.decode("#969696"));
		Panel.setBorder(new EmptyBorder(new Insets(46, 157, 47, 157)));

		JPanel panel = new JPanel(new BorderLayout(0, 4));
		panel.setBorder(new EmptyBorder(new Insets(20, 25, 11, 25)));
		Panel.add(panel, BorderLayout.CENTER);

		// titulo
		JLabel lblTitulo = new JLabel("Pregunta numero " + NUMEROPREGUNTA + " (" + Peso
				+ (((Peso == 1) || (Peso == -1)) ? " punto)" : " puntos)"));
		lblTitulo.setFont(new Font("Segoe UI", Font.PLAIN, 25));
		panel.add(lblTitulo, BorderLayout.NORTH);

		// panel del centro

		JPanel panelDelCentro = new JPanel(new BorderLayout());
		panel.add(panelDelCentro, BorderLayout.CENTER);

		JScrollPane scrollPane = new JScrollPane();
		panelDelCentro.add(scrollPane, BorderLayout.CENTER);

		JTextArea text = new JTextArea(19, 60);
		text.setLineWrap(true);
		text.setWrapStyleWord(true);
		text.setText(Pregunta);
		text.setEditable(false);
		scrollPane.setViewportView(text);

		respuestaAlumnoField = new JTextField(10);
		respuestaAlumnoField.setFont(new Font("Segoe UI", Font.PLAIN, 20));
		respuestaAlumnoField.setTransferHandler(null);
		new TextPrompt("Su respuesta", respuestaAlumnoField);
		panelDelCentro.add(respuestaAlumnoField, BorderLayout.SOUTH);

		// panel inferior

		JPanel panelBotonesInferiores = new JPanel(new BorderLayout());
		panelBotonesInferiores.setBorder(new EmptyBorder(10, 0, 10, 0));
		panel.add(panelBotonesInferiores, BorderLayout.SOUTH);

		JButton botonTerminarPanelRespuestasCortas = new JButton("Terminar");
		botonTerminarPanelRespuestasCortas.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		botonTerminarPanelRespuestasCortas.addActionListener(new botonTerminarPanelRespuestasCortas_Listener());
		botonTerminarPanelRespuestasCortas.setBackground(Color.decode("#00668c"));

		if (examenAlumno != null && !examenAlumno.getMostrarRespuestas() && preguntaActual >= 0) {
			JButton botonVolverPanelRespuestasCortas = new JButton();
			establecerIcono(botonVolverPanelRespuestasCortas, "\\res\\izquierda.png", "Volver");
			botonVolverPanelRespuestasCortas.addActionListener(new botonVolverPanelRespuestasCortas_Listener());
			botonVolverPanelRespuestasCortas.setBackground(Color.decode("#0077ff"));
			panelBotonesInferiores.add(botonVolverPanelRespuestasCortas, BorderLayout.WEST);
			if (examenAlumno != null && NUMEROPREGUNTA < examenAlumno.obtenerTotalPreguntas()) {
				panelBotonesInferiores.add(botonTerminarPanelRespuestasCortas, BorderLayout.CENTER);
				respuestaAlumnoField.addActionListener(new botonContinuarPanelRespuestasCortas_Listener());
			}

			// para poder mantener la respuesta del alumno al retroceder.
			if (examenAlumno != null && examenAlumno.preguntas_RC != null && examenAlumno.preguntas_RC.size() > 1
					&& examenAlumno.preguntas_RC.get(preguntaActual).getRespuestaAlumno() != null) {
				respuestaAlumnoField.setText(examenAlumno.preguntas_RC.get(preguntaActual).getRespuestaAlumno());
			}

		} else {
			panelBotonesInferiores.add(botonTerminarPanelRespuestasCortas, BorderLayout.WEST);
		}

		if (examenAlumno != null
				&& (NUMEROPREGUNTA < examenAlumno.obtenerTotalPreguntas() || examenAlumno.getMostrarRespuestas())) {
			JButton botonContinuarPanelRespuestasCortas = new JButton();
			establecerIcono(botonContinuarPanelRespuestasCortas, "\\res\\derecha.png", "Continuar");
			botonContinuarPanelRespuestasCortas.addActionListener(new botonContinuarPanelRespuestasCortas_Listener());
			botonContinuarPanelRespuestasCortas.setBackground(Color.decode("#0077ff"));
			panelBotonesInferiores.add(botonContinuarPanelRespuestasCortas, BorderLayout.EAST);
		} else {
			panelBotonesInferiores.add(botonTerminarPanelRespuestasCortas, BorderLayout.EAST);
		}

		return Panel;
	}

	/**
	 * Esta clase es el action listener del boton continuar del panel para responder
	 * las preguntas de tipo respuestas cortas.
	 */
	private class botonContinuarPanelRespuestasCortas_Listener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			if (examenAlumno != null) {
				if ((examenAlumno.TiempoLimite == true)
						&& ((System.currentTimeMillis() - TiempoExamen) > examenAlumno.getTiempoLimite())) {
					Mensajes.mensaje("SE ACABO EL TIEMPO!");
					horaFinal = LocalDateTime.now();
					revisarExamen();
					actualizarPanelTab(panelFinalizarExamen(), tabPreguntasActual);
					return;
				}

				if (respuestaAlumnoField == null || respuestaAlumnoField.getText().isBlank()) {
					examenAlumno.preguntas_RC.get(preguntaActual).setRespuestaAlumno(null);
				} else {
					examenAlumno.preguntas_RC.get(preguntaActual).setRespuestaAlumno(respuestaAlumnoField.getText());
				}

				if (examenAlumno.getMostrarRespuestas()) {
					if (respuestaAlumnoField != null && !respuestaAlumnoField.getText().isBlank()) {

						String respuestaAlumno = respuestaAlumnoField.getText();
						String textoRespuesta = examenAlumno.preguntas_RC.get(preguntaActual).getRespuesta();

						if (Texto.similitud(textoRespuesta, respuestaAlumno) > 79) {
							Mensajes.mensaje("Felicidades, la respuesta correcta era " + textoRespuesta + ".");
						} else {
							Mensajes.mensaje("La respuesta correcta era " + textoRespuesta + "...");
						}
					} else {
						Mensajes.mensaje("Por favor ingrese una respuesta valida.");
						return;
					}
				}
				if (NUMEROPREGUNTA <= examenAlumno.obtenerTotalPreguntas()) {
					NUMEROPREGUNTA++;
				} else {
					detenerAvancePreguntas = true;
				}

				if (!detenerAvancePreguntas) {
					preguntaActual++;
				}
				siguientePanel();
			}

		}

	}

	/**
	 * Esta clase es el action listener del boton terminar del panel para responder
	 * las preguntas de tipo respuestas cortas.
	 */
	private class botonTerminarPanelRespuestasCortas_Listener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			if (examenAlumno != null) {
				TERMINARANTES = true;

				if ((examenAlumno.TiempoLimite == true)
						&& ((System.currentTimeMillis() - TiempoExamen) > examenAlumno.getTiempoLimite())) {
					Mensajes.mensaje("SE ACABO EL TIEMPO!");
					revisarExamen();
					horaFinal = LocalDateTime.now();
					actualizarPanelTab(panelFinalizarExamen(), tabPreguntasActual);
					return;
				}

				if (respuestaAlumnoField == null || respuestaAlumnoField.getText().isBlank()) {
					examenAlumno.preguntas_RC.get(preguntaActual).setRespuestaAlumno(null);
				} else {
					examenAlumno.preguntas_RC.get(preguntaActual).setRespuestaAlumno(respuestaAlumnoField.getText());
				}

				if (Mensajes.consultar(
						"<html>Estas a punto de finalizar la prueba...<br><p align = \"center\">¿Estas segur@?</p></html>",
						"Finalizar")) {
					revisarExamen();
					horaFinal = LocalDateTime.now();
					if (!VISTAPREVIA) {
						actualizarPanelTab(panelFinalizarExamen(), tabPreguntasActual);
					} else {
						cambiarPanel(panelFinalizarExamen());
					}
				} else {
					return;
				}
			}
		}
	}

	/**
	 * Esta clase es el action listener del boton volver del panel para responder
	 * las preguntas de tipo respuestas cortas.
	 */
	private class botonVolverPanelRespuestasCortas_Listener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			if ((examenAlumno.TiempoLimite == true)
					&& ((System.currentTimeMillis() - TiempoExamen) > examenAlumno.getTiempoLimite())) {
				Mensajes.mensaje("SE ACABO EL TIEMPO!");
				revisarExamen();
				horaFinal = LocalDateTime.now();
				actualizarPanelTab(panelFinalizarExamen(), tabPreguntasActual);
				return;
			}

			if (respuestaAlumnoField == null || respuestaAlumnoField.getText().isBlank()) {
				examenAlumno.preguntas_RC.get(preguntaActual).setRespuestaAlumno(null);
			} else {
				examenAlumno.preguntas_RC.get(preguntaActual).setRespuestaAlumno(respuestaAlumnoField.getText());
			}

			if (NUMEROPREGUNTA > 1) {
				NUMEROPREGUNTA--;
			}
			if (!detenerRetrocesoPreguntas) {
				preguntaActual--;
			}

			panelAnterior();

		}

	}

	// endregion

	// region de paneles y clases de la pregunta de tipo verdadero y falso.

	/**
	 * Este panel le muestra la pregunta de tipo verdadero/falso, además de mostrar
	 * las opciones verdadero y falso, para que el alumno pueda responder.
	 * 
	 * @return retorna el panel generado.
	 */
	private JPanel panelVerdaderoFalso() {

		String Pregunta = (examenAlumno == null)
				? "Por favor vuelva al panel anterior, o reinicie el programa e intentelo nuevamente."
				: examenAlumno.preguntas_VF.get(preguntaActual).getPregunta();

		int Peso = (examenAlumno == null) ? 0 : examenAlumno.preguntas_VF.get(preguntaActual).getPeso();

		JPanel Panel = new JPanel(new BorderLayout()) {

			private static final long serialVersionUID = -1517270381206114252L;

			@Override
			protected void paintComponent(Graphics g) {
				BufferedImage img = ImagenesAleatorias.getImagen();
				if (img != null) {
					super.paintComponent(g);
					g.drawImage(img, 0, 0, 1000, 669, 0, 0, img.getWidth(), img.getHeight(), this);
				} else {
					super.paintComponent(g);
					g.setColor(Color.decode("#969696"));
				}
			}
		};
		Panel.setBackground(Color.decode("#969696"));
		Panel.setBorder(new EmptyBorder(new Insets(46, 157, 47, 157)));

		JPanel panel = new JPanel(new BorderLayout(0, 18));
		panel.setBorder(new EmptyBorder(new Insets(20, 25, 11, 25)));
		Panel.add(panel, BorderLayout.CENTER);

		// titulo

		JLabel lblTitulo = new JLabel("Pregunta numero " + NUMEROPREGUNTA + " (" + Peso
				+ (((Peso == 1) || (Peso == -1)) ? " punto)" : " puntos) "));
		lblTitulo.setFont(new Font("Segoe UI", Font.PLAIN, 25));
		panel.add(lblTitulo, BorderLayout.NORTH);

		// panel del centro

		JPanel panelDelCentro = new JPanel(new BorderLayout(0, 10));
		panel.add(panelDelCentro, BorderLayout.CENTER);

		JScrollPane scrollPane = new JScrollPane();
		panelDelCentro.add(scrollPane, BorderLayout.CENTER);

		JTextArea text = new JTextArea(17, 60);
		text.setLineWrap(true);
		text.setWrapStyleWord(true);
		text.setText(Pregunta);
		text.setEditable(false);
		scrollPane.setViewportView(text);

		JPanel panelRdBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 0));
		panelDelCentro.add(panelRdBotones, BorderLayout.SOUTH);

		JRadioButton rdbtnVerdadero = new JRadioButton("Verdadero");
		rdbtnVerdadero.setFont(new Font("Segoe UI", Font.PLAIN, 20));
		rdbtnVerdadero.setActionCommand("verdadero");
		panelRdBotones.add(rdbtnVerdadero);

		JRadioButton rdbtnFalso = new JRadioButton("Falso");
		rdbtnFalso.setFont(new Font("Segoe UI", Font.PLAIN, 20));
		rdbtnFalso.setActionCommand("falso");
		panelRdBotones.add(rdbtnFalso);

		group = new ButtonGroup();

		group.add(rdbtnFalso);
		group.add(rdbtnVerdadero);

		// panel inferior

		JPanel panelBotonesInferiores = new JPanel(new BorderLayout());
		panelBotonesInferiores.setBorder(new EmptyBorder(10, 0, 10, 0));
		panel.add(panelBotonesInferiores, BorderLayout.SOUTH);

		JButton botonTerminarPanelVerdaderoFalso = new JButton("Terminar");
		botonTerminarPanelVerdaderoFalso.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		botonTerminarPanelVerdaderoFalso.addActionListener(new botonTerminarPanelVerdaderoFalso_Listener());
		botonTerminarPanelVerdaderoFalso.setBackground(Color.decode("#00668c"));

		if (examenAlumno != null && !examenAlumno.getMostrarRespuestas() && preguntaActual >= 0) {
			JButton botonVolverPanelVerdaderoFalso = new JButton();
			establecerIcono(botonVolverPanelVerdaderoFalso, "\\res\\izquierda.png", "Volver");
			botonVolverPanelVerdaderoFalso.addActionListener(new botonVolverPanelVerdaderoFalso_Listener());
			botonVolverPanelVerdaderoFalso.setBackground(Color.decode("#0077ff"));
			panelBotonesInferiores.add(botonVolverPanelVerdaderoFalso, BorderLayout.WEST);
			if (examenAlumno != null && NUMEROPREGUNTA < examenAlumno.obtenerTotalPreguntas()) {
				panelBotonesInferiores.add(botonTerminarPanelVerdaderoFalso, BorderLayout.CENTER);
			}

			// para poder mantener la respuesta del alumno al retroceder.
			if (examenAlumno != null && examenAlumno.preguntas_VF != null && examenAlumno.preguntas_VF.size() > 1
					&& examenAlumno.preguntas_VF.get(preguntaActual).getRespuestaAlumno() != null) {
				if (examenAlumno.preguntas_VF.get(preguntaActual).getRespuestaAlumno().equals("verdadero")) {
					rdbtnVerdadero.setSelected(true);
				} else if (examenAlumno.preguntas_VF.get(preguntaActual).getRespuestaAlumno().equals("falso")) {
					rdbtnFalso.setSelected(true);
				}
			}

		} else {
			panelBotonesInferiores.add(botonTerminarPanelVerdaderoFalso, BorderLayout.WEST);
		}

		if (examenAlumno != null
				&& (NUMEROPREGUNTA < examenAlumno.obtenerTotalPreguntas() || examenAlumno.getMostrarRespuestas())) {
			JButton botonContinuarPanelVerdaderoFalso = new JButton();
			establecerIcono(botonContinuarPanelVerdaderoFalso, "\\res\\derecha.png", "Continuar");
			botonContinuarPanelVerdaderoFalso.addActionListener(new botonContinuarPanelVerdaderoFalso_Listener());
			botonContinuarPanelVerdaderoFalso.setBackground(Color.decode("#0077ff"));
			panelBotonesInferiores.add(botonContinuarPanelVerdaderoFalso, BorderLayout.EAST);
		} else {
			panelBotonesInferiores.add(botonTerminarPanelVerdaderoFalso, BorderLayout.EAST);
		}
		return Panel;
	}

	/**
	 * Esta clase es el action listener del boton continuar del panel para responder
	 * las preguntas de tipo verdadero falso.
	 */
	private class botonContinuarPanelVerdaderoFalso_Listener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			if ((examenAlumno.TiempoLimite == true)
					&& ((System.currentTimeMillis() - TiempoExamen) > examenAlumno.getTiempoLimite())) {
				Mensajes.mensaje("SE ACABO EL TIEMPO!");
				revisarExamen();
				horaFinal = LocalDateTime.now();
				actualizarPanelTab(panelFinalizarExamen(), tabPreguntasActual);
				return;
			}

			if (group.getSelection() == null) {
				examenAlumno.preguntas_VF.get(preguntaActual).setRespuestaAlumno(null);
			} else {
				examenAlumno.preguntas_VF.get(preguntaActual)
						.setRespuestaAlumno(group.getSelection().getActionCommand());
			}

			if (examenAlumno.getMostrarRespuestas()) {
				if (group.getSelection() != null) {

					String textoRespuesta = (examenAlumno.preguntas_VF.get(preguntaActual).getRespuesta() == true)
							? "verdadero"
							: "falso";

					if (textoRespuesta.equalsIgnoreCase(group.getSelection().getActionCommand())) {
						Mensajes.mensaje("Felicidades, la respuesta correcta era " + textoRespuesta + ".");
					} else {
						Mensajes.mensaje("La respuesta correcta era " + textoRespuesta + "...");
					}
				} else {
					Mensajes.mensaje("Por favor seleccione una opción.");
					return;
				}
			}
			if (NUMEROPREGUNTA <= examenAlumno.obtenerTotalPreguntas()) {
				NUMEROPREGUNTA++;
			} else {
				detenerAvancePreguntas = true;
			}

			if (!detenerAvancePreguntas) {
				preguntaActual++;
			}
			siguientePanel();
		}
	}

	/**
	 * Esta clase es el action listener del boton terminar del panel para responder
	 * las preguntas de tipo verdadero falso.
	 */
	private class botonVolverPanelVerdaderoFalso_Listener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			if ((examenAlumno.TiempoLimite == true)
					&& ((System.currentTimeMillis() - TiempoExamen) > examenAlumno.getTiempoLimite())) {
				Mensajes.mensaje("SE ACABO EL TIEMPO!");
				revisarExamen();
				horaFinal = LocalDateTime.now();
				actualizarPanelTab(panelFinalizarExamen(), tabPreguntasActual);
				return;
			}

			if (group.getSelection() == null) {
				examenAlumno.preguntas_VF.get(preguntaActual).setRespuestaAlumno(null);
			} else {
				examenAlumno.preguntas_VF.get(preguntaActual)
						.setRespuestaAlumno(group.getSelection().getActionCommand());
			}

			if (NUMEROPREGUNTA > 1) {
				NUMEROPREGUNTA--;
			}
			if (!detenerRetrocesoPreguntas) {
				preguntaActual--;
			}

			panelAnterior();
		}

	}

	/**
	 * Esta clase es el action listener del boton volver del panel para responder
	 * las preguntas de tipo verdadero falso.
	 */
	private class botonTerminarPanelVerdaderoFalso_Listener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			if (examenAlumno != null) {
				if ((examenAlumno.TiempoLimite == true)
						&& ((System.currentTimeMillis() - TiempoExamen) > examenAlumno.getTiempoLimite())) {
					Mensajes.mensaje("SE ACABO EL TIEMPO!");
					revisarExamen();
					horaFinal = LocalDateTime.now();
					actualizarPanelTab(panelFinalizarExamen(), tabPreguntasActual);
					return;
				}

				if (group.getSelection() == null) {
					examenAlumno.preguntas_VF.get(preguntaActual).setRespuestaAlumno(null);
				} else {
					examenAlumno.preguntas_VF.get(preguntaActual)
							.setRespuestaAlumno(group.getSelection().getActionCommand());
				}

				if (Mensajes.consultar(
						"<html>Estas a punto de finalizar la prueba...<br><p align = \"center\">¿Estas segur@?</p></html>",
						"Finalizar")) {
					TERMINARANTES = true;
					horaFinal = LocalDateTime.now();
					revisarExamen();
					if (!VISTAPREVIA) {
						actualizarPanelTab(panelFinalizarExamen(), tabPreguntasActual);
					} else {
						cambiarPanel(panelFinalizarExamen());
					}
				} else {
					return;
				}
			}
		}
	}

	// endregion

	// region de paneles y clases de la pregunta de tipo respuesta numerica.

	/**
	 * Este panel le muestra la pregunta de tipo respuestas numericas, además de
	 * habilitar un espacio para responder la pregunta (solo permite el ingreso de
	 * números y 1 punto).
	 * 
	 * @return retorna el panel generado.
	 */
	private JPanel panelRespuestasNumericas() {

		String Pregunta = (examenAlumno == null)
				? "Por favor vuelva al panel anterior, o reinicie el programa e intentelo nuevamente."
				: examenAlumno.preguntas_RM.get(preguntaActual).getPregunta();

		int Peso = (examenAlumno == null) ? 0 : examenAlumno.preguntas_RM.get(preguntaActual).getPeso();

		JPanel Panel = new JPanel(new BorderLayout()) {

			private static final long serialVersionUID = -5069106988844695224L;

			@Override
			protected void paintComponent(Graphics g) {
				BufferedImage img = ImagenesAleatorias.getImagen();
				if (img != null) {
					super.paintComponent(g);
					g.drawImage(img, 0, 0, 1000, 669, 0, 0, img.getWidth(), img.getHeight(), this);
				} else {
					super.paintComponent(g);
					g.setColor(Color.decode("#969696"));
				}
			}
		};
		Panel.setBackground(Color.decode("#969696"));
		Panel.setBorder(new EmptyBorder(new Insets(46, 157, 47, 157)));

		JPanel panel = new JPanel(new BorderLayout(0, 4));
		panel.setBorder(new EmptyBorder(new Insets(20, 25, 11, 25)));
		Panel.add(panel, BorderLayout.CENTER);

		// titulo
		JLabel lblTitulo = new JLabel("Pregunta numero " + NUMEROPREGUNTA + " (" + Peso
				+ (((Peso == 1) || (Peso == -1)) ? " punto)" : " puntos)"));
		lblTitulo.setFont(new Font("Segoe UI", Font.PLAIN, 25));
		panel.add(lblTitulo, BorderLayout.NORTH);

		// panel del centro

		JPanel panelDelCentro = new JPanel(new BorderLayout());
		panel.add(panelDelCentro, BorderLayout.CENTER);

		JScrollPane scrollPane = new JScrollPane();
		panelDelCentro.add(scrollPane, BorderLayout.CENTER);

		JTextArea text = new JTextArea(19, 60);
		text.setLineWrap(true);
		text.setWrapStyleWord(true);
		text.setText(Pregunta);
		text.setEditable(false);
		scrollPane.setViewportView(text);

		respuestaAlumnoField = new JTextField(10);
		respuestaAlumnoField.setFont(new Font("Segoe UI", Font.PLAIN, 20));
		respuestaAlumnoField.setTransferHandler(null);
		respuestaAlumnoField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {

				char letraIngresada = e.getKeyChar();

				if (!((letraIngresada >= '0') && (letraIngresada <= '9') || (letraIngresada == '-')
						|| (letraIngresada == '+') || (letraIngresada == '.')
						|| (letraIngresada == KeyEvent.VK_BACK_SPACE) || (letraIngresada == KeyEvent.VK_DELETE))) {
					e.consume();
				}

				if ((letraIngresada == '-' || letraIngresada == '+') && (respuestaAlumnoField.getText().length() > 0)) {
					e.consume();
				}

				if (letraIngresada == '.' && (respuestaAlumnoField.getText().contains(".")
						|| (respuestaAlumnoField.getText().length() < 1))) {
					e.consume();
				}
			}
		});
		new TextPrompt("Su respuesta", respuestaAlumnoField);
		panelDelCentro.add(respuestaAlumnoField, BorderLayout.SOUTH);

		// panel inferior

		JPanel panelBotonesInferiores = new JPanel(new BorderLayout());
		panelBotonesInferiores.setBorder(new EmptyBorder(10, 0, 10, 0));
		panel.add(panelBotonesInferiores, BorderLayout.SOUTH);

		JButton botonTerminarPanelRespuestasNumericas = new JButton("Terminar");
		botonTerminarPanelRespuestasNumericas.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		botonTerminarPanelRespuestasNumericas.addActionListener(new botonTerminarPanelRespuestasNumericas_Listener());
		botonTerminarPanelRespuestasNumericas.setBackground(Color.decode("#00668c"));

		if (examenAlumno != null && !examenAlumno.getMostrarRespuestas() && preguntaActual >= 0) {
			JButton botonVolverPanelRespuestasNumericas = new JButton();
			establecerIcono(botonVolverPanelRespuestasNumericas, "\\res\\izquierda.png", "Volver");
			botonVolverPanelRespuestasNumericas.addActionListener(new botonVolverPanelRespuestasNumericas_Listener());
			botonVolverPanelRespuestasNumericas.setBackground(Color.decode("#0077ff"));
			panelBotonesInferiores.add(botonVolverPanelRespuestasNumericas, BorderLayout.WEST);
			if (examenAlumno != null && NUMEROPREGUNTA < examenAlumno.obtenerTotalPreguntas()) {
				panelBotonesInferiores.add(botonTerminarPanelRespuestasNumericas, BorderLayout.CENTER);
				respuestaAlumnoField.addActionListener(new botonContinuarPanelRespuestasNumericas_Listener());
			}

			// para poder mantener la respuesta del alumno al retroceder.
			if (examenAlumno != null && examenAlumno.preguntas_RM != null && examenAlumno.preguntas_RM.size() > 1
					&& examenAlumno.preguntas_RM.get(preguntaActual).getRespuestaAlumno() != null) {
				respuestaAlumnoField.setText(examenAlumno.preguntas_RM.get(preguntaActual).getRespuestaAlumno());
			}

		} else {
			panelBotonesInferiores.add(botonTerminarPanelRespuestasNumericas, BorderLayout.WEST);
		}

		if (examenAlumno != null
				&& (NUMEROPREGUNTA < examenAlumno.obtenerTotalPreguntas() || examenAlumno.getMostrarRespuestas())) {
			JButton botonContinuarPanelRespuestasNumericas = new JButton();
			establecerIcono(botonContinuarPanelRespuestasNumericas, "\\res\\derecha.png", "Continuar");
			botonContinuarPanelRespuestasNumericas
					.addActionListener(new botonContinuarPanelRespuestasNumericas_Listener());
			botonContinuarPanelRespuestasNumericas.setBackground(Color.decode("#0077ff"));
			panelBotonesInferiores.add(botonContinuarPanelRespuestasNumericas, BorderLayout.EAST);
		} else {
			panelBotonesInferiores.add(botonTerminarPanelRespuestasNumericas, BorderLayout.EAST);
		}

		return Panel;
	}

	/**
	 * Esta clase es el action listener del boton continuar del panel para responder
	 * las preguntas de tipo respuesta numerica.
	 */
	private class botonContinuarPanelRespuestasNumericas_Listener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			if (examenAlumno != null) {
				if ((examenAlumno.TiempoLimite == true)
						&& ((System.currentTimeMillis() - TiempoExamen) > examenAlumno.getTiempoLimite())) {
					Mensajes.mensaje("SE ACABO EL TIEMPO!");
					revisarExamen();
					horaFinal = LocalDateTime.now();
					actualizarPanelTab(panelFinalizarExamen(), tabPreguntasActual);
					return;
				}

				if (respuestaAlumnoField == null || respuestaAlumnoField.getText().isBlank()) {
					examenAlumno.preguntas_RM.get(preguntaActual).setRespuestaAlumno(null);
				} else {
					examenAlumno.preguntas_RM.get(preguntaActual).setRespuestaAlumno(respuestaAlumnoField.getText());
				}

				if (examenAlumno.getMostrarRespuestas()) {
					if (respuestaAlumnoField != null && !respuestaAlumnoField.getText().isBlank()) {

						Float respuestaAlumno = Float.parseFloat(respuestaAlumnoField.getText());
						Float textoRespuesta = examenAlumno.preguntas_RM.get(preguntaActual).getRespuesta();

						Float minimo = examenAlumno.preguntas_RM.get(preguntaActual).getMinimo();
						Float maximo = examenAlumno.preguntas_RM.get(preguntaActual).getMaximo();

						if (textoRespuesta != null && respuestaAlumno.equals(textoRespuesta)) {
							Mensajes.mensaje("Felicidades, la respuesta correcta era " + textoRespuesta + ".");
						} else if (minimo != null && (respuestaAlumno >= minimo && respuestaAlumno <= textoRespuesta)) {
							Mensajes.mensaje(
									"<html>Estuviste cerca de la respuesta correcta, pero es valida.<br>La respuesta correcta es: "
											+ textoRespuesta + ".</html>");
						} else if (maximo != null && (respuestaAlumno <= maximo && respuestaAlumno >= textoRespuesta)) {
							Mensajes.mensaje(
									"<html>Estuviste cerca de la respuesta correcta, pero es valida.<br>La respuesta correcta es: "
											+ textoRespuesta + ".</html>");
						} else {
							Mensajes.mensaje("La respuesta correcta era " + textoRespuesta + "...");
						}

					} else {
						Mensajes.mensaje("Por favor ingrese una respuesta valida.");
						return;
					}
				}
				if (NUMEROPREGUNTA <= examenAlumno.obtenerTotalPreguntas()) {
					NUMEROPREGUNTA++;
				} else {
					detenerAvancePreguntas = true;
				}

				if (!detenerAvancePreguntas) {
					preguntaActual++;
				}
				siguientePanel();
			}

		}

	}

	/**
	 * Esta clase es el action listener del boton terminar del panel para responder
	 * las preguntas de tipo respuesta numerica.
	 */
	private class botonTerminarPanelRespuestasNumericas_Listener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			if (examenAlumno != null) {
				TERMINARANTES = true;

				if ((examenAlumno.TiempoLimite == true)
						&& ((System.currentTimeMillis() - TiempoExamen) > examenAlumno.getTiempoLimite())) {
					Mensajes.mensaje("SE ACABO EL TIEMPO!");
					revisarExamen();
					horaFinal = LocalDateTime.now();
					actualizarPanelTab(panelFinalizarExamen(), tabPreguntasActual);
					return;
				}

				if (respuestaAlumnoField == null || respuestaAlumnoField.getText().isBlank()) {
					examenAlumno.preguntas_RM.get(preguntaActual).setRespuestaAlumno(null);
				} else {
					examenAlumno.preguntas_RM.get(preguntaActual).setRespuestaAlumno(respuestaAlumnoField.getText());
				}

				if (Mensajes.consultar(
						"<html>Estas a punto de finalizar la prueba...<br><p align = \"center\">¿Estas segur@?</p></html>",
						"Finalizar")) {
					revisarExamen();
					horaFinal = LocalDateTime.now();
					if (!VISTAPREVIA) {
						actualizarPanelTab(panelFinalizarExamen(), tabPreguntasActual);
					} else {
						cambiarPanel(panelFinalizarExamen());
					}
				} else {
					return;
				}
			}
		}
	}

	/**
	 * Esta clase es el action listener del boton volver del panel para responder
	 * las preguntas de tipo respuesta numerica.
	 */
	private class botonVolverPanelRespuestasNumericas_Listener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			if ((examenAlumno.TiempoLimite == true)
					&& ((System.currentTimeMillis() - TiempoExamen) > examenAlumno.getTiempoLimite())) {
				Mensajes.mensaje("SE ACABO EL TIEMPO!");
				horaFinal = LocalDateTime.now();
				revisarExamen();
				actualizarPanelTab(panelFinalizarExamen(), tabPreguntasActual);
				return;
			}

			if (respuestaAlumnoField == null || respuestaAlumnoField.getText().isBlank()) {
				examenAlumno.preguntas_RM.get(preguntaActual).setRespuestaAlumno(null);
			} else {
				examenAlumno.preguntas_RM.get(preguntaActual).setRespuestaAlumno(respuestaAlumnoField.getText());
			}

			if (NUMEROPREGUNTA > 1) {
				NUMEROPREGUNTA--;
			}
			if (!detenerRetrocesoPreguntas) {
				preguntaActual--;
			}

			panelAnterior();

		}

	}

	// endregion

	// region del panel de bienvenida.

	/**
	 * Este es un panel inicial, para que cada vez que se desea volver al incio, se
	 * devuelva a este.
	 * 
	 * @return retorna el panel generado.
	 */
	private JPanel panelBienvenidaAlumno() {

		JPanel Panel = new JPanel(new BorderLayout()) {

			private static final long serialVersionUID = 7609068827072671859L;

			@Override
			protected void paintComponent(Graphics g) {
				BufferedImage img = ImagenesAleatorias.getImagen();
				if (img != null) {
					super.paintComponent(g);
					g.drawImage(img, 0, 0, 1000, 669, 0, 0, img.getWidth(), img.getHeight(), this);
				} else {
					super.paintComponent(g);
					g.setColor(Color.decode("#7C7E7C"));
				}
			}
		};
		Panel.setBackground(Color.decode("#7C7E7C"));
		Panel.setBorder(new EmptyBorder(new Insets(160, 200, 160, 200)));

		JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder(new EmptyBorder(new Insets(50, 70, 50, 70)));
		Panel.add(panel, BorderLayout.CENTER);

		JLabel lblIniciarSesin = new JLabel("Bienvenid@ " + nombreAlumno);
		lblIniciarSesin.setForeground(Color.decode("#707070"));
		lblIniciarSesin.setFont(new Font("Segoe UI", Font.PLAIN, 35));
		panel.add(lblIniciarSesin, BorderLayout.PAGE_START);

		JLabel lblNewLabel = new JLabel(
				"<html>Te encuentras en la plataforma de evaluaciones.<br>Para empezar, selecciona una de las pestañas que<br>se encuentran arriba.<br></html>");
		lblNewLabel.setFont(new Font("Segoe UI", Font.PLAIN, 20));
		panel.add(lblNewLabel, BorderLayout.CENTER);

		return Panel;

	}

	// endregion

	// region de los constructores.

	/**
	 * Constructor del alumno. Con esto, se da inicio a la vista previa del examen.
	 * 
	 * @param examenVistaPrevia es el examen, el cuál despues se mostrara en una
	 *                          ventana removible para el profesor, con el fin de
	 *                          que vea como es el examen.
	 */
	Alumno(Examen examenVistaPrevia) {
		examenAlumno = examenVistaPrevia;
		nombreAlumno = "vista";
		apellidoAlumno = "previa";
		rutAlumno = "00.000.000-0";
		VISTAPREVIA = true;
		add(panelResponderProfesorDescripcion());
	}

	/**
	 * Constructor del alumno. Con esto, se da inicio al panel del alumno.
	 * 
	 * @param Nombre   es el nombre del alumno, se guarda en los resultados del
	 *                 alumno.
	 * @param Apellido es el apellido del alumno, se guarda en los resultados del
	 *                 alumno.
	 * @param Rut      es el rut del alumno, se guarda en lso resultados del alumno.
	 * 
	 */
	Alumno(String Nombre, String Apellido, String Rut) {
		nombreAlumno = (Nombre);
		apellidoAlumno = (Apellido);
		rutAlumno = (Rut);

		JPanel panelBienvenida = panelBienvenidaAlumno();
		JPanel panelPregDef = panelResponderDefecto();
		JPanel panelPregProfe = panelResponderProfesor();
		JPanel panelPDF = panelVisualizarPdf();
		JPanel panelCerrar = panelCerrarSesion();

		tabbedPane.addTab(BIENVENIDA, panelBienvenida);
		tabbedPane.addTab(RESPONDERPREGUNTASDEFECTO, panelPregDef);
		tabbedPane.addTab(RESPONDERPREGUNTASPROFESOR, panelPregProfe);
		tabbedPane.addTab(VISUALIZARPDF, panelPDF);
		tabbedPane.addTab(CERRARSESION, panelCerrar);

		tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

		// para probar el panel, es necesario descomentar lo siguiente:

		/*-	
		JFrame ventana = new JFrame();
		ventana.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		ventana.setTitle("Menú Alumno");
		ventana.setResizable(false);
		
		JPanel PanelPrincipal = new JPanel(new BorderLayout());
		PanelPrincipal.setBorder(new EmptyBorder(5, 5, 5, 5));
		ventana.setContentPane(PanelPrincipal);*/

		// y comentar la linea de abajo.
		add(tabbedPane);

		/*-
		PanelPrincipal.add(tabbedPane);
		
		ventana.pack();
		ventana.setLocationRelativeTo(null);
		ventana.setVisible(true);
		*/
	}

	// endregion

	// region de los metodos que cambian o manipulan el panel.

	/**
	 * Este metodo tiene la logica para poder ir al panel anterior, manteniendo la
	 * respuesta del usuario si es que ha escrito/seleccionado alguna.
	 */
	private void panelAnterior() {
		if (examenAlumno != null) {
			if (examenAlumno.TiempoLimite == true && TiempoExamen == 0) {
				TiempoExamen = System.currentTimeMillis();
			}

			if (hayPreguntaAnterior && itemsUsados >= 0 && NUMEROPREGUNTA >= 0) {
				if (SELECCIONMULTIPLE.equalsIgnoreCase(ordenPreguntas[itemsUsados])) {
					if (examenAlumno.verificarExamen(1)) {
						detenerAvancePreguntas = false;
						if (preguntaActual >= 0) {
							if (!VISTAPREVIA) {
								actualizarPanelTab(panelSeleccionMultiple(), tabPreguntasActual);
							} else {
								cambiarPanel(panelSeleccionMultiple());
							}
						} else {
							itemsUsados--;
							verificarPanelAnterior();
							panelAnterior();
						}

					} else {
						itemsUsados--;
						panelAnterior();
					}
				} else if (VERDADEROFALSO.equalsIgnoreCase(ordenPreguntas[itemsUsados])) {
					if (examenAlumno.verificarExamen(2)) {
						detenerAvancePreguntas = false;
						if (preguntaActual >= 0) {
							if (!VISTAPREVIA) {
								actualizarPanelTab(panelVerdaderoFalso(), tabPreguntasActual);
							} else {
								cambiarPanel(panelVerdaderoFalso());
							}
						} else {
							itemsUsados--;
							verificarPanelAnterior();
							panelAnterior();
						}
					} else {
						itemsUsados--;
						panelAnterior();
					}
				} else if (RESPUESTASCORTAS.equalsIgnoreCase(ordenPreguntas[itemsUsados])) {
					if (examenAlumno.verificarExamen(3)) {
						detenerAvancePreguntas = false;
						if (preguntaActual >= 0) {
							if (!VISTAPREVIA) {
								actualizarPanelTab(panelRespuestasCortas(), tabPreguntasActual);
							} else {
								cambiarPanel(panelRespuestasCortas());
							}
						} else {
							itemsUsados--;
							verificarPanelAnterior();
							panelAnterior();
						}
					} else {
						itemsUsados--;
						panelAnterior();
					}
				} else if (RESPUESTASNUMERICAS.equalsIgnoreCase(ordenPreguntas[itemsUsados])) {
					if (examenAlumno.verificarExamen(4)) {
						detenerAvancePreguntas = false;
						if (preguntaActual >= 0) {
							if (!VISTAPREVIA) {
								actualizarPanelTab(panelRespuestasNumericas(), tabPreguntasActual);
							} else {
								cambiarPanel(panelRespuestasNumericas());
							}
						} else {
							itemsUsados--;
							verificarPanelAnterior();
							panelAnterior();
						}
					} else {
						itemsUsados--;
						panelAnterior();
					}
				}
			} else {
				Mensajes.error("¡Ya no se puede devolver, no hay preguntas que mostrar!");
				if (!hayPreguntaAnterior) {
					preguntaActual = 0;
					itemsUsados = 0;
				}
				return;
			}
		} else {
			Mensajes.error("¡Por favor, reporta este error al administrador!");
			return;
		}

	}

	/**
	 * Este metodo tiene la logica para poder ir al panel siguiente, manteniendo la
	 * respuesta del usuario si es que ha escrito/seleccionado alguna.
	 */
	private void siguientePanel() {

		if (examenAlumno != null) {

			if (examenAlumno.TiempoLimite == true && TiempoExamen == 0) {
				TiempoExamen = System.currentTimeMillis();
			}

			hayPreguntaAnterior = true;

			if (itemsUsados < ordenPreguntas.length && NUMEROPREGUNTA <= examenAlumno.obtenerTotalPreguntas()
					&& !detenerAvancePreguntas) {

				if (SELECCIONMULTIPLE.equalsIgnoreCase(ordenPreguntas[itemsUsados])) {
					if (examenAlumno.verificarExamen(1)) {
						if (preguntaActual < examenAlumno.preguntas_SM.size()) {
							if (!VISTAPREVIA) {
								actualizarPanelTab(panelSeleccionMultiple(), tabPreguntasActual);
							} else {
								cambiarPanel(panelSeleccionMultiple());
							}
						} else {
							preguntaActual = 0;
							itemsUsados++;
							siguientePanel();
						}

					} else {
						itemsUsados++;
						siguientePanel();
					}
				} else if (VERDADEROFALSO.equalsIgnoreCase(ordenPreguntas[itemsUsados])) {
					if (examenAlumno.verificarExamen(2)) {

						if (preguntaActual < examenAlumno.preguntas_VF.size()) {
							if (!VISTAPREVIA) {
								actualizarPanelTab(panelVerdaderoFalso(), tabPreguntasActual);
							} else {
								cambiarPanel(panelVerdaderoFalso());
							}
						} else {
							preguntaActual = 0;
							itemsUsados++;
							siguientePanel();
						}
					} else {
						itemsUsados++;
						siguientePanel();
					}
				} else if (RESPUESTASCORTAS.equalsIgnoreCase(ordenPreguntas[itemsUsados])) {
					if (examenAlumno.verificarExamen(3)) {

						if (preguntaActual < examenAlumno.preguntas_RC.size()) {
							if (!VISTAPREVIA) {
								actualizarPanelTab(panelRespuestasCortas(), tabPreguntasActual);
							} else {
								cambiarPanel(panelRespuestasCortas());
							}
						} else {
							preguntaActual = 0;
							itemsUsados++;
							siguientePanel();
						}
					} else {
						itemsUsados++;
						siguientePanel();
					}
				} else if (RESPUESTASNUMERICAS.equalsIgnoreCase(ordenPreguntas[itemsUsados])) {
					if (examenAlumno.verificarExamen(4)) {

						if (preguntaActual < examenAlumno.preguntas_RM.size()) {
							if (!VISTAPREVIA) {
								actualizarPanelTab(panelRespuestasNumericas(), tabPreguntasActual);
							} else {
								cambiarPanel(panelRespuestasNumericas());
							}
						} else {
							preguntaActual = 0;
							itemsUsados++;
							siguientePanel();
						}
					} else {
						itemsUsados++;
						siguientePanel();
					}
				}
			} else {

				if (examenAlumno != null) {

					if (!examenAlumno.getMostrarRespuestas()) {
						detenerAvancePreguntas = true;
						if (!Mensajes.consultar("¿Estas seguro de continuar?, estas a punto de finalizar el examen.",
								"")) {
							return;
						}
					}
				}

				revisarExamen();
			}
		} else {
			Mensajes.error("¡Por favor, reporta este error al administrador!");
			return;
		}

	}

	/**
	 * Este metodo, verifica si es que se puede devolver al panel anterior, con el
	 * fin de evitar que se "salga" de los limites del examen.
	 */
	private void verificarPanelAnterior() {

		if (itemsUsados < 0) {
			itemsUsados = 0;
			hayPreguntaAnterior = false;
		} else {
			hayPreguntaAnterior = true;
		}

		if (hayPreguntaAnterior) {
			if (SELECCIONMULTIPLE.equalsIgnoreCase(ordenPreguntas[itemsUsados])) {
				if (examenAlumno.verificarExamen(1)) {
					preguntaActual = examenAlumno.preguntas_SM.size() - 1;
					return;
				}
			}

			if (VERDADEROFALSO.equalsIgnoreCase(ordenPreguntas[itemsUsados])) {
				if (examenAlumno.verificarExamen(2)) {
					preguntaActual = examenAlumno.preguntas_VF.size() - 1;
					return;
				}
			}

			if (RESPUESTASCORTAS.equalsIgnoreCase(ordenPreguntas[itemsUsados])) {
				if (examenAlumno.verificarExamen(3)) {
					preguntaActual = examenAlumno.preguntas_RC.size() - 1;
					return;
				}
			}

			if (RESPUESTASNUMERICAS.equalsIgnoreCase(ordenPreguntas[itemsUsados])) {
				if (examenAlumno.verificarExamen(4)) {
					preguntaActual = examenAlumno.preguntas_RM.size() - 1;
					return;
				}
			}
		}
	}

	/**
	 * Este metodo permite cambiar el panel actual, por el que se desee. Este metodo
	 * esta diseñado especificamente para el caso de la vista previa.
	 * 
	 * @param panel es el panel que se desea mostrar en la pantalla.
	 */
	private void cambiarPanel(JPanel panel) {
		removeAll();
		add(panel);
		validate();
	}

	/**
	 * Este método se encarga de establecer un panel en el tab que se le
	 * especifique.
	 * 
	 * @param panel es el panel a insertar.
	 * @param tab   es el tab en donde se desea insertar el tab. El orden es el
	 *              siguiente:
	 *              <ol>
	 *              <li>tab panel bienvenida.</li>
	 *              <li>tab panel responder preguntas por defecto.</li>
	 *              <li>tab panel responder preguntas del profesor.</li>
	 *              <li>tab panel cerrar sesión.</li>
	 *              <li>tab panel vista previa profesor.</li>
	 *              <li>tab panel visualizar pdf.</li>
	 *              </ol>
	 * 
	 */
	private void actualizarPanelTab(JPanel panel, int tab) {
		if (tab == 1) {
			tabbedPane.setComponentAt(tabbedPane.indexOfTab(BIENVENIDA), panel);
		} else if (tab == 2) {
			tabbedPane.setComponentAt(tabbedPane.indexOfTab(RESPONDERPREGUNTASDEFECTO), panel);
		} else if (tab == 3) {
			tabbedPane.setComponentAt(tabbedPane.indexOfTab(RESPONDERPREGUNTASPROFESOR), panel);
		} else if (tab == 4) {
			tabbedPane.setComponentAt(tabbedPane.indexOfTab(CERRARSESION), panel);
		} else if (tab == 5) {
			tabbedPane.setComponentAt(tabbedPane.indexOfTab(VISTAPREVIAPROFESOR), panel);
		} else if (tab == 6) {
			tabbedPane.setComponentAt(tabbedPane.indexOfTab(VISUALIZARPDF), panel);
		}
	}

	/**
	 * Este método se encarga de establecer un icono en un botón. En caso de que no
	 * encuentre el icono, establecera el texto que se le ha dado, y si aún asi no
	 * se le ha dado un texto, establecerá null como texto en el icono.
	 * 
	 * @param boton          es el boton a modificar.
	 * @param ubicacionIcono es la ubicación en donde se encuentra el icono.
	 * @param texto          es el texto a establecer, en caso de que no encuentre
	 *                       el icono.
	 */
	private void establecerIcono(JButton boton, String ubicacionIcono, String texto) {
		try {
			// verifico si existe la imagen
			ImageIO.read(new File(new File(".").getCanonicalPath() + ubicacionIcono));
			// si existe, lo agrego al boton
			boton.setIcon(new ImageIcon(new File(".").getCanonicalPath() + ubicacionIcono));
		} catch (Exception e) {
			// si no existe, entrará aca, para luego reemplazar el botón con el texto.
			if (texto == null || texto.isBlank()) {
				// si el texto ingresado es invalido, o no tiene nada, establecerá el texto por
				// defecto (null)
				boton.setText("null");
				boton.setFont(new Font("Segoe UI", Font.PLAIN, 18));
				return;
			}
			boton.setText(texto);
			boton.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		}
	}

	/**
	 * Este metodo se encarga de activar/desactivar todos los tabs del jtabbedpane.
	 * 
	 * @param opcion es la opcion a establecer, puede set {@code true} para activar
	 *               todo, o {@code false} para desactivarlos a todos.
	 */
	private void tabActivado(boolean opcion) {
		for (int i = 0; i < tabbedPane.getTabCount(); i++) {
			tabbedPane.setEnabledAt(i, opcion);
		}
	}

	/**
	 * Este metodo se encarga de crear tabs con una x, con el fin de poder cerrar
	 * los tabs.
	 * 
	 * @param panel  es el panel que se desea agregar al jtabbedpane.
	 * @param titulo es el titulo del tab. Para este panel (alumno), se llama
	 *               "Prueba + numero"
	 */
	private void agregarVistaPrevia(JPanel panel, String titulo) {

		panel.setPreferredSize(tabbedPane.getComponentAt(0).getPreferredSize());

		tabbedPane.addTab(titulo, panel);
		int index = tabbedPane.indexOfTab(titulo);
		JPanel pnlTab = new JPanel(new GridBagLayout());
		pnlTab.setOpaque(false);
		JLabel lblTitle = new JLabel(titulo);
		JButton btnClose = new JButton("x");

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1;

		pnlTab.add(lblTitle, gbc);

		gbc.gridx++;
		gbc.weightx = 0;
		pnlTab.add(btnClose, gbc);

		tabbedPane.setTabComponentAt(index, pnlTab);

		btnClose.addActionListener(new cerrarTab_Listener(titulo));
	}

	/**
	 * Esta clase, es el actionlistener del botón x de la vista previa. Esto permite
	 * cerrar el tab en donde se encuentra la x.
	 */
	private class cerrarTab_Listener implements ActionListener {

		private String nombreTab;

		private cerrarTab_Listener(String tabName) {
			this.nombreTab = tabName;
		}

		@Override
		public void actionPerformed(ActionEvent evt) {

			int index = tabbedPane.indexOfTab(getNombreTab());
			if (index >= 0) {
				tabbedPane.removeTabAt(index);
			}

		}

		public String getNombreTab() {
			return nombreTab;
		}
	}

	// endregion

	// region de los metodos que utilizan o manipulan el examen.

	/**
	 * Este metodo se encarga, para tal como dice su nombre, revisar el examen. Aquí
	 * es donde se verifica si el usuario ha respondido correcta o incorrectamente.
	 */
	private void revisarExamen() {
		itemsUsados = 0;
		CANTIDADRESPUESTASCORRECTAS = 0;
		puntajeTotal = 0;
		CANTIDADRESPUESTASINCORRECTAS = 0;

		if (examenAlumno.verificarExamen(1)) {
			for (SeleccionMultiple preguntas : examenAlumno.preguntas_SM) {
				if (preguntas.getRespuestaAlumno() != null
						&& preguntas.getRespuestaAlumno().equals(preguntas.getOpciones()[preguntas.getRespuesta()])) {
					puntajeTotal += preguntas.getPeso();
					CANTIDADRESPUESTASCORRECTAS++;
				} else {
					CANTIDADRESPUESTASINCORRECTAS++;
				}
			}
		}

		if (examenAlumno.verificarExamen(2)) {
			for (VerdaderoFalso preguntas : examenAlumno.preguntas_VF) {

				String respuesta = (preguntas.getRespuesta() == true) ? "verdadero" : "falso";

				if (preguntas.getRespuestaAlumno() != null && preguntas.getRespuestaAlumno().equals(respuesta)) {
					puntajeTotal += preguntas.getPeso();
					CANTIDADRESPUESTASCORRECTAS++;
				} else {
					CANTIDADRESPUESTASINCORRECTAS++;
				}
			}
		}

		if (examenAlumno.verificarExamen(3)) {
			for (RespuestasCortas preguntas : examenAlumno.preguntas_RC) {

				String respuestaAlumno = preguntas.getRespuestaAlumno();

				if (respuestaAlumno != null && Texto.similitud(preguntas.getRespuesta(), respuestaAlumno) > 79) {
					puntajeTotal += preguntas.getPeso();
					CANTIDADRESPUESTASCORRECTAS++;
				} else {
					CANTIDADRESPUESTASINCORRECTAS++;
				}
			}
		}

		if (examenAlumno.verificarExamen(4)) {

			for (RespuestaNumerica preguntas : examenAlumno.preguntas_RM) {

				if (preguntas.getRespuestaAlumno() != null && !preguntas.getRespuestaAlumno().isEmpty()) {
					Float respuestaAlumno = Float.parseFloat(preguntas.getRespuestaAlumno());
					Float respuestaCorrecta = preguntas.getRespuesta();

					Float minimo = preguntas.getMinimo();
					Float maximo = preguntas.getMaximo();

					if (preguntas.getRespuestaAlumno() != null) {

						if (respuestaCorrecta != null && respuestaAlumno.equals(respuestaCorrecta)) {
							puntajeTotal += preguntas.getPeso();
							CANTIDADRESPUESTASCORRECTAS++;
						} else if (minimo != null && (respuestaAlumno >= minimo && respuestaAlumno <= respuestaCorrecta)) {
							puntajeTotal += preguntas.getPeso();
							CANTIDADRESPUESTASCORRECTAS++;
						} else if (maximo != null && (respuestaAlumno <= maximo && respuestaAlumno >= respuestaCorrecta)) {
							puntajeTotal += preguntas.getPeso();
							CANTIDADRESPUESTASCORRECTAS++;
						} else {
							CANTIDADRESPUESTASINCORRECTAS++;
						}
					} else {
						CANTIDADRESPUESTASINCORRECTAS++;
					}
				} else {
					CANTIDADRESPUESTASINCORRECTAS++;
				}
			}
		}

		if (!VISTAPREVIA) {
			actualizarPanelTab(panelFinalizarExamen(), tabPreguntasActual);
		} else {
			cambiarPanel(panelFinalizarExamen());
		}
	}

	/**
	 * Este metodo se encarga de cambiar el mensaje final del alumno, en el cual se
	 * le muestra su nota, cantidad de respuestas correctas, como tambien de
	 * incorrectas y su porcentaje de aciertos.
	 * 
	 * @return retorna un label que contiene los resultados del alumno.
	 */
	private JLabel cambiarMensajeFinalAlumno() {

		JLabel labelResultados = new JLabel();

		notaFinal = (examenAlumno == null) ? 1
				: ((float) examenAlumno.calcular_nota(puntajeTotal, 60/* examenAlumno.getPorcentaje() */) / 10);
		notaFinalString = String.format("%.1f", notaFinal);

		int porcentaje = (examenAlumno == null) ? 0
				: (int) (((float) puntajeTotal / examenAlumno.obtener_puntaje_total()) * 100);

		if (TERMINARANTES && examenAlumno != null) {
			CANTIDADRESPUESTASINCORRECTAS = (examenAlumno.obtenerTotalPreguntas()) - CANTIDADRESPUESTASCORRECTAS;
		} else if (TERMINARANTES && examenAlumno == null) {
			CANTIDADRESPUESTASINCORRECTAS = 0;
			CANTIDADRESPUESTASCORRECTAS = 0;
		}

		labelResultados
				.setText(
						"<html><div contenteditable>Tu puntaje es <span style=\"color: #ffcc00\">" + puntajeTotal
								+ "</span>, lo que te da un porcentaje de <span style=\"color: #cccc00\">" + porcentaje
								+ "</span>% de <br>respuestas correctas respondidas, mientras que tu nota <br>es "
								+ ((notaFinal >= 4) ? "<span style=\"color: #0066ff\">" + notaFinalString + "</span>"
										: "<span style=\"color: #ff0000\">" + notaFinalString + "</span>")
								+ ". Obtuviste "
								+ ((CANTIDADRESPUESTASCORRECTAS == 1)
										? "<span style=\"color: #00ff00\">" + CANTIDADRESPUESTASCORRECTAS
												+ "</span> respuesta correcta, y "
										: "<span style=\"color: #00ff00\">" + CANTIDADRESPUESTASCORRECTAS
												+ "</span> respuestas correctas, y ")
								+ ((CANTIDADRESPUESTASINCORRECTAS == 1)
										? "<span style=\"color: #ff0000\">" + CANTIDADRESPUESTASINCORRECTAS
												+ "</span> respuesta <br>incorrecta."
										: "<span style=\"color: #ff0000\">" + CANTIDADRESPUESTASINCORRECTAS
												+ "</span> respuestas <br>incorrectas.")
								+ "</div></html>");

		labelResultados.setFont(new Font("Segoe UI", Font.PLAIN, 20));

		return labelResultados;
	}

	/**
	 * Este metodo se encarga de obtener toda la información que tiene el examen,
	 * como lo es el puntaje total y el tiempo limite.
	 * 
	 * @return retorna la información del examen.
	 */
	private String obtenerInformacionExamen() {

		// puntaje del examen

		int puntajeTotal = ((examenAlumno == null) ? 0 : examenAlumno.obtener_puntaje_total());

		String mensajePuntajeTotal = "<html><p>El exámen tiene un total de " + puntajeTotal + " puntos";

		// tiempo del examen

		long tiempoLimite = ((examenAlumno == null) ? 0 : examenAlumno.getTiempoLimite());

		String mensajeTiempoLimite = ((tiempoLimite > 0)
				? ", además tienes un tiempo límite de " + (tiempoLimite / 60000)
						+ ((tiempoLimite == 1) ? " minuto." : " minutos.")
				: ".") + "</p>";

		// Mensajes.mensaje final del texto

		String mensajeFinal = "<br><p>Puedes terminar la prueba en cualquier momento que desees, pero recuerda que<br>las preguntas sin respuestas se consideraran malas. </p><br><h3>Suerte y vamos por el 7.0 !</h3></html>";

		// constructor del texto

		StringBuilder mensajeARetornar = new StringBuilder();

		mensajeARetornar.append(mensajePuntajeTotal);
		mensajeARetornar.append(mensajeTiempoLimite);
		mensajeARetornar.append(mensajeFinal);

		return mensajeARetornar.toString();
	}

	// endregion

}
