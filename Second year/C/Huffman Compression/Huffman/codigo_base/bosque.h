#ifndef stdio_h
#include <stdio.h>
#define stdio_h
#endif

#ifndef stdlib_h
#include <stdlib.h>
#define stdlib_h
#endif

#ifndef string_h
#include <string.h>
#define string_h
#endif

#ifndef nodo_huffman_h
#define nodo_huffman_h
#include "nodo_huffman.h"
#endif /* nodo_huffman_h */

#ifndef tabla_h
#define tabla_h
#include "tabla.h"
#endif /* tabla_h */

#ifndef ordena_arreglo_h
#define ordena_arreglo_h
#include "ordena_arreglo.h"
#endif /* ordena_arreglo_h */

#define MAX_BOSQUE 256

typedef struct bosque {
        int cantidadArboles;
        NodoHuffman *arboles[MAX_BOSQUE];
}Bosque;

Bosque *creaBosque(){
        Bosque *unBosque = (Bosque*)malloc(sizeof(Bosque));
        unBosque->cantidadArboles = 0;
        for(int i = 0; i < MAX_BOSQUE; i++) {
                unBosque->arboles[i] = NULL;
        }
        return(unBosque);
}

int agregaArbolBosque(Bosque *unBosque, NodoHuffman *unArbol){
        if(unBosque == NULL) return(0);
        unBosque->arboles[unBosque->cantidadArboles] = unArbol;
        unBosque->cantidadArboles++;
        return(1);
}