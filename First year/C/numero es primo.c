#include <stdio.h>

int main() {
        int a;
        int cont;
        printf("ingrese un numero por favor\n");
        scanf("%i", &a);
        cont = 0;
        for (int i = 1; i <= a; i += 1) {
                if (a % i == 0) {
                        cont++;
                }
        }
        if (cont == 2) {
                printf("%i es un numero primo\n", a);
        } else {
                printf("%i no es un numero primo\n", a);
        }
        return 0;
}
