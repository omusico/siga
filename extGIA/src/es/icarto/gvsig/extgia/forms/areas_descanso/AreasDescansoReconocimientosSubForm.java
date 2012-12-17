package es.icarto.gvsig.extgia.forms.areas_descanso;

import javax.swing.JTable;

import es.icarto.gvsig.extgia.forms.utils.AbstractSubForm;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;

@SuppressWarnings("serial")
public class AreasDescansoReconocimientosSubForm extends AbstractSubForm {

    public AreasDescansoReconocimientosSubForm(String formFile,
	    String dbTableName, JTable embebedTable, String idElementField,
	    String idElementValue, String idField, String idValue, boolean edit) {
	super(formFile, dbTableName, embebedTable, idElementField, idElementValue,
		idField, idValue, edit);

	AreasDescansoCalculateIndiceEstado index = new AreasDescansoCalculateIndiceEstado(
		this,
		this.getWidgetsVector(),
		DBFieldNames.AREA_DESCANSO_INDEX,
		DBFieldNames.AREA_DESCANSO_A,
		DBFieldNames.AREA_DESCANSO_B,
		DBFieldNames.AREA_DESCANSO_C);
	index.setListeners();
    }

    @Override
    public String getXMLPath() {
	return this.getClass().getClassLoader().getResource("rules/extgia.xml")
		.getPath();
    }

}
