package es.icarto.gvsig.commons.gui;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.iver.andami.PluginServices;

@SuppressWarnings("serial")
public class ExcelFileChooser extends JFileChooser {

    private final String description = PluginServices.getText(this,
	    "excel_files");
    private final String[] extensions = new String[] { "xls", "xlsx" };

    public ExcelFileChooser(File folder) {
	super(folder);
	FileNameExtensionFilter filter = new FileNameExtensionFilter(
		description, extensions);
	setFileFilter(filter);
	setAcceptAllFileFilterUsed(false);
    }

    public File showDialog() {
	File file = null;

	do {
	    int returnVal = this.showDialog(this, "Seleccionar");
	    if (returnVal == JFileChooser.CANCEL_OPTION) {
		break;
	    }

	    file = getSelectedFile();

	} while (file == null);

	return file;
    }

}
