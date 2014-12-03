package es.icarto.gvsig.audasacommons;

import java.io.File;
import java.net.URL;

import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;
import com.iver.cit.gvsig.project.documents.view.gui.View;

import es.icarto.gvsig.commons.gui.ExcelFileChooser;

public class ImportIncidenciasExtension extends Extension {

    public final static String KEY = "import-incidencias";

    // tracks the last folder selected by the user in this session
    private File folder = new File(System.getProperty("user.home"));

    @Override
    public void initialize() {
	registerIcon();
    }

    private void registerIcon() {
	URL iconResource = getClass().getClassLoader().getResource(
		"images/import-incidencias.png");
	PluginServices.getIconTheme().registerDefault(KEY, iconResource);
    }

    @Override
    public void execute(String actionCommand) {
	ExcelFileChooser excelFileChooser = new ExcelFileChooser(folder);
	File file = excelFileChooser.showDialog();
	if ((file != null) && (file.exists())) {
	    folder = file.getParentFile();
	    System.out.println(folder);
	    System.out.println(file);
	}
    }

    private String getNameWithOutExtension(File file) {
	int endIndex = file.getName().lastIndexOf(".");
	String fileNameWithOutExtension = file.getName().substring(0, endIndex);
	return fileNameWithOutExtension;
    }

    @Override
    public boolean isEnabled() {
	return PluginServices.getMDIManager().getActiveWindow() instanceof View;
    }

    @Override
    public boolean isVisible() {
	return true;
    }

}
