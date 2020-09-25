[![Work in Repl.it](https://classroom.github.com/assets/work-in-replit-14baed9a392b3a25080506f3b7b6d57f295ec2978f6f33ec97e36a161684cbe9.svg)](https://classroom.github.com/online_ide?assignment_repo_id=272915&assignment_repo_type=GroupAssignmentRepo)
# ¿Quién ha hecho qué?

Antony Rodriguez = Ha creado el árbol de Huffman y la tabla de resultados.

Sebastian Morgado = Ha creado la compresión.

Roberto Contreras = Ha creado la descompresión y la tabla de resultados.

# Compresión de Huffman

La compresión de Huffman, sirve para (tal como dice su nombre), comprimir archivos, la forma en que crea la tabla puede verse explicada en el siguiente video (haga click en la imagen).

[![](https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQbOGvrqlCsSLfeNyTPWAzFkE2NCh7H5ovHgw&usqp=CAU)](http://www.youtube.com/watch?v=8Gf8wutvS1w "paso a paso de huffman")

# Como usar nuestro algoritmo de huffman

  - primero, con todos los archivos descargados, compile el main.c, con el siguiente comando:
  ```sh
    gcc huffman.c -argumento NombreArchivoEntrada NombreArchivoSalida

    siendo argumento:
    -c / -comprimir : para comprimir el ArchivoEntrada
    -d / -descomprimir : para descomprimir el ArchivoEntrada
    -e / -experimento : mediante un archivo dado, toma 1/10 sucesivamente (1/10,2/10,etc.) de cada archivo, para luego comprimirlos. en este caso, solo necesita un archivo de entrada, ya que generara automaticamente los archivos de salida.
    -a / -ayuda : para un mensaje de ayuda

    siendo NombreArchivoEntrada: nombre del archivo en el cual se desea trabajar. este debe estar en la misma carpeta que el archivo c.

    siendo NombreArchivoSalida : nombre del archivo en el cual se desea dejar el resultado. este se ubicara en la misma carpeta que el archivo c.
```
  - ya con eso, estaria listo, dado a que los archivos de salida se ubicaran en la misma carpeta del main.c

# Tabla con los resultados: 

![alt text](https://github.com/edd-ic/taller-2-grupo2/blob/master/fotos/TABLA.png?raw=true)

# Gráfico costo:

![alt text](https://github.com/edd-ic/taller-2-grupo2/blob/master/fotos/GRAFICO_costo.png?raw=true)

# Gráfico peso original vs peso comprimido:

![alt text](https://github.com/edd-ic/taller-2-grupo2/blob/master/fotos/GRAFICO_pesoO_vs_pesoC.png?raw=true)

# Gráfico de la tasa de compresión:

![alt text](https://github.com/edd-ic/taller-2-grupo2/blob/master/fotos/GRAFICO_tasa_de_compresion.png?raw=true)
