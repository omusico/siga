package es.icarto.gvsig.extgia.forms.taludes;

import javax.swing.JTable;

import es.icarto.gvsig.extgia.forms.utils.AbstractSubForm;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;

@SuppressWarnings("serial")
public class TaludesReconocimientosSubForm extends AbstractSubForm {

    public TaludesReconocimientosSubForm(
	    String formFile,
	    String dbTableName,
	    JTable embebedTable,
	    String idElementField,
	    String idElementValue,
	    String idField,
	    String idValue,
	    boolean edit) {
	super(formFile, dbTableName, embebedTable, idElementField, idElementValue, idField, idValue,
		edit);

	CalculateIndiceEstado index = new CalculateIndiceEstado(
		this,
		this.getWidgetsVector(),
		DBFieldNames.TALUDES_INDEX,
		DBFieldNames.TALUDES_A,
		DBFieldNames.TALUDES_B,
		DBFieldNames.TALUDES_C,
		DBFieldNames.TALUDES_D);
	index.setListeners();
    }

    @Override
    public String getXMLPath() {
	return this.getClass().getClassLoader().getResource("rules/extgia.xml")
		.getPath();

    }
}
