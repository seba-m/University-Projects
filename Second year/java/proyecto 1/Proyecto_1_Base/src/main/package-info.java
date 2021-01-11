/**
	<h2>lista de conceptos vistos en clase que incorpora el codigo:</h2>
	<ol>
		<li>Encapsulamiento. (todas las clases del proyecto, por ejemplo {@link main.Pregunta Pregunta}).</li>
		<li>Abstract. ({@link main.Pregunta#buscar() Pregunta.buscar}).</li>
		<li>Herencia. (de la clase TFpregunta, Resp_Cortas_Pregunta y Selec_Mul_Pregunta ({@link main.Selec_Mul_Pregunta#buscar() Selec_Mul_Pregunta.buscar}, {@link main.TFpregunta#buscar() TFpregunta.buscar} y {@link main.Resp_Cortas_Pregunta#buscar() Resp_Cortas_Pregunta.buscar}).</li>
		<li>Polimorfismo. (vea la clase Exam.java ({@link main.Exam#agregaPregunta(TFpregunta) Exam.agregaPregunta(TFpregunta)}, {@link main.Exam#agregaPregunta(Selec_Mul_Pregunta) Exam.agregaPregunta(Selec_Mul_Pregunta)} y {@link main.Exam#agregaPregunta(Resp_Cortas_Pregunta) Exam.agregaPregunta(Resp_Cortas_Pregunta)}).</li>
		<li>Excepciones. (casi todo el codigo tiene excepciones, por ejemplo en {@link main.Selec_Mul_Pregunta#buscar() Selec_Mul_Pregunta.buscar}, hay try catch, los cuales sirven para evitar que el usuario ingrese letras en vez de numeros).</li>
	</ol>
 	
 	@author Sebastian Morgado
 	@version 1.0.0
 	@since 1.0.0
 */

package main;