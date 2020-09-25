//
//  entero_largo_arreglo.h
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

#ifndef math_h
#define math_h
#include <math.h>
#endif /* math_h */

/*todos estos define, fueron hechos para mejorar la legibilidad del codigo, no afecta en nada a este.*/
#define COMPARAR_LONGITUD 1
#define COMPARAR_DIGITO 2
#define COMPARAR_TODO 3
#define VERIFICAR_DIVISOR_IGUAL_0 4


#define MAX_DIGITOS 1000

typedef struct nodo {
        int CantidadDigitos;
        int digitos[MAX_DIGITOS];
        char signo;
} EnteroLargo;
/*-------------------------------------------------------------------------*/
EnteroLargo *sumaEnteroLargo(EnteroLargo *, EnteroLargo *);
int utilidades(EnteroLargo numero1, EnteroLargo numero2, int opcion);
EnteroLargo *eliminar_ceros_resultado(EnteroLargo *resultado);
/*-------------------------------------------------------------------------*/

EnteroLargo *creaEnteroLargo() {
        // ----------- declaraciones ----------- //
        EnteroLargo *numero_vacio;

        numero_vacio = (EnteroLargo *)malloc(sizeof(EnteroLargo));
        numero_vacio->CantidadDigitos = 1;

        // ----------- llenar con ceros ----------- //

        for (int i = 0; i < MAX_DIGITOS; i++) {
                numero_vacio->digitos[i] = 0; // asignamos ceros en numero_vacio
        }
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
        } else {
                numero->signo = '+';
                numero->CantidadDigitos = 1;
                numero->digitos[0] = 0;
                return numero;
        }

        // ----------- separar digitos ----------- //

        numero->CantidadDigitos = floor(log10(abs(valor_ingresado))) + 1;

        for (int i = 0; valor_ingresado > 0; i++) {
                numero->digitos[i] = valor_ingresado % 10; // extraemos los digitos de la funcion
                valor_ingresado = valor_ingresado / 10; // para guardarlos en la estructura entero largo
        }
        //eliminar_ceros_resultado(numero);
        return numero;
}

EnteroLargo *creaEnteroLargoStr(char numero[MAX_DIGITOS]) {
        EnteroLargo *entero = creaEnteroLargo();
        char num_aux; int posicion;
        if (numero[0] == '-') {
                entero->CantidadDigitos = strlen(numero);
                posicion = entero->CantidadDigitos;
                entero->CantidadDigitos--;
                entero->signo = '-';
        } else {
                if(numero[0] == '+') {
                        entero->CantidadDigitos--;
                }
                entero->CantidadDigitos = strlen(numero);
                posicion = entero->CantidadDigitos;
                entero->signo = '+';
        }
        for (int i = 1; i <= entero->CantidadDigitos; i++) {
                num_aux = numero[posicion-1];
                entero->digitos[i-1] = atoi(&num_aux);
                posicion--;
        }
        eliminar_ceros_resultado(entero);
        return entero;
}

void CopiaEnteroLargo(EnteroLargo *destino, EnteroLargo *origen) {
        destino->CantidadDigitos = origen->CantidadDigitos;
        for (int i = 0; i < origen->CantidadDigitos; i++) {
                destino->digitos[i] = origen->digitos[i];
        }
        destino->signo = origen->signo;
}
void EscribeEnteroLargo(char Nombre_Archivo[], EnteroLargo *numero) {
        FILE *Archivo = fopen(Nombre_Archivo, "wb");
        if(Archivo == NULL)
        {
                fprintf(stderr,"fopen fallo al abrir el archivo, intentelo nuevamente.\n");
                return;
        }
        fwrite(numero, sizeof(EnteroLargo), 1, Archivo);
        fclose(Archivo);
}

EnteroLargo *leeEnteroLargo(char* Nombre_Archivo) {
        EnteroLargo *buffer = (EnteroLargo *)malloc(sizeof(EnteroLargo));
        FILE *Archivo = fopen(Nombre_Archivo, "rb");
        if(!Archivo)
        {
                fprintf(stderr,"fopen fallo al abrir el archivo, intentelo nuevamente.\n");
                return NULL;
        }
        fread(buffer, sizeof(EnteroLargo), 1, Archivo);
        fclose(Archivo);
        return buffer;
}

void MuestraEnteroLargo(EnteroLargo *numero) {
        if (numero != NULL) {
                fprintf(stdout,"%c", numero->signo);
                for (int i = numero->CantidadDigitos - 1; i >= 0; i--) {
                        fprintf(stdout,"%d", (numero)->digitos[i]);
                }
        }else{
                fprintf(stderr,"no se puede mostrar algo vacio/nulo\n");
        }

}

