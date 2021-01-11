#ifndef stdio_h
#include <stdio.h>
#define stdio_h
#endif

#ifndef stdlib_h
#include <stdlib.h>
#define stdlib_h
#endif

#ifndef unistd_h
#include <unistd.h>
#define unistd_h
#endif

#ifndef nodo_h
#define nodo_h
#include "nodo.h"
#endif /* nodo_h */

#ifndef time_h
#define time_h
#include "time.h"
#endif /* time_h */

#ifndef tabla_h
#define tabla_h
#include "tabla.h"
#endif /* tabla_h */

#ifndef bosque_h
#define bosque_h
#include "bosque.h"
#endif /* bosque_h */

#ifndef archivos_comprimir_h
#define archivos_comprimir_h
#include "archivos comprimir.h"
#endif /* archivos_comprimir_h */

#ifndef archivos_descomprimir_h
#define archivos_descomprimir_h
#include "archivo descomprimir.h"
#endif /* archivos_descomprimir_h */

#ifndef archivos_arbol_h
#define archivos_arbol_h
#include "archivos arbol.h"
#endif /* archivos arbol_h */

void experimento(char *entrada){

        //sector de tiempos
        clock_t tiempo_general;
        clock_t tiempo_compresion;
        clock_t tiempo_compresion_general;
        clock_t tiempo_descompresion;
        clock_t tiempo_descompresion_general;
        double t_compresion[10];
        double t_descompresion[10];
        tiempo_general = clock();

        //sector de pesos
        int peso_comprimido[10];
        int peso_descomprimido[10];
        int peso_original[10];

        //declaraciones generales...
        Tabla* caracter_frecuencia = obtener_tabla_archivo(entrada);
        huffman(caracter_frecuencia);
        //muestraTabla(caracter_frecuencia);

        printf("\ncomprimiendo los 10 archivos, por favor espere...\n\n");
        int porcentajes[10] = { 10, 20, 30, 40, 50, 60, 70, 80, 90, 100 };
        char buff[5];
        char nombrecompleto[80] = "";

        printf("Tabla de resultados de compresion(todos los pesos son en bytes)\n\nnombre archivo original |\tpeso\t  |\tnombre archivo comprimido\t  |  peso\t| tiempo (en segundos)\n");
        printf("+-----------------------|-----------------|---------------------------------------|-------------|--------------------+\n");
        tiempo_compresion_general = clock();
        for(size_t i = 0; i < 10; i++) {
                tiempo_compresion = clock();
                itoa(porcentajes[i],buff,10);

                snprintf(nombrecompleto, sizeof nombrecompleto, "./resultados_experimento/compresion/archivo_comprimido_porcentaje_%s.huff", buff);

                comprimir_archivo(caracter_frecuencia,entrada,nombrecompleto,porcentajes[i]);

                tiempo_compresion = clock() - tiempo_compresion;
                t_compresion[i] = ((double)tiempo_compresion)/CLOCKS_PER_SEC;
                peso_comprimido[i] = bytes_del_archivo(nombrecompleto,100);
                peso_original[i] = bytes_del_archivo(entrada,porcentajes[i]);

                printf("%s (%i%c)| %i\t  | archivo_comprimido_porcentaje_%i.huff | %i\t| %lf\n",entrada,porcentajes[i],'%',peso_original[i],porcentajes[i],peso_comprimido[i],t_compresion[i]);

                nombrecompleto[0] = 0;
        }

        memset(peso_original, 0, sizeof(peso_original));
        tiempo_compresion_general = clock() - tiempo_compresion_general;
        tiempo_descompresion_general = clock();

        itoa(porcentajes[0],buff,10);

        snprintf(nombrecompleto, sizeof nombrecompleto, "./resultados_experimento/compresion/archivo_comprimido_porcentaje_%s.huff", buff);

        Tabla* frecuencias = interpretar_tabla(nombrecompleto);
        huffman(frecuencias);
        //muestraTabla(frecuencias);
        lista * valores = convertir_tabla_a_lista(frecuencias);

        printf("\ndescomprimiendo los 10 archivos, por favor espere...\n\n");
        char nombre_archivo_comprimido[80];
        printf("\n\nTabla de resultados de descompresion(todos los pesos son en bytes)\n\nnombre archivo original |\tpeso\t  |\tnombre archivo comprimido\t  |  peso\t| tiempo (en segundos)\n");
        printf("+-----------------------|-----------------|---------------------------------------|-------------|--------------------+\n");

        for(size_t i = 0; i < 10; i++) {
                tiempo_descompresion = clock();
                itoa(porcentajes[i],buff,10);

                snprintf(nombre_archivo_comprimido, sizeof nombre_archivo_comprimido, "./resultados_experimento/compresion/archivo_comprimido_porcentaje_%s.huff", buff);
                snprintf(nombrecompleto, sizeof nombrecompleto, "./resultados_experimento/descompresion/archivo_descomprimido_porcentaje_%s.txt", buff);

                descomprimir_archivo(valores,nombre_archivo_comprimido,nombrecompleto,1);

                tiempo_descompresion = clock() - tiempo_descompresion;
                t_descompresion[i] = ((double)tiempo_descompresion)/CLOCKS_PER_SEC;
                peso_descomprimido[i] = bytes_del_archivo(nombrecompleto,100);
                peso_original[i] = bytes_del_archivo(nombre_archivo_comprimido,100);

                printf("%s (%i%c)| %i\t  | archivo_descomprimido_porcentaje_%i.huff | %i\t| %lf\n",entrada,porcentajes[i],'%',peso_original[i],porcentajes[i],peso_descomprimido[i],t_descompresion[i]);

                nombrecompleto[0] = 0;
        }
        tiempo_descompresion_general = clock() - tiempo_descompresion_general;

        //obtengo el tiempo que tarda todo el experimento en realizarse//
        tiempo_general = clock() - tiempo_general;
        double t_general = ((double)tiempo_general)/CLOCKS_PER_SEC;
        printf("\narchivos comprimidos, el experimento ha finalizado, y se ha demorado %f segundos en completar todo.\n",t_general);
        printf("\nDISCLAIMER: el tiempo puede variar, dado a: velocidad del CPU, programas abiertos, etc.\n");
}
