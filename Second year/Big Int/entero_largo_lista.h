//
//  entero_largo_lista.h
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

/*todos estos define, fueron hechos para mejorar la legibilidad del codigo, no afecta en nada a este.*/
#define COMPARAR_LONGITUD 1
#define COMPARAR_DIGITO 2
#define COMPARAR_TODO 3
#define VERIFICAR_NUMERO_IGUAL_A_0 4
#define VERIFICAR_SI_NUMEROS_SON_IGUALES 5

#define MAX_DIGITOS 1000

typedef struct Lista {
        int digitos;
        struct Lista *siguiente;
} lista;

typedef struct nodo {
        int CantidadDigitos;
        char signo;
        lista *datos;
} EnteroLargo;

/*----------------------------DECLARACIONES---------------------------------*/

EnteroLargo *sumaEnteroLargo(EnteroLargo *numero1, EnteroLargo *numero2);
EnteroLargo *restaEnteroLargo(EnteroLargo *numero1, EnteroLargo *numero2);
void llenar(lista **destino,int algo);
void invertir_lista(lista **nodo);
int cantidad_datos(lista *puntero);
int dato_pos_x(EnteroLargo *puntero, int pos);
int utilidades(EnteroLargo numero1, EnteroLargo numero2, int opcion);
int verifica_numero(EnteroLargo *numero, EnteroLargo *verificador);
EnteroLargo *ceros(EnteroLargo *resultado);
void MuestraEnteroLargo(EnteroLargo *arreglo);
/*-----------------------------------------------------------------------------*/

/*--------------------------------FUNCIONES----------------------------------------*/
EnteroLargo *creaEnteroLargo(){
        EnteroLargo *numero_vacio;
        numero_vacio = (EnteroLargo*)malloc(sizeof(EnteroLargo));
        numero_vacio->CantidadDigitos = 1;
        numero_vacio->datos = 0;
        numero_vacio->signo = '+';
        return numero_vacio;
}
EnteroLargo *creaEnteroLargoInt(int valor_ingresado) {

        // ----------- declaraciones ----------- //

        EnteroLargo *numero = creaEnteroLargo();

        // ----------- verificar signos ----------- //

        if (valor_ingresado < 0) {
                valor_ingresado *= -1; // multiplico por -1, para hacer el valor positivo
                numero->signo = '-';
        } else if (valor_ingresado > 0) {
                numero->signo = '+';
        } else if (valor_ingresado == 0) {
                llenar(&numero->datos,0);
                return numero;
        }

        // ----------- separar digitos ----------- //

        while(valor_ingresado > 0) {
                llenar(&numero->datos,valor_ingresado % 10); // extraemos los digitos positivos de la funcion
                valor_ingresado /= 10;
        }
        numero->CantidadDigitos = cantidad_datos(numero->datos);
        return numero;
}
EnteroLargo *creaEnteroLargoStr(char numero[MAX_DIGITOS]) {
        EnteroLargo *entero = creaEnteroLargo();
        char num_aux; int posicion;
        if (numero[0] == '-') {
                entero->CantidadDigitos = strlen(numero) - 1;
                entero->signo = '-';
                posicion = entero->CantidadDigitos;
        } else if(numero[0] == '+') {
                entero->CantidadDigitos = strlen(numero) - 1;
                entero->signo = '+';
                posicion = entero->CantidadDigitos;
        }else{
                entero->CantidadDigitos = strlen(numero);
                entero->signo = '+';
                posicion = entero->CantidadDigitos - 1;
        }
        for(int i = 0; i < entero->CantidadDigitos; i++) {
                num_aux = numero[posicion];
                llenar(&entero->datos,atoi(&num_aux));
                posicion--;
        }
        return entero;
}
void CopiaEnteroLargo(EnteroLargo *destino, EnteroLargo *fuente) {
        destino->signo = fuente->signo;
        destino->CantidadDigitos = fuente->CantidadDigitos;
        lista *receptor = NULL;
        lista *receptor_aux = fuente->datos;
        int i = 0,copia;
        while (i < fuente->CantidadDigitos) {
                copia = receptor_aux->digitos;
                llenar(&receptor,copia);
                receptor_aux = receptor_aux->siguiente;
                i++;
        }
        destino->datos = receptor;
}
void EscribeEnteroLargo(char archivo[], EnteroLargo *numero) {
        FILE *fp = fopen(archivo, "wb");
        if (fp == NULL) {
                fprintf(stderr,"Error al abrir el archivo\n");
                exit(1);
        }
        fwrite(numero, sizeof(EnteroLargo), 1, fp);
        fclose(fp);
}
EnteroLargo *leeEnteroLargo(char archivo[]){
        EnteroLargo *buffer = (EnteroLargo*)malloc(sizeof(EnteroLargo));
        FILE *fp = fopen(archivo, "rb");
        if(fp == NULL)
        {
                fprintf(stderr,"fopen fallo al abrir el archivo %s en la linea # %i\n", __FILE__, __LINE__);
                exit(EXIT_FAILURE);
        }
        fread(buffer, sizeof(EnteroLargo), 1, fp);
        fclose(fp);
        return buffer;
}
void MuestraEnteroLargo(EnteroLargo *arreglo) {
        if (arreglo != NULL) {
                EnteroLargo *auxiliar = (EnteroLargo*)malloc(sizeof(EnteroLargo));
                CopiaEnteroLargo(auxiliar,arreglo);
                lista *nuevo = auxiliar->datos;
                invertir_lista(&nuevo);
                fprintf(stdout,"%c", arreglo->signo);
                for (int i = cantidad_datos(arreglo->datos) -1; i >= 0; i--) {
                        fprintf(stdout,"%i",dato_pos_x(arreglo,i));
                }
        }else{
                fprintf(stderr,"no se puede mostrar algo vacio/nulo\n");
        }
}
void EliminaEnteroLargo(EnteroLargo *numero) {
        if (numero != NULL) {
                lista *actual = numero->datos;
                lista *auxiliar;
                while (actual != NULL) {
                        auxiliar = actual;
                        actual = actual->siguiente;
                        free(auxiliar);
                }
                numero->datos = NULL;
                free(numero);
        }else{
                fprintf(stderr,"no hay necesidad de eliminar el entero largo, este ya esta vacio.\n");
        }
}

