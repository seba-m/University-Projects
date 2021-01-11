#ifndef stdio_h
#include <stdio.h>
#define stdio_h
#endif

#ifndef stdlib_h
#include <stdlib.h>
#define stdlib_h
#endif

#ifndef stdbool_h
#include <stdbool.h>
#define stdbool_h
#endif /*stdbool_h*/

#ifndef string_h
#include <string.h>
#define string_h
#endif

#ifndef time_h
#define time_h
#include "time.h"
#endif /* time_h */

#ifndef locale_h
#include <locale.h>
#define locale_h
#endif

#ifndef wchar_h
#include <wchar.h>
#define wchar_h
#endif

#ifndef unistd
#include <unistd.h>
#define unistd_h
#endif

#ifndef tabla_h
#define tabla_h
#include "tabla.h"
#endif /* tabla_h */

#ifndef bits_h
#define bits_h
#include "Bits.h"
#endif /* bits_h */

#define SIGUIENTE_DATO '-'

#define SEPARACION ':'

#define FIN_TABLA '~'

#define CARACTERES_TOTALES 256

typedef struct Lista { // esta estructura fue hecha para acelerar la descompresion.
        unsigned char caracter[CARACTERES_TOTALES];
        int longitud[CARACTERES_TOTALES];
        unsigned long long buffer[CARACTERES_TOTALES];
        int cantidad_caracteres_usado;
        unsigned long long int tamano_archivo;
}lista;

//la funcion interpretar_tabla, tal como dice su nombre, sirve para interpretar
//la tabla que fue escrita en el archivo.
Tabla * interpretar_tabla(char * name){
        FILE * archivo = fopen(name, "r");
        if(!archivo) {
                printf("no existe el archivo\n");
                exit(EXIT_FAILURE);
        }
        unsigned char letra;
        Tabla * esaTabla = iniciaTabla();
        char * ref;
        char caracter[5];
        char frecuencia[20];
        int pos = 0, i = 0, j = 0;

        //con esto, obtengo el tamano del archivo original.
        while (true) { //si da problemas, reemplazar con 1.
                letra = getc(archivo);
                if (letra != SIGUIENTE_DATO) {
                        caracter[i] = letra;
                        i++;
                }else{
                        break;
                }
        }

        //asigno al tamano del archivo, el string obtenido recien.
        esaTabla->tamano_archivo = atoi(caracter);
        i = 0;
        memset(caracter, '\0', strlen(caracter));


        //con esto, obtengo la cantidad de caracteres usados.
        while (true) { //si da problemas, reemplazar con 1.
                letra = getc(archivo);
                if (letra != SIGUIENTE_DATO) {
                        caracter[i] = letra;
                        i++;
                }else{
                        break;
                }
        }

        //asigno a la cantidad de caracteres, el string obtenido recien.
        esaTabla->cantidad_caracteres_usado = atoi(caracter);
        i = 0;
        memset(caracter, '\0', strlen(caracter));
        memset(frecuencia, '\0', strlen(frecuencia));

        // la explicacion de este ciclo es el siguiente:
        //imaginemos que tenemos lo siguiente:
        //32:355500-57:65410-
        //lo que hace esta funcion es:

        while (letra != FIN_TABLA && !feof(archivo)) {
                //obtiene la letra del archivo
                letra = getc(archivo);
                //comprueba si la letra es distinta de ':' , '-' y de '~' ('-' = separacion, ':' = siguiente dato y '~' = fin tabla).
                //                        pos 0 es el caracter
                if (letra != SEPARACION && pos == 0 && letra != SIGUIENTE_DATO && letra != FIN_TABLA) {
                        caracter[i] = letra;
                        i++;
                }else if (letra == SEPARACION && pos == 0 && letra != SIGUIENTE_DATO && letra != FIN_TABLA) {
                        i = 0;
                        pos++;
                        //                                pos 1 es la frecuencia
                }else if (letra != SEPARACION && pos == 1 && letra != SIGUIENTE_DATO && letra != FIN_TABLA) {
                        frecuencia[j] = letra;
                        j++;
                }else if (letra == SIGUIENTE_DATO) {
                        // en este caso, el caracter es '-', por lo que ya tiene el codigo decimal (32 = ' ') del caracter, y la frecuencia de este
                        // por lo que ahora tiene que agregar en la tabla los valores obtenidos.

                        unsigned char caracter1 = strtol(caracter, &ref, 10);//atoi(caracter);
                        int frecuencia1 = atoi(frecuencia);
                        agregaEnTabla(esaTabla,caracter1,frecuencia1);

                        // una vez agregado los datos en la tabla, se "limpian" las variables, para poder usarlas en otro caracter.
                        i = 0;
                        j = 0;
                        memset(caracter, '\0', strlen(caracter));
                        memset(frecuencia, '\0', strlen(frecuencia));

                        *frecuencia = 0;
                        pos = 0;

                }
        }
        // si llega hasta aca, significa que encontro el caracter '~', el cual simboliza el fin de la tabla.
        fclose(archivo);
        return esaTabla;
}

