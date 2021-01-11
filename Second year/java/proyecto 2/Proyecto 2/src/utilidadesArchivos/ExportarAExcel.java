package utilidadesArchivos;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;

import javax.swing.JTable;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Esta clase se encarga de hacer todo lo relacionado con los archivos de
 * formato excel, o sea, exportar una tabla a un documento excel.
 * 
 * @author Sebastian Morgado
 * @version 2.0.0
 * @since 2.0.0
 */
public class ExportarAExcel {

	/**
	 * Este metodo se encarga de convertir la tabla, a un documento excel.
	 * 
	 * @param tabla     es la tabla la cual contiene todos los datos que se quieren
	 *                  exportar.
	 * @param ubicacion es la ubicacion en donde se desea guardar el archivo
	 *                  generado.
	 */
	public static void exportarAExcel(JTable tabla, String ubicacion) {
		try {
			Workbook documentoBase = new XSSFWorkbook();
			Sheet hojaDocumento = documentoBase.createSheet("Resultados Examen");
			Font fuenteHojaDocumento = documentoBase.createFont();
			CellStyle estiloCelda = documentoBase.createCellStyle();
			fuenteHojaDocumento.setBold(true);

			TableModel modelo = tabla.getModel();

			// con esto se crea la primera fila (donde estan los nombres, etc.)
			TableColumnModel tcm = tabla.getColumnModel();
			Row hRow = hojaDocumento.createRow((short) 0);
			for (int i = 0; i < tcm.getColumnCount(); i++) {
				Cell celda = hRow.createCell((short) i);
				celda.setCellValue(tcm.getColumn(i).getHeaderValue().toString());
				celda.setCellStyle(estiloCelda);
			}

			// con esto se crea rellena todas las columnas.
			for (int i = 0; i < modelo.getRowCount(); i++) {
				Row fRow = hojaDocumento.createRow((short) i + 1);
				for (int j = 0; j < modelo.getColumnCount(); j++) {
					Cell celda = fRow.createCell((short) j);
					celda.setCellValue(modelo.getValueAt(i, j).toString());
					celda.setCellStyle(estiloCelda);
				}
			}

			FileOutputStream fileOutputStream = new FileOutputStream(ubicacion + ".xlsx");

			try (BufferedOutputStream bos = new BufferedOutputStream(fileOutputStream)) {
				documentoBase.write(bos);
			}
			documentoBase.close();
			fileOutputStream.close();
		} catch (Exception e) {
			throw new IllegalArgumentException(
					"<html>Ha ocurrido un error al guardar la tabla<br>Por favor, cierre todos los archivos de excel<br>que tengan el mismo nombre que el<br>archivo.</html>");
		}

	}
}
