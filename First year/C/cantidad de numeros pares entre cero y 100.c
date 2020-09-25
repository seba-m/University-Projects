#include <stdio.h>

int main() {
	int aux = 0, contador = 0;
	while (aux < 99) {
		aux++;
		if (aux % 2 == 0) {
			contador++;
		}
	}
	printf("la cantidad de numeros pares que hay entre cero y 100 es: %i\n",contador);
	return 0;
}
