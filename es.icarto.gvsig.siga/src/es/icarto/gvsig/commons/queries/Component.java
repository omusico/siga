package es.icarto.gvsig.commons.queries;

import java.io.File;

public interface Component {

    public boolean setOutputPath(File path);

    public void generateReportFile();

    public void finalActions();

}