EnteroLargo *restaEnteroLargo(EnteroLargo *numero1, EnteroLargo *numero2) {
        EnteroLargo *respuesta = creaEnteroLargo();
        EnteroLargo *auxiliar1 = creaEnteroLargo();
        EnteroLargo *auxiliar2 = creaEnteroLargo();
        int i = 0, resta = 0, sobra = 0, aux = 0;
        if (numero1->signo != numero2->signo) {
                if (numero2->signo == '+') {
                        numero2->signo = '-';
                } else {
                        numero2->signo = '+';
                }
                respuesta = sumaEnteroLargo(numero1,numero2);
                if (utilidades(*numero1, *numero2, COMPARAR_TODO) == 1) {
                        respuesta->signo = numero1->signo;
                } else if (utilidades(*numero1, *numero2, COMPARAR_TODO) == -1) {
                        respuesta->signo = numero2->signo;
                }
                EliminaEnteroLargo(auxiliar1);
                EliminaEnteroLargo(auxiliar2);
                return respuesta;
        } else {
                if (utilidades(*numero1, *numero2, COMPARAR_TODO) == 1) {
                        CopiaEnteroLargo(auxiliar1, numero1);
                        CopiaEnteroLargo(auxiliar2, numero2);
                } else if (utilidades(*numero1, *numero2, COMPARAR_TODO) == -1) {
                        CopiaEnteroLargo(auxiliar1, numero2);
                        CopiaEnteroLargo(auxiliar2, numero1);
                        if (auxiliar1->signo == '+') {
                                auxiliar1->signo = '-';
                                respuesta->datos = 0;
                        } else {
                                auxiliar1->signo = '+';
                        }
                } else if (utilidades(*numero1, *numero2, COMPARAR_TODO) == 0) {
                        respuesta = creaEnteroLargoStr("0");
                        return respuesta;
                }
                respuesta->signo = auxiliar1->signo;
        }
        respuesta->CantidadDigitos = auxiliar1->CantidadDigitos;
        for (i = 0; i < auxiliar2->CantidadDigitos; i++) {
                resta = dato_pos_x(auxiliar1,i) - dato_pos_x(auxiliar2,i) - sobra;
                if (resta < 0) {
                        resta += 10;
                        sobra = 1;
                } else {
                        sobra = 0;
                }
                llenar(&respuesta->datos,resta);
        }
        while (sobra > 0) {
                aux = dato_pos_x(auxiliar1,i) - sobra;
                if (aux < 0) {
                        aux += 10;
                        sobra = 1;
                } else {
                        sobra = 0;
                }
                llenar(&respuesta->datos,aux);
                i++;
        }
        while (i < auxiliar1->CantidadDigitos) {
                llenar(&respuesta->datos,dato_pos_x(auxiliar1,i));
                i++;
        }
        ceros(respuesta);
        return respuesta;
}

