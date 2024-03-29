package com.hardcode.driverManager;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import java.security.AllPermission;
import java.security.CodeSource;
import java.security.PermissionCollection;
import java.security.Permissions;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.jar.JarException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;


/**
 * Class loader que carga las clases pedidas por los plugins de manera que
 * primero busca en el classpath, luego busca en el directorio del propio
 * plugin en los jars especificados por el xml y en caso de no encontrar la
 * clase pide al PluginClassLoaderManager la lista de plugins que pueden
 * satisfacer la clase e intenta cargarlo con cada un de ellos hasta que lo
 * consigue con uno.
 *
 * @author Fernando Gonz�lez Cort�s
 */
public class DriverClassLoader extends URLClassLoader {
    private Hashtable clasesJar = new Hashtable();
    private String baseDir;

    /**
     * Creates a new PluginClassLoader object.
     *
     * @param jars Array con la ruta de los jars en los que buscar� las clases
     *        el plugin
     * @param baseDir Directorio base del plugin que se carga. Es en directorio
     *        donde se buscan los resources en el m�todo getResources
     * @param cl ClassLoader padre del classLoader, al que se le pedir�
     *        resolver las clases antes de utilizar el algoritmo propio
     *
     * @throws IOException Si no se puede leer alguno de los jars
     * @throws IllegalArgumentException Si no se especifica un array de jars
     * @throws JarException Si hay dos clases con el mismo nombre en el plugin
     */
    public DriverClassLoader(URL[] jars, String baseDir, ClassLoader cl)
        throws IOException {
        super(jars, cl);
        this.baseDir = baseDir;

        if (jars == null) {
            throw new IllegalArgumentException("jars cannot be null"); //$NON-NLS-1$
        }

        //Se itera por las URLS que deben de ser jar's
        ZipFile[] jarFiles = new ZipFile[jars.length];

        for (int i = 0; i < jars.length; i++) {
            jarFiles[i] = new ZipFile(jars[i].getPath());

            Enumeration entradas = jarFiles[i].entries();

            //Se itera por todos los .class del jar
            while (entradas.hasMoreElements()) {
                //Se obtiene la entrada
                ZipEntry file = (ZipEntry) entradas.nextElement();
                String fileName = file.getName();

                //Se obtiene el nombre de la clase
                if (!fileName.toLowerCase().endsWith(".class")) { //$NON-NLS-1$

                    continue;
                }

                fileName = fileName.substring(0, fileName.length() - 6).replace('/',
                        '.');

                //Se cromprueba si ya hab�a una clase con dicho nombre
                if (clasesJar.get(fileName) != null) {
                    throw new JarException(
                        "two or more classes with the same name in the jars: " +
                        fileName);
                }

                //Se registra la clase
                clasesJar.put(fileName, jarFiles[i]);
                DriverClassLoaderManager.registerClass(fileName, this);
            }
        }
    }

    /**
     * Carga la clase
     *
     * @param name Nombre de la clase
     * @param resolve SI se ha de resolver la clase o no
     *
     * @return Clase cargada
     *
     * @throws ClassNotFoundException Si no se pudo encontrar la clase
     */
    protected Class loadClass(String name, boolean resolve)
        throws ClassNotFoundException {
    	Class c = null;

        try {
            c = super.loadClass(name, resolve);
        } catch (ClassNotFoundException e1) {
            if (c == null) {
                // Convert class name argument to filename
                // Convert package names into subdirectories
                try {
                    ZipFile jar = (ZipFile) clasesJar.get(name);

                    if (jar == null) {
                        Vector cls = (Vector) DriverClassLoaderManager.getClassLoaderList(name);

                        if (cls != null) {
                            for (int i = 0; i < cls.size(); i++) {
                                c = ((DriverClassLoader) cls.elementAt(i)).loadClass(name,
                                        resolve);

                                if (c != null) {
                                    break;
                                }
                            }
                        }
                    } else {
                        String fileName = name.replace('.', '/') + ".class"; //$NON-NLS-1$
                        ZipEntry classFile = jar.getEntry(fileName);
                        byte[] data = loadClassData(classFile,
                                jar.getInputStream(classFile));

                        c = defineClass(name, data, 0, data.length);
                    }

                    if (c == null) {
                        throw new ClassNotFoundException(name);
                    }
                } catch (IOException e) {
                    throw new ClassNotFoundException("Error_reading_file" +
                        name); //$NON-NLS-1$
                }
            }
        }

        if (resolve) {
            resolveClass(c);
        }

        return c;
    }

