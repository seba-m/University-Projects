#include <stdio.h>  // libreria usada para hacer el trabajo
#include <stdlib.h> // libreria usada para hacer el trabajo

#define max 5 // si desea que el tablero sea mas grande, aumente el valor de max

#define xInicial 0 // ubicacion del inicio, este x es la fila
#define yInicial 0 // ubicacion del inicio, este yes la columna
// por ejemplo, podria usar #define xInicial 1 y #define yInicial 0

#define xFinal 4 // ubicacion de la meta, este x es la fila
#define yFinal 4 // ubicacion de la meta, este y es la columna
// por ejemplo, podria usar #define xFinal 3 y #define yFinal 2

// by sebastian morgado
// Ing. computacion, ULS.

// -- declaraciones -- //

void devolverse(int tablero[max][max], int tablero_auxiliar[max][max], int *opcion, int *x, int *y, int *posicion);
int valido(int tablero[max][max], int ny, int nx);
void llenar_tablero(int tablero[max][max]);
void imprimir_tablero(int tablero[max][max]);
int buscar_una_solucion(int tablero[max][max], int x, int y);
int menu(int tablero[max][max]);
void una_solucion(int tablero[max][max]);
void camino_corto(int tablero[max][max]);
void varias_soluciones(int tablero[max][max]);
void buscar_camino_corto(int tablero[max][max], int x, int y);
int buscar_todas_soluciones(int tablero[max][max], int x, int y);
void buscar_mejor_solucion(int almacenar_contadores[2000000], int contador, int tablero[max][max], int x, int y, int posicion);
void imprimir_tableros_solucion(int tablero[max][max], int x, int y, int almacenar_contadores[200000], int contador, int posicion);

//--------------------------------------------------------------------------------------------------------------------------------//

int main() {
  //        _         _    __             _            //
  //       | |       | |  /_/            | |           //
  //   ___ | |__  ___| |_ __ _  ___ _   _| | ___  ___  //
  //  / _ \| '_ \/ __| __/ _` |/ __| | | | |/ _ \/ __| //
  // | (_) | |_) \__ \ || (_| | (__| |_| | | (_) \__ \ //
  //  \___/|_.__/|___/\__\__,_|\___|\__,_|_|\___/|___/ //

  int tablero[max][max];    // matriz para almacenar el tablero con la solucion.
  llenar_tablero(tablero);  // rellena el tablero con ceros
  tablero[3][4] = -1;       // x\y 0 | 1 | 2 | 3 | 4 < -- valores en y
  // tablero[x][y] = -1     //   ____|___|___|___|___
  tablero[1][0] = -1;       // 0 | 1 | 2 | 3 | 4 | 5
  tablero[1][1] = -1;       // 1 |-1 |-1 |-1 |-1 | 6
  tablero[1][2] = -1;       // 2 |11 |10 | 9 | 8 | 7
  tablero[1][3] = -1;       // 3 |12 |-1 |-1 |-1 |-1
  tablero[3][1] = -1;       // 4 |13 |14 |15 |16 | 17 <-- salida
  tablero[3][2] = -1;       // ^valores en x
  tablero[3][3] = -1;
  // tablero[3][5] = -1;       // asi mas o menos se veria con los puntos dados
  // tablero[1][4] = -1;       //por si acaso, si desea agregar mas obstaculos, ingrese tablero[x][y], donde "x" es la fila e "y" es la columna.

  menu(tablero); // inicia todo el laberinto :)
}

// --- POR FAVOR NO EDITAR NADA DE AQUI ABAJO PARA EVITAR BUGS, PROBLEMAS, ETC... NO ME HAGO RESPONSABLE POR MODIFICACIONES DE AQUI HACIA ABAJO --- //

int menu(int tablero[max][max]) {
  int opcion;
  do {
    system("cls");
    imprimir_tablero(tablero);
    printf("\n\n   1. laberinto con una solucion.");
    printf("\n   2. laberinto con todas sus soluciones. // es recomendable modificar los obstaculos antes de iniciar esta funcion.");
    printf("\n   3. laberinto con el camino mas corto.");
    printf("\n   4. salir.");
    printf("\n\n   Introduzca la accion que quiera realizar (1-4): ");
    scanf("%d", &opcion);

    if (opcion <= 0 || opcion >= 5) {
      system("cls");
      printf("\neleccion incorrecta, intentelo nuevamente\n");
      main();
    }

    switch (opcion) {
    case 1: {
      system("clear");
      system("cls");
      una_solucion(tablero);
      break;
    }
    case 2: {
      system("clear");
      system("cls");
      varias_soluciones(tablero);
      break;
    }
    case 3: {
      system("clear");
      system("cls");
      camino_corto(tablero);
      break;
    }
    case 4: {
      system("clear");
      system("cls");
      exit(0);
      break;
    }
    }
  } while (opcion != 5);
  return 0;
}

