Proceso NumerosPares
	Escribir "Los numeros pares entre 1 y 100: ";
	num <-0;
	contador<-0;  
	Mientras num <99 Hacer
		num <- num + 1;
		Si num mod 2 = 0 Entonces
			contador <- contador+1;
		SiNo
			
		FinSi
	FinMientras 
	
	Escribir  "la cantidad total de numeros que hay en total es:" ,contador;
FinProceso
