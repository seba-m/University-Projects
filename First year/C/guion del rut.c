#include <stdio.h>

int main() {
  int c, ci, cic, cicl, ciclo, ciclom, ciclomm, n, resto, test, u;
  printf("por favor ingrese su rut sin el numero despues del guion\n");
  scanf("%i", &n);
  ciclomm = 0;
  ciclom = 0;
  ciclo = 0;
  cicl = 0;
  cic = 0;
  ci = 0;
  c = 0;
  u = 0;
  if (30000000 > n) {
    if (!(10000000 < n)) {
    } else {
      while ((n >= 10000000)) {
        ciclomm++;
        n = n - 10000000;
      }
    }
    if (!(1000000 < n)) {
    } else {
      while ((n >= 1000000)) {
        ciclom++;
        n = n - 1000000;
      }
    }
    if (!(100000 < n)) {
    } else {
      while ((n >= 100000)) {
        ciclo++;
        n = n - 100000;
      }
    }
    if (!(10000 < n)) {
    } else {
      while ((n >= 10000)) {
        cicl++;
        n = n - 10000;
      }
    }
    if (!(1000 < n)) {
    } else {
      while ((n >= 1000)) {
        cic++;
        n = n - 1000;
      }
    }
    if (!(100 < n)) {
    } else {
      while ((n >= 100)) {
        ci++;
        n = n - 100;
      }
    }
    if (!(10 < n)) {
    } else {
      while ((n >= 10)) {
        c++;
        n = n - 10;
      }
    }
    if (!(1 < n)) {
    }
    u = n;
    resto = (ciclomm * 3 + ciclom * 2 + ciclo * 7 + cicl * 6 + cic * 5 + ci * 4 + c * 3 + u * 2) % 11;
  } else {
  }
  if (resto == 0) {
    printf("el numero verificador es 0\n");
  } else {
    test = 11 - resto;
    if ((test == 10)) {
      printf("el numero verificador es k\n");
    } else {
      printf("el numero verificador es %i\n", test);
    }
  }
  return 0;
}
