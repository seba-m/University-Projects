#ifndef stdio_h
#include <stdio.h>
#define stdio_h
#endif

#ifndef stdlib_h
#include <stdlib.h>
#define stdlib_h
#endif

#ifndef nodo_h
#define nodo_h
#include "nodo.h"
#endif /* nodo_h */

typedef struct tabla {
        Nodo *primero, *ultimo;
        int cantidad_caracteres_usado;
        unsigned long long int tamano_archivo;
}Tabla;


Tabla *iniciaTabla(){
        Tabla *unaTabla = (Tabla*)malloc(sizeof(Tabla));
        unaTabla->primero = NULL;
        unaTabla->ultimo = NULL;
        unaTabla->cantidad_caracteres_usado = 0;
        unaTabla->tamano_archivo = 0;
        return(unaTabla);
}

int agregaPrimero(Tabla *unaTabla, char unSimbolo){
        if(unaTabla == NULL) return (0);
        if((unaTabla->primero!=NULL)||(unaTabla->ultimo!=NULL)) return(0);
        Nodo *nuevoNodo=creaNodo(unSimbolo);
        unaTabla->primero = nuevoNodo;
        unaTabla->ultimo = nuevoNodo;
        return(1);
}

int agregaAlFinal(Tabla *unaTabla, char unSimbolo){
        if(unaTabla == NULL) return (0);
        if((unaTabla->primero==NULL)&&(unaTabla->ultimo==NULL)) {
                agregaPrimero(unaTabla, unSimbolo);
                return(1);
        }
        Nodo *nuevoNodo=creaNodo(unSimbolo);
        unaTabla->ultimo->sgte = nuevoNodo;
        unaTabla->ultimo = nuevoNodo;
        return(1);
}

int insertaSimbolo(Tabla *unaTabla, char unSimbolo){
        if(unaTabla == NULL) return (0);
        if((unaTabla->primero==NULL)&&(unaTabla->ultimo==NULL)) {
                agregaPrimero(unaTabla, unSimbolo);
                unaTabla->cantidad_caracteres_usado++;
                return(1);
        }
        Nodo *aux = unaTabla->primero;
        while(aux != NULL) {
                if(comparaSimboloNodo(aux, unSimbolo)==1) {
                        incrementaFrecuenciaNodo(aux);
                        return(1);
                }else{
                        aux = aux->sgte;
                }
        }
        unaTabla->cantidad_caracteres_usado++;
        return(agregaAlFinal(unaTabla, unSimbolo));
}

int agregaEnTabla(Tabla *unaTabla,char unSimbolo,int frecuencia){
        if(unaTabla == NULL) return (0);
        if((unaTabla->primero==NULL)&&(unaTabla->ultimo==NULL)) {
                agregaPrimero(unaTabla, unSimbolo);
                unaTabla->primero->frecuencia = frecuencia;
                return(1);
        }
        agregaAlFinal(unaTabla, unSimbolo);
        unaTabla->ultimo->frecuencia = frecuencia;
        return 1;
}

Nodo *dato_pos_x_tabla(Tabla *puntero, int pos) {
        int contador = 0;
        Nodo *aux = puntero->primero;
        while (aux != NULL) {
                if (contador == pos) {
                        return aux;
                }
                aux = aux->sgte;
                contador = contador + 1;
        }
        return NULL;
}

int muestraTabla(Tabla *unaTabla){
        if(unaTabla == NULL) return (0);
        Nodo *aux = unaTabla->primero;
        printf("Tabla de frecuencias:\ncaracter\t|   frecuencia  | codigo    |  binario(10) ");
        printf("\n----------------+---------------+----------------------\n");
        while(aux != NULL) {
                muestraNodo(aux);
                printf("\n");
                aux = aux->sgte;
        }
        printf("\nletras totales usadas %i\n",unaTabla->cantidad_caracteres_usado);
        printf("----------------------------------------------------------\n");
        return(1);
}
