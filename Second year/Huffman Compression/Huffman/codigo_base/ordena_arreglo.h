#ifndef stdio_h
#include <stdio.h>
#define stdio_h
#endif

#ifndef stdlib_h
#include <stdlib.h>
#define stdlib_h
#endif

#ifndef nodo_huffman_h
#define nodo_huffman_h
#include "nodo_huffman.h"
#endif /* nodo_huffman_h */


void swap(NodoHuffman **a,NodoHuffman **b){
		NodoHuffman * aux = *a;
		*a = *b;
		*b = aux;
}

int partition (NodoHuffman *arreglo[], int inicio, int final){
								int pivote = arreglo[final]->peso;
								int i = (inicio - 1);
								for (int j = inicio; j <= final- 1; j++){
																if (arreglo[j]->peso > pivote){
																								i++;
																								swap(&arreglo[i],&arreglo[j]);
																}
								}
								swap(&arreglo[i+1],&arreglo[final]);
								return (i + 1);
}

int ordena_bosque_arreglo(NodoHuffman *arreglo[],int inicio,int final){
        if(inicio < final) {																					//simplemente utizizo quicksort para
                int mitad = partition(arreglo, inicio, final);				// ordenar el areglo
                ordena_bosque_arreglo(arreglo, inicio, mitad - 1);
                ordena_bosque_arreglo(arreglo, mitad + 1, final);
        }
        return 1;
}
