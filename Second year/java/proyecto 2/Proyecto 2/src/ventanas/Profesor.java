package ventanas;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.EventObject;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import org.javatuples.Triplet;

import base.Archivos;
import base.Examen;
import base.RespuestaNumerica;
import base.RespuestasCortas;
import base.RevisionAlumno;
import base.SeleccionMultiple;
import base.Texto;
import base.VerdaderoFalso;
import utilidadesArchivos.ExportarAExcel;
import utilidadesArchivos.ExportarAPDF;
import utilidadesArchivos.ExportarAWord;
import utilidadesInterfaces.ImagenesAleatorias;
import utilidadesInterfaces.Mensajes;
import utilidadesInterfaces.TextPrompt;
import utilidadesSeguridad.BASE64;
import utilidadesSeguridad.MD5;
import utilidadesSeguridad.RSA;

/**
 * Esta clase se encarga de hacer todo lo relacionado con el profesor.
 * 
 * @author Sebastian Morgado
 * @version 2.0.0
 * @since 2.0.0
 */
class Profesor extends JPanel {

	private static final long serialVersionUID = -7100844739097875673L;

	private static boolean PREGUNTACREADA = false;
	private static boolean CAMBIOHECHO = false;

	private final static String SELECCIONMULTIPLE = "selección multiple";
	private final static String RESPUESTANUMERICA = "respuestas numericas";
	private final static String VERDADEROFALSO = "verdadero falso";
	private final static String RESPUESTACORTA = "respuestas cortas";
	private final static String BIENVENIDA = "Bienvenid@ Profesor@";
	private final static String CREARPREGUNTAS = "Crear preguntas";
	private final static String EDITAREXAMEN = "Editar examen (solo archivos .smr)";
	private final static String RESULTADOSALUMNOS = "Resultados de alumnos";
	private final static String VISUALIZARPDF = "Visualizar PDF";
	private final static String VISUALIZARSMR = "Visualizar SMR";
	private final static String CERRARSESION = "Cerrar sesión";

	private static String PANELACTUAL = "";
	private static String TIPOPREGUNTA = "ninguna";
	private static String TIPOEXAMEN = "ninguno";
	private static int TABACTUAL;

	private static JTextField txtPregunta;
	private static JTextField txtfieldRespuesta;
	private static JTextField txtFieldUbicacionPDF;
	private static JTextField txtFieldUbicacionSMR;
	private static JTextField txtFieldContrasenaSMR;
	private static ArrayList<crearCheckTextRdBoton> rdBotones;
	private static List<RevisionAlumno> DatosArchivos;

	private Examen examenAlumno;

	private JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
	private ButtonGroup group;
	private JTextArea pregunta;
	private String[] opciones;
	private int peso;

	private boolean desordenarPreguntas = false;
	private boolean mostrarRespuestas = false;
	private boolean tiempoLimite = false;
	private boolean contrasenaEstablecida = false;
	private boolean agregarNombreYApellido = false;

	private int tiempo = 0;
	private int cantidadTotalPreguntas = 0;
	private String objetivo = "";
	private String fechaExamen;
	private String tituloExamen;
	private String cursoExamen;
	private String clase;
	private String nombreProfesor = "test";
	private String apellidoProfesor = "test";

	private JTextField txtUbicacionArchivo;
	private JTextField txtUbicacionCarpeta;
	private JTextField txtfieldPesoPregunta;
	private JTextField contrasenaResultados;
	private JTextField fieldContrasenaExamen;

	private String contrasenaExamen;
	private String tiempoMinutosExamen;

	private JTable tabla;

	private List<crearCheckTextBotones> botonesEditarPreguntas;
	private HashMap<String, Boolean> preguntasAEliminar;
	private crearCheckField valorMinimo;
	private crearCheckField valorMaximo;

	private String resultados = null;
	private List<String> resultadosSM = null;
	private List<String> resultadosVF = null;
	private List<String> resultadosRC = null;
	private List<Triplet<String, String, String>> resultadosRM = null;

	private int segundos = 1;
	private Timer espera;

	/**
	 * Constructor del profesor. Con esto, se da inicio al panel del profesor.
	 * 
	 * @param nombre   es el nombre del profesor, si se desea, se puede agregar al
	 *                 examen word o pdf generado.
	 * @param apellido es el apellido del profesor, si se desea, se puede agregar al
	 *                 examen word o pdf generado.
	 * 
	 */
	Profesor(String nombre, String apellido) {
		this.nombreProfesor = nombre;
		this.apellidoProfesor = apellido;

		JPanel panelBienvenida = panelBienvenidaProfesor();
		JPanel panelCrearPreguntas = panelCrearPreguntas();
		JPanel panelEditarPreguntas = panelEditarPreguntas();
		JPanel panelResultadosAlumnos = panelResultadosAlumnos();
		JPanel panelVisualizarSmr = panelVisualizarsSmr();
		JPanel panelVisualizarPdf = panelVisualizarPdf();
		JPanel panelCerrar = panelCerrarSesion();

		tabbedPane.addTab(BIENVENIDA, panelBienvenida);
		tabbedPane.addTab(CREARPREGUNTAS, panelCrearPreguntas);
		tabbedPane.addTab(EDITAREXAMEN, panelEditarPreguntas);
		tabbedPane.addTab(RESULTADOSALUMNOS, panelResultadosAlumnos);
		tabbedPane.addTab(VISUALIZARPDF, panelVisualizarPdf);
		tabbedPane.addTab(VISUALIZARSMR, panelVisualizarSmr);
		tabbedPane.addTab(CERRARSESION, panelCerrar);

		tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

		// para probar el panel, es necesario descomentar el jpanel y el jframe.

		/*-JFrame ventana = new JFrame();
		ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ventana.setTitle("Menú Profesor");
		ventana.setResizable(false);
		
		JPanel PanelPrincipal = new JPanel(new BorderLayout());
		PanelPrincipal.setBorder(new EmptyBorder(5, 5, 5, 5));
		ventana.setContentPane(PanelPrincipal);*/

		// y comentar la linea de abajo.
		add(tabbedPane);

		/*-PanelPrincipal.add(tabbedPane);
		
		ventana.pack();
		ventana.setLocationRelativeTo(null);
		ventana.setVisible(true);*/

	}

	// region del panel de bienvenida.

	/**
	 * Este es un panel inicial, para que cada vez que se desea volver al incio, se
	 * devuelva a este.
	 * 
	 * @return retorna el panel generado.
	 */
	private JPanel panelBienvenidaProfesor() {

		JPanel Panel = new JPanel(new BorderLayout()) {

			private static final long serialVersionUID = -2671596763069400864L;

			@Override
			protected void paintComponent(Graphics g) {
				BufferedImage img = ImagenesAleatorias.getImagen();
				if (img != null) {
					super.paintComponent(g);
					g.drawImage(img, 0, 0, 1000, 700, 0, 0, img.getWidth(), img.getHeight(), this);
				} else {
					super.paintComponent(g);
					g.setColor(Color.decode("#7C7E7C"));
				}
			}
		};
		Panel.setBackground(Color.decode("#7C7E7C"));
		Panel.setBorder(new EmptyBorder(new Insets(178, 208, 179, 208)));

		JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder(new EmptyBorder(new Insets(50, 70, 50, 70)));
		Panel.add(panel, BorderLayout.CENTER);

		JLabel lblIniciarSesin = new JLabel("Bienvenid@ " + nombreProfesor);
		lblIniciarSesin.setVerticalAlignment(SwingConstants.TOP);
		lblIniciarSesin.setForeground(Color.decode("#707070"));
		lblIniciarSesin.setFont(new Font("Segoe UI", Font.PLAIN, 30));
		panel.add(lblIniciarSesin, BorderLayout.PAGE_START);

		JLabel lblNewLabel = new JLabel(
				"<html><p align = \"center\">Te encuentras en la plataforma de evaluaciones.<br>Para empezar, selecciona una de las pestañas<br>que se encuentran arriba.</p></html>\r\n");
		lblNewLabel.setFont(new Font("Segoe UI", Font.PLAIN, 20));
		panel.add(lblNewLabel, BorderLayout.CENTER);

		return Panel;

	}

	// endregion

	// region de paneles y clases para crear preguntas.

