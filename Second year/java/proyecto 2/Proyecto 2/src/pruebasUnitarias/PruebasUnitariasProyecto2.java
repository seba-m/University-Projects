package pruebasUnitarias;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import base.Archivos;
import utilidadesArchivos.ExportarAExcel;

/**
 * Esta clase estan las pruebas unitarias.
 * 
 * @author Sebastian Morgado
 * @version 2.0.0
 * @since 2.0.0
 */
class PruebasUnitariasProyecto2 { // NO_UCD (unused code)

	@Test
	void testExcel() {
		Exception exception = assertThrows(RuntimeException.class, () -> {
			ExportarAExcel.exportarAExcel(null, null);
		});

		String expectedMessage = "Ha ocurrido un error al guardar la tabla";
		String actualMessage = exception.getMessage();

		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	void testCrearArchivos() {
		Exception exception = assertThrows(RuntimeException.class, () -> {
			Archivos.crearYEscribirEnArchivo(null, null, 0);
		});

		String expectedMessage = "Ha ocurrido un error al crear el archivo.";
		String actualMessage = exception.getMessage();

		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	void testGuardarResultados() {
		Exception exception = assertThrows(RuntimeException.class, () -> {
			Archivos.guardarResultados(null, null, null, null, null, null, null, null, null, null, null, null);
		});

		String expectedMessage = "Ha ocurrido un error al crear el archivo";
		String actualMessage = exception.getMessage();

		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	void testleerArchivoAlterado() {
		Exception exception = assertThrows(RuntimeException.class, () -> {
			Archivos.leerArchivoAlterado("", null);
		});

		String expectedMessage = "Ha ocurrido un error";
		String actualMessage = exception.getMessage();

		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	void testleerResultadosAlumnos() {
		Exception exception = assertThrows(RuntimeException.class, () -> {
			Archivos.LeerDatosDeCarpeta("", null);
		});

		String expectedMessage = "No se ha podido abrir/leer la llave";
		String actualMessage = exception.getMessage();

		assertTrue(actualMessage.contains(expectedMessage));
	}
}
