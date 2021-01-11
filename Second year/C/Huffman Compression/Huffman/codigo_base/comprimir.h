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

#ifndef archivos_comprimir_h
#define archivos_comprimir_h
#include "archivos comprimir.h"
#endif /* archivos_comprimir_h */

#ifndef archivos_arbol_h
#define archivos_arbol_h
#include "archivos arbol.h"
#endif /* archivos arbol_h */

void comprimir_huffman(char *entrada, char *salida){
        Tabla* caracter_frecuencia = obtener_tabla_archivo(entrada);
        huffman(caracter_frecuencia);
        //muestraTabla(caracter_frecuencia);
        printf("\ncomprimiendo archivo, por favor espere...\n");
        comprimir_archivo(caracter_frecuencia,entrada,salida,0);
        printf("\narchivo comprimido\n\nel programa ha finalizado.\n");
}