void una_solucion(int tablero[max][max]) {

  if (buscar_una_solucion(tablero, xInicial, yInicial)) // busca la solucion desde los puntos iniciales dados.
  {
    printf("hay solucion!\n\n");
    imprimir_tablero(tablero); // imprime el tablero con las respuestas
  } else {
    printf("no hay solucion :c\n\n");
    imprimir_tablero(tablero); // imprime el tablero con las respuestas
  }
  exit(0);
}

void camino_corto(int tablero[max][max]) {
  printf("\nel tablero es:\n\n");
  imprimir_tablero(tablero);
  printf("\n\ny el tablero de la solucion es:\n");
  buscar_camino_corto(tablero, xInicial, yInicial); // busca la solucion desde el punto cero,cero
  exit(0);
}

void varias_soluciones(int tablero[max][max]) {
  printf("\n\nel tablero es:\n\n");
  imprimir_tablero(tablero);
  printf("\n\ny los tableros de las soluciones son:\n");
  buscar_todas_soluciones(tablero, xInicial, yInicial); // busca la solucion desde el punto cero,cero
  exit(0);
}

void imprimir_tablero(int tablero[max][max]) {
  int i, j;
  for (i = 0; i < max; i++) {
    for (j = 0; j < max; j++) {
      if (tablero[i][j] == -1) {
        printf("%4i", tablero[i][j]);
      } else {
        printf("%4i", tablero[i][j]);
      }
    }
    printf("\n\n");
  }
}

void llenar_tablero(int tablero[max][max]) {
  int i, j;
  for (i = 0; i < max; i++) {
    for (j = 0; j < max; j++) {
      tablero[i][j] = 0;
    }
  }
}

int buscar_una_solucion(int tablero[max][max], int x, int y) {
  int tablero_auxiliar[max][max], opcion = 0, posicion = 2, ny, nx;
  llenar_tablero(tablero_auxiliar);
  int movy[4] = {1, 0, -1, 0};
  int movx[4] = {0, 1, 0, -1};
  tablero[0][0] = 1;
  while (opcion < 4) {
    ny = x + movy[opcion];
    nx = y + movx[opcion];

    if (valido(tablero, ny, nx)) {
      tablero[nx][ny] = posicion;
      if (ny == yFinal && nx == xFinal) {
        return 1;
      } else {
        tablero_auxiliar[x][y] = opcion;
        x = ny;
        y = nx;
        opcion = 0;
        posicion++;
      }
    } else {
      opcion++;
      if (opcion == 4) {
        while (posicion > 2 && opcion == 4) {
          devolverse(tablero, tablero_auxiliar, &opcion, &x, &y, &posicion);
        }
      }
    }
  }
  return 0;
}

int valido(int tablero[max][max], int ny, int nx) {
  if (ny >= 0 && ny < max) {
    if (nx >= 0 && nx < max) {
      if (tablero[nx][ny] == 0) {
        return 1;
      }
    }
  }
  return 0;
}

void devolverse(int tablero[max][max], int tablero_auxiliar[max][max], int *opcion, int *x, int *y, int *posicion) {
  int i, j;
  tablero[*x][*y] = 0;
  *posicion = *posicion - 1;
  for (i = 0; i < max; i++) {
    for (j = 0; j < max; j++) {
      if (tablero[i][j] == *posicion) {
        *x = i;
        *y = j;
        i = max;
        j = max;
      }
    }
  }
  *opcion = tablero_auxiliar[*x][*y] + 1;
  tablero_auxiliar[*x][*y] = 0;
}

int buscar_todas_soluciones(int tablero[max][max], int x, int y) {
  int tablero_auxiliar[max][max], opcion = 0, posicion = 1, ny, nx, solucion = 0;
  int movx[4] = {0, 1, 0, -1};
  int movy[4] = {1, 0, -1, 0};
  llenar_tablero(tablero_auxiliar);
  tablero[0][0] = posicion;
  while (opcion < 4) {
    ny = y + movy[opcion];
    nx = x + movx[opcion];
    if (valido(tablero, ny, nx)) {
      posicion++;
      tablero[nx][ny] = posicion;
      if (ny == yFinal && nx == xFinal) {
        solucion = 1;
        printf("\n");
        imprimir_tablero(tablero);
        tablero[nx][ny] = 0;
        posicion--;
        opcion++;
        if (opcion == 4) {
          devolverse(tablero, tablero_auxiliar, &opcion, &x, &y, &posicion);
        }
      } else {
        tablero_auxiliar[x][y] = opcion;
        x = nx;
        y = ny;
        opcion = 0;
      }
    } else {
      opcion++;
      if (opcion == 4) {
        while (posicion > 1 && opcion == 4) {
          devolverse(tablero, tablero_auxiliar, &opcion, &x, &y, &posicion);
        }
      }
    }
  }
  return solucion;
}

