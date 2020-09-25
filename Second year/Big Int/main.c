//
//  main.c
//  Taller 1
//
//  Creado por Sebastian Morgado - Antony Rodriguez - Roberto Contreras el 18/5/20.
//

#ifndef stdlib_h
#define stdlib_h
#include <stdlib.h>
#endif /* stdlid_h */

#ifndef stdio_h
#define stdio_h
#include <stdio.h>
#endif /* stdio_h */

#ifndef string_h
#define string_h
#include <string.h>
#endif /* string_h */

#ifndef locale_h
#define locale_h
#include <locale.h>
#endif /* locale_h */

#ifndef ctype_h
#define ctype_h
#include <ctype.h>
#endif /* ctype_h */

#ifndef entero_largo_arreglo_h
#define entero_largo_arreglo_h
#include "entero_largo_arreglo.h"
#endif

/*#ifndef entero_largo_lista_h
#define entero_largo_lista_h
#include "entero_largo_lista.h"
#endif*/

int main(void){
        EnteroLargo *N1, *N2 = creaEnteroLargo();
        int opcion = 0, numero = 0, lista_generada = 0;
        char numero_string[MAX_DIGITOS], numero_string2[MAX_DIGITOS];
        char name[MAX_DIGITOS];
        do {
                system("CLS");
                printf("Menu Taller 1 - grupo 2\n\n");
                printf("Elija una opcion: \n");
                printf("        Version %s\n",version());
                printf("1.  Generar entero largo \n");
                printf("2.  Agregar datos tipo int en entero largo\n");
                printf("3.  Agregar datos tipo string en entero largo\n");
                printf("4.  Eliminar entero largo \n");
                printf("5.  Ver entero largo \n");
                printf("6.  Ver entero largo desde un archivo\n");
                printf("7.  Escribir entero largo en un archivo \n");
                printf("8.  Copiar entero largo a otra variable\n");
                printf("9.  Sumar enteros largos\n");
                printf("10. Restar enteros largo\n");
                printf("11. Multiplicar enteros largos\n");
                printf("12. Dividir enteros largos\n");
                printf("13. SALIR\n");
                printf("\n(consejo: para poder usar la version arreglo, debe comentar las lineas 28-31, y descomentar las 23-26)\n");
                printf("\n\nopcion: ");
                scanf("%i", &opcion);

                switch (opcion) {
                case 1: // generar lista
                        system("CLS");
                        N1 = creaEnteroLargo();
                        printf("=== lista generada correctamente ===  \n\n");
                        lista_generada = 1;
                        system("PAUSE");
                        break;
                case 2: // agregar datos tipo int a lista
                        system("CLS");
                        if(lista_generada == 1) {
                                printf("ingrese un numero: ");
                                scanf("%i", &numero);
                                N1 = creaEnteroLargoInt(numero);
                                printf("=== numero ingresado correctamente ===  \n\n");
                        }else{
                                printf("\nPrimero tiene que generar un entero largo (opcion 1).\n\n=== EnteroLargo ingresado incorrectamente ===  \n\n");
                        }
                        system("PAUSE");
                        break;
                case 3: // agregar datos tipo string a lista
                        system("CLS");
                        if(lista_generada == 1) {
                                printf("ingrese un numero: ");
                                scanf("%s", &*numero_string);
                                N1 = creaEnteroLargoStr(numero_string);
                                lista_generada = 1;
                                printf("=== numero ingresado correctamente ===  \n\n");
                        }else{
                                printf("\nPrimero tiene que generar un entero largo (opcion 1).\n\n=== EnteroLargo ingresado incorrectamente ===  \n\n");
                        }
                        system("PAUSE");
                        break;
                case 4: // eliminar entero largo
                        system("CLS");
                        if(lista_generada == 1) {
                                EliminaEnteroLargo(N1);
                                lista_generada = 0;
                                printf("=== EnteroLargo eliminado correctamente ===  \n\n");
                        }else{
                                printf("\nPrimero tiene que generar un entero largo (opcion 1).\n\n=== EnteroLargo borrado incorrectamente ===  \n\n");
                        }
                        system("PAUSE");
                        break;
                case 5: //ver entero largo
                        system("CLS");
                        if(lista_generada == 0) {
                                MuestraEnteroLargo(N1);
                                printf("\n\n=== EnteroLargo mostrado correctamente ===  \n\n");
                        }else{
                                printf("\nPrimero tiene que generar un numero.\n\n=== EnteroLargo mostrado incorrectamente ===  \n\n");
                        }
                        system("PAUSE");
                        break;
                case 6: //ver entero largo desde archivo
                        system("CLS");
                        if(lista_generada == 1) {
                                printf("\npor favor ingrese el nombre del archivo (por ejemplo: archivo.bin):\n");
                                scanf("%s", name);
                                N1 = leeEnteroLargo(name);
                                lista_generada = 1;
                                MuestraEnteroLargo(N1);
                                printf("\n\n=== EnteroLargo mostrado correctamente ===  \n\n");
                        }else{
                                printf("\nPrimero tiene que generar un numero.\n\n=== EnteroLargo mostrado incorrectamente ===  \n\n");
                        }
                        system("PAUSE");
                        break;
                case 7: //escribir EnteroLargo a un archivo
                        system("CLS");
                        if(lista_generada == 1) {
                                printf("\npor favor ingrese el nombre del archivo (por ejemplo: archivo.bin):\n");
                                scanf("%s", name);
                                EscribeEnteroLargo(name,N1);
                                printf("\n\n=== EnteroLargo copiado correctamente ===  \n\n");
                        }else{
                                printf("\nPrimero tiene que generar un numero.\n\n=== EnteroLargo escrito incorrectamente ===  \n\n");
                        }
                        system("PAUSE");
                        break;
                case 8: //copiar entero largo
                        system("CLS");
                        if(lista_generada == 1) {
                                CopiaEnteroLargo(N2,N1);
                                printf("valor origen = ");
                                MuestraEnteroLargo(N1);
                                printf("\nvalor destino = ");
                                MuestraEnteroLargo(N2);
                                printf("\n\n=== EnteroLargo copiado correctamente ===  \n\n");
                        }else{
                                printf("\nPrimero tiene que generar un numero.\n\n=== EnteroLargo copiado incorrectamente ===  \n\n");
                        }
                        system("PAUSE");
                        break;
                case 9: //sumar
                        system("CLS");
                        if(lista_generada == 1) {
                                EliminaEnteroLargo(N1); //elimino el entero largo, para vaciar los contenidos de este, y evitar que se sobre escriban.
                                N1 = creaEnteroLargo(); //vuelve a asignarle memoria, para que se pueda almacenar contenido dentro de este.
                                printf("\npor favor ingrese dos numeros para ser sumados:\n");
                                scanf("%s %s", numero_string,numero_string2);
                                N1 = sumaEnteroLargo(creaEnteroLargoStr(numero_string),creaEnteroLargoStr(numero_string2));
                                MuestraEnteroLargo(N1);
                                printf("\n\n=== EnteroLargo sumado correctamente ===  \n\n");
                        }else{
                                printf("\nPrimero tiene que generar un entero largo (opcion 1).\n\n=== EnteroLargo sumado incorrectamente ===  \n\n");
                        }
                        system("PAUSE");
                        break;
                case 10: //restar
                        system("CLS"); lista_generada = 1;
                        if(lista_generada == 1) {
                                //EliminaEnteroLargo(N1); //elimino el entero largo, para vaciar los contenidos de este, y evitar que se sobre escriban.
                                N1 = creaEnteroLargo(); //vuelve a asignarle memoria, para que se pueda almacenar contenido dentro de este.
                                printf("\npor favor ingrese dos numeros para ser restados:\n");
                                scanf("%s %s", numero_string,numero_string2);
                                N1 = restaEnteroLargo(creaEnteroLargoStr(numero_string),creaEnteroLargoStr(numero_string2));
                                MuestraEnteroLargo(N1);
                                printf("\n\n=== EnteroLargo restado correctamente ===  \n\n");
                        }else{
                                printf("\nPrimero tiene que generar un entero largo (opcion 1).\n\n=== EnteroLargo restado incorrectamente ===  \n\n");
                        }
                        system("PAUSE");
                        break;
                case 11: //multiplicar
                        system("CLS");
                        if(lista_generada == 1) {
                                EliminaEnteroLargo(N1); //elimino el entero largo, para vaciar los contenidos de este, y evitar que se sobre escriban.
                                N1 = creaEnteroLargo(); //vuelve a asignarle memoria, para que se pueda almacenar contenido dentro de este.
                                printf("\npor favor ingrese dos numeros para ser multiplicado:\n");
                                scanf("%s %s", numero_string,numero_string2);
                                N1 = multiplicaEnteroLargo(creaEnteroLargoStr(numero_string),creaEnteroLargoStr(numero_string2));
                                if (N1 != NULL) {
                                        MuestraEnteroLargo(N1);
                                        printf("\n\n=== EnteroLargo multiplicado correctamente ===  \n\n");
                                } else {
                                        printf("\n\n=== EnteroLargo multiplicado incorrectamente ===  \n\n");
                                }
                        }else{
                                printf("\nPrimero tiene que generar un entero largo (opcion 1).\n\n=== EnteroLargo multiplicado incorrectamente ===  \n\n");
                        }

                        system("PAUSE");
                        break;
                case 12: //dividir
                        system("CLS");
                        if(lista_generada == 1) {
                                EliminaEnteroLargo(N1); //elimino el entero largo, para vaciar los contenidos de este, y evitar que se sobre escriban.
                                N1 = creaEnteroLargo(); //vuelve a asignarle memoria, para que se pueda almacenar contenido dentro de este.
                                printf("\npor favor ingrese dos numeros para ser divididos:\n(si el numero 1 es muy grande, y el numero 2 es muy pequeno, puede que el programa se cierre, o que se demore mucho.)\n\n");
                                scanf("%s", numero_string);
                                for (int i = 0; i < strlen(numero_string); i++) {
                                        printf("%c",196);
                                }
                                printf("\n");
                                scanf("%s",numero_string2);
                                N1 = divideEnteroLargo(creaEnteroLargoStr(numero_string),creaEnteroLargoStr(numero_string2));
                                if (N1 != NULL) {
                                        printf("\n\nel resultado es: ");
                                        MuestraEnteroLargo(N1);
                                        printf("\n\n=== EnteroLargo dividido correctamente ===  \n\n");
                                } else {
                                        printf("\n\n=== EnteroLargo dividido incorrectamente ===  \n\n");
                                }
                        }else{
                                printf("\nPrimero tiene que generar un entero largo (opcion 1).\n\n=== EnteroLargo dividido incorrectamente ===  \n\n");
                        }
                        system("PAUSE");
                        break;
                case 13:
                        break;
                }
                system("CLS");
        } while (opcion != 13);
}
