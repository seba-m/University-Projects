# Proyecto Inventariado - Servidor - InventarisPro

## Descripción
Este es el servidor que se encarga de la gestión de los datos del proyecto de inventario.

## Cómo usar
Importe el proyecto en el IDE que desee. Para la realización de este proyecto se utilizó el IDE Eclipse.

Luego, busque el archivo `src/main/java/com/pruebas/` y ejecute el main contenido en el archivo `App.java`.

Si desea cambiar la configuración de la aplicación, debe modificar los valores de las propiedades `config.properties` en el archivo `src/main/resources/config.properties`, ahí está detallado que es cada valor y que necesita poner ahí, Los cuales por temas de privacidad fueron removidos estos valores. finalmente cambie el link de su bucket de aws s3 en el archivo `src\main\java\com\pruebas\modelo\usuario\UserServiceImpl.java`.

Con esto hecho, puede ejecutar el proyecto y ver que funciona.

PD: si desea ejecutar el proyecto fuera de un IDE puede hacerlo con el comando `mvn clean package spring-boot:repackage` en la raíz del proyecto.

