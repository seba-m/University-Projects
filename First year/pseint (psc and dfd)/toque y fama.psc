Algoritmo toque_y_fama_modulos
	seguir <- 0
    Mientras seguir = 0
		Escribir "toque y fama"
		toqueyfama()
		Escribir "por lo que veo has llegado hasta aqui, deseas continuar jugando= 1=si, 2=no"
		Leer seguir
		Si (seguir=1)
		Sino
			seguir = seguir-1
		FinSi
	FinMientras
FinAlgoritmo

SubProceso  comprobacionnumeros(m Por Referencia,c Por Referencia,d Por Referencia,u Por Referencia) 
    valido = 1
    Mientras valido=1
        milesima = 0
        centecima = 0
        decena = 0
        unidad = 0
        Leer n
        Si (n >=100 y n <=9999)
			Mientras n>=1000
				n = n-1000
				milesima = milesima+1
			FinMientras
			Mientras n>=100
				n = n-100
				centecima = centecima+1
			FinMientras
			Mientras n>=10
				n = n-10
				decena = decena+1
			FinMientras
			Mientras n>=1
				n = n-1
				unidad = unidad+1
			FinMientras
			Si  No (milesima=centecima O milesima=decena O milesima=unidad O centecima=decena O centecima=unidad O decena=unidad )
				valido = 0
			Sino
				Escribir "Porfavor, los digitos del numero deben ser diferentes entre si"
			FinSi
			Sino
				Escribir "el numero ingresado no es valido, porfavor ingresa un numero entre 0 y 9999"
        FinSi
    FinMientras
	m <- milesima
	c <- centecima
	d <- decena
	u <- unidad
FinSubProceso

Funcion toqueyfama()
    int = 10
	acertado = 0
    numerosrandom( random1,random2,random3,random4 )
	Escribir random1,random2,random3,random4
    Mientras int>=1 y acertado <> 1
        toque = 0
        fama = 0
        comprobacionnumeros(milesima,centena,decena,unidad)
        Si ((milesima != random1) o (centena !=random2) o (decena !=random3) o (unidad !=random4))
            Si (milesima==random1)
				fama = fama + 1
            Sino
			Si (milesima==random2 O milesima==random3 O milesima==random4)
				toque = toque + 1
			FinSi
            FinSi
            Si (centena==random2)
                fama = fama + 1
            Sino
				Si (centena==random1 O centena==random3 O centena==random4)
                    toque = toque + 1
                FinSi
            FinSi
            Si (decena==random3)
                fama = fama + 1
            Sino
				Si (decena==random1 O decena==random2 O decena==random4)
                    toque = toque + 1
                FinSi
            FinSi
            Si (unidad==random4)
                fama = fama + 1
            Sino
				Si (unidad == random1 O unidad==random2 O unidad == random3)
                    toque = toque + 1
                FinSi
            FinSi
            int = int - 1
            Escribir "Tienes ',toque,' toques y ',fama,' famas , te quedan ',int,' intentos restantes y tu numero fue ',milesima,'',centena,'',decena,'',unidad,'"
        Sino
			Escribir "felicidades, acertaste al numero ",milesima,centena,decena,unidad
			acertado = 1
        FinSi
    FinMientras
FinFuncion

Funcion numerosrandom(random1 Por Referencia,random2 Por Referencia,random3 Por Referencia,random4 Por Referencia)
    nr1 <- azar(10)
    nr2 <- azar(10)
    nr3 <- azar(10)
    nr4 <- azar(10)
    Mientras nr1=nr2 O nr1=nr3 O nr1=nr4 O nr2=nr3 O nr2=nr4 O nr3=nr4
        nr1 <- azar(10)
        nr2 <- azar(10)
        nr3 <- azar(10)
        nr4 <- azar(10)
    FinMientras
    random1 = nr1
    random2 = nr2
    random3 = nr3
    random4 = nr4
FinFuncion

