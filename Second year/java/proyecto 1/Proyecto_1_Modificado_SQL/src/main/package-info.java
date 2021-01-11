/**
	<H1>Este paquete contiene un menu, el cual puede usar para poder ir a las opciones del profesor y del alumno.</H1>

	<h2>El contenido que hay en el menu de profesor es:</h2>
	<ol>
		<li>crear preguntas.</li>
		<li>borrar preguntas.</li>
		<li>manejar los resultados de los alumnos.</li>
	</ol>

	<H1>El contenido que hay en el menu de alumno es:</H1>
	<ol>
		<li>responder preguntas generadas por el codigo</li>
		<li>responder preguntas generadas por el profesor</li>
	</ol>

	<h2>lista de caracteristicas que tiene implementado el proyecto:</h2>
	<ol>
		<li>crear preguntas ({@link base.Examen#Agregar_Preguntas_Examen Crear Preguntas}).</li>
		<li>crear archivos ({@link base.Archivos#Crear_Archivo Crear Archivos}).</li>
		<li>guardar las preguntas en archivos ({@link base.Examen#convertir_a_smr Guardar Preguntas}).</li>
		<li>interpretar las preguntas de los archivos ({@link base.Archivos#Interpretar_Archivo Interpretar Preguntas}).</li>
		<li>borrar preguntas ({@link base.Examen#Eliminar_Preguntas_Examen Eliminar Preguntas}).</li>
		<li>calcular notas con escalas de notas ({@link base.Examen#calcular_nota calcular_nota}).</li>
		<li>desordernar items y preguntas ({@link base.Examen#desordenar_todo desordenar_todo}).</li>
		<li>establecer limite de tiempo ({@link base.Examen#convertir_a_smr En convertir_a_smr al generar las opciones}).</li>
		<li>generar archivos con las respuestas ({@link base.Archivos#Guardar_Resultados Guardar_Resultados}).</li>
		<li>ordenar los datos obtenidos de los archivos de las respuestas ({@link base.RevisionAlumno#OrdenarDatosAlumnos OrdenarDatosAlumnos}).</li>
		<li>iniciar sesión con rut y contraseña ({@link base.ServidorSql#inicio_sesion inicio sesión}).</li>
		<li>crear usuarios, con contraseña ({@link base.ServidorSql#crear_cuenta crear cuenta}).</li>
		<li>recuperar contraseña ({@link base.ServidorSql#cambiar_contrasena cambiar_contraseña}).</li>
	</ol>

	<h2>lista de conceptos vistos en clase que incorpora el codigo:</h2>
	<ol>
		<li>Encapsulamiento. (todas las clases del proyecto, por ejemplo {@link base.Pregunta Pregunta}).</li>
		<li>Abstract. ({@link base.Pregunta#buscar() Pregunta.buscar}).</li>
		<li>Herencia. (de la clase verdaderofalso, respuestascortas y seleccionmultiple ({@link base.SeleccionMultiple#buscar() SeleccionMultiple.buscar}, {@link base.VerdaderoFalso#buscar() VerdaderoFalso.buscar} y {@link base.RespuestasCortas#buscar() RespuestasCortas.buscar}).</li>
		<li>Polimorfismo. (vea la clase Exam.java ({@link base.Examen#agregaPregunta(VerdaderoFalso) Examen.agregaPregunta(VerdaderoFalso)}, {@link base.Examen#agregaPregunta(SeleccionMultiple) Examen.agregaPregunta(SeleccionMultiple)} y {@link base.Examen#agregaPregunta(RespuestasCortas) Examen.agregaPregunta(RespuestasCortas)}).</li>
		<li>Excepciones. (casi todo el codigo tiene excepciones, por ejemplo en {@link base.SeleccionMultiple#buscar() SeleccionMultiple.buscar}, hay try catch, los cuales sirven para evitar que el usuario ingrese letras en vez de numeros).</li>
		<li>Interfaces. ({@link base.IComparar IComparar}).</li>
	</ol>
 	
 	@author Sebastian Morgado
 	@version 1.6.1
 	@since 1.0.0
 */

package main;