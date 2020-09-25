#include <stdio.h>
#include <stdlib.h>
#define max 5

typedef struct lista {
  int longitud;
  int contenido[max];
} lista;

void agregar_lista(lista *puntero, int valor) {
  if ((*puntero).longitud < max) {
    if (valor != 0) {
      (*puntero).contenido[(*puntero).longitud] = valor;
      (*puntero).longitud = (*puntero).longitud + 1;
      if (((max - (*puntero).longitud)) != 0)
      {
        system("CLS");
        printf("=== dato introducido correctamente, le quedan %i datos restantes. === \n\nescriba '-1' para salir, o ingrese otro dato.\n\n", ((max) - (*puntero).longitud));
      }
      else
      {
        system("CLS");
        printf("la lista esta llena. escriba '-1' para salir\n");
      }
    } else {
      system("CLS");
      printf("no puede ingresar el valor '0'.\n");
      printf("=== dato introducido incorrectamente === \n\nescriba '-1' para salir, o ingrese otro dato.\n\n");
    }

  } else {
    system("CLS");
    printf("la lista esta llena. escriba '-1' para salir\n");
  }
}

int crear_lista(lista *puntero, int *aux) {
  *aux = 1;
  (*puntero).longitud = 0;
  for (int i = 0; i < max; i++) {
    (*puntero).contenido[i] = 0;
  }
  return *aux;
}

void imprimir_lista(lista puntero, int aux) {
  printf("\n");
  if (aux == 1) {
    printf("=== lista cargada correctamente ===\n\n");
    for (int i = 0; i < max; i++) {
      printf(" %i ", puntero.contenido[i]);
    }
    printf("\n\n");
  } else {
    printf("la lista no ha sido generada, por favor genere una, e intentelo nuevamente.\n\n");
  }
}

int buscar(lista puntero, int dato) {
  for (int i = 0; i < max; i++) {
    if (puntero.contenido[i] == dato) {
      printf("dato > %i < encontrado\n\n", dato);
      for (int i = 0; i < max; i++) {
        if (puntero.contenido[i] == dato) {
          printf(" >%i< ", puntero.contenido[i]);
        } else {
          printf(" %i ", puntero.contenido[i]);
        }
      }
      return 0;
    }
  }
  return printf("el dato: %i no esta en la lista", dato);
}

void borrar_elementos(lista *puntero, int dato) {
  for (int i = 0; i < max; i++) {
    if ((*puntero).contenido[i] == dato) {
      (*puntero).contenido[i] = 0;
      (*puntero).longitud--;
      for (; i < max - 1; i++) {
        int aux = 0;
        (*puntero).contenido[i] = (*puntero).contenido[i + 1];
        (*puntero).contenido[i + 1] = aux;
      }
    }
  }
}

void borrar_lista(lista *puntero) {
  int i;
  (*puntero).longitud = 0;
  for (i = 0; i < max; i++) {
    (*puntero).contenido[i] = 0;
  }
}

int verificar_lista(lista puntero) {
  for (int i = 0; i < max; i++) {
    if (puntero.contenido[i] != 0) {
      return printf("\n\n=== la lista no esta vacia ===  \n\n");
    }
  }
  return printf("\n\n=== la lista esta vacia ===  \n\n");
}

void menu() {
  int opcion, usuario = 0, lista_generada = 0;
  lista puntero;
  do {
    system("CLS");
    printf("Copyright (c) 2020 Sebastian Morgado All Rights Reserved.\n\n");
    printf("Menu lista estatica [ordenada] \n\n");
    printf("elija una opcion:\n\n");
    printf("1. generar lista\n");
    printf("2. llenar lista\n");
    printf("3. ver lista\n");
    printf("4. buscar datos de la lista\n");
    printf("5. eliminar elementos\n");
    printf("6. eliminar lista\n");
    printf("7. verificar si la lista esta vacia\n");
    printf("8. salir del menu\n");
    printf("\nopcion: ");
    scanf("%i", &opcion);

    switch (opcion) {
    case 1: // generar lista
      system("CLS");
      crear_lista(&puntero, &lista_generada);
      printf("=== lista generada correctamente ===  \n\n");
      system("PAUSE");
      break;
    case 2: // ingresar contenido
      system("CLS");
      printf("ingrese contenido para la lista: ");
      scanf("%i", &usuario);
      while (usuario != -1) {
        agregar_lista(&puntero, usuario);
        scanf("%i", &usuario);
      }
      system("PAUSE");
      break;
    case 3: // cargar lista
      system("CLS");
      imprimir_lista(puntero, lista_generada);
      system("PAUSE");
      break;
    case 4: // ver si esta el dato
      system("CLS");
      printf("introduzca dato para saber si esta en la lista: ");
      scanf("%i", &usuario);
      printf("\n");
      buscar(puntero, usuario);
      printf("\n\n=== dato introducido correctamente ===  \n\n");
      system("PAUSE");
      break;
    case 5: // eliminar elemento
      system("CLS");
      printf("introduzca dato para eliminar de la lista: ");
      scanf("%i", &usuario);
      printf("\n");
      printf("\n\n=== dato introducido correctamente ===  \n\n");
      borrar_elementos(&puntero, usuario);
      system("PAUSE");
      break;
    case 6: // eliminar lista
      system("CLS");
      borrar_lista(&puntero);
      printf("\n\n=== lista eliminada correctamente ===\n\n");
      system("PAUSE");
      break;
    case 7: // verificar si esta vacia la lista
      system("CLS");
      verificar_lista(puntero);
      system("PAUSE");
      break;
    case 8: // salir del menu
      break;
    }
    system("CLS");
  } while (opcion != 8);
}

int main(void) {
	menu();
}
