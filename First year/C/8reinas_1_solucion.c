#include <stdio.h>
#include <stdlib.h>

#define max 8

void llenar_vacio_tablero(int tabla[max][max]);
void imprimir_tablero(int tabla[max][max]);
void busca_solucion(int tabla[max][max]);
int valido(int tabla[max][max], int x, int y);
int horizontal(int tabla[max][max], int x, int y);
int vertical(int tabla[max][max], int x, int y);
int diagonal_p_arriba(int tabla[max][max], int x, int y);
int diagonal_p_abajo(int tabla[max][max], int x, int y);
int diagonal_s_arriba(int tabla[max][max], int x, int y);
int diagonal_s_abajo(int tabla[max][max], int x, int y);
void retrocede(int tabla[max][max], int *y, int *x);
int buscar_ficha(int tabla[max][max], int **x);

int main()
{
								int tabla[max][max];
								llenar_vacio_tablero(tabla);
								imprimir_tablero(tabla);
								busca_solucion(tabla);
								imprimir_tablero(tabla);
}

void llenar_vacio_tablero(int tabla[max][max])
{
								for (int i = 0; i < max; i++)
								{
																for (int j = 0; j < max; j++)
																{
																								tabla[i][j] = 0;
																}
								}
}

void imprimir_tablero(int tabla[max][max])
{
								for (int i = 0; i < max; i++)
								{
																for (int j = 0; j < max; j++)
																{
																								printf("%2i", tabla[i][j]);
																}
																printf("\n");
								}
								printf("\n");
}

void busca_solucion(int tabla[max][max])
{
								int x = 0, y = 0, fila = 0, solucion = 0, reina = 0;

								while (solucion == 0 && fila < max)
								{
																if (valido(tabla, x, y) == 1)
																{
																								tabla[y][x] = reina + 1;
																								fila++;
																								reina++;
																								x++;
																								y = 0;
																								if (reina == max)
																								{
																																solucion = 1;
																																printf("El tablero tiene solucion!!\n\n");
																								}
																}
																else
																{
																								y++;
																								while (y == max && x != 0)
																								{
																																reina--;
																																fila--;
																																retrocede(tabla, &y, &x);
																								}
																}
								}
}

int valido(int tabla[max][max], int x, int y)
{
								if ((horizontal(tabla, x, y) == 1) && (vertical(tabla, x, y) == 1) && (diagonal_p_arriba(tabla, x, y) == 1) && (diagonal_p_abajo(tabla, x, y) == 1) && (diagonal_s_arriba(tabla, x, y) == 1) && (diagonal_s_abajo(tabla, x, y) == 1))
								{
																return 1;
								}
								else
								{
																return 0;
								}
}

int horizontal(int tabla[max][max], int x, int y)
{
								for (int j = 0; j < max; j++)
								{
																if (tabla[y][j] != 0)
																{
																								return 0;
																}
								}
								return 1;
}

int vertical(int tabla[max][max], int x, int y)
{
								for (int i = 0; i < max; i++)
								{
																if (tabla[i][x] != 0)
																{
																								return 0;
																}
								}
								return 1;
}

int diagonal_p_arriba(int tabla[max][max], int x, int y)
{
								int i = x, j = y;
								while ((i >= 0 && j >= 0))
								{
																if (tabla[j][i] != 0)
																{
																								return 0;
																}
																else
																{
																								i--;
																								j--;
																}
								}
								return 1;
}

int diagonal_p_abajo(int tabla[max][max], int x, int y)
{
								int i = x, j = y;
								while (i < max && j < max)
								{
																if (tabla[j][i] != 0)
																{
																								return 0;
																}
																else
																{
																								i++;
																								j++;
																}
								}
								return 1;
}

int diagonal_s_arriba(int tabla[max][max], int x, int y)
{
								int i = x, j = y;
								while (j >= 0 && i < max)
								{
																if (tabla[j][i] != 0)
																{
																								return 0;
																}
																else
																{
																								i++;
																								j--;
																}
								}
								return 1;
}

int diagonal_s_abajo(int tabla[max][max], int x, int y)
{
								int i = x, j = y;
								while (j < max && i >= 0)
								{
																if (tabla[j][i] != 0)
																{
																								return 0;
																}
																else
																{
																								i--;
																								j++;
																}
								}
								return 1;
}

void retrocede(int tabla[max][max], int *y, int *x)
{
								(*x)--;
								*y = buscar_ficha(tabla, &x);
								tabla[*y][*x] = 0;
								(*y)++;
}

int buscar_ficha(int tabla[max][max], int **x)
{
								for (int aux = 0; aux < max; aux++)
								{
																if (tabla[aux][**x] != 0)
																{
																								return aux;
																}
								}
								return 0;
}
