package es.icarto.gvsig.extgia.forms.senhalizacion_vertical;

import javax.swing.JTable;

import es.icarto.gvsig.extgia.forms.utils.AbstractSubForm;

@SuppressWarnings("serial")
public class SenhalizacionVerticalTrabajosSubForm extends AbstractSubForm {

    public SenhalizacionVerticalTrabajosSubForm(String formFile,
	    String dbTableName, JTable embebedTable, String idElementField,
	    String idElementValue, String idField, String idValue, boolean edit) {
	super(formFile, dbTableName, embebedTable, idElementField, idElementValue,
		idField, idValue, edit);
	// TODO Auto-generated constructor stub
    }

    @Override
    public String getXMLPath() {
	return this
		.getClass()
		.getClassLoader()
		.getResource(
			"rules/senhalizacion_vertical_trabajos_metadata.xml")
		.getPath();
    }


}
