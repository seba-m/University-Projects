package utilidadesArchivos;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigInteger;

import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFAbstractNum;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFNumbering;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTAbstractNum;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLvl;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblLayoutType;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STNumberFormat;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTblLayoutType;

import base.Examen;
import base.RespuestaNumerica;
import base.RespuestasCortas;
import base.SeleccionMultiple;
import base.VerdaderoFalso;

/**
 * Esta clase se encarga de hacer todo lo relacionado con los archivos WORD
 * (crear archivos WORD).
 * 
 * @author Sebastian Morgado
 * @version 2.0.0
 * @since 2.0.0
 */
public class ExportarAWord {

	/**
	 * Este metodo se encarga de convertir el examen, a un documento docx (word).
	 * 
	 * @param examen           es el examen el cual contiene todas las preguntas que
	 *                         se desean almacenar.
	 * @param ubicacionArchivo es la ubicacion en donde se guardara el archivo.
	 * @param titulo           es el titulo que tendrá el examen.
	 * @param nombreProfesor   es el nombre del profesor que se ingresara al examen.
	 * @param apellidoProfesor es el apellido del profesor que se ingresara al
	 *                         examen.
	 * @param fechaExamen      es la fecha en la que se realizara el examen.
	 * @param clase            es la clase/materia del examen.
	 * @param curso            es el curso el cual realizara el examen.
	 * @param objetivos        son los objetivos que se mostraran para el alumno en
	 *                         el examen.
	 * 
	 *                         <br>
	 *                         <br>
	 * 
	 *                         <a href=
	 *                         "https://stackoverflow.com/questions/44433347/apache-poi-numbered-list">lista
	 *                         numerada</a>
	 */
	public static void examenAWord(String ubicacionArchivo, Examen examen, String titulo, String nombreProfesor,
			String apellidoProfesor, String fechaExamen, String clase, String curso, String objetivos) {
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

				if (clase == null || clase.isBlank()) {
					clase = "";
				}

				if (curso == null || curso.isBlank()) {
					curso = "";
				}

				if (objetivos == null || objetivos.isBlank()) {
					objetivos = "";
				}

				// con esto se crea la "base" del documento. //podria ser similar a un jframe?
				XWPFDocument document = new XWPFDocument();
				// con esto se crean parrafos. //podria ser similar a un jpanel?
				XWPFParagraph tmpParagraph = document.createParagraph();
				// con esto se crean las lineas que van en los parrafos. //podria ser similar a
				// un jlabel?
				XWPFRun tmpRun = tmpParagraph.createRun();

				int numeroPregunta = 1;

				tmpParagraph.addRun(tmpRun);
				tmpParagraph.setAlignment(ParagraphAlignment.CENTER);

				tmpRun.setText(titulo);
				tmpRun.setFontSize(18);
				tmpRun.addBreak();

				// creamos la tabla
				XWPFTable table = document.createTable(3, 8);
				table.setWidth("100%");
				CTTblLayoutType type = table.getCTTbl().getTblPr().addNewTblLayout();
				type.setType(STTblLayoutType.FIXED);

				// fila nombre alumno y clase
				table.getRow(0).getCell(0).setText("Nombre estudiante: ");
				table.getRow(0).getCell(5).setText("Clase: ");
				table.getRow(0).getCell(6).setText(clase);

				// fila nombre profesor y fecha
				table.getRow(1).getCell(0).setText("Nombre profesor: ");
				table.getRow(1).getCell(1).setText(nombreProfesor + " " + apellidoProfesor);
				table.getRow(1).getCell(5).setText("Fecha: ");
				table.getRow(1).getCell(6).setText(fechaExamen);

				// fila curso, puntaje tota, puntaje obtenido y nota
				table.getRow(2).getCell(0).setText("Curso:");
				table.getRow(2).getCell(1).setText(curso);
				table.getRow(2).getCell(2).setText("Puntaje total:");
				table.getRow(2).getCell(3).setText(String.valueOf(examen.obtener_puntaje_total()));
				table.getRow(2).getCell(4).setText("Puntaje obtenido:");
				table.getRow(2).getCell(5).setText("");
				table.getRow(2).getCell(6).setText("Nota:");
				table.getRow(2).getCell(7).setText("");

				// corrijo los espacios entre las tablas...
				mergeCellHorizontally(table, 0, 1, 4);
				mergeCellHorizontally(table, 0, 3, 4);
				mergeCellHorizontally(table, 1, 1, 4);
				mergeCellHorizontally(table, 1, 3, 4);

				tmpParagraph = document.createParagraph();
				tmpRun = tmpParagraph.createRun();
				tmpRun.addBreak();
				tmpRun.setText(objetivos);
				tmpRun.addBreak();
				// -------------------------------------------------------------------------------------------

				if (examen.preguntas_SM != null && examen.preguntas_SM.size() >= 1) {

					// crea parrafos
					tmpParagraph = document.createParagraph();
					// crea lineas
					tmpRun = tmpParagraph.createRun();
					tmpRun.setText("Encierra con un círculo la alternativa correcta (puntaje total: "
							+ examen.obtenerPuntajeTotalSeleccionMultiple() + " puntos).");
					tmpRun.setFontSize(15);
					tmpRun.addBreak();

					for (SeleccionMultiple pregunta : examen.preguntas_SM) {
						// crea parrafos
						tmpParagraph = document.createParagraph();
						// crea lineas
						tmpRun = tmpParagraph.createRun();
						tmpRun.setText(numeroPregunta + ") " + pregunta.getPregunta() + " (" + pregunta.getPeso()
								+ " puntos).");
						tmpRun.setFontSize(12);

						CTAbstractNum cTAbstractNum = CTAbstractNum.Factory.newInstance();
						cTAbstractNum.setAbstractNumId(BigInteger.valueOf(numeroPregunta));

						CTLvl cTLvl = cTAbstractNum.addNewLvl();
						cTLvl.addNewNumFmt().setVal(STNumberFormat.DECIMAL);
						cTLvl.addNewLvlText().setVal("%1.");
						cTLvl.addNewStart().setVal(BigInteger.valueOf(1));

						XWPFAbstractNum abstractNum = new XWPFAbstractNum(cTAbstractNum);
						XWPFNumbering numbering = document.createNumbering();

						BigInteger abstractNumID = numbering.addAbstractNum(abstractNum);
						BigInteger numID = numbering.addNum(abstractNumID);

						for (String string : pregunta.getOpciones()) {
							tmpParagraph = document.createParagraph();
							tmpParagraph.setNumID(numID);
							tmpRun = tmpParagraph.createRun();
							tmpRun.setText(string);
						}
						tmpRun.addBreak();
						numeroPregunta++;
					}
					tmpRun.addBreak();
				}

				if (examen.preguntas_VF != null && examen.preguntas_VF.size() >= 1) {

					// crea parrafos
					tmpParagraph = document.createParagraph();
					// crea lineas
					tmpRun = tmpParagraph.createRun();
					tmpRun.setText(
							"Completa con Verdadero (o v) si la expresión es verdadera, y Falso (o f) si es falso (puntaje total: "
									+ examen.obtenerPuntajeTotalVerdaderoFalso() + " puntos).");
					tmpRun.setFontSize(15);
					tmpRun.addBreak();

					for (VerdaderoFalso pregunta : examen.preguntas_VF) {
						// crea parrafos
						tmpParagraph = document.createParagraph();
						// crea lineas
						tmpRun = tmpParagraph.createRun();
						tmpRun.setText(numeroPregunta + ") ________________ " + pregunta.getPregunta() + " ("
								+ pregunta.getPeso() + " puntos).");
						tmpRun.setFontSize(12);

						CTAbstractNum cTAbstractNum = CTAbstractNum.Factory.newInstance();
						cTAbstractNum.setAbstractNumId(BigInteger.valueOf(numeroPregunta));

						CTLvl cTLvl = cTAbstractNum.addNewLvl();
						cTLvl.addNewNumFmt().setVal(STNumberFormat.DECIMAL);
						cTLvl.addNewLvlText().setVal("%1.");
						cTLvl.addNewStart().setVal(BigInteger.valueOf(1));

						tmpRun.addBreak();
						numeroPregunta++;
					}
					tmpRun.addBreak();
				}

				if (examen.preguntas_RC != null && examen.preguntas_RC.size() >= 1) {

					// crea parrafos
					tmpParagraph = document.createParagraph();
					// crea lineas
					tmpRun = tmpParagraph.createRun();
					tmpRun.setText("Escriba en el espacio designado, su respuesta (puntaje total: "
							+ examen.obtenerPuntajeTotalRespuestasCortas() + " puntos).");
					tmpRun.setFontSize(15);
					tmpRun.addBreak();
					tmpRun.addBreak();

					for (RespuestasCortas pregunta : examen.preguntas_RC) {
						// crea parrafos
						tmpParagraph = document.createParagraph();
						// crea lineas
						tmpRun = tmpParagraph.createRun();
						tmpRun.setText(numeroPregunta + ") " + pregunta.getPregunta() + " (" + pregunta.getPeso()
								+ " puntos).");
						tmpRun.setFontSize(12);

						CTAbstractNum cTAbstractNum = CTAbstractNum.Factory.newInstance();
						cTAbstractNum.setAbstractNumId(BigInteger.valueOf(numeroPregunta));

						CTLvl cTLvl = cTAbstractNum.addNewLvl();
						cTLvl.addNewNumFmt().setVal(STNumberFormat.DECIMAL);
						cTLvl.addNewLvlText().setVal("%1.");
						cTLvl.addNewStart().setVal(BigInteger.valueOf(1));

						tmpRun.addBreak();
						tmpRun.addBreak();
						tmpRun.addBreak();
						tmpRun.addBreak();
						tmpRun.addBreak();
						tmpRun.addBreak();
						tmpRun.addBreak();
						numeroPregunta++;
					}
					tmpRun.addBreak();
				}

				if (examen.preguntas_RM != null && examen.preguntas_RM.size() >= 1) {

					// crea parrafos
					tmpParagraph = document.createParagraph();
					// crea lineas
					tmpRun = tmpParagraph.createRun();
					tmpRun.setText(
							"Escriba en el espacio designado su respuesta, con su respectivo desarrollo (puntaje total: "
									+ examen.obtenerPuntajeTotalRespuestaNumerica() + " puntos).");
					tmpRun.setFontSize(15);
					tmpRun.addBreak();
					tmpRun.addBreak();

					for (RespuestaNumerica pregunta : examen.preguntas_RM) {
						// crea parrafos
						tmpParagraph = document.createParagraph();
						// crea lineas
						tmpRun = tmpParagraph.createRun();
						tmpRun.setText(numeroPregunta + ") " + pregunta.getPregunta() + " (" + pregunta.getPeso()
								+ " puntos).");
						tmpRun.setFontSize(12);

						CTAbstractNum cTAbstractNum = CTAbstractNum.Factory.newInstance();
						cTAbstractNum.setAbstractNumId(BigInteger.valueOf(numeroPregunta));

						CTLvl cTLvl = cTAbstractNum.addNewLvl();
						cTLvl.addNewNumFmt().setVal(STNumberFormat.DECIMAL);
						cTLvl.addNewLvlText().setVal("%1.");
						cTLvl.addNewStart().setVal(BigInteger.valueOf(1));

						tmpRun.addBreak();
						tmpRun.addBreak();
						tmpRun.addBreak();
						tmpRun.addBreak();
						tmpRun.addBreak();
						tmpRun.addBreak();
						tmpRun.addBreak();
						numeroPregunta++;
					}
					tmpRun.addBreak();
				}

				FileOutputStream fos = new FileOutputStream(new File(ubicacionArchivo + ".docx"));
				document.write(fos);
				document.close();
				fos.close();
			} catch (Exception e) {
				throw new IllegalArgumentException(
						"Ha ocurrido un error al guardar el examen en el documento. Intente cerrar todos los documentos docx abiertos.");
			}

		} else {
			throw new IllegalArgumentException("El documento está vacio, intentelo de nuevo.");
		}
	}

	/**
	 * Este metodo tiene por objetivo combinar celdas de manera horizontal.
	 * 
	 * @param tabla        es la tabla en donde se combinaran las celdas.
	 * @param fila         es la fila en donde se encuentra las celdas a combinar.
	 * @param desdeColumna es la posicion en donde se iniciara la combinacion.
	 * @param haciaColumna es la posicion en donde se finalizara la combinacion.
	 * 
	 *                     <br>
	 *                     <br>
	 *                     <a href=
	 *                     "https://stackoverflow.com/a/25016565/14820618">como
	 *                     combinar celdas en apache poi</a>
	 * 
	 */
	private static void mergeCellHorizontally(XWPFTable tabla, int fila, int desdeColumna, int haciaColumna) {
		/*- 
		* +-------+-------+		  	 +---------------+
		* |		  |       |   --->   |  	 		 |
		* +-------+-------+		  	 +---------------+
		*/

		try {
			XWPFTableCell cell = tabla.getRow(fila).getCell(desdeColumna);
			CTTcPr tcPr = cell.getCTTc().getTcPr();

			if (tcPr == null)
				tcPr = cell.getCTTc().addNewTcPr();
			if (tcPr.isSetGridSpan()) {
				tcPr.getGridSpan().setVal(BigInteger.valueOf(haciaColumna - desdeColumna + 1));
			} else {
				tcPr.addNewGridSpan().setVal(BigInteger.valueOf(haciaColumna - desdeColumna + 1));
			}
			for (int colIndex = haciaColumna; colIndex > desdeColumna; colIndex--) {
				tabla.getRow(fila).getCtRow().removeTc(colIndex);
				tabla.getRow(fila).removeCell(colIndex);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
}
