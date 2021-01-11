package utilidadesInterfaces;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.border.EmptyBorder;

import base.RespuestaNumerica;
import base.RespuestasCortas;
import base.SeleccionMultiple;
import base.VerdaderoFalso;

/**
 * Esta clase se encarga de hacer todo lo relacionado con los mensajes (mostrar
 * mensajes de error, o consultas, como tambien mostrar paneles para poder
 * editar las preguntas de diferentes tipos).
 * 
 * @author Sebastian Morgado
 * @version 2.0.0
 * @since 2.0.0
 */
public class Mensajes {

	private static ArrayList<crearCheckTextRdBoton> rdBotones;

	private static ButtonGroup group;

	private static JTextField textField;

	/**
	 * Este metodo se encarga de mostrar una ventana, en la cual se le pide que
	 * acepte o rechaze.
	 * 
	 * @param mensaje es el mensaje que se desea mostrar.
	 * @param titulo  es el titulo que se desea mostrar.
	 * 
	 * @return retorna {@code true} si la elección del usuario es "si", caso
	 *         contrario retorna {@code false}.
	 */
	public static boolean consultar(String mensaje, String titulo) {
		JLabel label = new JLabel(mensaje);
		label.setFont(new Font("Segoe ui", Font.BOLD, 20));
		int respuesta = JOptionPane.showConfirmDialog(null, label, titulo, JOptionPane.YES_NO_OPTION);
		if (respuesta == JOptionPane.YES_OPTION) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Este metodo se encarga de mostrar una ventana, en la cual usted puede
	 * ingresar datos.
	 * 
	 * @param mensaje es el mensaje que se desea mostrar.
	 * @param titulo  es el titulo que se desea mostrar.
	 * @param boton   es el boton el cual se le cambiara el estado
	 *                (activado/desactivado).
	 * 
	 * @return retorna el texto ingresado por el usuario, si no ingresa nada,
	 *         retornará vacio.
	 */
	public static String consultarTextArea(String mensaje, String titulo, JToggleButton boton) {
		String texto = "";

		boton.setText("Activado");
		JTextArea ta = new JTextArea(10, 30);
		ta.setLineWrap(true);
		ta.setWrapStyleWord(true);
		new TextPrompt(mensaje, ta);
		switch (JOptionPane.showConfirmDialog(null, new JScrollPane(ta), titulo, JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.PLAIN_MESSAGE)) {
		case JOptionPane.OK_OPTION:
			if (ta.getText() != null && !ta.getText().isBlank()) {
				texto = ta.getText();
				boton.setText("Activado");
			} else {
				texto = "";
				boton.setText("Desactivado");
			}
			break;
		case JOptionPane.CANCEL_OPTION:
			texto = "";
			boton.setText("Desactivado");
			break;
		default:
			texto = "";
			boton.setText("Desactivado");
			break;
		}

		return texto;
	}

	/**
	 * Este metodo se encarga de mostrar una ventana, en la cual usted puede
	 * ingresar datos.
	 * 
	 * @param mensaje      es el mensaje que se desea mostrar.
	 * @param mensajeError es el mensaje de error que se desea mostrar.
	 * @param titulo       es el titulo que se desea mostrar.
	 * @param boton        es el boton el cual se le cambiara el estado
	 *                     (activado/desactivado).
	 * @param field        es el espacio de donde se obtendra el texto ingresado por
	 *                     el profesor.
	 * 
	 * @return retorna el texto ingresado por el usuario, si no ingresa nada,
	 *         retornará vacio.
	 */
	public static String consultarTextField(String mensaje, String mensajeError, String titulo, JToggleButton boton,
			JTextField field) {
		String texto = "";

		JPanel panelCentral = new JPanel(new BorderLayout());

		JPanel panelSuperior = new JPanel(new BorderLayout(10, 0));
		panelCentral.add(panelSuperior, BorderLayout.NORTH);

		panelSuperior.add(new JLabel(mensaje), BorderLayout.WEST);
		panelSuperior.add(field, BorderLayout.EAST);

		JPanel panelInferior = new JPanel();
		panelCentral.add(panelInferior, BorderLayout.CENTER);

		JLabel lblMensajeError = new JLabel(mensajeError);
		lblMensajeError.setVisible(false);
		panelInferior.add(lblMensajeError);

		boolean continuar = true;

		do {
			if (JOptionPane.showOptionDialog(null, panelCentral, titulo, JOptionPane.YES_NO_OPTION,
					JOptionPane.PLAIN_MESSAGE, null, new Object[] { "Aceptar", "Cancelar" },
					null) == JOptionPane.YES_OPTION) {
				if ((field.getText() != null) && (!field.getText().isBlank())) {
					texto = field.getText();
					boton.setText("Activado");
					continuar = false;
				} else {
					lblMensajeError.setVisible(true);
					texto = "";
					boton.setText("Desactivado");
					continuar = true;
				}
			} else {
				texto = "";
				boton.setText("Desactivado");
				continuar = false;
			}
		} while (continuar);

		return texto;
	}

	/**
	 * Este metodo se encarga de mostrar un editor de preguntas de respuestas
	 * cortas.
	 * 
	 * @param pregunta es la pregunta de tipo respuestas cortas que se desea editar.
	 * @param titulo   es el titulo que tendra la ventana generada.
	 * @return retorna la pregunta modificada. Si no fue modificada, retornará
	 *         {@code null}, caso contrario, retorna la pregunta modificada.
	 */
	public static RespuestasCortas editarPreguntaRespuestasCortas(RespuestasCortas pregunta, String titulo) {

		if (pregunta != null) {

			JPanel panel = new JPanel(new BorderLayout(0, 10));
			panel.setBorder(new EmptyBorder(new Insets(20, 40, 20, 40)));

			// panel central

			// panel del centro

			JPanel panelCentral = new JPanel();
			panelCentral.setLayout(new BoxLayout(panelCentral, BoxLayout.Y_AXIS));
			panel.add(panelCentral, BorderLayout.CENTER);

			// panel central superior

			JPanel panelCentralSuperior = new JPanel(new BorderLayout());
			panelCentral.add(panelCentralSuperior);

			JLabel ingresePregunta = new JLabel("Edite la pregunta en el siguiente cuadro:");
			ingresePregunta.setFont(new Font("Segoe UI", Font.PLAIN, 25));
			panelCentralSuperior.add(ingresePregunta, BorderLayout.NORTH);

			JScrollPane scrollPane = new JScrollPane();
			panelCentralSuperior.add(scrollPane, BorderLayout.CENTER);

			JTextArea text = new JTextArea(7, 30);
			text.setLineWrap(true);
			text.setWrapStyleWord(true);
			text.setText(pregunta.getPregunta());
			text.setEditable(true);
			scrollPane.setViewportView(text);

			panelCentral.add(Box.createRigidArea(new Dimension(0, 10)));

			// panel central inferior

			JPanel panelCentralCentral = new JPanel(new BorderLayout());
			panelCentral.add(panelCentralCentral);

			JLabel ingreseRespuesta = new JLabel("Edite la respuesta en el siguiente cuadro:");
			ingreseRespuesta.setFont(new Font("Segoe UI", Font.PLAIN, 25));
			panelCentralCentral.add(ingreseRespuesta, BorderLayout.NORTH);

			textField = new JTextField(10);
			textField.setText(pregunta.getRespuesta());
			panelCentral.add(textField);

			panelCentral.add(Box.createRigidArea(new Dimension(0, 10)));

			// panel central inferior

			JPanel panelCentralInferior = new JPanel(new BorderLayout());
			panelCentral.add(panelCentralInferior);

			JLabel ingresePeso = new JLabel("Edite el puntaje en el siguiente cuadro:");
			ingresePeso.setFont(new Font("Segoe UI", Font.PLAIN, 25));
			panelCentralInferior.add(ingresePeso, BorderLayout.NORTH);

			JTextField peso = new JTextField(5);
			peso.setFont(new Font("Segoe UI", Font.PLAIN, 20));
			peso.setTransferHandler(null);
			peso.setText(String.valueOf(pregunta.getPeso()));
			peso.addKeyListener(new KeyAdapter() {
				@Override
				public void keyTyped(KeyEvent e) {
					int cantidadMaximaCaracteres = 3;

					char letraIngresada = e.getKeyChar();

					if (!((letraIngresada >= '0') && (letraIngresada <= '9') || (letraIngresada == '-')
							|| (letraIngresada == '+') || (letraIngresada == KeyEvent.VK_BACK_SPACE)
							|| (letraIngresada == KeyEvent.VK_DELETE))) {
						peso.setToolTipText("Por favor ingrese numeros entre -9999 y 9999.");
						e.consume();
					}

					if ((letraIngresada == '-' || letraIngresada == '+') && (peso.getText().length() > 0)) {
						e.consume();
					}

					if ((peso.getText().length() > 0)
							&& (peso.getText().charAt(0) == '-' || peso.getText().charAt(0) == '+')) {
						cantidadMaximaCaracteres = 4;
					}

					if (peso.getText().length() > cantidadMaximaCaracteres) {
						e.consume();
					}
				}
			});
			panelCentralInferior.add(peso, BorderLayout.CENTER);

			panelCentral.add(Box.createRigidArea(new Dimension(0, 10)));

			String[] botones = { "Aceptar", "Cancelar" };

			int eleccion = JOptionPane.showOptionDialog(new JFrame(), panel, titulo, JOptionPane.YES_NO_OPTION,
					JOptionPane.PLAIN_MESSAGE, null, botones, botones[0]);

			if (eleccion == JOptionPane.YES_OPTION) {

				if (text == null || text.getText().isBlank()) {
					error("Por favor ingrese una pregunta.");
					return editarPreguntaRespuestasCortas(pregunta, titulo);
				}

				if (textField == null || textField.getText().isBlank()) {
					error("Por favor ingrese una respuesta valida.");
					return editarPreguntaRespuestasCortas(pregunta, titulo);
				}

				String textoPeso = peso.getText();

				int pesoPregunta = 0;

				if (textoPeso == null || textoPeso.isBlank()) {
					Mensajes.error("Por favor, solo ingresa NUMEROS, el texto u otro tipo de caracteres NO sirven.");
					return editarPreguntaRespuestasCortas(pregunta, titulo);
				}

				try {
					pesoPregunta = Integer.parseInt(textoPeso);
				} catch (Exception e2) {
					error("Por favor, solo ingresa NUMEROS, el texto u otro tipo de caracteres NO sirven.");
					return editarPreguntaRespuestasCortas(pregunta, titulo);
				}

				return new RespuestasCortas(text.getText(), textField.getText(), pesoPregunta);

			} else {
				return null;
			}
		} else {
			error("¡Error 404, pregunta no encontrada!");
			return null;
		}

	}

	/**
	 * Este metodo se encarga de mostrar un editor de preguntas de respuestas
	 * numericas.
	 * 
	 * @param pregunta es la pregunta de tipo respuestas numericas que se desea
	 *                 editar.
	 * @param titulo   es el titulo que tendra la ventana generada.
	 * @return retorna la pregunta modificada. Si no fue modificada, retornará
	 *         {@code null}, caso contrario, retorna la pregunta modificada.
	 */
	public static RespuestaNumerica editarPreguntaRespuestasNumericas(RespuestaNumerica pregunta, String titulo) {

		if (pregunta != null) {

			JPanel panel = new JPanel(new BorderLayout(0, 5));
			panel.setBorder(new EmptyBorder(new Insets(10, 20, 11, 20)));

			// panel central

			// panel del centro

			JPanel panelCentral = new JPanel();
			panelCentral.setLayout(new BoxLayout(panelCentral, BoxLayout.Y_AXIS));
			panel.add(panelCentral, BorderLayout.CENTER);

			// panel central superior

			JPanel panelCentralSuperior = new JPanel(new BorderLayout());
			panelCentral.add(panelCentralSuperior);

			JLabel ingresePregunta = new JLabel("Edite la pregunta en el siguiente cuadro:");
			ingresePregunta.setFont(new Font("Segoe UI", Font.PLAIN, 25));
			panelCentralSuperior.add(ingresePregunta, BorderLayout.NORTH);

			JScrollPane scrollPane = new JScrollPane();
			panelCentralSuperior.add(scrollPane, BorderLayout.CENTER);

			JTextArea txtAreaPregunta = new JTextArea(7, 30);
			txtAreaPregunta.setText(pregunta.getPregunta());
			txtAreaPregunta.setLineWrap(true);
			txtAreaPregunta.setWrapStyleWord(true);
			txtAreaPregunta.setEditable(true);
			scrollPane.setViewportView(txtAreaPregunta);

			panelCentral.add(Box.createRigidArea(new Dimension(0, 10)));

			// panel central inferior

			JPanel panelCentralCentral = new JPanel(new BorderLayout());
			panelCentral.add(panelCentralCentral);

			JLabel ingreseRespuesta = new JLabel("Edite la respuesta en el siguiente cuadro:");
			ingreseRespuesta.setFont(new Font("Segoe UI", Font.PLAIN, 25));
			panelCentralCentral.add(ingreseRespuesta, BorderLayout.NORTH);

			JTextField txtfieldRespuesta = new JTextField(10);
			txtfieldRespuesta.setText(String.valueOf(pregunta.getRespuesta()));
			txtfieldRespuesta.addKeyListener(new KeyAdapter() {
				@Override
				public void keyTyped(KeyEvent e) {

					char letraIngresada = e.getKeyChar();

					if (!((letraIngresada >= '0') && (letraIngresada <= '9') || (letraIngresada == '-')
							|| (letraIngresada == '+') || (letraIngresada == '.')
							|| (letraIngresada == KeyEvent.VK_BACK_SPACE) || (letraIngresada == KeyEvent.VK_DELETE))) {
						e.consume();
					}

					if ((letraIngresada == '-' || letraIngresada == '+')
							&& (txtfieldRespuesta.getText().length() > 0)) {
						e.consume();
					}

					if (letraIngresada == '.' && (txtfieldRespuesta.getText().contains(".")
							|| (txtfieldRespuesta.getText().length() < 1))) {
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

			crearCheckField valorMinimo = new crearCheckField();
			if (pregunta.getMinimo() != null) {
				valorMinimo.texto.setText(String.valueOf(pregunta.getMinimo()));
				valorMinimo.check.setSelected(true);
			}
			panelCentralInferiorIzquierdo.add(valorMinimo, BorderLayout.CENTER);

			// panel central inferior derecho

			JPanel panelCentralInferiorDerecho = new JPanel(new BorderLayout());
			panelCentralInferior.add(panelCentralInferiorDerecho, BorderLayout.EAST);

			JLabel lblValorMaximo = new JLabel("valor máximo:");
			lblValorMaximo.setFont(new Font("Segoe UI", Font.PLAIN, 25));
			panelCentralInferiorDerecho.add(lblValorMaximo, BorderLayout.NORTH);

			crearCheckField valorMaximo = new crearCheckField();
			if (pregunta.getMaximo() != null) {
				valorMaximo.texto.setText(String.valueOf(pregunta.getMaximo()));
				valorMaximo.check.setSelected(true);
			}
			panelCentralInferiorDerecho.add(valorMaximo, BorderLayout.EAST);

			panelCentral.add(Box.createRigidArea(new Dimension(0, 10)));

			JPanel panelCentralInferiorInferior = new JPanel(new BorderLayout());
			panelCentral.add(panelCentralInferiorInferior);

			JLabel ingresePeso = new JLabel("Edite el puntaje en el siguiente cuadro:");
			ingresePeso.setFont(new Font("Segoe UI", Font.PLAIN, 25));
			panelCentralInferiorInferior.add(ingresePeso, BorderLayout.NORTH);

			JTextField txtfieldPesoPregunta = new JTextField(5);
			txtfieldPesoPregunta.setFont(new Font("Segoe UI", Font.PLAIN, 20));
			txtfieldPesoPregunta.setText(String.valueOf(pregunta.getPeso()));
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

					if ((letraIngresada == '-' || letraIngresada == '+')
							&& (txtfieldPesoPregunta.getText().length() > 0)) {
						e.consume();
					}

					if ((txtfieldPesoPregunta.getText().length() > 0)
							&& (txtfieldPesoPregunta.getText().charAt(0) == '-'
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

			String[] botones = { "Aceptar", "Cancelar" };

			int eleccion = JOptionPane.showOptionDialog(new JFrame(), panel, titulo, JOptionPane.YES_NO_OPTION,
					JOptionPane.PLAIN_MESSAGE, null, botones, botones[0]);

			if (eleccion == JOptionPane.YES_OPTION) {

				if (txtAreaPregunta == null || txtAreaPregunta.getText().isBlank()) {
					error("Por favor ingrese su pregunta.");
					return editarPreguntaRespuestasNumericas(pregunta, titulo);
				}

				if (txtfieldPesoPregunta == null || txtfieldPesoPregunta.getText().isBlank()) {
					error("Por favor, solo ingresa NUMEROS, el texto u otro tipo de caracteres NO sirven.");
					return editarPreguntaRespuestasNumericas(pregunta, titulo);
				}

				if (txtfieldRespuesta == null || txtfieldRespuesta.getText().isBlank()) {
					error("Por favor, ingrese una respuesta valida.");
					return editarPreguntaRespuestasNumericas(pregunta, titulo);
				}

				if (valorMaximo == null || (valorMaximo.check.isSelected() && valorMaximo.texto.getText().isBlank())) {
					error("Por favor, ingrese un valor maximo.");
					return editarPreguntaRespuestasNumericas(pregunta, titulo);
				}

				if (valorMinimo == null || (valorMinimo.check.isSelected() && valorMinimo.texto.getText().isBlank())) {
					error("Por favor, ingrese un valor minimo.");
					return editarPreguntaRespuestasNumericas(pregunta, titulo);
				}

				Float minimo = null;
				Float maximo = null;
				Float respuesta = null;
				Integer peso = null;

				if (valorMinimo.check.isSelected() && !valorMinimo.texto.getText().isBlank()) {
					try {
						minimo = Float.parseFloat(valorMinimo.texto.getText());
					} catch (Exception e2) {
						error("ha ocurrido un error al generar el minimo, por favor revise los datos ingresados.");
						return editarPreguntaRespuestasNumericas(pregunta, titulo);
					}
				}

				if (valorMaximo.check.isSelected() && !valorMaximo.texto.getText().isBlank()) {
					try {
						maximo = Float.parseFloat(valorMaximo.texto.getText());
					} catch (Exception e2) {
						error("ha ocurrido un error al generar el maximo, por favor revise los datos ingresados.");
						return editarPreguntaRespuestasNumericas(pregunta, titulo);
					}
				}

				try {
					respuesta = Float.parseFloat(txtfieldRespuesta.getText());
				} catch (Exception e2) {
					error("ha ocurrido un error en su respuesta, por favor revise los datos ingresados.");
					return editarPreguntaRespuestasNumericas(pregunta, titulo);
				}

				try {
					peso = Integer.parseInt(txtfieldPesoPregunta.getText());
				} catch (Exception e2) {
					error("ha ocurrido un error en su puntaje, por favor revise los datos ingresados.");
					return editarPreguntaRespuestasNumericas(pregunta, titulo);
				}

				if (maximo != null && !(maximo > respuesta)) {
					error("por favor, el valor máximo debe ser mayor que la respuesta.");
					return editarPreguntaRespuestasNumericas(pregunta, titulo);
				}

				if (minimo != null && !(minimo < respuesta)) {
					error("por favor, el valor mínimo debe ser menor que la respuesta.");
					return editarPreguntaRespuestasNumericas(pregunta, titulo);
				}

				return new RespuestaNumerica(txtAreaPregunta.getText(), respuesta, minimo, maximo, peso);

			} else {
				return null;
			}
		} else {
			error("¡Error 404, pregunta no encontrada!");
			return null;
		}

	}

	/**
	 * Este metodo se encarga de mostrar un editor de preguntas de selección
	 * multiple.
	 * 
	 * @param pregunta es la pregunta de tipo selección multiple que se desea
	 *                 editar.
	 * @param titulo   es el titulo que tendra la ventana generada.
	 * @return retorna la pregunta modificada. Si no fue modificada, retornará
	 *         {@code null}, caso contrario, retorna la pregunta modificada.
	 */
	public static SeleccionMultiple editarPreguntaSeleccionMultiple(SeleccionMultiple pregunta, String titulo) {
		if (pregunta != null) {
			JPanel panel = new JPanel(new BorderLayout(0, 10));
			panel.setBorder(new EmptyBorder(new Insets(20, 40, 20, 40)));

			// panel central

			// panel del centro

			JPanel panelCentral = new JPanel();
			panelCentral.setLayout(new BoxLayout(panelCentral, BoxLayout.Y_AXIS));
			panel.add(panelCentral, BorderLayout.CENTER);

			// panel central superior

			JPanel panelCentralSuperior = new JPanel(new BorderLayout());
			panelCentral.add(panelCentralSuperior);

			JLabel ingresePregunta = new JLabel("Edite la pregunta en el siguiente cuadro:");
			ingresePregunta.setFont(new Font("Segoe UI", Font.PLAIN, 25));
			panelCentralSuperior.add(ingresePregunta, BorderLayout.NORTH);

			JScrollPane scrollPane = new JScrollPane();
			panelCentralSuperior.add(scrollPane, BorderLayout.CENTER);

			JTextArea text = new JTextArea(7, 30);
			text.setLineWrap(true);
			text.setWrapStyleWord(true);
			text.setText(pregunta.getPregunta());
			text.setEditable(true);
			scrollPane.setViewportView(text);

			panelCentral.add(Box.createRigidArea(new Dimension(0, 10)));

			// panel central inferior

			JPanel panelCentralCentral = new JPanel(new BorderLayout());
			panelCentral.add(panelCentralCentral);

			JLabel ingreseOpciones = new JLabel("Edite las opciones en el siguiente cuadro:");
			ingreseOpciones.setFont(new Font("Segoe UI", Font.PLAIN, 25));
			panelCentralCentral.add(ingreseOpciones, BorderLayout.NORTH);

			JScrollPane scrllBotones = new JScrollPane();
			scrllBotones.setPreferredSize(new Dimension(panelCentral.getPreferredSize().width + 10,
					panelCentral.getPreferredSize().height / 2));
			panelCentralCentral.add(scrllBotones, BorderLayout.CENTER);

			JPanel panelRdBotones = new JPanel();
			panelRdBotones.setLayout(new BoxLayout(panelRdBotones, BoxLayout.Y_AXIS));
			scrllBotones.setViewportView(panelRdBotones);

			List<String> opciones = new ArrayList<String>(Arrays.asList(pregunta.getOpciones()));

			group = new ButtonGroup();

			rdBotones = new ArrayList<crearCheckTextRdBoton>();

			for (int i = 0; i < opciones.size(); i++) {

				if (!(opciones.get(i) == null)) {

					rdBotones.add(new crearCheckTextRdBoton(opciones.get(i)));
					if (rdBotones.get(i).texto.getText().equals(opciones.get(pregunta.getRespuesta()))) {
						rdBotones.get(i).boton.setSelected(true);
					}
					group.add(rdBotones.get(i).boton);
					panelRdBotones.add(rdBotones.get(i));
				}
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
						group.add(boton.boton);
						panelRdBotones.add(boton);
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
					panelRdBotones.updateUI();
				}
			});
			panelSeleccionarTodos.add(btnSeleccionarTodos);

			JButton Eliminar = new JButton("-");
			Eliminar.setToolTipText("Eliminar opciones seleccionadas");
			Eliminar.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {

					ArrayList<crearCheckTextRdBoton> auxiliar = new ArrayList<crearCheckTextRdBoton>();

					group = new ButtonGroup();
					panelRdBotones.removeAll();
					for (crearCheckTextRdBoton boton : rdBotones) {
						for (Component componente : boton.getComponents()) {
							if (componente instanceof JCheckBox) {
								if (!((JCheckBox) componente).isSelected()) {
									auxiliar.add(boton);
									panelRdBotones.add(boton);
									group.add(boton.boton);
									break;
								}
							}
						}
					}

					rdBotones = auxiliar;

					if (panelRdBotones.getComponentCount() < 3) {
						for (int i = 0; i < (3 - panelRdBotones.getComponentCount()); i++) {
							rdBotones.add(new crearCheckTextRdBoton(""));
						}
						for (crearCheckTextRdBoton boton : rdBotones) {
							panelRdBotones.add(boton);
							group.add(boton.boton);
						}
						btnSeleccionarTodos.setText("Seleccionar todos");
					}
					panelRdBotones.updateUI();
				}
			});
			panelAgregarEliminarOpciones.add(Eliminar, BorderLayout.EAST);

			panelCentral.add(Box.createRigidArea(new Dimension(0, 10)));

			// panel central inferior

			JPanel panelCentralInferior = new JPanel(new BorderLayout());
			panelCentral.add(panelCentralInferior);

			JLabel ingresePeso = new JLabel("Edite el puntaje en el siguiente cuadro:");
			ingresePeso.setFont(new Font("Segoe UI", Font.PLAIN, 25));
			panelCentralInferior.add(ingresePeso, BorderLayout.NORTH);

			JTextField peso = new JTextField(5);
			peso.setFont(new Font("Segoe UI", Font.PLAIN, 20));
			peso.setTransferHandler(null);
			peso.setText(String.valueOf(pregunta.getPeso()));
			peso.addKeyListener(new KeyAdapter() {
				@Override
				public void keyTyped(KeyEvent e) {
					int cantidadMaximaCaracteres = 3;

					char letraIngresada = e.getKeyChar();

					if (!((letraIngresada >= '0') && (letraIngresada <= '9') || (letraIngresada == '-')
							|| (letraIngresada == '+') || (letraIngresada == KeyEvent.VK_BACK_SPACE)
							|| (letraIngresada == KeyEvent.VK_DELETE))) {
						peso.setToolTipText("Por favor ingrese numeros entre -9999 y 9999.");
						e.consume();
					}

					if ((letraIngresada == '-' || letraIngresada == '+') && (peso.getText().length() > 0)) {
						e.consume();
					}

					if ((peso.getText().length() > 0)
							&& (peso.getText().charAt(0) == '-' || peso.getText().charAt(0) == '+')) {
						cantidadMaximaCaracteres = 4;
					}

					if (peso.getText().length() > cantidadMaximaCaracteres) {
						e.consume();
					}
				}
			});
			panelCentralInferior.add(peso, BorderLayout.CENTER);

			panelCentral.add(Box.createRigidArea(new Dimension(0, 10)));

			String[] botones = { "Aceptar", "Cancelar" };

			int eleccion = JOptionPane.showOptionDialog(new JFrame(), panel, titulo, JOptionPane.YES_NO_OPTION,
					JOptionPane.PLAIN_MESSAGE, null, botones, botones[0]);

			if (eleccion == JOptionPane.YES_OPTION) {

				if (group != null && group.getSelection() != null) {

					if (text == null || text.getText().isBlank()) {
						error("Por favor ingrese una pregunta.");
					}

					int respuestaSM = 0;

					List<String> opcionesProfesor = new ArrayList<String>();

					for (int i = 0; i < rdBotones.size(); i++) {
						if (rdBotones.get(i).boton.isSelected() && !rdBotones.get(i).texto.getText().isBlank()) {
							opcionesProfesor.add(rdBotones.get(i).texto.getText());
							respuestaSM = opcionesProfesor.indexOf(rdBotones.get(i).texto.getText());

						} else if (!rdBotones.get(i).boton.isSelected()
								&& !rdBotones.get(i).texto.getText().isBlank()) {
							opcionesProfesor.add(rdBotones.get(i).texto.getText());

						} else if (rdBotones.get(i).boton.isSelected() && rdBotones.get(i).texto.getText().isBlank()) {
							error("Por favor, no marque una alternativa vacia como correcta.");
							return editarPreguntaSeleccionMultiple(pregunta, titulo);
						}
					}

					if (opcionesProfesor.isEmpty()) {
						error("Por favor, ingrese el texto de una alternativa, y seleccione la respuesta correcta.");
						return editarPreguntaSeleccionMultiple(pregunta, titulo);
					}

					if (opcionesProfesor.size() < 3) {
						error("Por favor, ingrese 3 o mas alternativas.");
						return editarPreguntaSeleccionMultiple(pregunta, titulo);
					}

					String textoPeso = peso.getText();

					int pesoPregunta = 0;

					if (textoPeso == null || textoPeso.isBlank()) {
						Mensajes.error(
								"Por favor, solo ingresa NUMEROS, el texto u otro tipo de caracteres NO sirven.");
						return editarPreguntaSeleccionMultiple(pregunta, titulo);
					}

					try {
						pesoPregunta = Integer.parseInt(textoPeso);
					} catch (Exception e2) {
						error("Por favor, solo ingresa NUMEROS, el texto u otro tipo de caracteres NO sirven.");
						return editarPreguntaSeleccionMultiple(pregunta, titulo);
					}

					return new SeleccionMultiple(text.getText(), opcionesProfesor.toArray(new String[0]), respuestaSM,
							pesoPregunta);
				} else {
					error("Por favor agregue y seleccione una alternativa.");
					return editarPreguntaSeleccionMultiple(pregunta, titulo);
				}

			} else {
				return null;
			}
		} else {
			error("¡Error 404, pregunta no encontrada!");
			return null;
		}
	}

	/**
	 * Este metodo se encarga de mostrar un editor de preguntas de tipo verdadero
	 * falso.
	 * 
	 * @param pregunta es la pregunta de tipo verdadero falso que se desea editar.
	 * @return retorna la pregunta modificada. Si no fue modificada, retornará
	 *         {@code null}, caso contrario, retorna la pregunta modificada.
	 */
	public static VerdaderoFalso editarPreguntaVerdaderoFalso(VerdaderoFalso pregunta) {

		JPanel panel = new JPanel(new BorderLayout(0, 10));
		panel.setBorder(new EmptyBorder(new Insets(20, 40, 20, 40)));

		// panel central

		// panel del centro

		JPanel panelCentral = new JPanel();
		panelCentral.setLayout(new BoxLayout(panelCentral, BoxLayout.Y_AXIS));
		panel.add(panelCentral, BorderLayout.CENTER);

		// panel central superior

		JPanel panelCentralSuperior = new JPanel(new BorderLayout());
		panelCentral.add(panelCentralSuperior);

		JLabel ingresePregunta = new JLabel("Edite la pregunta en el siguiente cuadro:");
		ingresePregunta.setFont(new Font("Segoe UI", Font.PLAIN, 25));
		panelCentralSuperior.add(ingresePregunta, BorderLayout.NORTH);

		JScrollPane scrollPane = new JScrollPane();
		panelCentralSuperior.add(scrollPane, BorderLayout.CENTER);

		JTextArea text = new JTextArea(7, 30);
		text.setLineWrap(true);
		text.setWrapStyleWord(true);
		text.setText(pregunta.getPregunta());
		text.setEditable(true);
		scrollPane.setViewportView(text);

		panelCentral.add(Box.createRigidArea(new Dimension(0, 10)));

		// panel central inferior

		JPanel panelCentralCentral = new JPanel(new BorderLayout());
		panelCentral.add(panelCentralCentral);

		JLabel seleccioneRespuesta = new JLabel("Edite las opciones en el siguiente cuadro:");
		seleccioneRespuesta.setFont(new Font("Segoe UI", Font.PLAIN, 25));
		panelCentralCentral.add(seleccioneRespuesta, BorderLayout.NORTH);

		group = new ButtonGroup();

		JPanel panelRespuestaVerdaderoFalso = new JPanel();
		panelCentralCentral.add(panelRespuestaVerdaderoFalso, BorderLayout.SOUTH);

		JRadioButton rdbtnVerdadero = new JRadioButton("Verdadero");
		rdbtnVerdadero.setActionCommand("Verdadero");
		group.add(rdbtnVerdadero);
		panelRespuestaVerdaderoFalso.add(rdbtnVerdadero);

		JRadioButton rdbtnFalso = new JRadioButton("Falso");
		rdbtnFalso.setActionCommand("Falso");
		group.add(rdbtnFalso);
		panelRespuestaVerdaderoFalso.add(rdbtnFalso);

		if (pregunta.getRespuesta() == true) {
			rdbtnVerdadero.setSelected(true);
			rdbtnFalso.setSelected(false);
		} else if (pregunta.getRespuesta() == false) {
			rdbtnVerdadero.setSelected(false);
			rdbtnFalso.setSelected(true);
		}

		panelCentral.add(Box.createRigidArea(new Dimension(0, 10)));

		// panel central inferior

		JPanel panelCentralInferior = new JPanel(new BorderLayout());
		panelCentral.add(panelCentralInferior);

		JLabel ingresePeso = new JLabel("Edite el puntaje en el siguiente cuadro:");
		ingresePeso.setFont(new Font("Segoe UI", Font.PLAIN, 25));
		panelCentralInferior.add(ingresePeso, BorderLayout.NORTH);

		JTextField peso = new JTextField(5);
		peso.setFont(new Font("Segoe UI", Font.PLAIN, 20));
		peso.setTransferHandler(null);
		peso.setText(String.valueOf(pregunta.getPeso()));
		peso.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				int cantidadMaximaCaracteres = 3;

				char letraIngresada = e.getKeyChar();

				if (!((letraIngresada >= '0') && (letraIngresada <= '9') || (letraIngresada == '-')
						|| (letraIngresada == '+') || (letraIngresada == KeyEvent.VK_BACK_SPACE)
						|| (letraIngresada == KeyEvent.VK_DELETE))) {
					peso.setToolTipText("Por favor ingrese numeros entre -9999 y 9999.");
					e.consume();
				}

				if ((letraIngresada == '-' || letraIngresada == '+') && (peso.getText().length() > 0)) {
					e.consume();
				}

				if ((peso.getText().length() > 0)
						&& (peso.getText().charAt(0) == '-' || peso.getText().charAt(0) == '+')) {
					cantidadMaximaCaracteres = 4;
				}

				if (peso.getText().length() > cantidadMaximaCaracteres) {
					e.consume();
				}
			}
		});
		panelCentralInferior.add(peso, BorderLayout.CENTER);

		panelCentral.add(Box.createRigidArea(new Dimension(0, 10)));

		// ---------------------------------------------------------------------------------------------------------------------

		String[] botones = { "Aceptar", "Cancelar" };

		int eleccion = JOptionPane.showOptionDialog(new JFrame(), panel, "", JOptionPane.YES_NO_OPTION,
				JOptionPane.PLAIN_MESSAGE, null, botones, botones[0]);

		if (eleccion == JOptionPane.YES_OPTION) {

			boolean respuestaVF = false;

			if (group != null && group.getSelection() != null) {

				if (text == null || text.getText().isBlank()) {
					error("Por favor ingrese una pregunta.");
				}

				respuestaVF = (group.getSelection().getActionCommand().equals("Verdadero") ? true : false);

				String textoPeso = peso.getText();

				int pesoPregunta = 0;

				if (textoPeso == null || textoPeso.isBlank()) {
					Mensajes.error("Por favor, solo ingresa NUMEROS, el texto u otro tipo de caracteres NO sirven.");
					return editarPreguntaVerdaderoFalso(pregunta);
				}

				try {
					pesoPregunta = Integer.parseInt(textoPeso);
				} catch (Exception e2) {
					error("Por favor, solo ingresa NUMEROS, el texto u otro tipo de caracteres NO sirven.");
					return editarPreguntaVerdaderoFalso(pregunta);
				}

				return new VerdaderoFalso(text.getText(), respuestaVF, pesoPregunta);
			} else {
				error("Por favor, marque alguna alternativa como correcta.");
				return editarPreguntaVerdaderoFalso(pregunta);
			}

		} else {
			return null;
		}
	}