EnteroLargo *sumaEnteroLargo(EnteroLargo *numero1, EnteroLargo *numero2){
        EnteroLargo *resultado = NULL;
        if (numero1->signo == numero2->signo) {
                resultado = creaEnteroLargo();
                resultado->signo = numero1->signo;
                int acarreo = 0, i = 0, aux = 0, maxDigitos;
                if (numero1->CantidadDigitos >= numero2->CantidadDigitos) {
                        maxDigitos = numero1->CantidadDigitos;
                }else{
                        maxDigitos = numero2->CantidadDigitos;
                }
                for (i = 0; i < maxDigitos; i++) {
                        int digito1 = 0;
                        int digito2 = 0;
                        if (i < numero1->CantidadDigitos) {
                                digito1 = dato_pos_x(numero1,i);
                        }
                        if (i < numero2->CantidadDigitos) {
                                digito2 = dato_pos_x(numero2,i);
                        }
                        aux = digito1 + digito2 + acarreo;
                        llenar(&resultado->datos,aux % 10);
                        acarreo = aux / 10;
                }
                while (acarreo > 0) {
                        llenar(&resultado->datos,acarreo % 10);
                        acarreo /= 10;
                        i++;
                }
                while ((dato_pos_x(resultado,i) == 0) && (i>=0)) {
                        i--;
                }
                resultado->CantidadDigitos = i;
        }else{
                EnteroLargo *aux1 = creaEnteroLargo();
                EnteroLargo *aux2 = creaEnteroLargo();
                CopiaEnteroLargo(aux1,numero1);
                CopiaEnteroLargo(aux2,numero2);
                aux1->signo = '+';
                aux2->signo = '+';
                resultado = restaEnteroLargo(aux1,aux2);
                if (utilidades(*numero1, *numero2, COMPARAR_TODO) == 1) {
                        resultado->signo = numero1->signo;
                }else if (utilidades(*numero1, *numero2, COMPARAR_TODO) == -1) {
                        resultado->signo = numero2->signo;
                }else{
                        resultado->signo = '+';
                }
        }
        return resultado;
}

EnteroLargo *multiplicaEnteroLargo(EnteroLargo *numero1, EnteroLargo *numero2) {
        EnteroLargo *respuesta = creaEnteroLargo();
        EnteroLargo *auxiliar2 = creaEnteroLargo();
        EnteroLargo *auxiliar1 = creaEnteroLargo();
        EnteroLargo *sumador = creaEnteroLargoStr("1");
        EnteroLargo *verificador = creaEnteroLargoStr("1");
        CopiaEnteroLargo(auxiliar1,numero1);
        CopiaEnteroLargo(auxiliar2,numero2);
        CopiaEnteroLargo(respuesta,auxiliar1);
        auxiliar1->signo = '+';
        auxiliar2->signo = '+';
        respuesta->signo = '+';
        while((utilidades(*numero2, *verificador,VERIFICAR_SI_NUMEROS_SON_IGUALES)) != 0) {
                respuesta = sumaEnteroLargo(respuesta, auxiliar1);
                verificador = sumaEnteroLargo(verificador, sumador);
        }
        if(numero1->signo != numero2->signo) {
                respuesta->signo = '-';
        }
        EliminaEnteroLargo(auxiliar1);
        EliminaEnteroLargo(auxiliar2);
        EliminaEnteroLargo(sumador);
        EliminaEnteroLargo(verificador);
        return respuesta;
}


