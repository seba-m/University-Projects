/*problema de las 8 reinas por sebastian morgado.*/
#include <stdio.h>
#include <stdlib.h>

#define n 8 // aqui es el tamaño del tablero

void generar_tablero_vacio(int tablero[n][n]);
void imprimir_tablero(int tablero[n][n]);
void buscar_solucion(int tablero[n][n]);
int verificar_horizontal(int tablero[n][n], int x);
int verificar_vertical(int tablero[n][n], int y);
int verificar_diagonal_superior_derecha(int tablero[n][n], int x, int y);
int verificar_diagonal_superior_izquierda(int tablero[n][n], int x, int y);
int verificar_diagonal_inferior_derecha(int tablero[n][n], int x, int y);
int posicion_valida(int tablero[n][n], int x, int y);
int verificar_diagonal_inferior_izquierda(int tablero[n][n], int x, int y);
int buscar_en_la_fila(int tablero[n][n], int y);
void volver(int tablero[n][n], int *x1, int *y);

/*funciones principales del programa*/

int main() {
  int tablero[n][n];
  if (n == 3 || n == 2 || n == 0) {
    system("clear");
    printf("el numero %i no tiene solucion.\n\n", n);
    exit(0);
  }
  generar_tablero_vacio(tablero);

  buscar_solucion(tablero); // backtracking
}

/*funciones del tablero*/

void generar_tablero_vacio(int tablero[n][n]) {
  for (int i = 0; i < n; i++) {
    for (int j = 0; j < n; j++) {
      tablero[i][j] = 0;
    }
  }
}

void imprimir_tablero(int tablero[n][n]) {
  for (int i = 0; i < n; i++) {
    for (int j = 0; j < n; j++) {
      if (tablero[i][j] != 0) {
        printf("%4i", tablero[i][j]);
      } else {
        printf("%4i", tablero[i][j]);
      }
    }
    printf("\n\n");
  }
  printf("\n");
}

/*aqui se verifica alrededor, para ver si choca con algo, de ser asi, retorna 0 y si ocurre lo contrario, retorna 1*/

int posicion_valida(int tablero[n][n], int x, int y) {
  if ((verificar_vertical(tablero, y) == 1) && (verificar_horizontal(tablero, x) == 1) && (verificar_diagonal_inferior_derecha(tablero, x, y) == 1) && (verificar_diagonal_inferior_izquierda(tablero, x, y) == 1) && (verificar_diagonal_superior_derecha(tablero, x, y) == 1) && (verificar_diagonal_superior_izquierda(tablero, x, y) == 1)) {
    return 1;
  } else {
    return 0;
  }
}

int verificar_horizontal(int tablero[n][n], int x) {
  for (int j = 0; j < n; j++) {
    if (tablero[x][j] != 0) {
      return 0;
    }
  }
  return 1;
}

int verificar_vertical(int tablero[n][n], int y) {
  for (int i = 0; i < n; i++) {
    if (tablero[i][y] != 0) {
      return 0;
    }
  }
  return 1;
}

int verificar_diagonal_superior_derecha(int tablero[n][n], int x, int y) {
  int i = x, j = y;
  while (i < n && j < n) {
    if (tablero[i][j] != 0) {
      return 0;
    }
    i++;
    j++;
  }
  return 1;
}

int verificar_diagonal_superior_izquierda(int tablero[n][n], int x, int y) {
  int i = x, j = y;
  while (i >= 0 && j < n) {
    if (tablero[i][j] != 0) {
      return 0;
    }
    i--;
    j++;
  }
  return 1;
}

int verificar_diagonal_inferior_derecha(int tablero[n][n], int x, int y) {
  int i = x, j = y;
  while (i < n && j >= 0) {
    if (tablero[i][j] != 0) {
      return 0;
    }
    i++;
    j--;
  }
  return 1;
}

int verificar_diagonal_inferior_izquierda(int tablero[n][n], int x, int y) {
  int i = x, j = y;
  while (i >= 0 && j >= 0) {
    if (tablero[i][j] != 0) {
      return 0;
    }
    i--;
    j--;
  }
  return 1;
}

/*-----------------------------------------------------------------------------------------------------------------*/

int buscar_en_la_fila(int tablero[n][n], int y) {
  for (int i = 0; i < n; i++) {
    if (tablero[i][y] != 0) {
      return i;
    }
  }
  return 0;
}

void volver(int tablero[n][n], int *x, int *y) {
  (*y)--;
  *x = buscar_en_la_fila(tablero, *y);
  tablero[*x][*y] = 0;
  (*x)++;
}

void buscar_solucion(int tablero[n][n]) {
  int x = 0, y = 0, aux = 0, reina = 0;
  while (x < n) {
    if (posicion_valida(tablero, x, y) == 1) {
      tablero[x][y] = reina + 1;
      if ((reina + 1) == n) {
        aux++;
        printf("\nsolucion numero %i\n", aux);
        imprimir_tablero(tablero);
        tablero[x][y] = 0;
        x++;
        while (x == n) {
          reina--;
          volver(tablero, &x, &y);
        }
      } else {
        y++;
        x = 0;
        reina++;
      }
    } else {
      x++;
      while (x == n && y != 0) {
        reina--;
        volver(tablero, &x, &y);
      }
    }
  }
}

/*----------------------------------------------------------------------------------------------------------------------*/
