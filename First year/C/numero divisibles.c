#include <stdio.h>

int main() {

	int numero1, numero2;
	printf("ingrese el primer numero: ");
	scanf("%i",&numero1);
	printf("\ningrese el segundo numero: ");
	scanf("%i",&numero2);
	printf("\n");

	if (numero1 % numero2 == 0) {
		printf("el numero %i SI es divisible por %i\n",numero1,numero2);
	}else{
		printf("el numero %i NO es divisible por %i\n",numero1,numero2);
	}
	return 0;
}
