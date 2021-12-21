# Proyecto Inventariado - InventarisPro
Este es un proyecto realizado en la carrera Ingenieria en Computación en la Universidad de La serena, para la materia de Programación avanzada. Programado en c#, este es el servidor que se encarga de la gestión de los datos del proyecto de inventario.

## Desarrolladores
-	Sebastian Morgado
-	Antony Rodriguez
-	Roberto Contreras

## Cómo usar
Importe el proyecto en el IDE que desee. Para la realización de este proyecto se utilizó el IDE Visual Studio 2022 preview.

Luego, busque el archivo `InventarisPro.sln` y ejecute programa.

Si desea cambiar la configuración de la aplicación, debe modificar los valores de las propiedades en el archivo `appsettings.json` (también en el documento `services\ServicesAWSS3.cs`), ahí está detallado que es cada valor y que necesita poner ahí, Los cuales por temas de privacidad fueron removidos estos valores.

Con esto hecho, puede ejecutar el proyecto y ver que funciona.

## Consideraciones

-   El servidor de la base de datos usado fue AWS RDS y PostgreSql.
-   Se usa AWS S3 para almacenar las fotos.
-   Es un proyecto de C#, usando asp.net core mvc 5 y compilador .net 6
