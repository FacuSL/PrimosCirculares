Primos Circulares menores a 1 millón
===========

Aplicación realizada en lenguaje JAVA que permite calcular los primos circulares menor a un un millón.
En el repositorio se encuentran dos carpetas:

**[jar][]:** que contiene el programa ejecutable

**[proyecto][]:** que contiene el proyecto entero. Dentro de esta carpeta, se encuentra una carpeta src que contiene el código del programa.



Cómo ejecutar
-------------

Descargar el archivo .jar que se encuentra dentro de la carpeta [jar][]. Luego, por medio de la consola, dirigirse a la ruta donde se descargó el mismo y escribir el siguiente comando:
java -jar PrimosCirculares.jar

El programa además tiene la opción de aceptar dos parámetros:
* El primero determina la cantidad de hilos para calcular los primos; por defecto tiene 4.
* El segundo determina la forma en la que se debe mostrar el resultado. Si dice "agrupado", la lista de primos circulares se mostrará separada por dígitos, agrupando los números que son rotaciones entre si. Si no dice nada, se muestra como lista secuencial ordenada.

Entonces, si por ejemplo se quiere ejecutar con 2 hilos y mostrar el resultado agrupado por dígitos y rotaciones, se debería escribir:

java -jar PrimosCirculares.jar 2 agrupado

Si, en cambio, se quiere ejecutar con un solo hilo y mostrando el resultado de forma secuencial, se debería escribir:

java -jar PrimosCirculares.jar 1



Respecto de los hilos
---------------------

Se debe tener en cuenta que colocar muchos hilos para que calcule los primos circulares produce resultados negativos en la performance, ya que las áreas sincronizadas del programa bloquean la ejecución.
De hecho, el tiempo de ejecución es bastante chico, y en ocasiones no se ven resultados significativos con la utilización de hilos.

Se recomienda no utilizar más de 4 hilos.


[jar]: https://github.com/FacundoCasares/PrimosCirculares/tree/master/jar
[proyecto]: https://github.com/FacundoCasares/PrimosCirculares/tree/master/proyecto