lista * convertir_tabla_a_lista(Tabla * tabla){
        //esta funcion se hizo para optimizar la descompresion, dado a que antes usabamos la funcion
        //dato_pos_x_tabla, pero el problema es que es un ciclo while, que era llamado constantemente
        //lo que aumentaba el costo de la descompresion (o(n)^cantidad de veces que repito el while)

        //la explicacion de esta funcion es la siguiente:
        //tomo el contenido de tabla, y se lo paso a lista (nodo a lista)
        //para convertir la lista enlazada a un arreglo, con el objetivo de acelerar la busqueda
        //de valores en las posiciones x.

        lista * valores = (lista*)malloc(sizeof(lista));
        Nodo * aux = tabla->primero;
        valores->cantidad_caracteres_usado = tabla->cantidad_caracteres_usado;
        valores->tamano_archivo = tabla->tamano_archivo;
        while (aux != NULL) {
                for (int i = 0; i < 256; i++) {
                        //en este caso, i es el valor decimal del caracter
                        //siendo 256 el tope, para que tome todos los valores.
                        if (i == aux->simbolo) {//en este caso comparo los valores decimales del caracter con el i
                                //si son iguales los simbolos, asigna en la lista los valores de la tabla.
                                valores->caracter[i] = aux->simbolo;
                                valores->longitud[i] = aux->longitud;
                                valores->buffer[i] = aux->binario;
                                break;
                        }
                }
                aux = aux->sgte;
        }
        return valores;
}

void verificar_tamano_archivo(FILE *ArchivoSalida, lista * codigos, bits * bits){

        fseek(ArchivoSalida, 0L, SEEK_END);
        unsigned long long int tamano_descomprimido = ftell(ArchivoSalida);
        if (tamano_descomprimido > codigos->tamano_archivo) {
                ftruncate(fileno(ArchivoSalida), codigos->tamano_archivo);
        }else if (tamano_descomprimido < codigos->tamano_archivo) {
                printf("\n\nel archivo descomprimido pesa menos que el original, por favor intente comprimir algo con mas de 15 caracteres.\ntamano descomprimido = %u, tamano original = %u\n",tamano_descomprimido,codigos->tamano_archivo);
        }
        return;
}

void descomprimir_archivo(lista *codigos, char *nombreEntrada, char *nombreSalida, int experimento)
{
        setlocale(LC_ALL, "");//esto sirve para poder interpretar caracteres especiales, como la ñ
        FILE *ArchivoEntrada = fopen(nombreEntrada, "rb");
        FILE *ArchivoSalida = fopen(nombreSalida, "wb");
        if(!ArchivoEntrada || !ArchivoSalida) {
                printf("no existe el archivo!\n");
                exit(EXIT_FAILURE);
        }

        //este ciclo fue hecho para saltarse la tabla, dado a que sin esto
        //intentaria interpretar el codigo de esos numeros, y se corromperia todo.
        char caracter;
        do {
                caracter = fgetc(ArchivoEntrada);
        } while (caracter != '~');

        unsigned long long buffer = 0;
        int longitud = 0;
        bits * codigo = crear_codigo();

        //este es el while que descomprime todo.
        while (!feof(ArchivoEntrada)) {
                int bit = leer_codigo_bits(ArchivoEntrada,&codigo);
                //obtiene un bit del archivo y se lo asigna a buffer
                buffer = buffer << 1 | bit;
                longitud++;
                for (int i = 0; i < CARACTERES_TOTALES; i++) {
                        //va comprobando si el buffer y la longitud es igual a al buffer y longitud de alguna letra
                        if (buffer == (codigos->buffer[i]) && longitud == (codigos->longitud[i])) {
                                //si el buffer y la longitud concuerda, escribe la letra en el archivo.
                                fputc(i,ArchivoSalida);
                                buffer = 0;
                                longitud = 0;
                                break;
                        }
                }
        }
        
        if (!experimento) {
          verificar_tamano_archivo(ArchivoSalida,codigos, codigo);
        }

        fclose(ArchivoEntrada);
}
