#include <stdio.h>
#include <stdlib.h>

void comprobacionnumeros(int *m, int *c, int *d, int *u);
void toqueyfama();
void numerosrandom(int *random1, int *random2, int *random3, int *random4);

int main() {
  int seguir;
  seguir = 0;
  while (seguir == 0) {
    printf("toque y fama\n");
    toqueyfama();
    printf("por lo que veo has llegado hasta aqui, deseas continuar jugando? 1=si, 2=no\n");
    scanf("%i", &seguir);
    if ((seguir == 1)) {
      seguir--;
    }
  }
  return 0;
}

void comprobacionnumeros(int *m, int *c, int *d, int *u) {
  int centecima, decena, milesima, n, unidad, valido = 1;
  while (valido == 1) {
    milesima = 0;
    centecima = 0;
    decena = 0;
    unidad = 0;
    scanf("%i", &n);
    if ((n >= 100 && n <= 9999)) {
      while (n >= 1000) {
        n = n - 1000;
        milesima++;
      }
      while (n >= 100) {
        n = n - 100;
        centecima++;
      }
      while (n >= 10) {
        n = n - 10;
        decena++;
      }
      while (n >= 1) {
        n--;
        unidad++;
      }
      if (!(milesima == centecima || milesima == decena || milesima == unidad || centecima == decena || centecima == unidad || decena == unidad)) {
        valido = 0;
      } else {
        printf("Porfavor, los digitos del numero deben ser diferentes entre si\n");
      }
    } else {
      printf("el numero ingresado no es valido, porfavor ingresa un numero entre 0 y 9999\n");
    }
  }
  (*m) = milesima;
  (*c) = centecima;
  (*d) = decena;
  (*u) = unidad;
}

void toqueyfama() {
  int acertado, centena, decena, fama, intento, milesima, random1, random2, random3, random4, toque, unidad;
  intento = 10;
  acertado = 0;
  numerosrandom(&random1, &random2, &random3, &random4);
  while (intento >= 1 && acertado != 1) {
    toque = 0;
    fama = 0;
    comprobacionnumeros(&milesima, &centena, &decena, &unidad);
    if (((milesima != random1) || (centena != random2) || (decena != random3) || (unidad != random4))) {
      if ((milesima == random1)) {
        fama++;
      } else {
        if ((milesima == random2 || milesima == random3 || milesima == random4)) {
          toque++;
        }
      }
      if ((centena == random2)) {
        fama++;
      } else {
        if ((centena == random1 || centena == random3 || centena == random4)) {
					toque++;
        }
      }
      if ((decena == random3)) {
        fama++;
      } else {
        if ((decena == random1 || decena == random2 || decena == random4)) {
          toque++;
        }
      }
      if ((unidad == random4)) {
        fama++;
      } else {
        if ((unidad == random1 || unidad == random2 || unidad == random3)) {
          toque++;
        }
      }
      intento--;
      printf("Tienes %i toques y %i famas , te quedan %i intentos restantes y tu numero fue %i%i%i%i\n", toque, fama, intento, milesima, centena, decena, unidad);
    } else {
      printf("felicidades, acertaste al numero %i%i%i%i\n", milesima, centena, decena, unidad);
      acertado = 1;
    }
  }
}

void numerosrandom(int *random1, int *random2, int *random3, int *random4) {
  int nr1;
  int nr2;
  int nr3;
  int nr4;
  nr1 = (rand() % 10);
  nr2 = (rand() % 10);
  nr3 = (rand() % 10);
  nr4 = (rand() % 10);
  while (nr1 == nr2 || nr1 == nr3 || nr1 == nr4 || nr2 == nr3 || nr2 == nr4 || nr3 == nr4) {
    nr1 = (rand() % 10);
    nr2 = (rand() % 10);
    nr3 = (rand() % 10);
    nr4 = (rand() % 10);
  }
  (*random1) = nr1;
  (*random2) = nr2;
  (*random3) = nr3;
  (*random4) = nr4;
}
