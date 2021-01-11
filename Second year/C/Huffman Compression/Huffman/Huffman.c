//
//  main.c
//  Compreción de Huffman
//
//  Created by Sebastian Morgado, Roberto Contreras & Antony Rodriguez on 7/7/20.
//  Copyright © 2020 Sebastian Morgado, Roberto Contreras & Antony Rodriguez. All rights reserved.
//

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
#endif /*string_h*/

#ifndef comprimir_h
#define comprimir_h
#include "./codigo_base/comprimir.h"
#endif /* comprimir_h */

#ifndef descomprimir_h
#define descomprimir_h
#include "./codigo_base/descomprimir.h"
#endif /* descomprimir_h */

#ifndef experimento_h
#define experimento_h
#include "./codigo_base/experimento.h"
#endif /* experimento_h */

int main(int cantidad_argumentos, char *argumentos[]) {
        if (argumentos[1] != '\0' && !((strcmp(argumentos[1], "-a") == 0 || strcmp(argumentos[1], "-ayuda") == 0) || (strcmp(argumentos[1], "-c") == 0 || strcmp(argumentos[1], "-comprimir") == 0) || (strcmp(argumentos[1], "-d") == 0 || strcmp(argumentos[1], "-descomprimir") == 0) || (strcmp(argumentos[1], "-e") == 0 || strcmp(argumentos[1], "-experimento") == 0))) {
          printf("comando desconocido, use -a o -ayuda para ver la ayuda.\n");
          exit(0);
        }

        if(argumentos[1] != '\0' && (strcmp(argumentos[1], "-a") == 0 || strcmp(argumentos[1], "-ayuda") == 0)) {
                printf("para poder usar el programa, debe ser de esta forma:\n\n\thuffman -argumento NombreArchivoEntrada NombreArchivoSalida\n\n\nsiendo argumento:\n\n-c / -comprimir : para comprimir el ArchivoEntrada\n\n-d / -descomprimir : para descomprimir el ArchivoEntrada\n\n-e / -experimento : mediante un archivo dado, toma 1/10 sucesivamente (1/10,2/10,etc.) de cada archivo, para luego comprimirlos. en este caso, solo necesita un archivo de entrada, ya que generara automaticamente los archivos de salida. \n\n-a / -ayuda : para mostrar estos mensajes\n\n\nsiendo NombreArchivoEntrada : nombre del archivo en el cual se desea trabajar. este debe estar en la misma carpeta que el archivo c.\n\n\nsiendo NombreArchivoSalida : nombre del archivo en el cual se desea dejar el resultado. este se ubicara en la misma carpeta que el archivo c.\n\n\n");
                exit(0);
        }
        if (cantidad_argumentos == 4) {
                if(strcmp(argumentos[1], "-c") == 0 || strcmp(argumentos[1], "-comprimir") == 0) {
                        comprimir_huffman(argumentos[2],argumentos[3]);
                        exit(0);
                }

                if(strcmp(argumentos[1], "-d") == 0 || strcmp(argumentos[1], "-descomprimir") == 0) {
                        descomprimir_huffman(argumentos[2],argumentos[3]);
                        exit(0);
                }
        }else if (cantidad_argumentos < 4) {
                if (cantidad_argumentos == 3) {
                        if(strcmp(argumentos[1], "-e") == 0 || strcmp(argumentos[1], "-experimento") == 0) {
                                experimento(argumentos[2]);
                                exit(0);
                        }
                }
                printf("\nmuy pocos argumentos o archivos ingresados, intente ingresar el siguiente ejemplo: \n\n%s -c archivo.txt archivo.huf \n\n\no con el siguiente comando para el experimento: \n\n%s -e archivo.txt\n\n\no con el siguiente comando para la ayuda: \n\n%s -a\n",argumentos[0],argumentos[0],argumentos[0]);
                exit(EXIT_FAILURE);
        } else {
                printf("\nmuchos argumentos o archivos ingresados, intente ingresar el siguiente ejemplo: \n\n%s -c archivo.txt archivo.huf \n\n\no con el siguiente comando para el experimento: \n\n%s -e archivo.txt\n\n\no con el siguiente comando para la ayuda: \n\n%s -a\n",argumentos[0],argumentos[0],argumentos[0]);
                exit(EXIT_FAILURE);
        }
        exit(EXIT_SUCCESS);
        return 0;
}
