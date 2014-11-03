package es.icarto.gvsig.extgia.forms.obras_desague;

import javax.swing.JTable;

import es.icarto.gvsig.extgia.forms.utils.BasicAbstractSubForm;

@SuppressWarnings("serial")
public class ObrasDesagueTrabajosSubForm extends BasicAbstractSubForm {

    public ObrasDesagueTrabajosSubForm(String formFile, String dbTableName,
	    JTable embebedTable, String idElementField, String idElementValue,
	    String idField, String idValue, boolean edit) {
	super(formFile, dbTableName, embebedTable, idElementField, idElementValue,
		idField, idValue, edit);
	// TODO Auto-generated constructor stub
    }

    @Override
    public String getXMLPath() {
	return this.getClass().getClassLoader()
		.getResource("rules/obras_desague_trabajos_metadata.xml")
		.getPath();
    }

}
