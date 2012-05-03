package es.icarto.gvsig.extgex.navtable.decorators.fileslink;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import es.udc.cartolab.gvsig.navtable.AbstractNavTable;

public class FilesLinkObserver implements ActionListener {

    private AbstractNavTable dialog = null;
    private FilesLinkData data = null;

    public FilesLinkObserver(AbstractNavTable dialog, FilesLinkData data) {
	this.dialog = dialog;
	this.data = data;
    }

    public void actionPerformed(ActionEvent arg0) {
	FilesLinkAction fl = new FilesLinkAction(dialog, data);
	fl.showFiles((int) dialog.getPosition());
    }

}
