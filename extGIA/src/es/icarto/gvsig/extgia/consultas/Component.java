package es.icarto.gvsig.extgia.consultas;

import java.io.File;

public interface Component {

    public boolean setOutputPath(File path);
    public void finalActions();
    
    // Da la orden de lanzar la query contra la base de datos y guardar el resultado en el propio component
    // devuelve falso si no hay resultados que coincidan con la búsqueda
    public boolean lookUp();
    
    // Genera el informe. Se debe haber hecho antes un lookUp 
    public void generateReportFile();
    
}
