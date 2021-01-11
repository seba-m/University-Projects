#ifndef stdio_h
#include <stdio.h>
#define stdio_h
#endif

#ifndef stdlib_h
#include <stdlib.h>
#define stdlib_h
#endif

#ifndef tabla_h
#define tabla_h
#include "tabla.h"
#endif /* tabla_h */


typedef struct nodo_huffman{
    unsigned char simbolo;
    int peso;
    struct nodo_huffman *hizq, *hder;
}NodoHuffman;

NodoHuffman *creaNodoHuffman(unsigned char unSimbolo, int unPeso){
    NodoHuffman *unNodo = (NodoHuffman*)malloc(sizeof(NodoHuffman));
    unNodo->simbolo = unSimbolo;
    unNodo->peso = unPeso;
    unNodo->hizq = NULL;
    unNodo->hder = NULL;
    return(unNodo);
}

int setNodoHuffmanHI(NodoHuffman *unNodo, NodoHuffman *hijoIzq){
    if(unNodo == NULL) return(0);
    unNodo->hizq = hijoIzq;
    return(1);
}

int setNodoHuffmanHD(NodoHuffman *unNodo, NodoHuffman *hijoDer){
    if(unNodo == NULL) return(0);
    unNodo->hder = hijoDer;
    return(1);
}

void imprimir_huffman(NodoHuffman *raiz){

        if (raiz->hizq) {
                imprimir_huffman(raiz->hizq);
        }
        if (raiz->hder) {
                imprimir_huffman(raiz->hder);
        }
        if (!(raiz->hizq) && !(raiz->hder)) {
                printf(" %c=(%i)  |%i|\n", raiz->simbolo,raiz->simbolo,raiz->peso);
        }
}


void imprimir(NodoHuffman *raiz) {
        imprimir_huffman(raiz);
}