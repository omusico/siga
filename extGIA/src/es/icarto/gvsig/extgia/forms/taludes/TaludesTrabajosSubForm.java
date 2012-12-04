package es.icarto.gvsig.extgia.forms.taludes;

import javax.swing.JTable;

import es.icarto.gvsig.extgia.forms.utils.AbstractSubForm;

@SuppressWarnings("serial")
public class TaludesTrabajosSubForm extends AbstractSubForm {

    public TaludesTrabajosSubForm(
	    String formFile,
	    String dbTableName,
	    JTable embebedTable,
	    String idElementField,
	    String idElementValue,
	    String idField,
	    String idValue,
	    boolean edit) {
	super(formFile, dbTableName, embebedTable, idElementField, idElementValue,idField, idValue,
		edit);
	// TODO Auto-generated constructor stub
    }

    @Override
    public String getXMLPath() {
	return this.getClass().getClassLoader().getResource("rules/extgia.xml")
		.getPath();
    }
}