EnteroLargo *divideEnteroLargo(EnteroLargo *numerador, EnteroLargo *divisor){
        EnteroLargo *respuesta = creaEnteroLargoStr("1");
        EnteroLargo *auxiliar1 = creaEnteroLargo();
        EnteroLargo *auxiliar2 = creaEnteroLargo();
        EnteroLargo *sumador = creaEnteroLargoStr("1");
        EnteroLargo *variable_para_eliminar1;
        EnteroLargo *variable_para_eliminar2;
        char signo_resultado = '+';
        CopiaEnteroLargo(auxiliar1,numerador);
        CopiaEnteroLargo(auxiliar2,divisor);
        auxiliar1->signo = '+';
        auxiliar2->signo = '+';

        if ((numerador->signo == '+' && divisor->signo == '-') || (numerador->signo == '-' && divisor->signo == '+')) {
                signo_resultado = '-';
        }

        if (utilidades(*divisor, *divisor, VERIFICAR_NUMERO_IGUAL_A_0) == 0) { //evito la posibilidad de dividir x entre 0
                fprintf(stderr,"\n\npor favor, no divida entre cero, el resultado es indefinido.\n");
                EliminaEnteroLargo(sumador);
                EliminaEnteroLargo(auxiliar1);
                EliminaEnteroLargo(auxiliar2);
                EliminaEnteroLargo(respuesta);
                return NULL;
        }

        if (utilidades(*auxiliar1, *auxiliar2, COMPARAR_TODO) == 1) // me aseguro que el numerador sea mayor que el denominador, caso contrario, la solucion es cero (o 1)
        {
                do {

                        variable_para_eliminar1 = auxiliar1;
                        auxiliar1 = restaEnteroLargo(auxiliar1, auxiliar2);
                        EliminaEnteroLargo(variable_para_eliminar1);

                        if (utilidades(*auxiliar1, *auxiliar2, COMPARAR_DIGITO) < 0) { break; }

                        variable_para_eliminar2 = respuesta;
                        respuesta = sumaEnteroLargo(respuesta, sumador); //resultado + 1
                        EliminaEnteroLargo(variable_para_eliminar2);

                } while (utilidades(*auxiliar1, *auxiliar2, COMPARAR_TODO) > 0);

        } else {
                EliminaEnteroLargo(respuesta);
                if (utilidades(*auxiliar1, *auxiliar2, COMPARAR_TODO) == 0) { // caso en que ambos valores son iguales
                        respuesta = creaEnteroLargoStr("1");
                } else { // caso en que denominador es mayor que el numerador
                        respuesta = creaEnteroLargoStr("0");
                }
        }
        respuesta->signo = signo_resultado;
        EliminaEnteroLargo(sumador);
        EliminaEnteroLargo(auxiliar1);
        EliminaEnteroLargo(auxiliar2);
        return respuesta;
}

/*---------------------------------------------------------------------------------*/

/*--------------------------------MISCELANEO------------------------------------*/

int dato_pos_x(EnteroLargo *numero, int posicion) {
        int contador = 0;
        lista *aux = numero->datos;
        while (aux != NULL && posicion <= numero->CantidadDigitos) {
                if (contador == posicion) {
                        return aux->digitos;
                }
                aux = aux->siguiente;
                contador++;
        }
        return -1;
}

int cantidad_datos(lista *puntero) {
        int contador = 0;
        while (puntero != NULL) {
                puntero = puntero->siguiente;
                contador = contador + 1;
        }
        return contador;
}
void invertir_lista(lista **nodo) {
        lista *iterador = *nodo;
        lista *nodoAnterior = NULL;
        lista *nodoSiguiente = NULL;
        while (iterador != NULL) {
                nodoSiguiente = iterador->siguiente;
                iterador->siguiente = nodoAnterior;
                nodoAnterior = iterador;
                iterador = nodoSiguiente;
        }
        *nodo = nodoAnterior;
}

void eliminar_dato_pos_x(lista **digito, int posicion) {
        if (digito == NULL) {
                return;
        }
        lista *auxiliar = *digito;
        if (posicion == 0) {
                *digito = auxiliar->siguiente;
                free(auxiliar);
                return;
        }
        for (int i = 0; (auxiliar != NULL) && (i < posicion - 1); i++) {
                auxiliar = auxiliar->siguiente;
        }

        if (auxiliar == NULL || auxiliar->siguiente == NULL) {
                return;
        }

        lista *next = auxiliar->siguiente->siguiente;
        free(auxiliar->siguiente);
        auxiliar->siguiente = next;
}

