#include <stdio.h>
#define n 5     // es el numero maximo del tablero (5x5)
#define pos_x 0 // posicion inicial en x
#define pos_y 0 // posicion inicial en y
#define todas_las_soluciones 1 // 1 = mostrar todas las soluciones, 0 = mostrar 1 solucion.

void iniciar_tablero(int tablero[n][n]);
void imprimir_tablero(int tablero[n][n]);
void buscar_solucion(int tablero[n][n], int x, int y, int *solucion, int contador);
int valido(int tablero[n][n], int x, int y, int opcion);

int main() {
  int tablero[n][n];
  int solucion = 0;
  iniciar_tablero(tablero);
  tablero[0][0] = 1;
  buscar_solucion(tablero, 0, 0, &solucion, 2);
  if (solucion == 0) {
    printf("no tiene solucion.");
  }
}

void buscar_solucion(int tablero[n][n], int x, int y, int *solucion, int contador) {
  int nx[8] = {-2, -1, 1, 2, 2, 1, -1, -2};
  int ny[8] = {1, 2, 2, 1, -1, -2, -2, -1};
  int opcion = 0;
  while (opcion < 8) {
    if (valido(tablero, x, y, opcion)) {
      tablero[x + nx[opcion]][y + ny[opcion]] = contador;
      if (contador == (n * n)) {
        *solucion = 1;
        printf("La solucion es : \n");
        imprimir_tablero(tablero);
        tablero[x + nx[opcion]][y + ny[opcion]] = 0;
      } else {
        buscar_solucion(tablero, (x + nx[opcion]), (y + ny[opcion]), solucion, contador + 1);
        if (*solucion == 0 || todas_las_soluciones) {
          tablero[x + nx[opcion]][y + ny[opcion]] = 0;
        }
      }
    }
    opcion++;
  }
}

int valido(int tablero[n][n], int x, int y, int opcion) {
  int nx[8] = {-2, -1, 1, 2, 2, 1, -1, -2};
  int ny[8] = {1, 2, 2, 1, -1, -2, -2, -1};
  if (x + nx[opcion] >= 0 && x + nx[opcion] < n) {
    if (y + ny[opcion] >= 0 && y + ny[opcion] < n) {
      if (tablero[x + nx[opcion]][y + ny[opcion]] == 0) {
        return 1;
      }
    }
  }
  return 0;
}

void iniciar_tablero(int tablero[n][n]) {
  for (int i = 0; i < n; i++) {
    for (int j = 0; j < n; j++) {
      tablero[i][j] = 0;
    }
  }
}

void imprimir_tablero(int tablero[n][n]) {
  for (int i = 0; i < n; i++) {
    for (int j = 0; j < n; j++) {
      printf("%4i", tablero[i][j]);
    }
    printf("\n");
  }
  printf("\n");
}
