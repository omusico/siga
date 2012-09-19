================================
Construcción del Modelo de datos
================================
* Los nombres bbdd siempre en minúsculas, sin caracteres extraños, empezando por una letra, sin espacios y sin tildes
* No puede haber dos nombres bbdd iguales en la misma capa, ni en el mismo formulario

==================================
Implementación del Modelo de datos
==================================

Dominios
========
* Los dominios se introducen en un esquema propio de dominios del plugin que se esté desarrollando
* El DDL de las tablas de dominio tendrá dos formas:

CREATE TABLE *NombreDominio* (
       item character varying(25) PRIMARY KEY
);


CREATE TABLE *NombreDominio* (
       id integer PRIMARY KEY,
       item character varying(25)
);

Usaremos la de una sóla columna cuando en el modelo de datos no hayamos especificado que tiene un código númerico asociado.

La idea de usar siempre los mismos nombres para la columna (id, item) en lugar de otros más significativos, como *id_talud* y *tipo_talud* por ejemplo, se basa que en caso de necesitar hacer scripts, substituciones masivas u otras tareas de mantenimiento la operación será más sencilla. La perdida de significado no parece ser tanta además.


Otros
=====
* En aquellos casos en que por formato se haya fijado el número de decimales emplear el tipo numeric

* Ademas de emplear validaciones en los formularios vamos a emplear las constraints en la base de datos para gestionar la integridad de los datos, usando:
  + CHECK para validaciones específicas
  + FOREIGN KEY para dominios
  + NOT NULL para campos obligatorios

**Nota:** He dejado esta regla en suspenso al menos por ahora, en la mayoría de los casos porque tuve algunos problemas a la hora de cargar datos, ...




=======================
Formularios con Abeille
=======================
* Los formularios deben guardarse siempre en xml. Para que haga esto por defecto Tools -> Preferences -> Project -> Store Forms as XML. La excepción a esta regla es cuando trabajemos con widgets externos como JCalendar, en este caso debemos guardarlos en formato binario (.jfrm)

* El grid del formulario debe tener siempre el menor número de filas y columnas posibles. Esto disminuye el tamaño del fichero, se parsea más rápido, probablemente redimensione mejor... En la medida de los posible es mejor hacer columnas grandes a expandir un widget entre varias columnas

* La columna tooltip del modelo de datos se fija modificando la propiedad *toolTipText* del propio widget.

* En los JTextArea activar siempre las propiedades: *lineWrap* y *wrapStyleWord*


=============
Entrega final
=============
* Revisar duplicidades de las tablas de dominios y pensar como hacer para que el plugin sea usable de forma independiente. Un esquema de dominios por plugin aunque haya duplicidades, un esquema de dominios común y arrastrarlo entero (o en parte) si el módulo se quiere usar por separado.