void EliminaEnteroLargo(EnteroLargo *numero){
        if (numero != NULL) {
                free (numero);/*libera la memoria almacenada en la variable numero*/
        }else{
                fprintf(stderr,"no hay necesidad de eliminar el entero largo, este ya esta vacio.\n");
        }
}

EnteroLargo *restaEnteroLargo(EnteroLargo *numero1, EnteroLargo *numero2) {
        int i = 0, resta = 0, sobra = 0, aux = 0;
        EnteroLargo *respuesta = creaEnteroLargo();
        EnteroLargo *auxiliar1 = creaEnteroLargo();
        EnteroLargo *auxiliar2 = creaEnteroLargo();
        if (numero1->signo != numero2->signo) {
                if (numero2->signo == '+') {
                        numero2->signo = '-';
                } else {
                        numero2->signo = '+';
                }
                respuesta = sumaEnteroLargo(numero1, numero2);
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
                                respuesta->digitos[i] = resta;
                        } else {
                                auxiliar1->signo = '+';
                        }
                } else if (utilidades(*numero1, *numero2, COMPARAR_TODO) == 0) {
                        EliminaEnteroLargo(auxiliar1);
                        EliminaEnteroLargo(auxiliar2);
                        return respuesta;
                }
                respuesta->signo = auxiliar1->signo;
        }
        respuesta->CantidadDigitos = auxiliar1->CantidadDigitos;
        for (i = 0; i < auxiliar2->CantidadDigitos; i++) {
                resta = auxiliar1->digitos[i] - auxiliar2->digitos[i] - sobra;
                if (resta < 0) {
                        resta += 10;
                        sobra = 1;
                } else {
                        sobra = 0;
                }
                respuesta->digitos[i] = resta;
        }
        while (sobra > 0) {
                aux = auxiliar1->digitos[i] - sobra;
                if (aux < 0) {
                        aux += 10;
                        sobra = 1;
                } else {
                        sobra = 0;
                }
                respuesta->digitos[i] = aux;
                i++;
        }
        while (i < auxiliar1->CantidadDigitos) {
                respuesta->digitos[i] = auxiliar1->digitos[i];
                i++;
        }
        eliminar_ceros_resultado(respuesta);
        EliminaEnteroLargo(auxiliar1);
        EliminaEnteroLargo(auxiliar2);
        return respuesta;
}

EnteroLargo *sumaEnteroLargo(EnteroLargo *numero1, EnteroLargo *numero2){
        EnteroLargo *respuesta = creaEnteroLargo();
        EnteroLargo *auxiliar1 = creaEnteroLargo();
        EnteroLargo *auxiliar2 = creaEnteroLargo();
        int i = 0, suma = 0, sobra = 0, aux = 0;
        if (numero1->signo != numero2->signo) {
                if (numero2->signo == '+') {
                        numero2->signo = '-';
                } else {
                        numero2->signo = '+';
                }
                respuesta = restaEnteroLargo(numero1, numero2);
                EliminaEnteroLargo(auxiliar1);
                EliminaEnteroLargo(auxiliar2);
                return respuesta;
        }else{
                if(numero1->CantidadDigitos > numero2->CantidadDigitos) {
                        CopiaEnteroLargo(auxiliar1, numero1);
                        CopiaEnteroLargo(auxiliar2, numero2);
                }else if(numero1->CantidadDigitos < numero2->CantidadDigitos) {
                        CopiaEnteroLargo(auxiliar2, numero1);
                        CopiaEnteroLargo(auxiliar1, numero2);
                }else{
                        CopiaEnteroLargo(auxiliar1, numero1);
                        CopiaEnteroLargo(auxiliar2, numero2);
                }
                respuesta->CantidadDigitos = auxiliar1->CantidadDigitos;
                respuesta->signo = auxiliar1->signo;
                for(i = 0; i < auxiliar2->CantidadDigitos; i++) {
                        suma = auxiliar1->digitos[i] + auxiliar2->digitos[i] + sobra;
                        if(suma > 9) {
                                suma -= 10;
                                sobra = 1;
                        }else{
                                sobra = 0;
                        }
                        respuesta->digitos[i] = suma;
                }
                while (sobra > 0) {
                        aux = auxiliar1->digitos[i] + sobra;
                        if(aux > 9) {
                                aux -=10;
                                sobra = 1;
                        } else{
                                sobra = 0;
                        }
                        respuesta->digitos[i] = aux;
                        i++;
                }
                while (i < auxiliar1->CantidadDigitos) {
                        respuesta->digitos[i] = auxiliar1->digitos[i];
                        i++;
                }
                if(i > 0) {
                        respuesta->CantidadDigitos++;
                }
        }
        eliminar_ceros_resultado(respuesta);
        EliminaEnteroLargo(auxiliar1);
        EliminaEnteroLargo(auxiliar2);
        return respuesta;
}

