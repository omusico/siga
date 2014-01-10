package es.icarto.gvsig.extgia.forms.areas_mantenimiento;

import javax.swing.JTable;

import es.icarto.gvsig.extgia.forms.utils.AbstractSubForm;

@SuppressWarnings("serial")
public class AreasMantenimientoRamalesSubForm extends AbstractSubForm {

    public AreasMantenimientoRamalesSubForm(String formFile,
	    String dbTableName, JTable embebedTable, String idElementField,
	    String idElementValue, String idField, String idValue, boolean edit) {
	super(formFile, dbTableName, embebedTable, idElementField, idElementValue,
		idField, idValue, edit);
    }

    @Override
    public String getXMLPath() {
	return this.getClass().getClassLoader()
		.getResource("rules/areas_mantenimiento_ramales_metadata.xml")
		.getPath();
    }

}
