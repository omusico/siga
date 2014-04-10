Kit de herramientas desarollado por [Cartolab](http://cartolab.udc.es) para crear versiones personalizadas y portables de gvSIG 1.x

Este proyecto es básicamente un script de Ant que permite crear versiones portable de gvSIG para Windows y Linux.

# Requisitos

* Configurar un workspace válido con los proyectos que quieras incluir en tu versión
* Incluír en el workspace el directorio **install** que está en la ráiz del repositorio de gvSIG
* Descargar y descomprimir en /var/tmp el [fichero](http://www.adrive.com/public/Y4awtk/portable.zip) que contiene la máquina virtual de java y algunos ficheros adicionales.
* El proceso debe ejecutarse en una máquina con Linux
* Añadir al workspace el proyecto **create-gvsig-portable**, o copiar el contenido, script **deploy.xml** y directorio **portable** a tu propio proyecto

# Como construir la versión portable

* Editar el fichero **portable/projects.xml**. Eliminar de este proyecto los plugins que no nos interesen, y añadir los que queramos
* Editar las properties *this-folder*, *base-path*, *custom-portable-path*, *name-lin, *name-win* con los valores que queramos
* Ejectuar el target **createPortables** del script **deploy.xml**

Esto generará un .zip y un .tgz en el directorio /tmp que contendrá el gvSIG portable.


# Personalizaciones

Ademas de tocar el fichero proyect.xml para decidir que plugins añadir en tu versión se puede:

* Incluir ficheros adicionales añadiéndolos con la estructura de directorios completa bajo el directorio **portable**. Mira por ejemplo como se modifican el config.xml de extAnnotation creando un nuevo fichero en create-gvsig-portable/portable/common/bin/gvSIG/extensiones/com.iver.cit.gvsig.annotation/config.xml

* Los ficheros de configuración van el directorio **cfg**. Puedes acceder a este directorio por código llamando a Launcher.getAppHomeDir()

# Crear un entorno de desarrollo

Para configurar un entorno de desarrollo se puede ejecutar el target **createTestEnviroment** del fichero **deploy.xml**. Esto limpiará andami y comilará todos los proyectos que le hayamos indicado, además de sobreescribir los ficheros que hayamos definido bajo el directorio **portable**
