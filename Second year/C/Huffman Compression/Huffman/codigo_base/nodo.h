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

typedef struct nodo {
        unsigned char simbolo;
        int frecuencia;
        int longitud;
        char *codigo;
        unsigned long int binario;
        struct nodo *sgte;
}Nodo;

Nodo *creaNodo(char unSimbolo){
        Nodo *unNodo = (Nodo*)malloc(sizeof(Nodo));
        unNodo->simbolo = unSimbolo;
        unNodo->frecuencia = 1;
        unNodo->longitud = 0;
        unNodo->codigo = NULL;
        unNodo->sgte = NULL;
        unNodo->binario = 0;
        return(unNodo);
}

int incrementaFrecuenciaNodo(Nodo *unNodo){
        if(unNodo == NULL) return(0);
        unNodo->frecuencia++;
        return(1);
}

int setSgte(Nodo *unNodo, Nodo *sgteNodo){
        if(unNodo == NULL) return(0);
        unNodo->sgte = sgteNodo;
        return(1);
}

int comparaSimboloNodo(Nodo *unNodo, char unSimbolo){
        if(unNodo == NULL) {
                return(0);
        }
        if (unSimbolo < 0) {
                int nodo = unNodo->simbolo;
                int simbolo = 256 + unSimbolo;
                if(nodo == simbolo) {
                        return(1);
                }
        } else {
                if(unNodo->simbolo == unSimbolo) {
                        return(1);
                }
        }

        return(0);
}

int muestraNodo(Nodo *unNodo){
        if(unNodo == NULL) return(0);
        printf("  %c (%d)\t|\t%d\t| %s\t    | %lu", unNodo->simbolo, unNodo->simbolo, unNodo->frecuencia,unNodo->codigo,unNodo->binario);
        return(1);
}
