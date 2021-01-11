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

Tabla *ordenar_tabla (Tabla *laTabla){
        //tal como dice su nombre, ordena la tabla, usando el metodo bubble sort.
        for (int i = 0; i < laTabla->cantidad_caracteres_usado; i++) {
                Nodo* dato_actual = laTabla->primero;
                Nodo* siguiente_dato = dato_actual->sgte;
                Nodo* dato_anterior = NULL;

                while(siguiente_dato != NULL) {
                        if (dato_actual->frecuencia < siguiente_dato->frecuencia) {
                                if (dato_actual == laTabla->primero) {
                                        laTabla->primero = siguiente_dato;
                                } else {
                                        dato_anterior->sgte = siguiente_dato;
                                }
                                dato_actual->sgte = siguiente_dato->sgte;
                                siguiente_dato->sgte = dato_actual;

                                dato_anterior = siguiente_dato;
                                siguiente_dato = dato_actual->sgte;
                        }
                        else {
                                dato_anterior = dato_actual;
                                dato_actual = dato_actual->sgte;
                                siguiente_dato = dato_actual->sgte;
                        }
                }

        }
        return laTabla;
}
//Esta funcion lee los caracteres del archivo, y va insertandolo en la tabla.
Tabla *obtener_tabla_archivo(char *nombreEntrada){
        setlocale(LC_ALL, "");//esto sirve para poder obtener caracteres especiales, como la ñ
        Tabla *tablaArchivo = iniciaTabla();
        FILE *archivo = fopen(nombreEntrada, "rb");
        char letra;
        if (archivo == NULL) {
                printf("\nel archivo no existe.");
                exit(EXIT_FAILURE);
        } else {
             while (!feof(archivo)) {
                  letra = fgetc(archivo);
                  insertaSimbolo(tablaArchivo, letra);


             }
        }
        fclose(archivo);
        ordenar_tabla(tablaArchivo);
        return tablaArchivo;
}

int bytes_del_archivo(char* nombre, int porcentaje){
        //lo que hace esta funcion es obtener el tamaño en bytes del archivo

        //ademas puede obtener el x% del archivo, con la formula:
        //(i * porcentaje)/100
        //donde:
        //i es el tamaño en bytes del archivo
        //porcentaje es la cantidad de bytes que queremos usar del archivo
        FILE* fp = fopen(nombre, "r");
        if (fp == NULL) {
                printf("no existe el archivo!\n");
                exit(EXIT_FAILURE);
        }
        fseek(fp, 0L, SEEK_END);
        unsigned long long int i = ftell(fp);
        double tamano = (i * porcentaje)/100;
        fclose(fp);
        return tamano;
}

void escribir_tabla_en_archivo(Tabla * unaTabla, char* nombreEntrada, FILE* nombreSalida){
        //tal como lo dice el nombre, escribe la tabla en el archivo, para que la funcion descomprimir pueda obtener la tabla.
        unaTabla->tamano_archivo = bytes_del_archivo(nombreEntrada,100);
        fprintf(nombreSalida,"%i-%i-",unaTabla->tamano_archivo,unaTabla->cantidad_caracteres_usado);
        // el ':' simboliza la separacion entre el caracter y la frecuencia.
        // mientras que el '-' simboliza la separacion de la frecuencia y el caracter.

        // ej: 32:654322-65:345345
        // el 32 es el simbolo, el 654322 es la frecuencia
        // el - simboliza la separacion de estos, y el inicio de un nuevo caracter

        // lo que quedaria de la siguiente forma:
        // caracter: 32 frecuencia: 654322
        // caracter: 65 frecuencia 345345
        for (int i = 0; i < unaTabla->cantidad_caracteres_usado; i++) {
                fprintf(nombreSalida,"%i:%i-",dato_pos_x_tabla(unaTabla,i)->simbolo,dato_pos_x_tabla(unaTabla,i)->frecuencia);
        }
        fputc('~', nombreSalida);
}

void comprimir_archivo(Tabla *codigos, char *nombreEntrada, char *nombreSalida, int porcentaje){
        setlocale(LC_ALL, "");

        FILE *ArchivoEntrada = fopen(nombreEntrada, "rb");
        FILE *ArchivoSalida = fopen(nombreSalida, "wb+");

        //escribo la tabla en el archivo.

        escribir_tabla_en_archivo(codigos, nombreEntrada, ArchivoSalida);

        unsigned char letra;
        unsigned long long int cantidad_bytes = 0;

        // este limite es solo para el experimento, pero sirve para comprimir hasta cierta cantidad de bytes.
        unsigned long long int limite = bytes_del_archivo(nombreEntrada,porcentaje);

        //esta variable sirve para guardar la longitud, el buffer y la posicion, siendo posicion la cantidad de bits.
        bits * codigo = crear_codigo();

        while (!feof(ArchivoEntrada)) {
                letra = fgetc(ArchivoEntrada);

                //como dije antes, si quiero comprimir un archivo a un x% de bytes,
                //esta condicion se encarga de verificar que sea esa cantidad de bytes la que comprima.

                if (porcentaje != 0) {
                        cantidad_bytes++;
                        if (cantidad_bytes > limite) {
                                break;
                        }
                }

                //a medida que vaya leyendo una letra, irá escribiendo su codigo en bits en el archivo.
                for (int i = 0; i < codigos->cantidad_caracteres_usado; i++) {
                        if (letra == dato_pos_x_tabla(codigos, i)->simbolo) {
                                codigo->longitud = dato_pos_x_tabla(codigos, i)->longitud;
                                escribir_codigo_bits(ArchivoSalida,&codigo,dato_pos_x_tabla(codigos, i)->binario);
                                break;
                        }
                }
        }
        fclose(ArchivoEntrada);
        fclose(ArchivoSalida);
}
