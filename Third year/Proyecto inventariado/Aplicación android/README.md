# Proyecto Inventariado - Aplicación Android - InventarisPro

## Descripción
Esta aplicación necesita como mínimo Android 5.0 (o sdk 21).

## Cómo usar
Importe el proyecto, y luego ejecutelo desde el IDE. No necesita instalar ningún paquete de Android.

Si desea cambiar la configuración de la aplicación, debe modificar los strings de los siguientes archivos:

| Archivo | Strings a modificar | Razón |
|---|---|---|
| app/src/main/res/values/strings.xml  | <ul><li>serverURL</li><li>productPhotosURL</li></ul>|<ul><li>Es el link en donde está hosteado el servidor.</li><li>Es el link de AWS S3, donde se obtendrán las fotos.</li></ul> |
| app/src/main/java/com/seba/inventariado/utils/ApiClient.java  | API_BASE_URL | Es el link en donde está hosteado el servidor. |

Pd: Si modifica estos links, asegurese que antes funcionen correctamente.