	/**
	 * Este metodo se encarga de mostrar el texto ingresado, como un mensaje de
	 * error emergente.
	 * 
	 * @param mensaje es el texto que se desea mostrar en la ventana.
	 */
	public static void error(String mensaje) {
		JLabel label = new JLabel(mensaje);
		label.setFont(new Font("Segoe ui", Font.BOLD, 20));
		JOptionPane.showMessageDialog(null, label, "Error!", JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * Este metodo se encarga de mostrar el texto ingresado, como un mensaje
	 * emergente.
	 * 
	 * @param textoMensaje es el texto que se desea mostrar en la ventana.
	 */
	public static void mensaje(String textoMensaje) {
		JLabel label = new JLabel(textoMensaje);
		label.setFont(new Font("Segoe ui", Font.BOLD, 20));
		JOptionPane.showMessageDialog(null, label, "Ayuda", JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * Esta clase, permite crear paneles con 2 items al mismo tiempo (JCheckBox y
	 * JTextField).
	 */
	private static class crearCheckField extends JPanel {

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

	/**
	 * Esta clase, permite crear paneles con 3 items al mismo tiempo (JRadioButton,
	 * JCheckBox, JTextField).
	 */
	private static class crearCheckTextRdBoton extends JPanel {
		private static final long serialVersionUID = 2245540280370839626L;
		private JRadioButton boton;
		private JCheckBox check;
		private JTextField texto;

		private crearCheckTextRdBoton(String textoTextField) {
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

}
