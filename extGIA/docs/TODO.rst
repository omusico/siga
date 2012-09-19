====
TODO
====


Genéricos
=========
* Si la capa está en edición y cierra el formulario con los datos mal, debe salir un mensaje diciendo que en ese estado no va a poder salvar la capa sin errores. Esto es especialmente importante cuando se creen nuevos elementos.

Taludes
========
* numero talud. Hay que automatizar que el valor del campo se genere automáticamente cuando se cree un nuevo talud. La opción que se me ocurre es subscribirse a los eventos que lanza opencadtools al crear una nueva geometría, comprobar si la capa que está siendo modificada es la de taludes, preguntar a la base de datos cual sería el siguiente número a introducir, curr_val(secuencia), e insertarlo en el formulario que se abra.

* numero talud. Comprobar que se trata de un número positivo de tres digitos, es decir está entre 0 y 999. En este momento las validaciones no son parametrizables, es decir, no tenemos una validación del tipo number_between_values(x,y). Eso sería lo ideal.

* pk_inicial, pk_final. Requiere de un formateado específico. Hay que revisarlo con Antón.
