package es.icarto.gvsig.audasacommons.forms.reports.imagefilechooser;

import java.io.File;

import javax.swing.JFileChooser;

public class ImageFileChooser extends JFileChooser {


    public ImageFileChooser() {

	this.addChoosableFileFilter(new ImageFilter());
	this.setAcceptAllFileFilterUsed(false);

	this.setAccessory(new ImagePreview(this));

    }

    public File showDialog() {
	File file = null;

	do {
	    int returnVal = this.showDialog(this, "Seleccionar");
	    if (returnVal == JFileChooser.CANCEL_OPTION) {
		break;
	    }

	    file = getSelectedFile();


	}while (file == null);

	return file;
    }

}
