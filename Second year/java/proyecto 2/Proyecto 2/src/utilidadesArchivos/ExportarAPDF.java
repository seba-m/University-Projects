package utilidadesArchivos;

import java.awt.Color;
import java.io.FileOutputStream;

import javax.swing.JPanel;

import org.icepdf.ri.common.MyAnnotationCallback;
import org.icepdf.ri.common.SwingController;
import org.icepdf.ri.common.SwingViewBuilder;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.FontFactory;
import com.lowagie.text.List;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import base.Examen;
import base.RespuestaNumerica;
import base.RespuestasCortas;
import base.SeleccionMultiple;
import base.VerdaderoFalso;

/**
 * Esta clase se encarga de hacer todo lo relacionado con los archivos PDF
 * (crear archivos pdf y mostrar en pantalla el archivo).
 * 
 * @author Sebastian Morgado
 * @version 2.0.0
 * @since 2.0.0
 */
public class ExportarAPDF {

	/**
	 * Este metodo se encarga de convertir el examen, a un documento pdf.
	 * 
	 * @param examen           es el examen el cual contiene todas las preguntas que
	 *                         se desean almacenar.
	 * @param ubicacionArchivo es la ubicacion en donde se guardara el archivo.
	 * @param titulo           es el titulo que tendrá el examen.
	 * @param nombreProfesor   es el nombre del profesor que se ingresara al examen.
	 * @param apellidoProfesor es el apellido del profesor que se ingresara al
	 *                         examen.
	 * @param fechaExamen      es la fecha en la que se realizara el examen.
	 * @param claseExamen      es la clase/materia del examen.
	 * @param cursoExamen      es el curso el cual realizara el examen.
	 * @param objetivos        son los objetivos que se mostraran para el alumno en
	 *                         el examen.
	 * 
	 */
	public static void exportarAPDF(String ubicacionArchivo, Examen examen, String titulo, String nombreProfesor,
			String apellidoProfesor, String fechaExamen, String claseExamen, String cursoExamen, String objetivos) {
		if (examen != null) {
			try {

				if (titulo == null || titulo.isBlank()) {
					titulo = "Prueba";
				}

				if (nombreProfesor == null || nombreProfesor.isBlank()) {
					nombreProfesor = "";
				}

				if (apellidoProfesor == null || apellidoProfesor.isBlank()) {
					apellidoProfesor = "";
				}

				if (fechaExamen == null || fechaExamen.isBlank()) {
					fechaExamen = "";
				}

				if (claseExamen == null || claseExamen.isBlank()) {
					claseExamen = "";
				}

				if (cursoExamen == null || cursoExamen.isBlank()) {
					cursoExamen = "";
				}

				if (objetivos == null || objetivos.isBlank()) {
					objetivos = "";
				}

				int numeroPregunta = 1;

				Document document = new Document();
				PdfWriter.getInstance(document, new FileOutputStream(ubicacionArchivo + ".pdf"));
				document.open();

				// titulo: ----------------------------------------------

				Paragraph tituloAEscribir = new Paragraph(titulo,
						FontFactory.getFont(FontFactory.HELVETICA, 25, Color.black));
				tituloAEscribir.setAlignment(Chunk.ALIGN_CENTER);
				document.add(tituloAEscribir);

				document.add(new Phrase(System.lineSeparator()));

				// tabla con datos: ------------------------

				PdfPTable table = new PdfPTable(8);
				table.setSpacingBefore(8);
				table.setWidthPercentage(100);

				table.addCell("Nombre estudiante:");

				PdfPCell nombreEstudianteCelda = new PdfPCell();
				nombreEstudianteCelda.setColspan(4);
				table.addCell(nombreEstudianteCelda);

				table.addCell("Clase:");

				PdfPCell clase = new PdfPCell(new Phrase(claseExamen));
				clase.setColspan(2);
				table.addCell(clase);

				table.addCell("Nombre profesor:");

				PdfPCell nombreProfesorCelda = new PdfPCell(new Phrase(nombreProfesor + " " + apellidoProfesor));
				nombreProfesorCelda.setColspan(4);
				table.addCell(nombreProfesorCelda);

				table.addCell("Fecha:");

				PdfPCell fecha = new PdfPCell(new Phrase(fechaExamen));
				fecha.setColspan(2);
				table.addCell(fecha);

				table.addCell("Curso:");
				table.addCell(cursoExamen);
				table.addCell("Puntaje total:");
				table.addCell(String.valueOf(examen.obtener_puntaje_total()));

				table.addCell("Puntaje obtenido:");
				table.addCell("");
				table.addCell("Nota:");
				table.addCell("");
				document.add(table);

				// -----------------------------------------objetivos-------------------------------------------------

				document.add(new Phrase(System.lineSeparator()));

				Chunk objetivosAEscribir = new Chunk(objetivos,
						FontFactory.getFont(FontFactory.HELVETICA, 11, Color.black));
				document.add(new Phrase(objetivosAEscribir));

				document.add(new Phrase(System.lineSeparator()));
				document.add(new Phrase(System.lineSeparator()));

				// -----------------------------------------preguntas-------------------------------------------

				if (examen.preguntas_SM != null && examen.preguntas_SM.size() >= 1) {

					Chunk enunciadoSeleccionMultiple = new Chunk(
							"Encierra con un círculo la alternativa correcta (puntaje total: "
									+ examen.obtenerPuntajeTotalSeleccionMultiple() + " puntos).",
							FontFactory.getFont(FontFactory.HELVETICA, 15, Color.black));
					document.add(enunciadoSeleccionMultiple);
					document.add(new Phrase(System.lineSeparator()));
					document.add(new Phrase(System.lineSeparator()));

					for (SeleccionMultiple pregunta : examen.preguntas_SM) {
						Chunk preguntaSeleccionMultiple = new Chunk(numeroPregunta + ") " + pregunta.getPregunta()
								+ " (" + pregunta.getPeso() + " puntos).",
								FontFactory.getFont(FontFactory.HELVETICA, 12, Color.black));

						document.add(preguntaSeleccionMultiple);

						document.add(new Phrase(System.lineSeparator()));

						List lista = new List(List.ORDERED);
						lista.setFirst(1);
						for (String string : pregunta.getOpciones()) {
							lista.add(string);
						}
						document.add(lista);
						document.add(new Phrase(System.lineSeparator()));
						numeroPregunta++;
					}
					document.add(new Phrase(System.lineSeparator()));
				}

				if (examen.preguntas_VF != null && examen.preguntas_VF.size() >= 1) {

					Chunk enunciadoVerdaderoFalso = new Chunk(
							"Completa con Verdadero (o v) si la expresión es verdadera, y Falso (o f) si es falso (puntaje total: "
									+ examen.obtenerPuntajeTotalVerdaderoFalso() + " puntos).",
							FontFactory.getFont(FontFactory.HELVETICA, 15, Color.black));
					document.add(enunciadoVerdaderoFalso);
					document.add(new Phrase(System.lineSeparator()));
					document.add(new Phrase(System.lineSeparator()));

					for (VerdaderoFalso pregunta : examen.preguntas_VF) {
						Chunk preguntaVerdaderoFalso = new Chunk(
								numeroPregunta + ") ________________ " + pregunta.getPregunta() + " ("
										+ pregunta.getPeso() + " puntos).",
								FontFactory.getFont(FontFactory.HELVETICA, 12, Color.black));
						document.add(preguntaVerdaderoFalso);
						document.add(new Phrase(System.lineSeparator()));
						numeroPregunta++;
					}
					document.add(new Phrase(System.lineSeparator()));
				}

				if (examen.preguntas_RC != null && examen.preguntas_RC.size() >= 1) {

					Chunk enunciadoRespuestasCortas = new Chunk(
							"Escriba en el espacio designado, su respuesta (puntaje total: "
									+ examen.obtenerPuntajeTotalRespuestasCortas() + " puntos).",
							FontFactory.getFont(FontFactory.HELVETICA, 15, Color.black));
					document.add(enunciadoRespuestasCortas);
					document.add(new Phrase(System.lineSeparator()));
					document.add(new Phrase(System.lineSeparator()));

					for (RespuestasCortas pregunta : examen.preguntas_RC) {
						Chunk preguntaRespuestasCortas = new Chunk(numeroPregunta + ") " + pregunta.getPregunta() + " ("
								+ pregunta.getPeso() + " puntos).",
								FontFactory.getFont(FontFactory.HELVETICA, 12, Color.black));
						document.add(preguntaRespuestasCortas);
						document.add(new Phrase(System.lineSeparator()));
						document.add(new Phrase(System.lineSeparator()));
						document.add(new Phrase(System.lineSeparator()));
						document.add(new Phrase(System.lineSeparator()));
						document.add(new Phrase(System.lineSeparator()));

						numeroPregunta++;
					}
					document.add(new Phrase(System.lineSeparator()));
				}

				if (examen.preguntas_RM != null && examen.preguntas_RM.size() >= 1) {

					Chunk enunciadoRespuestasNumericas = new Chunk(
							"Escriba en el espacio designado su respuesta, con su respectivo desarrollo. (puntaje total: "
									+ examen.obtenerPuntajeTotalRespuestaNumerica() + " puntos).",
							FontFactory.getFont(FontFactory.HELVETICA, 15, Color.black));
					document.add(enunciadoRespuestasNumericas);
					document.add(new Phrase(System.lineSeparator()));
					document.add(new Phrase(System.lineSeparator()));

					for (RespuestaNumerica pregunta : examen.preguntas_RM) {
						Chunk preguntaRespuestasNumericas = new Chunk(numeroPregunta + ") " + pregunta.getPregunta()
								+ " (" + pregunta.getPeso() + " puntos).",
								FontFactory.getFont(FontFactory.HELVETICA, 12, Color.black));
						document.add(preguntaRespuestasNumericas);
						document.add(new Phrase(System.lineSeparator()));
						document.add(new Phrase(System.lineSeparator()));
						document.add(new Phrase(System.lineSeparator()));
						document.add(new Phrase(System.lineSeparator()));
						document.add(new Phrase(System.lineSeparator()));

						numeroPregunta++;
					}
					document.add(new Phrase(System.lineSeparator()));
				}

				document.close();

			} catch (Exception e) {
				throw new IllegalArgumentException("Ha ocurrido un error al inicializar el constructor de pdf.");
			}
		}

	}

	/**
	 * Este metodo se encarga de generar un panel, el cual contiene el archivo pdf
	 * cargado, lo que permite al alumno (y profesor) poder ver el contenido del pdf
	 * sin necesidad de una aplicación externa.
	 * 
	 * @param rutaArchivo es la ubicacion en donde se encuentra el pdf.
	 * 
	 * @return retorna el panel con el archivo pdf cargado.
	 */
	public static JPanel mostrarPDF(String rutaArchivo) {

		// construyo un controlador de componente
		SwingController controller = new SwingController();
		SwingViewBuilder factory = new SwingViewBuilder(controller);
		JPanel viewerComponentPanel = factory.buildViewerPanel();

		controller.getDocumentViewController()
				.setAnnotationCallback(new MyAnnotationCallback(controller.getDocumentViewController()));

		controller.openDocument(rutaArchivo);

		return viewerComponentPanel;
	}
}
