package es.icarto.gvsig.extgia.consultas;

import java.io.File;

public interface Component {

    public boolean setOutputPath(File path);

    public void generateReportFile();

    public void finalActions();

}