EnteroLargo *divideEnteroLargo(EnteroLargo *numerador, EnteroLargo *divisor) {
        EnteroLargo *resultado = creaEnteroLargoStr("1");
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

        if (utilidades(*numerador, *divisor, VERIFICAR_DIVISOR_IGUAL_0) == 0) { //evito la posibilidad de dividir x entre 0
                fprintf(stderr,"\n\npor favor, no divida entre cero, el resultado es indefinido.\n");
                EliminaEnteroLargo(sumador);
                EliminaEnteroLargo(auxiliar1);
                EliminaEnteroLargo(auxiliar2);
                EliminaEnteroLargo(resultado);
                return NULL;
        }

        if (utilidades(*auxiliar1, *auxiliar2, COMPARAR_TODO) == 1) // me aseguro que el numerador sea mayor que el denominador, caso contrario, la solucion es cero (o 1)
        {
                do {
                        variable_para_eliminar1 = auxiliar1;
                        auxiliar1 = restaEnteroLargo(auxiliar1, auxiliar2);
                        EliminaEnteroLargo(variable_para_eliminar1);

                        if (utilidades(*auxiliar1, *auxiliar2, COMPARAR_DIGITO) < 0) { break; }

                        variable_para_eliminar2 = resultado;
                        resultado = sumaEnteroLargo(resultado, sumador); //resultado + 1
                        EliminaEnteroLargo(variable_para_eliminar2);

                } while (utilidades(*auxiliar1, *auxiliar2, COMPARAR_TODO) > 0);

        } else {
                EliminaEnteroLargo(resultado);
                if (utilidades(*auxiliar1, *auxiliar2, COMPARAR_TODO) == 0) { // caso en que ambos valores son iguales
                        resultado = creaEnteroLargoStr("1");
                } else { // caso en que denominador es mayor que el numerador
                        resultado = creaEnteroLargoStr("0");
                }
        }
        resultado->signo = signo_resultado;
        EliminaEnteroLargo(sumador);
        EliminaEnteroLargo(auxiliar1);
        EliminaEnteroLargo(auxiliar2);
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
        while (utilidades(*auxiliar2, *verificador, COMPARAR_TODO) != 0) {
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


/*---------------------------MODDULOS DE AYUDA--------------------------------*/
EnteroLargo *eliminar_ceros_resultado(EnteroLargo *resultado) {
        EnteroLargo *auxiliar = resultado;

        for (int i = auxiliar->CantidadDigitos - 1; i > 0; i--) { // como lo dice el nombre,
                if (auxiliar->digitos[i] == 0) {            // esta funcion sirve para eliminar
                        resultado->CantidadDigitos--; // los ceros de la izquierda del resultado
                } else {            // ej: +0061 -> +61
                        break;
                }
        }
        return resultado;
}

int utilidades(EnteroLargo numero1, EnteroLargo numero2, int opcion) {
        // retorna 1 para cuando numero 1 es mayor que numero2 (numero1 > numero2)
        // retorna -1 cuando ambos valores son iguales         (numero1 = numero2)
        // retorna 0 cuando numero 2 es mayor que numero 1     (numero1 < numero2)
        switch (opcion) {
        case COMPARAR_LONGITUD:
                if (numero1.CantidadDigitos > numero2.CantidadDigitos) {
                        return 1;
                } else if (numero1.CantidadDigitos < numero2.CantidadDigitos) {
                        return -1;
                } else {
                        return 0;
                }
                break;
        case COMPARAR_DIGITO:
                for (int i = numero1.CantidadDigitos - 1; i >= 0; i--) {
                        if (numero1.digitos[i] > numero2.digitos[i]) {
                                return 1;
                        } else if (numero1.digitos[i] < numero2.digitos[i]) {
                                return -1;
                        }
                }
                return 0;
                break;
        case COMPARAR_TODO:
                if (numero1.CantidadDigitos > numero2.CantidadDigitos) {
                        return 1; // numero1 es mayor
                } else if (numero1.CantidadDigitos < numero2.CantidadDigitos) {
                        return -1; // numero2 es mayor
                } else if (numero1.CantidadDigitos == numero2.CantidadDigitos) {
                        for (int i = numero1.CantidadDigitos - 1; i >= 0; i--) {
                                if (numero1.digitos[i] > numero2.digitos[i]) {
                                        return 1; // numero1 es mayor
                                } else if (numero2.digitos[i] > numero1.digitos[i]) {
                                        return -1; // numero2 es mayor
                                }
                        }
                        return 0; // son iguales
                }
                break;
        case VERIFICAR_DIVISOR_IGUAL_0:
                for (int i = 0; i < numero2.CantidadDigitos; i++) {
                        if (numero2.digitos[i] != 0) {
                                return 1;
                        }
                }
                return 0;
                break;
        }
        return 0;
}
char * version(){
        return "arreglo";
}
/*----------------------------------------------------------------------------*/
Prueba de participación