    /**
     * obtiene el array de bytes de la clase
     *
     * @param classFile Entrada dentro del jar contiene los bytecodes de la
     *        clase (el .class)
     * @param is InputStream para leer la entrada del jar
     *
     * @return Bytes de la clase
     *
     * @throws IOException Si no se puede obtener el .class del jar
     */
    private byte[] loadClassData(ZipEntry classFile, InputStream is)
        throws IOException {
        // Get size of class file
        int size = (int) classFile.getSize();

        // Reserve space to read
        byte[] buff = new byte[size];

        // Get stream to read from
        DataInputStream dis = new DataInputStream(is);

        // Read in data
        dis.readFully(buff);

        // close stream
        dis.close();

        // return data
        return buff;
    }

    /**
     * Obtiene los recursos tomando como la raiz el directorio base del plugin.
     * Si no se encuentra el recurso ah� se invoca a getResource del
     * classloader padre, que buscar� en el jar de la aplicaci�n. Si ah�
     * tampoco se encuentra nada se devolver� null.
     *
     * @param res Nombre del recurso
     *
     * @return URL del recurso o null si no se pudo encontrar
     */
    public URL getResource(String res) {
        File dir = new File(baseDir);

        try {
            ArrayList resource = new ArrayList();
            StringTokenizer st = new StringTokenizer(res, "\\/");

            while (st.hasMoreTokens()) {
                String token = st.nextToken();
                resource.add(token);
            }

            URL ret = getResource(dir, resource);

            if (ret != null) {
                return ret;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return super.getResource(res);
    }

    /**
     * Busca recursivamente el recurso res en el directorio base. res es una
     * lista de String's con los directorios del path y base es el directorio
     * a partir del cual se busca dicho recurso. En cada ejecuci�n del m�todo
     * se toma el primer elemento de res y se busca dicho directorio en el
     * directorio base. Si se encuentra, ser� el directorio base para una
     * nueva llamada.
     *
     * @param base Directorio desde donde parte la b�squeda del recurso.
     * @param res Lista de strings con el path del recurso que se quiere
     *        encontrar
     *
     * @return URL con el recurso
     */
    private URL getResource(File base, List res) {
        File[] files = base.listFiles();

        String parte = (String) res.get(0);

        for (int i = 0; i < files.length; i++) {
            if (files[i].getName().compareTo(parte) == 0) {
                if (res.size() == 1) {
                    try {
                        return new URL("file:" + files[i].toString());
                    } catch (MalformedURLException e) {
                        return null;
                    }
                } else {
                    return getResource(files[i], res.subList(1, res.size()));
                }
            }
        }

        return null;
    }

    /**
     * Devuelve el nombre del directorio del plugin
     *
     * @return
     */
    public String getPluginName() {
        String ret = baseDir.substring(baseDir.lastIndexOf(File.separatorChar) +
                1);

        return ret;
    }

    /**
     * @see java.security.SecureClassLoader#getPermissions(java.security.CodeSource)
     */
    protected PermissionCollection getPermissions(CodeSource codesource) {
        Permissions perms = new Permissions();
        perms.add(new AllPermission());

        return (perms);
    }

    /**
     * Devuelve una instancia de cada driver encontrado en los jars del
     * classloader
     *
     * @return array de drivers
     *
     * @throws ClassNotFoundException if the class cannot be located by the
     *         specified class loader
     */
    public Class[] getDrivers() throws ClassNotFoundException{
        ArrayList drivers = new ArrayList();
        Enumeration e = clasesJar.keys();

        while (e.hasMoreElements()) {
            String fileName = (String) e.nextElement();

            if (fileName.endsWith("Driver")) {
                Class driver = (Class) this.loadClass(fileName);
                drivers.add(driver);
            }
        }

        return (Class[]) drivers.toArray(new Class[0]);
    }
    /**
     * Devuelve una instancia de cada writer encontrado en los jars del
     * classloader
     *
     * @return array de drivers
     *
     * @throws ClassNotFoundException if the class cannot be located by the
     *         specified class loader
     */
    public Class[] getWriters() throws ClassNotFoundException{
        ArrayList writers = new ArrayList();
        Enumeration e = clasesJar.keys();

        while (e.hasMoreElements()) {
            String fileName = (String) e.nextElement();

            if (fileName.endsWith("Writer")) {
                Class driver = (Class) this.loadClass(fileName);
                writers.add(driver);
            }
        }

        return (Class[]) writers.toArray(new Class[0]);
    }
}
