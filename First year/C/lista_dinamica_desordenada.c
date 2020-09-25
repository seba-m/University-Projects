#include <stdio.h>
#include <stdlib.h>

typedef struct nodo {
  int contenido;
  struct nodo *direccion;
} * lista;

void llenar(lista *puntero, int dato) {
  lista nuevo = (lista)malloc(sizeof(struct nodo));
  nuevo->contenido = dato;

  lista actual = *puntero;
  lista anterior;

  while ((actual != NULL)) {
    anterior = actual;
    actual = actual->direccion;
  }

  if (*puntero == actual) {
    *puntero = nuevo;
  } else {
    anterior->direccion = nuevo;
  }

  nuevo->direccion = actual;
}

void mostrar(lista puntero) {
  lista nuevo = puntero;
  while (nuevo != NULL) {
    printf("%i ", nuevo->contenido);
    nuevo = nuevo->direccion;
    if (nuevo != NULL) {
      printf("-> ");
    }
  }
  printf("\n\n");
}

void buscar(lista puntero, int dato) {
  int aux = 0;
  lista nuevo;
  nuevo = puntero;
  while ((nuevo != NULL) && (nuevo->contenido <= dato)) {
    if (nuevo->contenido == dato) {
      aux = 1;
    }
    nuevo = nuevo->direccion;
  }

  if (aux == 1) {
    printf("dato > %i < encontrado", dato);
  } else {
    printf("el dato: %i no esta en la lista", dato);
  }
}

void crear(lista *puntero) { *puntero = NULL; }

void borrar_elementos(lista puntero, int dato) {
  if (puntero != NULL) {
    lista aux = puntero; // aux == auxiliar
    lista ant;           // ant == anterior
    while ((aux != NULL) && (aux->contenido != dato)) {
      ant = aux;
      aux = aux->direccion;
    }
    if (aux == NULL) {
      printf("\n\n=== el valor indicado no existe ===  \a\n\n");
    } else if (ant == NULL) {
      puntero = puntero->direccion;
      free(aux);
      printf("\n\n=== el valor ha sido eliminado correctamente ===  \a\n\n");
    } else {
      ant->direccion = aux->direccion;
      free(aux);
      printf("\n\n=== el valor ha sido eliminado correctamente ===  \a\n\n");
    }
  }
}

void borrar_lista(lista *puntero) {
  lista actual = *puntero;
  lista siguiente;
  while (actual != NULL) {
    siguiente = actual->direccion;
    free(actual);
    actual = siguiente;
  }
  *puntero = NULL;
}

int verificar_lista(lista puntero) {
  if (puntero == NULL) {
    return printf("\n\n=== la lista esta vacia ===  \n\n");
  } else {
    return printf("\n\n=== la lista no esta vacia ===  \n\n");
  }
}

void menu() {
  int opcion, usuario = 0;
  lista puntero;
  do {
    system("CLS");
    printf("Copyright (c) 2020 Sebastian Morgado All Rights Reserved.\n\n");
    printf("Menu lista dinamica [desordenada] \n\n");
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
      crear(&puntero);
      printf("\n\n=== lista generada correctamente ===  \n\n");
      system("PAUSE");
      break;
    case 2: // ingresar datos
      system("CLS");
      printf("ingrese datos para la lista: ");
      scanf("%i", &usuario);
      while (usuario != -1) {
        llenar(&puntero, usuario);
        printf("\n\n=== dato introducido correctamente ===  \n\nescriba '-1' para salir, o ingrese otro dato.\n\n");
        scanf("%i", &usuario);
      }
      system("PAUSE");
      break;
    case 3: // cargar lista
      system("CLS");
      printf("\n\n=== lista cargada correctamente ===\n\n");
      mostrar(puntero);
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
      borrar_elementos(puntero, usuario);
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

int main(void) { menu(); }