	/**
	 * Este panel se encarga consultar que tipo de examen desea crear.
	 * 
	 * @return retorna el panel generado.
	 */
	private JPanel panelCrearPreguntas() {

		JPanel Panel = new JPanel(new BorderLayout()) {

			private static final long serialVersionUID = -234193172935328207L;

			@Override
			protected void paintComponent(Graphics g) {
				BufferedImage img = ImagenesAleatorias.getImagen();
				if (img != null) {
					super.paintComponent(g);
					g.drawImage(img, 0, 0, 1000, 700, 0, 0, img.getWidth(), img.getHeight(), this);
				} else {
					super.paintComponent(g);
					g.setColor(Color.decode("#C9C9C9"));
				}
			}
		};
		Panel.setBackground(Color.decode("#C9C9C9"));
		Panel.setBorder(new EmptyBorder(new Insets(170, 200, 170, 200)));

		JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder(new EmptyBorder(new Insets(20, 20, 20, 20)));
		Panel.add(panel, BorderLayout.CENTER);

		JLabel lblPreguntas = new JLabel("¿Examen de que tipo desea crear?");
		lblPreguntas.setHorizontalAlignment(SwingConstants.CENTER);
		lblPreguntas.setFont(new Font("Segoe UI", Font.PLAIN, 25));
		panel.add(lblPreguntas, BorderLayout.PAGE_START);

		JPanel panelBotonesPreguntas = new JPanel();
		panelBotonesPreguntas.setLayout(new BoxLayout(panelBotonesPreguntas, BoxLayout.Y_AXIS));
		panel.add(panelBotonesPreguntas, BorderLayout.CENTER);

		// examen tipo smr
		JPanel panelBotones1 = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
		panelBotones1.setBorder(new EmptyBorder(10, 5, 5, 5));
		panelBotonesPreguntas.add(panelBotones1);

		JButton botonExamenTipoSMRPanelCrearPreguntas = new JButton("Examen tipo SMR");
		botonExamenTipoSMRPanelCrearPreguntas.setFont(new Font("Segoe UI", Font.PLAIN, 20));
		botonExamenTipoSMRPanelCrearPreguntas.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				tabActivado(false);
				TIPOEXAMEN = "SMR";
				PANELACTUAL = CREARPREGUNTAS;
				actualizarPanelTab(panelCrearPreguntasTiposPreguntas(), 2);
			}
		});
		panelBotones1.add(botonExamenTipoSMRPanelCrearPreguntas);

		// examen tipo pdf/word
		JPanel panelBotones2 = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
		panelBotones2.setBorder(new EmptyBorder(10, 5, 5, 5));
		panelBotonesPreguntas.add(panelBotones2);

		JButton botonExamenTipoPDFWORDPanelCrearPreguntas = new JButton("Examen tipo Pdf/Word");
		botonExamenTipoPDFWORDPanelCrearPreguntas.setFont(new Font("Segoe UI", Font.PLAIN, 20));
		botonExamenTipoPDFWORDPanelCrearPreguntas.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				tabActivado(false);
				TIPOEXAMEN = "PDFWORD";
				PANELACTUAL = CREARPREGUNTAS;
				actualizarPanelTab(panelCrearPreguntasTiposPreguntas(), 2);
			}
		});

		panelBotones2.add(botonExamenTipoPDFWORDPanelCrearPreguntas);

		JPanel panelBotonesInferiores = new JPanel(new BorderLayout());
		panel.add(panelBotonesInferiores, BorderLayout.SOUTH);

		JButton botonCancelarPanelResponderDefecto = new JButton("?");
		botonCancelarPanelResponderDefecto.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		botonCancelarPanelResponderDefecto.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Mensajes.mensaje(
						"<html>El programa le permite crear 2 tipos de exámenes:<br><br>Examen SMR: Crea un archivo, en el cuál puede<br>guardar las preguntas, con sus respuestas y puntajes.<br><br>Examen PDF/Word: Crea un archivo, en donde solo<br>están almacenadas las preguntas y puntajes.<br><br>Para más detalles, revise el archivo 'LEAME.txt'.</html>");

			}
		});
		botonCancelarPanelResponderDefecto.setBackground(Color.decode("#00668C"));
		panelBotonesInferiores.add(botonCancelarPanelResponderDefecto, BorderLayout.WEST);

		return Panel;
	}

	/**
	 * Este panel se encarga de consultar que tipo de pregunta desea crear.
	 * 
	 * @return retorna el panel generado.
	 */
	private JPanel panelCrearPreguntasTiposPreguntas() {

		int cantidadPreguntasAgregadasSM = (examenAlumno != null && examenAlumno.preguntas_SM != null
				? examenAlumno.preguntas_SM.size()
				: 0);
		int cantidadPreguntasAgregadasVF = (examenAlumno != null && examenAlumno.preguntas_VF != null
				? examenAlumno.preguntas_VF.size()
				: 0);
		int cantidadPreguntasAgregadasRC = (examenAlumno != null && examenAlumno.preguntas_RC != null
				? examenAlumno.preguntas_RC.size()
				: 0);
		int cantidadPreguntasAgregadasRM = (examenAlumno != null && examenAlumno.preguntas_RM != null
				? examenAlumno.preguntas_RM.size()
				: 0);

		JPanel Panel = new JPanel(new BorderLayout()) {

			private static final long serialVersionUID = 4398396474721737555L;

			@Override
			protected void paintComponent(Graphics g) {
				BufferedImage img = ImagenesAleatorias.getImagen();
				if (img != null) {
					super.paintComponent(g);
					g.drawImage(img, 0, 0, 1000, 700, 0, 0, img.getWidth(), img.getHeight(), this);
				} else {
					super.paintComponent(g);
					g.setColor(Color.decode("#C9C9C9"));
				}
			}
		};

		Panel.setBackground(Color.decode("#C9C9C9"));
		Panel.setBorder(new EmptyBorder(new Insets(100, 130, 100, 130)));

		JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder(new EmptyBorder(new Insets(40, 20, 30, 20)));
		Panel.add(panel, BorderLayout.CENTER);

		JLabel lblPreguntas = new JLabel("¿Preguntas de que tipo desea añadir?");
		lblPreguntas.setHorizontalAlignment(SwingConstants.CENTER);
		lblPreguntas.setFont(new Font("Segoe UI", Font.PLAIN, 25));
		panel.add(lblPreguntas, BorderLayout.PAGE_START);

		JPanel panelBotonesPreguntas = new JPanel();
		panelBotonesPreguntas.setLayout(new BoxLayout(panelBotonesPreguntas, BoxLayout.Y_AXIS));
		panel.add(panelBotonesPreguntas, BorderLayout.CENTER);

		// seleccion multiple
		JPanel panelBotones1 = new JPanel();
		panelBotones1.setBorder(new EmptyBorder(5, 5, 5, 5));
		panelBotonesPreguntas.add(panelBotones1);

		JButton botonSeleccionMultiplePanelCrearPreguntas = new JButton("Selección multiple");
		botonSeleccionMultiplePanelCrearPreguntas.setFont(new Font("Segoe UI", Font.PLAIN, 20));
		botonSeleccionMultiplePanelCrearPreguntas.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				TIPOPREGUNTA = SELECCIONMULTIPLE;
				actualizarPanelTab(panelCrearPreguntaSeleccionMultiple(), 2);
			}
		});
		panelBotones1.add(botonSeleccionMultiplePanelCrearPreguntas);

		JLabel lblCantidadPreguntasSM = new JLabel("Cantidad de preguntas añadidas: " + cantidadPreguntasAgregadasSM);
		lblCantidadPreguntasSM.setHorizontalAlignment(SwingConstants.LEFT);
		panelBotones1.add(lblCantidadPreguntasSM);

		JButton botonEditarPreguntaSeleccionMultiplePanelCrearPreguntas = new JButton("Editar/borrar");
		botonEditarPreguntaSeleccionMultiplePanelCrearPreguntas.setFont(new Font("Segoe UI", Font.PLAIN, 20));
		botonEditarPreguntaSeleccionMultiplePanelCrearPreguntas.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				TIPOPREGUNTA = SELECCIONMULTIPLE;
				TABACTUAL = 2;
				actualizarPanelTab(panelEditarPreguntasEleccionPreguntas(), 2);
			}
		});
		if (cantidadPreguntasAgregadasSM < 1) {
			botonEditarPreguntaSeleccionMultiplePanelCrearPreguntas.setEnabled(false);
		}
		panelBotones1.add(botonEditarPreguntaSeleccionMultiplePanelCrearPreguntas);

		// verdadero falso
		JPanel panelBotones2 = new JPanel();
		panelBotones2.setBorder(new EmptyBorder(5, 5, 5, 5));
		panelBotonesPreguntas.add(panelBotones2);

		JButton botonVerdaderoFalsoPanelCrearPreguntas = new JButton("Verdadero / falso");
		botonVerdaderoFalsoPanelCrearPreguntas.setFont(new Font("Segoe UI", Font.PLAIN, 20));
		botonVerdaderoFalsoPanelCrearPreguntas.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				TIPOPREGUNTA = VERDADEROFALSO;
				actualizarPanelTab(panelCrearPreguntaVerdaderoFalso(), 2);
			}
		});
		panelBotones2.add(botonVerdaderoFalsoPanelCrearPreguntas);

		JLabel lblCantidadPreguntasVF = new JLabel("Cantidad de preguntas añadidas: " + cantidadPreguntasAgregadasVF);
		lblCantidadPreguntasVF.setHorizontalAlignment(JLabel.LEFT);
		panelBotones2.add(lblCantidadPreguntasVF);

		JButton botonEditarPreguntaVerdaderoFalsoPanelCrearPreguntas = new JButton("Editar/borrar");
		botonEditarPreguntaVerdaderoFalsoPanelCrearPreguntas.setFont(new Font("Segoe UI", Font.PLAIN, 20));
		botonEditarPreguntaVerdaderoFalsoPanelCrearPreguntas.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				TIPOPREGUNTA = VERDADEROFALSO;
				TABACTUAL = 2;
				actualizarPanelTab(panelEditarPreguntasEleccionPreguntas(), 2);
			}
		});
		if (cantidadPreguntasAgregadasVF < 1) {
			botonEditarPreguntaVerdaderoFalsoPanelCrearPreguntas.setEnabled(false);
		}
		panelBotones2.add(botonEditarPreguntaVerdaderoFalsoPanelCrearPreguntas);

		// respuestas cortas
		JPanel panelBotones3 = new JPanel();
		panelBotones3.setBorder(new EmptyBorder(5, 5, 5, 5));
		panelBotonesPreguntas.add(panelBotones3);

		JButton botonRespuestasCortasPanelCrearPreguntas = new JButton("Respuestas cortas");
		botonRespuestasCortasPanelCrearPreguntas.setFont(new Font("Segoe UI", Font.PLAIN, 20));
		botonRespuestasCortasPanelCrearPreguntas.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				TIPOPREGUNTA = RESPUESTACORTA;
				actualizarPanelTab(panelCrearPreguntaRespuestasCortas(), 2);
			}
		});
		panelBotones3.add(botonRespuestasCortasPanelCrearPreguntas);

		JLabel lblCantidadPreguntasRC = new JLabel("Cantidad de preguntas añadidas: " + cantidadPreguntasAgregadasRC);
		lblCantidadPreguntasRC.setHorizontalAlignment(JLabel.LEFT);
		panelBotones3.add(lblCantidadPreguntasRC);

		JButton botonEditarPreguntaRespuestasCortasPanelCrearPreguntas = new JButton("Editar/borrar");
		botonEditarPreguntaRespuestasCortasPanelCrearPreguntas.setFont(new Font("Segoe UI", Font.PLAIN, 20));
		botonEditarPreguntaRespuestasCortasPanelCrearPreguntas.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				TIPOPREGUNTA = RESPUESTACORTA;
				TABACTUAL = 2;
				actualizarPanelTab(panelEditarPreguntasEleccionPreguntas(), 2);
			}
		});
		if (cantidadPreguntasAgregadasRC < 1) {
			botonEditarPreguntaRespuestasCortasPanelCrearPreguntas.setEnabled(false);
		}
		panelBotones3.add(botonEditarPreguntaRespuestasCortasPanelCrearPreguntas);

		// respuestas numerica
		JPanel panelBotones4 = new JPanel();
		panelBotones4.setBorder(new EmptyBorder(5, 5, 5, 5));
		panelBotonesPreguntas.add(panelBotones4);

		JButton botonRespuestasNumericasPanelCrearPreguntas = new JButton("Respuesta numerica");
		botonRespuestasNumericasPanelCrearPreguntas.setFont(new Font("Segoe UI", Font.PLAIN, 20));
		botonRespuestasNumericasPanelCrearPreguntas.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				TIPOPREGUNTA = RESPUESTANUMERICA;
				actualizarPanelTab(panelCrearPreguntaRespuestaNumerica(), 2);
			}
		});
		panelBotones4.add(botonRespuestasNumericasPanelCrearPreguntas);

		JLabel lblCantidadPreguntasRM = new JLabel("Cantidad de preguntas añadidas: " + cantidadPreguntasAgregadasRM);
		lblCantidadPreguntasRM.setHorizontalAlignment(JLabel.LEFT);
		panelBotones4.add(lblCantidadPreguntasRM);

		JButton botonEditarPreguntaRespuestasNumericasPanelCrearPreguntas = new JButton("Editar/borrar");
		botonEditarPreguntaRespuestasNumericasPanelCrearPreguntas.setFont(new Font("Segoe UI", Font.PLAIN, 20));
		botonEditarPreguntaRespuestasNumericasPanelCrearPreguntas.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				TIPOPREGUNTA = RESPUESTANUMERICA;
				TABACTUAL = 2;
				actualizarPanelTab(panelEditarPreguntasEleccionPreguntas(), 2);
			}
		});
		if (cantidadPreguntasAgregadasRM < 1) {
			botonEditarPreguntaRespuestasNumericasPanelCrearPreguntas.setEnabled(false);
		}
		panelBotones4.add(botonEditarPreguntaRespuestasNumericasPanelCrearPreguntas);

		// panel botones inferiores

		JPanel panelBotonesInferiores = new JPanel(new BorderLayout());
		panel.add(panelBotonesInferiores, BorderLayout.SOUTH);

		if ((cantidadPreguntasAgregadasSM > 0 || cantidadPreguntasAgregadasVF > 0 || cantidadPreguntasAgregadasRC > 0)
				|| cantidadPreguntasAgregadasRM > 0) {
			JButton botonFinalizarPanelCrearPreguntas = new JButton("Finalizar");
			botonFinalizarPanelCrearPreguntas.setFont(new Font("Segoe UI", Font.PLAIN, 18));
			botonFinalizarPanelCrearPreguntas.addActionListener(new botonFinalizarPanelCrearPreguntas_Listener());
			botonFinalizarPanelCrearPreguntas.setBackground(Color.decode("#0077ff"));
			panelBotonesInferiores.add(botonFinalizarPanelCrearPreguntas, BorderLayout.EAST);
		}

		JButton botonCancelarPanelResponderDefecto = new JButton("Cancelar");
		botonCancelarPanelResponderDefecto.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		botonCancelarPanelResponderDefecto.addActionListener(new botonCancelarPanelCrearPreguntas_Listener());
		botonCancelarPanelResponderDefecto.setBackground(Color.decode("#00668C"));
		panelBotonesInferiores.add(botonCancelarPanelResponderDefecto, BorderLayout.WEST);

		return Panel;
	}

	/**
	 * Esta clase, es el actionlistener del boton cancelar, del panel de crear
	 * preguntas.
	 */
	private class botonCancelarPanelCrearPreguntas_Listener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (PREGUNTACREADA) {
				if (JOptionPane.showConfirmDialog(null, "¿Estas seguro de volver?, tienes preguntas sin guardar.",
						"AVISO", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
					examenAlumno = null;
					PREGUNTACREADA = false;
					tabActivado(true);
					actualizarPanelTab(panelCrearPreguntas(), 2);
				} else {
					return;
				}
			} else {
				tabActivado(true);
				examenAlumno = null;
				PREGUNTACREADA = false;
				actualizarPanelTab(panelCrearPreguntas(), 2);
			}
		}
	}

	/**
	 * Esta clase, es el actionlistener del boton cancelar, del panel de finalizar
	 * preguntas.
	 */
	private class botonFinalizarPanelCrearPreguntas_Listener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			PANELACTUAL = CREARPREGUNTAS;
			if (TIPOEXAMEN.equals("SMR")) {
				actualizarPanelTab(panelOpcionesGuardarExamenEnArchivoSMR(), 2);
			} else if (TIPOEXAMEN.equals("PDFWORD")) {
				actualizarPanelTab(panelOpcionesGuardarExamenEnArchivoWORDPDF(), 2);
			}

		}
	}

	/**
	 * Este panel se encarga de crear preguntas de tipo seleccion multiple.
	 * 
	 * @return retorna el panel generado.
	 */
	private JPanel panelCrearPreguntaSeleccionMultiple() {

		JPanel Panel = new JPanel(new BorderLayout()) {

			private static final long serialVersionUID = 2401696606905413201L;

			@Override
			protected void paintComponent(Graphics g) {
				BufferedImage img = ImagenesAleatorias.getImagen();
				if (img != null) {
					super.paintComponent(g);
					g.drawImage(img, 0, 0, 1000, 700, 0, 0, img.getWidth(), img.getHeight(), this);
				} else {
					super.paintComponent(g);
					g.setColor(Color.decode("#C9C9C9"));
				}
			}
		};
		Panel.setBackground(Color.decode("#C9C9C9"));
		Panel.setBorder(new EmptyBorder(new Insets(30, 150, 30, 150)));

		JPanel panel = new JPanel(new BorderLayout(0, 5));
		panel.setBorder(new EmptyBorder(new Insets(10, 20, 11, 20)));
		Panel.add(panel, BorderLayout.CENTER);

		// panel central

		// panel del centro

		JPanel panelCentral = new JPanel();
		panelCentral.setLayout(new BoxLayout(panelCentral, BoxLayout.Y_AXIS));
		panel.add(panelCentral, BorderLayout.CENTER);

		// panel central superior

		JPanel panelCentralSuperior = new JPanel(new BorderLayout());
		panelCentral.add(panelCentralSuperior);

		JLabel ingresePregunta = new JLabel("Ingrese la pregunta en el siguiente cuadro:");
		ingresePregunta.setFont(new Font("Segoe UI", Font.PLAIN, 25));
		panelCentralSuperior.add(ingresePregunta, BorderLayout.NORTH);

		JScrollPane scrollPane = new JScrollPane();
		panelCentralSuperior.add(scrollPane, BorderLayout.CENTER);

		pregunta = new JTextArea(7, 30);
		pregunta.setLineWrap(true);
		pregunta.setWrapStyleWord(true);
		pregunta.setEditable(true);
		scrollPane.setViewportView(pregunta);

		panelCentral.add(Box.createRigidArea(new Dimension(0, 10)));

		// panel central inferior

		JPanel panelCentralCentral = new JPanel(new BorderLayout());
		panelCentral.add(panelCentralCentral);

		JLabel ingreseOpciones = new JLabel("Ingrese las opciones en el siguiente cuadro:");
		ingreseOpciones.setFont(new Font("Segoe UI", Font.PLAIN, 25));
		panelCentralCentral.add(ingreseOpciones, BorderLayout.NORTH);

		JScrollPane scrllBotones = new JScrollPane();
		scrllBotones.setPreferredSize(
				new Dimension(panelCentral.getPreferredSize().width, panelCentral.getPreferredSize().height / 2 + 15));
		panelCentralCentral.add(scrllBotones, BorderLayout.CENTER);

		JPanel panelRdBotones = new JPanel();
		panelRdBotones.setLayout(new BoxLayout(panelRdBotones, BoxLayout.Y_AXIS));
		scrllBotones.setViewportView(panelRdBotones);

		rdBotones = new ArrayList<crearCheckTextRdBoton>();

		group = new ButtonGroup();

		for (int i = 0; i < 3; i++) {
			rdBotones.add(new crearCheckTextRdBoton(""));
			group.add(rdBotones.get(i).boton);
			panelRdBotones.add(rdBotones.get(i));
		}

		JPanel panelAgregarEliminarOpciones = new JPanel(new BorderLayout());
		panelCentralCentral.add(panelAgregarEliminarOpciones, BorderLayout.SOUTH);

		JButton Agregar = new JButton("+");
		Agregar.setToolTipText("Agregar opciones");
		Agregar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent evt) {
				rdBotones.add(new crearCheckTextRdBoton(""));
				panelRdBotones.removeAll();
				for (crearCheckTextRdBoton boton : rdBotones) {
					panelRdBotones.add(boton);
					group.add(boton.boton);
				}
				panelRdBotones.updateUI();
			}
		});
		panelAgregarEliminarOpciones.add(Agregar, BorderLayout.WEST);

		JPanel panelSeleccionarTodos = new JPanel(new FlowLayout(FlowLayout.CENTER));
		panelAgregarEliminarOpciones.add(panelSeleccionarTodos, BorderLayout.CENTER);

		JButton btnSeleccionarTodos = new JButton("Seleccionar Todos");
		btnSeleccionarTodos.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				if (btnSeleccionarTodos.getText().equals("Deseleccionar todos")) {
					btnSeleccionarTodos.setText("Seleccionar Todos");
					for (crearCheckTextRdBoton boton : rdBotones) {
						boton.check.setSelected(false);
					}
				} else {
					btnSeleccionarTodos.setText("Deseleccionar todos");
					for (crearCheckTextRdBoton boton : rdBotones) {
						boton.check.setSelected(true);
					}
				}
				// panelRdBotones.updateUI();
				// panelRdBotones.revalidate();
				// panelRdBotones.repaint();
				repaint();
			}
		});
		panelSeleccionarTodos.add(btnSeleccionarTodos);

		JButton Eliminar = new JButton("-");
		Eliminar.setToolTipText("Eliminar opciones seleccionadas");
		Eliminar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				ArrayList<crearCheckTextRdBoton> auxiliar = new ArrayList<crearCheckTextRdBoton>();

				panelRdBotones.removeAll();
				for (crearCheckTextRdBoton boton : rdBotones) {
					if (!boton.check.isSelected()) {
						auxiliar.add(boton);
						panelRdBotones.add(boton);
					}
				}

				rdBotones = auxiliar;

				if (panelRdBotones.getComponentCount() < 3) {
					for (int i = 0; i < (3 - panelRdBotones.getComponentCount()); ++i) {
						rdBotones.add(new crearCheckTextRdBoton(""));
						group.add(rdBotones.get(i).boton);
					}

					for (crearCheckTextRdBoton boton : rdBotones) {
						panelRdBotones.add(boton);
					}
					btnSeleccionarTodos.setText("Seleccionar Todos");
				}
				panelRdBotones.updateUI();
			}
		});
		panelAgregarEliminarOpciones.add(Eliminar, BorderLayout.EAST);

		panelCentral.add(Box.createRigidArea(new Dimension(0, 10)));

		// panel central inferior

		JPanel panelCentralInferior = new JPanel(new BorderLayout());
		panelCentral.add(panelCentralInferior);

		JLabel ingresePeso = new JLabel("Ingrese el puntaje en el siguiente cuadro:");
		ingresePeso.setFont(new Font("Segoe UI", Font.PLAIN, 25));
		panelCentralInferior.add(ingresePeso, BorderLayout.NORTH);

		txtfieldPesoPregunta = new JTextField(5);
		txtfieldPesoPregunta.setFont(new Font("Segoe UI", Font.PLAIN, 20));
		txtfieldPesoPregunta.setTransferHandler(null);
		txtfieldPesoPregunta.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				int cantidadMaximaCaracteres = 3;

				char letraIngresada = e.getKeyChar();

				if (!((letraIngresada >= '0') && (letraIngresada <= '9') || (letraIngresada == '-')
						|| (letraIngresada == '+') || (letraIngresada == KeyEvent.VK_BACK_SPACE)
						|| (letraIngresada == KeyEvent.VK_DELETE))) {
					txtfieldPesoPregunta.setToolTipText("Por favor ingrese numeros entre -9999 y 9999.");
					e.consume();
				}

				if ((letraIngresada == '-' || letraIngresada == '+') && (txtfieldPesoPregunta.getText().length() > 0)) {
					e.consume();
				}

				if ((txtfieldPesoPregunta.getText().length() > 0) && (txtfieldPesoPregunta.getText().charAt(0) == '-'
						|| txtfieldPesoPregunta.getText().charAt(0) == '+')) {
					cantidadMaximaCaracteres = 4;
				}

				if (txtfieldPesoPregunta.getText().length() > cantidadMaximaCaracteres) {
					e.consume();
				}
			}
		});
		panelCentralInferior.add(txtfieldPesoPregunta, BorderLayout.CENTER);

		panelCentral.add(Box.createRigidArea(new Dimension(0, 10)));

		// panel inferior

		JPanel panelBotonesInferiores = new JPanel(new BorderLayout());
		panel.add(panelBotonesInferiores, BorderLayout.SOUTH);

		JButton botonSiguientePanelCrearPreguntaSeleccionMultiple = new JButton("Siguiente");
		botonSiguientePanelCrearPreguntaSeleccionMultiple.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		botonSiguientePanelCrearPreguntaSeleccionMultiple
				.addActionListener(new botonSiguientePanelCrearPreguntaSeleccionMultiple());
		botonSiguientePanelCrearPreguntaSeleccionMultiple.setBackground(Color.decode("#0077ff"));
		panelBotonesInferiores.add(botonSiguientePanelCrearPreguntaSeleccionMultiple, BorderLayout.EAST);

		JButton botonCancelarPanelCrearPreguntaSeleccionMultiple = new JButton("Cancelar");
		botonCancelarPanelCrearPreguntaSeleccionMultiple.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		botonCancelarPanelCrearPreguntaSeleccionMultiple
				.addActionListener(new botonCancelarPanelCrearPreguntaSeleccionMultiple_Listener());
		botonCancelarPanelCrearPreguntaSeleccionMultiple.setBackground(Color.decode("#00668C"));
		panelBotonesInferiores.add(botonCancelarPanelCrearPreguntaSeleccionMultiple, BorderLayout.WEST);

		return Panel;
	}

	/**
	 * Esta clase, es el actionlistener del boton cancelar, del panel de crear
	 * preguntas de selección multiple.
	 */
	private class botonCancelarPanelCrearPreguntaSeleccionMultiple_Listener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			boolean hayCambios = false;

			if (pregunta != null && !pregunta.getText().isBlank()) {
				hayCambios = true;
			}

			if (!hayCambios) {
				for (int i = 0; i < rdBotones.size(); i++) {
					if (rdBotones.get(i) != null && !rdBotones.get(i).texto.getText().isBlank()) {
						hayCambios = true;
						break;
					}
				}
			}

			if (!hayCambios && txtfieldPesoPregunta.getText() != null && !txtfieldPesoPregunta.getText().isBlank()) {
				hayCambios = true;
			}

			if (hayCambios) {
				if (Mensajes.consultar("Hay cambios sin guardar, ¿esta seguro de volver?", "Confirmación")) {
					actualizarPanelTab(panelCrearPreguntasTiposPreguntas(), 2);
				}
			} else {
				actualizarPanelTab(panelCrearPreguntasTiposPreguntas(), 2);
			}
		}
	}

	/**
	 * Esta clase, es el actionlistener del boton siguiente, del panel de crear
	 * preguntas de tipo selección multiple.
	 */
	private class botonSiguientePanelCrearPreguntaSeleccionMultiple implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			try {

				int respuestaSM = 0;
				if (!PREGUNTACREADA) {
					examenAlumno = new Examen();
				}

				if (pregunta == null || pregunta.getText().isBlank()) {
					Mensajes.error("Por favor ingrese su pregunta.");
					return;
				}

				if (group != null && group.getSelection() != null) {

					// vacio el array, para evitar que se agreguen opciones indeseadas.
					opciones = new String[0];

					List<String> opcionesProfesor = new ArrayList<String>();

					for (int i = 0; i < rdBotones.size(); i++) {
						if (rdBotones.get(i).boton.isSelected() && !rdBotones.get(i).texto.getText().isBlank()) {
							if (!opcionesProfesor.contains(rdBotones.get(i).texto.getText())) {
								opcionesProfesor.add(rdBotones.get(i).texto.getText());
								respuestaSM = opcionesProfesor.indexOf(rdBotones.get(i).texto.getText());
							} else {
								throw new IllegalArgumentException("Por favor, no repita las alternativas.");
							}

						} else if (!rdBotones.get(i).boton.isSelected()
								&& !rdBotones.get(i).texto.getText().isBlank()) {
							if (!opcionesProfesor.contains(rdBotones.get(i).texto.getText())) {
								opcionesProfesor.add(rdBotones.get(i).texto.getText());
							} else {
								throw new IllegalArgumentException("Por favor, no repita las alternativas.");
							}
						} else if (rdBotones.get(i).boton.isSelected() && rdBotones.get(i).texto.getText().isBlank()) {
							throw new IllegalArgumentException(
									"Por favor, no marque una alternativa vacia como correcta.");
						}
					}

					if (opcionesProfesor.isEmpty()) {
						throw new IllegalArgumentException(
								"Por favor, ingrese el texto de una alternativa, y seleccione la respuesta correcta.");
					}

					if (opcionesProfesor.size() < 3) {
						throw new IllegalArgumentException("Por favor, ingrese 3 o mas alternativas.");
					}
					opciones = opcionesProfesor.toArray(new String[0]);

					String textoPeso = txtfieldPesoPregunta.getText();

					if (textoPeso == null || textoPeso.isBlank()) {
						Mensajes.error(
								"Por favor, solo ingresa NUMEROS, el texto u otro tipo de caracteres NO sirven.");
						return;
					}

					try {
						peso = Integer.parseInt(textoPeso);

						String tmp = pregunta.getText();

						examenAlumno.agregaPregunta(new SeleccionMultiple(tmp, opciones, respuestaSM, peso));
						PREGUNTACREADA = true;
						if (Mensajes.consultar("¿Desea seguir agregando preguntas de tipo selección multiple?",
								"Confirmación")) {
							actualizarPanelTab(panelCrearPreguntaSeleccionMultiple(), 2);
						} else {
							actualizarPanelTab(panelCrearPreguntasTiposPreguntas(), 2);
						}

					} catch (Exception e2) {
						Mensajes.error(
								"Por favor, solo ingresa NUMEROS, el texto u otro tipo de caracteres NO sirven.");
						e2.printStackTrace();
					}

				} else {
					Mensajes.error("Por favor agregue y seleccione una alternativa.");
				}
			} catch (Exception e2) {
				Mensajes.error(e2.getMessage());
			}

		}
	}

	/**
	 * Este panel se encarga de crear preguntas de tipo respuestas cortas.
	 * 
	 * @return retorna el panel generado.
	 */
	private JPanel panelCrearPreguntaRespuestasCortas() {

		JPanel Panel = new JPanel(new BorderLayout()) {

			private static final long serialVersionUID = -6573362931456938470L;

			@Override
			protected void paintComponent(Graphics g) {
				BufferedImage img = ImagenesAleatorias.getImagen();
				if (img != null) {
					super.paintComponent(g);
					g.drawImage(img, 0, 0, 1000, 700, 0, 0, img.getWidth(), img.getHeight(), this);
				} else {
					super.paintComponent(g);
					g.setColor(Color.decode("#C9C9C9"));
				}
			}
		};
		Panel.setBackground(Color.decode("#C9C9C9"));
		Panel.setBorder(new EmptyBorder(new Insets(70, 150, 70, 150)));

		JPanel panel = new JPanel(new BorderLayout(0, 5));
		panel.setBorder(new EmptyBorder(new Insets(10, 20, 11, 20)));
		Panel.add(panel, BorderLayout.CENTER);

		// panel central

		// panel del centro

		JPanel panelCentral = new JPanel();
		panelCentral.setLayout(new BoxLayout(panelCentral, BoxLayout.Y_AXIS));
		panel.add(panelCentral, BorderLayout.CENTER);

		// panel central superior

		JPanel panelCentralSuperior = new JPanel(new BorderLayout());
		panelCentral.add(panelCentralSuperior);

		JLabel ingresePregunta = new JLabel("Ingrese la pregunta en el siguiente cuadro:");
		ingresePregunta.setFont(new Font("Segoe UI", Font.PLAIN, 25));
		panelCentralSuperior.add(ingresePregunta, BorderLayout.NORTH);

		JScrollPane scrollPane = new JScrollPane();
		panelCentralSuperior.add(scrollPane, BorderLayout.CENTER);

		pregunta = new JTextArea(7, 30);
		pregunta.setLineWrap(true);
		pregunta.setWrapStyleWord(true);
		pregunta.setEditable(true);
		scrollPane.setViewportView(pregunta);

		panelCentral.add(Box.createRigidArea(new Dimension(0, 10)));

		// panel central inferior

		JPanel panelCentralCentral = new JPanel(new BorderLayout());
		panelCentral.add(panelCentralCentral);

		JLabel ingreseRespuesta = new JLabel("Ingrese la respuesta en el siguiente cuadro:");
		ingreseRespuesta.setFont(new Font("Segoe UI", Font.PLAIN, 25));
		panelCentralCentral.add(ingreseRespuesta, BorderLayout.NORTH);

		txtPregunta = new JTextField(10);
		txtPregunta.setFont(new Font("Segoe UI", Font.PLAIN, 20));
		panelCentral.add(txtPregunta);

		panelCentral.add(Box.createRigidArea(new Dimension(0, 10)));

		// panel central inferior

		JPanel panelCentralInferior = new JPanel(new BorderLayout());
		panelCentral.add(panelCentralInferior);

		JLabel ingresePeso = new JLabel("Ingrese el puntaje en el siguiente cuadro:");
		ingresePeso.setFont(new Font("Segoe UI", Font.PLAIN, 25));
		panelCentralInferior.add(ingresePeso, BorderLayout.NORTH);

		txtfieldPesoPregunta = new JTextField(5);
		txtfieldPesoPregunta.setFont(new Font("Segoe UI", Font.PLAIN, 20));
		txtfieldPesoPregunta.setTransferHandler(null);
		txtfieldPesoPregunta.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				int cantidadMaximaCaracteres = 3;

				char letraIngresada = e.getKeyChar();

				if (!((letraIngresada >= '0') && (letraIngresada <= '9') || (letraIngresada == '-')
						|| (letraIngresada == '+') || (letraIngresada == KeyEvent.VK_BACK_SPACE)
						|| (letraIngresada == KeyEvent.VK_DELETE))) {
					txtfieldPesoPregunta.setToolTipText("Por favor ingrese numeros entre -9999 y 9999.");
					e.consume();
				}

				if ((letraIngresada == '-' || letraIngresada == '+') && (txtfieldPesoPregunta.getText().length() > 0)) {
					e.consume();
				}

				if ((txtfieldPesoPregunta.getText().length() > 0) && (txtfieldPesoPregunta.getText().charAt(0) == '-'
						|| txtfieldPesoPregunta.getText().charAt(0) == '+')) {
					cantidadMaximaCaracteres = 4;
				}

				if (txtfieldPesoPregunta.getText().length() > cantidadMaximaCaracteres) {
					e.consume();
				}
			}
		});
		panelCentralInferior.add(txtfieldPesoPregunta, BorderLayout.CENTER);

		panelCentral.add(Box.createRigidArea(new Dimension(0, 10)));
		// panel inferior

		JPanel panelBotonesInferiores = new JPanel(new BorderLayout());
		panel.add(panelBotonesInferiores, BorderLayout.SOUTH);

		JButton botonSiguientePanelCrearPreguntaRespuestasCortas = new JButton("Siguiente");
		botonSiguientePanelCrearPreguntaRespuestasCortas.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		botonSiguientePanelCrearPreguntaRespuestasCortas
				.addActionListener(new botonSiguientePanelCrearPreguntaRespuestasCortas_Listener());
		botonSiguientePanelCrearPreguntaRespuestasCortas.setBackground(Color.decode("#0077ff"));
		panelBotonesInferiores.add(botonSiguientePanelCrearPreguntaRespuestasCortas, BorderLayout.EAST);

		JButton botonCancelarPanelCrearPreguntaRespuestasCortas = new JButton("Cancelar");
		botonCancelarPanelCrearPreguntaRespuestasCortas.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		botonCancelarPanelCrearPreguntaRespuestasCortas
				.addActionListener(new botonCancelarPanelCrearPreguntaRespuestasCortas_Listener());
		botonCancelarPanelCrearPreguntaRespuestasCortas.setBackground(Color.decode("#00668C"));
		panelBotonesInferiores.add(botonCancelarPanelCrearPreguntaRespuestasCortas, BorderLayout.WEST);

		return Panel;
	}

	/**
	 * Esta clase, es el actionlistener del boton cancelar, del panel de crear
	 * preguntas de tipo respuestas cortas.
	 */
	private class botonCancelarPanelCrearPreguntaRespuestasCortas_Listener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			boolean hayCambios = false;

			if (pregunta != null && !pregunta.getText().isBlank()) {
				hayCambios = true;
			}

			if (!hayCambios && txtPregunta != null && !txtPregunta.getText().isBlank()) {
				hayCambios = true;
			}

			if (!hayCambios && txtfieldPesoPregunta.getText() != null && !txtfieldPesoPregunta.getText().isBlank()) {
				hayCambios = true;
			}

			if (hayCambios) {
				if (Mensajes.consultar("Hay cambios sin guardar, ¿esta seguro de volver?", "Confirmación")) {
					actualizarPanelTab(panelCrearPreguntasTiposPreguntas(), 2);
				}
			} else {
				actualizarPanelTab(panelCrearPreguntasTiposPreguntas(), 2);
			}
		}
	}

	/**
	 * Esta clase, es el actionlistener del boton siguiente, del panel de crear
	 * preguntas de tipo respuestas cortas.
	 */
	private class botonSiguientePanelCrearPreguntaRespuestasCortas_Listener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			try {

				if (!PREGUNTACREADA) {
					examenAlumno = new Examen();
				}

				if (pregunta == null || pregunta.getText().isBlank()) {
					Mensajes.error("Por favor ingrese su pregunta.");
					return;
				}

				String respuestaRC = null;

				if (txtPregunta != null && !txtPregunta.getText().isBlank()) {
					respuestaRC = txtPregunta.getText();
				} else {
					Mensajes.error("Por favor ingrese la respuesta de su pregunta.");
					return;
				}

				String textoPeso = txtfieldPesoPregunta.getText();

				if (textoPeso == null || textoPeso.isBlank()) {
					Mensajes.error("Por favor, solo ingresa NUMEROS, el texto u otro tipo de caracteres NO sirven.");
					return;
				}

				try {
					peso = Integer.parseInt(textoPeso);

					String tmp = pregunta.getText();

					examenAlumno.agregaPregunta(new RespuestasCortas(tmp, respuestaRC, peso));
					PREGUNTACREADA = true;
					if (Mensajes.consultar("¿Desea seguir agregando preguntas de tipo respuestas cortas?",
							"Confirmación")) {
						actualizarPanelTab(panelCrearPreguntaRespuestasCortas(), 2);
					} else {
						actualizarPanelTab(panelCrearPreguntasTiposPreguntas(), 2);
					}

				} catch (Exception e2) {
					Mensajes.error("Por favor, solo ingresa NUMEROS, el texto u otro tipo de caracteres NO sirven.");
					e2.printStackTrace();
				}
			} catch (Exception e2) {
				Mensajes.error(
						"<html>¡Ha ocurrido un error critico, por favor contacte con<br>el creador del programa para arreglar el problema!</html>");
			}
		}
	}

	/**
	 * Este panel se encarga de crear preguntas de tipo verdadero falso.
	 * 
	 * @return retorna el panel generado.
	 */
	private JPanel panelCrearPreguntaVerdaderoFalso() {

		JPanel Panel = new JPanel(new BorderLayout()) {

			private static final long serialVersionUID = 297795924102183L;

			@Override
			protected void paintComponent(Graphics g) {
				BufferedImage img = ImagenesAleatorias.getImagen();
				if (img != null) {
					super.paintComponent(g);
					g.drawImage(img, 0, 0, 1000, 700, 0, 0, img.getWidth(), img.getHeight(), this);
				} else {
					super.paintComponent(g);
					g.setColor(Color.decode("#C9C9C9"));
				}
			}
		};
		Panel.setBackground(Color.decode("#C9C9C9"));
		Panel.setBorder(new EmptyBorder(new Insets(70, 150, 70, 150)));

		JPanel panel = new JPanel(new BorderLayout(0, 5));
		panel.setBorder(new EmptyBorder(new Insets(10, 20, 11, 20)));
		Panel.add(panel, BorderLayout.CENTER);

		// panel central

		// panel del centro

		JPanel panelCentral = new JPanel();
		panelCentral.setLayout(new BoxLayout(panelCentral, BoxLayout.Y_AXIS));
		panel.add(panelCentral, BorderLayout.CENTER);

		// panel central superior

		JPanel panelCentralSuperior = new JPanel(new BorderLayout());
		panelCentral.add(panelCentralSuperior);

		JLabel ingresePregunta = new JLabel("Ingrese la pregunta en el siguiente cuadro:");
		ingresePregunta.setFont(new Font("Segoe UI", Font.PLAIN, 25));
		panelCentralSuperior.add(ingresePregunta, BorderLayout.NORTH);

		JScrollPane scrollPane = new JScrollPane();
		panelCentralSuperior.add(scrollPane, BorderLayout.CENTER);

		pregunta = new JTextArea(7, 30);
		pregunta.setLineWrap(true);
		pregunta.setWrapStyleWord(true);
		pregunta.setEditable(true);
		scrollPane.setViewportView(pregunta);

		panelCentral.add(Box.createRigidArea(new Dimension(0, 10)));

		// panel central superior

		JPanel panelCentralCentral = new JPanel(new BorderLayout());
		panelCentral.add(panelCentralCentral);

		JLabel seleccioneRespuesta = new JLabel("Seleccione la respuesta:");
		seleccioneRespuesta.setHorizontalAlignment(JLabel.CENTER);
		seleccioneRespuesta.setFont(new Font("Segoe UI", Font.PLAIN, 25));
		panelCentralCentral.add(seleccioneRespuesta, BorderLayout.NORTH);

		group = new ButtonGroup();

		JPanel panelRespuestaVerdaderoFalso = new JPanel();
		panelCentralCentral.add(panelRespuestaVerdaderoFalso, BorderLayout.SOUTH);

		JRadioButton rdbtnVerdadero = new JRadioButton("Verdadero");
		rdbtnVerdadero.setFont(new Font("Segoe UI", Font.PLAIN, 20));
		rdbtnVerdadero.setActionCommand("Verdadero");
		group.add(rdbtnVerdadero);
		panelRespuestaVerdaderoFalso.add(rdbtnVerdadero);

		JRadioButton rdbtnFalso = new JRadioButton("Falso");
		rdbtnFalso.setFont(new Font("Segoe UI", Font.PLAIN, 20));
		rdbtnFalso.setActionCommand("Falso");
		group.add(rdbtnFalso);
		panelRespuestaVerdaderoFalso.add(rdbtnFalso);

		panelCentral.add(Box.createRigidArea(new Dimension(0, 10)));

		// panel central inferior

		JPanel panelCentralInferior = new JPanel(new BorderLayout());
		panelCentral.add(panelCentralInferior);

		JLabel ingresePeso = new JLabel("Ingrese el puntaje en el siguiente cuadro:");
		ingresePeso.setHorizontalAlignment(JLabel.CENTER);
		ingresePeso.setFont(new Font("Segoe UI", Font.PLAIN, 25));
		panelCentralInferior.add(ingresePeso, BorderLayout.NORTH);

		txtfieldPesoPregunta = new JTextField(5);
		txtfieldPesoPregunta.setFont(new Font("Segoe UI", Font.PLAIN, 20));
		txtfieldPesoPregunta.setTransferHandler(null);
		txtfieldPesoPregunta.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				int cantidadMaximaCaracteres = 3;

				char letraIngresada = e.getKeyChar();

				if (!((letraIngresada >= '0') && (letraIngresada <= '9') || (letraIngresada == '-')
						|| (letraIngresada == '+') || (letraIngresada == KeyEvent.VK_BACK_SPACE)
						|| (letraIngresada == KeyEvent.VK_DELETE))) {
					txtfieldPesoPregunta.setToolTipText("Por favor ingrese numeros entre -9999 y 9999.");
					e.consume();
				}

				if ((letraIngresada == '-' || letraIngresada == '+') && (txtfieldPesoPregunta.getText().length() > 0)) {
					e.consume();
				}

				if ((txtfieldPesoPregunta.getText().length() > 0) && (txtfieldPesoPregunta.getText().charAt(0) == '-'
						|| txtfieldPesoPregunta.getText().charAt(0) == '+')) {
					cantidadMaximaCaracteres = 4;
				}

				if (txtfieldPesoPregunta.getText().length() > cantidadMaximaCaracteres) {
					e.consume();
				}
			}
		});
		panelCentralInferior.add(txtfieldPesoPregunta, BorderLayout.CENTER);

		panelCentral.add(Box.createRigidArea(new Dimension(0, 10)));

		// panel inferior

		JPanel panelBotonesInferiores = new JPanel(new BorderLayout());
		panel.add(panelBotonesInferiores, BorderLayout.SOUTH);

		JButton botonSiguientePanelCrearPreguntaVerdaderoFalso = new JButton("Siguiente");
		botonSiguientePanelCrearPreguntaVerdaderoFalso.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		botonSiguientePanelCrearPreguntaVerdaderoFalso
				.addActionListener(new botonSiguientePanelCrearPreguntaVerdaderoFalso_Listener());
		botonSiguientePanelCrearPreguntaVerdaderoFalso.setBackground(Color.decode("#0077ff"));
		panelBotonesInferiores.add(botonSiguientePanelCrearPreguntaVerdaderoFalso, BorderLayout.EAST);

		JButton botonCancelarPanelCrearPreguntaVerdaderoFalso = new JButton("Cancelar");
		botonCancelarPanelCrearPreguntaVerdaderoFalso.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		botonCancelarPanelCrearPreguntaVerdaderoFalso
				.addActionListener(new botonCancelarPanelCrearPreguntaVerdaderoFalso_Listener());
		botonCancelarPanelCrearPreguntaVerdaderoFalso.setBackground(Color.decode("#00668C"));
		panelBotonesInferiores.add(botonCancelarPanelCrearPreguntaVerdaderoFalso, BorderLayout.WEST);

		return Panel;
	}

	/**
	 * Esta clase, es el actionlistener del boton cancelar, del panel de crear
	 * preguntas de verdadero falso.
	 */
	private class botonCancelarPanelCrearPreguntaVerdaderoFalso_Listener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			boolean hayCambios = false;

			if (pregunta != null && !pregunta.getText().isBlank()) {
				hayCambios = true;
			}

			if (!hayCambios) {
				for (int i = 0; i < rdBotones.size(); i++) {
					if (rdBotones.get(i) != null && !rdBotones.get(i).texto.getText().isBlank()) {
						hayCambios = true;
						break;
					}
				}
			}

			if (!hayCambios && txtfieldPesoPregunta.getText() != null && !txtfieldPesoPregunta.getText().isBlank()) {
				hayCambios = true;
			}

			if (group != null && group.getSelection() != null) {
				hayCambios = true;
			}

			if (hayCambios) {
				if (Mensajes.consultar("Hay cambios sin guardar, ¿esta seguro de volver?", "Confirmación")) {
					actualizarPanelTab(panelCrearPreguntasTiposPreguntas(), 2);
				}
			} else {
				actualizarPanelTab(panelCrearPreguntasTiposPreguntas(), 2);
			}
		}
	}

	/**
	 * Esta clase, es el actionlistener del boton siguiente, del panel de crear
	 * preguntas de tipo verdadero falso.
	 */
	private class botonSiguientePanelCrearPreguntaVerdaderoFalso_Listener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			try {

				if (!PREGUNTACREADA) {
					examenAlumno = new Examen();
				}

				if (pregunta == null || pregunta.getText().isBlank()) {
					Mensajes.error("Por favor ingrese su pregunta.");
					return;
				}

				if (group != null && group.getSelection() != null) {

					boolean respuestaVF = (group.getSelection().getActionCommand().equals("Verdadero") ? true : false);

					String textoPeso = txtfieldPesoPregunta.getText();

					if (textoPeso == null || textoPeso.isBlank()) {
						Mensajes.error(
								"Por favor, solo ingresa NUMEROS, el texto u otro tipo de caracteres NO sirven.");
						return;
					}

					try {
						peso = Integer.parseInt(textoPeso);

						String tmp = pregunta.getText();

						examenAlumno.agregaPregunta(new VerdaderoFalso(tmp, respuestaVF, peso));
						PREGUNTACREADA = true;
						if (Mensajes.consultar("¿Desea seguir agregando preguntas de tipo verdadero falso?",
								"Confirmación")) {
							actualizarPanelTab(panelCrearPreguntaVerdaderoFalso(), 2);
						} else {
							actualizarPanelTab(panelCrearPreguntasTiposPreguntas(), 2);
						}

					} catch (Exception e2) {
						Mensajes.error(
								"Por favor, solo ingresa NUMEROS, el texto u otro tipo de caracteres NO sirven.");
						e2.printStackTrace();
					}

				} else {
					Mensajes.error("Por favor, marque alguna alternativa como correcta.");
				}
			} catch (Exception e2) {
				Mensajes.error(e2.getMessage());
			}

		}
	}

	/**
	 * Este panel se encarga de crear preguntas de tipo respuesta numerica.
	 * 
	 * @return retorna el panel generado.
	 */
	private JPanel panelCrearPreguntaRespuestaNumerica() {

		JPanel Panel = new JPanel(new BorderLayout()) {

			private static final long serialVersionUID = -8308752555859698587L;

			@Override
			protected void paintComponent(Graphics g) {
				BufferedImage img = ImagenesAleatorias.getImagen();
				if (img != null) {
					super.paintComponent(g);
					g.drawImage(img, 0, 0, 1000, 700, 0, 0, img.getWidth(), img.getHeight(), this);
				} else {
					super.paintComponent(g);
					g.setColor(Color.decode("#C9C9C9"));
				}
			}
		};
		Panel.setBackground(Color.decode("#C9C9C9"));
		Panel.setBorder(new EmptyBorder(new Insets(40, 150, 40, 150)));

		JPanel panel = new JPanel(new BorderLayout(0, 5));
		panel.setBorder(new EmptyBorder(new Insets(10, 20, 11, 20)));
		Panel.add(panel, BorderLayout.CENTER);

		// panel central

		// panel del centro

		JPanel panelCentral = new JPanel();
		panelCentral.setLayout(new BoxLayout(panelCentral, BoxLayout.Y_AXIS));
		panel.add(panelCentral, BorderLayout.CENTER);

		// panel central superior

		JPanel panelCentralSuperior = new JPanel(new BorderLayout());
		panelCentral.add(panelCentralSuperior);

		JLabel ingresePregunta = new JLabel("Ingrese la pregunta en el siguiente cuadro:");
		ingresePregunta.setFont(new Font("Segoe UI", Font.PLAIN, 25));
		panelCentralSuperior.add(ingresePregunta, BorderLayout.NORTH);

		JScrollPane scrollPane = new JScrollPane();
		panelCentralSuperior.add(scrollPane, BorderLayout.CENTER);

		pregunta = new JTextArea(7, 30);
		pregunta.setLineWrap(true);
		pregunta.setWrapStyleWord(true);
		pregunta.setEditable(true);
		scrollPane.setViewportView(pregunta);

		panelCentral.add(Box.createRigidArea(new Dimension(0, 10)));

		// panel central inferior

		JPanel panelCentralCentral = new JPanel(new BorderLayout());
		panelCentral.add(panelCentralCentral);

		JLabel ingreseRespuesta = new JLabel("Ingrese la respuesta en el siguiente cuadro:");
		ingreseRespuesta.setFont(new Font("Segoe UI", Font.PLAIN, 25));
		panelCentralCentral.add(ingreseRespuesta, BorderLayout.NORTH);

		txtfieldRespuesta = new JTextField(10);
		txtfieldRespuesta.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {

				char letraIngresada = e.getKeyChar();

				if (!((letraIngresada >= '0') && (letraIngresada <= '9') || (letraIngresada == '-')
						|| (letraIngresada == '+') || (letraIngresada == '.')
						|| (letraIngresada == KeyEvent.VK_BACK_SPACE) || (letraIngresada == KeyEvent.VK_DELETE))) {
					e.consume();
				}

				if ((letraIngresada == '-' || letraIngresada == '+') && (txtfieldRespuesta.getText().length() > 0)) {
					e.consume();
				}

				if (letraIngresada == '.'
						&& (txtfieldRespuesta.getText().contains(".") || (txtfieldRespuesta.getText().length() < 1))) {
					e.consume();
				}
			}
		});
		txtfieldRespuesta.setFont(new Font("Segoe UI", Font.PLAIN, 20));
		panelCentral.add(txtfieldRespuesta);

		panelCentral.add(Box.createRigidArea(new Dimension(0, 10)));

		// panel central inferior

		JPanel panelCentralInferior = new JPanel(new BorderLayout(10, 0));
		panelCentral.add(panelCentralInferior);

		// panel central inferior izquierdo

		JPanel panelCentralInferiorIzquierdo = new JPanel(new BorderLayout());
		panelCentralInferior.add(panelCentralInferiorIzquierdo, BorderLayout.WEST);

		JLabel lblValorMinimo = new JLabel("valor mínimo:");
		lblValorMinimo.setFont(new Font("Segoe UI", Font.PLAIN, 25));
		panelCentralInferiorIzquierdo.add(lblValorMinimo, BorderLayout.NORTH);

		valorMinimo = new crearCheckField();

		panelCentralInferiorIzquierdo.add(valorMinimo, BorderLayout.CENTER);

		// panel central inferior derecho

		JPanel panelCentralInferiorDerecho = new JPanel(new BorderLayout());
		panelCentralInferior.add(panelCentralInferiorDerecho, BorderLayout.EAST);

		JLabel lblValorMaximo = new JLabel("valor máximo:");
		lblValorMaximo.setFont(new Font("Segoe UI", Font.PLAIN, 25));
		panelCentralInferiorDerecho.add(lblValorMaximo, BorderLayout.NORTH);

		valorMaximo = new crearCheckField();
		panelCentralInferiorDerecho.add(valorMaximo, BorderLayout.EAST);

		panelCentral.add(Box.createRigidArea(new Dimension(0, 10)));

		JPanel panelCentralInferiorInferior = new JPanel(new BorderLayout());
		panelCentral.add(panelCentralInferiorInferior);

		JLabel ingresePeso = new JLabel("Ingrese el puntaje en el siguiente cuadro:");
		ingresePeso.setFont(new Font("Segoe UI", Font.PLAIN, 25));
		panelCentralInferiorInferior.add(ingresePeso, BorderLayout.NORTH);

		txtfieldPesoPregunta = new JTextField(5);
		txtfieldPesoPregunta.setFont(new Font("Segoe UI", Font.PLAIN, 20));
		txtfieldPesoPregunta.setTransferHandler(null);
		txtfieldPesoPregunta.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				int cantidadMaximaCaracteres = 3;

				char letraIngresada = e.getKeyChar();

				if (!((letraIngresada >= '0') && (letraIngresada <= '9') || (letraIngresada == '-')
						|| (letraIngresada == '+') || (letraIngresada == KeyEvent.VK_BACK_SPACE)
						|| (letraIngresada == KeyEvent.VK_DELETE))) {
					txtfieldPesoPregunta.setToolTipText("Por favor ingrese numeros entre -9999 y 9999.");
					e.consume();
				}

				if ((letraIngresada == '-' || letraIngresada == '+') && (txtfieldPesoPregunta.getText().length() > 0)) {
					e.consume();
				}

				if ((txtfieldPesoPregunta.getText().length() > 0) && (txtfieldPesoPregunta.getText().charAt(0) == '-'
						|| txtfieldPesoPregunta.getText().charAt(0) == '+')) {
					cantidadMaximaCaracteres = 4;
				}

				if (txtfieldPesoPregunta.getText().length() > cantidadMaximaCaracteres) {
					e.consume();
				}
			}
		});
		panelCentralInferiorInferior.add(txtfieldPesoPregunta, BorderLayout.CENTER);

		panelCentral.add(Box.createRigidArea(new Dimension(0, 10)));

		// panel inferior

		JPanel panelBotonesInferiores = new JPanel(new BorderLayout());
		panel.add(panelBotonesInferiores, BorderLayout.SOUTH);

		JButton botonSiguientePanelCrearPreguntaRespuestasCortas = new JButton("Siguiente");
		botonSiguientePanelCrearPreguntaRespuestasCortas.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		botonSiguientePanelCrearPreguntaRespuestasCortas.setBackground(Color.decode("#0077ff"));
		botonSiguientePanelCrearPreguntaRespuestasCortas
				.addActionListener(new botonSiguientePanelCrearPreguntaRespuestaNumerica_Listener());
		panelBotonesInferiores.add(botonSiguientePanelCrearPreguntaRespuestasCortas, BorderLayout.EAST);

		JButton botonCancelarPanelCrearPreguntaRespuestasNumericas = new JButton("Cancelar");
		botonCancelarPanelCrearPreguntaRespuestasNumericas
				.addActionListener(new botonCancelarPanelCrearPreguntaRespuestaNumerica_Listener());
		botonCancelarPanelCrearPreguntaRespuestasNumericas.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		botonCancelarPanelCrearPreguntaRespuestasNumericas.setBackground(Color.decode("#00668C"));
		panelBotonesInferiores.add(botonCancelarPanelCrearPreguntaRespuestasNumericas, BorderLayout.WEST);

		return Panel;
	}

	/**
	 * Esta clase, es el actionlistener del boton cancelar, del panel de crear
	 * preguntas de tipo respuestas numericas.
	 */
	private class botonCancelarPanelCrearPreguntaRespuestaNumerica_Listener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			boolean hayCambios = false;

			if (pregunta != null && !pregunta.getText().isBlank()) {
				hayCambios = true;
			}

			if (!hayCambios && txtPregunta != null && !txtPregunta.getText().isBlank()) {
				hayCambios = true;
			}

			if (!hayCambios && txtfieldPesoPregunta.getText() != null && !txtfieldPesoPregunta.getText().isBlank()) {
				hayCambios = true;
			}

			if (!hayCambios && valorMaximo != null && valorMaximo.check.isSelected()
					&& !valorMaximo.texto.getText().isBlank()) {
				hayCambios = true;
			}

			if (!hayCambios && valorMinimo != null && valorMinimo.check.isSelected()
					&& !valorMinimo.texto.getText().isBlank()) {
				hayCambios = true;
			}

			if (hayCambios) {
				if (Mensajes.consultar("Hay cambios sin guardar, ¿esta seguro de volver?", "Confirmación")) {
					actualizarPanelTab(panelCrearPreguntasTiposPreguntas(), 2);
				}
			} else {
				actualizarPanelTab(panelCrearPreguntasTiposPreguntas(), 2);
			}
		}
	}

	/**
	 * Esta clase, es el actionlistener del boton siguiente, del panel de crear
	 * preguntas de tipo respuestas cortas.
	 */
	private class botonSiguientePanelCrearPreguntaRespuestaNumerica_Listener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			try {

				if (!PREGUNTACREADA) {
					examenAlumno = new Examen();
				}

				if (pregunta == null || pregunta.getText().isBlank()) {
					Mensajes.error("Por favor ingrese su pregunta.");
					return;
				}

				if (txtfieldPesoPregunta == null || txtfieldPesoPregunta.getText().isBlank()) {
					Mensajes.error("Por favor, solo ingresa NUMEROS, el texto u otro tipo de caracteres NO sirven.");
					return;
				}

				if (txtfieldRespuesta == null || txtfieldRespuesta.getText().isBlank()) {
					Mensajes.error("Por favor, ingrese una respuesta valida.");
					return;
				}

				if (valorMaximo == null || (valorMaximo.check.isSelected() && valorMaximo.texto.getText().isBlank())) {
					Mensajes.error("Por favor, ingrese un valor maximo.");
					return;
				}

				if (valorMinimo == null || (valorMinimo.check.isSelected() && valorMinimo.texto.getText().isBlank())) {
					Mensajes.error("Por favor, ingrese un valor minimo.");
					return;
				}

				Float minimo = null;
				Float maximo = null;
				Float respuesta = null;
				Integer peso = null;

				if (valorMinimo.check.isSelected() && !valorMinimo.texto.getText().isBlank()) {
					try {
						minimo = Float.parseFloat(valorMinimo.texto.getText());
					} catch (Exception e2) {
						Mensajes.error(
								"ha ocurrido un error al generar el minimo, por favor revise los datos ingresados.");
					}
				}

				if (valorMaximo.check.isSelected() && !valorMaximo.texto.getText().isBlank()) {
					try {
						maximo = Float.parseFloat(valorMaximo.texto.getText());
					} catch (Exception e2) {
						Mensajes.error(
								"ha ocurrido un error al generar el maximo, por favor revise los datos ingresados.");
						return;
					}
				}

				try {
					respuesta = Float.parseFloat(txtfieldRespuesta.getText());
				} catch (Exception e2) {
					Mensajes.error("ha ocurrido un error en su respuesta, por favor revise los datos ingresados.");
					return;
				}

				try {
					peso = Integer.parseInt(txtfieldPesoPregunta.getText());
				} catch (Exception e2) {
					Mensajes.error("ha ocurrido un error en su puntaje, por favor revise los datos ingresados.");
					return;
				}

				if (maximo != null && !(maximo > respuesta)) {
					Mensajes.error("por favor, el valor maximo debe ser mayor que la respuesta.");
					return;
				}

				if (minimo != null && !(minimo < respuesta)) {
					Mensajes.error("por favor, el valor minimo debe ser menor que la respuesta.");
					return;
				}

				try {

					examenAlumno
							.agregaPregunta(new RespuestaNumerica(pregunta.getText(), respuesta, minimo, maximo, peso));

					PREGUNTACREADA = true;

					if (Mensajes.consultar("¿Desea seguir agregando preguntas de tipo respuesta numerica?",
							"Confirmación")) {
						actualizarPanelTab(panelCrearPreguntaRespuestaNumerica(), 2);
					} else {
						actualizarPanelTab(panelCrearPreguntasTiposPreguntas(), 2);
					}

				} catch (Exception e2) {
					Mensajes.error("Por favor, solo ingresa NUMEROS, el texto u otro tipo de caracteres NO sirven.");
					e2.printStackTrace();
				}
			} catch (Exception e2) {
				Mensajes.error(
						"<html>¡Ha ocurrido un error critico, por favor contacte con<br>el creador del programa para arreglar el problema!</html>");
			}
		}
	}

	// endregion

	// region de paneles y clases de mostrar resultados de alumnos.

	/**
	 * Este panel pide que ingrese la ubicacion de la carpeta con los resultados de
	 * los alumnos, al igual que la contraseña del examen.
	 * 
	 * @return retorna el panel generado.
	 */
	private JPanel panelResultadosAlumnos() {

		JPanel Panel = new JPanel(new BorderLayout()) {

			private static final long serialVersionUID = -1310522308825035142L;

			@Override
			protected void paintComponent(Graphics g) {
				BufferedImage img = ImagenesAleatorias.getImagen();
				if (img != null) {
					super.paintComponent(g);
					g.drawImage(img, 0, 0, 1000, 700, 0, 0, img.getWidth(), img.getHeight(), this);
				} else {
					super.paintComponent(g);
					g.setColor(Color.decode("#B1C9B1"));
				}
			}
		};
		Panel.setBackground(Color.decode("#B1C9B1"));
		Panel.setBorder(new EmptyBorder(new Insets(135, 200, 135, 200)));

		JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder(new EmptyBorder(20, 25, 20, 25));
		Panel.add(panel);

		// titulo

		JLabel lblUbicacionArchivo = new JLabel(
				"<html><p align = \"center\">Antes de continuar, por favor ingrese la ubicación de la<br>carpeta con todos los resultados de los alumnos.</p></html>");
		lblUbicacionArchivo.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblUbicacionArchivo.setFont(new Font("Segoe UI", Font.BOLD, 20));
		panel.add(lblUbicacionArchivo, BorderLayout.NORTH);

		// panel central

		JPanel panelCentral = new JPanel();
		panelCentral.setBorder(new EmptyBorder(50, 0, 50, 0));
		panelCentral.setLayout(new BoxLayout(panelCentral, BoxLayout.Y_AXIS));
		panel.add(panelCentral, BorderLayout.CENTER);

		// panel central superior

		JPanel panelUbicacion = new JPanel();
		panelCentral.add(panelUbicacion);

		txtUbicacionCarpeta = new JTextField(20);
		txtUbicacionCarpeta.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		new TextPrompt("Ubicación de la carpeta", txtUbicacionCarpeta);
		panelUbicacion.add(txtUbicacionCarpeta);

		JButton botonAbrirArchivoPanelResultadosAlumnos = new JButton("Abrir Carpeta");
		botonAbrirArchivoPanelResultadosAlumnos.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		botonAbrirArchivoPanelResultadosAlumnos.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser Archivo = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
				Archivo.setLocale(getLocale());
				Archivo.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int returnVal = Archivo.showOpenDialog(new JFrame());
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					txtUbicacionCarpeta.setText(Archivo.getSelectedFile().getAbsolutePath());
				}
			}
		});
		botonAbrirArchivoPanelResultadosAlumnos.setBackground(Color.decode("#0077ff"));
		panelUbicacion.add(botonAbrirArchivoPanelResultadosAlumnos, BorderLayout.EAST);

		// panel central inferior (contraseña resultados)

		JPanel panelContrasena = new JPanel(new FlowLayout());
		panelCentral.add(panelContrasena);

		contrasenaResultados = new JTextField(21);
		contrasenaResultados.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		new TextPrompt("Contraseña del examen del profesor", contrasenaResultados);
		panelContrasena.add(contrasenaResultados);

		JButton botonAbrirArchivoContrasenaPanelResultadosAlumnos = new JButton("Contraseña");
		botonAbrirArchivoContrasenaPanelResultadosAlumnos.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		botonAbrirArchivoContrasenaPanelResultadosAlumnos.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser Archivo = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
				Archivo.setLocale(getLocale());
				Archivo.setFileFilter(new FileNameExtensionFilter("Archivos .key", "key"));
				int returnVal = Archivo.showOpenDialog(new JFrame());
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					if (Pattern.compile("[:*<>\\/|?\"]+").matcher(Archivo.getSelectedFile().getName()).find()) {
						Mensajes.error(
								"<html><p align = \"center\">Por favor, no use caracteres ilegales en el nombre de su archivo.<br>(< > : \\ / | ? *)<br>Inténtelo nuevamente.</p></html>");
						return;
					}
					contrasenaResultados.setText(Archivo.getSelectedFile().getAbsolutePath());
				}
			}
		});
		botonAbrirArchivoContrasenaPanelResultadosAlumnos.setBackground(Color.decode("#0077ff"));
		panelContrasena.add(botonAbrirArchivoContrasenaPanelResultadosAlumnos, BorderLayout.EAST);

		// panel inferior

		JPanel panelInferior = new JPanel(new BorderLayout());
		panel.add(panelInferior, BorderLayout.SOUTH);

		JButton botonSiguientePanelResultadosAlumnos = new JButton("Siguiente");
		panelInferior.add(botonSiguientePanelResultadosAlumnos, BorderLayout.EAST);
		botonSiguientePanelResultadosAlumnos.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		botonSiguientePanelResultadosAlumnos.addActionListener(new botonSiguientePanelResultadosAlumnos_Listener());
		botonSiguientePanelResultadosAlumnos.setBackground(Color.decode("#0077ff"));

		return Panel;

	}

	/**
	 * Esta clase, es el actionlistener del boton siguiente, del panel de resultados
	 * del alumno.
	 */
	private class botonSiguientePanelResultadosAlumnos_Listener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			try {
				if (txtUbicacionCarpeta == null || txtUbicacionCarpeta.getText().isBlank()) {
					Mensajes.error(
							"<html><p align = \"center\">Por favor ingrese la ubicación de la carpeta con los resultados de los alumnos.</p></html>");
					return;
				}

				if (contrasenaResultados == null || contrasenaResultados.getText().isBlank()) {
					Mensajes.error(
							"<html><p align = \"center\">Por favor ingrese la ubicación del archivo con la contraseña del profesor.<br>Ejemplo: Nombre Examen + \"_NO_BORRAR_LlaveProfesor.key\"</p></html>");
					return;
				}

				String contra = (contrasenaResultados == null) ? "" : contrasenaResultados.getText();

				Triplet<List<RevisionAlumno>, Integer, String> tmp = RevisionAlumno
						.obtenerDatosAlumnos(txtUbicacionCarpeta.getText(), contra);

				DatosArchivos = tmp.getValue0();

				cantidadTotalPreguntas = tmp.getValue1();

				resultados = tmp.getValue2();

				if (DatosArchivos != null && DatosArchivos.size() > 0) {

					actualizarPanelTab(panelResultadosAlumnosTabla(), 4);

				} else {
					Mensajes.error("Por favor intentelo nuevamente, no hay datos que utilizar.");
				}
			} catch (Exception e2) {
				Mensajes.error(e2.getMessage());
			}
		}
	}

	/**
	 * Este metodo convierte los resultados de tipo respuesta corta, a una lista de
	 * triplets, con el fin de que esto almacene los resultados del profesor.
	 * 
	 * @param respuestas son las respuestas del profesor. tienen el formato
	 *                   ['0.15/0.20/0.25', '0.70/0.72/null', 'null/0.134/0.140',
	 *                   'null/0.777/null'].
	 * 
	 * @return retorna una lista de triplets con los resultados de los alumnos, o
	 *         sea, es lo mismo del ejemplo anterior, pero de la siguiente manera:
	 *         [{0.15,0.20,0.25},{0.70,0.72,null},{null,0.134,0.140},{null,0.777,null}]
	 */
	private List<Triplet<String, String, String>> convertirAResultadosRM(String[] respuestas) {
		String respuesta = null;
		String minimo = null;
		String maximo = null;

		List<Triplet<String, String, String>> resultadosRM = new ArrayList<Triplet<String, String, String>>(
				respuestas.length);

		for (String aux : respuestas) {
			String[] tmp = aux.split("/");

			respuesta = tmp[0];
			minimo = tmp[1];
			maximo = tmp[2];

			resultadosRM.add(new Triplet<String, String, String>(respuesta, minimo, maximo));
		}
		return resultadosRM;
	}

	/**
	 * Este panel muestra los resultados de los alumnos. En el los puede ordenar de
	 * acuerdo a lo que usted desee.
	 * 
	 * @return retorna el panel generado.
	 */
	private JPanel panelResultadosAlumnosTabla() {
		JPanel Panel = new JPanel(new BorderLayout()) {

			private static final long serialVersionUID = 3538317097146753763L;

			@Override
			protected void paintComponent(Graphics g) {
				BufferedImage img = ImagenesAleatorias.getImagen();
				if (img != null) {
					super.paintComponent(g);
					g.drawImage(img, 0, 0, 1000, 700, 0, 0, img.getWidth(), img.getHeight(), this);
				} else {
					super.paintComponent(g);
					g.setColor(Color.decode("#B1C9B1"));
				}
			}
		};
		Panel.setBackground(Color.decode("#B1C9B1"));
		Panel.setBorder(new EmptyBorder(new Insets(10, 100, 30, 100)));

		JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder(new EmptyBorder(new Insets(20, 20, 20, 20)));
		Panel.add(panel, BorderLayout.CENTER);

		JLabel lblTitulo = new JLabel("<html><p align = \"center\">Resultados de los alumnos del examen</p></html>");
		lblTitulo.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
		panel.add(lblTitulo, BorderLayout.NORTH);

		JPanel panelInferior = new JPanel(new BorderLayout());
		panel.add(panelInferior, BorderLayout.SOUTH);

		if (DatosArchivos != null) {

			if (resultados != null && !resultados.isBlank()) {

				String[] tiposResultados = BASE64.DesprotegerTextoBase64(resultados).split(" ");

				if (tiposResultados[0].length() > 0 && tiposResultados[0].charAt(0) == '!') {
					List<String> aux = new ArrayList<String>(
							Arrays.asList(BASE64.DesprotegerTextoBase64(tiposResultados[0].substring(1)).split(" ")));
					aux.removeAll(Arrays.asList("", null));

					resultadosSM = new ArrayList<String>();
					for (String tmp : aux) {
						resultadosSM.add(BASE64.DesprotegerTextoBase64(tmp));
					}
				}

				if (tiposResultados[1].length() > 0 && tiposResultados[1].charAt(0) == '#') {
					List<String> aux = new ArrayList<String>(
							Arrays.asList(BASE64.DesprotegerTextoBase64(tiposResultados[1].substring(1)).split(" ")));
					aux.removeAll(Arrays.asList("", null));

					resultadosVF = new ArrayList<String>();
					for (String tmp : aux) {
						resultadosVF.add(BASE64.DesprotegerTextoBase64(tmp));
					}
				}

				if (tiposResultados[2].length() > 0 && tiposResultados[2].charAt(0) == '$') {
					List<String> aux = new ArrayList<String>(
							Arrays.asList(BASE64.DesprotegerTextoBase64(tiposResultados[2].substring(1)).split(" ")));
					aux.removeAll(Arrays.asList("", null));

					resultadosRC = new ArrayList<String>();
					for (String tmp : aux) {
						resultadosRC.add(BASE64.DesprotegerTextoBase64(tmp));
					}
				}

				if (tiposResultados[3].length() > 0 && tiposResultados[3].charAt(0) == '%') {
					resultadosRM = convertirAResultadosRM(
							BASE64.DesprotegerTextoBase64(tiposResultados[3].substring(1)).split(";"));
				}
			}

			// nombre columnas

			List<String> tmpNombreColumnas = new ArrayList<String>();

			tmpNombreColumnas.add("N°");
			tmpNombreColumnas.add("Nombres");
			tmpNombreColumnas.add("Apellidos");
			tmpNombreColumnas.add("Rut");

			for (int i = 0; i < cantidadTotalPreguntas; i++) {
				tmpNombreColumnas.add("Pregunta " + (i + 1));
			}
			tmpNombreColumnas.add("Puntaje");
			tmpNombreColumnas.add("Nota");
			tmpNombreColumnas.add("Tiempo inicio");
			tmpNombreColumnas.add("Tiempo final");
			tmpNombreColumnas.add("Tiempo desarrollo");

			String[] nombreColumnas = tmpNombreColumnas.toArray(new String[0]);

			tabla = new JTable() {

				private static final long serialVersionUID = 6879652827269337035L;

				@Override
				public boolean editCellAt(int fila, int columna, EventObject evento) {
					return false;
				}

			};

			DefaultTableModel tablaDefecto = new DefaultTableModel(nombreColumnas, 0);
			tabla.setModel(tablaDefecto);

			TableRowSorter<DefaultTableModel> ordenarFilas = new TableRowSorter<DefaultTableModel>(tablaDefecto);
			tabla.setRowSorter(ordenarFilas);

			for (int i = 0; i < (5 + cantidadTotalPreguntas); i++) {
				ordenarFilas.setComparator(i, new Comparator<String>() {
					@Override
					public int compare(String o1, String o2) {

						// caso de los puntajes (o respuestas, igual es valido).
						if (Pattern.matches("\\b[0-9]+\\b", o1) && Pattern.matches("\\b[0-9]+\\b", o2)) {
							return Integer.parseInt(o1) - Integer.parseInt(o2);
						}

						// ordenar por rut.
						if (Texto.validarRut(o1) && Texto.validarRut(o2)) {
							return (Integer.parseInt(o1.replace(".", "").replace("-", ""))
									- Integer.parseInt(o2.replace(".", "").replace("-", "")));
						}

						// caso de las notas.
						if (Pattern.matches("\\b[0-9]+\\b", o1.replaceAll(",", ""))
								&& Pattern.matches("\\b[0-9]+\\b", o2.replaceAll(",", ""))) {
							return Integer.parseInt(o1.replaceAll(",", "")) - Integer.parseInt(o2.replaceAll(",", ""));
						}

						// nombres, apellidos y respuestas. tiene el 0-9 por si es que el usuario logro
						// ingresar un numero en su nombre.
						if (Pattern.matches("[a-zA-Z0-9]+", o1) && Pattern.matches("[a-zA-Z0-9]+", o2)) {
							return o1.compareTo(o2);
						}

						// demas texto que pase.
						return o1.compareTo(o2);
					}
				});
			}

			tabla.setFillsViewportHeight(true);
			tabla.setOpaque(true);
			tabla.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			tabla.getTableHeader().setReorderingAllowed(false);

			for (int i = 0; i < DatosArchivos.size(); i++) {
				List<Object> informacion = new ArrayList<Object>();
				informacion.add(String.valueOf(i + 1));
				informacion.add(DatosArchivos.get(i).getNombreAlumno());
				informacion.add(DatosArchivos.get(i).getApellido());
				informacion.add(DatosArchivos.get(i).getRut());

				// i = fila , j = columna
				// i = (0 = numero, 1 = nombre, 2 = apellido, 3 = rut, 4 = respuestas)

				int index = 0;
				boolean continuarSM = true;
				boolean continuarVF = false;
				boolean continuarRC = false;
				boolean continuarRM = false;

				for (int j = 0; j < cantidadTotalPreguntas; j++) {

					informacion.add(DatosArchivos.get(i).getRespuestas()[j]);

					if (resultadosSM != null && resultadosSM.size() > 0 && index < resultadosSM.size() && continuarSM) {
						tabla.getColumnModel().getColumn(j + 4).setCellRenderer(new RendererSeleccionMultiple(index));
						index++;
						continue;
					} else if ((resultadosSM == null || index > resultadosSM.size() - 1) && continuarSM) {
						index = 0;
						continuarSM = false;
						continuarVF = true;
						continuarRC = false;
						continuarRM = false;
					}

					if (resultadosVF != null && resultadosVF.size() > 0 && index < resultadosVF.size() && continuarVF) {
						tabla.getColumnModel().getColumn(j + 4).setCellRenderer(new RendererVerdaderoFalso(index));
						index++;
						continue;
					} else if ((resultadosVF == null || index > resultadosVF.size() - 1) && continuarVF) {
						index = 0;
						continuarSM = false;
						continuarVF = false;
						continuarRC = true;
						continuarRM = false;
					}

					if (resultadosRC != null && resultadosRC.size() > 0 && index < resultadosRC.size() && continuarRC) {
						tabla.getColumnModel().getColumn(j + 4).setCellRenderer(new RendererRespuestasCortas(index));
						index++;
						continue;
					} else if ((resultadosRC == null || index > resultadosRC.size() - 1) && continuarRC) {
						index = 0;
						continuarSM = false;
						continuarVF = false;
						continuarRC = false;
						continuarRM = true;
					}

					if (resultadosRM != null && resultadosRM.size() > 0 && index < resultadosRM.size() && continuarRM) {
						tabla.getColumnModel().getColumn(j + 4).setCellRenderer(new RendererRespuestasNumericas(index));
						index++;
						continue;
					} else if ((resultadosRM == null || index > resultadosRM.size() - 1) && continuarRM) {
						index = 0;
						continuarSM = true;
						continuarVF = false;
						continuarRC = false;
						continuarRM = false;
					}
				}

				informacion.add(DatosArchivos.get(i).getPuntaje_Total());
				informacion.add(DatosArchivos.get(i).getNota());
				informacion.add(DatosArchivos.get(i).getTiempoInicio());
				informacion.add(DatosArchivos.get(i).getTiempoFinal());
				informacion.add(DatosArchivos.get(i).getTiempoDesarrollo());

				tablaDefecto.addRow(informacion.toArray());
			}

			JScrollPane scrollPane = new JScrollPane(tabla);

			panel.add(scrollPane);

			// barra de busqueda

			JTextField buscador = new JTextField(20);
			new TextPrompt("Ingrese aquí el dato que desea buscar.", buscador);
			buscador.getDocument().addDocumentListener(new DocumentListener() {
				@Override
				public void changedUpdate(DocumentEvent e) {
					// agregado por el documentlistener
				}

				@Override
				public void insertUpdate(DocumentEvent e) {
					String texto = buscador.getText();
					if (texto.trim().length() == 0) {
						ordenarFilas.setRowFilter(null);
					} else {
						ordenarFilas.setRowFilter(RowFilter.regexFilter("(?i)" + texto));
					}
					tabla.repaint();
				}

				@Override
				public void removeUpdate(DocumentEvent e) {
					String texto = buscador.getText();
					if (texto.trim().length() == 0) {
						ordenarFilas.setRowFilter(null);
					} else {
						ordenarFilas.setRowFilter(RowFilter.regexFilter("(?i)" + texto));
					}
				}
			});
			panelInferior.add(buscador, BorderLayout.CENTER);

		} else {

			JLabel noResultados = new JLabel(
					"<html><p align = \"center\">No hay resultados de alumnos que mostrar. Intente agregarlos nuevamente.</p></html>");
			noResultados.setAlignmentX(Component.CENTER_ALIGNMENT);
			noResultados.setFont(new Font("Segoe UI", Font.BOLD, 20));
			panel.add(noResultados, BorderLayout.CENTER);

		}

		// panel inferior --------------------------------------------------------

		JButton botonExportarPanelResultadosAlumnosTabla = new JButton("Exportar datos");
		botonExportarPanelResultadosAlumnosTabla.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		botonExportarPanelResultadosAlumnosTabla.addActionListener(new botonExportarPanelResultadosAlumnosTabla());
		botonExportarPanelResultadosAlumnosTabla.setBackground(Color.decode("#00668C"));
		panelInferior.add(botonExportarPanelResultadosAlumnosTabla, BorderLayout.WEST);

		JButton botonFinalizarPanelResultadosAlumnosTabla = new JButton("Finalizar");
		botonFinalizarPanelResultadosAlumnosTabla.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		botonFinalizarPanelResultadosAlumnosTabla.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				DatosArchivos = null;
				actualizarPanelTab(panelResultadosAlumnos(), 4);
			}
		});
		botonFinalizarPanelResultadosAlumnosTabla.setBackground(Color.decode("#0077ff"));
		panelInferior.add(botonFinalizarPanelResultadosAlumnosTabla, BorderLayout.EAST);

		return Panel;
	}

	/**
	 * Esta clase permite cambiar el color de la celda, dependiendo de la respuesta
	 * del alumno.
	 */
	private class RendererSeleccionMultiple extends DefaultTableCellRenderer {
		private static final long serialVersionUID = 6107230387975043558L;

		private int index = 0;

		public RendererSeleccionMultiple(int index) {
			super();
			this.index = index;
			setHorizontalAlignment(JLabel.RIGHT);
		}

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
			super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

			if (resultadosSM != null && resultadosSM.size() > 0) {
				String fila = (String) value;

				if (fila != null) {
					if (fila.equalsIgnoreCase(resultadosSM.get(index))) {
						setForeground(Color.black);
						setBackground(Color.decode("#38F04A"));// verde
					} else {
						setForeground(Color.WHITE);
						setBackground(Color.decode("#EB3218"));// rojo
					}
				}
			}
			return this;
		}
	}

	/**
	 * Esta clase permite cambiar el color de la celda, dependiendo de la respuesta
	 * del alumno.
	 */
	private class RendererVerdaderoFalso extends DefaultTableCellRenderer {
		private static final long serialVersionUID = -4368223044401266085L;

		private int index = 0;

		public RendererVerdaderoFalso(int index) {
			super();
			this.index = index;
			setHorizontalAlignment(JLabel.RIGHT);
		}

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
			super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

			if (resultadosVF != null && resultadosVF.size() > 0) {
				String fila = (String) value;

				if (fila != null) {
					if (fila.equalsIgnoreCase(resultadosVF.get(index))) {
						setForeground(Color.black);
						setBackground(Color.decode("#38F04A"));// verde
					} else {
						setForeground(Color.WHITE);
						setBackground(Color.decode("#EB3218"));// rojo
					}
				}
			}
			return this;
		}
	}

	/**
	 * Esta clase permite cambiar el color de la celda, dependiendo de la respuesta
	 * del alumno.
	 */
	private class RendererRespuestasCortas extends DefaultTableCellRenderer {
		private static final long serialVersionUID = -7701610812403710788L;

		private int index = 0;

		public RendererRespuestasCortas(int index) {
			super();
			this.index = index;
			setHorizontalAlignment(JLabel.RIGHT);
		}

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
			super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

			if (resultadosRC != null && resultadosRC.size() > 0) {
				String fila = (String) value;

				if (Texto.similitud(fila, resultadosRC.get(index)) > 79) {
					setForeground(Color.black);
					setBackground(Color.decode("#38F04A"));// verde
				} else {
					setForeground(Color.WHITE);
					setBackground(Color.decode("#EB3218"));// rojo
				}
			}
			return this;
		}
	}

	/**
	 * Esta clase permite cambiar el color de la celda, dependiendo de la respuesta
	 * del alumno.
	 */
	private class RendererRespuestasNumericas extends DefaultTableCellRenderer {
		private static final long serialVersionUID = -8772313548998676495L;

		private int index = 0;

		public RendererRespuestasNumericas(int index) {
			super();
			this.index = index;
			setHorizontalAlignment(JLabel.RIGHT);
		}

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
			super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

			if (resultadosRM != null && resultadosRM.size() > 0) {
				String fila = (String) value;

				if (fila != null) {
					Float respuestaAlumno = Texto.TieneLetras(fila) ? null : Float.valueOf(fila);
					Float respuestaCorrecta = Texto.TieneLetras(resultadosRM.get(index).getValue0()) ? null : Float.valueOf(resultadosRM.get(index).getValue0());

					Float minimo = Texto.TieneLetras(resultadosRM.get(index).getValue1()) ? null : Float.valueOf(resultadosRM.get(index).getValue1());
					Float maximo = Texto.TieneLetras(resultadosRM.get(index).getValue2()) ? null : Float.valueOf(resultadosRM.get(index).getValue2());

					if (respuestaCorrecta.equals(respuestaAlumno)) {
						setForeground(Color.black);
						setBackground(Color.decode("#38F04A"));// verde
					} else if (maximo != null && (maximo >= respuestaAlumno && respuestaAlumno >= respuestaCorrecta)) {
						setForeground(Color.black);
						setBackground(Color.decode("#38F04A"));// verde
					} else if (minimo != null && (minimo >= respuestaAlumno && respuestaCorrecta <= respuestaAlumno)) {
						setForeground(Color.black);
						setBackground(Color.decode("#38F04A"));// verde
					} else {
						setForeground(Color.WHITE);
						setBackground(Color.decode("#EB3218"));// rojo
					}
				}
			}
			return this;
		}
	}

	/**
	 * Esta clase, es el actionlistener del boton exportar, de la tabla del panel de
	 * resultados del alumno.
	 */
	private class botonExportarPanelResultadosAlumnosTabla implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			String nombreArchivo = "";

			JFileChooser Archivo = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
			Archivo.setFileFilter(new FileNameExtensionFilter("Archivos .xlsx", "xlsx"));
			Archivo.setAcceptAllFileFilterUsed(false);
			int returnVal = Archivo.showSaveDialog(new JFrame());

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				/*-
				 [] = inicio y fin del regex.
				 :*<>\/|?" = caracteres a verificar.
				 + = para todo el string.
				 
				 Pattern.compile("[:*<>\\/|?\"]+") es una clase la cual compila el regex que se le da.
				 .matcher(String).find() estas funciones se encargan de verificar si el string encaja 
				 						 dentro de las caracteristicas del regex.
				 */
				if (Pattern.compile("[:*<>\\/|?\"]+").matcher(Archivo.getSelectedFile().getName()).find()) {
					Mensajes.error(
							"<html><p align = \"center\">Por favor, no use caracteres ilegales en el nombre de su archivo.<br>(< > : \\ / | ? *)<br>Inténtelo nuevamente.</p></html>");
					return;
				}
				nombreArchivo = Archivo.getSelectedFile().getAbsolutePath();
				try {
					ExportarAExcel.exportarAExcel(tabla, nombreArchivo);
				} catch (Exception e2) {
					Mensajes.error("Ha fallado al guardar la tabla, por favor intente nuevamente.");
				}

			} else {
				return;
			}

		}
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

			private static final long serialVersionUID = 3304785110227402415L;

			@Override
			protected void paintComponent(Graphics g) {
				BufferedImage img = ImagenesAleatorias.getImagen();
				if (img != null) {
					super.paintComponent(g);
					g.drawImage(img, 0, 0, 1000, 669, 0, 0, img.getWidth(), img.getHeight(), this);
				} else {
					super.paintComponent(g);
					g.setColor(Color.decode("#FA892A"));
				}
			}
		};
		Panel.setBackground(Color.decode("#FA892A"));
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

				String titulo = "Vista Previa PDF";

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

	// region de paneles y clases de visualizar examenes de tipo smr.

	/**
	 * Este panel consulta por la ubicación del archivo smr, para poder luego
	 * mostrarlo en pantalla en una pestaña nueva.
	 * 
	 * @return retorna el panel generado.
	 */
	private JPanel panelVisualizarsSmr() {
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
					g.setColor(Color.decode("#05AB97"));
				}
			}
		};

		Panel.setBackground(Color.decode("#05AB97"));
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

		txtFieldUbicacionSMR = new JTextField(20);
		txtFieldUbicacionSMR.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		new TextPrompt("Ubicación del archivo", txtFieldUbicacionSMR);
		panelUbicacion.add(txtFieldUbicacionSMR);

		JButton botonAbrirArchivoPanelResponderProfesor = new JButton("Abrir Archivo");
		botonAbrirArchivoPanelResponderProfesor.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		botonAbrirArchivoPanelResponderProfesor.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser Archivo = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
				Archivo.setFileFilter(new FileNameExtensionFilter("Archivos .smr", "smr"));
				int returnVal = Archivo.showOpenDialog(new JFrame());
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					txtFieldUbicacionSMR.setText(Archivo.getSelectedFile().getAbsolutePath());
				}
			}
		});
		botonAbrirArchivoPanelResponderProfesor.setBackground(Color.decode("#0077ff"));
		panelUbicacion.add(botonAbrirArchivoPanelResponderProfesor);

		// panel central inferior (contraseña archivo)

		JPanel panelContrasena = new JPanel(new FlowLayout());
		panelCentral.add(panelContrasena);

		txtFieldContrasenaSMR = new JTextField(21);
		txtFieldContrasenaSMR.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		txtFieldContrasenaSMR.setEnabled(false);
		new TextPrompt("Contraseña del archivo", txtFieldContrasenaSMR);
		panelContrasena.add(txtFieldContrasenaSMR);

		JToggleButton tglbtnContra = new JToggleButton("Contraseña");
		tglbtnContra.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		tglbtnContra.setBackground(Color.decode("#0077ff"));
		tglbtnContra.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent itemEvent) {
				int estado = itemEvent.getStateChange();
				if (estado == ItemEvent.SELECTED) {
					txtFieldContrasenaSMR.setEnabled(true);
				} else {
					txtFieldContrasenaSMR.setText("");
					txtFieldContrasenaSMR.setEnabled(false);
				}
			}
		});
		panelContrasena.add(tglbtnContra);

		// panel del sur

		JPanel panelBotonesInferiores = new JPanel(new BorderLayout());
		panel.add(panelBotonesInferiores, BorderLayout.SOUTH);

		JButton botonSiguientePanelResponderProfesor = new JButton("Siguiente");
		botonSiguientePanelResponderProfesor.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		botonSiguientePanelResponderProfesor.addActionListener(new botonSiguientePanelVisualizarSmr());
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
	 * Esta clase es el action listener del boton siguiente del panel para
	 * visualizar archivos smr.
	 */
	private class botonSiguientePanelVisualizarSmr implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			try {

				if (tabbedPane.getTabCount() > 25) {
					Mensajes.error(
							"<html>Ha llegado al máximo de examenes mostrados.<br>Por favor cierre algunos antes de abrir otro.</html>");
					return;
				}

				JPanel panel = new Alumno(
						Archivos.interpretarArchivo(txtFieldUbicacionSMR.getText(), txtFieldContrasenaSMR.getText()));

				String titulo = "Vista previa SMR";

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

				txtFieldUbicacionSMR.setText("");
				txtFieldContrasenaSMR.setText("");

			} catch (Exception e2) {
				Mensajes.error(e2.getMessage());
			}
		}
	}

	// endregion

	// region de paneles y clases de eliminar/editar preguntas.

	/**
	 * Este panel pide que ingrese la ubicacion del archivo .smr que se desea
	 * modificar.
	 * 
	 * @return retorna el panel generado.
	 */
	private JPanel panelEditarPreguntas() {

		JPanel Panel = new JPanel(new BorderLayout()) {

			private static final long serialVersionUID = 3304785110227402415L;

			@Override
			protected void paintComponent(Graphics g) {
				BufferedImage img = ImagenesAleatorias.getImagen();
				if (img != null) {
					super.paintComponent(g);
					g.drawImage(img, 0, 0, 1000, 700, 0, 0, img.getWidth(), img.getHeight(), this);
				} else {
					super.paintComponent(g);
					g.setColor(Color.decode("#B1C9B1"));
				}
			}
		};
		Panel.setBackground(Color.decode("#B1C9B1"));
		Panel.setBorder(new EmptyBorder(new Insets(135, 200, 135, 200)));

		JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder(new EmptyBorder(20, 25, 20, 25));
		Panel.add(panel);

		// titulo

		JLabel lblUbicacionArchivo = new JLabel(
				"<html><p align = \"center\">Antes de continuar, por favor ingrese la ubicación del<br>archivo con el examen.</p></html>");
		lblUbicacionArchivo.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblUbicacionArchivo.setFont(new Font("Segoe UI", Font.BOLD, 20));
		panel.add(lblUbicacionArchivo, BorderLayout.NORTH);

		// panel central

		JPanel panelCentral = new JPanel();
		panelCentral.setBorder(new EmptyBorder(50, 0, 50, 0));
		panelCentral.setLayout(new BoxLayout(panelCentral, BoxLayout.Y_AXIS));
		panel.add(panelCentral, BorderLayout.CENTER);

		// panel central superior

		JPanel panelUbicacion = new JPanel();
		panelCentral.add(panelUbicacion);

		txtUbicacionArchivo = new JTextField(20);
		txtUbicacionArchivo.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		new TextPrompt("Ubicación del archivo", txtUbicacionArchivo);
		panelUbicacion.add(txtUbicacionArchivo);

		JButton botonAbrirArchivopanelEditarPreguntas = new JButton("Abrir Archivo");
		botonAbrirArchivopanelEditarPreguntas.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		botonAbrirArchivopanelEditarPreguntas.addActionListener(new botonAbrirArchivopanelEditarPreguntas_Listener());
		botonAbrirArchivopanelEditarPreguntas.setBackground(Color.decode("#0077ff"));
		panelUbicacion.add(botonAbrirArchivopanelEditarPreguntas);

		// panel central inferior (contraseña archivo)

		JPanel panelContrasena = new JPanel(new FlowLayout());
		panelCentral.add(panelContrasena);

		fieldContrasenaExamen = new JTextField(21);
		fieldContrasenaExamen.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		fieldContrasenaExamen.setEnabled(false);
		new TextPrompt("Contraseña del archivo", fieldContrasenaExamen);
		panelContrasena.add(fieldContrasenaExamen);

		JToggleButton tglbtnContra = new JToggleButton("Contraseña");
		tglbtnContra.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		tglbtnContra.setBackground(Color.decode("#0077ff"));
		tglbtnContra.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent itemEvent) {
				int estado = itemEvent.getStateChange();
				if (estado == ItemEvent.SELECTED) {
					fieldContrasenaExamen.setText("");
					fieldContrasenaExamen.setEnabled(true);
				} else {
					fieldContrasenaExamen.setText("");
					fieldContrasenaExamen.setEnabled(false);
				}
			}
		});
		panelContrasena.add(tglbtnContra);

		// panel inferior

		JPanel panelBotonesInferiores = new JPanel(new BorderLayout());
		panel.add(panelBotonesInferiores, BorderLayout.SOUTH);

		JButton botonSiguientepanelEditarPreguntas = new JButton("Siguiente");
		botonSiguientepanelEditarPreguntas.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		botonSiguientepanelEditarPreguntas.addActionListener(new botonSiguientepanelEditarPreguntas_Listener());
		botonSiguientepanelEditarPreguntas.setBackground(Color.decode("#0077ff"));
		panelBotonesInferiores.add(botonSiguientepanelEditarPreguntas, BorderLayout.EAST);

		return Panel;
	}

	/**
	 * Esta clase, es el actionlistener del boton abrir archivo, del panel editar
	 * preguntas.
	 */
	private class botonAbrirArchivopanelEditarPreguntas_Listener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser Archivo = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
			Archivo.setLocale(getLocale());
			Archivo.setFileFilter(new FileNameExtensionFilter("Archivos .smr", "smr"));
			Archivo.setAcceptAllFileFilterUsed(false);
			int returnVal = Archivo.showOpenDialog(new JFrame());
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				if (Pattern.compile("[:*<>\\/|?\"]+").matcher(Archivo.getSelectedFile().getName()).find()) {
					Mensajes.error(
							"<html><p align = \"center\">Por favor, no use caracteres ilegales en el nombre de su archivo.<br>(< > : \\ / | ? *)<br>Inténtelo nuevamente.</p></html>");
					return;
				}
				txtUbicacionArchivo.setText(Archivo.getSelectedFile().getAbsolutePath());
			}
		}
	}

	/**
	 * Esta clase, es el actionlistener del boton siguiente, del panel editar
	 * preguntas.
	 */
	private class botonSiguientepanelEditarPreguntas_Listener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			try {
				PANELACTUAL = EDITAREXAMEN;
				examenAlumno = Archivos.interpretarArchivo(txtUbicacionArchivo.getText(),
						fieldContrasenaExamen.getText());
				tabActivado(false);
				actualizarPanelTab(panelEditarPreguntasTipoPreguntas(), 3);
			} catch (Exception e2) {
				Mensajes.error(e2.getMessage());
			}
		}
	}

	/**
	 * Este panel pide que seleccione que tipo de pregunta desea editar.
	 * 
	 * @return retorna el panel generado.
	 */
	private JPanel panelEditarPreguntasTipoPreguntas() {

		JPanel Panel = new JPanel(new BorderLayout()) {

			private static final long serialVersionUID = -320467074946314524L;

			@Override
			protected void paintComponent(Graphics g) {
				BufferedImage img = ImagenesAleatorias.getImagen();
				if (img != null) {
					super.paintComponent(g);
					g.drawImage(img, 0, 0, 1000, 700, 0, 0, img.getWidth(), img.getHeight(), this);
				} else {
					super.paintComponent(g);
					g.setColor(Color.decode("#C9C9C9"));
				}
			}
		};
		Panel.setBackground(Color.decode("#C9C9C9"));
		Panel.setBorder(new EmptyBorder(new Insets(120, 210, 120, 210)));

		JPanel panel = new JPanel(new BorderLayout(0, 20));
		panel.setBorder(new EmptyBorder(new Insets(40, 70, 30, 70)));
		Panel.add(panel, BorderLayout.CENTER);

		JLabel lblPreguntas = new JLabel("<html><p align = \"center\">¿Preguntas de que tipo desea editar?</p></html>");
		lblPreguntas.setFont(new Font("Segoe UI", Font.PLAIN, 25));
		panel.add(lblPreguntas, BorderLayout.PAGE_START);

		if (examenAlumno != null && (examenAlumno.verificarExamen(1) || examenAlumno.verificarExamen(2)
				|| examenAlumno.verificarExamen(3))) {
			JPanel panelBotonesPreguntas = new JPanel(new GridLayout(0, 2, 10, 10));
			panel.add(panelBotonesPreguntas, BorderLayout.CENTER);

			if (examenAlumno.verificarExamen(1)) {
				JButton botonSeleccionMultiplepanelEditarPreguntasTipoPreguntas = new JButton("Selección multiple");
				botonSeleccionMultiplepanelEditarPreguntasTipoPreguntas.setFont(new Font("Segoe UI", Font.PLAIN, 20));
				botonSeleccionMultiplepanelEditarPreguntasTipoPreguntas
						.addActionListener(new botonSeleccionMultiplepanelEditarPreguntasTipoPreguntas_Listener());
				botonSeleccionMultiplepanelEditarPreguntasTipoPreguntas.setHorizontalAlignment(JButton.CENTER);
				panelBotonesPreguntas.add(botonSeleccionMultiplepanelEditarPreguntasTipoPreguntas);

				JLabel lblCantidadPreguntasSM = new JLabel(
						"Cantidad de preguntas: " + examenAlumno.preguntas_SM.size());
				lblCantidadPreguntasSM.setHorizontalAlignment(JLabel.CENTER);
				panelBotonesPreguntas.add(lblCantidadPreguntasSM);
			}

			if (examenAlumno.verificarExamen(2)) {
				JButton botonVerdaderoFalsopanelEditarPreguntasTipoPreguntas = new JButton("Verdadero / falso");
				botonVerdaderoFalsopanelEditarPreguntasTipoPreguntas.setFont(new Font("Segoe UI", Font.PLAIN, 20));
				botonVerdaderoFalsopanelEditarPreguntasTipoPreguntas
						.addActionListener(new botonVerdaderoFalsopanelEditarPreguntasTipoPreguntas_Listener());
				botonVerdaderoFalsopanelEditarPreguntasTipoPreguntas.setHorizontalAlignment(JButton.CENTER);
				panelBotonesPreguntas.add(botonVerdaderoFalsopanelEditarPreguntasTipoPreguntas);

				JLabel lblCantidadPreguntasVF = new JLabel(
						"Cantidad de preguntas: " + examenAlumno.preguntas_VF.size());
				lblCantidadPreguntasVF.setHorizontalAlignment(JLabel.CENTER);
				panelBotonesPreguntas.add(lblCantidadPreguntasVF);
			}

			if (examenAlumno.verificarExamen(3)) {
				JButton botonRespuestasCortaspanelEditarPreguntasTipoPreguntas = new JButton("Respuestas cortas");
				botonRespuestasCortaspanelEditarPreguntasTipoPreguntas.setFont(new Font("Segoe UI", Font.PLAIN, 20));
				botonRespuestasCortaspanelEditarPreguntasTipoPreguntas
						.addActionListener(new botonRespuestasCortaspanelEditarPreguntasTipoPreguntas_Listener());
				botonRespuestasCortaspanelEditarPreguntasTipoPreguntas.setHorizontalAlignment(JButton.CENTER);
				panelBotonesPreguntas.add(botonRespuestasCortaspanelEditarPreguntasTipoPreguntas);

				JLabel lblCantidadPreguntasRC = new JLabel(
						"Cantidad de preguntas: " + examenAlumno.preguntas_RC.size());
				lblCantidadPreguntasRC.setHorizontalAlignment(JLabel.CENTER);
				panelBotonesPreguntas.add(lblCantidadPreguntasRC);
			}

			if (examenAlumno.verificarExamen(4)) {
				JButton botonRespuestaNumericaPanelEditarPreguntasTipoPreguntas = new JButton("Respuestas numericas");
				botonRespuestaNumericaPanelEditarPreguntasTipoPreguntas.setFont(new Font("Segoe UI", Font.PLAIN, 20));
				botonRespuestaNumericaPanelEditarPreguntasTipoPreguntas
						.addActionListener(new botonRespuestaNumericaPanelEditarPreguntasTipoPreguntas_Listener());
				botonRespuestaNumericaPanelEditarPreguntasTipoPreguntas.setHorizontalAlignment(JButton.CENTER);
				panelBotonesPreguntas.add(botonRespuestaNumericaPanelEditarPreguntasTipoPreguntas);

				JLabel lblCantidadPreguntasRC = new JLabel(
						"Cantidad de preguntas: " + examenAlumno.preguntas_RC.size());
				lblCantidadPreguntasRC.setHorizontalAlignment(JLabel.CENTER);
				panelBotonesPreguntas.add(lblCantidadPreguntasRC);
			}

		} else {
			JLabel error = new JLabel(
					"<html><p align = \"center\">¡Por favor, agregue un examen!<br>Vuelva al panel anterior e<br>intentelo nuevamente.</p></html>");
			error.setFont(new Font("Segoe UI", Font.PLAIN, 30));
			panel.add(error, BorderLayout.CENTER);
		}

		JPanel panelInferior = new JPanel(new BorderLayout());
		panel.add(panelInferior, BorderLayout.SOUTH);

		JButton botonCancelarpanelEditarPreguntasTipoPreguntas = new JButton("Cancelar");
		botonCancelarpanelEditarPreguntasTipoPreguntas.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		botonCancelarpanelEditarPreguntasTipoPreguntas.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				tabActivado(true);
				examenAlumno = null;
				actualizarPanelTab(panelEditarPreguntas(), 3);
			}
		});
		botonCancelarpanelEditarPreguntasTipoPreguntas.setBackground(Color.decode("#00668C"));
		panelInferior.add(botonCancelarpanelEditarPreguntasTipoPreguntas, BorderLayout.WEST);

		if (CAMBIOHECHO && examenAlumno != null && (examenAlumno.verificarExamen(1) || examenAlumno.verificarExamen(2)
				|| examenAlumno.verificarExamen(3))) {
			JButton botonFinalizarpanelEditarPreguntasTipoPreguntas = new JButton("Finalizar");
			botonFinalizarpanelEditarPreguntasTipoPreguntas.setFont(new Font("Segoe UI", Font.PLAIN, 18));
			botonFinalizarpanelEditarPreguntasTipoPreguntas
					.addActionListener(new botonFinalizarpanelEditarPreguntasTipoPreguntas_Listener());
			botonFinalizarpanelEditarPreguntasTipoPreguntas.setBackground(Color.decode("#0077ff"));
			panelInferior.add(botonFinalizarpanelEditarPreguntasTipoPreguntas, BorderLayout.EAST);
		}

		return Panel;

	}

	/**
	 * Esta clase, es el actionlistener del boton "respuestas cortas", del panel de
	 * elegir pregunta a editar.
	 */
	private class botonRespuestasCortaspanelEditarPreguntasTipoPreguntas_Listener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (examenAlumno != null) {
				if (examenAlumno.preguntas_RC.size() > 0) {
					TIPOPREGUNTA = RESPUESTACORTA;
					TABACTUAL = 3;
					actualizarPanelTab(panelEditarPreguntasEleccionPreguntas(), 3);
				} else {
					Mensajes.error("¡No hay preguntas de tipo respuestas cortas para eliminar!");
					actualizarPanelTab(panelEditarPreguntasTipoPreguntas(), 3);
				}
			} else {
				Mensajes.error("¡Si ves este mensaje, por favor reporta al creador del programa este problema!");
			}
		}
	}

	/**
	 * Esta clase, es el actionlistener del boton "respuestas numericas", del panel
	 * de elegir pregunta a editar.
	 */
	private class botonRespuestaNumericaPanelEditarPreguntasTipoPreguntas_Listener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (examenAlumno != null) {
				if (examenAlumno.preguntas_RM.size() > 0) {
					TIPOPREGUNTA = RESPUESTANUMERICA;
					TABACTUAL = 3;
					actualizarPanelTab(panelEditarPreguntasEleccionPreguntas(), 3);
				} else {
					Mensajes.error("¡No hay preguntas de tipo respuestas numericas para eliminar!");
					actualizarPanelTab(panelEditarPreguntasTipoPreguntas(), 3);
				}
			} else {
				Mensajes.error("¡Si ves este mensaje, por favor reporta al creador del programa este problema!");
			}
		}
	}

	/**
	 * Esta clase, es el actionlistener del boton "seleccion multiple", del panel de
	 * elegir pregunta a editar.
	 */
	private class botonSeleccionMultiplepanelEditarPreguntasTipoPreguntas_Listener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (examenAlumno != null) {
				if (examenAlumno.preguntas_SM.size() > 0) {
					TIPOPREGUNTA = SELECCIONMULTIPLE;
					TABACTUAL = 3;
					actualizarPanelTab(panelEditarPreguntasEleccionPreguntas(), 3);
				} else {
					Mensajes.error("¡No hay preguntas de tipo selección múltiple para eliminar!");
					actualizarPanelTab(panelEditarPreguntasTipoPreguntas(), 3);
				}
			} else {
				Mensajes.error("¡Si ves este mensaje, por favor reporta al creador del programa este problema!");
			}
		}
	}

	/**
	 * Esta clase, es el actionlistener del boton "verdadero falso", del panel de
	 * elegir pregunta a editar.
	 */
	private class botonVerdaderoFalsopanelEditarPreguntasTipoPreguntas_Listener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (examenAlumno != null) {
				if (examenAlumno.preguntas_VF.size() > 0) {
					TIPOPREGUNTA = VERDADEROFALSO;
					TABACTUAL = 3;
					actualizarPanelTab(panelEditarPreguntasEleccionPreguntas(), 3);
				} else {
					Mensajes.error("¡No hay preguntas de tipo verdadero / falso para eliminar!");
					actualizarPanelTab(panelEditarPreguntasTipoPreguntas(), 3);
				}
			} else {
				Mensajes.error("¡Si ves este mensaje, por favor reporta al creador del programa este problema!");
			}

		}
	}

	/**
	 * Esta clase, es el actionlistener del boton finalizar, del panel de elegir
	 * pregunta a editar.
	 */
	private class botonFinalizarpanelEditarPreguntasTipoPreguntas_Listener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			if (examenAlumno != null && (examenAlumno.preguntas_SM.size() > 0 || examenAlumno.preguntas_VF.size() > 0
					|| examenAlumno.preguntas_RC.size() > 0)) {
				PANELACTUAL = EDITAREXAMEN;
				actualizarPanelTab(panelOpcionesGuardarExamenEnArchivoSMR(), 3);
			} else {
				Mensajes.error("¡No se puede guardar un examen sin preguntas!, por favor deje como minimo 1 pregunta.");
				actualizarPanelTab(panelEditarPreguntas(), 3);
			}

		}
	}

	/**
	 * Este panel muestra las preguntas, ademas permite eliminar muchas preguntas al
	 * mismo tiempo.
	 * 
	 * @return retorna el panel generado.
	 */
	private JPanel panelEditarPreguntasEleccionPreguntas() {

		JPanel Panel = new JPanel(new BorderLayout()) {

			private static final long serialVersionUID = -759781485494659289L;

			@Override
			protected void paintComponent(Graphics g) {
				BufferedImage img = ImagenesAleatorias.getImagen();
				if (img != null) {
					super.paintComponent(g);
					g.drawImage(img, 0, 0, 1000, 700, 0, 0, img.getWidth(), img.getHeight(), this);
				} else {
					super.paintComponent(g);
					g.setColor(Color.decode("#C9C9C9"));
				}
			}
		};
		Panel.setBackground(Color.decode("#C9C9C9"));
		Panel.setBorder(new EmptyBorder(new Insets(60, 100, 60, 100)));

		JPanel panel = new JPanel(new BorderLayout(0, 10));
		panel.setBorder(new EmptyBorder(new Insets(20, 40, 20, 40)));
		Panel.add(panel, BorderLayout.CENTER);

		// titulo

		JLabel lblPreguntas = new JLabel(
				"<html><p align = \"center\">Ahora seleccione cuales preguntas desea eliminar:</p></html>");
		lblPreguntas.setFont(new Font("Segoe UI", Font.PLAIN, 25));
		panel.add(lblPreguntas, BorderLayout.NORTH);

		// panel central

		JPanel panelCentral = new JPanel(new BorderLayout());
		panel.add(panelCentral, BorderLayout.CENTER);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.getViewport().setViewPosition(new Point(0, 0));
		panelCentral.add(scrollPane, BorderLayout.CENTER);

		JPanel panelDelScrollPane = new JPanel();
		panelDelScrollPane.setLayout(new BoxLayout(panelDelScrollPane, BoxLayout.Y_AXIS));
		scrollPane.setViewportView(panelDelScrollPane);

		botonesEditarPreguntas = new ArrayList<crearCheckTextBotones>();

		preguntasAEliminar = new HashMap<String, Boolean>();

		if (examenAlumno != null) {
			if (TIPOPREGUNTA.equals(SELECCIONMULTIPLE)) {

				for (int i = 0; i < examenAlumno.preguntas_SM.size(); i++) {

					SeleccionMultiple pregunta = examenAlumno.preguntas_SM.get(i);

					String preguntaDelExamen = pregunta.getPregunta();

					preguntasAEliminar.put(preguntaDelExamen, false);

					botonesEditarPreguntas.add(
							new crearCheckTextBotones("ver pregunta completa", "editar pregunta", preguntaDelExamen));

					botonesEditarPreguntas.get(i).boton.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							JOptionPane.showMessageDialog(new JFrame(), preguntaDelExamen);
						}
					});

					botonesEditarPreguntas.get(i).boton2.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							SeleccionMultiple preguntaEditada = Mensajes.editarPreguntaSeleccionMultiple(pregunta, "");

							if (preguntaEditada != null && !pregunta.equals(preguntaEditada)) {
								examenAlumno.preguntas_SM.set(examenAlumno.preguntas_SM.indexOf(pregunta),
										preguntaEditada);
								CAMBIOHECHO = true;
								actualizarPanelTab(panelEditarPreguntasEleccionPreguntas(), TABACTUAL);
							}
						}
					});

					botonesEditarPreguntas.get(i).check.addItemListener(new ItemListener() {

						@Override
						public void itemStateChanged(ItemEvent e) {

							boolean estadoCheckbox = e.getStateChange() == ItemEvent.SELECTED;

							if (estadoCheckbox) {
								preguntasAEliminar.put(preguntaDelExamen, true);
							} else {
								preguntasAEliminar.put(preguntaDelExamen, false);
							}
						}
					});

					panelDelScrollPane.add(botonesEditarPreguntas.get(i));
				}
			}

			if (TIPOPREGUNTA.equals(VERDADEROFALSO)) {

				for (int i = 0; i < examenAlumno.preguntas_VF.size(); i++) {

					VerdaderoFalso pregunta = examenAlumno.preguntas_VF.get(i);

					String preguntaDelExamen = examenAlumno.preguntas_VF.get(i).getPregunta();

					preguntasAEliminar.put(preguntaDelExamen, false);

					botonesEditarPreguntas.add(
							new crearCheckTextBotones("ver pregunta completa", "editar pregunta", preguntaDelExamen));

					botonesEditarPreguntas.get(i).boton.addActionListener(new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent e) {
							JOptionPane.showMessageDialog(null, preguntaDelExamen);
						}
					});

					botonesEditarPreguntas.get(i).boton2.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							VerdaderoFalso preguntaEditada = Mensajes.editarPreguntaVerdaderoFalso(pregunta);

							if (preguntaEditada != null && !pregunta.equals(preguntaEditada)) {
								examenAlumno.preguntas_VF.set(examenAlumno.preguntas_VF.indexOf(pregunta),
										preguntaEditada);

								CAMBIOHECHO = true;
								actualizarPanelTab(panelEditarPreguntasEleccionPreguntas(), TABACTUAL);
							}
						}
					});

					botonesEditarPreguntas.get(i).check.addItemListener(new ItemListener() {

						@Override
						public void itemStateChanged(ItemEvent e) {

							boolean estadoCheckbox = e.getStateChange() == ItemEvent.SELECTED;

							if (estadoCheckbox) {
								preguntasAEliminar.put(preguntaDelExamen, true);
							} else {
								preguntasAEliminar.put(preguntaDelExamen, false);
							}
						}
					});

					panelDelScrollPane.add(botonesEditarPreguntas.get(i));
				}
			}

			if (TIPOPREGUNTA.equals(RESPUESTACORTA)) {

				for (int i = 0; i < examenAlumno.preguntas_RC.size(); i++) {

					RespuestasCortas pregunta = examenAlumno.preguntas_RC.get(i);

					String preguntaDelExamen = examenAlumno.preguntas_RC.get(i).getPregunta();

					preguntasAEliminar.put(preguntaDelExamen, false);
					botonesEditarPreguntas.add(
							new crearCheckTextBotones("ver pregunta completa", "editar pregunta", preguntaDelExamen));

					botonesEditarPreguntas.get(i).boton.addActionListener(new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent e) {
							JOptionPane.showMessageDialog(null, preguntaDelExamen);
						}
					});

					botonesEditarPreguntas.get(i).boton2.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							RespuestasCortas preguntaEditada = Mensajes.editarPreguntaRespuestasCortas(pregunta, "");

							if (preguntaEditada != null && !pregunta.equals(preguntaEditada)) {
								examenAlumno.preguntas_RC.set(examenAlumno.preguntas_RC.indexOf(pregunta),
										preguntaEditada);

								CAMBIOHECHO = true;
								actualizarPanelTab(panelEditarPreguntasEleccionPreguntas(), TABACTUAL);
							}
						}
					});

					botonesEditarPreguntas.get(i).check.addItemListener(new ItemListener() {

						@Override
						public void itemStateChanged(ItemEvent e) {

							boolean estadoCheckbox = e.getStateChange() == ItemEvent.SELECTED;

							preguntasAEliminar.put(preguntaDelExamen, (estadoCheckbox) ? true : false);
						}
					});

					panelDelScrollPane.add(botonesEditarPreguntas.get(i));
				}
			}

			if (TIPOPREGUNTA.equals(RESPUESTANUMERICA)) {

				for (int i = 0; i < examenAlumno.preguntas_RM.size(); i++) {

					RespuestaNumerica pregunta = examenAlumno.preguntas_RM.get(i);

					String preguntaDelExamen = examenAlumno.preguntas_RM.get(i).getPregunta();

					preguntasAEliminar.put(preguntaDelExamen, false);
					botonesEditarPreguntas.add(
							new crearCheckTextBotones("ver pregunta completa", "editar pregunta", preguntaDelExamen));

					botonesEditarPreguntas.get(i).boton.addActionListener(new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent e) {
							JOptionPane.showMessageDialog(null, preguntaDelExamen);
						}
					});

					botonesEditarPreguntas.get(i).boton2.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							RespuestaNumerica preguntaEditada = Mensajes.editarPreguntaRespuestasNumericas(pregunta,
									"");

							if (preguntaEditada != null && !pregunta.equals(preguntaEditada)) {
								examenAlumno.preguntas_RM.set(examenAlumno.preguntas_RM.indexOf(pregunta),
										preguntaEditada);

								CAMBIOHECHO = true;
								actualizarPanelTab(panelEditarPreguntasEleccionPreguntas(), TABACTUAL);
							}
						}
					});

					botonesEditarPreguntas.get(i).check.addItemListener(new ItemListener() {

						@Override
						public void itemStateChanged(ItemEvent e) {

							boolean estadoCheckbox = e.getStateChange() == ItemEvent.SELECTED;

							preguntasAEliminar.put(preguntaDelExamen, (estadoCheckbox) ? true : false);
						}
					});

					panelDelScrollPane.add(botonesEditarPreguntas.get(i));
				}
			}
		} else {
			crearCheckTextBotones mensajeError = new crearCheckTextBotones("error", "error",
					"Error, por favor intente volver al panel anterior.");
			panelDelScrollPane.add(mensajeError);
		}

		// panel inferior

		JPanel panelInferior = new JPanel(new BorderLayout());
		panel.add(panelInferior, BorderLayout.SOUTH);

		JButton botonCancelarpanelEditarPreguntasEleccionPreguntas = new JButton("Volver");
		botonCancelarpanelEditarPreguntasEleccionPreguntas.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		botonCancelarpanelEditarPreguntasEleccionPreguntas
				.addActionListener(new botonCancelarpanelEditarPreguntasEleccionPreguntas_Listener());
		botonCancelarpanelEditarPreguntasEleccionPreguntas.setBackground(Color.decode("#00668C"));
		panelInferior.add(botonCancelarpanelEditarPreguntasEleccionPreguntas, BorderLayout.WEST);

		if (examenAlumno != null) {
			scrollPane.setPreferredSize(
					new Dimension(panelCentral.getPreferredSize().width, panelCentral.getPreferredSize().height / 3));

			JPanel panelSeleccionarTodos = new JPanel(new FlowLayout(FlowLayout.CENTER));
			panelInferior.add(panelSeleccionarTodos, BorderLayout.CENTER);

			JButton btnSeleccionarTodos = new JButton("Seleccionar Todos");
			btnSeleccionarTodos.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {

					if (btnSeleccionarTodos.getText().equals("Deseleccionar todos")) {
						btnSeleccionarTodos.setText("Seleccionar Todos");
						for (crearCheckTextBotones boton : botonesEditarPreguntas) {
							boton.check.setSelected(false);
						}
					} else {
						btnSeleccionarTodos.setText("Deseleccionar todos");
						for (crearCheckTextBotones boton : botonesEditarPreguntas) {
							boton.check.setSelected(true);
						}
					}
					panelDelScrollPane.updateUI();
				}
			});
			panelSeleccionarTodos.add(btnSeleccionarTodos);
			// btnSeleccionarTodos.setText("Seleccionar Todos");

			JButton botonEliminarPanelEditarPreguntasEleccionPreguntas = new JButton("Eliminar");
			botonEliminarPanelEditarPreguntasEleccionPreguntas.setFont(new Font("Segoe UI", Font.PLAIN, 18));
			botonEliminarPanelEditarPreguntasEleccionPreguntas
					.addActionListener(new botonEliminarPanelEditarPreguntasEleccionPreguntas_Listener());
			botonEliminarPanelEditarPreguntasEleccionPreguntas.setBackground(Color.decode("#0077ff"));
			panelInferior.add(botonEliminarPanelEditarPreguntasEleccionPreguntas, BorderLayout.EAST);
		}

		return Panel;

	}

	/**
	 * Esta clase, es el actionlistener del boton eliminar preguntas, del panel de
	 * eleccion de preguntas a editar/eliminar.
	 */
	private class botonEliminarPanelEditarPreguntasEleccionPreguntas_Listener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (examenAlumno != null) {
				if (TIPOPREGUNTA.equals(SELECCIONMULTIPLE)) {
					if (examenAlumno.preguntas_SM != null && examenAlumno.preguntas_SM.size() > 0) {

						List<SeleccionMultiple> temp = new ArrayList<SeleccionMultiple>(examenAlumno.preguntas_SM);

						for (SeleccionMultiple preguntasSM : examenAlumno.preguntas_SM) {
							if (preguntasAEliminar.get(preguntasSM.getPregunta())) {
								temp.remove(temp.indexOf(preguntasSM));
								CAMBIOHECHO = true;
							}
						}

						examenAlumno.preguntas_SM = temp;

						if (PANELACTUAL.equals(CREARPREGUNTAS)) {
							actualizarPanelTab(panelEditarPreguntasEleccionPreguntas(), 2);
						} else if (PANELACTUAL.equals(EDITAREXAMEN)) {
							actualizarPanelTab(panelEditarPreguntasEleccionPreguntas(), 3);
						} else {
							Mensajes.error("¡Por favor reporta este error con el administrador!");
							return;
						}

						if (examenAlumno.preguntas_SM.size() < 1) {
							Mensajes.error("¡No hay mas preguntas para eliminar!");
							if (PANELACTUAL.equals(CREARPREGUNTAS)) {
								actualizarPanelTab(panelCrearPreguntasTiposPreguntas(), 2);
							} else if (PANELACTUAL.equals(EDITAREXAMEN)) {
								actualizarPanelTab(panelEditarPreguntasTipoPreguntas(), 3);
							} else {
								Mensajes.error("¡Por favor reporta este error con el administrador!");
								return;
							}
						}
					} else {
						Mensajes.error("¡No hay mas preguntas para eliminar!");
						if (PANELACTUAL.equals(CREARPREGUNTAS)) {
							actualizarPanelTab(panelCrearPreguntasTiposPreguntas(), 2);
						} else if (PANELACTUAL.equals(EDITAREXAMEN)) {
							actualizarPanelTab(panelEditarPreguntasTipoPreguntas(), 3);
						} else {
							Mensajes.error("¡Por favor reporta este error con el administrador!");
							return;
						}
					}
				}

				if (TIPOPREGUNTA.equals(VERDADEROFALSO)) {
					if (examenAlumno.preguntas_VF != null && examenAlumno.preguntas_VF.size() > 0) {

						List<VerdaderoFalso> temp = new ArrayList<VerdaderoFalso>(examenAlumno.preguntas_VF);

						for (VerdaderoFalso preguntasVF : examenAlumno.preguntas_VF) {
							if (preguntasAEliminar.get(preguntasVF.getPregunta())) {
								temp.remove(temp.indexOf(preguntasVF));
								CAMBIOHECHO = true;
							}
						}

						examenAlumno.preguntas_VF = temp;

						if (PANELACTUAL.equals(CREARPREGUNTAS)) {
							actualizarPanelTab(panelEditarPreguntasEleccionPreguntas(), 2);
						} else if (PANELACTUAL.equals(EDITAREXAMEN)) {
							actualizarPanelTab(panelEditarPreguntasEleccionPreguntas(), 3);
						} else {
							Mensajes.error("¡Por favor reporta este error con el administrador!");
							return;
						}

						if (examenAlumno.preguntas_VF.size() < 1) {
							Mensajes.error("¡No hay mas preguntas para eliminar!");
							if (PANELACTUAL.equals(CREARPREGUNTAS)) {
								actualizarPanelTab(panelCrearPreguntasTiposPreguntas(), 2);
							} else if (PANELACTUAL.equals(EDITAREXAMEN)) {
								actualizarPanelTab(panelEditarPreguntasTipoPreguntas(), 3);
							} else {
								Mensajes.error("¡Por favor reporta este error con el administrador!");
								return;
							}
						}

					} else {
						Mensajes.error("¡No hay mas preguntas para eliminar!");
						if (PANELACTUAL.equals(CREARPREGUNTAS)) {
							actualizarPanelTab(panelCrearPreguntasTiposPreguntas(), 2);
						} else if (PANELACTUAL.equals(EDITAREXAMEN)) {
							actualizarPanelTab(panelEditarPreguntasTipoPreguntas(), 3);
						} else {
							Mensajes.error("¡Por favor reporta este error con el administrador!");
							return;
						}
					}
				}

				if (TIPOPREGUNTA.equals(RESPUESTACORTA)) {
					if (examenAlumno.preguntas_RC != null && examenAlumno.preguntas_RC.size() > 0) {

						List<RespuestasCortas> temp = new ArrayList<RespuestasCortas>(examenAlumno.preguntas_RC);

						for (RespuestasCortas preguntasRC : examenAlumno.preguntas_RC) {
							if (preguntasAEliminar.get(preguntasRC.getPregunta())) {
								temp.remove(temp.indexOf(preguntasRC));
								CAMBIOHECHO = true;
							}
						}

						examenAlumno.preguntas_RC = temp;

						if (PANELACTUAL.equals(CREARPREGUNTAS)) {
							actualizarPanelTab(panelEditarPreguntasEleccionPreguntas(), 2);
						} else if (PANELACTUAL.equals(EDITAREXAMEN)) {
							actualizarPanelTab(panelEditarPreguntasEleccionPreguntas(), 3);
						} else {
							Mensajes.error("¡Por favor reporta este error con el administrador!");
							return;
						}

						if (examenAlumno.preguntas_RC.size() < 1) {
							Mensajes.error("¡No hay mas preguntas para eliminar!");
							if (PANELACTUAL.equals(CREARPREGUNTAS)) {
								actualizarPanelTab(panelCrearPreguntasTiposPreguntas(), 2);
							} else if (PANELACTUAL.equals(EDITAREXAMEN)) {
								actualizarPanelTab(panelEditarPreguntasTipoPreguntas(), 3);
							} else {
								Mensajes.error("¡Por favor reporta este error con el administrador!");
								return;
							}
						}

					} else {
						Mensajes.error("¡No hay mas preguntas para eliminar!");
						if (PANELACTUAL.equals(CREARPREGUNTAS)) {
							actualizarPanelTab(panelCrearPreguntasTiposPreguntas(), 2);
						} else if (PANELACTUAL.equals(EDITAREXAMEN)) {
							actualizarPanelTab(panelEditarPreguntasTipoPreguntas(), 3);
						} else {
							Mensajes.error("¡Por favor reporta este error con el administrador!");
							return;
						}
					}
				}

				if (TIPOPREGUNTA.equals(RESPUESTANUMERICA)) {
					if (examenAlumno.preguntas_RC != null && examenAlumno.preguntas_RM.size() > 0) {

						List<RespuestaNumerica> temp = new ArrayList<RespuestaNumerica>(examenAlumno.preguntas_RM);

						for (RespuestaNumerica preguntasRM : examenAlumno.preguntas_RM) {
							if (preguntasAEliminar.get(preguntasRM.getPregunta())) {
								temp.remove(temp.indexOf(preguntasRM));
								CAMBIOHECHO = true;
							}
						}

						examenAlumno.preguntas_RM = temp;

						if (PANELACTUAL.equals(CREARPREGUNTAS)) {
							actualizarPanelTab(panelEditarPreguntasEleccionPreguntas(), 2);
						} else if (PANELACTUAL.equals(EDITAREXAMEN)) {
							actualizarPanelTab(panelEditarPreguntasEleccionPreguntas(), 3);
						} else {
							Mensajes.error("¡Por favor reporta este error con el administrador!");
							return;
						}

						if (examenAlumno.preguntas_RM.size() < 1) {
							Mensajes.error("¡No hay mas preguntas para eliminar!");
							if (PANELACTUAL.equals(CREARPREGUNTAS)) {
								actualizarPanelTab(panelCrearPreguntasTiposPreguntas(), 2);
							} else if (PANELACTUAL.equals(EDITAREXAMEN)) {
								actualizarPanelTab(panelEditarPreguntasTipoPreguntas(), 3);
							} else {
								Mensajes.error("¡Por favor reporta este error con el administrador!");
								return;
							}
						}

					} else {
						Mensajes.error("¡No hay mas preguntas para eliminar!");
						if (PANELACTUAL.equals(CREARPREGUNTAS)) {
							actualizarPanelTab(panelCrearPreguntasTiposPreguntas(), 2);
						} else if (PANELACTUAL.equals(EDITAREXAMEN)) {
							actualizarPanelTab(panelEditarPreguntasTipoPreguntas(), 3);
						} else {
							Mensajes.error("¡Por favor reporta este error con el administrador!");
							return;
						}
					}
				}
			} else {
				Mensajes.error("¡No puede eliminar preguntas de un examen sin preguntas!");
				return;
			}
		}
	}

	/**
	 * Esta clase, es el actionlistener del boton cancelar, del panel de eleccion de
	 * preguntas a editar/eliminar.
	 */
	private class botonCancelarpanelEditarPreguntasEleccionPreguntas_Listener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (PANELACTUAL.equals(CREARPREGUNTAS)) {
				actualizarPanelTab(panelCrearPreguntasTiposPreguntas(), 2);
			} else if (PANELACTUAL.equals(EDITAREXAMEN)) {
				actualizarPanelTab(panelEditarPreguntasTipoPreguntas(), 3);
			} else {
				Mensajes.error("¡Por favor reporta este error con el administrador!");
				return;
			}
		}
	}

	// endregion

	// region de paneles y clases de cerrar sesión.

	/**
	 * Este panel consulta si desea finalizar la sesión.
	 * 
	 * @return retorna el panel generado.
	 */
	private JPanel panelCerrarSesion() {

		JPanel Panel = new JPanel(new BorderLayout()) {

			private static final long serialVersionUID = -7246528761026883629L;

			@Override
			protected void paintComponent(Graphics g) {
				BufferedImage img = ImagenesAleatorias.getImagen();
				if (img != null) {
					super.paintComponent(g);
					g.drawImage(img, 0, 0, 1000, 700, 0, 0, img.getWidth(), img.getHeight(), this);
				} else {
					super.paintComponent(g);
					g.setColor(Color.decode("#807E7D"));
				}
			}
		};
		Panel.setBackground(Color.decode("#807E7D"));
		Panel.setBorder(new EmptyBorder(new Insets(180, 200, 180, 200)));

		JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder(new EmptyBorder(new Insets(50, 40, 50, 40)));
		Panel.add(panel, BorderLayout.CENTER);

		JLabel lblPregunta = new JLabel("<html><p align = \"center\">¿Estas segur@ de cerrar la sesión?</p></html>");
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

				actualizarPanelTab(panelCerrarSesionMensaje(), 5);

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
	 * Este panel muestra un mensaje de despedida al finalizar la sesión.
	 * 
	 * @return retorna el panel generado.
	 */
	private JPanel panelCerrarSesionMensaje() {

		JPanel Panel = new JPanel(new BorderLayout()) {

			private static final long serialVersionUID = 7791750487795259899L;

			@Override
			protected void paintComponent(Graphics g) {
				BufferedImage img = ImagenesAleatorias.getImagen();
				if (img != null) {
					super.paintComponent(g);
					g.drawImage(img, 0, 0, 1000, 700, 0, 0, img.getWidth(), img.getHeight(), this);
				} else {
					super.paintComponent(g);
					g.setColor(Color.decode("#807E7D"));
				}
			}
		};
		Panel.setBackground(Color.decode("#807E7D"));
		Panel.setBorder(new EmptyBorder(new Insets(180, 200, 180, 200)));

		JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder(new EmptyBorder(new Insets(50, 40, 50, 40)));
		Panel.add(panel, BorderLayout.CENTER);

		JLabel lblDespedida = new JLabel("¡Hasta luego!");
		lblDespedida.setFont(new Font("Segoe UI", Font.BOLD, 29));
		lblDespedida.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(lblDespedida, BorderLayout.CENTER);

		tabActivado(false);

		return Panel;

	}

	// endregion

	// region opciones para guardar archivos.

	/**
	 * Este panel consulta que opciones desea agregar al examen de tipo smr.
	 * 
	 * @return retorna el panel generado.
	 */
	private JPanel panelOpcionesGuardarExamenEnArchivoSMR() {

		JPanel Panel = new JPanel(new BorderLayout()) {

			private static final long serialVersionUID = -5224577721056506383L;

			@Override
			protected void paintComponent(Graphics g) {
				BufferedImage img = ImagenesAleatorias.getImagen();
				if (img != null) {
					super.paintComponent(g);
					g.drawImage(img, 0, 0, 1000, 700, 0, 0, img.getWidth(), img.getHeight(), this);
				} else {
					super.paintComponent(g);
					g.setColor(Color.decode("#C9C9C9"));
				}
			}
		};
		Panel.setBackground(Color.decode("#C9C9C9"));
		Panel.setBorder(new EmptyBorder(new Insets(40, 98, 40, 98)));

		JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder(new EmptyBorder(new Insets(20, 20, 20, 20)));
		Panel.add(panel, BorderLayout.CENTER);

		// parte del norte

		JLabel lblTitulo = new JLabel(
				"<html><p align = \"center\">Para ir finalizando, necesito que respondas las siguientes preguntas:</p></html>");
		lblTitulo.setFont(new Font("Segoe UI", Font.PLAIN, 25));
		panel.add(lblTitulo, BorderLayout.PAGE_START);

		// parte del centro

		JPanel panelCentral = new JPanel();
		panel.add(panelCentral);
		panelCentral.setLayout(new BoxLayout(panelCentral, BoxLayout.X_AXIS));

		// panelcentralizquierdo contiene las labels

		JPanel panelCentralIzquierdo = new JPanel(new GridLayout(0, 1, 0, 20));
		panelCentral.add(panelCentralIzquierdo);
		panelCentralIzquierdo.setBorder(new EmptyBorder(20, 0, 20, 0));

		JLabel lblDesordenarPreguntas = new JLabel("¿Deseas desordenar los ítems y preguntas?");
		lblDesordenarPreguntas.setHorizontalAlignment(SwingConstants.CENTER);
		lblDesordenarPreguntas.setFont(new Font("Segoe UI", Font.PLAIN, 17));
		panelCentralIzquierdo.add(lblDesordenarPreguntas);

		JLabel lblMostrarRespuestas = new JLabel("¿Deseas mostrar la respuesta si el alumno se equivoca?");
		lblMostrarRespuestas.setHorizontalAlignment(SwingConstants.CENTER);
		lblMostrarRespuestas.setFont(new Font("Segoe UI", Font.PLAIN, 17));
		panelCentralIzquierdo.add(lblMostrarRespuestas);

		JLabel lblTiempoLimiteExamen = new JLabel("¿Deseas fijar un límite de tiempo para el examen?");
		lblTiempoLimiteExamen.setHorizontalAlignment(SwingConstants.CENTER);
		lblTiempoLimiteExamen.setFont(new Font("Segoe UI", Font.PLAIN, 17));
		panelCentralIzquierdo.add(lblTiempoLimiteExamen);

		JLabel lblContrasenaExamen = new JLabel("¿Deseas establecer una contraseña para el examen?");
		lblContrasenaExamen.setHorizontalAlignment(SwingConstants.CENTER);
		lblContrasenaExamen.setFont(new Font("Segoe UI", Font.PLAIN, 17));
		panelCentralIzquierdo.add(lblContrasenaExamen);

		// panelcentralderecho contiene los botones, switches y labels

		JPanel panelCentralDerecho = new JPanel(new GridLayout(0, 1, 10, 12));
		panelCentralDerecho.setBorder(new EmptyBorder(20, 20, 20, 20));
		panelCentral.add(panelCentralDerecho, BorderLayout.CENTER);

		JToggleButton tglbtnDesordenarPreguntas = new JToggleButton("Desactivado");
		tglbtnDesordenarPreguntas.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent itemEvent) {
				int estado = itemEvent.getStateChange();
				if (estado == ItemEvent.SELECTED) {
					tglbtnDesordenarPreguntas.setText("Activado");
					desordenarPreguntas = true;
				} else {
					tglbtnDesordenarPreguntas.setText("Desactivado");
					desordenarPreguntas = false;
				}
			}
		});
		panelCentralDerecho.add(tglbtnDesordenarPreguntas);

		JToggleButton tglbtnMostrarRespuestas = new JToggleButton("Desactivado");
		tglbtnMostrarRespuestas.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent itemEvent) {
				int estado = itemEvent.getStateChange();
				if (estado == ItemEvent.SELECTED) {
					tglbtnMostrarRespuestas.setText("Activado");
					mostrarRespuestas = true;
				} else {
					tglbtnMostrarRespuestas.setText("Desactivado");
					mostrarRespuestas = false;
				}
			}
		});
		panelCentralDerecho.add(tglbtnMostrarRespuestas);

		JToggleButton tglbtnTiempoLimite = new JToggleButton("Desactivado");
		tglbtnTiempoLimite.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent itemEvent) {
				int estado = itemEvent.getStateChange();
				if (estado == ItemEvent.SELECTED) {
					JTextField fieldTiempoMinutosExamen = new JTextField(10);
					fieldTiempoMinutosExamen.setTransferHandler(null);
					fieldTiempoMinutosExamen.addKeyListener(new KeyAdapter() {
						@Override
						public void keyTyped(KeyEvent e) {
							int cantidadMaximaCaracteres = 5;

							char letraIngresada = e.getKeyChar();

							if (!((letraIngresada >= '0') && (letraIngresada <= '9')
									|| (letraIngresada == KeyEvent.VK_BACK_SPACE)
									|| (letraIngresada == KeyEvent.VK_DELETE))) {
								e.consume();
							}

							if ((fieldTiempoMinutosExamen.getText().isBlank()) && (letraIngresada == '0')) {
								e.consume();
							}

							if (fieldTiempoMinutosExamen.getText().length() > cantidadMaximaCaracteres) {
								e.consume();
							}
						}
					});

					tiempoMinutosExamen = Mensajes.consultarTextField("Ingrese el total de minutos:",
							"Por favor ingrese numeros entre 1 y 10080 (7 días).", "", tglbtnTiempoLimite,
							fieldTiempoMinutosExamen);

					tiempoLimite = (tiempoMinutosExamen != null && !tiempoMinutosExamen.isBlank());
				} else {
					tiempoMinutosExamen = "";
					tiempoLimite = false;
					tglbtnTiempoLimite.setText("Desactivado");
				}
				panel.revalidate();
				panel.repaint();
			}
		});
		panelCentralDerecho.add(tglbtnTiempoLimite);

		JToggleButton tglbtnContrasena = new JToggleButton("Desactivado");
		tglbtnContrasena.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent itemEvent) {
				int estado = itemEvent.getStateChange();
				if (estado == ItemEvent.SELECTED) {
					JTextField contrasenaExamenField = new JTextField(10);
					contrasenaExamenField.setTransferHandler(null);
					contrasenaEstablecida = true;
					contrasenaExamen = Mensajes.consultarTextField("Ingrese la contraseña del examen:",
							"Por favor ingrese una contraseña.", "", tglbtnContrasena, contrasenaExamenField);
					contrasenaEstablecida = (contrasenaExamen != null && contrasenaExamen.isBlank());
				} else {
					contrasenaExamen = "";
					contrasenaEstablecida = false;
					tglbtnContrasena.setText("Desactivado");
				}
				panel.revalidate();
				panel.repaint();
			}
		});
		panelCentralDerecho.add(tglbtnContrasena);

		// parte del sur

		JPanel panelBotonesInferiores = new JPanel(new BorderLayout());
		panel.add(panelBotonesInferiores, BorderLayout.SOUTH);

		JButton botonVolverPanelOpcionesGuardarExamenEnArchivo = new JButton("Volver");
		botonVolverPanelOpcionesGuardarExamenEnArchivo.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		botonVolverPanelOpcionesGuardarExamenEnArchivo.setBackground(Color.decode("#00668C"));
		botonVolverPanelOpcionesGuardarExamenEnArchivo.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (PANELACTUAL.equals(CREARPREGUNTAS)) {
					actualizarPanelTab(panelCrearPreguntasTiposPreguntas(), 2);

				} else /* if (PANELACTUAL.equals(EDITAREXAMEN)) */ {
					actualizarPanelTab(panelEditarPreguntas(), 3);
				}
			}
		});

		panelBotonesInferiores.add(botonVolverPanelOpcionesGuardarExamenEnArchivo, BorderLayout.WEST);

		JButton botonFinalizarPanelOpcionesGuardarExamenEnArchivo = new JButton("Finalizar");
		botonFinalizarPanelOpcionesGuardarExamenEnArchivo.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		botonFinalizarPanelOpcionesGuardarExamenEnArchivo.addActionListener(new guardarExamenEnArchivoSMR());
		botonFinalizarPanelOpcionesGuardarExamenEnArchivo.setBackground(Color.decode("#0077ff"));
		panelBotonesInferiores.add(botonFinalizarPanelOpcionesGuardarExamenEnArchivo, BorderLayout.EAST);

		return Panel;
	}

	/**
	 * Esta clase, es el actionlistener de los botones guardar, de los paneles que
	 * modifican o crean archivos smr.
	 */
	private class guardarExamenEnArchivoSMR implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (examenAlumno != null) {
				try {
					String texto = "";

					if (desordenarPreguntas) {
						texto += "-\n";
					}

					if (mostrarRespuestas) {
						texto += "+\n";
					}

					if (tiempoLimite && !tiempoMinutosExamen.isBlank()) {
						tiempo = Integer.parseInt(tiempoMinutosExamen);
						if (tiempo > 0 && tiempo < 100000) {
							texto += "^" + tiempo + "\n";
						} else {
							Mensajes.error("Por favor ingrese algun valor entre 1 y 99999");
							return;
						}
					} else if (tiempoLimite && tiempoMinutosExamen.isBlank()) {
						Mensajes.error("Por favor ingrese algun valor entre 1 y 99999");
						return;
					}

					if (contrasenaEstablecida && (contrasenaExamen == null || contrasenaExamen.isBlank())) {
						Mensajes.error("Por favor ingrese una contraseña.");
						return;
					}

					// SIMBOLOGIA:

					// #{tipo_pregunta} {pregunta} = pregunta + el tipo de pregunta.
					// @{numero opciones} {opciones} = opciones de la pregunta (si es que las
					// tiene).
					// ~{respuesta} = respuesta correcta.
					// &{peso} = peso de la pregunta.
					// - = desordenar preguntas al responder el examen.
					// + = permitir al estudiante saber la respuesta despues de equivocarse.
					// ^{tiempo} = tiempo limite de desarrollo que tiene el alumno para resolver el
					// examen.
					// %{token md5} = token para diferenciar entre los examenes generados.
					// !{llave publica} = llave con la cual se encriptara los resultados de los
					// alumnos.

					// escribo las preguntas con sus respuestas en el archivo

					String nombreExamen = "";
					String rutaExamen = "";

					JFileChooser Archivo = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
					Archivo.setFileFilter(new FileNameExtensionFilter("Archivos .smr", "smr"));
					Archivo.setAcceptAllFileFilterUsed(false);
					int returnVal = Archivo.showSaveDialog(new JFrame());

					if (returnVal == JFileChooser.APPROVE_OPTION) {
						/*-
						 [] = inicio y fin del regex.
						 :*<>\/|?" = caracteres a verificar.
						 + = para todo el string.
						 
						 Pattern.compile("[:*<>\\/|?\"]+") es una clase la cual compila el regex que se le da.
						 .matcher(String).find() estas funciones se encargan de verificar si el string encaja 
						 						 dentro de las caracteristicas del regex.
						 */

						if (Archivo.getSelectedFile() == null) {
							Mensajes.error(
									"<html><p align = \"center\">Por favor, ingrese un nombre de archivo valido.</p></html>");
							return;
						}

						if (Pattern.compile("[:*<>\\/|?\"]+").matcher(Archivo.getSelectedFile().getName()).find()) {
							Mensajes.error(
									"<html><p align = \"center\">Por favor, no use caracteres ilegales en el nombre de su archivo.<br>(< > : \\ / | ? *)<br>Inténtelo nuevamente.</p></html>");
							return;
						}
						rutaExamen = Archivo.getSelectedFile().getAbsolutePath();
						nombreExamen = Archivo.getSelectedFile().getName() + ".smr";
					} else {
						return;
					}

					// genero el identificador aleatorio del examen.
					String tokenExamen = MD5.generarTokenExamen("random", "");
					texto += "%" + tokenExamen + "\n";

					StringBuilder respuestasSM = new StringBuilder();

					if (examenAlumno.verificarExamen(1)) {// preguntas seleccion multiple
						for (int i = 0; i < examenAlumno.preguntas_SM.size(); i++) {

							texto += "#1" + BASE64.ProtegerTextoBase64(examenAlumno.preguntas_SM.get(i).getPregunta())
									+ "\n";
							texto += "@" + (examenAlumno.preguntas_SM.get(i).getOpciones().length) + " ";
							for (int k = 0; k < examenAlumno.preguntas_SM.get(i).getOpciones().length; k++) {
								texto += BASE64.ProtegerTextoBase64(examenAlumno.preguntas_SM.get(i).getOpciones()[k])
										+ " ";
							}
							texto += "\n~1"
									+ BASE64.ProtegerTextoBase64(examenAlumno.preguntas_SM.get(i).getRespuesta())
									+ "\n";
							texto += "&1" + examenAlumno.preguntas_SM.get(i).getPeso() + "\n";
							respuestasSM.append(" " + BASE64.ProtegerTextoBase64(examenAlumno.preguntas_SM.get(i)
									.getOpciones()[examenAlumno.preguntas_SM.get(i).getRespuesta()]));
						}
					}

					StringBuilder respuestasVF = new StringBuilder();
					if (examenAlumno.verificarExamen(2)) {// preguntas verdadero falso
						for (int i = 0; i < examenAlumno.preguntas_VF.size(); i++) {

							texto += "#2" + BASE64.ProtegerTextoBase64(examenAlumno.preguntas_VF.get(i).getPregunta())
									+ "\n";
							texto += "~2" + BASE64.ProtegerTextoBase64(examenAlumno.preguntas_VF.get(i).getRespuesta())
									+ "\n";
							texto += "&2" + examenAlumno.preguntas_VF.get(i).getPeso() + "\n";

							respuestasVF.append(" " + BASE64.ProtegerTextoBase64(
									(examenAlumno.preguntas_VF.get(i).getRespuesta() ? "Verdadero" : "Falso")));
						}
					}

					StringBuilder respuestasRC = new StringBuilder();
					if (examenAlumno.verificarExamen(3)) {// preguntas respuesta corta
						for (int i = 0; i < examenAlumno.preguntas_RC.size(); i++) {

							texto += "#3" + BASE64.ProtegerTextoBase64(examenAlumno.preguntas_RC.get(i).getPregunta())
									+ "\n";
							texto += "~3" + BASE64.ProtegerTextoBase64(examenAlumno.preguntas_RC.get(i).getRespuesta())
									+ "\n";
							texto += "&3" + examenAlumno.preguntas_RC.get(i).getPeso() + "\n";

							respuestasRC.append(
									" " + BASE64.ProtegerTextoBase64(examenAlumno.preguntas_RC.get(i).getRespuesta()));
						}
					}

					StringBuilder respuestasRM = new StringBuilder();
					if (examenAlumno.verificarExamen(4)) {// preguntas respuesta numerica
						for (int i = 0; i < examenAlumno.preguntas_RC.size(); i++) {

							texto += "#4" + BASE64.ProtegerTextoBase64(examenAlumno.preguntas_RM.get(i).getPregunta())
									+ "\n";
							texto += "~4" + BASE64.ProtegerTextoBase64(BASE64.ProtegerTextoBase64(
									String.valueOf(examenAlumno.preguntas_RM.get(i).getRespuesta()))
									+ " "
									+ BASE64.ProtegerTextoBase64(
											String.valueOf(examenAlumno.preguntas_RM.get(i).getMinimo()))
									+ " " + BASE64.ProtegerTextoBase64(
											String.valueOf(examenAlumno.preguntas_RM.get(i).getMaximo())))
									+ "\n";
							texto += "&4" + examenAlumno.preguntas_RM.get(i).getPeso() + "\n";

							respuestasRM
									.append(String
											.valueOf(examenAlumno.preguntas_RM.get(i).getRespuesta() + "/"
													+ String.valueOf(examenAlumno.preguntas_RM.get(i).getMinimo() + "/"
															+ String.valueOf(
																	examenAlumno.preguntas_RM.get(i).getMaximo())))
											+ ";");
						}
					}

					// genero llaves rsa, una publica y otra privada. Esto es para evitar que el
					// alumno modifique sus resultados del examen.
					String[] llaveStrings = RSA.crearLlavesRSA();

					texto += "!" + llaveStrings[1] + "\n";

					String nombreArchivoKeyProfesor = nombreExamen + "_NO_BORRAR_LlaveProfesor.key";

					// con esto me aseguro que al leer el examen, el profesor pueda ver bien las
					// preguntas.
					int cantidadPreguntasSM = ((examenAlumno.preguntas_SM == null) ? 0
							: examenAlumno.preguntas_SM.size());
					int cantidadPreguntasVF = ((examenAlumno.preguntas_VF == null) ? 0
							: examenAlumno.preguntas_VF.size());
					int cantidadPreguntasRC = ((examenAlumno.preguntas_RC == null) ? 0
							: examenAlumno.preguntas_RC.size());
					int cantidadPreguntasRM = ((examenAlumno.preguntas_RM == null) ? 0
							: examenAlumno.preguntas_RM.size());

					/*-aqui lo que se guarda es lo siguiente:
					 
					 1~ la llave privada del profesor.
					 2~ el id del examen y cuantas preguntas tiene el examen.
					 3~ las respuestas del examen.
					 */

					Archivos.escribirEnArchivoDirectorio(rutaExamen + "\\llave del examen\\", nombreArchivoKeyProfesor,
							llaveStrings[0] + "\n"
									+ BASE64.ProtegerTextoBase64(tokenExamen + " "
											+ (cantidadPreguntasSM
													+ cantidadPreguntasVF + cantidadPreguntasRC + cantidadPreguntasRM)
											+ " "
											+ BASE64.ProtegerTextoBase64(
													"!" + BASE64.ProtegerTextoBase64(respuestasSM.toString()) + " #"
															+ BASE64.ProtegerTextoBase64(respuestasVF.toString()) + " $"
															+ BASE64.ProtegerTextoBase64(respuestasRC.toString()) + " %"
															+ BASE64.ProtegerTextoBase64(respuestasRM.toString())))
									+ "\n\nPOR FAVOR NO BORRAR/MODIFICAR ESTE ARCHIVO, DADO A QUE ES NECESARIO\nPARA CUANDO DESEE REVISAR LOS RESULTADOS DE SUS ALUMNOS");

					// con esto agrego una contraseña para el examen, con el fin de evitar que los
					// alumnos puedan acceder a las preguntas antes de tiempo.
					String contrasenaMD5 = null;
					contrasenaEstablecida = false;

					if ((contrasenaExamen != null) && !contrasenaExamen.isBlank()) {
						contrasenaMD5 = MD5.generarTokenExamen("no random", contrasenaExamen);
						contrasenaEstablecida = true;
					}

					Archivos.crearYCodificarArchivo(rutaExamen, nombreExamen, texto, contrasenaMD5,
							contrasenaEstablecida);

					PREGUNTACREADA = false;
					tabActivado(true);

					if (Mensajes.consultar("¿Desea obtener una vista previa de las preguntas del examen?",
							"Consulta")) {
						try {
							JPanel panel = new Alumno(examenAlumno);

							String titulo = "Vista previa";

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

						} catch (Exception e2) {
							System.err.println(e2.getMessage());
							e2.printStackTrace();
						}

					}

					examenAlumno = null;
					if (PANELACTUAL.equals(CREARPREGUNTAS)) {
						examenAlumno = null;
						actualizarPanelTab(panelCrearPreguntas(), 2);
					} else if (PANELACTUAL.equals(EDITAREXAMEN)) {
						actualizarPanelTab(panelEditarPreguntas(), 3);
					} else {
						Mensajes.error("¡Por favor reporta este error con el administrador!");
						return;
					}
				} catch (Exception e2) {
					Mensajes.error(e2.getMessage());
				}
			} else {
				Mensajes.error(
						"¡No puede guardar un examen vacío, por favor haga un nuevo examen e intentelo nuevamente!");
			}
		}
	}

	/**
	 * Este panel consulta que opciones desea agregar al examen de tipo word o pdf.
	 * 
	 * @return retorna el panel generado.
	 */
	private JPanel panelOpcionesGuardarExamenEnArchivoWORDPDF() {

		JPanel Panel = new JPanel(new BorderLayout()) {

			private static final long serialVersionUID = 406285933479692475L;

			@Override
			protected void paintComponent(Graphics g) {
				BufferedImage img = ImagenesAleatorias.getImagen();
				if (img != null) {
					super.paintComponent(g);
					g.drawImage(img, 0, 0, 1000, 700, 0, 0, img.getWidth(), img.getHeight(), this);
				} else {
					super.paintComponent(g);
					g.setColor(Color.decode("#C9C9C9"));
				}
			}
		};
		Panel.setBackground(Color.decode("#C9C9C9"));
		Panel.setBorder(new EmptyBorder(new Insets(49, 99, 49, 98)));

		JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder(new EmptyBorder(new Insets(20, 20, 20, 20)));
		Panel.add(panel, BorderLayout.CENTER);

		// parte del norte

		JLabel lblTitulo = new JLabel(
				"<html><p align = \"center\">Para ir finalizando, necesito que respondas las siguientes preguntas:</p></html>");
		lblTitulo.setFont(new Font("Segoe UI", Font.PLAIN, 25));
		panel.add(lblTitulo, BorderLayout.PAGE_START);

		// parte del centro

		JPanel panelCentral = new JPanel();
		panel.add(panelCentral);
		panelCentral.setLayout(new BoxLayout(panelCentral, BoxLayout.X_AXIS));

		// panelcentralizquierdo contiene las labels

		JPanel panelCentralIzquierdo = new JPanel(new GridLayout(0, 1, 0, 20));
		panelCentral.add(panelCentralIzquierdo);
		panelCentralIzquierdo.setBorder(new EmptyBorder(20, 0, 20, 0));

		JLabel lblNombreYApellido = new JLabel("¿Deseas agregar su nombre y apellido al examen?");
		lblNombreYApellido.setHorizontalAlignment(SwingConstants.CENTER);
		lblNombreYApellido.setFont(new Font("Segoe UI", Font.PLAIN, 17));
		panelCentralIzquierdo.add(lblNombreYApellido);

		JLabel lblTituloExamen = new JLabel("¿Deseas establecer un titulo para el examen?");
		lblTituloExamen.setHorizontalAlignment(SwingConstants.CENTER);
		lblTituloExamen.setFont(new Font("Segoe UI", Font.PLAIN, 17));
		panelCentralIzquierdo.add(lblTituloExamen);

		JLabel lblCursoExamen = new JLabel("¿Deseas agregar un curso al examen?");
		lblCursoExamen.setHorizontalAlignment(SwingConstants.CENTER);
		lblCursoExamen.setFont(new Font("Segoe UI", Font.PLAIN, 17));
		panelCentralIzquierdo.add(lblCursoExamen);

		JLabel lblFechaExamen = new JLabel("¿Deseas agregar una fecha al examen?");
		lblFechaExamen.setHorizontalAlignment(SwingConstants.CENTER);
		lblFechaExamen.setFont(new Font("Segoe UI", Font.PLAIN, 17));
		panelCentralIzquierdo.add(lblFechaExamen);

		JLabel lblObjetivoExamen = new JLabel("¿Deseas establecer un objetivo para el examen?");
		lblObjetivoExamen.setHorizontalAlignment(SwingConstants.CENTER);
		lblObjetivoExamen.setFont(new Font("Segoe UI", Font.PLAIN, 17));
		panelCentralIzquierdo.add(lblObjetivoExamen);

		// panelcentralderecho contiene los botones, switches y labels

		JPanel panelCentralDerecho = new JPanel(new GridLayout(0, 1, 10, 12));
		panelCentralDerecho.setBorder(new EmptyBorder(20, 20, 20, 20));
		panelCentral.add(panelCentralDerecho, BorderLayout.CENTER);

		JToggleButton tglbtnAgregarNombreYApellido = new JToggleButton("Desactivado");
		tglbtnAgregarNombreYApellido.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent itemEvent) {
				int estado = itemEvent.getStateChange();
				if (estado == ItemEvent.SELECTED) {
					tglbtnAgregarNombreYApellido.setText("Activado");
					agregarNombreYApellido = true;
				} else {
					tglbtnAgregarNombreYApellido.setText("Desactivado");
					agregarNombreYApellido = false;
				}
			}
		});
		panelCentralDerecho.add(tglbtnAgregarNombreYApellido);

		JToggleButton tglbtnTituloExamen = new JToggleButton("Desactivado");
		tglbtnTituloExamen.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent itemEvent) {
				int estado = itemEvent.getStateChange();
				if (estado == ItemEvent.SELECTED) {
					tituloExamen = Mensajes.consultarTextArea("Escriba aquí el titulo del examen.",
							"Titulo del examen.", tglbtnTituloExamen);
				} else {
					tituloExamen = "";
					tglbtnTituloExamen.setText("Desactivado");
				}
				panel.revalidate();
				panel.repaint();
			}
		});
		panelCentralDerecho.add(tglbtnTituloExamen);

		JToggleButton tglbtnCursoExamen = new JToggleButton("Desactivado");
		tglbtnCursoExamen.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent itemEvent) {
				int estado = itemEvent.getStateChange();
				if (estado == ItemEvent.SELECTED) {
					cursoExamen = Mensajes.consultarTextArea("Escriba aquí el curso del examen.", "Curso del examen.",
							tglbtnCursoExamen);
				} else {
					cursoExamen = "";
					tglbtnCursoExamen.setText("Desactivado");
				}
				panel.revalidate();
				panel.repaint();
			}
		});
		panelCentralDerecho.add(tglbtnCursoExamen);

		JToggleButton tglbtnFechaExamen = new JToggleButton("Desactivado");
		tglbtnFechaExamen.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent itemEvent) {
				int estado = itemEvent.getStateChange();
				if (estado == ItemEvent.SELECTED) {
					fechaExamen = Mensajes.consultarTextArea("Escriba aquí la fecha del examen.", "Fecha del examen.",
							tglbtnFechaExamen);
				} else {
					fechaExamen = "";
					tglbtnFechaExamen.setText("Desactivado");
				}
				panel.revalidate();
				panel.repaint();
			}
		});
		panelCentralDerecho.add(tglbtnFechaExamen);

		JToggleButton tglbtnObjetivoExamen = new JToggleButton("Desactivado");
		tglbtnObjetivoExamen.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent itemEvent) {
				int estado = itemEvent.getStateChange();
				if (estado == ItemEvent.SELECTED) {
					objetivo = Mensajes.consultarTextArea("Escriba aquí el objetivo del examen.",
							"Objetivo del examen.", tglbtnObjetivoExamen);
				} else {
					objetivo = "";
					tglbtnObjetivoExamen.setText("Desactivado");
				}
				panel.revalidate();
				panel.repaint();
			}
		});
		panelCentralDerecho.add(tglbtnObjetivoExamen);

		// parte del sur

		JPanel panelBotonesInferiores = new JPanel(new BorderLayout());
		panel.add(panelBotonesInferiores, BorderLayout.SOUTH);

		JButton botonVolverPanelOpcionesGuardarExamenEnArchivo = new JButton("Volver");
		botonVolverPanelOpcionesGuardarExamenEnArchivo.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		botonVolverPanelOpcionesGuardarExamenEnArchivo.setBackground(Color.decode("#00668C"));
		botonVolverPanelOpcionesGuardarExamenEnArchivo.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (PANELACTUAL.equals(CREARPREGUNTAS)) {
					actualizarPanelTab(panelCrearPreguntasTiposPreguntas(), 2);

				} else /* if (PANELACTUAL.equals(EDITAREXAMEN)) */ {
					actualizarPanelTab(panelEditarPreguntas(), 3);
				}
			}
		});

		panelBotonesInferiores.add(botonVolverPanelOpcionesGuardarExamenEnArchivo, BorderLayout.WEST);

		JButton botonFinalizarPanelOpcionesGuardarExamenEnArchivo = new JButton("Finalizar");
		botonFinalizarPanelOpcionesGuardarExamenEnArchivo.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		botonFinalizarPanelOpcionesGuardarExamenEnArchivo.addActionListener(new guardarExamenEnArchivoWORDPDF());
		botonFinalizarPanelOpcionesGuardarExamenEnArchivo.setBackground(Color.decode("#0077ff"));
		panelBotonesInferiores.add(botonFinalizarPanelOpcionesGuardarExamenEnArchivo, BorderLayout.EAST);

		return Panel;
	}

	/**
	 * Esta clase, es el actionlistener de los botones guardar, de los paneles que
	 * modifican o crean archivos word o pdf.
	 */
	private class guardarExamenEnArchivoWORDPDF implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (examenAlumno != null) {
				try {

					if (!agregarNombreYApellido) {
						nombreProfesor = "";
						apellidoProfesor = "";
					}

					JFileChooser Archivo = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
					Archivo.setFileFilter(new FileNameExtensionFilter("Portable Document Format (.pdf)", "pdf"));
					Archivo.setFileFilter(new FileNameExtensionFilter("Office Open XML Document (.docx)", "docx"));
					Archivo.setAcceptAllFileFilterUsed(false);

					if (Archivo.showSaveDialog(new JFrame()) == JFileChooser.APPROVE_OPTION) {

						File archivoSeleccionado = Archivo.getSelectedFile();

						if (archivoSeleccionado != null) {

							// verifico si el nombre ingresado no es invalido

							if (Texto.verificarNombreArchivo(archivoSeleccionado.getName())) {
								Mensajes.error(
										"<html><p align = \"center\">Por favor, no use caracteres ilegales en el nombre de su archivo.<br>(< > : \\ / | ? *)<br>Inténtelo nuevamente.</p></html>");
								return;
							}

							// verifico si el archivo tiene la extension pdf o docx

							String extension = "";

							FileFilter extensionArchivo = Archivo.getFileFilter();
							if (extensionArchivo instanceof FileNameExtensionFilter
									&& !extensionArchivo.accept(archivoSeleccionado)) {
								extension = ((FileNameExtensionFilter) extensionArchivo).getExtensions()[0];
							}

							String nombreArchivo = Archivo.getSelectedFile().getAbsolutePath();

							if (extension.equals("pdf")) {

								ExportarAPDF.exportarAPDF(nombreArchivo, examenAlumno, tituloExamen, nombreProfesor,
										apellidoProfesor, fechaExamen, clase, cursoExamen, objetivo);

								if (Mensajes.consultar("¿Desea obtener una vista previa de las preguntas del examen?",
										"Consulta")) {
									try {

										JPanel panel = ExportarAPDF
												.mostrarPDF(Archivo.getSelectedFile().getAbsolutePath() + ".pdf");

										String titulo = "Vista previa";

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

									} catch (Exception e2) {
										System.err.println(e2.getMessage());
										e2.printStackTrace();
									}

								}

							} else if (extension.equals("docx")) {
								ExportarAWord.examenAWord(nombreArchivo, examenAlumno, tituloExamen, nombreProfesor,
										apellidoProfesor, fechaExamen, clase, cursoExamen, objetivo);
							}

							PREGUNTACREADA = false;
							examenAlumno = null;
							tituloExamen = "";
							fechaExamen = "";
							clase = "";
							cursoExamen = "";
							objetivo = "";

						} else {
							Mensajes.error(
									"<html><p align = \"center\">Por favor, ingrese un nombre de archivo.<br>Inténtelo nuevamente.</p></html>");
							return;
						}

					} else {
						return;
					}

					if (PANELACTUAL.equals(CREARPREGUNTAS)) {
						actualizarPanelTab(panelCrearPreguntas(), 2);
					} else if (PANELACTUAL.equals(EDITAREXAMEN)) {
						actualizarPanelTab(panelEditarPreguntas(), 3);
					} else {
						Mensajes.error("¡Por favor reporta este error con el administrador!");
						return;
					}
				} catch (Exception e2) {
					Mensajes.error(e2.getMessage());
					return;
				}
			} else {
				Mensajes.error(
						"¡No puede guardar un examen vacío, por favor haga un nuevo examen e intentelo nuevamente!");
				return;
			}
		}
	}

	// endregion

	// region creadores de componentes especiales

	/**
	 * Este metodo se encarga de crear tabs con una x, con el fin de poder cerrar
	 * los tabs.
	 * 
	 * @param panel  es el panel que se desea agregar al jtabbedpane.
	 * @param titulo es el titulo del tab. Para este panel (profesor), se llama
	 *               "vista previa + numero"
	 */
	private void agregarVistaPrevia(JPanel panel, String titulo) {

		panel.setPreferredSize(tabbedPane.getComponentAt(0).getPreferredSize());

		tabbedPane.addTab(titulo, panel);
		int index = tabbedPane.indexOfTab(titulo);
		JPanel pnlTab = new JPanel(new GridBagLayout());
		pnlTab.setOpaque(false);
		JLabel lblTitulo = new JLabel(titulo);
		JButton btnClose = new JButton("x");

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1;

		pnlTab.add(lblTitulo, gbc);

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

	/**
	 * Esta clase, permite crear paneles con 4 items al mismo tiempo (JCheckBox,
	 * JTextField, y 2 JButtons).
	 */
	private static class crearCheckTextBotones extends JPanel {

		private static final long serialVersionUID = 4058219522009605530L;
		public JButton boton;
		public JButton boton2;
		public JCheckBox check;
		private JTextField texto;

		/**
		 * @param textoBoton     texto para el primer boton (por lo general es "ver
		 *                       pregunta")
		 * @param textoBoton2    texto para el segundo boton (por lo general es "editar
		 *                       pregunta")
		 * @param textoTextField texto para el text field (por lo general es la
		 *                       pregunta)
		 */

		public crearCheckTextBotones(String textoBoton, String textoBoton2, String textoTextField) {

			String textoTextFieldRecortado = "";
			boolean recortado = false;

			boton = new JButton(textoBoton);
			boton2 = new JButton(textoBoton2);
			texto = new JTextField(30);

			if (textoTextField.length() >= 58) {
				textoTextFieldRecortado = textoTextField.replaceAll("\\s+", " ").substring(0,
						Math.min(textoTextField.length(), 68)) + "...";
				recortado = true;
			}

			texto.setText((recortado) ? textoTextFieldRecortado : textoTextField);
			texto.setHighlighter(null);
			texto.setFocusTraversalKeysEnabled(false);
			texto.setTransferHandler(null);
			texto.setEnabled(false);
			texto.addKeyListener(new KeyAdapter() {
				// Con eso me evito el que se pueda seleccionar teclas como enter, delete y el
				// backspace.
				@Override
				public void keyPressed(KeyEvent e) {
					if ((e.getKeyCode() == KeyEvent.VK_DELETE || e.getKeyCode() == KeyEvent.VK_BACK_SPACE
							|| e.getKeyCode() == KeyEvent.VK_ENTER)) {
						e.consume();
					}
				}

				@Override
				public void keyTyped(KeyEvent e) {

					char teclaIngresada = e.getKeyChar();

					if (!((teclaIngresada == KeyEvent.VK_LEFT) || (teclaIngresada == KeyEvent.VK_RIGHT))) {
						e.consume();
					}
				}
			});

			check = new JCheckBox();

			if (!recortado) {
				boton.setEnabled(false);
			}

			this.setLayout(new FlowLayout());
			this.add(check);
			this.add(texto);
			this.add(boton);
			this.add(boton2);
		}
	}

	/**
	 * Esta clase, permite crear paneles con 3 items al mismo tiempo (JRadioButton,
	 * JCheckBox, JTextField).
	 */
	private static class crearCheckTextRdBoton extends JPanel {

		private static final long serialVersionUID = 4958871866995946415L;
		public JRadioButton boton;
		public JCheckBox check;
		public JTextField texto;

		public crearCheckTextRdBoton(String textoTextField) {
			boton = new JRadioButton("");
			texto = new JTextField(35);
			check = new JCheckBox();

			texto.setText(textoTextField);

			this.setLayout(new FlowLayout());
			this.add(check);
			this.add(boton);
			this.add(texto);
		}
	}

	/**
	 * Esta clase, permite crear paneles con 2 items al mismo tiempo (JCheckBox y
	 * JTextField).
	 */
	private class crearCheckField extends JPanel {

		private static final long serialVersionUID = -7858381412901493031L;
		public JCheckBox check;
		public JTextField texto;

		public crearCheckField() {
			texto = new JTextField(15);
			texto.setFont(new Font("Segoe UI", Font.PLAIN, 20));
			check = new JCheckBox();

			texto.addKeyListener(new KeyAdapter() {
				@Override
				public void keyTyped(KeyEvent e) {

					char letraIngresada = e.getKeyChar();

					if (!((letraIngresada >= '0') && (letraIngresada <= '9') || (letraIngresada == '-')
							|| (letraIngresada == '+') || (letraIngresada == '.')
							|| (letraIngresada == KeyEvent.VK_BACK_SPACE) || (letraIngresada == KeyEvent.VK_DELETE))) {
						e.consume();
					}

					if ((letraIngresada == '-' || letraIngresada == '+') && (texto.getText().length() > 0)) {
						e.consume();
					}

					if (letraIngresada == '.' && (texto.getText().contains(".") || (texto.getText().length() < 1))) {
						e.consume();
					}
				}
			});

			this.setLayout(new FlowLayout());
			this.add(check);
			this.add(texto);
		}
	}

	// endregion

	// region metodos

	/**
	 * Este metodo permite cambiar el panel del jtabbedpane, por el que se desee.
	 * 
	 * @param panel es el panel que se desea agregar.
	 * @param tab   es el tab en donde se desea agregar el panel. El orden es el
	 *              siguiente:
	 * 
	 *              <ol>
	 *              <li>tab de bienvenida</li>
	 *              <li>tab de crear preguntas</li>
	 *              <li>tab de editar examen</li>
	 *              <li>tab de resultados alumnos</li>
	 *              <li>tab de cerrar sesion</li>
	 *              </ol>
	 */
	private void actualizarPanelTab(JPanel panel, int tab) {
		repaint();
		if (tab == 1) {
			tabbedPane.setComponentAt(tabbedPane.indexOfTab(BIENVENIDA), panel);
		} else if (tab == 2) {
			tabbedPane.setComponentAt(tabbedPane.indexOfTab(CREARPREGUNTAS), panel);
		} else if (tab == 3) {
			tabbedPane.setComponentAt(tabbedPane.indexOfTab(EDITAREXAMEN), panel);
		} else if (tab == 4) {
			tabbedPane.setComponentAt(tabbedPane.indexOfTab(RESULTADOSALUMNOS), panel);
		} else if (tab == 5) {
			tabbedPane.setComponentAt(tabbedPane.indexOfTab(CERRARSESION), panel);
		} else if (tab == 6) {
			tabbedPane.setComponentAt(tabbedPane.indexOfTab(VISUALIZARPDF), panel);
		} else if (tab == 7) {
			tabbedPane.setComponentAt(tabbedPane.indexOfTab(VISUALIZARSMR), panel);
		}
	}

	/**
	 * Este metodo activa o desactiva los tabs del jtabbedpane.
	 * 
	 * @param opcion si es {@code true}, todos los tabs se activaran, caso
	 *               contrario, se desactivaran.
	 */
	private void tabActivado(boolean opcion) {
		for (int i = 0; i < tabbedPane.getTabCount(); i++) {
			tabbedPane.setEnabledAt(i, opcion);
		}
	}

	// endregion

}
