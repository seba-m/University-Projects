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

#ifndef bosque_h
#define bosque_h
#include "bosque.h"
#endif /* bosque_h */

#ifndef archivos_comprimir_h
#define archivos_comprimir_h
#include "archivos comprimir.h"
#endif /* archivos_comprimir_h */

int ordena_bosque(Bosque *aquelBosque){
        ordena_bosque_arreglo(aquelBosque->arboles,0,aquelBosque->cantidadArboles-1); // ya que la estructura bosque utiliza un
        return 1;                                                                     // arreglo de nodos huffman, yo ordeno los arboles
}                                                                                     // unitarios con esta funcion.

NodoHuffman *sacaNodoMenor(Bosque *miBosque){
        NodoHuffman *menor = miBosque->arboles[miBosque->cantidadArboles-1]; // como ordeno el arreglo de nodos huffman de menor a mayor
        miBosque->arboles[miBosque->cantidadArboles-1] = NULL;               // saco siempre el ultimo,hago NULL la posicion y le resto uno
        miBosque->cantidadArboles--;                                         // a la cantidadArboles
        return menor;
}

int crear_ArbolHuffman(Bosque *tuBosque){                                      // voy creando el arbol de hufman dentro de un arreglo
        NodoHuffman *hijoIzq,*hijoDer,*cabeza;                                 // siempre y cuando la cantidad de arboles sea distinta de 1
        while(tuBosque->cantidadArboles != 1) {                                // caso contrario solo me quedaria un arbol que seria la raiz
                ordena_bosque(tuBosque);
                hijoIzq = sacaNodoMenor(tuBosque);                             // aca saco los dos menores y los asigno a un hijo izquierdo o derecho
                hijoDer = sacaNodoMenor(tuBosque);
                cabeza = creaNodoHuffman('$',hijoDer->peso+hijoIzq->peso);     //como cabeza de los hijos creo una raiz que tiene la suma de los hijos
                setNodoHuffmanHI(cabeza,hijoIzq);
                setNodoHuffmanHD(cabeza,hijoDer);
                agregaArbolBosque(tuBosque,cabeza);                            // y de nuevo lo meto al arreglo
        }
        return 1;
}

void crea_CodigoHuffman(NodoHuffman *arbol, char codigo[], int pos,Tabla *unaTabla, int binario) {
        if (!(arbol->hder) && !(arbol->hizq)) {                                             // si el nodo no tiene hijo dercho o hijo izquierdo
                Nodo *aux = (unaTabla)->primero;                                            // significa que llego a una hoja, lo que significa que
                while (aux != NULL) {                                                       // es momento de anotar el codigo dnetro de la tabla
                        if(comparaSimboloNodo(aux, arbol->simbolo) == 1) {
                                aux->codigo = (char*)malloc(sizeof(char)*pos);
                                memset(aux->codigo, '\0', sizeof(char)*pos+1);
                                for(int i = 0; i< pos; i++) {
                                        aux->codigo[i] = codigo[i];
                                }
                                aux->longitud = pos;
                                aux->binario = binario;
                                return;
                        }else{
                                aux = aux->sgte;
                        }
                }
        }else{
                binario = binario << 1;
                codigo[pos] = '0';                                                      // recursividad para recorrer el arbol
                crea_CodigoHuffman(arbol->hizq, codigo, pos + 1,unaTabla,binario);      // si se va a la hizquierda se anota un 0 en el arreglo "codigo"
                codigo[pos] = '1';                                                      // si se va a la derecha se anota un 1
                crea_CodigoHuffman(arbol->hder, codigo, pos + 1,unaTabla,binario+1);
        }
}

void LlenaTablaConCodigo(Nodo *unNodo,Tabla *unaTabla){
        char numeros[30]; //variable para el codigo de huffman
        memset(numeros, '\0', sizeof(numeros));
        NodoHuffman *unNodoDeHuffman;
        Bosque *eseBosque = creaBosque();
        while(unNodo != NULL) {                                                         // aca simplemente voy sacando un elemento de la tabla y creo
                unNodoDeHuffman = creaNodoHuffman(unNodo->simbolo,unNodo->frecuencia);  // un nodo de huffman, de ahí lo agrego al bosque, asi para
                agregaArbolBosque(eseBosque,unNodoDeHuffman);                           // todos los datos de la tabla
                unNodo = unNodo->sgte;
        }
        crear_ArbolHuffman(eseBosque);
        ordenar_tabla(unaTabla);
        crea_CodigoHuffman(eseBosque->arboles[0],numeros,0,unaTabla,0);
}

int huffman(Tabla *unaTabla){
        if(unaTabla == NULL) return 0;
        LlenaTablaConCodigo(unaTabla->primero,unaTabla);
        return 1;
}
