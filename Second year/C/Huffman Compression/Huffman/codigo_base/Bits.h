#ifndef stdio_h
#include <stdio.h>
#define stdio_h
#endif

#ifndef stdlib_h
#include <stdlib.h>
#define stdlib_h
#endif

typedef struct Bits{
    int longitud;
    int posicion;
    unsigned char buffer;
}bits;

bits * crear_codigo(){
  bits * codigo = (bits*)malloc(sizeof(bits));
  codigo->longitud = 0;
  codigo->buffer = 0;
  codigo->posicion = 0;
  return codigo;
}

void escribir_codigo_bits(FILE *ArchivoSalida, bits ** bit, unsigned long long codigo) {
        for (int i = (*bit)->longitud - 1; i >= 0; i--) {
                (*bit)->buffer = (*bit)->buffer | ((codigo >> i) & 1) << (*bit)->posicion;
                (*bit)->posicion++;
                if ((*bit)->posicion == 8) {
                        (*bit)->buffer = fputc((*bit)->buffer, ArchivoSalida);
                        (*bit)->buffer = 0;
                        (*bit)->posicion = 0;
                }

        }
}

int leer_codigo_bits(FILE *ArchivoEntrada, bits ** codigo)
{
        if ((*codigo)->longitud == 0) {
                (*codigo)->buffer = fgetc(ArchivoEntrada);
        }
        int bit = (*codigo)->buffer & 1;
        (*codigo)->buffer = (*codigo)->buffer >> 1;
        (*codigo)->longitud++;
        if ((*codigo)->longitud == 8) {
                (*codigo)->longitud = 0;
        }
        return bit;
}
