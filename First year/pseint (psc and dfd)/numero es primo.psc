Proceso es_primo_o_no
	Escribir "ingrese un numero por favor";
	Leer a;
	
	cont<-0;
	
	para i<-1 hasta a hacer
		si a%i=0 entonces
			cont<-cont+1;
		FinSi
	FinPara
	
	si cont=2 Entonces
		Escribir a," es un numero primo";
	SiNo
		Escribir a," no es un numero primo";
	FinSi
FinProceso
