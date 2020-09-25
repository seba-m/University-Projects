#include<stdio.h>

int main() {
	int num;
	printf("Ingrese un numero\n");
	scanf("%i",&num);
	if (num%2==0) {
		printf("El numero es par\n");
	} else {
		printf("El numero es impar\n");
	}
	return 0;
}