void buscar_camino_corto(int tablero[max][max], int x, int y) {
  int tablero_auxiliar[max][max], opcion = 0, posicion = 1, ny, nx;
  int almacenar_contadores[10000], solucion = 0, contador;
  int movx[4] = {0, 1, 0, -1};
  int movy[4] = {1, 0, -1, 0};
  llenar_tablero(tablero_auxiliar);
  tablero[0][0] = posicion;
  while (opcion < 4) {
    ny = y + movy[opcion];
    nx = x + movx[opcion];
    if (valido(tablero, ny, nx)) {
      posicion++;
      tablero[nx][ny] = posicion;
      if (ny == yFinal && nx == xFinal) {
        contador = tablero[nx] [ny]; // asignar un contador con la posicion final de x e y, ej tablero[xFinal][yFinal] = 11, contador = 11
        almacenar_contadores[solucion] = contador; // ahora el contador generado recien se almacena en el modulo almacenar contadores
        solucion++;   // se le suma 1 a solucion (que esta en almacenar_contadores[solucion]), para que siga con el siguiente contador
        tablero[nx][ny] = 0; // aqui se iguala a cero para que retroceda
        posicion--;
        opcion++;
        if (opcion == 4) {
          devolverse(tablero, tablero_auxiliar, &opcion, &x, &y, &posicion); // backtracking iterativo
        }
      } else {
        tablero_auxiliar[x][y] = opcion;
        x = nx;
        y = ny;
        opcion = 0;
      }
    } else {
      opcion++;
      if (opcion == 4) {
        while (posicion > 1 && opcion == 4) {
          devolverse(tablero, tablero_auxiliar, &opcion, &x, &y, &posicion); // backtracking
        }
      }
    }
  }
  buscar_mejor_solucion(almacenar_contadores, contador, tablero, x, y, posicion);
}

void imprimir_tableros_solucion(int tablero[max][max], int x, int y, int almacenar_contadores[200000], int contador, int posicion) {
  // este modulo es igual al anterior, solo que con la diferencia que en vez de imprimir todos los tableros, solo imprimira los tableros con movimientos = contador, ej: 11.
  int tablero_auxiliar[max][max], opcion = 0, ny, nx;
  int movx[4] = {0, 1, 0, -1};
  int movy[4] = {1, 0, -1, 0};
  llenar_tablero(tablero_auxiliar);
  tablero[0][0] = posicion;
  while (opcion < 4) {
    ny = y + movy[opcion];
    nx = x + movx[opcion];
    if (valido(tablero, ny, nx)) {
      posicion++;
      tablero[nx][ny] = posicion;
      if (ny == yFinal && nx == xFinal) {
        contador = tablero[nx][ny];
        if (contador ==
            almacenar_contadores[0]) // aqui si la posicion final de x e y = contador (que es el menor en este caso)
        { // imprimira ese tablero, con esa cantidad de movimientos
          imprimir_tablero(tablero); // por ejemplo, todos los tableros con 11 movimientos.
          printf("\n");
        }
        tablero[nx][ny] = 0;
        posicion--;
        opcion++;
        if (opcion == 4) {
          devolverse(tablero, tablero_auxiliar, &opcion, &x, &y, &posicion); // backtracking
        }
      } else {
        tablero_auxiliar[x][y] = opcion;
        x = nx;
        y = ny;
        opcion = 0;
      }
    } else {
      opcion++;
      if (opcion == 4) {
        while (posicion > 1 && opcion == 4) {
          devolverse(tablero, tablero_auxiliar, &opcion, &x, &y, &posicion); // backtracking
        }
      }
    }
  }
}

void buscar_mejor_solucion(int almacenar_contadores[2000000], int contador, int tablero[max][max], int x, int y, int posicion) {
  for (int i = 0; i < contador; i++) {
    for (int j = i + 1; j < contador - 1; j++) {
      if (almacenar_contadores[j] > almacenar_contadores[j + 1]) {
        int temp = almacenar_contadores[j];
        almacenar_contadores[j] = almacenar_contadores[j + 1];
        almacenar_contadores[j + 1] = temp;
      }
    }
  }
  x = 0, y = 0;
  system("cls");
  printf("\nLa menor cantidad de pasos que se pueden recorrer en el tablero son %i pasos y los posibles tableros son: \n\n", almacenar_contadores[0]);
  imprimir_tableros_solucion(tablero, x, y, almacenar_contadores, contador, posicion);
}
