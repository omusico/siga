package es.icarto.gvsig.extgia.forms.firme;

import javax.swing.JTable;

import com.iver.andami.ui.mdiManager.WindowInfo;

import es.icarto.gvsig.extgia.forms.utils.AbstractSubForm;

@SuppressWarnings("serial")
public class FirmeTrabajosSubForm extends AbstractSubForm {

    public FirmeTrabajosSubForm(String formFile, String dbTableName,
	    JTable embebedTable, String idElementField, String idElementValue,
	    String idField, String idValue, boolean edit) {
	super(formFile, dbTableName, embebedTable, idElementField, idElementValue,
		idField, idValue, edit);
	// TODO Auto-generated constructor stub
    }

    @Override
    public String getXMLPath() {
	return this.getClass().getClassLoader()
		.getResource("rules/firme_trabajos_metadata.xml")
		.getPath();
    }

    @Override
    public WindowInfo getWindowInfo() {
	viewInfo = new WindowInfo(WindowInfo.MODALDIALOG);
	viewInfo.setWidth(700);
	viewInfo.setHeight(800);
	return viewInfo;
    }

}
