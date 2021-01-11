#ifndef stdio_h
#include <stdio.h>
#define stdio_h
#endif

#ifndef stdlib_h
#include <stdlib.h>
#define stdlib_h
#endif

#ifndef unistd_h
#include <unistd.h>
#define unistd_h
#endif

#ifndef nodo_h
#define nodo_h
#include "nodo.h"
#endif /* nodo_h */

#ifndef tabla_h
#define tabla_h
#include "tabla.h"
#endif /* tabla_h */

#ifndef bosque_h
#define bosque_h
#include "bosque.h"
#endif /* bosque_h */

#ifndef archivos_descomprimir_h
#define archivos_descomprimir_h
#include "archivo descomprimir.h"
#endif /* archivos_comprimir_h */

#ifndef archivos_arbol_h
#define archivos_arbol_h
#include "archivos arbol.h"
#endif /* archivos arbol_h */

void descomprimir_huffman(char *entrada, char *salida){
        Tabla* caracter_frecuencia = interpretar_tabla(entrada);
        huffman(caracter_frecuencia);
        //printf("Tabla de frecuencias:\ncaracter|   frecuencia  | codigo");
        //muestraTabla(caracter_frecuencia);
        printf("\ndescomprimiendo archivo, por favor espere...\n");
        lista * lista_frecuencias = convertir_tabla_a_lista(caracter_frecuencia);
        descomprimir_archivo(lista_frecuencias,entrada,salida,0);
        printf("\n\narchivo descomprimido, el programa ha finalizado.");
}
