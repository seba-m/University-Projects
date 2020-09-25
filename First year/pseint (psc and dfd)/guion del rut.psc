Algoritmo guion
	
	Escribir "por favor ingrese su rut sin el numero despues del guion"
	Leer n
	ciclomm = 0
	ciclom = 0
	ciclo = 0
	cicl = 0
	cic = 0
	ci = 0
	c = 0
	u = 0
    Si 30000000 > n
		Si No(10000000 < n)
		SiNo
			Mientras (n>=10000000)
				ciclomm = ciclomm + 1
				n = n - 10000000
			FinMientras
		FinSi
		Si No(1000000 < n)
		SiNo
			Mientras (n >=1000000)
				ciclom = ciclom+1
				n = n-1000000
			FinMientras
		FinSi
		Si No (100000<n)
		SiNo
			Mientras (n>=100000)
				ciclo = ciclo + 1
				n = n - 100000
			FinMientras
		FinSi
		Si No (10000<n)
		SiNo
			Mientras (n>=10000)
				cicl = cicl + 1
				n = n - 10000
			FinMientras
		FinSi
		Si No (1000<n)
		SiNo
			Mientras (n>=1000)
				cic = cic + 1
				n = n - 1000
			FinMientras
		FinSi
		Si No (100<n)
		SiNo
			Mientras (n>=100)
				ci = ci + 1
				n = n - 100
			FinMientras
		FinSi
		Si No (10<n)
		SiNo
			Mientras (n>=10)
				c = c + 1
				n = n - 10
			FinMientras
		FinSi
		Si No (1<n)
		FinSi
		u = n
		
		resto = (ciclomm*3+ciclom*2+ciclo*7+cicl*6+cic*5+ci*4+c*3+u*2) mod 11
	SiNo
	FinSi
	Si resto = 0
		Escribir "el numero verificador es 0"
	SiNo
		test = 11-resto
		Si (test = 10)
			Escribir "el numero verificador es k"
		SiNo
			Escribir "el numero verificador es ',test,'"
		FinSi
	FinSi
FinAlgoritmo