EnteroLargo *ceros(EnteroLargo *resultado) {
        EnteroLargo *auxiliar = resultado;
        for (int i = resultado->CantidadDigitos - 1; i > 0; i--) { // como lo dice el nombre,
                if (dato_pos_x(auxiliar,i) == 0) {            // esta funcion sirve para eliminar
                        eliminar_dato_pos_x(&auxiliar->datos,i);// los ceros de la izquierda del resultado
                        auxiliar->CantidadDigitos--;
                } else {            // ej: +0061 -> +61
                        break;
                }
        }
        resultado = auxiliar;
        return resultado;
}

int verifica_numero(EnteroLargo *numero, EnteroLargo *verificador) {
        lista *aux1 = numero->datos;
        lista *aux2 = verificador->datos;

        while(aux1 != NULL && aux2 != NULL) {
                if (aux1->digitos != aux2->digitos) {
                        return 1;
                }
                aux1 = aux1->siguiente;
                aux2 = aux2->siguiente;
        }

        return 0;
}

void llenar(lista **destino,int algo){
        lista *nuevo_nodo = (lista*)malloc(sizeof(lista));
        nuevo_nodo->digitos = algo;
        lista *auxiliar1 = *destino;
        lista *auxiliar2;
        while (auxiliar1 != NULL) {
                auxiliar2 = auxiliar1;
                auxiliar1 = auxiliar1->siguiente;
        }
        if(*destino == auxiliar1) {
                *destino = nuevo_nodo;
        }else{
                auxiliar2->siguiente = nuevo_nodo;
        }
        nuevo_nodo->siguiente = auxiliar1;
}

int utilidades(EnteroLargo numero1, EnteroLargo numero2, int opcion) {
        // retorna 1 para cuando numero 1 es mayor que numero2 (numero1 > numero2)
        // retorna -1 cuando ambos valores son iguales         (numero1 = numero2)
        // retorna 0 cuando numero 2 es mayor que numero 1     (numero1 < numero2)

        EnteroLargo aux1 = numero1;
        EnteroLargo aux2 = numero2;

        switch (opcion) {
        case COMPARAR_LONGITUD:
                if (aux1.CantidadDigitos > aux2.CantidadDigitos) {
                        return 1;
                } else if (aux1.CantidadDigitos < aux2.CantidadDigitos) {
                        return -1;
                } else {
                        return 0;
                }
                break;
        case COMPARAR_DIGITO:
                for (int i = cantidad_datos(aux1.datos); i >= 0; i--) {
                        if (dato_pos_x(&aux1,i) > dato_pos_x(&aux2,i)) {
                                return 1;
                        } else if (dato_pos_x(&aux1,i) < dato_pos_x(&aux2,i)) {
                                return -1;
                        }
                }
                return 0;
                break;
        case COMPARAR_TODO:
                if (aux1.CantidadDigitos > aux2.CantidadDigitos) {
                        return 1;
                } else if (aux1.CantidadDigitos < aux2.CantidadDigitos) {
                        return -1;
                }
                for (int i = aux1.CantidadDigitos - 1; i >= 0; i--) {
                        if (dato_pos_x(&aux1,i) > dato_pos_x(&aux2,i)) {
                                return 1;
                        } else if (dato_pos_x(&aux1,i) < dato_pos_x(&aux2,i)) {
                                return -1;
                        }
                }
                return 0;
                break;
        case VERIFICAR_NUMERO_IGUAL_A_0:
                for (int i = 0; i < cantidad_datos(aux1.datos); i++) {
                        if (dato_pos_x(&aux1,i) != 0) {
                                return 1;
                        }
                }
                return 0;
                break;
        case VERIFICAR_SI_NUMEROS_SON_IGUALES:
                for(int i = aux1.CantidadDigitos-1; i>=0; i--) {
                        if(dato_pos_x(&aux2,i) != dato_pos_x(&aux1,i)) {
                                return 1;
                        }
                }
                return 0;
                break;
        }
        return 0;
}
char *version(){
        return "lista";
}
/*-----------------------------------------------------------------------------*/
